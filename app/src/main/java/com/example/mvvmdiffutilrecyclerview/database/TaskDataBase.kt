package com.example.mvvmdiffutilrecyclerview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mvvmdiffutilrecyclerview.dao.TaskDao
import com.example.mvvmdiffutilrecyclerview.models.Task

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(com.example.mvvmdiffutilrecyclerview.converters.TypeConverter::class)
abstract class TaskDataBase : RoomDatabase() {

    abstract val taskDao : TaskDao

    companion object{
        @Volatile
        private var INSTANCE : TaskDataBase?= null

        fun getInstance(context: Context) : TaskDataBase{

            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDataBase::class.java,
                    "task_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}