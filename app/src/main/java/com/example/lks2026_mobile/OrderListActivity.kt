package com.example.lks2026_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.lks2026_mobile.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding
    private var orders = listOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTitle.text = "Pesanan - ${ApiClient.supplierName}"
        binding.btnRefresh.setOnClickListener { loadOrders() }
        binding.btnLogout.setOnClickListener { logout() }

        binding.listOrders.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, OrderDetailActivity::class.java)
            intent.putExtra("orderId", orders[position].orderId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        binding.tvStatus.text = "Memuat..."
        Thread {
            try {
                val result = ApiClient.getOrders()
                runOnUiThread {
                    orders = result
                    binding.listOrders.adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, orders)
                    binding.tvStatus.text = if (orders.isEmpty()) "Belum ada pesanan." else ""
                }
            } catch (e: Exception) {
                runOnUiThread { binding.tvStatus.text = e.message }
            }
        }.start()
    }

    private fun logout() {
        ApiClient.token = ""
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
