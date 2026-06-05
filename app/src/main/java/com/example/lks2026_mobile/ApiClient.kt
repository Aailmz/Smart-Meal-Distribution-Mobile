package com.example.lks2026_mobile

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Helper untuk memanggil API juri.
 *
 * Sesuai ketentuan lomba: HANYA memakai fitur bawaan Java/Kotlin
 * (HttpURLConnection, URL, InputStream/OutputStream, JSONObject/JSONArray).
 * TIDAK memakai Retrofit / OkHttp / Volley / Ktor.
 *
 * Semua fungsi di sini bersifat blocking, jadi WAJIB dipanggil dari
 * background thread (lihat penggunaan Dispatchers.IO di Activity).
 * Jika gagal, fungsi akan melempar Exception berisi pesan yang siap ditampilkan.
 */
object ApiClient {

    private const val BASE_URL = "https://smart-meal-api-production.up.railway.app"

    // ---- Endpoint sesuai TOR ----

    /** POST /auth/login → menyimpan token & nama pemasok ke Session. */
    fun login(username: String, password: String) {
        val body = JSONObject()
            .put("username", username)
            .put("password", password)

        val json = request("POST", "/auth/login", body)
        if (!json.optBoolean("success")) {
            throw Exception(json.optString("message", "Login gagal."))
        }

        val data = json.getJSONObject("data")
        Session.token = data.getString("token")
        Session.supplierName = data.optString("supplierName")
        Session.username = data.optString("username")
    }

    /** GET /orders → daftar pesanan. */
    fun getOrders(): List<Order> {
        val json = request("GET", "/orders", null)
        if (!json.optBoolean("success")) {
            throw Exception(json.optString("message", "Gagal memuat daftar pesanan."))
        }

        val data = json.getJSONArray("data")
        val orders = ArrayList<Order>()
        for (i in 0 until data.length()) {
            val o = data.getJSONObject(i)
            orders.add(
                Order(
                    orderId = o.getInt("orderId"),
                    supplierName = o.optString("supplierName"),
                    orderDate = o.optString("orderDate"),
                    status = o.optString("status")
                )
            )
        }
        return orders
    }

    /** GET /orders/{id} → detail pesanan beserta daftar bahan. */
    fun getOrderDetail(id: Int): OrderDetail {
        val json = request("GET", "/orders/$id", null)
        if (!json.optBoolean("success")) {
            throw Exception(json.optString("message", "Gagal memuat detail pesanan."))
        }

        val d = json.getJSONObject("data")
        val itemsArray = d.optJSONArray("items") ?: JSONArray()
        val items = ArrayList<OrderItem>()
        for (i in 0 until itemsArray.length()) {
            val it = itemsArray.getJSONObject(i)
            items.add(
                OrderItem(
                    itemName = it.optString("itemName"),
                    quantity = it.optInt("quantity"),
                    unit = it.optString("unit")
                )
            )
        }

        return OrderDetail(
            orderId = d.getInt("orderId"),
            supplierName = d.optString("supplierName"),
            orderDate = d.optString("orderDate"),
            status = d.optString("status"),
            notes = d.optString("notes"),
            items = items
        )
    }

    /** PUT /orders/{id}/status → ubah status. Mengembalikan pesan sukses dari server. */
    fun updateStatus(id: Int, status: String): String {
        val body = JSONObject().put("status", status)
        val json = request("PUT", "/orders/$id/status", body)
        if (!json.optBoolean("success")) {
            throw Exception(json.optString("message", "Gagal mengubah status."))
        }
        return json.optString("message", "Status berhasil diubah.")
    }

    // ---- Inti request HTTP (dipakai semua fungsi di atas) ----

    private fun request(method: String, path: String, body: JSONObject?): JSONObject {
        val connection = URL(BASE_URL + path).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = method
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.setRequestProperty("Accept", "application/json")

            // Lampirkan token kalau sudah login.
            if (Session.token.isNotEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer ${Session.token}")
            }

            // Kirim body JSON kalau ada (untuk POST/PUT).
            if (body != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.outputStream.use { output ->
                    output.write(body.toString().toByteArray(Charsets.UTF_8))
                }
            }

            // Baca respons: inputStream kalau sukses, errorStream kalau error.
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val text = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""

            if (text.isEmpty()) {
                throw Exception("Server tidak mengirim respons (kode $code).")
            }
            return JSONObject(text)
        } finally {
            connection.disconnect()
        }
    }
}
