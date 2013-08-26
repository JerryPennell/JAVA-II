/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 20, 2013
 */
package com.jpennell.weathercast;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.jpennell.library.FileSystem;
import com.jpennell.library.Singleton;
import com.jpennell.library.StorageParser;
import com.jpennell.library.WeatherContentProvider;
import com.jpennell.library.Web;


// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends SherlockActivity {


    /** The _is connected. */
    Boolean _isConnected = false;
    
    /** The _list view. */
    ListView _listView;
    
    /** The Constant DAY_PICKED_REQUEST. */
    static final int DAY_PICKED_REQUEST = 1;

    /**
     * Creates the layout.
     */
    @SuppressLint("HandlerLeak")
	public void createLayout() {
    	
    	//Setting Context from singleton class
    	Singleton.getInstance().setContext(this);

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
                                    Toast toast = Toast.makeText(Singleton.getInstance().getContext(),"Invalid location", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    // Get JSON data to determine correct zip code
                                    String request = data.getJSONArray("request").getJSONObject(0).getString("query");

                                    // Create toast (popup)
                                    Toast toast = Toast.makeText(Singleton.getInstance().getContext(),"Valid location, " + request, Toast.LENGTH_SHORT);
                                    toast.show();

                                    // Query content provider
                                    Cursor cursor = getContentResolver().query(WeatherContentProvider.WeatherData.CONTENT_URI, null, null, null, null);

                                    // Call displayCurrent method
                                    displayCurrent();

                                    // Call displayFiveDay method
                                    displayFiveDay(cursor);
                                }
                            } catch (JSONException e) {
                                Log.e("JSON", e.toString());
                            }
                        }
                    }
                };
                // Create Messenger Class
                Messenger myMessenger = new Messenger(myHandler);

                Intent intent = new Intent(Singleton.getInstance().getContext(),WeatherService.class);
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
        _isConnected = Web.getConnectionStatus(Singleton.getInstance().getContext());
        if (_isConnected) {
            Log.i("NETWORK CONNECTION", Web.getConnectionType(Singleton.getInstance().getContext()));

            // Enable button
            searchButton.setClickable(true);

        } else {
            // Alert if not connected
            AlertDialog.Builder alert = new AlertDialog.Builder(Singleton.getInstance().getContext());
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
     * Display current.
     */
    public void displayCurrent() {
        // Display current condition via parsing stored json string
        String readStorage = FileSystem.readStringFile(Singleton.getInstance().getContext(), "temp", false);

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
            ((TextView) findViewById(R.id.textView)).setText(ccTemp + " ¼F");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display five day.
     *
     * @param cursor the cursor
     */
    public void displayFiveDay(Cursor cursor) {
        // Display weather data via the content provider
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                HashMap<String, String> displayMap = new HashMap<String, String>();

                // Get values for each day
                displayMap.put("date", cursor.getString(1));
                displayMap.put("desc", cursor.getString(2));
                displayMap.put("hi", cursor.getString(3));
                displayMap.put("low", cursor.getString(4));
                displayMap.put("wind", cursor.getString(5));

                cursor.moveToNext();

                dataList.add(displayMap);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(Singleton.getInstance().getContext(), dataList, R.layout.list_row, new String[] {"desc", "hi", "low"}, new int[] {R.id.desc, R.id.tempLow, R.id.tempHi});

        // Set adapter to listView
        _listView.setAdapter(adapter);

        // Call selectDay method to allow the user to click a row
        selectDay();
    }


    /**
     * Select day.
     */
    private void selectDay() {
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("ROW CLICKED", "Row " + i + " clicked");

                @SuppressWarnings("unchecked")
				HashMap<String, String> detailMap = (HashMap<String, String>) _listView.getItemAtPosition(i);

                Intent detailIntent = new Intent(Singleton.getInstance().getContext(), DetailsActivity.class);
                // Put extra
                detailIntent.putExtra("detailDate", detailMap.get("date"));
                detailIntent.putExtra("detailDesc", detailMap.get("desc"));
                detailIntent.putExtra("detailHi", detailMap.get("hi"));
                detailIntent.putExtra("detailLow", detailMap.get("low"));
                detailIntent.putExtra("detailWind", detailMap.get("wind"));

                Log.i("INTENT", "Works");

                // Start Activity for results
                startActivityForResult(detailIntent,DAY_PICKED_REQUEST);
            }
        });
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
     * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
    return true;
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == DAY_PICKED_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle result = data.getExtras();
                if (result != null) {
                    String date = result.getString("date");

                    Toast toast = Toast.makeText(Singleton.getInstance().getContext(), "You checked the weather for " + date, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

}