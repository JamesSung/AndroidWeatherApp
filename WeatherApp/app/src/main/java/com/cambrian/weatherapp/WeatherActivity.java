package com.cambrian.weatherapp;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.cambrian.weatherapp.data.Astronomy;
import com.cambrian.weatherapp.data.Atmosphere;
import com.cambrian.weatherapp.data.Forecast;
import com.cambrian.weatherapp.data.Units;
import com.cambrian.weatherapp.data.Wind;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.cambrian.weatherapp.data.Channel;
import com.cambrian.weatherapp.data.Item;
import com.cambrian.weatherapp.service.WeatherServiceCallback;
import com.cambrian.weatherapp.service.YahooWeatherService;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIcon;
    private TextView temperature;
    private TextView condition;
    private  TextView location;
    private  TextView humidity;
    private TextView visibility;
    private TextView sunset;
    private TextView sunrise;

    private YahooWeatherService service;
    private ProgressDialog dialog;
    private SlidingUpPanelLayout slidingLayout;
    private ArrayList<Forecast> forecasts;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIcon = (ImageView)findViewById(R.id.weatherIconImageView);
        temperature = (TextView)findViewById(R.id.txtTemp);
        condition = (TextView)findViewById(R.id.txtCond);
        location = (TextView)findViewById(R.id.txtLoc);
        humidity = (TextView)findViewById(R.id.txtHumid);
        //visibility = (TextView)findViewById(R.id.txtVisib);
        sunset = (TextView)findViewById(R.id.txtSunset);
        sunrise = (TextView)findViewById(R.id.txtSunrise);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        //service.refreshWeather("Austin, TX");
        service.refreshWeather("Toronto, ON");//Default City

        //set layout slide listener
        slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        //slidingLayout.setPanelSlideListener(onSlideListener());
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Units units = channel.getUnits();
        Item item = channel.getItem();
        forecasts = item.getForecasts();
        Wind wind = channel.getWind();
        Astronomy astronomy = channel.getAstronomy();
        Atmosphere atmosphere = channel.getAtmosphere();
        int resourceId = getResources().getIdentifier("drawable/icon_"+String.valueOf(item.getCondition().getCode()), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrauable = getResources().getDrawable(resourceId);
        weatherIcon.setImageDrawable(weatherIconDrauable);
        if("F".equals(units.getTemperature())){
            String temp = item.getCondition().getCelsiusTemp() + " / " + wind.getCelsiusTemp() + " \u00B0C";
            temperature.setText(temp);
        }else {
            String temp = item.getCondition().getTemp() + " / " + wind.getChill() + " \u00B0C";
            temperature.setText(temp);
        }
        condition.setText(item.getCondition().getText());
        location.setText(service.getLocation());
        humidity.setText("Humidity is " + atmosphere.getHumidity() + ", Visibility is " + atmosphere.getVisibility() + units.getDistance());
        //visibility.setText("Visibility: " + atmosphere.getVisibility() + units.getDistance());
        sunrise.setText("Sunrise at " + astronomy.getSunrise() + ", Sunset at " + astronomy.getSunset());
        //sunset.setText("Sunset: " + astronomy.getSunset());

        DailyWeatherListAdapter adapter = new DailyWeatherListAdapter(
                this, R.layout.daily_list, new ArrayList<Forecast>());
        listView = (ListView) findViewById(R.id.dailyList);
        listView.setAdapter(adapter);

        new DailyWeatherTask().execute();
    }

    public void loadWeather(View view) {
        EditText city = (EditText) findViewById(R.id.edtxtCity);
        String strCity = city.getText().toString();
        //Toast.makeText(WeatherActivity.this,strCity,Toast.LENGTH_LONG).show();
        if(strCity == null || strCity.length() == 0){
            Toast.makeText(WeatherActivity.this,"Input City and State!",Toast.LENGTH_LONG).show();
            return;
        }else if(strCity.indexOf(",") == -1){
            Toast.makeText(WeatherActivity.this,"Input City and State(ex: Tronto, ON)!",Toast.LENGTH_LONG).show();
            return;
        }

        //service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        //service.refreshWeather("Toronto, ON");
        service.refreshWeather(strCity);
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(),Toast.LENGTH_LONG).show();
    }

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                //textView.setText("panel is sliding");
            }

            @Override
            public void onPanelCollapsed(View view) {
                //textView.setText("panel Collapse");
            }

            @Override
            public void onPanelExpanded(View view) {
                //textView.setText("panel expand");
            }

            @Override
            public void onPanelAnchored(View view) {
                //textView.setText("panel anchored");
            }

            @Override
            public void onPanelHidden(View view) {
                //textView.setText("panel is Hidden");
            }
        };
    }

    class DailyWeatherTask extends AsyncTask<Void, Forecast, String> {

        ArrayAdapter<Forecast> adapter;

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<Forecast>)listView.getAdapter();
        }

        @Override
        protected String doInBackground(Void... params) {
            if(forecasts == null)
                return "No Daily Weather Info Founds";

            for(Forecast forecast : forecasts){
                publishProgress(forecast);
//                try{
//                    Thread.sleep(100);
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }
            }
            return "All Daily Weather Were Loaded";
        }

        @Override
        protected void onProgressUpdate(Forecast... values) {
            //super.onProgressUpdate(values);
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }
}
