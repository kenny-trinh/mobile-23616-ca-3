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
import timber.log.Timber

class EditItemActivity : AppCompatActivity() {

    var currentTasks = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i(getString(R.string.timber_EditItemActivity_onCreate_called))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
        val editTextTaskName = findViewById<EditText>(R.id.editText_edit_task_name)
        val saveButton = findViewById<Button>(R.id.button_save_changes)
        val currentTaskName = intent.getStringExtra(MainViewHolder.TASK_NAME_KEY)
        editTextTaskName.setText(currentTaskName)

        findViewById<EditText>(R.id.editText_edit_task_name).setOnClickListener {
            Timber.i(getString(R.string.timber_editText_edit_task_name_clicked))
            saveButton.visibility = View.VISIBLE
        }

        findViewById<Button>(R.id.button_save_changes).setOnClickListener {
            Timber.i(getString(R.string.timber_save_button_clicked))
            saveChanges()
            saveButton.visibility = View.INVISIBLE


        }

        findViewById<ImageButton>(R.id.imageButton_delete_task).setOnClickListener {
            Timber.i(getString(R.string.timber_delete_button_clicked))
            deleteTask()
            finish()
        }

        findViewById<EditText>(R.id.editText_edit_task_name).setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // save new task name after pressing the done button on the keyboard
                Timber.i(getString(R.string.timber_keyboard_done_button_clicked))
                saveChanges()
                Timber.i(getString(R.string.timber_editText_edit_task_name_clicked))
                saveButton.visibility = View.INVISIBLE
                false
            } else {
                false
            }
        }

    }

    private fun deleteTask() {
        Timber.i(getString(R.string.timber_deleteTask_called))
        val gson = Gson()
        val prefs = getSharedPreferences(getString(R.string.package_name),
                Context.MODE_PRIVATE)
        currentTasks = getTasks()

        val id = intent.getIntExtra(MainViewHolder.TASK_ID_KEY, 0)
        currentTasks.removeAt(id)
        val editor = prefs.edit()
        val json = gson.toJson(currentTasks)
        editor.putString(getString(R.string.json_tasks_tag), json)

        editor.apply()
        Timber.i(getString(R.string.timber_task_deleted))
    }

    private fun saveChanges() {
        Timber.i(getString(R.string.timber_saveChanges_called))
        var newTaskName = editText_edit_task_name.text.toString()
        if (newTaskName.isNotEmpty()) {
            intent.putExtra(MainViewHolder.TASK_NAME_KEY, newTaskName)
            val gson = Gson()
            val prefs = getSharedPreferences(getString(R.string.package_name),
                    Context.MODE_PRIVATE)
            currentTasks = getTasks()
            val id = intent.getIntExtra(MainViewHolder.TASK_ID_KEY, 0)
            currentTasks.set(id, Task(newTaskName))
            val editor = prefs.edit()
            val json = gson.toJson(currentTasks)
            editor.putString(getString(R.string.json_tasks_tag), json)
            editor.apply()
            Timber.i(getString(R.string.timber_saved_changes))
            finish()
        }
        if (newTaskName.isEmpty()) {
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
            Timber.i(getString(R.string.timber_tasks_loaded_from_shared_prefs))
        }
        return tasks
    }

}
