package com.example.warehouse_accounting.ServerController

// Singleton
object GlobalWebSocket {
    val instance = WebSocketConnection("ws://95.165.27.159:5405")
}
