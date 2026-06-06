package com.example.lks2026_mobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lks2026_mobile.databinding.ActivityOrderDetailBinding

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private var orderId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getIntExtra("orderId", 0)
        binding.btnDiproses.setOnClickListener { updateStatus("Diproses") }
        binding.btnDikirim.setOnClickListener { updateStatus("Dikirim") }

        loadDetail()
    }

    private fun loadDetail() {
        Thread {
            try {
                val d = ApiClient.getOrderDetail(orderId)
                runOnUiThread { showDetail(d) }
            } catch (e: Exception) {
                runOnUiThread { toast(e.message) }
            }
        }.start()
    }

    private fun updateStatus(status: String) {
        Thread {
            try {
                val msg = ApiClient.updateStatus(orderId, status)
                val d = ApiClient.getOrderDetail(orderId) // muat ulang status terbaru
                runOnUiThread {
                    toast(msg)
                    showDetail(d)
                }
            } catch (e: Exception) {
                runOnUiThread { toast(e.message) }
            }
        }.start()
    }

    private fun showDetail(d: OrderDetail) {
        binding.tvInfo.text =
            "Pesanan #${d.orderId}\n${d.supplierName}\nTanggal: ${d.orderDate}\n" +
                "Status: ${d.status}\nCatatan: ${if (d.notes.isBlank()) "-" else d.notes}"

        val sb = StringBuilder()
        for (item in d.items) sb.append("• ${item.itemName} : ${item.quantity} ${item.unit}\n")
        binding.tvItems.text = sb.toString().trim()
    }

    private fun toast(msg: String?) {
        Toast.makeText(this, msg ?: "Error", Toast.LENGTH_SHORT).show()
    }
}
