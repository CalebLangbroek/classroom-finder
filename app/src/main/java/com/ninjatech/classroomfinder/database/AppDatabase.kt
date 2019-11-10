package com.ninjatech.classroomfinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Course class
@Database(entities = [(Course::class), (Location::class), (SavedList::class), (Section::class), (Time::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Declare our DAOs
    abstract val courseDao: CourseDao
    abstract val locationDao: LocationDao
    abstract val savedDao: SavedDao
    abstract val sectionDao: SectionDao
    abstract val timeDao: TimeDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    // Add sample courses and times.
                    var course = Course(0 , "STAT 270 Introduction to Probability and Statistics", "STAT" )
                    database.courseDao.insertCourse(course)
                    course = Course(0, "COMP 370 Software Engineering", "COMP")
                    database.courseDao.insertCourse(course)
                    course = Course(90340, "COMP 380 Artificial Intelligence", "COMP")
                    database.courseDao.insertCourse(course)

                    var time = Time(90724, 1, 0, "11:20", "Tuesday", "A310", "", "")
                    database.timeDao.insertTime(time)
                    time = Time(90724, 2, 0, "11:20", "Thursday", "D223","", "")
                    database.timeDao.insertTime(time)
                    time = Time(90724, 3, 0, "11:20", "Friday", "A310","", "")
                    database.timeDao.insertTime(time)
                    time = Time(90339, 4, 0, "2:20", "Thursday", "D223","", "")
                    database.timeDao.insertTime(time)
                    time = Time(90340, 5, 0, "5:20", "Thursday", "D223","", "")
                    database.timeDao.insertTime(time)
                }
            }
        }
    }

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
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}
