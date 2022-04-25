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
import java.text.NumberFormat


/**
 * CartItemsAdapter
 *
 * An adapter for items added to the cart table for display in the cart items recycler view
 * It extends the PagingDataAdapter class
 *
 * @param CartItem DataClass/Entity for the PagingDataAdapter.
 * @param CartItemsAdapter.CartItemsViewHolder Custom ViewHolder Class the PagingDataAdapter.
 */
class CartItemsAdapter :
    PagingDataAdapter<CartItem, CartItemsAdapter.CartItemsViewHolder>(CartItemDiffUtil()) {

    /**
     * CartItemsViewHolder
     *
     * A custom ViewHolder for items added to the cart table for display in the cart items recycler view
     * It extends the RecyclerView.ViewHolder
     *
     * @property binding ViewBinding for the cart_item.xml layout view.
     */
    class CartItemsViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ViewModelProvider(
            binding.root.context as FragmentActivity,
            CartFragmentViewModelFactory(
                ((binding.root.context as FragmentActivity)?.application as App).furnitureRepository
            )
        )[CartFragmentViewModel::class.java]


        /**
         * Populates the cart_item.xml layout with values.
         * @return unit
         */
        fun bind(cartItem: CartItem) {
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

            binding.subtractButton.setOnClickListener {
                if (cartItem.quantity <= 1) {
                    viewModel.updateCartTotalAmount(-cartItem.subTotal.toLong())
                    viewModel.deleteCartItem(cartItem.id)
                } else {
                    val unitPrice = cartItem.subTotal / cartItem.quantity
                    val updatedItem = CartItem(
                        cartItem.id,
                        cartItem.title,
                        cartItem.quantity - 1,
                        cartItem.subTotal - unitPrice,
                        cartItem.imageUrl
                    )
                    viewModel.updateSingleCartItem(updatedItem)
                    viewModel.updateCartTotalAmount(-unitPrice.toLong())

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
                viewModel.updateCartTotalAmount(unitPrice.toLong())


            }
        }

    }

    /**
     * CartItemDiffUtil
     *
     * A custom DiffUtil class for items added to the cart table
     * It extends the PagingDataAdapter class
     *
     * @param CartItem Cart Item DataClass/Entity.
     */
    class CartItemDiffUtil : DiffUtil.ItemCallback<CartItem>() {
        /**
         * Checks whether items updated/added to the cart table are the same.
         * @return Boolean
         */
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id && oldItem.quantity == newItem.quantity
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
        val cartItem = getItem(position)
        cartItem?.let {
            holder.bind(it)
        }
    }

    /**
     * Create the ViewHolder for the cart items
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
}