package com.cambrian.weatherapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cambrian.weatherapp.data.Astronomy;
import com.cambrian.weatherapp.data.Atmosphere;
import com.cambrian.weatherapp.data.Channel;
import com.cambrian.weatherapp.data.Forecast;
import com.cambrian.weatherapp.data.Item;
import com.cambrian.weatherapp.data.Units;
import com.cambrian.weatherapp.data.Wind;
import com.cambrian.weatherapp.service.WeatherServiceCallback;
import com.cambrian.weatherapp.service.YahooWeatherService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

/**
 * Created by sung on 2017-03-14.
 */

public class WeatherSlidePageFragment extends Fragment implements WeatherServiceCallback {

    String[] cities = {"Toronto, ON","Vancouver, BC","Quebec, QC","Ottawa, ON","Montreal, QC"};

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
    private EditText city;
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static WeatherSlidePageFragment create(int pageNumber) {
        WeatherSlidePageFragment fragment = new WeatherSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.weather_slide_page, container, false);

        city = (EditText)rootView.findViewById(R.id.edtxtCity);

        weatherIcon = (ImageView)rootView.findViewById(R.id.weatherIconImageView);
        temperature = (TextView)rootView.findViewById(R.id.txtTemp);
        condition = (TextView)rootView.findViewById(R.id.txtCond);
        location = (TextView)rootView.findViewById(R.id.txtLoc);
        humidity = (TextView)rootView.findViewById(R.id.txtHumid);
        //visibility = (TextView)rootView.findViewById(R.id.txtVisib);
        sunset = (TextView)rootView.findViewById(R.id.txtSunset);
        sunrise = (TextView)rootView.findViewById(R.id.txtSunrise);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading..");
        dialog.show();

        //service.refreshWeather("Austin, TX");
        service.refreshWeather(cities[mPageNumber]);//Default City

        slidingLayout = (SlidingUpPanelLayout)rootView.findViewById(R.id.sliding_layout);

        listView = (ListView) rootView.findViewById(R.id.dailyList);

        Button go = (Button)rootView.findViewById(R.id.btnGo);
        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadWeather(v);
            }
        });

        return rootView;
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
        int resourceId = getResources().getIdentifier("drawable/icon_"+String.valueOf(item.getCondition().getCode()), null, getActivity().getPackageName());

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
                getActivity(), R.layout.daily_list, new ArrayList<Forecast>());

        listView.setAdapter(adapter);

        new DailyWeatherTask().execute();
    }

    private void loadWeather(View view) {

        city.clearFocus();

        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(), 0);

        String strCity = city.getText().toString();

        if(strCity == null || strCity.length() == 0){
            Toast.makeText(getContext(),"Input City and State!",Toast.LENGTH_LONG).show();
            return;
        }
        /*else if(strCity.indexOf(",") == -1){
            Toast.makeText(WeatherActivity.this,"Input City and State(ex: Tronto, ON)!",Toast.LENGTH_LONG).show();
            return;
        }*/

        //service = new YahooWeatherService(this);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading..");
        dialog.show();

        //service.refreshWeather("Toronto, ON");
        service.refreshWeather(strCity);
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(getContext(), exception.getMessage(),Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
