package com.example.listmaker.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.listmaker.models.TaskList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
     lateinit var onListAdded: (() -> Unit)
    val lists: MutableList<TaskList> by lazy {
        retrieveLists()
    }

    lateinit var onTaskAdded: (() -> Unit)
    lateinit var list: TaskList

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded.invoke()
    }

    private fun retrieveLists(): MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()
        for(taskList in sharedPreferencesContents){
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }
        return taskLists
    }

    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name,
            list.tasks.toHashSet()).apply()
        lists.add(list)
    }
    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }

    fun saveList(list: TaskList){
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)
        onListAdded = {}
        onListAdded.invoke()
    }
}