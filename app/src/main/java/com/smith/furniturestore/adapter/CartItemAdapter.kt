package com.smith.furniturestore.adapter

import android.view.LayoutInflater
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

class CartItemsAdapter :
    PagingDataAdapter<CartItem, CartItemsAdapter.CartItemsViewHolder>(CartItemDiffUtil()) {

    class CartItemsViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ViewModelProvider(
            binding.root.context as FragmentActivity,
            CartFragmentViewModelFactory(
                ((binding.root.context as FragmentActivity)?.application as App).furnitureRepository
            )
        )[CartFragmentViewModel::class.java]


        fun bind(cartItem: CartItem) {
            binding.cartItemTitleTextView.text = cartItem.title
            binding.cartItemQuantityTextView.text =
                binding.root.context.getString(R.string.quantity_format, cartItem.quantity)
            binding.cartItemPriceTextView.text =
                binding.root.context.getString(R.string.total_format, cartItem.subTotal)


            val imgUri = cartItem.imageUrl.toUri().buildUpon().scheme("https").build()
            binding.cartItemImageView.load(imgUri) {
                placeholder(R.drawable.ic_loading)
                error(R.drawable.ic_broken_image)
            }

            binding.subtractButton.setOnClickListener {
                if (cartItem.quantity <= 1) {
                    viewModel.deleteCartItem(cartItem.id.toString())
                } else {
                    val unitPrice = cartItem.subTotal / cartItem.quantity
                    val updatedItem = CartItem(
                        cartItem.id,
                        cartItem.title,
                        cartItem.quantity - 1,
                        cartItem.subTotal - unitPrice,
                        cartItem.imageUrl
                    )
                    viewModel.insert(updatedItem)

                }


            }

            binding.addButton.setOnClickListener {
                val unitPrice = cartItem.subTotal / cartItem.quantity
                val updatedItem = CartItem(
                    cartItem.id,
                    cartItem.title,
                    cartItem.quantity + 1,
                    cartItem.subTotal + unitPrice,
                    cartItem.imageUrl
                )
                viewModel.updateSingleCartItem(updatedItem)

            }
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

    override fun onBindViewHolder(holder: CartItemsAdapter.CartItemsViewHolder, position: Int) {
        val cartItem = getItem(position)
        cartItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartItemsAdapter.CartItemsViewHolder {
        return CartItemsAdapter.CartItemsViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}