package me.dio.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.dio.todolist.databinding.ActivityMainBinding
import me.dio.todolist.datasource.TaskDataSource
import me.dio.todolist.ui.adapter.TaskListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter

        insertListeners()
    }

    private fun insertListeners() {
        binding.fabButton.setOnClickListener {
            val mIntent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(mIntent, CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val mIntent = Intent(this, AddTaskActivity::class.java)
            mIntent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(mIntent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            updateList()
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility =
            if (list.isEmpty()) View.VISIBLE
            else View.GONE
        adapter.submitList(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}