package com.example.grpcexample.grpc.echo

import com.example.grpcexample.grpc.EchoRequest
import com.example.grpcexample.grpc.EchoResponse

object EchoUtil {
    fun exists(response: EchoResponse): Boolean {
        return response != null && response.message.isNotEmpty()
    }

    fun newRequest(msg: String): EchoRequest {
        return EchoRequest.newBuilder().setMessage(msg).build()
    }
}