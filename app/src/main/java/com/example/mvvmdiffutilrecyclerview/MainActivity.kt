package com.example.mvvmdiffutilrecyclerview

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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
                taskViewModel.inssertTask(newTask).observe(this){
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
        updateTaskBtn.setOnClickListener {
            if (validateEditText(updateETTitle, updateETTitleL) &&  validateEditText(updateETDesc, updateETDescL))
            {
                updateTaskDialog.dismiss()
                Toast.makeText( this,"Validated!!", Toast.LENGTH_SHORT).show()
                loadingDialog.show()
            }
        }
        //Update Task End
    }
}