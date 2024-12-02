# Prayer Times App

The **Prayer Times App** is an Android application that provides accurate prayer times for 30 days based on the user's location. It features modern Android development practices, offline support, and a user-friendly interface to ensure accessibility and reliability.

---

## 🛠️ Features

- **Prayer Times API Integration**: Fetches prayer times for the next 30 days based on the user's location.
- **Google Maps Integration**:
  - Displays the user's current location.
  - Shows the Kaaba location with directional guidance.
- **Offline Mode**: Stores prayer times locally using **Room Database** for access without an internet connection.
- **MockWebServer Testing**: Simulates API responses for integration testing, ensuring robustness.
- **Modular Architecture**: Organized into `app`, `data`, and `domain` modules for scalability and maintainability.
- **Modern Android Development**:
  - **Coroutines** for efficient background operations.
  - **Hilt** for dependency injection.
  - **MVVM** pattern for clear separation of concerns.
- **Unit Testing**: Comprehensive test cases for core app functionalities to ensure reliability and quality.
- **Lightweight and Efficient**: Optimized for performance with a focus on responsiveness.

---

## 🛠️ Tech Stack

### Language & Frameworks
- **Kotlin**
- **XML** for UI layouts.

### Libraries & Tools
- **Room**: Local database for caching prayer times.
- **Hilt**: Dependency injection for easier management of dependencies.
- **Retrofit**: Networking library for API calls.
- **MockWebServer**: Mock server for integration testing.
- **Coroutines**: Asynchronous programming for smooth user experience.
- **Google Maps SDK**: For location-based features and direction guidance.
- **JUnit & MockK**: For unit testing and mocking.

---

## 🗂️ Project Modules

### 1. **App Module**
   - Entry point of the application.
   - Contains UI and app-level configurations.

### 2. **Data Module**
   - Manages data from both API and local storage.
   - Implements repository patterns.

### 3. **Domain Module**
   - Holds business logic and use case classes.
   - Acts as an intermediary between the `Data` module and `App` module.

---

## 📝 Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/AhmedGamalRamadan/PrayerTimes.git
