package com.cambrian.weatherapp.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * Created by sung on 2017-03-08.
 */

public class Item implements JsonPopulator {
    private Condition condition;
    private ArrayList<Forecast> forecasts;

    public Condition getCondition() {
        return condition;
    }

    public ArrayList<Forecast> getForecasts() {
        return forecasts;
    }

    @Override
    public void poupulate(JSONObject data) {
        condition = new Condition();
        condition.poupulate(data.optJSONObject("condition"));

        forecasts = new ArrayList<Forecast>();
        JSONArray array = null;
        try {
            array = data.getJSONArray("forecast");
            System.out.println("forecast count: " + array.length());
            for(int n = 0; n < array.length(); n++)
            {
                JSONObject object = array.getJSONObject(n);
                Forecast forecast = new Forecast();
                forecast.poupulate(object);
                forecasts.add(forecast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
