package com.example.lks2026_mobile

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.core.content.ContextCompat

/** Helper kecil untuk menampilkan badge status berwarna. */
object StatusUtil {

    /** Pasang teks status + warna badge ke TextView (background-nya @drawable/bg_chip). */
    fun applyStatus(textView: TextView, status: String) {
        val colorRes = when (status.lowercase()) {
            "pending" -> R.color.status_pending
            "diproses" -> R.color.status_diproses
            "dikirim", "selesai" -> R.color.status_dikirim
            else -> R.color.status_default
        }
        textView.text = status
        textView.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(textView.context, colorRes))
    }
}
