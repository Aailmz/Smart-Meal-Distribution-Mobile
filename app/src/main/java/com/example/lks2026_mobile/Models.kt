package com.example.lks2026_mobile

/** Satu baris daftar pesanan. toString() = teks yang tampil di ListView. */
data class Order(
    val orderId: Int,
    val supplierName: String,
    val orderDate: String,
    val status: String
) {
    override fun toString() = "#$orderId  •  $status\n$supplierName  ($orderDate)"
}

data class OrderItem(val itemName: String, val quantity: Int, val unit: String)

data class OrderDetail(
    val orderId: Int,
    val supplierName: String,
    val orderDate: String,
    val status: String,
    val notes: String,
    val items: List<OrderItem>
)
