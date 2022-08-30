package com.example.listmaker.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listmaker.MainActivity
import com.example.listmaker.databinding.ListDetailFragmentBinding
import com.example.listmaker.models.TaskList
import com.example.listmaker.ui.main.MainViewModel
import com.example.listmaker.ui.main.MainViewModelFactory

class ListDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ListDetailFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ListDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this, MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(requireActivity())
            )
        ).get(MainViewModel::class.java)
        val list: TaskList? =
            arguments?.getParcelable(MainActivity.INTENT_LIST_KEY)
        if (list != null) {
            viewModel.list = list
            requireActivity().title = list.name
        } else {
            val list: TaskList =
                requireActivity().intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!
            viewModel.list = list
        }
        val recyclerAdapter = ListItemsAdapter(viewModel.list)
        binding.recyclerviewDetail.adapter = recyclerAdapter
        binding.recyclerviewDetail.layoutManager = LinearLayoutManager(requireContext())

        viewModel.onTaskAdded = {
            recyclerAdapter.notifyDataSetChanged()
        }
    }

}