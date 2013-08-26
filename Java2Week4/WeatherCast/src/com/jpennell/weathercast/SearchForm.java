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

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchForm.
 */
@SuppressLint("ViewConstructor")
public class SearchForm extends LinearLayout {

    //Create variables
    /** The _search field. */
    EditText _searchField;
    
    /** The _lp. */
    LayoutParams _lp;
    
    /** The _search button. */
    Button _searchButton;

    /**
     * Instantiates a new search form.
     *
     * @param context the context
     * @param hint the hint
     * @param buttonText the button text
     */
    public SearchForm(Context context, String hint, String buttonText) {
        super(context);
        // Set layout params
        _lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(_lp);

        // Declare variables
        _searchField = new EditText(context);
        _lp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        _searchField.setLayoutParams(_lp);
        _searchField.setHint(hint);
        // Sets keyboard to numbers only
        _searchField.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        _searchButton = new Button(context);
        _searchButton.setText(buttonText);

        // Add elements to view
        this.addView(_searchField);
        this.addView(_searchButton);
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    public EditText getField() {
        return _searchField;
    }

    /**
     * Gets the button.
     *
     * @return the button
     */
    public Button getButton() {
        return _searchButton;
    }
}
