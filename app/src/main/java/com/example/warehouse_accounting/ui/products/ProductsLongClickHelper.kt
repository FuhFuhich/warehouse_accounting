package com.example.warehouse_accounting.ui.products

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ProductsLongClickHelper(private val context: Context) {

    fun showOptionsDialog(onOptionSelected: (String) -> Unit) {
        val options = arrayOf("Наличие на складах", "Движение товаров", "Удалить")
        val dialog = AlertDialog.Builder(context)
            .setTitle("Выберите действие")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> onOptionSelected("Наличие на складах")
                    1 -> onOptionSelected("Движение товаров")
                    2 -> onOptionSelected("Удалить")
                }
            }
            .create()
        dialog.show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
