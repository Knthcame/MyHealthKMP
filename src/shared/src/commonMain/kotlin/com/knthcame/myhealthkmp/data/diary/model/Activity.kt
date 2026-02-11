package com.knthcame.myhealthkmp.data.diary.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration
import kotlin.time.Instant

@Entity("ActivitiesTable")
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duration: Duration,
    val timeStamp: Instant,
)
