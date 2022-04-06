package com.smith.furniturestore.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.smith.furniturestore.R
import com.smith.furniturestore.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userProfileCreateItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_createItemFragment)

        }

        binding.userProfileOrdersButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_fragment_to_orders_fragment)

        }

        binding.userProfileCartButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_fragment_to_cart_fragment)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}