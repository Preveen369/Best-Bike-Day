# 🏍️ Best Bike Day App

![Platform](https://img.shields.io/badge/Platform-Android-blue.svg)
![Tech](https://img.shields.io/badge/Backend-Firebase-orange.svg)
![API](https://img.shields.io/badge/API-OpenWeatherMap-lightblue.svg)
![Language](https://img.shields.io/badge/Language-Java-yellow.svg)
![UI](https://img.shields.io/badge/UI-XML-green.svg)
![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)

**Best Bike Day** is a biker-friendly mobile application that delivers **accurate 5-day weather forecasts** to help riders plan safer and smarter journeys. Integrated with the **OpenWeatherMap API** and backed by **Firebase Authentication and Realtime Database**, the app provides personalized settings, real-time notifications, and a smooth, intuitive user experience. Built with a focus on usability, reliability, and modern UI/UX design, it showcases expertise in **Android development** and **API integration**.

---

## ✨ Features

### 🌦️ 5-Day Weather Forecast
- Access detailed weather data including temperature, wind speed, and precipitation.
- Real-time weather updates using OpenWeatherMap API.

### 🌙 Light/Dark Theme Toggle
- Switch between light and dark modes based on your preference.
- Enhances visibility and reduces eye strain.

### ⚙️ User Preferences
- Choose preferred **temperature units** (°C/°F) and **wind speed formats** (km/h, mph, m/s).
- Save favorite riding locations for quick forecast checks.

### 🔒 Secure User Authentication
- Sign up or log in via **Firebase Authentication**.
- Supports both **Email/Password** and **Google Sign-In** methods.

### 📍 Intuitive UI/UX
- Clean dashboard highlighting today’s and upcoming weather conditions.
- Lightweight and responsive design for a seamless experience.

---

## 🚀 Tech Stack

- **Frontend**: Java, XML  
- **API**: [OpenWeatherMap](https://openweathermap.org/)  
- **Backend**: Firebase Authentication, Firebase Realtime Database  
- **Development Tools**: Android Studio  
- **UI**: Material UI Components  

---

## 📂 Project Structure

```
BestBikeDay/
└── app/
    └── src/
        └── main/
            ├── java/
            │   └── com/example/bestbikeday/
            │       ├── MainActivity.java         # Entry point and main weather screen
            │       ├── services/                 # WeatherService for API calls and parsing
            │       ├── preferences/              # User settings, Firebase sync, theme handling
            │       ├── adapters/                 # WeatherAdapter, ForecastAdapter, etc.
            │       ├── models/                   # Weather.java, UserPreferences.java
            │       ├── utils/                    # DateUtils, LocationHelper, ApiEndpoints
            └── res/
                ├── drawable/                     # Icons, background images
                ├── layout/                       # activity_main.xml, item_weather.xml, etc.
                ├── menu/                         # menu_main.xml for toolbar menus
                ├── values/                       # strings.xml, styles.xml, colors.xml
                └── values-night/                 # Dark mode specific resources

```

---

## 📲 Installation & Setup

### Prerequisites
- Android Studio installed
- Firebase project set up
- OpenWeatherMap API key

### Steps
1. **Clone the repository**
   ```sh
   git clone https://github.com/your-username/best-bike-day-app.git
   ```
2. **Open in Android Studio** and sync dependencies.
3. **Configure Firebase**:
   - Add `google-services.json` to `app/` directory.
   - Enable Firebase Authentication & Realtime Database.
4. **Set up API keys**:
   - Get a free API key from [OpenWeatherMap](https://openweathermap.org/).
   - Add it to `gradle.properties` or use a secure method to store it.
5. **Run the app** on an emulator or a physical device.

---

## 🤝 Contributing
Pull requests are welcome! Feel free to **fork the repository** and submit improvements.

### Contributions are welcome! Follow these steps:
1. **Fork the project.**
2. **Create a feature branch:**
   ```sh
   git checkout -b feature-name
   ```
3. **Commit your changes:**
   ```sh
   git commit -m "Add feature description"
   ```
4. **Push to the branch:**
   ```sh
   git push origin feature-name
   ```
5. **Open a pull request.**

---

## 💎 Contact
For any inquiries or suggestions, reach out at:
- 💌 Email: spreveen123@gmail.com
- 🌐 LinkedIn: www.linkedin.com/in/preveen-s-17250529b/

---

## 🌟 **Show your support**
If you like this project, please consider giving it a ⭐ on GitHub!

🚴 **Ride Safe & Plan Ahead with Best Bike Day!**
