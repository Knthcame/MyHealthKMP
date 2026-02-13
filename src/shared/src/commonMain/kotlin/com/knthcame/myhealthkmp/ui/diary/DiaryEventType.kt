package com.knthcame.myhealthkmp.ui.diary

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.ui.theme.myHealthColorScheme
import myhealthkmp.shared.generated.resources.Res
import myhealthkmp.shared.generated.resources.diary_activity_event_name
import myhealthkmp.shared.generated.resources.diary_sleep_event_name
import myhealthkmp.shared.generated.resources.ic_activity
import myhealthkmp.shared.generated.resources.ic_sleep
import myhealthkmp.shared.generated.resources.unit_hours
import myhealthkmp.shared.generated.resources.unit_minutes
import org.jetbrains.compose.resources.stringResource

val DiaryEvent.Type.icon
    get() = when (this) {
        DiaryEvent.Type.Activity -> Res.drawable.ic_activity
        DiaryEvent.Type.Sleep -> Res.drawable.ic_sleep
    }

val DiaryEvent.Type.eventName
    @Composable
    get() = when (this) {
        DiaryEvent.Type.Activity -> stringResource(Res.string.diary_activity_event_name)
        DiaryEvent.Type.Sleep -> stringResource(Res.string.diary_sleep_event_name)
    }

val DiaryEvent.Type.unit
    @Composable
    get() = when (this) {
        DiaryEvent.Type.Activity -> stringResource(Res.string.unit_minutes)
        DiaryEvent.Type.Sleep -> stringResource(Res.string.unit_hours)
    }

val DiaryEvent.Type.color
    @Composable
    get() = when (this) {
        DiaryEvent.Type.Activity -> MaterialTheme.myHealthColorScheme.activity
        DiaryEvent.Type.Sleep -> MaterialTheme.myHealthColorScheme.sleep
    }