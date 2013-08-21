/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 19, 2013
 */
package com.jpennell.weathercast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


// TODO: Auto-generated Javadoc
/**
 * The Class DetailsActivity.
 */
public class DetailsActivity extends SherlockActivity {

    /** The date. */
    String date;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public void getData() {
        Log.i("DETAIL ACTIVITY", "Running!");
        Bundle data = getIntent().getExtras();

        if (data != null) {
            date = data.getString("detailDate");
            String desc = data.getString("detailDesc");
            String hi = data.getString("detailHi");
            String low = data.getString("detailLow");
            String wind = data.getString("detailWind");

            Log.i("DETAIL STRINGS", date + ", " + desc + ", " + hi + ", " + low + ", " + wind);

            // Set layout elements
            ((TextView) findViewById(R.id.detailDate)).setText(date);
            ((TextView) findViewById(R.id.detailDesc)).setText(desc);
            ((TextView) findViewById(R.id.low_description)).setText(low);
            ((TextView) findViewById(R.id.hi_description)).setText(hi);
            ((TextView) findViewById(R.id.wind_description)).setText(wind);
        }
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content
        setContentView(R.layout.details);

        // ActionBarSherlock
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Call getData method
        getData();

    }


    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);

        menu.add("Web").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.i("item ID : ", "onOptionsItemSelected Item ID" + id);

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ACTION BAR", "Up navigation pressed");

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case 0:
                Log.i("ACTION BAR", "Web navigation pressed");

                Intent implicitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.worldweatheronline.com"));
                
                startActivity(implicitIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* (non-Javadoc)
     * @see android.app.Activity#finish()
     */
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("date", date);

        setResult(RESULT_OK, data);
        super.finish();
    }
}