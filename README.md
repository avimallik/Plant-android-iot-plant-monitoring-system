# 🌱 Plant: Android & IoT Plant Monitoring System

**Plant** Plant is an open-source IoT solution for real-time monitoring of soil moisture, temperature, and humidity in grass and turf management. It is built on an ESP8266/NodeMCU microcontroller (Arduino IDE) with a companion Android application that provides live data visualization, configurable alerts, and user-friendly controls. Using low-cost, power-efficient sensors such as AHT-20 (Temperature and Humidity) and Analog Soil Moisture Sensor, the system achieved up to 25% water savings and reduced manual intervention by nearly 40%, while proving scalable for larger agricultural applications. This project was conducted by <b>Jaff Arena and Academy—Bangladesh’s Futsal Playground and BSD BD Ltd</b>, where this project deployed as an IoT-enabled grass or turf watering system. 

---

## 📊 System Architecture
![Dashboard](https://github.com/user-attachments/assets/9f4f8a88-eea9-4f0b-ad22-cd738a8738f2)
---

## ✨ Features

- **Live Sensor Monitoring:**  
  Real-time soil moisture, temperature, and humidity from your plant’s environment.
- **Customizable Alerts:**  
  Set a soil moisture threshold and get notified with a sound and warning if the plant is dry.
- **Visual Dashboard:**  
  The Android app shows live readings with color-coded status, progress bars, and warning messages.
- **LCD Status Display:**  
  NodeMCU’s LCD shows the device’s IP and connection status.
- **Simple Wi-Fi Only:**  
  Runs fully on your own Wi-Fi, no cloud/internet dependency.
- **Easy Installation & Use:**  
  Minimal hardware, clear app, no coding experience needed.

---

## 🛠️ Hardware Requirements

- NodeMCU (ESP8266) board
- Analog Soil Moisture Sensor
- AHT-20 Sensor (Humidity & Temperature)
- I2C 16x2 LCD Display (default I2C address 0x3F)
- Android smartphone (Android 5.0+ recommended)
- Breadboard, jumper wires, USB cable

---

## 🔌 Circuit Diagram

**Wiring Table:**

| Component         | NodeMCU Pin | Note               |
|-------------------|-------------|--------------------|
| AHT-20 Data        | D3          | Digital GPIO       |
| Soil Moisture Out | A0          | Analog input       |
| LCD SDA           | D1          | I2C Data           |
| LCD SCL           | D2          | I2C Clock          |
| LCD VCC           | 5V          | Or 3.3V per module |
| LCD GND           | GND         |                    |
| DHT11 VCC         | 3.3V/5V     | Per module         |
| DHT11 GND         | GND         |                    |
| Soil Sensor VCC   | 3.3V/5V     | Per sensor         |
| Soil Sensor GND   | GND         |                    |

---

## 🖼️ System Workflow

- The **NodeMCU** reads data from the DHT11 and soil moisture sensor.
- Data is shown on the LCD display for local status.
- NodeMCU runs a simple **HTTP server** (port 80) returning JSON-formatted sensor data.
- The **Android app** fetches data every 2 seconds from NodeMCU using its IP address.
- App visualizes plant health, provides warnings, and sounds an alarm if needed.

---

## 🧑‍💻 Software Components

### 1. NodeMCU (ESP8266) Firmware

- Developed in Arduino IDE.
- Handles Wi-Fi connection and local server.
- Sensor reading, LCD update, and JSON data output.

**Key Libraries:**
- [ESP8266WiFi](https://github.com/esp8266/Arduino)
- [ArduinoJson](https://github.com/bblanchon/ArduinoJson)
- [Adafruit DHT sensor library](https://github.com/adafruit/DHT-sensor-library)
- [LiquidCrystal_I2C](https://github.com/johnrickman/LiquidCrystal_I2C)

## 🚀 Getting Started

### 1. Flash NodeMCU (ESP8266)

- Connect NodeMCU to your PC via USB.
- Open the Arduino IDE.
- Install required libraries and select the correct board.
- Upload the provided Arduino code.
- Open the Serial Monitor (baud rate: 115200) or check the LCD for the device’s IP address.

### 2. Setup Hardware

- Wire up all sensors and the LCD as described in the wiring table above.
- Insert the sensors into your plant pot.

### 3. Build or Install Android App

- Clone or download the Android source code.
- Open the project in Android Studio.
- Build and run the app on your Android device, or install the provided APK.
- Open the app, go to **Settings**, and enter your NodeMCU’s local IP address.
- Set your desired soil moisture alarm threshold.

---

## 🖥️ Usage

1. Power on NodeMCU with sensors placed in the soil.
2. Wait for Wi-Fi connection. The LCD will display the IP address.
3. Start the Android app and connect using the NodeMCU’s IP.
4. View real-time plant health status on your phone.
5. If the soil moisture is below your threshold, you’ll see a warning and hear an alarm.

---

## 📱 Android App Screenshots
![Dashboard](https://github.com/user-attachments/assets/e2a0d51a-2b2d-4a66-bae2-c18faeadca7d)![Dashboard](https://github.com/user-attachments/assets/c4578d63-fbc8-4fff-8765-7cd817a82b9d)
---

## 🌐 API Details

- **Endpoint:** `GET http://<NodeMCU_IP>/`
- **Method:** `GET`
- **Returns:** JSON with `moisture_percentage`, `humidity`, `temperature`

**Example response:**
```json
{
  "moisture_percentage": "37",
  "humidity": "85",
  "temperature": "25"
}


