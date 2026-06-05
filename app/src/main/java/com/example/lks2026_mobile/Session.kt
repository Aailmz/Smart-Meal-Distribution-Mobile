package com.example.lks2026_mobile

/**
 * Menyimpan data sesi login (token & nama pemasok) selama aplikasi berjalan.
 * Sederhana saja: disimpan di memori, hilang saat aplikasi ditutup.
 */
object Session {
    var token: String = ""
    var supplierName: String = ""
    var username: String = ""

    fun clear() {
        token = ""
        supplierName = ""
        username = ""
    }
}
