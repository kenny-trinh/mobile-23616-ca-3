package com.example.todolist.activities.Main.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.activities.Edit.EditItemActivity
import com.example.todolist.models.Task
import kotlinx.android.synthetic.main.task_row.view.*

class MainAdapter(val tasks: ArrayList<Task>) : RecyclerView.Adapter<MainViewHolder>() {

    override fun getItemCount(): Int {
        return tasks.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.task_row, parent, false)
        return MainViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = tasks[position]
        holder.itemView.textView_task_name?.text = task.name
//        holder.itemView.textView_due_date.text = "Due date: " + task.dueDate

        holder.task = task
        holder.id = position
    }

}

class MainViewHolder(val view: View, var task: Task? = null, var id: Int? = null) : RecyclerView.ViewHolder(view) {

    companion object {
        val TASK_NAME_KEY = "TASK_NAME"
        val TASK_ID_KEY = "TASK_ID"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, EditItemActivity::class.java)
            intent.putExtra(TASK_NAME_KEY, task?.name)
            intent.putExtra(TASK_ID_KEY, id)
            view.context.startActivity(intent)
        }
    }

}
