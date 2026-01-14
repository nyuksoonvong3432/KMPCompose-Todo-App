package com.habitap.todoapp.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.habitap.todoapp.ui.AddEditTodoScreen
import com.habitap.todoapp.ui.TodoScreen
import com.habitap.todoapp.viewmodel.TodoViewModel
import kotlinx.serialization.Serializable

@Serializable
object TodoList

@Serializable
object AddTodo

@Serializable
data class EditTodo(val todoId: String)

@Composable
fun TodoNavHost(
    navController: NavHostController,
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TodoList,
        modifier = modifier,
        enterTransition = { slideInHorizontally() },
        exitTransition = { slideOutHorizontally() }
    ) {
        // Todo list screen
        composable<TodoList> {
            TodoScreen(
                viewModel = viewModel,
                onAddTodoClick = {
                    navController.navigate(AddTodo)
                },
                onEditTodoClick = { todoId ->
                    navController.navigate(EditTodo(todoId))
                }
            )
        }

        // Add new todo screen
        composable<AddTodo>(
            deepLinks = listOf(
                navDeepLink { uriPattern = "demo://open-todo-view" }
            )
        ) {
            AddEditTodoScreen(
                todoToEdit = null,
                onSave = { title, description, tags ->
                    viewModel.addTodo(title, description, tags)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        // Edit existing todo screen
        composable<EditTodo> { backStackEntry ->
            val todoId = backStackEntry.toRoute<EditTodo>().todoId
            val todo = todoId?.let { viewModel.getTodoById(it) }
            AddEditTodoScreen(
                todoToEdit = todo,
                onSave = { title, description, tags ->
                    todoId?.let {
                        viewModel.updateTodo(it, title, description, tags)
                    }
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}
