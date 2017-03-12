package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-12.
 */

public class Forecast implements JsonPopulator{
    private String code;
    private String date; //"07 Mar 2017",
    private String day; //"Tue"
    private int high;
    private int low;
    private String text; //"Mostly Cloudy"


    @Override
    public void poupulate(JSONObject data) {
        code = data.optString("code");
        date = data.optString("date");
        day = data.optString("day");
        high = data.optInt("high");
        low = data.optInt("low");
        text = data.optString("text");
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }

    public int getCelsiusLow(){
        return (low - 32) * 5 / 9;
    }

    public int getCelsiusHigh(){
        return (high - 32) * 5 / 9;
    }

    public String getText() {
        return text;
    }

}
