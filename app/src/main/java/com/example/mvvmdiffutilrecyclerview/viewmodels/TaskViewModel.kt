package com.example.mvvmdiffutilrecyclerview.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdiffutilrecyclerview.models.Task
import com.example.mvvmdiffutilrecyclerview.repository.TaskRepository
import com.example.mvvmdiffutilrecyclerview.utils.Resource

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)


    fun inssertTask(task: Task): MutableLiveData<Resource<Long>> {
        return taskRepository.insertTask(task)
    }
}