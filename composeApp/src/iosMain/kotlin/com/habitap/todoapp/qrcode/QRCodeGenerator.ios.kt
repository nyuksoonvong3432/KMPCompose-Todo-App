package com.habitap.todoapp.qrcode

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import qrcode.QRCode

/**
 * iOS-specific implementation of QRCodeGenerator.
 * Uses qrcode-kotlin with Skia to create QR code images.
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosQRCodeGenerator : QRCodeGenerator {

    override fun generateQRCode(content: String, size: Int): ImageBitmap {
        // Generate QR code using qrcode-kotlin builder API
        // render() returns QRCodeGraphics, call getBytes() to get PNG bytes
        val qrCodeBytes = QRCode.ofSquares()
            .withSize(size / 25) // Cell size calculation
            .build(content)
            .render()
            .getBytes()

        // Convert PNG bytes to Skia Image
        val skiaImage = Image.makeFromEncoded(qrCodeBytes)

        // Scale to exact size if needed
        val scaledImage = if (skiaImage.width != size || skiaImage.height != size) {
            val surface = org.jetbrains.skia.Surface.makeRasterN32Premul(size, size)
            val canvas = surface.canvas
            canvas.clear(org.jetbrains.skia.Color.WHITE)

            val paint = org.jetbrains.skia.Paint()
            val srcRect = org.jetbrains.skia.Rect.makeWH(
                skiaImage.width.toFloat(),
                skiaImage.height.toFloat()
            )
            val dstRect = org.jetbrains.skia.Rect.makeWH(size.toFloat(), size.toFloat())

            canvas.drawImageRect(skiaImage, srcRect, dstRect, paint)
            surface.makeImageSnapshot()
        } else {
            skiaImage
        }

        return scaledImage.toComposeImageBitmap()
    }
}

/**
 * Creates an iOS-specific QRCodeGenerator instance.
 */
actual fun createQRCodeGenerator(): QRCodeGenerator = IosQRCodeGenerator()
