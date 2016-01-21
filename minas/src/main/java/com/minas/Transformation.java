package com.minas;

import android.graphics.Bitmap;

/**
 * Always call bitmap.recycle() if you return new bitmap
 *
 * @author armansimonyan
 */
public interface Transformation {

	Bitmap transform(Bitmap bitmap);

	String name();

}
