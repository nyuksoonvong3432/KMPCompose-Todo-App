package com.habitap.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.habitap.todoapp.navigation.ExternalUriHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Handle deep link if activity was launched with one
        handleDeepLink(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Handle deep link when app is already running
        handleDeepLink(intent)
    }

    /**
     * Processes incoming deep link intents and forwards them to the ExternalUriHandler.
     *
     * @param intent The intent that may contain a deep link URI
     */
    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.toString()?.let { uri ->
            ExternalUriHandler.onNewUri(uri)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}