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

class ProductsFabHelper(
    private val context: Context,
    private val viewModel: ProductsViewModel
) {
    private var productImageUri: String? = null
    private val IMAGE_PICK_CODE = 1000

    fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_products_dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_product_name)
        val barcodeEditText: EditText = dialogView.findViewById(R.id.et_product_barcode)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_product_description)
        val quantityEditText: EditText = dialogView.findViewById(R.id.et_product_quantity)
        val productImageView: ImageView = dialogView.findViewById(R.id.iv_product_image)
        val warehouseEditText: EditText = dialogView.findViewById(R.id.et_product_warehouse)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Добавить новый товар")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val productName = nameEditText.text.toString()
                val barcode = barcodeEditText.text.toString()
                val description = descriptionEditText.text.toString()
                val quantity = quantityEditText.text.toString()
                val warehouse = warehouseEditText.text.toString()

                if (productName.isNotEmpty() && barcode.isNotEmpty() && quantity.isNotEmpty()) {
                    val product = Product(
                        name = productName,
                        description = description,
                        barcode = barcode,
                        imageUri = productImageUri,
                        quantity = quantity.toInt(),
                        warehouse = warehouse
                    )
                    viewModel.addProducts(product)
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

    fun showEditProductDialog(product: Product, onProductUpdated: (Product) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_products_dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_product_name)
        val barcodeEditText: EditText = dialogView.findViewById(R.id.et_product_barcode)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_product_description)
        val quantityEditText: EditText = dialogView.findViewById(R.id.et_product_quantity)
        val productImageView: ImageView = dialogView.findViewById(R.id.iv_product_image)

        nameEditText.setText(product.name)
        barcodeEditText.setText(product.barcode)
        descriptionEditText.setText(product.description)
        quantityEditText.setText(product.quantity.toString())
        productImageUri = product.imageUri

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Редактировать товар")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newBarcode = barcodeEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()
                val newQuantity = quantityEditText.text.toString()

                if (newName.isNotEmpty() && newBarcode.isNotEmpty() && newQuantity.isNotEmpty()) {
                    val updatedProduct = product.copy(
                        name = newName,
                        barcode = newBarcode,
                        description = newDescription,
                        imageUri = productImageUri,
                        quantity = newQuantity.toInt()
                    )
                    onProductUpdated(updatedProduct)
                    viewModel.updateProducts(updatedProduct)
                    Toast.makeText(context, "Товар обновлён", Toast.LENGTH_SHORT).show()
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
            productImageUri = data?.data.toString()
            Toast.makeText(context, "Изображение выбрано", Toast.LENGTH_SHORT).show()
        }
    }
}
