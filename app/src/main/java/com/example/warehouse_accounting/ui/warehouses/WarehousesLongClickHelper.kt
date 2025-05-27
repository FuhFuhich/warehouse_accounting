package com.example.warehouse_accounting.ui.warehouses

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Warehouses

class WarehousesLongClickHelper(
    private val context: Context
) {
    fun showOptionsDialog(
        warehouse: Warehouses,
        onEdit: () -> Unit,
        onDelete: () -> Unit
    ) {
        val options = arrayOf("Удалить")

        AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Выберите действие")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showDeleteConfirmDialog(warehouse, onDelete)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showDeleteConfirmDialog(warehouse: Warehouses, onDelete: () -> Unit) {
        AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Удалить склад")
            .setMessage("Вы уверены, что хотите удалить склад \"${warehouse.warehousesName}\"?")
            .setPositiveButton("Удалить") { _, _ ->
                onDelete()
                Toast.makeText(context, "Склад удалён", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
