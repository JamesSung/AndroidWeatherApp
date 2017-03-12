package com.cambrian.weatherapp.service;

import com.cambrian.weatherapp.data.Channel;
/**
 * Created by sung on 2017-03-08.
 */

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
