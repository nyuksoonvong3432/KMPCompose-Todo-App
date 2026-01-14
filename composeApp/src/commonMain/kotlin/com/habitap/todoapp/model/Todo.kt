package com.habitap.todoapp.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Data model representing a Todo item.
 *
 * @property id Unique identifier for the todo item
 * @property title The title of the todo
 * @property description Optional description providing additional details
 * @property tags List of text tags for categorization
 * @property isCompleted Whether the todo has been completed
 */
@OptIn(ExperimentalUuidApi::class)
data class Todo(
    val id: String = Uuid.random().toString(),
    val title: String,
    val description: String = "",
    val tags: List<String> = emptyList(),
    val isCompleted: Boolean = false
) {
    /**
     * Creates a copy of this todo with the completion status toggled.
     */
    fun toggleComplete(): Todo = copy(isCompleted = !isCompleted)
}
