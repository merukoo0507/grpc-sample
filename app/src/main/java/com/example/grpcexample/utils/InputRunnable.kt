package com.example.grpcexample.utils

import com.example.grpcexample.grpc.EchoRequest
import com.example.grpcexample.grpc.echo.EchoUtil
import io.grpc.stub.StreamObserver
import timber.log.Timber

class InputRunnable(private var requestObserver: StreamObserver<EchoRequest>): Thread() {
    override fun run() {
        try {
            while (true) {
                requestObserver.onNext(
                    EchoUtil.newRequest(
                        "."
                    )
                )
                sleep(1000L)
            }
        } catch (e: Exception) {
            Timber.d("InputRunnable Exception: ${e.message}")
        }
    }
}