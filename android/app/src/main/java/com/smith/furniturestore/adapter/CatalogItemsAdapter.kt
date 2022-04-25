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


/**
 * CatalogItemsAdapter
 *
 * An adapter for items added to the catalog table for display in the catalog items recycler view
 * It extends the PagingDataAdapter class
 *
 * @param CatalogItem DataClass/Entity for the PagingDataAdapter.
 * @param CatalogItemsAdapter.CartItemsViewHolder Custom ViewHolder Class the PagingDataAdapter.
 */
class CatalogItemsAdapter : PagingDataAdapter<CatalogItem, CatalogItemsAdapter.CatalogItemsViewHolder>(
    CatalogItemDiffUtil()
) {

    /**
     * CatalogItemsViewHolder
     *
     * A custom ViewHolder for items added to the catalog table for display in the catalog items recycler view
     * It extends the RecyclerView.ViewHolder
     *
     * @property binding ViewBinding for the catalog_item.xml layout view.
     */
    class CatalogItemsViewHolder(private val binding: CatalogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Populates the catalog_item.xml layout with values.
         * @param catalogItem CatalogItem DataClass/Entity
         * @return unit
         */
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


    /**
     * CatalogItemDiffUtil
     *
     * A custom DiffUtil class for items added to the catalog table
     * It extends the PagingDataAdapter class
     *
     * @param CatalogItem Catalog Item DataClass/Entity.
     */
    class CatalogItemDiffUtil : DiffUtil.ItemCallback<CatalogItem>() {
        /**
         * Checks whether items updated/added to the catalog table are the same.
         * @return Boolean
         */
        override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Checks whether contents updated/added to the catalog table are the same.
         * @return Boolean
         */
        override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Binds view holder to the adapter
     * @param holder ViewHolder for the adapter
     * @param position Current item position in the recycler view
     * @return Unit
     */
    override fun onBindViewHolder(holder: CatalogItemsViewHolder, position: Int) {
        val catalogItem = getItem(position)
        catalogItem?.let {
            holder.bind(it)
        }
    }

    /**
     * Creates the ViewHolder for the catalog items
     * @param parent ViewGroup
     * @param viewType Integer that indicates the view type
     * @return an inflated CatalogItemsViewHolder
     */
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

