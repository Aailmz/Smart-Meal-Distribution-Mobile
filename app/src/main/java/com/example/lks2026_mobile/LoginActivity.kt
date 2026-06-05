package com.example.lks2026_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lks2026_mobile.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.tvError.visibility = View.GONE

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password wajib diisi.")
            return
        }

        setLoading(true)
        lifecycleScope.launch {
            try {
                // Panggil API di background thread.
                withContext(Dispatchers.IO) { ApiClient.login(username, password) }

                // Login sukses → pindah ke daftar pesanan.
                startActivity(Intent(this@LoginActivity, OrderListActivity::class.java))
                finish()
            } catch (e: Exception) {
                showError(e.message ?: "Terjadi kesalahan saat login.")
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }
}
