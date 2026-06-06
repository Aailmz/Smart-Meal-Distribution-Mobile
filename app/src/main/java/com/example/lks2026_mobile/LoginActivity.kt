package com.example.lks2026_mobile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lks2026_mobile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (username.isEmpty() || password.isEmpty()) {
            binding.tvError.text = "Username dan password wajib diisi."
            return
        }

        binding.tvError.text = "Memproses..."
        Thread {
            try {
                ApiClient.login(username, password)
                runOnUiThread {
                    startActivity(Intent(this, OrderListActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread { binding.tvError.text = e.message }
            }
        }.start()
    }
}
