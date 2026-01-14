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
     * @param description Optional description for the todo
     * @param tags List of tags for categorization
     */
    fun addTodo(title: String, description: String = "", tags: List<String> = emptyList()) {
        if (title.isBlank()) return

        val newTodo = Todo(
            title = title.trim(),
            description = description.trim(),
            tags = tags.map { it.trim() }.filter { it.isNotBlank() }
        )
        _todos.update { currentList -> currentList + newTodo }
    }

    /**
     * Updates an existing todo with all fields.
     *
     * @param todoId The ID of the todo to update
     * @param newTitle The new title for the todo
     * @param newDescription The new description for the todo
     * @param newTags The new list of tags for the todo
     */
    fun updateTodo(
        todoId: String,
        newTitle: String,
        newDescription: String = "",
        newTags: List<String> = emptyList()
    ) {
        if (newTitle.isBlank()) return

        _todos.update { currentList ->
            currentList.map { todo ->
                if (todo.id == todoId) {
                    todo.copy(
                        title = newTitle.trim(),
                        description = newDescription.trim(),
                        tags = newTags.map { it.trim() }.filter { it.isNotBlank() }
                    )
                } else {
                    todo
                }
            }
        }
    }

    /**
     * Finds a todo by its ID.
     *
     * @param todoId The ID of the todo to find
     * @return The todo if found, null otherwise
     */
    fun getTodoById(todoId: String): Todo? = _todos.value.find { it.id == todoId }

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
