package com.habitap.todoapp.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Data model representing a Todo item.
 *
 * @property id Unique identifier for the todo item
 * @property title The title/description of the todo
 * @property isCompleted Whether the todo has been completed
 */
@OptIn(ExperimentalUuidApi::class)
data class Todo(
    val id: String = Uuid.random().toString(),
    val title: String,
    val isCompleted: Boolean = false
) {
    /**
     * Creates a copy of this todo with the completion status toggled.
     */
    fun toggleComplete(): Todo = copy(isCompleted = !isCompleted)
}
