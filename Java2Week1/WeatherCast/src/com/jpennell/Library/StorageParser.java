/*
 * project		WeatherCast
 * 
 * package		com.jpennell.Library
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 6, 2013
 */
package com.jpennell.Library;

import com.jpennell.weathercast.R;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class StorageParser.
 */
public class StorageParser {

    /**
     * Gets the desc image.
     *
     * @param info the info
     * @return the desc image
     */
    public static Integer getDescImage (String[] info) {
        String _description = info[9].split(":")[2];

        // Get rid of extra characters
        String _descText =_description.replaceAll("[^A-Za-z0-9 ]", "");

        Integer _image;

        Log.i("DESCRIPTION", _descText);

        // Set description image
        if (_descText.equals(" Sunny  ")) {
            _image = R.drawable.sunny;
        } else if (_descText.equals(" Partly Cloudy  ")) {
            _image = R.drawable.partly_cloudy;
        } else if (_descText.equals(" Overcast  ") || _descText.equals(" Cloudy  ")) {
            _image = R.drawable.overcast;
        } else {
            _image = R.drawable.warning;
        }

        return _image;
    }

    /**
     * Gets the temp c.
     *
     * @param info the info
     * @return the temp c
     */
    public static String getTempC(String[] info) {
        String _tempC = info[5].split(":")[1];

        // Get rid of extra characters
        String _tempCText = _tempC.replace("\"", "");

        return _tempCText;
    }

    /**
     * Gets the temp f.
     *
     * @param info the info
     * @return the temp f
     */
    public static String getTempF(String[] info) {
        String _tempF = info[6].split(":")[1];

        // Get rid of extra characters
        String _tempFText = _tempF.replace("\"", "");

        return _tempFText;
    }

    /**
     * Gets the wind speed text.
     *
     * @param info the info
     * @return the wind speed text
     */
    public static String getWindSpeedText(String[] info) {
        String _windSpeed = info[14].split(":")[1];

        // Get rid of extra characters
        String _windSpeedText = _windSpeed.replaceAll("[^A-Za-z0-9 ]", "");

        return _windSpeedText;
    }

    /**
     * Gets the wind dir text.
     *
     * @param info the info
     * @return the wind dir text
     */
    public static String getWindDirText(String[] info) {
        String _windDirection = info[11].split(":")[1];

        // Get rid of extra characters
        String _windDirText = _windDirection.replace("\"", "");

        return _windDirText;
    }

    /**
     * Gets the humidity text.
     *
     * @param info the info
     * @return the humidity text
     */
    public static String getHumidityText(String[] info) {
        String _humidity = info[1].split(":")[1];

        // Get rid of extra characters
        String _humidityText = _humidity.replace("\"", "");

        return _humidityText;
    }
}