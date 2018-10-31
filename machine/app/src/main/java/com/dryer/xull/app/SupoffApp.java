package com.dryer.xull.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

public class SupoffApp extends Application {

	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();

	public static String currentUserNick = "";

	public static Context appContext;
	private String TAG = "SupoffApp";



	@Override
	public void onCreate() {
		super.onCreate();
		appContext = this.getApplicationContext();

	}

}
