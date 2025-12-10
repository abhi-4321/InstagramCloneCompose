# **Instagram Clone — Jetpack Compose**

A modern Android Instagram-style application built with Jetpack Compose, Coroutines, Retrofit, Room, WorkManager, Koin, Coil, Navigation 2, and a custom TypeScript backend. The project follows the MVVM architecture pattern.

---

## **Quick Start**

Start by cloning the project and preparing the Android app:

git clone https://github.com/yourname/instagram-clone

Open the project in Android Studio.

Set your backend URL in:

data/network/NetworkModule.kt

Run the backend (if you're using your own server):

cd backend
npm install
npm run dev

Restart the app after configuration.

---

## **Key Features**

📱 Fully Jetpack Compose UI with Material Theme  
⚡ Coroutines-based asynchronous operations  
🌐 Retrofit networking with TypeScript backend  
💾 Room caching and offline support  
🔄 WorkManager for background uploads  
🧩 Koin for dependency injection  
🖼️ Coil for image loading  
🧭 Navigation 2 with clean screen transitions  
💬 Realtime chat system  
🔍 User search functionality  
🛠️ Custom backend including auth, posts, follows, chat, media  

---

## **How the System Works**

🧠 **MVVM Architecture**  
ViewModels handle UI logic, repositories manage data, and Compose renders UI reactively.

📡 **Network Layer**  
All REST requests communicate with the TypeScript backend for posts, auth, chat, and profiles.

🔄 **WorkManager**  
Handles background media uploads and retry mechanisms.

🔌 **Dependency Injection**  
Koin modules provide all ViewModels, repositories, and services.

🧭 **Navigation**  
Navigation 2 manages all screen-to-screen transitions.

💬 **Chat System**  
Real-time messaging implemented through a backend service with efficient message syncing.

🔍 **User Search**  
Search API allows querying users by username or name.

---

## **Documentation**

📚 **Architecture Overview**  
Explains how MVVM is used across UI, ViewModels, and repositories.

🗄️ **Database**  
Room entities, DAOs, and local caching logic.

🌐 **API Contract**  
Endpoints for auth, posts, search, chat, upload, and profile operations.

---

## **Getting Started**

Installation Guide – Setting up Android + backend  
Usage Guide – Interaction between ViewModels, repositories, and Compose screens  
API Guide – Endpoints and network mappings  
Development Guide – Adding new screens and features  

---

## **Best Practices**

📐 **MVVM State Management**  
ViewModels expose UI states and handle all business logic.

🔄 **Offline-First Logic**  
Room provides caching for posts and user data.

🧩 **Modular DI Structure**  
Each feature defines its own Koin module for easier maintenance.

---

## **Future Improvements**

🎥 Reel-like short video feed  
📲 Push notifications via FCM  
🔐 Encrypted chat messages  
📈 Analytics and event tracking  

---

## **Author**

Developed using Kotlin, Jetpack Compose, and a custom TypeScript backend.
