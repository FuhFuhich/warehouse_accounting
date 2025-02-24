package com.example.warehouse_accounting.ui.products

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Product

class ProductsFabHelper(
    private val fragment: Fragment,  // Заменил context на Fragment для работы с результатом
    private val onProductAdded: (Product) -> Unit
) {
    private var productImageUri: Uri? = null

    // Новый API для обработки результата выбора изображения
    private val imagePickerLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                productImageUri = uri
                Toast.makeText(fragment.requireContext(), "Изображение выбрано", Toast.LENGTH_SHORT).show()
            }
        }

    fun showAddProductDialog() {
        val context = fragment.requireContext()
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_products_dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_product_name)
        val barcodeEditText: EditText = dialogView.findViewById(R.id.et_product_barcode)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_product_description)
        val quantityEditText: EditText = dialogView.findViewById(R.id.et_product_quantity)
        val productImageView: ImageView = dialogView.findViewById(R.id.iv_product_image)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Добавить новый товар")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val productName = nameEditText.text.toString()
                val barcode = barcodeEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val quantity = quantityEditText.text.toString()

                if (productName.isNotEmpty() && barcode.isNotEmpty() && quantity.isNotEmpty()) {
                    val product = Product(
                        name = productName,
                        description = description,
                        barcode = barcode,
                        imageUri = productImageUri,
                        quantity = quantity.toInt()
                    )
                    onProductAdded(product)
                    Toast.makeText(context, "Товар добавлен", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()

        productImageView.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        imagePickerLauncher.launch("image/*")  // Открытие галереи
    }
}
