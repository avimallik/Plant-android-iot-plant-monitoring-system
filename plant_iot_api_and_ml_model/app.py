from flask import Flask, request, jsonify, render_template, url_for
import requests
import json

app = Flask(__name__)

# location's coordinate
latitude = "23.7702883"
longitude = "90.3984476"

# urls
urlRainPrecipitation = f"https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=precipitation"
# urlSensors = "https://jsonkeeper.com/b/39AUC"
urlSensors = "http://192.168.0.193/data"


def rainPrecipitation():
    responseRain = requests.get(urlRainPrecipitation)
    dataRain = responseRain.json()
    rainPrecipitation = dataRain["current"]["precipitation"]
    return rainPrecipitation

def getSensorData():
    responseSensor = requests.get(urlSensors)
    dataSensor = responseSensor.json()

    humidity_pct = dataSensor[0]["humidity_pct"]
    # soil_moisture_pct = dataSensor[0]["soil_moisture_pct"]
    temperature_c = dataSensor[0]["temperature_c"]

    # volumetric water content calculation
    rainPrecipitationTemp = rainPrecipitation()
    
    if rainPrecipitationTemp > 0:
        soil_moisture_pct = 50
    else:
        soil_moisture_pct = dataSensor[0]["soil_moisture_pct"]
    # calculation end

    sensorDict = {
                  "soil_moisture_pct" : soil_moisture_pct, 
                  "temperature_c": temperature_c, 
                  "humidity_pct" : humidity_pct,
                  "rain_precip" : rainPrecipitationTemp
                  }
    
    return sensorDict

@app.route('/display-sensor-data', methods=['GET', 'POST'])
def getSensorInput():
    result = jsonify(getSensorData())
    # tempHumid = str(result["humidity_pct"])
    return result

@app.route('/store_sensor_data', methods=['GET','POST'])
def storeSensorData():
    message = "under contruction!"
    return message

if __name__ == "__main__":
    app.debug = True
    app.run()