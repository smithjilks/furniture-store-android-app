package com.smith.furniturestore.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.adapter.CartItemsAdapter
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.FragmentCartBinding
import com.smith.furniturestore.viewmodel.CartFragmentViewModel
import com.smith.furniturestore.viewmodel.CartFragmentViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: CartFragmentViewModel by viewModels {
        CartFragmentViewModelFactory(
            (activity?.application as App).cartRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.cartRecyclerView
        val adapter = CartItemsAdapter()
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
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}