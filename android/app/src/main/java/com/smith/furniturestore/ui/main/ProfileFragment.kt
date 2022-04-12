package com.smith.furniturestore.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.UserInfo
import com.smith.furniturestore.databinding.FragmentProfileBinding
import com.smith.furniturestore.ui.AuthActivity
import com.smith.furniturestore.ui.MainActivity
import com.smith.furniturestore.ui.auth.LoginFragment
import com.smith.furniturestore.viewmodel.SharedAuthViewModel
import com.smith.furniturestore.viewmodel.SharedAuthViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedAuthViewModel by activityViewModels {
        SharedAuthViewModelFactory(
            (activity?.application as App).furnitureRepository
        )
    }

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


        viewModel.userProfileInfo.observe(viewLifecycleOwner, Observer { userInfo ->
            binding.userProfileNameTextView.text =
                getString(R.string.userprofile_info_format, "${userInfo.firstName} ${userInfo.lastName}")
            binding.userProfilePhoneTextView.text =
                getString(R.string.userprofile_info_format, "+${userInfo.phone}")
            binding.userProfileEmailTextView.text =
                getString(R.string.userprofile_info_format, "${userInfo.email}")
            if (userInfo.userType != "admin") {
                binding.userProfileCreateItemButton.isEnabled = false
            }

        })


        binding.userProfileCreateItemButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_createItemFragment)

        }

        binding.userProfileOrdersButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_fragment_to_orders_fragment)
        }

        binding.userProfileCartButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_fragment_to_cart_fragment)
        }

        binding.userProfileLogoutButton.setOnClickListener {
            viewModel.logoutUser()
            val intent = Intent(context, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}