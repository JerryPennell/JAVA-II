/*
 * project		WeatherCast
 * 
 * package		com.jpennell.library
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 26, 2013
 */
package com.jpennell.library;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.jpennell.weathercast.R;


// TODO: Auto-generated Javadoc
/**
 * The Class DetailFragment.
 */
public class DetailFragment extends SherlockFragment {
    // Global variables
    /** The date view. */
    TextView dateView;
    
    /** The desc view. */
    TextView descView;
    
    /** The low view. */
    TextView lowView;
    
    /** The hi view. */
    TextView hiView;
    
    /** The wind view. */
    TextView windView;


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Allows items to work in ActionBar
        setHasOptionsMenu(true);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.details, container, false);

        // Get views by id
        if (view != null) {
            dateView = (TextView) view.findViewById(R.id.detailDate);
            descView = (TextView) view.findViewById(R.id.detailDesc);
            lowView = (TextView) view.findViewById(R.id.low_description);
            hiView = (TextView) view.findViewById(R.id.hi_description);
            windView = (TextView) view.findViewById(R.id.wind_description);
        }

        return view;
    }


    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }


    /**
     * Display details.
     *
     * @param date the date
     * @param desc the desc
     * @param low the low
     * @param hi the hi
     * @param wind the wind
     */
    public void displayDetails(String date, String desc, String low, String hi, String wind) {
        // Set layout elements
        dateView.setText(date);
        descView.setText(desc);
        lowView.setText(low);
        hiView.setText(hi);
        windView.setText(wind);
    }
}