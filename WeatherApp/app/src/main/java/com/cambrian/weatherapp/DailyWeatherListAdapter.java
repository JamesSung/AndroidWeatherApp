package com.cambrian.weatherapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cambrian.weatherapp.data.Forecast;

import java.util.List;

public class DailyWeatherListAdapter extends ArrayAdapter<Forecast> {

    private List<Forecast> forecasts;

    public DailyWeatherListAdapter(Context context, int resource, List<Forecast> objects) {
        super(context, resource, objects);
        forecasts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.daily_list, parent, false);
        }

        Forecast forecast = forecasts.get(position);

        ImageView imgView = (ImageView)convertView.findViewById(R.id.dailyWeatherIcon);
        int resourceId = getContext().getResources().getIdentifier("drawable/icon_"+String.valueOf(forecast.getCode()), null, getContext().getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrauable = parent.getResources().getDrawable(resourceId);
        imgView.setImageDrawable(weatherIconDrauable);

        TextView dayText = (TextView) convertView.findViewById(R.id.txtDay);
        dayText.setText(forecast.getDay());

        String temp = String.valueOf(forecast.getCelsiusHigh()) + " / " + String.valueOf(forecast.getCelsiusLow()) + " \u00B0C";
        TextView ageText = (TextView) convertView.findViewById(R.id.txtDailyTemp);
        ageText.setText(temp);

        TextView condText = (TextView) convertView.findViewById(R.id.txtDailyCond);
        condText.setText(forecast.getText());

        return convertView;
    }

}
