package com.knthcame.myhealthkmp.ui.diary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.knthcame.myhealthkmp.ui.common.DefaultScaffold
import com.knthcame.myhealthkmp.ui.common.formatWithCurrentLocale
import com.knthcame.myhealthkmp.ui.common.mutableStateMapSaver
import com.knthcame.myhealthkmp.ui.theme.myHealthColorScheme
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import myhealthkmp.shared.generated.resources.Res
import myhealthkmp.shared.generated.resources.action_cancel
import myhealthkmp.shared.generated.resources.action_delete
import myhealthkmp.shared.generated.resources.diary_add_event_title
import myhealthkmp.shared.generated.resources.diary_delete_event_message
import myhealthkmp.shared.generated.resources.diary_delete_event_title
import myhealthkmp.shared.generated.resources.diary_empty_list_header
import myhealthkmp.shared.generated.resources.diary_empty_list_message
import myhealthkmp.shared.generated.resources.diary_header
import myhealthkmp.shared.generated.resources.diary_title
import myhealthkmp.shared.generated.resources.ic_add
import myhealthkmp.shared.generated.resources.ic_delete
import myhealthkmp.shared.generated.resources.ic_empty_list
import myhealthkmp.shared.generated.resources.ic_keyboard_arrow_down
import myhealthkmp.shared.generated.resources.ic_keyboard_arrow_up
import myhealthkmp.shared.generated.resources.ic_warning
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiaryScreenRoute(
    onNavigationIconClick: () -> Unit,
    onFABClicked: () -> Unit,
    viewModel: DiaryViewModel = koinViewModel(),
) {
    val events by viewModel.groupedDiaryEvents.collectAsState()

    DiaryScreen(
        onNavigationIconClick = onNavigationIconClick,
        onFABClicked = onFABClicked,
        groupedDiaryEvents = events,
        onDeleteEventConfirmed = { event -> viewModel.delete(event) }
    )
}

@Composable
fun DiaryScreen(
    onNavigationIconClick: () -> Unit,
    onFABClicked: () -> Unit,
    groupedDiaryEvents: Map<LocalDate, List<DiaryUIEvent>>,
    onDeleteEventConfirmed: (DiaryUIEvent) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val shouldCollapseFAB = lazyListState.canScrollBackward || lazyListState.isScrollInProgress

    DefaultScaffold(
        topBarTitle = stringResource(Res.string.diary_title),
        onNavigationIconClick = onNavigationIconClick,
        floatingActionButton = {
            AddEventButton(
                onClick = onFABClicked,
                expanded = !shouldCollapseFAB,
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                stringResource(Res.string.diary_header),
                style = MaterialTheme.typography.headlineMedium
            )

            DiaryEventsList(
                groupedDiaryEvents = groupedDiaryEvents,
                lazyListState = lazyListState,
                onDeleteEventConfirmed = onDeleteEventConfirmed,
                contentPadding = PaddingValues(bottom = 80.dp),
            )
        }
    }
}

@Composable
private fun AddEventButton(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = {
            Text(stringResource(Res.string.diary_add_event_title))
        },
        icon = {
            Icon(painterResource(Res.drawable.ic_add), null)
        },
        onClick = onClick,
        modifier = modifier,
        expanded = expanded,
    )
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ColumnScope.DiaryEventsList(
    groupedDiaryEvents: Map<LocalDate, List<DiaryUIEvent>>,
    lazyListState: LazyListState,
    onDeleteEventConfirmed: (DiaryUIEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    if (groupedDiaryEvents.isEmpty()) {
        EmptyDiaryView()
        return
    }

    val expandedStateSaver = mutableStateMapSaver<LocalDate, Boolean>()

    val expandedState: MutableMap<LocalDate, Boolean> by rememberSaveable(stateSaver = expandedStateSaver) {
        mutableStateOf(mutableStateMapOf())
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        groupedDiaryEvents.onEachIndexed { index, (localDate, events) ->
            val expanded = expandedState[localDate] ?: (index == 0)
            stickyHeader(key = localDate.toEpochDays()) {
                DateHeader(
                    localDate = localDate,
                    expanded = expanded,
                    onExpansionIconClick = {
                        expandedState[localDate] = !expanded
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 4.dp)
                )
            }

            items(
                items = events,
                key = { event ->
                    "${event.eventType}:${event.eventId}"
                },
            ) { event ->
                AnimatedVisibility(expanded) {
                    DiaryEventItem(
                        event = event,
                        onDeleteEventConfirmed = onDeleteEventConfirmed,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyDiaryView() {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_empty_list),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )
        Text(
            text = stringResource(Res.string.diary_empty_list_header),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(stringResource(Res.string.diary_empty_list_message), textAlign = TextAlign.Center)
    }
}

@Composable
private fun DateHeader(
    localDate: LocalDate,
    expanded: Boolean,
    onExpansionIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        val color = MaterialTheme.colorScheme.secondary
        ListItem(
            headlineContent = {
                Text(
                    text = localDate.formatWithCurrentLocale(),
                    style = MaterialTheme.typography.titleMedium.copy(color),
                    color = color,
                )
            },
            trailingContent = {
                Crossfade(targetState = expanded) { expanded ->
                    Icon(
                        painter = painterResource(
                            resource = if (expanded) Res.drawable.ic_keyboard_arrow_up else Res.drawable.ic_keyboard_arrow_down
                        ),
                        contentDescription = null,
                        tint = color
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            modifier = Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = onExpansionIconClick
            ),
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun DiaryEventItem(
    event: DiaryUIEvent,
    onDeleteEventConfirmed: (DiaryUIEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var deleteEventConfirmationDialogIsVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            DiaryEventDismissBackground(dismissState)
        },
        onDismiss = { swipeToDismissBoxValue ->
            deleteEventConfirmationDialogIsVisible =
                swipeToDismissBoxValue == SwipeToDismissBoxValue.EndToStart
        },
        modifier = modifier,
    ) {
        DiaryEventItemContent(event)
    }

    if (deleteEventConfirmationDialogIsVisible) {
        DeleteEventConfirmationDialog(
            event = event,
            onDismissRequest = {
                deleteEventConfirmationDialogIsVisible = false
                scope.launch { dismissState.reset() }
            },
            onDeleteEventConfirmed = onDeleteEventConfirmed,
        )
    }
}

@Composable
private fun DiaryEventDismissBackground(dismissState: SwipeToDismissBoxState) {
    val backgroundColor = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Transparent
    }
    val deleteIconColorIsVisible =
        dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart

    Row(
        modifier = Modifier.fillMaxSize().background(backgroundColor).padding(
            horizontal = 16.dp, vertical = 8.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(modifier = Modifier)
        if (deleteIconColorIsVisible) {
            Icon(
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = "delete Diary event",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun DiaryEventItemContent(
    event: DiaryUIEvent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            headlineContent = {
                Text(event.value)
            },
            leadingContent = {
                Icon(
                    painter = painterResource(event.eventType.icon),
                    contentDescription = null,
                    tint = event.eventType.color,
                    modifier = Modifier.size(24.dp),
                )
            },
            supportingContent = {
                Text(event.eventType.eventName)
            },
            trailingContent = {
                Text(event.localTime.formatWithCurrentLocale())
            })

        HorizontalDivider()
    }
}

@Composable
private fun DeleteEventConfirmationDialog(
    event: DiaryUIEvent,
    onDismissRequest: () -> Unit,
    onDeleteEventConfirmed: (DiaryUIEvent) -> Unit,
) {
    AlertDialog(onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(
            onClick = {
                onDeleteEventConfirmed(event)
                onDismissRequest()
            },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text(stringResource(Res.string.action_delete))
        }
    }, dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text(stringResource(Res.string.action_cancel))
        }
    }, icon = {
        Icon(
            painter = painterResource(Res.drawable.ic_warning),
            contentDescription = null,
            tint = MaterialTheme.myHealthColorScheme.warning,
        )
    }, title = {
        Text(stringResource(Res.string.diary_delete_event_title))
    }, text = {
        Text(
            stringResource(
                Res.string.diary_delete_event_message,
                event.eventType.eventName.lowercase(),
                event.localDate.formatWithCurrentLocale(),
                event.localTime.formatWithCurrentLocale(),
            )
        )
    })
}