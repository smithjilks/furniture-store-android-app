package com.smith.furniturestore.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.databinding.FragmentCheckoutBinding
import com.smith.furniturestore.viewmodel.CheckoutFragmentViewModel
import com.smith.furniturestore.viewmodel.CheckoutFragmentViewModelFactory
import com.smith.furniturestore.BuildConfig.MAPS_API_KEY


class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private var locationLatLng: LatLng? = null

    private var progressBar: ProgressBar? = null

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
        progressBar = binding.checkoutProgressBar


        viewModel.totalAmount.observe(viewLifecycleOwner, Observer {
            binding.checkoutTotalCostTextView.text =
                getString(R.string.checkout_total, it.toString())
        })

        binding.selectLocationButton.setOnClickListener {
            getLocation()
        }

        binding.completeCheckoutButton.setOnClickListener {
            if (binding.deliveryLocationTextView.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please select a delivery location", Toast.LENGTH_LONG)
                    .show()

            } else {

                progressBar!!.visibility = View.VISIBLE
                binding.completeCheckoutButton.isEnabled = false

                viewModel.allCartItems.observe(viewLifecycleOwner, Observer {
                    val orderItem = viewModel.totalAmount.value?.let { amount ->
                        OrderItem(
                            id = "",
                            totalCost = amount,
                            items = it,
                            creator = "",
                            deliveryLat = locationLatLng?.latitude!!,
                            deliveryLong = locationLatLng?.longitude!!,
                            orderStatus = "",
                            createdAt = "",
                            updatedAt = ""
                        )
                    }
                    Log.d("Order Item", orderItem.toString())
                    viewModel.submitOrderToRemote(orderItem!!)
                })


            }

        }

        viewModel.orderStatus.observe(viewLifecycleOwner, Observer {
            if(it == "success") {
                Toast.makeText(context, "Order placed successfully", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            } else {
                progressBar!!.visibility = View.GONE
                binding.completeCheckoutButton.isEnabled = true
                Toast.makeText(context, "Failed to place order. Try again.", Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getLocation() {
        /**
         * Initialize Places. API Key is located in
         * com.smith.furniturestore.BuildConfig.GOOGLE_MAPS_API_KEY which is ignored by version control
         */
        if (!Places.isInitialized()) {
            Places.initialize(context, MAPS_API_KEY)
        }

        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(context)

        resultLauncher.launch(intent)


    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            data?.let {
                val place = Autocomplete.getPlaceFromIntent(data)
                binding.deliveryLocationTextView.text = place.name
                locationLatLng = place.latLng
                Log.i("Location Data", "Place: ${place.name}, ${place.id}")
            }


        }
    }
}