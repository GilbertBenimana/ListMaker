package com.example.listmaker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listmaker.databinding.MainFragmentBinding
import com.example.listmaker.models.TaskList

class MainFragment(
) : Fragment(),
    RecyclerViewAdapter.RecyclerViewClickListener {

    interface MainFragmentInteractionListener {
        fun listItemTapped(list: TaskList)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
    lateinit var clickListener: MainFragmentInteractionListener
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this, MainViewModelFactory(
                PreferenceManager.getDefaultSharedPreferences(requireActivity())
            )
        ).get(MainViewModel::class.java)

        val recyclerViewAdapter =
            RecyclerViewAdapter(viewModel.lists, this)
        binding.recyclerview.adapter = recyclerViewAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.onListAdded = {
            recyclerViewAdapter.notifyDataSetChanged()
        }

    }

    override fun listItemClicked(list: TaskList) {
        clickListener.listItemTapped(list)
    }

}