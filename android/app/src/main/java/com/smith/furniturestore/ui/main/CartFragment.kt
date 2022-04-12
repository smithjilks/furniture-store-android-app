package com.smith.furniturestore.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.R
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
            (activity?.application as App).furnitureRepository
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
         */
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAll.collectLatest { pagedList ->
                pagedList.let {
                    adapter.submitData(pagedList)
                }
            }
        }

        binding.clearCartButton.setOnClickListener {
            viewModel.clearCart()
        }

        binding.cartCheckoutButton.setOnClickListener {
            if (adapter.snapshot().size == 0) {
                Toast.makeText(context, "You have an empty cart", Toast.LENGTH_LONG).show()
            } else {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            }
        }


        viewModel.totalCost.observe(viewLifecycleOwner, Observer {
            it.let {
                binding.cartTotalCostOfItemsTextView.text = "Ksh." + it.toString()
                Log.d("Total Cost Observing", it.toString())
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}