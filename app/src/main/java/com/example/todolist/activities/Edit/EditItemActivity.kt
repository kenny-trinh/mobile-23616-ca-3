package com.example.todolist.activities.Edit

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R
import com.example.todolist.activities.Main.adapters.MainViewHolder
import com.example.todolist.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AppCompatActivity() {

    var currentTasks = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val editTextTaskName = findViewById<EditText>(R.id.editText_edit_task_name)
        val saveButton = findViewById<Button>(R.id.button_save_changes)

        val currentTaskName = intent.getStringExtra(MainViewHolder.TASK_NAME_KEY)
        editTextTaskName.setText(currentTaskName)

        findViewById<EditText>(R.id.editText_edit_task_name).setOnClickListener {

            saveButton.visibility = View.VISIBLE
        }

        findViewById<Button>(R.id.button_save_changes).setOnClickListener {
            saveChanges()
            saveButton.visibility = View.INVISIBLE


        }

        findViewById<ImageButton>(R.id.imageButton_delete_task).setOnClickListener {
            deleteTask()
            finish()
        }

        findViewById<EditText>(R.id.editText_edit_task_name).setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // save new task name after pressing the done button on the keyboard
                saveChanges()
                saveButton.visibility = View.INVISIBLE
                false
            } else {
                false
            }
        }

    }

    private fun deleteTask() {
        val gson = Gson()
        val prefs = getSharedPreferences("com.example.todolist",
                Context.MODE_PRIVATE)
        currentTasks = getTasks()

        val id = intent.getIntExtra(MainViewHolder.TASK_ID_KEY, 0)
        currentTasks.removeAt(id)
        val editor = prefs.edit()
        val json = gson.toJson(currentTasks)
        editor.putString("tasks", json)

        editor.apply()
    }

    private fun saveChanges() {

        var newTaskName = editText_edit_task_name.text.toString()

        if (newTaskName.isNotEmpty()) {
            intent.putExtra(MainViewHolder.TASK_NAME_KEY, newTaskName)

            val gson = Gson()
            val prefs = getSharedPreferences("com.example.todolist",
                    Context.MODE_PRIVATE)
            currentTasks = getTasks()

            val id = intent.getIntExtra(MainViewHolder.TASK_ID_KEY, 0)
            currentTasks.set(id, Task(newTaskName))

            val editor = prefs.edit()
            val json = gson.toJson(currentTasks)
            editor.putString("tasks", json)

            editor.apply()

            finish()
        }

        if (newTaskName.isEmpty()) {
            Toast.makeText(this, "Task name field is empty.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getTasks(): ArrayList<Task> {
        val gson = Gson()
        val prefs = getSharedPreferences("com.example.todolist",
                Context.MODE_PRIVATE)
        var tasks = ArrayList<Task>()

        if (prefs.contains("tasks")) {
            val json = prefs.getString("tasks", null)
            val type = object : TypeToken<ArrayList<Task>>() {}.type
            tasks = gson.fromJson(json, type)
        }
        return tasks
    }

}
