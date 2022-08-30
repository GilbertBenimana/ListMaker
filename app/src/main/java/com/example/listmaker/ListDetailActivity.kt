package com.example.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.listmaker.databinding.ListDetailActivityBinding
import com.example.listmaker.models.TaskList
import com.example.listmaker.ui.detail.ListDetailFragment
import com.example.listmaker.ui.detail.ListDetailViewModel
import com.example.listmaker.ui.main.MainViewModel
import com.example.listmaker.ui.main.MainViewModelFactory

class ListDetailActivity : AppCompatActivity() {
    lateinit var binding: ListDetailActivityBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(this)
            )
        ).get(MainViewModel::class.java)

        binding.fabDetail.setOnClickListener { showCreateTaskDialog() }

        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
        title = viewModel.list.name
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListDetailFragment.newInstance())
                .commitNow()
        }
    }

    private fun showCreateTaskDialog() {
        var editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.task_to_add))
            .setView(editText)
            .setPositiveButton(getString(R.string.add_task)) { dialog, _ ->
                val task = editText.text.toString()
                // 4
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create().show()

    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(
            MainActivity.INTENT_LIST_KEY,
            viewModel.list
        )
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}