package com.example.lks2026_mobile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lks2026_mobile.databinding.ActivityOrderDetailBinding
import com.example.lks2026_mobile.databinding.ItemOrderLineBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ORDER_ID = "order_id"
    }

    private lateinit var binding: ActivityOrderDetailBinding
    private var orderId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getIntExtra(EXTRA_ORDER_ID, 0)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnDiproses.setOnClickListener { updateStatus("Diproses") }
        binding.btnDikirim.setOnClickListener { updateStatus("Dikirim") }

        loadDetail()
    }

    private fun loadDetail() {
        setLoading(true)
        lifecycleScope.launch {
            try {
                val detail = withContext(Dispatchers.IO) { ApiClient.getOrderDetail(orderId) }
                showDetail(detail)
            } catch (e: Exception) {
                toast(e.message ?: "Gagal memuat detail pesanan.")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun updateStatus(status: String) {
        setLoading(true)
        lifecycleScope.launch {
            try {
                val message = withContext(Dispatchers.IO) { ApiClient.updateStatus(orderId, status) }
                toast(message)
                // Muat ulang detail agar status terbaru langsung tampil.
                val detail = withContext(Dispatchers.IO) { ApiClient.getOrderDetail(orderId) }
                showDetail(detail)
            } catch (e: Exception) {
                toast(e.message ?: "Gagal mengubah status.")
            } finally {
                setLoading(false)
            }
        }
    }

    private fun showDetail(detail: OrderDetail) {
        binding.tvOrderId.text = "Pesanan #${detail.orderId}"
        binding.tvSupplier.text = detail.supplierName
        binding.tvDate.text = "Tanggal: ${detail.orderDate}"
        binding.tvNotes.text = if (detail.notes.isBlank()) "-" else detail.notes
        StatusUtil.applyStatus(binding.tvStatus, detail.status)

        // Bangun daftar bahan secara dinamis ke dalam LinearLayout.
        binding.llItems.removeAllViews()
        for (item in detail.items) {
            val row = ItemOrderLineBinding.inflate(layoutInflater, binding.llItems, false)
            row.tvItemName.text = item.itemName
            row.tvQty.text = "${item.quantity} ${item.unit}"
            binding.llItems.addView(row.root)
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnDiproses.isEnabled = !loading
        binding.btnDikirim.isEnabled = !loading
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
