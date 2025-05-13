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
    private val onWarehousesAdded: (Warehouses) -> Unit,
    private val viewModel: WarehousesViewModel
) {

    fun showAddWarehousesDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_warehouses_dialog_add_warehouses, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_warehouses_name)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Добавить новый склад")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val warehousesName = nameEditText.text.toString()

                if (warehousesName.isNotEmpty())
                {
                    // Никакого generateUniqueId, удалить к чертовой матери
                    // Я буду брать айдишник сразу из ServerController.
                    // Я отправляю запрос на добавление склада
                    // Там же я возвращаю сгенерированный айдишник

                    val warehouses = Warehouses(
                        id = viewModel.getId(),
                        warehousesName = warehousesName,
                    )
                    onWarehousesAdded(warehouses)
                    Toast.makeText(context, "Склад добавлен", Toast.LENGTH_SHORT).show()
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

        nameEditText.setText(warehouses.warehousesName)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Редактировать склад")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()

                if (newName.isNotEmpty()) {
                    val updatedWarehouses = Warehouses(
                        id = warehouses.id,
                        warehousesName = newName,
                    )
                    onWarehousesUpdated(updatedWarehouses)
                    Toast.makeText(context, "Склад обновлён", Toast.LENGTH_SHORT).show()
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

            val newWarehouses = Warehouses(
                id = viewModel.getId(),
                warehousesName = warehousesName,
            )

            onWarehousesAdded(newWarehouses)
            Toast.makeText(context, "Данные поставщика обновлены", Toast.LENGTH_SHORT).show()
        }
    }
}
