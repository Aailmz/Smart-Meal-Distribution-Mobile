package com.example.lks2026_mobile

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Pemanggil API juri. WAJIB native: HttpURLConnection + JSONObject (tanpa Retrofit/OkHttp).
 * Semua fungsi blocking -> panggil dari Thread (lihat Activity).
 */
object ApiClient {

    private const val BASE_URL = "https://smart-meal-api-production.up.railway.app"

    // Disimpan di memori selama app jalan.
    var token = ""
    var supplierName = ""

    fun login(username: String, password: String) {
        val body = JSONObject().put("username", username).put("password", password)
        val data = request("POST", "/auth/login", body).getJSONObject("data")
        token = data.getString("token")
        supplierName = data.optString("supplierName")
    }

    fun getOrders(): List<Order> {
        val arr = request("GET", "/orders", null).getJSONArray("data")
        val list = ArrayList<Order>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                Order(
                    o.getInt("orderId"),
                    o.optString("supplierName"),
                    o.optString("orderDate"),
                    o.optString("status")
                )
            )
        }
        return list
    }

    fun getOrderDetail(id: Int): OrderDetail {
        val d = request("GET", "/orders/$id", null).getJSONObject("data")
        val itemsArr = d.optJSONArray("items") ?: JSONArray()
        val items = ArrayList<OrderItem>()
        for (i in 0 until itemsArr.length()) {
            val it = itemsArr.getJSONObject(i)
            items.add(OrderItem(it.optString("itemName"), it.optInt("quantity"), it.optString("unit")))
        }
        return OrderDetail(
            d.getInt("orderId"),
            d.optString("supplierName"),
            d.optString("orderDate"),
            d.optString("status"),
            d.optString("notes"),
            items
        )
    }

    fun updateStatus(id: Int, status: String): String {
        val json = request("PUT", "/orders/$id/status", JSONObject().put("status", status))
        return json.optString("message", "Status berhasil diubah.")
    }

    // Inti request HTTP. Cek "success" dipusatkan di sini.
    private fun request(method: String, path: String, body: JSONObject?): JSONObject {
        val conn = URL(BASE_URL + path).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = method
            conn.connectTimeout = 15000
            conn.readTimeout = 15000
            if (token.isNotEmpty()) conn.setRequestProperty("Authorization", "Bearer $token")

            if (body != null) {
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.outputStream.use { it.write(body.toString().toByteArray()) }
            }

            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val text = stream?.bufferedReader()?.use { it.readText() } ?: ""
            val json = JSONObject(text)
            if (!json.optBoolean("success")) throw Exception(json.optString("message", "Terjadi kesalahan."))
            return json
        } finally {
            conn.disconnect()
        }
    }
}
