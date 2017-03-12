package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-12.
 */

public class Astronomy implements JsonPopulator{
    private String sunrise;
    private String sunset;

    @Override
    public void poupulate(JSONObject data) {
        sunrise = data.optString("sunrise");
        sunset = data.optString("sunset");
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
