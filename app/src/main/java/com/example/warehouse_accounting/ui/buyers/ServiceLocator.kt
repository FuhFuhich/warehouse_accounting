package com.example.warehouse_accounting.ui.buyers

import com.example.warehouse_accounting.ServerController.Service.nya

object ServiceLocator {
    val nyaService: nya by lazy {
        val ws = com.example.warehouse_accounting.ServerController.WebSocketConnection("ws://your_server_url")
        val repo = com.example.warehouse_accounting.ServerController.Repositories.poka_tak(ws)
        nya(repo)
    }
}
