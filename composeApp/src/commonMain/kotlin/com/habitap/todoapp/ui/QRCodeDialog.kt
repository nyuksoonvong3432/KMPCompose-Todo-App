package com.habitap.todoapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.habitap.todoapp.model.Todo
import com.habitap.todoapp.qrcode.createQRCodeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Dialog that displays a QR code for a todo item.
 * The QR code encodes both the title and description of the todo.
 *
 * @param todo The todo item to generate a QR code for
 * @param onDismiss Callback invoked when the dialog is dismissed
 */
@Composable
fun QRCodeDialog(
    todo: Todo,
    onDismiss: () -> Unit
) {
    var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(todo) {
        try {
            isLoading = true
            errorMessage = null

            // Generate QR code content from todo
            val qrContent = buildQRContent(todo)

            // Debug: Log the content being encoded
            println("QR Code Content: $qrContent")

            // Generate QR code on background dispatcher
            val bitmap = withContext(Dispatchers.Default) {
                val generator = createQRCodeGenerator()
                generator.generateQRCode(qrContent, size = 512)
            }

            qrCodeBitmap = bitmap
        } catch (e: Exception) {
            errorMessage = "Failed to generate QR code: ${e.message}"
            println("QR Code generation error: $e")
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Todo QR Code",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Todo title
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // QR Code display or loading/error state
                when {
                    isLoading -> {
                        LoadingPlaceholder()
                    }
                    errorMessage != null -> {
                        ErrorMessage(errorMessage!!)
                    }
                    qrCodeBitmap != null -> {
                        QRCodeImage(qrCodeBitmap!!)
                        QRCodeDescription()
                    }
                }

                // Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

/**
 * Builds the QR code content string from a todo item.
 * Encodes the todo as JSON with title and description fields.
 */
private fun buildQRContent(todo: Todo): String {
    val escapedTitle = todo.title.escapeJson()
    val escapedDesc = todo.description.escapeJson()
    return """{"title":"$escapedTitle","desc":"$escapedDesc"}"""
}

/**
 * Escapes special characters for JSON string values.
 */
private fun String.escapeJson(): String {
    return this
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
}

/**
 * Displays the QR code image.
 */
@Composable
private fun QRCodeImage(bitmap: ImageBitmap) {
    Card(
        modifier = Modifier.size(280.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = "QR Code",
            modifier = Modifier
                .size(280.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

/**
 * Displays a loading placeholder while the QR code is being generated.
 */
@Composable
private fun LoadingPlaceholder() {
    Column(
        modifier = Modifier
            .size(280.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Generating QR Code...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Displays an error message when QR code generation fails.
 */
@Composable
private fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.errorContainer,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

/**
 * Displays a description about the QR code.
 */
@Composable
private fun QRCodeDescription() {
    Text(
        text = "Scan this QR code to view the todo details",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
