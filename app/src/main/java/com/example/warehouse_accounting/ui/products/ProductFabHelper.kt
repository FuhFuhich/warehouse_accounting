package com.example.warehouse_accounting.ui.products

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Product

class ProductFabHelper(
    private val context: Context,
    private val productList: MutableList<Product>,
    private val onProductAdded: (Product) -> Unit
) {
    private var productImageUri: Uri? = null
    private val IMAGE_PICK_CODE = 1000

    fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_products_dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_product_name)
        val barcodeEditText: EditText = dialogView.findViewById(R.id.et_barcode)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_description)
        val quantityEditText: EditText = dialogView.findViewById(R.id.et_quantity)
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
                    productList.add(product)
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        (context as Activity).startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            productImageUri = data?.data
            Toast.makeText(context, "Изображение выбрано", Toast.LENGTH_SHORT).show()
        }
    }
}
