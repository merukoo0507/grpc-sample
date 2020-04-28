package com.example.grpcexample.grpc.echo

import android.os.AsyncTask
import com.example.grpcexample.grpc.EchoGrpc
import io.grpc.ManagedChannel
import java.lang.ref.WeakReference

class GrpcTask(
    val grpcRunnable: GrpcRunnable,
    val channnel: ManagedChannel,
    val activityReference: WeakReference<EchoActivity>
) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String {
        return try {
            grpcRunnable.run(
                EchoGrpc.newBlockingStub(
                    channnel
                ),
                EchoGrpc.newStub(channnel), activityReference)
        } catch (e: Exception) {
            "Fail: " + e.printStackTrace().toString()
        }
    }

    override fun onPostExecute(result: String) {
        if (activityReference.get() != null) {
            var activity: EchoActivity = activityReference.get()!!
            activity.setResultText("$result\n ---End")
            activity.enableButtons()
        }
    }

}