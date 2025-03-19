package com.example.warehouse_accounting.ui.documents

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R

class DocumentsFabHelper(
    private val context: Context,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onDocumentTypeSelectedListener: OnDocumentTypeSelectedListener
) {
    interface OnDocumentTypeSelectedListener {
        fun onDocumentTypeSelected(documentType: String?)
    }

    fun showDocumentTypeSelectionDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_documents_dialog_add_document, null)

        val dialog = AlertDialog.Builder(
            context, R.style.MyAlertDialogTheme
        )
            .setTitle("Выберите тип документа")
            .setView(dialogView)
            .setPositiveButton("Выбрать", null)
            .setNegativeButton(
                "Отмена"
            ) { d: DialogInterface, which: Int -> d.dismiss() }
            .create()

        dialog.setOnShowListener { d: DialogInterface? ->
            val button: View =
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener { v: View? ->
                showPopupMenu(
                    button
                )
            }
        }

        dialog.show()
    }

    private fun showPopupMenu(anchor: View) {
        val popupMenu = PopupMenu(context, anchor)
        popupMenu.menuInflater.inflate(R.menu.documents_fab_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            this.onMenuItemSelected(
                item
            )
        }
        popupMenu.show()
    }

    private fun onMenuItemSelected(item: MenuItem): Boolean {
        var documentType = ""
        documentType = when (item.itemId) {
            R.id.menu_documents_income -> "Приход"
            R.id.menu_documents_expense -> "Расход"
            R.id.menu_documents_movement -> "Перемещение"
            R.id.menu_documents_inventory -> "Инвентаризация"
            else -> return false
        }
        Toast.makeText(context, "Выбран документ: $documentType", Toast.LENGTH_SHORT).show()
        onDocumentTypeSelectedListener.onDocumentTypeSelected(documentType)
        return true
    }
}