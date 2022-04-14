package com.smith.furniturestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.smith.furniturestore.R
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.databinding.CatalogItemBinding
import com.smith.furniturestore.ui.main.CatalogFragmentDirections
import java.text.NumberFormat


class CatalogItemsAdapter : PagingDataAdapter<CatalogItem, CatalogItemsAdapter.CatalogItemsViewHolder>(
    CatalogItemDiffUtil()
) {

    class CatalogItemsViewHolder(private val binding: CatalogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(catalogItem: CatalogItem) {
            binding.catalogItemTitleTextView.text = catalogItem.title
            binding.catalogItemDescriptionTextView.text = catalogItem.shortDescription

            val catalogPrice = NumberFormat.getCurrencyInstance().format(catalogItem.price)
            binding.catalogItemPriceTextView.text = binding.root.context.getString(R.string.price_format, catalogPrice)

            val imgUri = catalogItem.imageUrl.toUri().buildUpon().scheme("https").build()
            binding.catalogItemImageView.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_broken_image)
            }

            binding.readMoreButton.setOnClickListener {
                val action = CatalogFragmentDirections.actionCatalogFragmentToCatalogItemDetailsFragment(catalogItem.id.toString())
                binding.root.findNavController().navigate(action)

            }
        }

    }

    class CatalogItemDiffUtil : DiffUtil.ItemCallback<CatalogItem>() {
        override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: CatalogItemsAdapter.CatalogItemsViewHolder, position: Int) {
        val catalogItem = getItem(position)
        catalogItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogItemsAdapter.CatalogItemsViewHolder {
        return CatalogItemsAdapter.CatalogItemsViewHolder(
            CatalogItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

}

