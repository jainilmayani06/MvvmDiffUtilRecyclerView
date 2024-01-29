package com.example.mvvmdiffutilrecyclerview.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdiffutilrecyclerview.dao.TaskDao
import com.example.mvvmdiffutilrecyclerview.database.TaskDataBase
import com.example.mvvmdiffutilrecyclerview.models.Task
import com.example.mvvmdiffutilrecyclerview.utils.Resource
import com.example.mvvmdiffutilrecyclerview.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskRepository(application: Application) {

    private val taskDao : TaskDao = TaskDataBase.getInstance(application).taskDao

    fun insertTask(task: Task) = MutableLiveData<Resource<Long>>().apply {
        postValue(Resource.Loading())

        try {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = taskDao.insertTask(task)
                    postValue(Success(result))
                }

        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }
}