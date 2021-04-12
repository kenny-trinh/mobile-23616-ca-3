package com.example.todolist.activities.Create

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R
import com.example.todolist.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_create_item.*

class CreateItemActivity : AppCompatActivity() {

    var currentTasks = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        findViewById<Button>(R.id.button_create_task).setOnClickListener {
            createTask()
        }

        findViewById<EditText>(R.id.editText_create_task_name).setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // create task after pressing the done button on the keyboard
                createTask()
                false
            } else {
                false
            }
        }

    }

    private fun createTask() {
        val taskName = editText_create_task_name.text.toString()
        if (taskName.isNotEmpty()) {
            val gson = Gson()
            val prefs = getSharedPreferences(getString(R.string.package_name),
                    Context.MODE_PRIVATE)
            currentTasks = getTasks()
            currentTasks.add(Task(taskName))
            val editor = prefs.edit()
            val json = gson.toJson(currentTasks)
            editor.putString(getString(R.string.json_tasks_tag), json)
            editor.apply()
            finish()
        }
        if (taskName.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_task_name_field_empty), Toast.LENGTH_SHORT).show()
        }

    }

    private fun getTasks(): ArrayList<Task> {
        val gson = Gson()
        val prefs = getSharedPreferences(getString(R.string.package_name),
                Context.MODE_PRIVATE)
        var tasks = ArrayList<Task>()
        if (prefs.contains(getString(R.string.json_tasks_tag))) {
            val json = prefs.getString(getString(R.string.json_tasks_tag), null)
            val type = object : TypeToken<ArrayList<Task>>() {}.type
            tasks = gson.fromJson(json, type)
        }
        return tasks
    }

}