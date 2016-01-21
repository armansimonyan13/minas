package com.minas;

import android.util.Log;

/**
 * @author armansimonyan
 */
public class DefaultLogger implements Logger {

	private static String TAG = "MINAS";

	@Override
	public void log(String message) {
		Log.d(TAG + ": ", message);
	}

}
