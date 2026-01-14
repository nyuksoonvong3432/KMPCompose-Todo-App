package com.habitap.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.habitap.todoapp.ui.AddEditTodoScreen
import com.habitap.todoapp.ui.TodoScreen
import com.habitap.todoapp.viewmodel.TodoViewModel

/**
 * Sealed interface representing navigation destinations in the app.
 * Uses type-safe navigation with compile-time checked routes.
 */
sealed interface TodoDestination {
    val route: String

    /**
     * Todo list screen - the main screen showing all todos.
     */
    data object TodoList : TodoDestination {
        override val route: String = "todo_list"
    }

    /**
     * Add new todo screen.
     */
    data object AddTodo : TodoDestination {
        override val route: String = "add_todo"
    }

    /**
     * Edit existing todo screen with todo ID parameter.
     */
    data object EditTodo : TodoDestination {
        override val route: String = "edit_todo"
        const val TODO_ID_ARG = "todoId"
        val routeWithArgs: String = "$route/{$TODO_ID_ARG}"

        /**
         * Creates a navigation route with the specified todo ID.
         *
         * @param todoId The ID of the todo to edit
         * @return The complete route string with the todo ID
         */
        fun createRoute(todoId: String): String = "$route/$todoId"
    }
}

/**
 * Sets up the navigation graph for the Todo app.
 *
 * @param navController The navigation controller managing navigation state
 * @param viewModel The shared TodoViewModel instance
 * @param modifier Modifier to be applied to the NavHost
 */
@Composable
fun TodoNavHost(
    navController: NavHostController,
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TodoDestination.TodoList.route,
        modifier = modifier
    ) {
        // Todo list screen
        composable(route = TodoDestination.TodoList.route) {
            TodoScreen(
                viewModel = viewModel,
                onAddTodoClick = {
                    navController.navigate(TodoDestination.AddTodo.route)
                },
                onEditTodoClick = { todoId ->
                    navController.navigate(TodoDestination.EditTodo.createRoute(todoId))
                }
            )
        }

        // Add new todo screen
        composable(route = TodoDestination.AddTodo.route) {
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
        composable(
            route = TodoDestination.EditTodo.routeWithArgs,
            arguments = listOf(
                navArgument(TodoDestination.EditTodo.TODO_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString(TodoDestination.EditTodo.TODO_ID_ARG)
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
