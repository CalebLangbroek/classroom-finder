package com.ninjatech.classroomfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

// Annotates class to be a Room Database with a table (entity) of the Course class
@Database(entities = [(Course::class), (Location::class), (SavedCourse::class), (Section::class), (Time::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Declare our DAOs
    abstract val courseDao: CourseDao
    abstract val locationDao: LocationDao
    abstract val savedDao: SavedDao
    abstract val sectionDao: SectionDao
    abstract val timeDao: TimeDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            // Synchronize so multiple threads can't access
            synchronized(lock = this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        )
                        .createFromAsset("database/database.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}
