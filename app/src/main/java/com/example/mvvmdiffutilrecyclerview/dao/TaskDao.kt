package com.example.mvvmdiffutilrecyclerview.dao

import androidx.room.*
import com.example.mvvmdiffutilrecyclerview.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY date DESC")
    fun getTaskList() : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task) : Long

 /*   //First way
    @Delete
    suspend fun deleteTask(task: Task) : Int*/

    //Second Way
    @Query("DELETE FROM task WHERE taskId == :taskId")
    suspend fun deleteTaskUsingId(taskId : String) : Int


    @Update
    suspend fun updateTask(task: Task) : Int

    //Update By Id
    @Query("UPDATE Task SET taskTitle= :title,description= :description WHERE taskId= :taskId")
    suspend fun updateTaskParticularField(taskId: String, title: String,description: String) : Int

}