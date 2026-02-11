package com.knthcame.myhealthkmp.ui.diary

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.ui.common.DefaultScaffold
import com.knthcame.myhealthkmp.ui.common.formatWithCurrentLocale
import com.knthcame.myhealthkmp.ui.common.resolveMonoChromeFromLuminance
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import myhealthkmp.shared.generated.resources.Res
import myhealthkmp.shared.generated.resources.action_cancel
import myhealthkmp.shared.generated.resources.action_ok
import myhealthkmp.shared.generated.resources.action_save
import myhealthkmp.shared.generated.resources.add_event_activity_text_field_title
import myhealthkmp.shared.generated.resources.add_event_date_picker_title
import myhealthkmp.shared.generated.resources.add_event_sleep_text_field_title
import myhealthkmp.shared.generated.resources.add_event_time_picker_title
import myhealthkmp.shared.generated.resources.add_event_title
import myhealthkmp.shared.generated.resources.add_event_type_picker_title
import myhealthkmp.shared.generated.resources.ic_clock
import myhealthkmp.shared.generated.resources.ic_date_range
import myhealthkmp.shared.generated.resources.placeholder
import myhealthkmp.shared.generated.resources.validation_error_invalid_number
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEventScreenRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: AddEventViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigationIconClick()
    }

    AddEventScreen(
        onNavigationIconClick = onNavigationIconClick,
        uiState = uiState,
        onDateSelected = { utcMillisecondsSinceEpoch ->
            viewModel.editEventDate(utcMillisecondsSinceEpoch)
        },
        onTimeSelected = { hour, minute -> viewModel.editEventTime(hour, minute) },
        onEditEntryType = { entryType -> viewModel.editEventType(entryType) },
        onEditValue = { value -> viewModel.editValue(value) },
        onSave = { viewModel.saveEvent() },
    )
}

@Composable
fun AddEventScreen(
    onNavigationIconClick: () -> Unit,
    uiState: DiaryEventUiState,
    onDateSelected: (millisecondsSinceEpoch: Long) -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onEditEntryType: (DiaryEvent.Type) -> Unit,
    onEditValue: (String) -> Unit,
    onSave: () -> Unit,
) {
    DefaultScaffold(
        topBarTitle = stringResource(Res.string.add_event_title),
        onNavigationIconClick = onNavigationIconClick,
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EventDatePicker(
                date = uiState.entryDate,
                maxSelectableDate = uiState.maxSelectableDate,
                onValueSelected = onDateSelected,
            )

            EventTimePicker(
                time = uiState.entryTime,
                onValueSelected = onTimeSelected,
            )

            EventTypePicker(
                selectedEntryType = uiState.entryType,
                onEditEntryType = onEditEntryType,
            )

            EventValueTextField(
                value = uiState.value,
                entryType = uiState.entryType,
                isError = !uiState.valueIsValid,
                errorMessage = getErrorMessage(uiState.valueError),
                onValueChange = onEditValue,
            )

            Spacer(Modifier.weight(1f))

            SaveEventButton(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isSaveEnabled,
            )
        }
    }
}

@Composable
private fun getErrorMessage(errorType: AddEventValidationError) = when (errorType) {
    AddEventValidationError.None -> null
    AddEventValidationError.InvalidNumber -> stringResource(Res.string.validation_error_invalid_number)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/**
 * A readonly [TextField] that shows a [DatePickerDialog] when focused.
 *
 * @param date the date shown on the text field.
 * @param maxSelectableDate the maximum date that can be selected.
 * @param onValueSelected function executed when a date is selected and confirmed.
 */
private fun EventDatePicker(
    date: LocalDate,
    maxSelectableDate: LocalDate,
    onValueSelected: (millisecondsSinceEpoch: Long) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    Column {
        Text(stringResource(Res.string.add_event_date_picker_title))
        TextField(
            value = date.formatWithCurrentLocale(),
            onValueChange = { },
            trailingIcon = {
                Icon(painterResource(Res.drawable.ic_date_range), null)
            },
            readOnly = true,
            modifier = Modifier.onFocusChanged { focusState ->
                showDatePicker = focusState.isFocused
            }.fillMaxWidth(),
        )
    }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableYear(year: Int): Boolean {
            return year <= maxSelectableDate.year
        }

        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val utcMaxTimeMillis = LocalDateTime(
                date = maxSelectableDate,
                time = LocalTime(23, 59, 59),
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

            return utcTimeMillis <= utcMaxTimeMillis
        }
    })
    val focusManager = LocalFocusManager.current
    val onDismiss = {
        showDatePicker = false
        focusManager.clearFocus()
    }

    if (showDatePicker) {
        DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let(onValueSelected)
                onDismiss()
            }) {
                Text(stringResource(Res.string.action_ok))
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.action_cancel))
            }
        }) {
            DatePicker(datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
/**
 * A readonly [TextField] that shows a [TimePicker] inside an [AlertDialog] when focused.
 *
 * @param time the time of day shown on the text field.
 * @param onValueSelected function executed when a time of day is selected and confirmed.
 */
private fun EventTimePicker(
    time: LocalTime,
    onValueSelected: (hour: Int, minute: Int) -> Unit,
) {
    var showTimePicker by remember { mutableStateOf(false) }
    Column {
        Text(stringResource(Res.string.add_event_time_picker_title))
        TextField(
            value = time.formatWithCurrentLocale(),
            onValueChange = { },
            trailingIcon = {
                Icon(painterResource(Res.drawable.ic_clock), null)
            },
            readOnly = true,
            modifier = Modifier.fillMaxWidth().onFocusChanged { focusState ->
                showTimePicker = focusState.isFocused
            },
        )
    }

    val timePickerState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
    )
    val focusManager = LocalFocusManager.current
    val onDismiss = {
        showTimePicker = false
        focusManager.clearFocus()
    }

    if (showTimePicker) {
        AlertDialog(onDismissRequest = onDismiss, confirmButton = {
            TextButton(onClick = {
                onValueSelected(timePickerState.hour, timePickerState.minute)
                onDismiss()
            }) {
                Text(stringResource(Res.string.action_ok))
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.action_cancel))
            }
        }, text = {
            TimePicker(timePickerState)
        })
    }
}

@Composable
fun EventTypePicker(
    selectedEntryType: DiaryEvent.Type,
    onEditEntryType: (DiaryEvent.Type) -> Unit,
) {
    Column {
        Text(stringResource(Res.string.add_event_type_picker_title))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DiaryEvent.Type.entries) { item ->
                EventTypeItem(
                    selectedEntryType = selectedEntryType,
                    item = item,
                    onEditEntryType = onEditEntryType,
                )
            }
        }
    }
}

@Composable
private fun EventTypeItem(
    selectedEntryType: DiaryEvent.Type,
    item: DiaryEvent.Type,
    onEditEntryType: (DiaryEvent.Type) -> Unit,
) {
    val isSelected by derivedStateOf { selectedEntryType == item }
    val backgroundColor by animateColorAsState(
        if (isSelected) item.color
        else MaterialTheme.colorScheme.surfaceVariant
    )
    Card(
        onClick = { onEditEntryType(item) },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            val iconColor by animateColorAsState(
                if (isSelected) resolveMonoChromeFromLuminance(backgroundColor)
                else item.color
            )
            Icon(
                painter = painterResource(item.icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            val textColor by animateColorAsState(
                if (isSelected) resolveMonoChromeFromLuminance(backgroundColor)
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(item.eventName, color = textColor)
        }
    }
}

@Composable
private fun EventValueTextField(
    value: String,
    entryType: DiaryEvent.Type,
    isError: Boolean,
    errorMessage: String?,
    onValueChange: (String) -> Unit,
) {
    Column {
        val titleResource = when (entryType) {
            DiaryEvent.Type.Activity -> Res.string.add_event_activity_text_field_title
            DiaryEvent.Type.Sleep -> Res.string.add_event_sleep_text_field_title
        }

        Text(stringResource(titleResource))

        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            supportingText = errorMessage?.let {
                { Text(errorMessage) }
            },
            placeholder = {
                Text(stringResource(Res.string.placeholder))
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            trailingIcon = {
                Text(entryType.unit)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SaveEventButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(
            text = stringResource(Res.string.action_save),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}