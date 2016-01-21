package com.minas;

import android.graphics.Bitmap;

/**
 * @author armansimonyan
 */
public interface LruCache {

	void put(String key, Bitmap bitmap);

	Bitmap get(String key);

}
