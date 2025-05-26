package com.example.warehouse_accounting.ui.buyers

import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.ServerController.Service.nya

object ServiceLocator {
    val nyaService: nya by lazy {
        nya(poka_tak.getInstance())
    }
}
