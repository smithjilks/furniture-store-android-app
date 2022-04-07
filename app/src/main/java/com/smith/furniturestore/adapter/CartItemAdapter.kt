package com.smith.furniturestore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.databinding.CartItemBinding

class CartItemsAdapter : PagingDataAdapter<CartItem, CartItemsAdapter.CartItemsViewHolder>(CartItemDiffUtil()) {

    class CartItemsViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.cartItemTitleTextView.text = cartItem.title
            binding.cartItemQuantityTextView.text = cartItem.quantity.toString()
            binding.cartItemPriceTextView.text = cartItem.subTotal.toString()
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
        val cartItem = getItem(position)
        cartItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        return CartItemsViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}