package com.example.warehouse_accounting.ui.warehouses

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Warehouses

class WarehousesLongClickHelper(
    private val context: Context
) {
    fun showOptionsDialog(
        warehouse: Warehouses,
        onDelete: () -> Unit
    ) {
        showDeleteConfirmDialog(warehouse, onDelete)
    }

    private fun showDeleteConfirmDialog(warehouse: Warehouses, onDelete: () -> Unit) {
        val message = SpannableString("Вы уверены, что хотите удалить склад и все продукты \"${warehouse.warehousesName}\"?")
        message.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Удалить склад")
            .setMessage(message)
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
