from flask import Flask, request, jsonify, render_template, url_for
import requests

app = Flask(__name__)

def rainPrecipitation():

    latitude = "20.443478"
    longitude = "85.110119"

    urlRainPrecipitation = f"https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=precipitation"

    response = requests.get(urlRainPrecipitation)
    networkData = response.json()
    rainPrecipitation = networkData["current"]["precipitation"]

    return rainPrecipitation

@app.route('/get-sensor-input', methods=['GET', 'POST'])
def getSensorInput():
    result = str(rainPrecipitation())
    return result

if __name__ == "__main__":
    app.debug = True
    app.run()