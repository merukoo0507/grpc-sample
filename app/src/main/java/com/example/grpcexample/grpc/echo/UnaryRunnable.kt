package com.example.grpcexample.grpc.echo

import com.example.grpcexample.grpc.*
import io.grpc.Status
import java.lang.ref.WeakReference

class UnaryRunnable: GrpcRunnable {
    override fun run(blockingStub: EchoGrpc.EchoBlockingStub, asyncStub: EchoGrpc.EchoStub, activityReference: WeakReference<EchoActivity>): String {
        return unary("Grpc Test", blockingStub)
    }

    fun unary(msg: String, blockingStub: EchoGrpc.EchoBlockingStub): String {
        var logs = StringBuffer()
        try {
            var request = EchoRequest.newBuilder()
                .setMessage(msg).build()
            var response = blockingStub.unaryEcho(request)

            if (EchoUtil.exists(response)) {
                logs.append("unary: " + response.message)
            } else {
                logs.append("Fail to response.")
            }
        } catch (e: Exception) {
            val status = Status.fromThrowable(e)
            logs.append("Exception: ${status.code} ${status.description}")
        }
        return logs.toString()
    }
}