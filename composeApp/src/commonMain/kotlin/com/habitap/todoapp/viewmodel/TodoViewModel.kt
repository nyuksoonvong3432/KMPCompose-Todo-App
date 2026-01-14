package com.habitap.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.habitap.todoapp.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for managing todo list state and operations.
 * Follows MVVM pattern with StateFlow for reactive UI updates.
 */
class TodoViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    /**
     * Adds a new todo to the list.
     *
     * @param title The title of the todo to create
     */
    fun addTodo(title: String) {
        if (title.isBlank()) return

        val newTodo = Todo(title = title.trim())
        _todos.update { currentList -> currentList + newTodo }
    }

    /**
     * Updates an existing todo's title.
     *
     * @param todoId The ID of the todo to update
     * @param newTitle The new title for the todo
     */
    fun updateTodo(todoId: String, newTitle: String) {
        if (newTitle.isBlank()) return

        _todos.update { currentList ->
            currentList.map { todo ->
                if (todo.id == todoId) {
                    todo.copy(title = newTitle.trim())
                } else {
                    todo
                }
            }
        }
    }

    /**
     * Toggles the completion status of a todo.
     *
     * @param todoId The ID of the todo to toggle
     */
    fun toggleTodoComplete(todoId: String) {
        _todos.update { currentList ->
            currentList.map { todo ->
                if (todo.id == todoId) {
                    todo.toggleComplete()
                } else {
                    todo
                }
            }
        }
    }

    /**
     * Deletes a todo from the list.
     *
     * @param todoId The ID of the todo to delete
     */
    fun deleteTodo(todoId: String) {
        _todos.update { currentList ->
            currentList.filterNot { it.id == todoId }
        }
    }
}
