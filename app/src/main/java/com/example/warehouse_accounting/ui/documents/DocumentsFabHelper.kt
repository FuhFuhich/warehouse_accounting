package com.example.warehouse_accounting.ui.documents

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R

class DocumentsFabHelper(
    private val context: Context,
    private val onDocumentsTypeSelectedListener: OnDocumentsTypeSelectedListener
) {
    interface OnDocumentsTypeSelectedListener {
        fun onDocumentsTypeSelected(documentType: String)
    }

    fun showDocumentTypeSelectionDialog() {
        val options = arrayOf("Приход", "Расход", "Перемещение", "Инвентаризация")

        val adapter = object : ArrayAdapter<String>(
            context,
            android.R.layout.simple_list_item_1,
            options
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).apply {
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#222222"))
                }
                return view
            }
        }

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Выберите тип документа")
            .setAdapter(adapter) { _, which ->
                val documentType = options[which]
                onDocumentsTypeSelectedListener.onDocumentsTypeSelected(documentType)
            }
            .setNegativeButton("ОТМЕНА", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
    }
}
