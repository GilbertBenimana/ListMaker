package com.example.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.listmaker.databinding.MainActivityBinding
import com.example.listmaker.models.TaskList
import com.example.listmaker.ui.detail.ListDetailFragment
import com.example.listmaker.ui.main.MainFragment
import com.example.listmaker.ui.main.MainViewModel
import com.example.listmaker.ui.main.MainViewModelFactory
import com.example.listmaker.ui.main.RecyclerViewAdapter

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(this)
            )
        ).get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container
            } else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }
        }
        binding.fab.setOnClickListener { showCreateListDialog() }
    }

    private fun showCreateListDialog() {
        val title = getString(R.string.name_of_list)
        val buttonName = getString(R.string.create_list)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        alertDialog.setView(editText)
        alertDialog.setPositiveButton(buttonName) { dialog, _ ->
            val taskList = TaskList(editText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
            dialog.dismiss()
        }
        alertDialog.create().show()
    }
    private fun showCreateTaskDialog() {
        var editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.task_to_add))
            .setView(editText)
            .setPositiveButton(getString(R.string.add_task)){
                    dialog,_ ->
                val task = editText.text.toString()
                // 4
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create().show()

    }
    private fun showListDetail(list: TaskList) {

        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(
                this,
                ListDetailActivity::class.java
            )
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivityForResult(
                listDetailIntent,
                LIST_DETAIL_REQUEST_CODE
            )
        } else {
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.list_detail_fragment_container,
                    ListDetailFragment::class.java, bundle, null
                )
            }
            binding.fab.setOnClickListener {
                showCreateTaskDialog()
            }
        }

    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data:
        Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // 1
        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode ==
            Activity.RESULT_OK
        ) {
            // 2
            data?.let {
                viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
                viewModel.refreshLists()
            }
        }
    }

    override fun onBackPressed() {
        // 1
        val listDetailFragment =

            supportFragmentManager.findFragmentById(R.id.list_detail_fragment_container)
        // 2
        if (listDetailFragment == null) {
            super.onBackPressed()
        } else {
            // 3
            title = resources.getString(R.string.app_name)
            // 4
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }

            // 5
            binding.fab.setOnClickListener {
                showCreateListDialog()
            }
        }
    }
}