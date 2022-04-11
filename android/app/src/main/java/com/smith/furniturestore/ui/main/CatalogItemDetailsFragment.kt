package com.smith.furniturestore.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.databinding.FragmentCatalogItemDetailsBinding
import com.smith.furniturestore.databinding.FragmentCheckoutBinding
import com.smith.furniturestore.viewmodel.CatalogFragmentViewModel
import com.smith.furniturestore.viewmodel.CatalogFragmentViewModelFactory
import com.smith.furniturestore.viewmodel.CatalogItemDetailsFragmentViewModel
import com.smith.furniturestore.viewmodel.CatalogItemDetailsFragmentViewModelFactory
import kotlinx.coroutines.launch

class CatalogItemDetailsFragment : Fragment() {

    private var _binding: FragmentCatalogItemDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: CatalogItemDetailsFragmentArgs by navArgs()

    private val viewModel: CatalogItemDetailsFragmentViewModel by viewModels {
        CatalogItemDetailsFragmentViewModelFactory(
            (activity?.application as App).furnitureRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatalogItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            val item = viewModel.getSingleCatalogItemById(args.itemId)
            item?.let {
                binding.itemTitleTextView.text = it.title
                binding.itemPriceTextView.text = getString(R.string.price_format, it.price)
                binding.itemShortDescTextView.text = it.shortDescription
                binding.itemLongDescTextView.text = it.longDescription

                val imgUri = it.imageUrl.toUri().buildUpon().scheme("https").build()
                binding.itemDetailsImageView.load(imgUri) {
                    placeholder(R.drawable.ic_loading)
                    error(R.drawable.ic_broken_image)
                }

                val cartItem = CartItem(it.id, it.title, 1, it.price, it.imageUrl)

                binding.addToCartFab.setOnClickListener {
                    viewModel.insert(cartItem)
                    Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_LONG).show()
                }
            }
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}