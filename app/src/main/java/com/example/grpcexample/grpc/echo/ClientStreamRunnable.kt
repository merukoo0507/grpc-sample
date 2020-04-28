package com.example.grpcexample.grpc.echo

import android.os.Handler
import com.example.grpcexample.grpc.*
import io.grpc.stub.StreamObserver
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch

class ClientStreamRunnable:
    GrpcRunnable {
    lateinit var activityReference: WeakReference<EchoActivity>
    var countDownLatch = CountDownLatch(1)
    var logs = StringBuffer("Res: ")
    var handler = Handler()
    lateinit var requestObserver: StreamObserver<EchoRequest>
//    lateinit var inputRunnable: InputRunnable

    override fun run(blockingStub: EchoGrpc.EchoBlockingStub, asyncStub: EchoGrpc.EchoStub, activityReference: WeakReference<EchoActivity>): String {
        this.activityReference = activityReference
        return serverStream("Client Stream Test.", asyncStub)
    }

    fun serverStream(msg: String, asyncStub: EchoGrpc.EchoStub): String {
        requestObserver = asyncStub.clientStreamingEcho(
            object : StreamObserver<EchoResponse> {
                override fun onNext(value: EchoResponse) {
                    Timber.d("onNext: ${value?.message}")
                    logs.insert(4,"${value?.message}")
                    setLogs()
                }

                override fun onError(t: Throwable?) {
                    Timber.d("onError: ${t?.message}")
                    logs.append("onError: ${t?.message} \n")
                    setLogs()

//                    inputRunnable.interrupt()
                    countDownLatch.countDown()
                }

                override fun onCompleted() {
                    Timber.d("onCompleted")
                    logs.append("onCompleted. \n")
                    setLogs()
                    countDownLatch.countDown()
                }
            }
        )

        requestObserver.onNext(
            EchoUtil.newRequest(
                "test1"
            )
        )
        requestObserver.onNext(
            EchoUtil.newRequest(
                "test2"
            )
        )

//        inputRunnable =
//            InputRunnable(requestObserver)
//        inputRunnable.start()
//        inputRunnable.join()

        countDownLatch.await()
        return logs.toString()
    }

    fun setLogs() {
        if (activityReference.get() != null) {
            var activity: EchoActivity = activityReference.get()!!
            handler.post{
                activity.setResultText(logs.toString())
            }
        }
    }
}