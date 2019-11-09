package com.example.databasetest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Course class
@Database(entities = [(Section::class), (TimeTable::class), (Location::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sectionDao(): SectionDao
    abstract fun timeDao(): TimeDao
    abstract fun locationDao(): LocationDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var sectionDao = database.sectionDao()
                    var timeDao = database.timeDao()
                    var locationDao = database.locationDao()

                    // Delete all content here.
                    sectionDao.deleteAll()
                    timeDao.deleteAll()
                    locationDao.deleteAll()

                    // Add sample courses and times.
                    var course = CourseList(90724, "Stats", "Ruwan")
                    courseDao.insertCourse(course)
                    course = CourseList(90339, "Software Engineering", "Opeyemi")
                    courseDao.insertCourse(course)
                    course = CourseList(90340, "Artificial Intelligence", "Amir")
                    courseDao.insertCourse(course)
                    var time = TimeTable(90724, 1, "10:00", "11:20", "Tuesday", "A310")
                    timeDao.insertTime(time)
                    time = TimeTable(90724, 2, "10:00", "11:20", "Thursday", "D223")
                    timeDao.insertTime(time)
                    time = TimeTable(90724, 3, "10:00", "11:20", "Friday", "A310")
                    timeDao.insertTime(time)
                    time = TimeTable(90339, 4, "11:30", "2:20", "Thursday", "D223")
                    timeDao.insertTime(time)
                    time = TimeTable(90340, 5, "2:30", "5:20", "Thursday", "D223")
                    timeDao.insertTime(time)

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
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, "example")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return INSTANCE!!
            }
        }
}
