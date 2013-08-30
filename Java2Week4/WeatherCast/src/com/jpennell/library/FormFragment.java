/*
 * project		WeatherCast
 * 
 * package		com.jpennell.weathercast
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 27, 2013
 */
package com.jpennell.library;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jpennell.weathercast.R;

// TODO: Auto-generated Javadoc
/**
 * The Class FormFragment.
 */
public class FormFragment extends SherlockFragment {
    
    /** The listener. */
    private formListener listener;

    /**
     * The listener interface for receiving form events.
     * The class that is interested in processing a form
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addformListener<code> method. When
     * the form event occurs, that object's appropriate
     * method is invoked.
     *
     * @see formEvent
     */
    public interface formListener {
        
        /**
         * On weather search.
         */
        public void onWeatherSearch ();
        
        /**
         * On single day selected.
         *
         * @param i the i
         */
        public void onSingleDaySelected(Integer i);
    }


    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (formListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }


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

        // Create view
        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.form, container, false);

        // Add "Get Weather" button from MainActivity here
        Button searchButton = (Button) view.findViewById(com.jpennell.weathercast.R.id.searchButton);

        // OnClickListener
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onWeatherSearch();
            }
        });

        // Attach list adapter
        ListView listView = (ListView) view.findViewById(R.id.list);


        // OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onSingleDaySelected(i);
            }
        });

        return view;
    }


    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        menu.add("Web").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }
}