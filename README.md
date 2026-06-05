# Smart Meal — Aplikasi Mobile Pemasok (LKS SMK Jabar 2026)

Aplikasi Android (Kotlin) untuk **Pemasok** pada studi kasus *Smart Meal Distribution System*.
Dibuat untuk Hari ke-2 LKS — mengonsumsi **API yang disediakan juri** (peserta tidak membuat API).

## Base URL API
```
https://smart-meal-api-production.up.railway.app
```
Tidak memakai API key. Autentikasi memakai **Bearer token** yang didapat dari endpoint login.

## Akun Login (latihan)
| Username  | Password    | Pemasok               |
|-----------|-------------|-----------------------|
| pemasok1  | pemasok123  | CV Pangan Sejahtera   |
| pemasok2  | pemasok456  | UD Hasil Bumi         |

## Endpoint yang dipakai
| Method | Endpoint              | Fungsi                |
|--------|-----------------------|-----------------------|
| POST   | `/auth/login`         | Login pemasok         |
| GET    | `/orders`             | Daftar pesanan        |
| GET    | `/orders/{id}`        | Detail pesanan        |
| PUT    | `/orders/{id}/status` | Ubah status pesanan   |

## Cara Menjalankan
1. Buka project di **Android Studio** (versi stabil terbaru).
2. Tunggu Gradle sync selesai (butuh internet untuk unduh dependency pertama kali).
3. Jalankan di emulator atau perangkat (Run ▶). `minSdk 24`, `targetSdk 35`.
4. Login pakai akun di atas → lihat daftar pesanan → buka detail → ubah status (Diproses / Dikirim).

## Catatan Teknis (sesuai ketentuan lomba)
- Networking **hanya** memakai bawaan Java/Kotlin: `HttpURLConnection`, `URL`,
  `InputStream`/`OutputStream`, `JSONObject`/`JSONArray`.
- **Tidak** memakai Retrofit / OkHttp / Volley / Ktor (sesuai larangan TOR).
- UI memakai View klasik: Activity + XML layout + RecyclerView.

## Struktur Kode (ringkas)
| File                     | Fungsi                                              |
|--------------------------|-----------------------------------------------------|
| `ApiClient.kt`           | Semua pemanggilan API (HttpURLConnection)           |
| `Session.kt`             | Menyimpan token & nama pemasok selama app berjalan  |
| `Models.kt`              | Data class `Order`, `OrderItem`, `OrderDetail`      |
| `StatusUtil.kt`          | Warna badge status                                  |
| `LoginActivity.kt`       | Halaman login                                       |
| `OrderListActivity.kt`   | Daftar pesanan (RecyclerView)                       |
| `OrderAdapter.kt`        | Adapter daftar pesanan                              |
| `OrderDetailActivity.kt` | Detail pesanan + tombol ubah status                 |
