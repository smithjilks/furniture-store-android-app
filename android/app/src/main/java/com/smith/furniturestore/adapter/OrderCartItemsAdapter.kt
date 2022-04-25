package com.smith.furniturestore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.databinding.CartItemBinding
import com.smith.furniturestore.viewmodel.CartFragmentViewModel
import com.smith.furniturestore.viewmodel.CartFragmentViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 * OrderCartItemsAdapter
 *
 * An adapter for items added to the cart table for display in the cart items recycler view
 * It extends the RecyclerView.Adapter class
 *
 * @param OrderCartItemsAdapter.CartItemsViewHolder Custom ViewHolder Class the PagingDataAdapter.
 * @property dataset List of Cart Items
 */
class OrderCartItemsAdapter(private val dataset: List<CartItem>
) : RecyclerView.Adapter<OrderCartItemsAdapter.CartItemsViewHolder>() {

    /**
     * CartItemsViewHolder
     *
     * A custom ViewHolder for items added to the cart table for display in the order cart items recycler view
     * It extends the RecyclerView.ViewHolder
     *
     * @property binding ViewBinding for the cart_item.xml layout view.
     */
    class CartItemsViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Populates the cart_item.xml layout with values.
         * @return unit
         */
        fun bind(cartItem: CartItem) {
            binding.root.layoutParams.width = 750
            binding.cartItemTitleTextView.text = cartItem.title
            binding.cartItemQuantityTextView.text =
                binding.root.context.getString(R.string.quantity_format, cartItem.quantity)
            val cartSubtotal = NumberFormat.getCurrencyInstance().format(cartItem.subTotal)
            binding.cartItemPriceTextView.text =
                binding.root.context.getString(R.string.total_format, cartSubtotal)

            val imgUri = cartItem.imageUrl.toUri().buildUpon().scheme("https").build()
            binding.cartItemImageView.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_broken_image)
            }

            binding.addButton.visibility = View.GONE
            binding.subtractButton.visibility = View.GONE

        }

    }

    /**
     * CartItemDiffUtil
     *
     * A custom DiffUtil class for items added to the cart table
     * It extends the PagingDataAdapter class
     *
     * @param CartItem Catalog Item DataClass/Entity.
     */
    class CartItemDiffUtil : DiffUtil.ItemCallback<CartItem>() {
        /**
         * Checks whether items updated/added to the cart table are the same.
         * @return Boolean
         */
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Checks whether contents updated/added to the cart table are the same.
         * @return Boolean
         */
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }
    }


    /**
     * Binds view holder to the adapter
     * @param holder ViewHolder for the adapter
     * @param position Current item position in the recycler view
     * @return Unit
     */
    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val cartItem = dataset[position]
        cartItem?.let {
            holder.bind(it)
        }
    }

    /**
     * Creates the ViewHolder for the cart items
     * @param parent ViewGroup
     * @param viewType Integer that indicates the view type
     * @return an inflated CartItemsViewHolder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartItemsViewHolder {
        return CartItemsViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**
     * Returns the dataset size
     * @return dataset.size
     */
    override fun getItemCount(): Int {
        return dataset.size
    }
}