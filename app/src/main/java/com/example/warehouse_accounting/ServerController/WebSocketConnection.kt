package com.example.warehouse_accounting.ServerController

import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class WebSocketConnection(private val url: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val messageQueue = mutableListOf<String>()

    var onTextMessage: ((String) -> Unit)? = null

    fun connect() {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("Websocket is connected")
                synchronized(messageQueue) {
                    for (msg in messageQueue) {
                        val sent = webSocket.send(msg)
                        println("send from queue: $msg, result: $sent")
                    }
                    messageQueue.clear()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("=== MESSAGE WILL BE COME ===: $text")
                println("Raw message: '$text'")
                println("Length: ${text.length}")
                onTextMessage?.invoke(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("binary message will be come: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket is closing: $code $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket is closed: $code $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("fail WebSocket: ${t.message}")
            }
        })
        println("WebSocket object will be created: $webSocket")
    }

    fun sendMessage(message: String) {
        println("I'm trying to send a message: $message")
        if (webSocket != null) {
            val result = webSocket!!.send(message)
            println("Sending result: $result")
        } else {
            println("WebSocket is not ready, message is queued: $message")
            synchronized(messageQueue) {
                messageQueue.add(message)
            }
        }
    }

    fun close() {
        webSocket?.close(1000, "Закрыто клиентом")
    }
}
