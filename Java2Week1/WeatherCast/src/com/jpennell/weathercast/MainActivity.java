/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 6, 2013
 */
package com.jpennell.weathercast;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jpennell.Library.FileSystem;
import com.jpennell.Library.StorageParser;
import com.jpennell.Library.Web;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

    //Global variables
    /** The _context. */
    Context _context;
    
    /** The _is connected. */
    Boolean _isConnected = false;


    /**
     * Creates the layout.
     */
    public void createLayout() {
        // Declare variables
        _context = this;

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
                    //getWeather(field.getText().toString());

                    // Call WeatherService
                    Handler weatherHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            if (message.arg1 == RESULT_OK && message.obj != null) {
                                String confirmedURL = message.obj.toString();
                                Log.i("URL RESPONSE", confirmedURL);

                                try {
                                    // Pull JSON data from API
                                    JSONObject json = new JSONObject(confirmedURL);
                                    JSONObject data = json.getJSONObject("data");
                                    Boolean error = data.has("error");
                                    if (error) {
                                        // Create toast (popup)
                                        Toast toast = Toast.makeText(_context,"Bad Zip", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        // Get JSON data to determine correct zip code
                                        String request = json.getJSONObject("data").getJSONArray("request").getJSONObject(0).getString("query");

                                        // Create toast (popup)
                                        Toast toast = Toast.makeText(_context,"Valid Zip, " + request, Toast.LENGTH_SHORT);
                                        toast.show();

                                        // Return data from storage and display in UI
                                        readAndDisplay();
                                    }
                                } catch (JSONException e) {
                                    Log.e("JSON", e.toString());
                                }
                            }
                        }
                    };
                    // Create Messenger Class
                    Messenger myMessenger = new Messenger(weatherHandler);

                    Intent intent = new Intent(_context,WeatherService.class);
                    intent.putExtra("messenger", myMessenger);
                    intent.putExtra("zip", field.getText().toString());

                    // Start Intent
                    startService(intent);
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
     * Read and display.
     */
    public void readAndDisplay() {
        // Return data from storage and display in UI
        String readStorage = FileSystem.readStringFile(_context, "temp", false);
        String info[] = readStorage.split(",");

        // Set values in setWeatherInfo
        setWeatherInfo(StorageParser.getDescImage(info), StorageParser.getTempF(info), StorageParser.getTempC(info), StorageParser.getHumidityText(info), StorageParser.getWindSpeedText(info), StorageParser.getWindDirText(info));
    }

    /**
     * Sets the weather info.
     *
     * @param descImage the desc image
     * @param tempFText the temp f text
     * @param tempCText the temp c text
     * @param humidityText the humidity text
     * @param windSpeedText the wind speed text
     * @param windDirText the wind dir text
     */
    public void setWeatherInfo(Integer descImage, String tempFText, String tempCText, String humidityText, String windSpeedText, String windDirText) {
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