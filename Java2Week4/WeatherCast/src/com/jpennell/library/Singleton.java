/*
 * project		WeatherCast
 * 
 * package		com.jpennell.library
 * 
 * author		Jerry Pennell
 * 
 * date			Aug 22, 2013
 */
package com.jpennell.library;

import android.content.Context;

public class Singleton {
	private static Singleton mInstance = null;
 
	private Context _context;
 
	/**
	 * Instantiates a new singleton.
	 */
	private Singleton(){
		
	}
 
	public static Singleton getInstance(){
	if(mInstance == null)
	{
	mInstance = new Singleton();
	}
	return mInstance;
	}
	 
	public Context getContext(){
	return this._context;
	}
	 
	public void setContext(Context value){
	_context = value;
	}


}