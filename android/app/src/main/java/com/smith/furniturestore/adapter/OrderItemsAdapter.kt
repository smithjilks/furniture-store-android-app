package com.smith.furniturestore.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.CartItem
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.databinding.OrderItemBinding
import com.smith.furniturestore.viewmodel.OrderFragmentViewModel
import com.smith.furniturestore.viewmodel.OrderFragmentViewModelFactory
import kotlin.coroutines.coroutineContext


class OrderItemsAdapter : PagingDataAdapter<OrderItem, OrderItemsAdapter.OrderItemsViewHolder>(
    OrderItemDiffUtil()
) {

    class OrderItemsViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ViewModelProvider(
            binding.root.context as FragmentActivity,
            OrderFragmentViewModelFactory(
                ((binding.root.context as FragmentActivity)?.application as App).furnitureRepository
            )
        )[OrderFragmentViewModel::class.java]

        fun bind(orderItem: OrderItem) {

            if (viewModel.getUserType() == "admin") {
                binding.updateOrderItemStatusButton.visibility = View.VISIBLE
            }

            binding.orderItemIdTextView.text =
                binding.root.context.getString(R.string.order_item_id, orderItem.id)
            binding.orderItemTotalTextView.text = binding.root.context.getString(
                R.string.order_item_total_format,
                orderItem.totalCost
            )

            // TODO: Get actual delivery location to display
            binding.orderItemDeliveryTextView.text = binding.root.context.getString(
                R.string.order_item_location_format,
                orderItem.deliveryLat.toString()
            )
            binding.orderItemStatusTextView.text = binding.root.context.getString(
                R.string.order_item_status_format,
                orderItem.orderStatus
            )

            binding.orderItemUserNameTextView.text = binding.root.context.getString(
                R.string.order_item_user_name_format,
                orderItem.userDetails.firstName,
                orderItem.userDetails.lastName
            )
            binding.orderItemUserPhoneTextView.text = binding.root.context.getString(
                R.string.order_item_phone_format,
                orderItem.userDetails.phone.toString()
            )
            binding.orderItemUserEmailTextView.text = binding.root.context.getString(
                R.string.order_item_email_format,
                orderItem.userDetails.email
            )


            //set up child recyclerview linear layout manager and adapter
            val adapter = OrderCartItemsAdapter(orderItem.items)
            binding.orderCartItemsRecyclerView.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.orderCartItemsRecyclerView.adapter = adapter

            binding.updateOrderItemStatusButton.setOnClickListener {

            }

        }

    }

    class OrderItemDiffUtil : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: OrderItemsViewHolder, position: Int) {
        val orderItem = getItem(position)
        orderItem?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemsViewHolder {
        return OrderItemsViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

}

