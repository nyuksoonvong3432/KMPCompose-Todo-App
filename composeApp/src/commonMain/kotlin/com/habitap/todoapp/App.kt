package com.habitap.todoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.habitap.todoapp.navigation.TodoNavHost
import com.habitap.todoapp.viewmodel.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main app composable that sets up the navigation and theme.
 * Uses Jetpack Compose Navigation for Kotlin Multiplatform.
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val viewModel: TodoViewModel = viewModel { TodoViewModel() }

        TodoNavHost(
            navController = navController,
            viewModel = viewModel
        )
    }
}