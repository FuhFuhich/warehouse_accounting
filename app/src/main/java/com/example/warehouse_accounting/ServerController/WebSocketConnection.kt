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

    fun connect() {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket подключён")
                webSocket.send("")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Получено сообщение: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Получены бинарные данные: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket закрывается: $code $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket закрыт: $code $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Ошибка WebSocket: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Закрыто клиентом")
    }
}