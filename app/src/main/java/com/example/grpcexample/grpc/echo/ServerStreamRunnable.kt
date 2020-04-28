package com.example.grpcexample.grpc.echo

import com.example.grpcexample.grpc.EchoGrpc
import com.example.grpcexample.grpc.EchoRequest
import io.grpc.Status
import java.lang.ref.WeakReference

class ServerStreamRunnable: GrpcRunnable {
    override fun run(blockingStub: EchoGrpc.EchoBlockingStub, asyncStub: EchoGrpc.EchoStub, activityReference: WeakReference<EchoActivity>): String {
        return serverStream("Server Stream Test", blockingStub)
    }

    fun serverStream(msg: String, blockingStub: EchoGrpc.EchoBlockingStub): String {
        var logs = StringBuffer()
        try {
            var request = EchoRequest.newBuilder()
                .setMessage(msg).build()
            var response = blockingStub.serverStreamingEcho(request)

            if (response.hasNext()) {
                logs.append("serverStream: ")
                while (response.hasNext()) {
                    logs.append(response.next().message + " \n")
                }
            } else {
                logs.append("Fail to response. \n")
            }
        } catch (e: Exception) {
            val status = Status.fromThrowable(e)
            logs.append("Exception: ${status.code} ${status.description}")
        }
        return logs.toString()
    }
}