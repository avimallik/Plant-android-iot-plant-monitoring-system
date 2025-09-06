#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

const char* WIFI_SSID = "Avi";
const char* WIFI_PASS = "oxyzen1234";

// Coordinates (example: Dhaka)
const float LAT = 23.8103;
const float LON = 90.4125;

void setup() {
  Serial.begin(115200);
  delay(200);

  // Connect to WiFi
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASS);
  Serial.print("Connecting to WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected!");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    // Build URL
    String url = "https://api.open-meteo.com/v1/forecast?latitude=";
    url += String(LAT, 4);
    url += "&longitude=" + String(LON, 4);
    url += "&current=precipitation&timezone=auto";

    WiFiClientSecure client;
    client.setInsecure(); // Skip TLS verification for demo
    HTTPClient https;

    if (https.begin(client, url)) {
      int httpCode = https.GET();
      if (httpCode == HTTP_CODE_OK) {
        String payload = https.getString();

        // Parse JSON
        StaticJsonDocument<4096> doc;
        DeserializationError error = deserializeJson(doc, payload);
        if (!error) {
          if (doc["current"].containsKey("precipitation")) {
            float precip = doc["current"]["precipitation"].as<float>();
            String time = doc["current"]["time"].as<String>();

            Serial.print("Precipitation: ");
            Serial.print(precip);
            Serial.print(" mm at ");
            Serial.println(time);
          } else {
            Serial.println("No precipitation data found.");
          }
        } else {
          Serial.println("JSON parse error!");
        }
      } else {
        Serial.print("HTTP error: ");
        Serial.println(httpCode);
      }
      https.end();
    } else {
      Serial.println("HTTPS connection failed.");
    }
  }

  delay(600000); // Update every 10 minutes
}
