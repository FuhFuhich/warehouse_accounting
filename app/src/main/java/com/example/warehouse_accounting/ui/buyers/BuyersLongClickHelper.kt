package com.example.warehouse_accounting.ui.buyers

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Buyers

class BuyersLongClickHelper(
    private val context: Context
) {
    fun showOptionsDialog(
        buyer: Buyers,
        onDelete: () -> Unit
    ) {
        showDeleteConfirmDialog(buyer, onDelete)
    }

    private fun showDeleteConfirmDialog(buyer: Buyers, onDelete: () -> Unit) {
        val message = SpannableString("Вы уверены, что хотите удалить покупателя \"${buyer.name}\"?")
        message.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Удалить покупателя")
            .setMessage(message)
            .setPositiveButton("Удалить") { _, _ ->
                onDelete()
                Toast.makeText(context, "Покупатель удалён", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
