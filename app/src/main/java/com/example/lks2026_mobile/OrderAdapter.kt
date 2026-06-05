package com.example.lks2026_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lks2026_mobile.databinding.ItemOrderBinding

/** Adapter sederhana untuk menampilkan daftar pesanan di RecyclerView. */
class OrderAdapter(
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val items = mutableListOf<Order>()

    fun submit(newItems: List<Order>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = items[position]
        holder.binding.tvOrderId.text = "Pesanan #${order.orderId}"
        holder.binding.tvDate.text = "Tanggal: ${order.orderDate}"
        holder.binding.tvSupplier.text = order.supplierName
        StatusUtil.applyStatus(holder.binding.tvStatus, order.status)
        holder.binding.root.setOnClickListener { onClick(order) }
    }

    override fun getItemCount(): Int = items.size
}
