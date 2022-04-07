package com.smith.furniturestore.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.adapter.CatalogItemsAdapter
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.FragmentCatalogBinding
import com.smith.furniturestore.viewmodel.CatalogFragmentViewModel
import com.smith.furniturestore.viewmodel.CatalogFragmentViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CatalogFragment : Fragment() {
    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: CatalogFragmentViewModel by viewModels {
        CatalogFragmentViewModelFactory(
            (activity?.application as App).catalogRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.catalogRecyclerView
        val adapter = CatalogItemsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        /**
         * Running with paging source
         * Note: Uses the MoviesAdapter
         */
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAll.collectLatest { pagedList ->
                pagedList?.let {
                    adapter.submitData(pagedList)
                    Toast.makeText(context, adapter.snapshot().size.toString(), Toast.LENGTH_LONG).show()

                }

            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}