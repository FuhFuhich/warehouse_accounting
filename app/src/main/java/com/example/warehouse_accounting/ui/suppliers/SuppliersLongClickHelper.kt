package com.example.warehouse_accounting.ui.suppliers

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Suppliers

class SuppliersLongClickHelper(
    private val context: Context
) {
    fun showOptionsDialog(
        supplier: Suppliers,
        onDelete: () -> Unit
    ) {
        showDeleteConfirmDialog(supplier, onDelete)
    }

    private fun showDeleteConfirmDialog(supplier: Suppliers, onDelete: () -> Unit) {
        val message = SpannableString("Вы уверены, что хотите удалить поставщика \"${supplier.name}\"?")
        message.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Удалить поставщика")
            .setMessage(message)
            .setPositiveButton("Удалить") { _, _ ->
                onDelete()
                Toast.makeText(context, "Поставщик удалён", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
