package com.smith.furniturestore.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.databinding.OrderItemBinding
import com.smith.furniturestore.viewmodel.OrderFragmentViewModel
import com.smith.furniturestore.viewmodel.OrderFragmentViewModelFactory
import java.text.NumberFormat

/**
 * OrderItemsAdapter
 *
 * An adapter for items added to the order table for display in the order items recycler view
 * It extends the PagingDataAdapter class
 *
 * @param OrderItem DataClass/Entity for the PagingDataAdapter.
 * @param OrderItemsAdapter.OrderItemsViewHolder Custom ViewHolder Class the PagingDataAdapter.
 */
class OrderItemsAdapter : PagingDataAdapter<OrderItem, OrderItemsAdapter.OrderItemsViewHolder>(
    OrderItemDiffUtil()
) {

    /**
     * OrderItemsViewHolder
     *
     * A custom ViewHolder for items added to the order table for display in the order items recycler view
     * It extends the RecyclerView.ViewHolder
     *
     * @property binding ViewBinding for the order_item.xml layout view.
     */
    class OrderItemsViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ViewModelProvider(
            binding.root.context as FragmentActivity,
            OrderFragmentViewModelFactory(
                ((binding.root.context as FragmentActivity)?.application as App).furnitureRepository
            )
        )[OrderFragmentViewModel::class.java]

        /**
         * Populates the order_item.xml layout with values.
         * @return unit
         */
        fun bind(orderItem: OrderItem) {

            if (viewModel.getUserType() == "admin") {
                binding.updateOrderItemStatusButton.visibility = View.VISIBLE
            }

            binding.orderItemIdTextView.text =
                binding.root.context.getString(R.string.order_item_id, orderItem.id)

            // Format currency
            val formattedTotalCost = NumberFormat.getCurrencyInstance().format(orderItem.totalCost)
            binding.orderItemTotalTextView.text = binding.root.context.getString(
                R.string.order_item_total_format,
                formattedTotalCost
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
                val status = when (orderItem.orderStatus) {
                    "placed" -> "confirmed"
                    "confirmed" -> "out for delivery"
                    else -> "delivered"
                }
                Toast.makeText(binding.root.context, status, Toast.LENGTH_LONG).show()
                viewModel.updateOrderStatus(orderItem.id, status)

            }

            // Open Maps app and show delivery location
            binding.orderItemOpenMapButton.setOnClickListener {
                val gmmIntentUri: Uri =
                    Uri.parse("geo:${orderItem.deliveryLat},${orderItem.deliveryLong}" +
                            "?q=${orderItem.deliveryLat},${orderItem.deliveryLong}" +
                            "(Furniture Store Customer {${orderItem.userDetails.firstName}  ${orderItem.userDetails.lastName}} Address)")

                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                if (mapIntent.resolveActivity(binding.root.context.packageManager) != null) {
                    binding.root.context.startActivity(mapIntent)
                }
            }

            binding.orderItemCallUserButton.setOnClickListener {
                val userPhoneNumberUri: Uri =
                    Uri.parse("tel:${orderItem.userDetails.phone}")
                val phoneIntent = Intent(Intent.ACTION_CALL, userPhoneNumberUri)
                if (phoneIntent.resolveActivity(binding.root.context.packageManager) != null) {
                    binding.root.context.startActivity(phoneIntent)
                }
            }

//            binding.root.findViewTreeLifecycleOwner()?.let {
//                viewModel.statusLiveData.observe(it, Observer {status ->
//                    if(status == "success") {
//
//                        Toast.makeText(binding.root.context, "Item added Successfully", Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(binding.root.context, "Create new item failed", Toast.LENGTH_LONG).show()
//                    }
//                })
//            }

        }

    }

    /**
     * OrderItemDiffUtil
     *
     * A custom DiffUtil class for items added to the order table
     * It extends the PagingDataAdapter class
     *
     * @param OrderItem Order Item DataClass/Entity.
     */
    class OrderItemDiffUtil : DiffUtil.ItemCallback<OrderItem>() {
        /**
         * Checks whether items updated/added to the order table are the same.
         * @return Boolean
         */
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id && oldItem.orderStatus == newItem.orderStatus
        }

        /**
         * Checks whether contents updated/added to the order table are the same.
         * @return Boolean
         */
        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Binds view holder to the adapter
     * @param holder ViewHolder for the adapter
     * @param position Current item position in the recycler view
     * @return Unit
     */
    override fun onBindViewHolder(holder: OrderItemsViewHolder, position: Int) {
        val orderItem = getItem(position)
        orderItem?.let {
            holder.bind(it)
        }
    }

    /**
     * Create the ViewHolder for the order items
     * @param parent ViewGroup
     * @param viewType Integer that indicates the view type
     * @return an inflated OrderItemsViewHolder
     */
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

