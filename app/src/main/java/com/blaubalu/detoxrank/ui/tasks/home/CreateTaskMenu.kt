package com.blaubalu.detoxrank.ui.tasks.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TaskIconCategory
import com.blaubalu.detoxrank.ui.tasks.task.TaskUiState
import com.blaubalu.detoxrank.ui.tasks.task.TaskViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.getIcon
import kotlinx.coroutines.launch

/**
 * UI to create a custom task
 */
@ExperimentalMaterial3Api
@Composable
fun CreateTaskMenu(
    viewModel: TasksHomeViewModel,
    taskViewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    var wasClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    AnimatedVisibility(
        viewModel.createTaskMenuShown,
        enter = slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> height },
        exit = slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height }
    ) {
        BackHandler {
            viewModel.invertCreateTaskMenuShownValue()
        }
        Column(
            modifier = modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .border(BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(start = 30.dp)
        ) {
            val context = LocalContext.current
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(end = 3.dp, top = 3.dp)
            ) {
                Text(
                    text = stringResource(R.string.custom_task_label),
                    style = Typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(top = 5.dp)
                )
                IconButton(
                    onClick = {
                        viewModel.invertCreateTaskMenuShownValue()
                        wasClicked = false
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.Top)
                            .size(32.dp)
                    )
                }
            }
            CategoryDropdownMenu(
                options = TaskIconCategory.entries.toTypedArray(),
                viewModel = viewModel,
                modifier = Modifier.padding(top = 20.dp, end = 30.dp)
            )
            OutlinedTextField(
                viewModel.createdTaskDescription.value,
                onValueChange = { if (it.length < 100) viewModel.setCreatedTaskDescription(it) },
                label = { Text(stringResource(R.string.task)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, end = 30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.outline
                ),
                maxLines = 3,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                isError = viewModel.createdTaskDescription.value.trim() == "" && wasClicked
            )

            FilledIconButton(
                onClick = {
                    if (viewModel.createdTaskDescription.value.trim() == "") {
                        wasClicked = true
                        Toast.makeText(context, "Task is empty!", Toast.LENGTH_SHORT).show()
                    } else {
                        wasClicked = false
                        taskViewModel.updateUiState(
                            TaskUiState(
                                description = viewModel.createdTaskDescription.value.trim(),
                                completed = false,
                                durationCategory = TaskDurationCategory.Uncategorized,
                                iconCategory = viewModel.createdTaskSelectedIcon.value,
                                selectedAsCurrentTask = true
                            )
                        )
                        coroutineScope.launch {
                            taskViewModel.insertTaskToDatabase()
                        }
                        viewModel.invertCreateTaskMenuShownValue()
                        viewModel.setCreatedTaskDescription("")
                    }
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, end = 30.dp)
                    .height(60.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Create,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(18.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        stringResource(R.string.create_task_label),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun CategoryDropdownMenu(
    options: Array<TaskIconCategory>,
    viewModel: TasksHomeViewModel,
    modifier: Modifier = Modifier
) {
    val selectedItem = viewModel.createdTaskSelectedIcon.value
//    var selectedItem by remember { mutableStateOf(TaskIconCategory.Health) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedItem.name,
            onValueChange = {},
            label = { Text(stringResource(R.string.category)) },
            leadingIcon = { Icon(getIcon(selectedItem), contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(250.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        viewModel.setCreatedTaskIcon(selectionOption)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            getIcon(selectionOption),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

    }
}