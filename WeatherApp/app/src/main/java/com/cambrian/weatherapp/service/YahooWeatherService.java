package com.cambrian.weatherapp.service;

import android.net.Uri;
import android.os.AsyncTask;

import com.cambrian.weatherapp.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sung on 2017-03-08.
 */

public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    public YahooWeatherService(WeatherServiceCallback callback){
        this.callback = callback;
    }

    public void refreshWeather(final String loc){
        this.location = loc;

        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... params) {

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", params[0]);
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }
                    return result.toString();

                }catch(MalformedURLException e){
                    e.printStackTrace();
                    error = e;
                } catch (IOException e) {
                    e.printStackTrace();
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                if(s == null && error != null){
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);
                    JSONObject queryResult = data.optJSONObject("query");
                    int count = queryResult.optInt("count");
                    if(count == 0){
                        callback.serviceFailure(new LocalException("No weather information found"));
                        return;
                    }

                    Channel channel = new Channel();
                    channel.poupulate(queryResult.optJSONObject("results").optJSONObject("channel"));

                    callback.serviceSuccess(channel);
                } catch (JSONException e) {
                    //e.printStackTrace();
                    callback.serviceFailure(e);
                }
            }
        }.execute(location);
    }

    public String getLocation() {
        return location;
    }

    public class LocalException extends Exception{
        public LocalException(String message){
            super(message);
        }
    }
}
