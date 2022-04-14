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

class OrderCartItemsAdapter(private val dataset: List<CartItem>
) : RecyclerView.Adapter<OrderCartItemsAdapter.CartItemsViewHolder>() {

    class CartItemsViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


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

    class CartItemDiffUtil : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val cartItem = dataset[position]
        cartItem?.let {
            holder.bind(it)
        }
    }

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

    override fun getItemCount(): Int {
        return dataset.size
    }
}