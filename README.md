# Smart Meal Supplier Mobile App

Android application built with **Kotlin** for suppliers participating in the Smart Meal Distribution System.

The application allows suppliers to authenticate, view incoming orders, check order details, and update order statuses through a REST API.

## API Base URL

```text
https://smart-meal-api-production.up.railway.app
```

Authentication uses a **Bearer Token** obtained from the login endpoint.

> **Note:** This application is configured to use the hosted API by default. For local development, you can also run your own backend instance using the API source code available at:
>
> https://github.com/Aailmz/smart-meal-api
>
> After starting the API locally, update the application's base URL accordingly.

---

## Demo Accounts

| Username | Password   | Supplier            |
| -------- | ---------- | ------------------- |
| pemasok1 | pemasok123 | CV Pangan Sejahtera |
| pemasok2 | pemasok456 | UD Hasil Bumi       |

---

## API Endpoints

| Method | Endpoint              | Description             |
| ------ | --------------------- | ----------------------- |
| POST   | `/auth/login`         | Supplier authentication |
| GET    | `/orders`             | Retrieve order list     |
| GET    | `/orders/{id}`        | Retrieve order details  |
| PUT    | `/orders/{id}/status` | Update order status     |

---

## Features

* User authentication
* View supplier orders
* View detailed order information
* Update order status
* Session-based authentication using Bearer Token
* Status badge visualization

---

## Getting Started

### Requirements

* Android Studio (latest stable version recommended)
* Android SDK
* Internet connection (for initial Gradle dependency download)

### Run the Application

1. Open the project in Android Studio.
2. Wait until Gradle synchronization completes.
3. Connect an Android device or start an emulator.
4. Build and run the application.

Project configuration:

```text
minSdk 24
targetSdk 35
```

### Login

Use one of the demo accounts listed above.

After logging in, users can:

* View available orders
* Open order details
* Update order status (Processing / Shipped)

---

## Technical Notes

### Networking

The application uses only native Java/Kotlin networking components:

* HttpURLConnection
* URL
* InputStream
* OutputStream
* JSONObject
* JSONArray

No third-party networking libraries are used.

Examples of libraries intentionally not used:

* Retrofit
* OkHttp
* Volley
* Ktor

### User Interface

The application uses the traditional Android View system:

* Activities
* XML Layouts
* RecyclerView

---

## Project Structure

| File                     | Description                                                         |
| ------------------------ | ------------------------------------------------------------------- |
| `ApiClient.kt`           | Handles all API communication using HttpURLConnection               |
| `Session.kt`             | Stores authentication token and supplier information during runtime |
| `Models.kt`              | Data models (`Order`, `OrderItem`, `OrderDetail`)                   |
| `StatusUtil.kt`          | Status badge color utilities                                        |
| `LoginActivity.kt`       | Login screen                                                        |
| `OrderListActivity.kt`   | Order list screen                                                   |
| `OrderAdapter.kt`        | RecyclerView adapter for displaying orders                          |
| `OrderDetailActivity.kt` | Order detail screen and status update actions                       |

---

## Architecture Overview

```text
Android App
    │
    ├── LoginActivity
    │       │
    │       ▼
    │   ApiClient
    │       │
    │       ▼
    │ REST API
    │
    ├── OrderListActivity
    │       │
    │       ▼
    │   RecyclerView
    │
    └── OrderDetailActivity
            │
            ▼
      Status Update API
```

---

## License

This project is provided for educational and demonstration purposes.
