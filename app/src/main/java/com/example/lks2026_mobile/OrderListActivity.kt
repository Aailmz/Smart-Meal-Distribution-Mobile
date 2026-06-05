package com.example.lks2026_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lks2026_mobile.databinding.ActivityOrderListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding

    // Saat sebuah pesanan diklik → buka halaman detail.
    private val adapter = OrderAdapter { order -> openDetail(order) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.subtitle = Session.supplierName
        binding.toolbar.inflateMenu(R.menu.menu_order_list)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_refresh -> { loadOrders(); true }
                R.id.action_logout -> { logout(); true }
                else -> false
            }
        }

        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = adapter
    }

    // Dipanggil tiap kali halaman tampil → data status terbaru selalu ikut ter-refresh.
    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        setLoading(true)
        binding.tvStatus.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val orders = withContext(Dispatchers.IO) { ApiClient.getOrders() }
                adapter.submit(orders)
                if (orders.isEmpty()) showStatus("Belum ada pesanan.")
            } catch (e: Exception) {
                adapter.submit(emptyList())
                showStatus(e.message ?: "Gagal memuat data.")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun openDetail(order: Order) {
        val intent = Intent(this, OrderDetailActivity::class.java)
        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, order.orderId)
        startActivity(intent)
    }

    private fun logout() {
        Session.clear()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun setLoading(loading: Boolean) {
        binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showStatus(message: String) {
        binding.tvStatus.text = message
        binding.tvStatus.visibility = View.VISIBLE
    }
}
