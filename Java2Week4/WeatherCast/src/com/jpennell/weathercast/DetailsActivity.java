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
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jpennell.library.DetailFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class DetailsActivity.
 */
public class DetailsActivity extends SherlockFragmentActivity {

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


            DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_frag);
            fragment.displayDetails(date, desc, low, hi, wind);
        }
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content
        setContentView(R.layout.detail_frag);

        // ActionBarSherlock
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        // Call getData method
        getData();

    }


    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
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