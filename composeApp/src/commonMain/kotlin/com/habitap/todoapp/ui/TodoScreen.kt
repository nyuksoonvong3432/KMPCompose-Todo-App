package com.habitap.todoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.habitap.todoapp.model.Todo
import com.habitap.todoapp.viewmodel.TodoViewModel

/**
 * Main screen displaying the todo list with add, edit, and delete functionality.
 *
 * @param modifier Modifier to be applied to the root composable
 * @param viewModel The ViewModel managing todo state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel { TodoViewModel() }
) {
    val todos by viewModel.todos.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Todo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Input section
            AddTodoSection(
                inputText = inputText,
                onInputChange = { inputText = it },
                onAddClick = {
                    viewModel.addTodo(inputText)
                    inputText = ""
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Todo list
            if (todos.isEmpty()) {
                EmptyStateMessage()
            } else {
                TodoList(
                    todos = todos,
                    onToggleComplete = viewModel::toggleTodoComplete,
                    onDelete = viewModel::deleteTodo,
                    onUpdate = viewModel::updateTodo
                )
            }
        }
    }
}

/**
 * Section for adding new todos.
 */
@Composable
private fun AddTodoSection(
    inputText: String,
    onInputChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter a new todo...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = onAddClick,
            enabled = inputText.isNotBlank(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Add")
        }
    }
}

/**
 * Displays an empty state message when there are no todos.
 */
@Composable
private fun EmptyStateMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No todos yet!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add your first todo above",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Scrollable list of todos.
 */
@Composable
private fun TodoList(
    todos: List<Todo>,
    onToggleComplete: (String) -> Unit,
    onDelete: (String) -> Unit,
    onUpdate: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(todos, key = { it.id }) { todo ->
            TodoItem(
                todo = todo,
                onToggleComplete = { onToggleComplete(todo.id) },
                onDelete = { onDelete(todo.id) },
                onUpdate = { newTitle -> onUpdate(todo.id, newTitle) }
            )
        }
    }
}

/**
 * Individual todo item with checkbox, title, and action buttons.
 */
@Composable
private fun TodoItem(
    todo: Todo,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(todo.title) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (isEditing) {
            // Edit mode
            EditTodoContent(
                editText = editText,
                onEditTextChange = { editText = it },
                onSave = {
                    onUpdate(editText)
                    isEditing = false
                },
                onCancel = {
                    editText = todo.title
                    isEditing = false
                }
            )
        } else {
            // Display mode
            DisplayTodoContent(
                todo = todo,
                onToggleComplete = onToggleComplete,
                onEdit = { isEditing = true },
                onDelete = onDelete
            )
        }
    }
}

/**
 * Edit mode content for a todo item.
 */
@Composable
private fun EditTodoContent(
    editText: String,
    onEditTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = editText,
            onValueChange = onEditTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Edit todo...") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSave,
                enabled = editText.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}

/**
 * Display mode content for a todo item.
 */
@Composable
private fun DisplayTodoContent(
    todo: Todo,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggleComplete() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = todo.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (todo.isCompleted) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
            color = if (todo.isCompleted) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        TextButton(onClick = onEdit) {
            Text("Edit", maxLines = 1)
        }

        TextButton(onClick = onDelete) {
            Text("Delete", maxLines = 1)
        }
    }
}
