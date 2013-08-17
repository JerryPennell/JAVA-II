/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 7, 2013
 */
package com.jpennell.weathercast;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.jpennell.library.Web;
import com.jpennell.library.FileSystem;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

// TODO: Auto-generated Javadoc
/**
 * The Class WeatherService.
 */
public class WeatherService extends IntentService {
	
    //Global Variables
    /** The final url. */
    URL finalURL = null;
    
    /** The _requested zip. */
    String _requestedZip = null;
    
    /** The _response. */
    String _response = null;

    /**
     * Instantiates a new weather service.
     */
    public WeatherService() {
        super("WeatherService");
    }

    /* (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("onHandleIntent", "Service started");

        Bundle extras = intent.getExtras();
        if (extras != null) {
            // Zip code that was entered into the editText
            _requestedZip = (String) extras.get("zip");
        }

        // Get requested URL
        String baseUrl = "http://api.worldweatheronline.com/free/v1/weather.ashx";
        String apiKey = "qsxcvw8kpztq9hpwjsm3yaa6";
        String qs = "";
        try {
            qs = URLEncoder.encode(_requestedZip, "UTF-8");
        } catch (Exception e) {
            Log.e("BAD URL", "ENCODING PROBLEM");
        }

        try {
            finalURL = new URL(baseUrl + "?q=" + qs + "&format=json&key=" + apiKey);
            _response = Web.getURLStringResponse(finalURL);
            Log.i("FINAL URL", _response);

            // Save to internal storage
            FileSystem.storeStringFile(this, "temp", _response, false);
        } catch (MalformedURLException e) {
            Log.e("BAD URL", "MalformedURLException");
            finalURL = null;
        }
        Log.i("onHandleIntent", "Service is done.");

        // Create Message
        Messenger messenger = (Messenger) extras.get("messenger");
        Message message = Message.obtain();
        if (message != null) {
            message.arg1 = Activity.RESULT_OK;
            message.obj = _response;
        }

        // Send message to MainActivity
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            Log.e("onHandleIntent", e.getMessage());
        }
    }

}