package com.example.todolist.activities.Main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.activities.Create.CreateItemActivity
import com.example.todolist.activities.Main.adapters.MainAdapter
import com.example.todolist.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var tasks = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView_Main.layoutManager = LinearLayoutManager(this)

        initializeSharedPrefs()
        loadTasksFromSharedPrefs()

        findViewById<Button>(R.id.button_add_task).setOnClickListener {
            val intent = Intent(this, CreateItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasksFromSharedPrefs()
    }

    private fun initializeSharedPrefs() {
        tasks = loadTasksFromSharedPrefs()

        val gson = Gson()
        val prefs = getSharedPreferences("com.example.todolist",
                Context.MODE_PRIVATE)

        if (tasks.size == 0) { // check if tasks exist in sharedprefs
            tasks = ArrayList<Task>()
//            val currentDate = LocalDate.now().toString()
            // fill tasks with example tasks
            tasks.add(Task("Read a book"))
            tasks.add(Task("Buy groceries"))
            tasks.add(Task("Water flowers"))

        }
        val editor = prefs.edit()
        val json = gson.toJson(tasks)
        editor.putString("tasks", json)
        editor.apply()
    }

    private fun loadTasksFromSharedPrefs(): ArrayList<Task> {
        val gson = Gson()
        val prefs = getSharedPreferences("com.example.todolist",
                Context.MODE_PRIVATE)
        if (prefs.contains("tasks")) {
            val json = prefs.getString("tasks", null)
            val type = object : TypeToken<ArrayList<Task>>() {}.type
            tasks = gson.fromJson(json, type)
            var adapter = MainAdapter(tasks)
            recyclerView_Main.adapter = adapter
        }
        return tasks
    }



}