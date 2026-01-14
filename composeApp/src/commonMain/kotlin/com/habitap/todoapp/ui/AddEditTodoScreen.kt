package com.habitap.todoapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.habitap.todoapp.model.Todo

/**
 * Screen for adding a new todo or editing an existing one.
 * Supports title, description, and tags.
 *
 * @param todoToEdit The todo to edit, or null if creating a new todo
 * @param onSave Callback invoked when user saves the todo
 * @param onCancel Callback invoked when user cancels the operation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    todoToEdit: Todo? = null,
    onSave: (title: String, description: String, tags: List<String>) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(todoToEdit?.title ?: "") }
    var description by remember { mutableStateOf(todoToEdit?.description ?: "") }
    var tags by remember { mutableStateOf(todoToEdit?.tags ?: emptyList()) }
    var tagInput by remember { mutableStateOf("") }

    val isEditMode = todoToEdit != null
    val screenTitle = if (isEditMode) "Edit Todo" else "Add Todo"

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
                navigationIcon = {
                    TextButton(onClick = onCancel) {
                        Text("Back")
                    }
                },
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                placeholder = { Text("Enter todo title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (Optional)") },
                placeholder = { Text("Enter description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            // Tags section
            TagsSection(
                tags = tags,
                tagInput = tagInput,
                onTagInputChange = { tagInput = it },
                onAddTag = {
                    if (tagInput.isNotBlank() && !tags.contains(tagInput.trim())) {
                        tags = tags + tagInput.trim()
                        tagInput = ""
                    }
                },
                onRemoveTag = { tagToRemove ->
                    tags = tags.filterNot { it == tagToRemove }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action buttons
            ActionButtons(
                onSave = {
                    if (title.isNotBlank()) {
                        onSave(title, description, tags)
                    }
                },
                onCancel = onCancel,
                isSaveEnabled = title.isNotBlank()
            )
        }
    }
}

/**
 * Section for managing tags with input field and tag chips.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsSection(
    tags: List<String>,
    tagInput: String,
    onTagInputChange: (String) -> Unit,
    onAddTag: () -> Unit,
    onRemoveTag: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tags",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Tag input field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = tagInput,
                onValueChange = onTagInputChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Add a tag") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedButton(
                onClick = onAddTag,
                enabled = tagInput.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add")
            }
        }

        // Display existing tags
        if (tags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        onRemove = { onRemoveTag(tag) }
                    )
                }
            }
        }
    }
}

/**
 * Individual tag chip with remove button.
 */
@Composable
private fun TagChip(
    tag: String,
    onRemove: () -> Unit
) {
    SuggestionChip(
        onClick = onRemove,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(tag)
                Text(
                    text = "×",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable(onClick = onRemove)
                )
            }
        },
        shape = RoundedCornerShape(8.dp)
    )
}

/**
 * Action buttons for save and cancel operations.
 */
@Composable
private fun ActionButtons(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isSaveEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cancel")
        }

        Button(
            onClick = onSave,
            enabled = isSaveEnabled,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save")
        }
    }
}
