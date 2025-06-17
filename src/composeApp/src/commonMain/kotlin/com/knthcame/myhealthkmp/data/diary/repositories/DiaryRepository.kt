package com.knthcame.myhealthkmp.data.diary.repositories

import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    /** Gets all the stored [DiaryEvent]s. **/
    val diaryEvents: Flow<List<DiaryEvent>>

    /** Deletes the corresponding [DiaryEvent]. **/
    suspend fun delete(type: DiaryEvent.Type, id: Int)

    /** Stores a new [DiaryEvent]. **/
    suspend fun save(event: DiaryEvent)
}