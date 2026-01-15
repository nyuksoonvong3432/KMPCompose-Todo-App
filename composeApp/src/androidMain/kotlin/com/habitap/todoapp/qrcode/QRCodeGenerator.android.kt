package com.habitap.todoapp.qrcode

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import qrcode.QRCode

/**
 * Android-specific implementation of QRCodeGenerator.
 * Uses Android's Bitmap API to create QR code images.
 */
internal class AndroidQRCodeGenerator : QRCodeGenerator {

    override fun generateQRCode(content: String, size: Int): ImageBitmap {
        // Generate QR code using qrcode-kotlin builder API
        // render() returns QRCodeGraphics, call getBytes() to get PNG bytes
        val qrCodeBytes = QRCode.ofSquares()
            .withSize(size / 25) // Cell size calculation
            .build(content)
            .render()
            .getBytes()

        // Decode the PNG byte array into a Bitmap
        val options = android.graphics.BitmapFactory.Options()
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(qrCodeBytes, 0, qrCodeBytes.size, options)
            ?: createFallbackQRCode(content, size)

        // Scale to exact size if needed
        val scaledBitmap = if (bitmap.width != size || bitmap.height != size) {
            Bitmap.createScaledBitmap(bitmap, size, size, false)
        } else {
            bitmap
        }

        return scaledBitmap.asImageBitmap()
    }

    /**
     * Creates a simple fallback QR code if the primary generation fails.
     */
    private fun createFallbackQRCode(content: String, size: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.WHITE)

        // Create a simple pattern to indicate QR code generation
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint().apply {
            color = Color.BLACK
            style = android.graphics.Paint.Style.FILL
        }

        // Draw a simple border to show something went wrong but app didn't crash
        canvas.drawRect(0f, 0f, size.toFloat(), 10f, paint)
        canvas.drawRect(0f, 0f, 10f, size.toFloat(), paint)
        canvas.drawRect(size - 10f, 0f, size.toFloat(), size.toFloat(), paint)
        canvas.drawRect(0f, size - 10f, size.toFloat(), size.toFloat(), paint)

        return bitmap
    }
}

/**
 * Creates an Android-specific QRCodeGenerator instance.
 */
actual fun createQRCodeGenerator(): QRCodeGenerator = AndroidQRCodeGenerator()
