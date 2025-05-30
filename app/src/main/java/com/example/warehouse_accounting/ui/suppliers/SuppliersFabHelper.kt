package com.example.warehouse_accounting.ui.suppliers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Suppliers

class SuppliersFabHelper(
    private val context: Context,
    private val onSuppliersAdded: (Suppliers) -> Unit,
    private val viewModel: SuppliersViewModel
) {

    fun showAddSuppliersDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_suppliers_dialog_add_supplier, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_suppliers_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_suppliers_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_suppliers_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_suppliers_phone)
        val tinEditText: EditText = dialogView.findViewById(R.id.et_suppliers_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_suppliers_bank_details)
        val noteEditText: EditText = dialogView.findViewById(R.id.et_suppliers_note)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Добавить нового поставщика")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = nameEditText.text.toString()
                val address = addressEditText.text.toString()
                val email = emailEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val tin = tinEditText.text.toString()
                val bankDetails = bankDetailsEditText.text.toString()
                val note = noteEditText.text.toString()

                if (name.isNotEmpty()) {
                    val supplier = Suppliers(
                        id = 0,
                        name = name,
                        address = address,
                        email = email,
                        phone = phone,
                        tin = tin,
                        bankDetails = bankDetails,
                        note = note
                    )
                    onSuppliersAdded(supplier)
                    Toast.makeText(context, "Поставщик добавлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    fun showEditSuppliersDialog(suppliers: Suppliers, onSuppliersUpdated: (Suppliers) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_suppliers_dialog_add_supplier, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_suppliers_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_suppliers_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_suppliers_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_suppliers_phone)
        val tinEditText: EditText = dialogView.findViewById(R.id.et_suppliers_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_suppliers_bank_details)
        val noteEditText: EditText = dialogView.findViewById(R.id.et_suppliers_note)

        nameEditText.setText(suppliers.name)
        addressEditText.setText(suppliers.address ?: "")
        emailEditText.setText(suppliers.email ?: "")
        phoneEditText.setText(suppliers.phone ?: "")
        tinEditText.setText(suppliers.tin ?: "")
        bankDetailsEditText.setText(suppliers.bankDetails ?: "")
        noteEditText.setText(suppliers.note ?: "")

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Редактировать поставщика")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newAddress = addressEditText.text.toString()
                val newEmail = emailEditText.text.toString()
                val newPhone = phoneEditText.text.toString()
                val newTin = tinEditText.text.toString()
                val newBankDetails = bankDetailsEditText.text.toString()
                val newNote = noteEditText.text.toString()

                if (newName.isNotEmpty()) {
                    val updatedSuppliers = suppliers.copy(
                        id = suppliers.id,
                        name = newName,
                        address = newAddress,
                        email = newEmail,
                        phone = newPhone,
                        tin = newTin,
                        bankDetails = newBankDetails,
                        note = newNote
                    )
                    onSuppliersUpdated(updatedSuppliers)
                    viewModel.updateSuppliers(updatedSuppliers)
                    Toast.makeText(context, "Поставщик обновлён", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val supplierName = data.getStringExtra("supplier_name") ?: return
            val supplierAddress = data.getStringExtra("supplier_address") ?: ""
            val supplierEmail = data.getStringExtra("supplier_email") ?: ""
            val supplierPhone = data.getStringExtra("supplier_phone") ?: ""
            val supplierTIN = data.getStringExtra("supplier_tin") ?: ""
            val supplierBankDetails = data.getStringExtra("supplier_bank_details") ?: ""
            val supplierNote = data.getStringExtra("supplier_note") ?: ""

            val newSupplier = Suppliers(
                id = 0,
                name = supplierName,
                address = supplierAddress,
                email = supplierEmail,
                phone = supplierPhone,
                tin = supplierTIN,
                bankDetails = supplierBankDetails,
                note = supplierNote
            )

            onSuppliersAdded(newSupplier)
            Toast.makeText(context, "Данные поставщика обновлены", Toast.LENGTH_SHORT).show()
        }
    }
}
