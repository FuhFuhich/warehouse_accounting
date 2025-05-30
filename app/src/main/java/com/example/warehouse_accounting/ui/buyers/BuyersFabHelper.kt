package com.example.warehouse_accounting.ui.buyers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Buyers

class BuyersFabHelper(
    private val context: Context,
    private val onBuyersAdded: (Buyers) -> Unit,
    private val viewModel: BuyersViewModel
) {
    fun showAddBuyersDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_buyers_dialog_add_buyers, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_buyers_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_buyers_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_buyers_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_buyers_phone)
        val tinEditText: EditText = dialogView.findViewById(R.id.et_buyers_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_buyers_bank_details)
        val noteEditText: EditText = dialogView.findViewById(R.id.et_buyers_note)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Добавить нового покупателя")
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
                    val buyers = Buyers(
                        id = 0,
                        name = name,
                        address = address,
                        email = email,
                        phone = phone,
                        tin = tin,
                        bankDetails = bankDetails,
                        note = note
                    )
                    onBuyersAdded(buyers)
                    Toast.makeText(context, "Покупатель добавлен", Toast.LENGTH_SHORT).show()  // Исправлено
                } else {
                    Toast.makeText(context, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    fun showEditBuyersDialog(buyers: Buyers, onBuyersUpdated: (Buyers) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_buyers_dialog_add_buyers, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_buyers_name)
        val addressEditText: EditText = dialogView.findViewById(R.id.et_buyers_address)
        val emailEditText: EditText = dialogView.findViewById(R.id.et_buyers_email)
        val phoneEditText: EditText = dialogView.findViewById(R.id.et_buyers_phone)
        val tinEditText: EditText = dialogView.findViewById(R.id.et_buyers_TIN)
        val bankDetailsEditText: EditText = dialogView.findViewById(R.id.et_buyers_bank_details)
        val noteEditText: EditText = dialogView.findViewById(R.id.et_buyers_note)

        nameEditText.setText(buyers.name)
        addressEditText.setText(buyers.address ?: "")
        emailEditText.setText(buyers.email ?: "")
        phoneEditText.setText(buyers.phone ?: "")
        tinEditText.setText(buyers.tin ?: "")
        bankDetailsEditText.setText(buyers.bankDetails ?: "")
        noteEditText.setText(buyers.note ?: "")

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Редактировать покупателя")
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
                    val updatedBuyers = buyers.copy(
                        id = buyers.id,
                        name = newName,
                        address = newAddress,
                        email = newEmail,
                        phone = newPhone,
                        tin = newTin,
                        bankDetails = newBankDetails,
                        note = newNote
                    )
                    onBuyersUpdated(updatedBuyers)
                    viewModel.updateBuyers(updatedBuyers)
                    Toast.makeText(context, "Покупатель обновлён", Toast.LENGTH_SHORT).show()
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
            val buyersName = data.getStringExtra("buyers_name") ?: return
            val buyersAddress = data.getStringExtra("buyers_address") ?: ""
            val buyersEmail = data.getStringExtra("buyers_email") ?: ""
            val buyersPhone = data.getStringExtra("buyers_phone") ?: ""
            val buyersTIN = data.getStringExtra("buyers_tin") ?: ""
            val buyersBankDetails = data.getStringExtra("buyers_bank_details") ?: ""
            val buyersNote = data.getStringExtra("buyers_note") ?: ""

            val newBuyers = Buyers(
                id = 0,
                name = buyersName,
                address = buyersAddress,
                email = buyersEmail,
                phone = buyersPhone,
                tin = buyersTIN,
                bankDetails = buyersBankDetails,
                note = buyersNote
            )

            onBuyersAdded(newBuyers)
            Toast.makeText(context, "Данные покупателя обновлены", Toast.LENGTH_SHORT).show()
        }
    }
}
