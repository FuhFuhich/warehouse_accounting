package com.example.warehouse_accounting.ui.documents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Documents
import java.time.format.DateTimeFormatter

class DocumentsFabHelper(
    private val context: Context,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onDocumentsAdded: (Documents) -> Unit,
    private val formatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
) {

    fun showAddDocumentsDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_documents_dialog_add_document, null)
        val documentsEditText: EditText = dialogView.findViewById(R.id.et_documents)
        val documentsNumberEditText: EditText = dialogView.findViewById(R.id.et_documents_number)
        val creationDateEditText: EditText = dialogView.findViewById(R.id.et_creation_date)
        val nameOfWarehouseEditText: EditText = dialogView.findViewById(R.id.et_name_of_warehouse)
        val quantityOfGoodsEditText: EditText = dialogView.findViewById(R.id.et_quantity_of_goods)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Добавить нового поставщика")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val documents = documentsEditText.text.toString()
                val documentsNumber = documentsNumberEditText.text.toString()
                val creationDate = creationDateEditText.text.toString()
                val nameOfWarehouse = nameOfWarehouseEditText.text.toString()
                val quantityOfGoods = quantityOfGoodsEditText.text.toString()

                if (documents.isNotEmpty())
                {
                    val supplier = Documents(
                        documents = documents,
                        documentsNumber = documentsNumber,
                        creationDate = creationDate,
                        nameOfWarehouse = nameOfWarehouse,
                        quantityOfGoods = quantityOfGoods.toInt()
                    )
                    onDocumentsAdded(supplier)
                    Toast.makeText(context, "Поставщик добавлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    fun showEditDocumentsDialog(documents: Documents, onDocumentsUpdated: (Documents) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_documents_dialog_add_documents, null)
        val documentsEditText: EditText = dialogView.findViewById(R.id.et_documents)
        val documentsNumberEditText: EditText = dialogView.findViewById(R.id.et_documents_number)
        val creationDateEditText: EditText = dialogView.findViewById(R.id.et_creation_date)
        val nameOfWarehouseEditText: EditText = dialogView.findViewById(R.id.et_name_of_warehouse)
        val quantityOfGoodsEditText: EditText = dialogView.findViewById(R.id.et_quantity_of_goods)

        documentsEditText.setText(documents.documents)
        documentsNumberEditText.setText(documents.documentsNumber)
        creationDateEditText.setText(documents.creationDate)
        nameOfWarehouseEditText.setText(documents.nameOfWarehouse)
        quantityOfGoodsEditText.setText(documents.quantityOfGoods)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Редактировать поставщика")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val documents = documentsEditText.text.toString()
                val documentsNumber = documentsNumberEditText.text.toString()
                val creationDate = creationDateEditText.text.toString()
                val nameOfWarehouse = nameOfWarehouseEditText.text.toString()
                val quantityOfGoods = quantityOfGoodsEditText.text.toString()

                if (documents.isNotEmpty())
                {
                    val updatedDocuments = Documents(
                        documents = documents,
                        documentsNumber = documentsNumber,
                        creationDate = creationDate,
                        nameOfWarehouse = nameOfWarehouse,
                        quantityOfGoods = quantityOfGoods.toInt()
                    )
                    onDocumentsUpdated(updatedDocuments)
                    Toast.makeText(context, "Поставщик обновлён", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val documentsDocuments = data.getStringExtra("document_document") ?: return
            val documentsDocumentsNumber = data.getStringExtra("document_documents_number") ?: return
            val documentsCreationDate = data.getStringExtra("document_creation_date") ?: return
            val documentsNameOfWarehouse = data.getStringExtra("document_name_of_warehouse") ?: return
            val documentsQuantityOfGoods = data.getStringExtra("document_quantity_of_goods") ?: return

            val newDocuments = Documents(
                documents = documentsDocuments,
                documentsNumber = documentsDocumentsNumber,
                creationDate = documentsCreationDate,
                nameOfWarehouse = documentsNameOfWarehouse,
                quantityOfGoods = documentsQuantityOfGoods.toInt()
            )

            onDocumentsAdded(newDocuments)
            Toast.makeText(context, "Данные документов обновлены", Toast.LENGTH_SHORT).show()
        }
    }
}
