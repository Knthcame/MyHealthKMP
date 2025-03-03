package com.knthcame.myhealthkmp.data.diary.sources

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.knthcame.myhealthkmp.data.diary.converters.InstantConverters
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [HeartRate::class], version = 1)
@ConstructedBy(DiaryDatabaseConstructor::class)
@TypeConverters(InstantConverters::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object DiaryDatabaseConstructor : RoomDatabaseConstructor<DiaryDatabase> {
    override fun initialize(): DiaryDatabase
}

fun buildDiaryDatabase(builder: (String) -> RoomDatabase.Builder<DiaryDatabase>): DiaryDatabase {
    return builder("diary.db")
        //.addMigrations()
        //.fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}