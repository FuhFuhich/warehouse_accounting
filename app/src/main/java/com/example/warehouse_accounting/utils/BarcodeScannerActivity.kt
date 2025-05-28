package com.example.warehouse_accounting.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.warehouse_accounting.databinding.ActivityBarcodeScannerBinding
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarcodeScannerBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null

    companion object {
        const val EXTRA_BARCODE_RESULT = "barcode_result"
        const val EXTRA_PRODUCT_NAME = "product_name"
        const val EXTRA_BARCODE_INFO = "barcode_info"
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcodeData ->
                        returnBarcodeResult(barcodeData)
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Ошибка запуска камеры", Toast.LENGTH_SHORT).show()
                finish()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun returnBarcodeResult(barcodeData: Triple<String, String?, String?>) {
        val resultIntent = Intent().apply {
            putExtra(EXTRA_BARCODE_RESULT, barcodeData.first)      // штрих-код
            putExtra(EXTRA_PRODUCT_NAME, barcodeData.second)       // название товара
            putExtra(EXTRA_BARCODE_INFO, barcodeData.third)        // описание
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Разрешения не предоставлены", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private inner class BarcodeAnalyzer(private val onBarcodeDetected: (Triple<String, String?, String?>) -> Unit) : ImageAnalysis.Analyzer {

        private val scanner = BarcodeScanning.getClient()

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            barcode.rawValue?.let { barcodeValue ->
                                val productName = getProductName(barcodeValue, barcode.format)
                                val description = getBarcodeDescription(barcodeValue, barcode.format)

                                onBarcodeDetected(Triple(barcodeValue, productName, description))
                                return@addOnSuccessListener
                            }
                        }
                    }
                    .addOnFailureListener {
                        // Тут будет обработка ошибки сканирования
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }

        private fun getProductName(barcode: String, format: Int): String? {
            return when {
                (barcode.startsWith("978") || barcode.startsWith("979")) && barcode.length == 13 -> "Книга"

                barcode.startsWith("460") || barcode.startsWith("461") ||
                        barcode.startsWith("462") || barcode.startsWith("463") -> "Российский товар"

                format == Barcode.FORMAT_QR_CODE -> "QR товар"

                else -> null
            }
        }

        private fun getBarcodeDescription(barcode: String, format: Int): String? {
            val parts = mutableListOf<String>()

            when (format) {
                Barcode.FORMAT_EAN_13 -> parts.add("EAN-13")
                Barcode.FORMAT_EAN_8 -> parts.add("EAN-8")
                Barcode.FORMAT_UPC_A -> parts.add("UPC-A")
                Barcode.FORMAT_CODE_128 -> parts.add("Code 128")
                Barcode.FORMAT_QR_CODE -> parts.add("QR код")
                else -> parts.add("Штрих-код")
            }

            when {
                barcode.startsWith("460") -> parts.add("Россия")
                barcode.startsWith("482") -> parts.add("Украина")
                barcode.startsWith("481") -> parts.add("Беларусь")
                barcode.startsWith("978") || barcode.startsWith("979") -> parts.add("ISBN")
            }

            return if (parts.isNotEmpty()) parts.joinToString(", ") else null
        }
    }
}
