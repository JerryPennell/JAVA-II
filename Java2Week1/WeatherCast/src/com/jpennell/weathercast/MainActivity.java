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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpennell.Library.FileSystem;
import com.jpennell.Library.Web;


// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

    //Global variables
    /** The _context. */
    Context _context;
    
    /** The _history. */
    HashMap<String, String> _history;
    
    /** The _is connected. */
    Boolean _isConnected = false;


    /**
     * Creates the layout.
     */
    public void createLayout() {
        // Declare variables
        _context = this;
        _history = getHistory();

        Log.i("HISTORY READ", _history.toString());

        // Add Search Handler
        Button searchButton = (Button) findViewById(R.id.searchButton);

        // Create onClickListener for searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText field = (EditText) findViewById(R.id.searchField);
                Log.i("CLICK HANDLER", field.getText().toString());

                // Check to make sure entered value is valid zip
                if (field.getText().toString().length() != 5) {
                    // Create toast (popup)
                    Toast toast = Toast.makeText(_context,"Bad Zip", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // Call getWeather method
                    getWeather(field.getText().toString());
                }
            }
        });

        // Detect Network Connection
        _isConnected = Web.getConnectionStatus(_context);
        if (_isConnected) {
            Log.i("NETWORK CONNECTION", Web.getConnectionType(_context));

            // Enable button
            searchButton.setClickable(true);

        } else {
            // Alert if not connected
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("No Network Connection");
            alert.setMessage("Please check your network connections and try again.");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();

            // Disable button
            searchButton.setClickable(false);
        }
    }

    /**
     * Gets the weather.
     *
     * @param zip the zip
     * @return the weather
     */
    private void getWeather(String zip) {
        Log.i("CLICKED", zip);
       
        String baseUrl = "http://api.worldweatheronline.com/free/v1/weather.ashx";
        String apiKey = "qsxcvw8kpztq9hpwjsm3yaa6";
        String qs = "";
        try {
            qs = URLEncoder.encode(zip, "UTF-8");
        } catch (Exception e) {
            Log.e("BAD URL", "ENCODING PROBLEM");
        }

        URL finalURL;
        try {
            finalURL = new URL(baseUrl + "?q=" + qs + "&format=json&key=" + apiKey);
            // Call weatherRequest method
            weatherRequest wr = new weatherRequest();
            wr.execute(finalURL);
        } catch (MalformedURLException e) {
            Log.e("BAD URL", "MalformedURLException");
            finalURL = null;
        }
    }

    /**
     * Gets the history.
     *
     * @return the history
     */
    @SuppressWarnings("unchecked")
	private HashMap<String, String> getHistory() {
        Object stored = FileSystem.readObjectFile(_context, "history", false);

        HashMap<String, String> history;
        if (stored == null) {
            Log.i("HISTORY", "NO HISTORY FILE FOUND");
            history = new HashMap<String, String>();
        } else {
            history = (HashMap<String, String>) stored;
        }
        return history;
    }

    /**
     * The Class weatherRequest.
     */
    private class weatherRequest extends AsyncTask<URL, Void, String> {

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected String doInBackground(URL...urls) {
            String response = "";
            for (URL url:urls) {
                response = Web.getURLStringResponse(url);
            }
            return response;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i("URL RESPONSE", result);

            try {
                // Pull JSON data from API
                JSONObject json = new JSONObject(result);
                JSONObject data = json.getJSONObject("data");
                Boolean error = data.has("error");
                if (error) {
                    // Create toast (popup)
                    Toast toast = Toast.makeText(_context,"Bad Zip", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // Get JSON data
                    JSONArray results = json.getJSONObject("data").getJSONArray("current_condition");
                    String request = json.getJSONObject("data").getJSONArray("request").getJSONObject(0).getString("query");

                    // Get values from JSON to display
                    String _description = results.getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
                    String _tempC = results.getJSONObject(0).getString("temp_C");
                    String _tempF = results.getJSONObject(0).getString("temp_F");
                    String _humidity = results.getJSONObject(0).getString("humidity");
                    String _windSpeed = results.getJSONObject(0).getString("windspeedMiles");
                    String _windDirection = results.getJSONObject(0).getString("winddir16Point");
                    Integer _image = null;

                    // Set description image
                    if (_description.equals("Sunny")) {
                        _image = R.drawable.sunny;
                    } else if (_description.equals("Partly Cloudy")) {
                        _image = R.drawable.partly_cloudy;
                    } else if (_description.equals("Overcast") || _description.equals("Cloudy")) {
                        _image = R.drawable.overcast;
                    } else {
                        _image = R.drawable.warning;
                    }

                    Log.i("RESULTS", results.toString());
                    Log.i("REQUEST", request);
                    Log.i("WEATHER VALUES", _description + _tempF + _tempC + _humidity + _windSpeed + _windDirection);

                    // Set values in WeatherDisplay
                    setWeatherInfo(_image, _tempF, _tempC, _humidity, _windSpeed, _windDirection);

                    // Create toast (popup)
                    Toast toast = Toast.makeText(_context,"Valid Zip, " + request, Toast.LENGTH_SHORT);
                    toast.show();

                    // Adds JSON data to internal and external storage
                    _history.put(request, results.toString());
                    // Saves to internal
                    FileSystem.storeObjectFile(_context, "history", _history, false);
                    // Saves to external
                    FileSystem.storeStringFile(_context, "temp", results.toString(), true);
                }
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }
        }
    }
    
    

    /**
     * Sets the weather info.
     *
     * @param descImage the desc image
     * @param tempCText the temp c text
     * @param tempFText the temp f text
     * @param humidityText the humidity text
     * @param windSpeedText the wind speed text
     * @param windDirText the wind dir text
     */
    public void setWeatherInfo(Integer descImage , String tempFText, String tempCText, String humidityText, String windSpeedText, String windDirText) {
        // Set TextView in GridLayout
        ((ImageView)findViewById(R.id.data_image)).setImageResource(descImage);
        ((TextView) findViewById(R.id.data_tempF)).setText(tempFText);
        ((TextView) findViewById(R.id.data_tempC)).setText(tempCText);
        ((TextView) findViewById(R.id.data_humidity)).setText(humidityText);
        ((TextView) findViewById(R.id.data_windSpeed)).setText(windSpeedText);
        ((TextView) findViewById(R.id.data_windDirection)).setText(windDirText);
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content from XML layout
        setContentView(R.layout.form);

        // Call createLayout method
        createLayout();
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}