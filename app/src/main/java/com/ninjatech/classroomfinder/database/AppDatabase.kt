package com.ninjatech.classroomfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Course class
@Database(entities = [(Coordinate::class),(Course::class), (ClassRoom::class), (SavedSection::class), (Section::class), (Time::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Declare our DAOs
    abstract val coursesDao: CoursesDao
    abstract val savedSectionsDao: SavedSectionsDao

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
