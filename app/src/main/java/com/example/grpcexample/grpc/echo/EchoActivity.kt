package com.example.grpcexample.grpc.echo

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.grpcexample.R
import com.example.grpcexample.grpc.echo.EchoUtil.newRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.android.synthetic.main.activity_echo.*
import java.lang.ref.WeakReference

class EchoActivity : AppCompatActivity() {
    private lateinit var channel: ManagedChannel
    private lateinit var bidirectRunnable: BidirectionalStreamRunnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_echo)

        disableButtons()
    }

    fun startEcho(view: View?) {
        var host: String = host_edit_text.text.toString()
        var portStr: String = port_edit_text.text.toString()
        if (host.isEmpty()) {
            host = "10.20.70.43"
        }
        if (portStr.isEmpty()) {
            portStr = "8443"
        }
        val port = if (TextUtils.isEmpty(portStr)) 0 else Integer.valueOf(portStr)

        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(host_edit_text.windowToken, 0)
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        host_edit_text.isEnabled = false
        port_edit_text.isEnabled = false
        start_button.isEnabled = false
        enableButtons()
    }

    fun exitEcho(view: View?) {
        channel!!.shutdown()
        disableButtons()
        host_edit_text.isEnabled = true
        port_edit_text.isEnabled = true
        start_button.isEnabled = true
    }

    fun unaryEcho(view: View?) {
        setResultText("")
        disableButtons()
        GrpcTask(
            UnaryRunnable(),
            channel,
            WeakReference(this)
        ).execute()
    }

    fun serverStreamingEcho(view: View?) {
        setResultText("")
        disableButtons()
        GrpcTask(
            ServerStreamRunnable(),
            channel,
            WeakReference(this)
        ).execute()
    }

    fun clientStreamingEcho(view: View?) {
        setResultText("")
        disableButtons()
        GrpcTask(
            ClientStreamRunnable(),
            channel,
            WeakReference(this)
        ).execute()
    }

    fun bidirectionalStreamingEcho(view: View?) {
        setResultText("")
        disableButtons()
        send_stream.isEnabled = true
        end_stream.isEnabled = true
        bidirectRunnable =
            BidirectionalStreamRunnable()
        GrpcTask(
            bidirectRunnable,
            channel,
            WeakReference(this)
        ).execute()
    }

    fun sendStream(view: View?) {
        bidirectRunnable.requestObserver.onNext(newRequest(input_stream.text.toString()))
        input_stream.text.clear()
    }

    fun endStream(view: View?) {
        bidirectRunnable.inputRunnable.interrupt()
    }

    fun setResultText(text: String) {
        result_text.text = text
    }

    private fun disableButtons() {
        unary_button.isEnabled = false
        server_stream_button.isEnabled = false
        client_stream_button.isEnabled = false
        bidirectional_stream_button.isEnabled = false
        exit_button.isEnabled = false
        send_stream.isEnabled = false
        end_stream.isEnabled = false
    }

    fun enableButtons() {
        exit_button.isEnabled = true
        unary_button.isEnabled = true
        server_stream_button.isEnabled = true
        client_stream_button.isEnabled = true
        bidirectional_stream_button.isEnabled = true
        send_stream.isEnabled = false
        end_stream.isEnabled = false
    }
}
