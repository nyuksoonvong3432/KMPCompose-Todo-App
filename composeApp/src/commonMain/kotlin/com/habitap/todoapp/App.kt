package com.habitap.todoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.habitap.todoapp.navigation.ExternalUriHandler
import com.habitap.todoapp.navigation.TodoDestination
import com.habitap.todoapp.navigation.TodoNavHost
import com.habitap.todoapp.viewmodel.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main app composable that sets up the navigation and theme.
 * Uses Jetpack Compose Navigation for Kotlin Multiplatform.
 *
 * Integrates deep link handling through [ExternalUriHandler] to support
 * navigation from external sources like custom URL schemes.
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val viewModel: TodoViewModel = viewModel { TodoViewModel() }

        // Set up deep link listener
        DisposableEffect(Unit) {
            ExternalUriHandler.listener = { uri ->
                // Parse the deep link URI and navigate to the appropriate destination
                when {
                    uri.contains("open-todo-view") -> {
                        navController.navigate(TodoDestination.AddTodo.route)
                    }
                }
            }
            onDispose {
                ExternalUriHandler.listener = null
            }
        }

        TodoNavHost(
            navController = navController,
            viewModel = viewModel
        )
    }
}