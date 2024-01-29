package com.example.mvvmdiffutilrecyclerview.dao

import androidx.room.*
import com.example.mvvmdiffutilrecyclerview.models.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task) : Long

}