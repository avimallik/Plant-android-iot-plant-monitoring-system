#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include "DHT.h"

#include <Wire.h>
#include <LiquidCrystal_I2C.h>

//DHT-11 pin defination
#define DHTPIN D3
#define DHTTYPE DHT11 
DHT dht(DHTPIN, DHTTYPE);

//Network parameter
String request;
char* ssid = "plant";
char* password = "hydrogen2020";

//IPAddress ip(192, 168, 0, 184); //set static ip
//IPAddress gateway(192, 168, 0, 1); //set gateway
//IPAddress subnet(255, 255, 255, 0);//set subnet

//Moisture sensor analog pin initialization &
//Moisture percentage variable
const int sensor_pin = A0;
float moisturePercentage;

//LCD I square 2 address
LiquidCrystal_I2C lcd(0x3F, 16, 2);

//WiFiServer object
WiFiServer server(80);

void setup() {
  
//Init Serial USB
Serial.begin(115200);

//DHT-11 serial begin
dht.begin();

//Init ESPBrowser
//WiFi.config(ip, gateway, subnet);

WiFi.begin(ssid, password);
  
// Connect to Wifi network.
while (WiFi.status() != WL_CONNECTED){
  delay(500);
  Serial.print(F("."));
}
  server.begin();
  Serial.println();
  Serial.println(WiFi.localIP());
  server.begin();

  lcd.init();   // initializing the LCD
  lcd.backlight(); // Enable or Turn On the backlight  
  lcd.setCursor(0, 0);   
//  lcd.print("IP:");
  lcd.print(WiFi.localIP());
  
}

void loop() {
  
 WiFiClient client = server.available();

  if (!client) {
    return;
  }
  // Wait until the client sends some data
  Serial.println("new client");
  while(!client.available()){
    delay(1);
  }
  // Read the first line of the request
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();

  //pir status json funstion call
  moistureJSONView(client);//Return webpage
}

void moistureJSONView(WiFiClient client){

  //Moisture calculation
  moisturePercentage = (100.00-((analogRead(sensor_pin)/1023.00) * 100.00));
  if(moisturePercentage<0){
    moisturePercentage = 0.0;
  }
  ///////////////////////

  //DHT-11 weather parameters
  //Humidity
  float humidity = dht.readHumidity();
  //Read temperature as Celsius (the default)
  float temperature = dht.readTemperature();
  ///////////////////////

  //Android Application connection status display 
  lcd.setCursor(0, 1);  
  lcd.print("App:Connected");  
  
  ////Send wbepage to client
  client.println("HTTP/1.1 200 OK");           // This tells the browser that the request to provide data was accepted
  client.println("Access-Control-Allow-Origin: *");  //Tells the browser it has accepted its request for data from a different domain (origin).
  client.println("Content-Type: application/json;charset=utf-8");  //Lets the browser know that the data will be in a JSON format
  client.println("Server: Arduino");           // The data is coming from an Arduino Web Server (this line can be omitted)
  client.println("Connection: close");         // Will close the connection at the end of data transmission.
  client.println();                            // You need to include this blank line - it tells the browser that it has reached the end of the Server reponse header.
  
  client.print("{\"moisture_percentage\": \"");
  client.print(moisturePercentage,0);          //Moisture percentage JSON object depicts the water presentaiontion in soil
  client.print("\", \"humidity\": \"");
  client.print(humidity,0);                    //Humidity JSON object depicts the humid level of place whers plants are exists
  client.print("\", \"temperature\": \"");
  client.print(temperature,0);                 //Temperature JSON object depicts the temperature of place whers plants are exists
  client.print("\"}");                    
  
  delay(1);
}
