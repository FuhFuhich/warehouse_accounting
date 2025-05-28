package com.example.warehouse_accounting.ui.products

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
                        imageUri = null,
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

        currentBarcodeEditText = barcodeEditText
        currentNameEditText = nameEditText
        currentDescriptionEditText = descriptionEditText

        Log.d("ProductsFabHelper", "Dialog shown, EditTexts assigned")

        scanBarcodeButton.setOnClickListener {
            Log.d("ProductsFabHelper", "Scan button clicked")
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
        descriptionEditText.setText(product.description)
        quantityEditText.setText(product.quantity.toString())
        warehouseEditText.setText(product.warehouse ?: "")

        val dialog = AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
            .setTitle("Редактировать товар")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newBarcode = barcodeEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()
                val newQuantity = quantityEditText.text.toString()
                val newWarehouse = warehouseEditText.text.toString()

                if (newName.isNotEmpty() && newBarcode.isNotEmpty() && newQuantity.isNotEmpty()) {
                    val updatedProduct = product.copy(
                        id = product.id,
                        name = newName,
                        barcode = newBarcode,
                        description = newDescription,
                        imageUri = null,
                        quantity = newQuantity.toInt(),
                        warehouse = newWarehouse
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

        currentBarcodeEditText = barcodeEditText
        currentNameEditText = nameEditText
        currentDescriptionEditText = descriptionEditText

        scanBarcodeButton.setOnClickListener {
            startBarcodeScanner()
        }
    }

    private fun startBarcodeScanner() {
        Log.d("ProductsFabHelper", "Starting barcode scanner")
        val intent = Intent(context, BarcodeScannerActivity::class.java)
        (context as Activity).startActivityForResult(intent, BARCODE_SCAN_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("ProductsFabHelper", "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (resultCode == Activity.RESULT_OK && requestCode == BARCODE_SCAN_CODE) {
            val scannedBarcode = data?.getStringExtra(BarcodeScannerActivity.EXTRA_BARCODE_RESULT)
            val productName = data?.getStringExtra(BarcodeScannerActivity.EXTRA_PRODUCT_NAME)
            val barcodeInfo = data?.getStringExtra(BarcodeScannerActivity.EXTRA_BARCODE_INFO)

            Log.d("ProductsFabHelper", "Scanned data: barcode=$scannedBarcode, name=$productName, info=$barcodeInfo")
            Log.d("ProductsFabHelper", "Current EditTexts: barcode=${currentBarcodeEditText != null}, name=${currentNameEditText != null}, desc=${currentDescriptionEditText != null}")

            if (!scannedBarcode.isNullOrEmpty()) {
                currentBarcodeEditText?.let { editText ->
                    editText.setText(scannedBarcode)
                    Log.d("ProductsFabHelper", "Barcode set: $scannedBarcode")
                } ?: Log.e("ProductsFabHelper", "currentBarcodeEditText is null!")

                if (!productName.isNullOrEmpty()) {
                    currentNameEditText?.let { editText ->
                        if (editText.text.toString().isEmpty()) {
                            editText.setText(productName)
                            Log.d("ProductsFabHelper", "Product name set: $productName")
                        }
                    }
                }

                if (!barcodeInfo.isNullOrEmpty()) {
                    currentDescriptionEditText?.let { editText ->
                        if (editText.text.toString().isEmpty()) {
                            editText.setText(barcodeInfo)
                            Log.d("ProductsFabHelper", "Description set: $barcodeInfo")
                        }
                    }
                }

                Toast.makeText(context, "Штрих-код отсканирован: $scannedBarcode", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("ProductsFabHelper", "Scanned barcode is null or empty")
                Toast.makeText(context, "Не удалось отсканировать штрих-код", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
