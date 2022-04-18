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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.FragmentLoginBinding
import com.smith.furniturestore.model.UserAuthCredentials
import com.smith.furniturestore.ui.MainActivity
import com.smith.furniturestore.viewmodel.CatalogFragmentViewModel
import com.smith.furniturestore.viewmodel.CatalogFragmentViewModelFactory
import com.smith.furniturestore.viewmodel.SharedAuthViewModel
import com.smith.furniturestore.viewmodel.SharedAuthViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var progressBar: ProgressBar? = null


    private val viewModel: SharedAuthViewModel by activityViewModels {
        SharedAuthViewModelFactory(
            (activity?.application as App).furnitureRepository,
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loginProgressBar


        binding.getAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.forgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.loginButton.setOnClickListener {
            if (isEnteredDataValid()) {
                progressBar!!.visibility = View.VISIBLE
                binding.loginButton.isEnabled = false

                val userAuthCredentials = UserAuthCredentials(
                    binding.loginEmailEditText.text.toString().trim(),
                    binding.loginPasswordEditText.text.toString()
                )
                viewModel.loginUser(userAuthCredentials, requireActivity().application)
            }
        }

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            if (it == "success") {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show()

            } else {
                progressBar!!.visibility = View.GONE
                binding.loginButton.isEnabled = true
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
            }


        })

    }

    private fun isEnteredDataValid(): Boolean {
        val email = binding.loginEmailEditText
        val password = binding.loginPasswordEditText

        if (password.text.toString().length <= 8) {
            password.error = getString(R.string.error_password)
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim())
                .matches() || email.text.isNullOrEmpty()
        ) {
            email.error = getString(R.string.error_email)
        }

        return !(email.error != null || password.error != null)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}