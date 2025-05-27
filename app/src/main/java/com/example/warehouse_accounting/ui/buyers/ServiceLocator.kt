package com.example.warehouse_accounting.ui.buyers

import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.ServerController.Service.Serv

object ServiceLocator {
    val nyaService: Serv by lazy {
        Serv(poka_tak.getInstance())
    }
}
