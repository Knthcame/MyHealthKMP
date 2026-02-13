package com.knthcame.myhealthkmp.data.diary.sources

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDiaryDatabaseBuilder(
    context: Context,
    dbName: String,
): RoomDatabase.Builder<DiaryDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(dbName)
    return Room.databaseBuilder<DiaryDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}