package com.cambrian.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by sung on 2017-03-08.
 */

public class Channel implements JsonPopulator{
    private Item item;
    private Units units;
    private Wind wind;
    private Astronomy astronomy;
    private Atmosphere atmosphere;

    @Override
    public void poupulate(JSONObject data) {
        units = new Units();
        units.poupulate(data.optJSONObject("units"));

        wind = new Wind();
        wind.poupulate(data.optJSONObject("wind"));

        atmosphere = new Atmosphere();
        atmosphere.poupulate(data.optJSONObject("atmosphere"));

        astronomy = new Astronomy();
        astronomy.poupulate(data.optJSONObject("astronomy"));

        item = new Item();
        item.poupulate(data.optJSONObject("item"));
    }

    public Item getItem() {
        return item;
    }

    public Units getUnits() {
        return units;
    }

    public Wind getWind() {
        return wind;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }
}
