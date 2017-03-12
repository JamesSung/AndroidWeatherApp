package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-12.
 */

public class Wind implements JsonPopulator{
    private int chill; //-2
    private String direction; //90
    private String speed; //4

    @Override
    public void poupulate(JSONObject data) {
        chill = data.optInt("chill");
        direction = data.optString("direction");
        speed = data.optString("speed");
    }

    public int getChill() {
        return chill;
    }

    public int getCelsiusTemp(){
        return (chill - 32) * 5 / 9;
    }

    public String getDirection() {
        return direction;
    }

    public String getSpeed() {
        return speed;
    }
}
