package com.example.warehouse_accounting.ui.warehouses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Warehouses

class WarehousesFabHelper(
    private val context: Context,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onWarehousesAdded: (Warehouses) -> Unit
) {

    fun showAddWarehousesDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_warehouses_dialog_add_warehouses, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_warehouses_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_warehouses_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_warehouses_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_warehouses_phone)
        val tinEditText: EditText = dialogView.findViewById(R.id.et_warehouses_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_warehouses_bank_details)
        val noteEditText: EditText = dialogView.findViewById(R.id.et_warehouses_note)

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

                if (name.isNotEmpty())
                {
                    val warehouses = Warehouses(
                        name = name,
                        address = address,
                        email = email,
                        phone = phone,
                        tin = tin,
                        bankDetails = bankDetails,
                        note = note
                    )
                    onWarehousesAdded(warehouses)
                    Toast.makeText(context, "Поставщик добавлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    fun showEditWarehousesDialog(warehouses: Warehouses, onWarehousesUpdated: (Warehouses) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_warehouses_dialog_add_warehouses, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_warehouses_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_warehouses_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_warehouses_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_warehouses_phone)
        val tinEditText: EditText = dialogView.findViewById(R.id.et_warehouses_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_warehouses_bank_details)
        val noteEditText: EditText = dialogView.findViewById(R.id.et_warehouses_note)

        nameEditText.setText(warehouses.name)
        addressEditText.setText(warehouses.address)
        emailEditText.setText(warehouses.email)
        phoneEditText.setText(warehouses.phone)
        tinEditText.setText(warehouses.tin)
        bankDetailsEditText.setText(warehouses.bankDetails)
        noteEditText.setText(warehouses.note)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Редактировать склад")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newAddress = addressEditText.text.toString()
                val newEmail = emailEditText.text.toString()
                val newPhone = phoneEditText.text.toString()
                val newTin = tinEditText.text.toString()
                val newBankDetails = bankDetailsEditText.text.toString()
                val newNote = noteEditText.text.toString()

                if (newName.isNotEmpty())
                {
                    val updatedWarehouses = Warehouses(
                        name = newName,
                        address = newAddress,
                        email = newEmail,
                        phone = newPhone,
                        tin = newTin,
                        bankDetails = newBankDetails,
                        note = newNote
                    )
                    onWarehousesUpdated(updatedWarehouses)
                    Toast.makeText(context, "Warehouses обновлён", Toast.LENGTH_SHORT).show()
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
            val warehousesName = data.getStringExtra("warehouses_name") ?: return
            val warehousesAddress = data.getStringExtra("warehouses_address") ?: return
            val warehousesEmail = data.getStringExtra("warehouses_email") ?: return
            val warehousesPhone = data.getStringExtra("warehouses_phone") ?: return
            val warehousesTIN = data.getStringExtra("warehouses_tin") ?: return
            val warehousesBankDetails = data.getStringExtra("warehouses_bank_details") ?: return
            val warehousesNote = data.getStringExtra("warehouses_note") ?: return

            val newWarehouses = Warehouses(
                name = warehousesName,
                address = warehousesAddress,
                email = warehousesEmail,
                phone = warehousesPhone,
                tin = warehousesTIN,
                bankDetails = warehousesBankDetails,
                note = warehousesNote
            )

            onWarehousesAdded(newWarehouses)
            Toast.makeText(context, "Данные поставщика обновлены", Toast.LENGTH_SHORT).show()
        }
    }
}
