package com.example.warehouse_accounting.ui.products

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Product
import com.example.warehouse_accounting.utils.BarcodeScannerActivity
import android.util.Log

class ProductsFabHelper(
    private val context: Context,
    private val viewModel: ProductsViewModel
) {
    private val BARCODE_SCAN_CODE = 2000

    private var currentBarcodeEditText: EditText? = null
    private var currentNameEditText: EditText? = null
    private var currentDescriptionEditText: EditText? = null

    fun showAddProductDialog() {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_products_dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_product_name)
        val barcodeEditText: EditText = dialogView.findViewById(R.id.et_product_barcode)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_product_description)
        val quantityEditText: EditText = dialogView.findViewById(R.id.et_product_quantity)
        val scanBarcodeButton: ImageView = dialogView.findViewById(R.id.iv_scan_barcode)
        val warehouseEditText: EditText = dialogView.findViewById(R.id.et_product_warehouse)

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Добавить новый товар")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val product = Product(
                    name = nameEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    barcode = barcodeEditText.text.toString(),
                    quantity = quantityEditText.text.toString().toIntOrNull() ?: 0,
                    warehouse = warehouseEditText.text.toString().takeIf { it.isNotBlank() }
                )

                viewModel.addProducts(product)
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()

        currentBarcodeEditText = barcodeEditText
        currentNameEditText = nameEditText
        currentDescriptionEditText = descriptionEditText

        scanBarcodeButton.setOnClickListener {
            startBarcodeScanner()
        }
    }

    fun showEditProductDialog(product: Product, onProductUpdated: (Product) -> Unit) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_products_dialog_add_product, null)
        val nameEditText: EditText = dialogView.findViewById(R.id.et_product_name)
        val barcodeEditText: EditText = dialogView.findViewById(R.id.et_product_barcode)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.et_product_description)
        val quantityEditText: EditText = dialogView.findViewById(R.id.et_product_quantity)
        val scanBarcodeButton: ImageView = dialogView.findViewById(R.id.iv_scan_barcode)
        val warehouseEditText: EditText = dialogView.findViewById(R.id.et_product_warehouse)

        nameEditText.setText(product.name)
        barcodeEditText.setText(product.barcode)
        descriptionEditText.setText(product.description ?: "")
        quantityEditText.setText(product.quantity.toString())
        warehouseEditText.setText(product.warehouse ?: "")

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Редактировать товар")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val updatedProduct = product.copy(
                    name = nameEditText.text.toString(),
                    barcode = barcodeEditText.text.toString(),
                    description = descriptionEditText.text.toString(),
                    quantity = quantityEditText.text.toString().toIntOrNull() ?: 0,
                    warehouse = warehouseEditText.text.toString().takeIf { it.isNotBlank() }
                )

                val success = viewModel.updateProducts(updatedProduct)
                if (success) {
                    onProductUpdated(updatedProduct)
                }
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()

        currentBarcodeEditText = barcodeEditText
        currentNameEditText = nameEditText
        currentDescriptionEditText = descriptionEditText

        scanBarcodeButton.setOnClickListener {
            startBarcodeScanner()
        }
    }

    private fun startBarcodeScanner() {
        val intent = Intent(context, BarcodeScannerActivity::class.java)
        (context as Activity).startActivityForResult(intent, BARCODE_SCAN_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == BARCODE_SCAN_CODE) {
            val scannedBarcode = data?.getStringExtra(BarcodeScannerActivity.EXTRA_BARCODE_RESULT)
            val productName = data?.getStringExtra(BarcodeScannerActivity.EXTRA_PRODUCT_NAME)
            val barcodeInfo = data?.getStringExtra(BarcodeScannerActivity.EXTRA_BARCODE_INFO)

            if (!scannedBarcode.isNullOrEmpty()) {
                currentBarcodeEditText?.setText(scannedBarcode)

                if (!productName.isNullOrEmpty() && currentNameEditText?.text.toString().isEmpty()) {
                    currentNameEditText?.setText(productName)
                }

                if (!barcodeInfo.isNullOrEmpty() && currentDescriptionEditText?.text.toString().isEmpty()) {
                    currentDescriptionEditText?.setText(barcodeInfo)
                }

                Toast.makeText(context, "Штрих-код отсканирован: $scannedBarcode", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
