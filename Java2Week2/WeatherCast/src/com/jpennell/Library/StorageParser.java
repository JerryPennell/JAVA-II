/*
 * project		WeatherCast
 * 
 * package		com.jpennell.library
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 6, 2013
 */
package com.jpennell.library;

import com.jpennell.weathercast.R;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class StorageParser.
 */
public class StorageParser {

	    public static Integer getDescImage (String descText) {
	        Log.i("DESCRIPTION", descText);

	        Integer _image;

	        // Set description image
	        if (descText.equals("Sunny")) {
	            _image = R.drawable.sunny;
	        } else if (descText.equals("Partly Cloudy")) {
	            _image = R.drawable.partly_cloudy;
	        } else if (descText.equals("Overcast") || descText.equals("Cloudy")) {
	            _image = R.drawable.overcast;
	        } else {
	            _image = R.drawable.warning;
	        }

	        return _image;
	    }
}