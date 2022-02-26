package com.supersuman.macronium

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors


class ScannerActivity : AppCompatActivity(){

    private lateinit var previewView : PreviewView
    private lateinit var scanner : BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        initViews()
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),100)
        val options : BarcodeScannerOptions = BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,Barcode.FORMAT_AZTEC).build()
        scanner = BarcodeScanning.getClient(options)
        startCamera()

    }

    private fun initViews() {
        previewView = findViewById(R.id.previewView)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val imageAnalyzer =  ImageAnalysis.Builder().build().also {
                it.setAnalyzer(Executors.newSingleThreadExecutor(),ScannerAnalyzer(scanner,this))
                it.targetRotation = previewView.display.rotation
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
}

private class ScannerAnalyzer(val scanner: BarcodeScanner, val scannerActivity: ScannerActivity) : ImageAnalysis.Analyzer{

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val image1 = InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
        scanner.process(image1)
            .addOnSuccessListener { mutableList ->
                mutableList.forEach {
                    val returnIntent = Intent()
                    returnIntent.putExtra("result",it.rawValue)
                    scannerActivity.setResult(Activity.RESULT_OK, returnIntent)
                    scannerActivity.finish()
                }
            }
            .addOnCompleteListener {
                image.close()
            }
    }
}