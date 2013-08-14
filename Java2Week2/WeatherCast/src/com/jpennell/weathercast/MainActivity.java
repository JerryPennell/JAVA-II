/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 6, 2013
 * updated      Aug 13, 2013
 */
package com.jpennell.weathercast;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jpennell.library.Web;
import com.jpennell.library.FileSystem;
import com.jpennell.library.StorageParser;
import com.jpennell.library.WeatherContentProvider;

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
    
    /** The _list view. */
    ListView _listView;
    
    /** The Constant IMAGE_VIEW. */
    static final String IMAGE_VIEW = "imageView";
    
    /** The Constant TEXT_VIEW. */
    static final String TEXT_VIEW = "textView";
    
    /** The Constant LIST_VIEW. */
    static final String LIST_VIEW = "listView";


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
                final EditText field = (EditText) findViewById(R.id.searchField);
                Log.i("CLICK HANDLER", field.getText().toString());

                Handler myHandler = new Handler() {
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
                                    Toast toast = Toast.makeText(_context,"Bad location or zip", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    // Get JSON data to determine correct zip code
                                    String request = data.getJSONArray("request").getJSONObject(0).getString("query");

                                    // Create toast (popup)
                                    Toast toast = Toast.makeText(_context,"Valid location, " + request, Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Query content provider
                                    Cursor cursor = getContentResolver().query(WeatherContentProvider.WeatherData.CONTENT_URI, null, null, null, null);
                                    // Call display method
                                    display(cursor);

                                    // Return data from storage and display in UI
                                    //readAndDisplay();
                                }
                            } catch (JSONException e) {
                                Log.e("JSON", e.toString());
                            }
                        }
                    }
                };
                // Create Messenger Class
                Messenger myMessenger = new Messenger(myHandler);

                Intent intent = new Intent(_context,WeatherService.class);
                intent.putExtra("messenger", myMessenger);
                intent.putExtra("zip", field.getText().toString());

                // Start Intent
                startService(intent);
                
                //Close out Keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
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
     * Display.
     *
     * @param cursor the cursor
     */
    public void display(Cursor cursor) {
        // Display current condition via parsing stored json string
        String readStorage = FileSystem.readStringFile(_context, "temp", false);

        JSONObject json;
        JSONObject data;
        JSONArray cc;

        try {
            // Current condition
            json = new JSONObject(readStorage);
            data = json.getJSONObject("data");
            cc = data.getJSONArray("current_condition");

            JSONObject ccObject = cc.getJSONObject(0);
            // Get description
            String ccDesc = ccObject.getJSONArray("weatherDesc").getJSONObject(0).getString("value");
            // Get temp
            String ccTemp = ccObject.getString("temp_F");

            // Convert description text with image and set current condition description image
            ((ImageView)findViewById(R.id.imageView)).setImageResource(StorageParser.getDescImage(ccDesc));
            ((TextView) findViewById(R.id.textView)).setText(ccTemp + " F");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Display weather data via the content provider
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                HashMap<String, String> displayMap = new HashMap<String, String>();

                // Get values for each day
                displayMap.put("description", cursor.getString(1));
                displayMap.put("temp", cursor.getString(2));

                cursor.moveToNext();

                dataList.add(displayMap);
                Log.i("DATA LIST", dataList.toString());
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(_context, dataList, R.layout.list_row, new String[] {"description", "temp"}, new int[] {R.id.desc, R.id.temp});

        // Set adapter to listView
        _listView.setAdapter(adapter);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Set content from XML layout
        setContentView(R.layout.form);
        _listView = (ListView) this.findViewById(R.id.list);
        View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
        _listView.addHeaderView(listHeader);

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