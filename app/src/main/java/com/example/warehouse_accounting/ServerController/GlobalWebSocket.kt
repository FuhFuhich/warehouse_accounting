package com.example.warehouse_accounting.ServerController

// Singleton
object GlobalWebSocket {
    val instance = WebSocketConnection("ws://192.168.8.104:5400")
}
