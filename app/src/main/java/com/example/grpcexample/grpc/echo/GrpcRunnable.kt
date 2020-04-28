package com.example.grpcexample.grpc.echo

import com.example.grpcexample.grpc.EchoGrpc
import java.lang.ref.WeakReference

interface GrpcRunnable {
    fun run(blockingStub: EchoGrpc.EchoBlockingStub, asyncStub: EchoGrpc.EchoStub, activityReference: WeakReference<EchoActivity>): String
}