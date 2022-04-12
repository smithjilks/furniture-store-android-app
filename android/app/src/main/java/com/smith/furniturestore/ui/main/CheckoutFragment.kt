package com.smith.furniturestore.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.FragmentCatalogBinding
import com.smith.furniturestore.databinding.FragmentCheckoutBinding
import com.smith.furniturestore.viewmodel.CartFragmentViewModel
import com.smith.furniturestore.viewmodel.CartFragmentViewModelFactory
import com.smith.furniturestore.viewmodel.CheckoutFragmentViewModel
import com.smith.furniturestore.viewmodel.CheckoutFragmentViewModelFactory

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckoutFragmentViewModel by viewModels {
        CheckoutFragmentViewModelFactory(
            (activity?.application as App).furnitureRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.totalAmount.observe(viewLifecycleOwner, Observer {
            binding.checkoutTotalCostTextView.text = getString(R.string.checkout_total, it.toString())
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}