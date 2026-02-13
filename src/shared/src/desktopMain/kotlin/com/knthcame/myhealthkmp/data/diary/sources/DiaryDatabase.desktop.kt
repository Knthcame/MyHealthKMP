package com.knthcame.myhealthkmp.data.diary.sources

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDiaryDatabaseBuilder(dbName: String): RoomDatabase.Builder<DiaryDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbName)
    return Room.databaseBuilder<DiaryDatabase>(
        name = dbFile.absolutePath,
    )
}