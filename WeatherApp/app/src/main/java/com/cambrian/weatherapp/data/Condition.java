package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-08.
 */

public class Condition implements JsonPopulator {

    private int code;
    private int temp;
    private String text;
    private String date;


    @Override
    public void poupulate(JSONObject data) {
        code = data.optInt("code");
        temp = data.optInt("temp");
        text = data.optString("text");
        date = data.optString("date");
    }

    public int getCode() {
        return code;
    }

    public int getTemp() {
        return temp;
    }

    public int getCelsiusTemp(){
        return (temp - 32) * 5 / 9;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
