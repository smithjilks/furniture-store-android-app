package com.smith.furniturestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.dao.CartDao
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.CatalogItem
import com.smith.furniturestore.data.repository.CartRepository
import com.smith.furniturestore.data.repository.CatalogRepository
import com.smith.furniturestore.databinding.CatalogItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogItemsAdapter : PagingDataAdapter<CatalogItem, CatalogItemsAdapter.CatalogItemsViewHolder>(
    CatalogItemDiffUtil()
) {

    class CatalogItemsViewHolder(private val binding: CatalogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(catalogItem: CatalogItem) {
            binding.catalogItemTitleTextView.text = catalogItem.title
            binding.catalogItemDescriptionTextView.text = catalogItem.shortDescription
            binding.catalogItemPriceTextView.text = catalogItem.price.toString()

            binding.floatingActionButton.setOnClickListener {
                val cartItem = CartItem(0, catalogItem.id, catalogItem.title, 1, catalogItem.price)
                CoroutineScope(Dispatchers.IO).launch {

                }

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
        return CatalogItemsViewHolder(CatalogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}