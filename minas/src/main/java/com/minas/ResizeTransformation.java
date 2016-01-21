package com.minas;

import android.graphics.Bitmap;

/**
 * @author armansimonyan
 */
public class ResizeTransformation implements Transformation {

	private int width;
	private int height;

	public ResizeTransformation(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Bitmap transform(Bitmap bitmap) {
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		bitmap.recycle();
		return scaledBitmap;
	}

	@Override
	public String name() {
		return "resize";
	}

}
