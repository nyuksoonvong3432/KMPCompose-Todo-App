package com.habitap.todoapp.qrcode

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Interface for generating QR codes from text content.
 * Platform-specific implementations handle bitmap creation.
 */
interface QRCodeGenerator {
    /**
     * Generates a QR code image from the provided content.
     *
     * @param content The text content to encode in the QR code
     * @param size The size of the QR code in pixels (default: 512)
     * @return An ImageBitmap containing the generated QR code
     */
    fun generateQRCode(content: String, size: Int = 512): ImageBitmap
}

/**
 * Creates a platform-specific QRCodeGenerator instance.
 */
expect fun createQRCodeGenerator(): QRCodeGenerator
