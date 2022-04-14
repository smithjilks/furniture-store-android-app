package com.smith.furniturestore.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.FragmentRegisterBinding
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.model.UserRegistrationInfo
import com.smith.furniturestore.ui.MainActivity
import com.smith.furniturestore.viewmodel.SharedAuthViewModel
import com.smith.furniturestore.viewmodel.SharedAuthViewModelFactory

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var progressBar: ProgressBar? = null


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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.signupProgressBar

        binding.signupHaveAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {
            if (isEnteredDataValid()) {
                progressBar!!.visibility = View.VISIBLE
                binding.signupButton.isEnabled = false

                val registrationInfo = UserRegistrationInfo(
                    binding.signupFirstnameEditText.text.toString().trim(),
                    binding.signupLastnameEditText.text.toString().trim(),
                    binding.signupEmailEditText.text.toString().trim(),
                    binding.signupPhoneEditText.text.toString().trim().toInt(),
                    binding.signupPasswordEditText.text.toString()
                )
                viewModel.signupUser(registrationInfo)
            }
        }

        viewModel.signupStatus.observe(viewLifecycleOwner, Observer {
            if (it == "success") {
                Toast.makeText(context, "Account created successfully. Login to Continue", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                progressBar!!.visibility = View.GONE
                binding.signupButton.isEnabled = true
            }

        })


    }

    private fun isEnteredDataValid(): Boolean {
        val firstName = binding.signupFirstnameEditText
        val lastName = binding.signupLastnameEditText
        val email = binding.signupEmailEditText
        val phone = binding.signupPhoneEditText
        val password = binding.signupPasswordEditText
        val confirmPassword = binding.signupConfirmPasswordEditText

        if (firstName.text.isNullOrEmpty()) {
            firstName.error = getString(R.string.error_first_name)
        }

        if (lastName.text.isNullOrEmpty()) {
            lastName.error = getString(R.string.error_last_name)
        }

        if (phone.text.isNullOrEmpty()) {
            phone.error = getString(R.string.error_phone)
        }

        if (confirmPassword.text.toString().length <= 8) {
            confirmPassword.error = getString(R.string.error_password)
        }

        if (password.text.toString() != confirmPassword.text.toString()) {
            confirmPassword.error = getString(R.string.error_confirm_password)
        }

        if (password.text.toString().length <= 8) {
            password.error = getString(R.string.error_password)
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim())
                .matches() || email.text.isNullOrEmpty()
        ) {
            email.error = getString(R.string.error_email)
        }

        return !(firstName.error != null ||
                lastName.error != null ||
                phone.error != null ||
                email.error != null ||
                password.error != null
                )


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}