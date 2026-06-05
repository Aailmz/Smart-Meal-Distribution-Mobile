package com.example.lks2026_mobile

/** Satu baris pada daftar pesanan (GET /orders). */
data class Order(
    val orderId: Int,
    val supplierName: String,
    val orderDate: String,
    val status: String
)

/** Satu item bahan di dalam detail pesanan. */
data class OrderItem(
    val itemName: String,
    val quantity: Int,
    val unit: String
)

/** Detail lengkap sebuah pesanan (GET /orders/{id}). */
data class OrderDetail(
    val orderId: Int,
    val supplierName: String,
    val orderDate: String,
    val status: String,
    val notes: String,
    val items: List<OrderItem>
)
