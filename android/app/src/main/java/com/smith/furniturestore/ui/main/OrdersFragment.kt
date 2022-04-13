package com.smith.furniturestore.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.adapter.OrderItemsAdapter
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.FragmentOrdersBinding
import com.smith.furniturestore.viewmodel.OrderFragmentViewModel
import com.smith.furniturestore.viewmodel.OrderFragmentViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView


    private val viewModel: OrderFragmentViewModel by viewModels {
        OrderFragmentViewModelFactory(
            (activity?.application as App).furnitureRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.ordersRecyclerView
        val adapter = OrderItemsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        /**
         * Running with paging source
         */
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dbOrderItems.collectLatest { pagedList ->
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