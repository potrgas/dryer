package com.dryer.xull.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.dryer.xull.app.SupoffApp;

public class ScreenUtil {

	public static int getScreenHeight() {
		DisplayMetrics mMetrics = SupoffApp.appContext.getResources().getDisplayMetrics();
		return mMetrics.heightPixels;
	}

	public static int getScreenWidth() {
		DisplayMetrics mMetrics = SupoffApp.appContext.getResources().getDisplayMetrics();
		return mMetrics.widthPixels;
	}

}
