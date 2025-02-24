package com.example.warehouse_accounting.ui.suppliers

import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Suppliers

class SuppliersFabHelper(
    private val context: Context,
    private val onSuppliersAdded: (Suppliers) -> Unit
) {

    fun showAddSuppliersDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_suppliers_dialog_add_supplier, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_suppliers_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_suppliers_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_suppliers_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_suppliers_phone)
        val TinImageView: EditText = dialogView.findViewById(R.id.et_suppliers_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_suppliers_bank_details)
        val noteImageView: EditText = dialogView.findViewById(R.id.et_suppliers_note)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Добавить нового поставщика")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = nameEditText.text.toString()
                val address = addressEditText.text.toString()
                val email = emailEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val tin = TinImageView.text.toString()
                val bankDetails = bankDetailsEditText.text.toString()
                val note = noteImageView.text.toString()

                if (name.isNotEmpty() && address.isNotEmpty() &&
                    email.isNotEmpty() && phone.isNotEmpty() &&
                    tin.isNotEmpty() && bankDetails.isNotEmpty() &&
                    note.isNotEmpty()) {
                    val supplier = Suppliers(
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
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
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
        val TinImageView: EditText = dialogView.findViewById(R.id.et_suppliers_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_suppliers_bank_details)
        val noteImageView: EditText = dialogView.findViewById(R.id.et_suppliers_note)

        nameEditText.setText(suppliers.name)
        addressEditText.setText(suppliers.address)
        emailEditText.setText(suppliers.email)
        phoneEditText.setText(suppliers.phone)
        TinImageView.setText(suppliers.tin)
        bankDetailsEditText.setText(suppliers.bankDetails)
        noteImageView.setText(suppliers.note)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Редактировать товар")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newAddress = addressEditText.text.toString()
                val newEmail = emailEditText.text.toString()
                val newPhone = phoneEditText.text.toString()
                val newTin = TinImageView.text.toString()
                val newBankDetails = bankDetailsEditText.text.toString()
                val newNote = noteImageView.text.toString()

                if (newName.isNotEmpty() && newAddress.isNotEmpty() &&
                    newEmail.isNotEmpty() && newPhone.isNotEmpty() &&
                    newTin.isNotEmpty() && newBankDetails.isNotEmpty() &&
                    newNote.isNotEmpty()) {
                    val updatedSuppliers = Suppliers(
                        name = newName,
                        address = newAddress,
                        email = newEmail,
                        phone = newPhone,
                        tin = newTin,
                        bankDetails = newBankDetails,
                        note = newNote
                    )
                    onSuppliersUpdated(updatedSuppliers)
                    Toast.makeText(context, "Поставщик обновлён", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }
}
