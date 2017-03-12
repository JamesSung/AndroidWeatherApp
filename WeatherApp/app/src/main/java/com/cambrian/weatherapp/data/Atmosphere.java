package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-12.
 */

public class Atmosphere implements JsonPopulator{
    private String humidity;
    private String pressure;
    private String visibility;

    @Override
    public void poupulate(JSONObject data) {
        humidity = data.optString("humidity");
        pressure = data.optString("pressure");
        visibility = data.optString("visibility");
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }
}
