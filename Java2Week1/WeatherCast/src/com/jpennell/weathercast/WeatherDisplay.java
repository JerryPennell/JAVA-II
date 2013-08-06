/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Jul 23, 2013
 */
package com.jpennell.weathercast;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.TextView;


// TODO: Auto-generated Javadoc
/**
 * The Class WeatherDisplay.
 */
public class WeatherDisplay extends GridLayout {

    //Create variables
    /** The _context. */
    Context _context;
    
    /** The _description. */
    TextView _description;
    
    /** The _temp c. */
    TextView _tempC;
    
    /** The _temp f. */
    TextView _tempF;
    
    /** The _humidity. */
    TextView _humidity;
    
    /** The _wind speed. */
    TextView _windSpeed;
    
    /** The _wind direction. */
    TextView _windDirection;

    /**
     * Instantiates a new weather display.
     *
     * @param context the context
     */
    public WeatherDisplay(Context context) {
        super(context);

        _context = context;

        // Sets the number of columns in the GridLayout
        this.setColumnCount(2);

        // Set TextViews
        TextView descriptionLabel = new TextView(_context);
        descriptionLabel.setText(getResources().getString(R.string.description_label));
        _description = new TextView(_context);
        
        TextView tempfLabel = new TextView(_context);
        tempfLabel.setText(getResources().getString(R.string.tempf_label));
        _tempF = new TextView(_context);

        TextView tempcLabel = new TextView(_context);
        tempcLabel.setText(getResources().getString(R.string.tempc_label));
        _tempC = new TextView(_context);

        TextView humidityLabel = new TextView(_context);
        humidityLabel.setText(getResources().getString(R.string.humidity_label));
        _humidity = new TextView(_context);

        TextView windSpeedLabel = new TextView(_context);
        windSpeedLabel.setText(getResources().getString(R.string.wind_speed_label));
        _windSpeed= new TextView(_context);

        TextView windDirectionLabel = new TextView(_context);
        windDirectionLabel.setText(getResources().getString(R.string.wind_direction_label));
        _windDirection = new TextView(_context);

        // Add elements to view
        this.addView(descriptionLabel);
        this.addView(_description);
        this.addView(tempfLabel);
        this.addView(_tempF);
        this.addView(tempcLabel);
        this.addView(_tempC);
        this.addView(humidityLabel);
        this.addView(_humidity);
        this.addView(windSpeedLabel);
        this.addView(_windSpeed);
        this.addView(windDirectionLabel);
        this.addView(_windDirection);
    }

    /**
     * Sets the weather info.
     *
     * @param descriptionText the description text
     * @param tempFText the temp f text
     * @param tempCText the temp c text
     * @param humidityText the humidity text
     * @param windSpeedText the wind speed text
     * @param windDirText the wind dir text
     */
    public void setWeatherInfo(String descriptionText, String tempFText, String tempCText, String humidityText, String windSpeedText, String windDirText) {
        // Set text in GridLayout
        _description.setText(descriptionText);
        _tempF.setText(tempFText);
        _tempC.setText(tempCText);
        _humidity.setText(humidityText);
        _windSpeed.setText(windSpeedText);
        _windDirection.setText(windDirText);
    }
}