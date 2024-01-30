package com.example.mvvmdiffutilrecyclerview

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdiffutilrecyclerview.adapter.TaskRVVBListAdapter
import com.example.mvvmdiffutilrecyclerview.databinding.ActivityMainBinding
import com.example.mvvmdiffutilrecyclerview.models.Task
import com.example.mvvmdiffutilrecyclerview.utils.Status
import com.example.mvvmdiffutilrecyclerview.utils.clearEditText
import com.example.mvvmdiffutilrecyclerview.utils.longToastShow
import com.example.mvvmdiffutilrecyclerview.utils.setupDialog
import com.example.mvvmdiffutilrecyclerview.utils.validateEditText
import com.example.mvvmdiffutilrecyclerview.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID




class MainActivity : AppCompatActivity() {

    private val mainBinding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addTaskDialog : Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }

    private val updateTaskDialog : Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }

    private val loadingDialog : Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val taskViewModel : TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }




    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)




    //Add Task start
        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener{
            addTaskDialog.dismiss()
            loadingDialog.show()
        }


        //Add Task start
        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })

        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }

        })



        mainBinding.addTaskFABtn.setOnClickListener {
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            addTaskDialog.show()
        }


        val saveTaskBtn  = addTaskDialog.findViewById<Button>(R.id.saveTaskBtn)
        saveTaskBtn.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL) &&  validateEditText(addETDesc, addETDescL))
            {
                addTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    Date()
                )
                taskViewModel.insertTask(newTask).observe(this){
                    when(it.status){
                        Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Status.SUCCESS ->{
                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1){
                                longToastShow("Task Added Successfully!")
                            }
                        }
                        Status.ERROR->{
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                    }
                }
            }
        }

        //Add Task End


        //Update Task Start
        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }

        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }

        })


        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)
        updateCloseImg.setOnClickListener{
            updateTaskDialog.dismiss()
        }

        val updateTaskBtn  = updateTaskDialog.findViewById<Button>(R.id.updateTaskBtn)

        //Update Task End


        val taskRVVBListAdapter = TaskRVVBListAdapter{ type,position, task ->
            if (type == "delete") {
                taskViewModel
                    /* first way method
                             .deleteTask(task)
                 */
                    .deleteTaskUsingId(task.id)
                    .observe(this) {
                        when (it.status) {
                            Status.LOADING -> {
                                loadingDialog.show()
                            }

                            Status.SUCCESS -> {
                                loadingDialog.dismiss()
                                if (it.data != -1) {
                                    longToastShow("Task Deleted Successfully!")
                                }
                            }

                            Status.ERROR -> {
                                loadingDialog.dismiss()
                                it.message?.let { it1 -> longToastShow(it1) }
                            }
                        }
                    }
            }else if(type == "update"){
                updateETTitle.setText(task.title)
                updateETDesc.setText(task.description)

                updateTaskBtn.setOnClickListener {
                    if (validateEditText(updateETTitle, updateETTitleL) &&  validateEditText(updateETDesc, updateETDescL))
                    {
                        val updateTask = Task(
                            task.id,
                            updateETTitle.text.toString().trim(),
                            updateETDesc.text.toString().trim(),
                        //For date update
                            Date()
                        )
                        updateTaskDialog.dismiss()
                        loadingDialog.show()


                        taskViewModel
                            /* first way method
                                     .deleteTask(task)
                         */
  //                          .updateTask(updateTask)
                            .updateTaskParticularField(
                                task.id,
                                updateETTitle.text.toString().trim(),
                                updateETDesc.text.toString().trim(),
                            )
                            .observe(this) {
                                when (it.status) {
                                    Status.LOADING -> {
                                        loadingDialog.show()
                                    }

                                    Status.SUCCESS -> {
                                        loadingDialog.dismiss()
                                        if (it.data != -1) {
                                            longToastShow("Task Updated Successfully!")
                                        }
                                    }

                                    Status.ERROR -> {
                                        loadingDialog.dismiss()
                                        it.message?.let { it1 -> longToastShow(it1) }
                                    }
                                }
                            }

                    }
                }

                updateTaskDialog.show()

            }

        }

        mainBinding.taskRv.adapter = taskRVVBListAdapter
        taskRVVBListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                mainBinding.taskRv.smoothScrollToPosition(positionStart)
            }
        })
        callGetTaskList(taskRVVBListAdapter)

    }
    private fun callGetTaskList(taskRecyclerViewAdapter : TaskRVVBListAdapter){
        CoroutineScope(Dispatchers.Main).launch {

            taskViewModel.getTaskList().collect{
                when(it.status){
                    Status.LOADING -> {
                        loadingDialog.show()
                    }
                    Status.SUCCESS ->{
                        it.data?.collect{taskList ->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.submitList(taskList)
                        }

                    }
                    Status.ERROR->{
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                }
            }
        }
    }
}