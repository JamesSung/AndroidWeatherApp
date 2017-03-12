package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-08.
 */

public class Units implements JsonPopulator {
    private String temperature;
    private String speed;
    private String pressure;
    private String distance;

    @Override
    public void poupulate(JSONObject data) {
        temperature = data.optString("temperature");
        speed = data.optString("speed");
        pressure = data.optString("pressure");
        distance = data.optString("distance");
    }

    public String getDistance() {
        return distance;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getSpeed() {
        return speed;
    }

    public String getPressure() {
        return pressure;
    }
}
