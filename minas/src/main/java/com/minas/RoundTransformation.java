package com.minas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author armansimonyan
 */
public class RoundTransformation implements Transformation {

	@Override
	public Bitmap transform(Bitmap bitmap) {
		final double width = bitmap.getWidth();
		final double height = bitmap.getHeight();
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final float diameter = (float) Math.min(width, height);
		final double x = Math.ceil((width - diameter) / 2);
		final double y = Math.ceil((height - diameter) / 2);
		final Rect srcRect = new Rect((int) x, (int) y, (int) diameter, (int) diameter);
		final RectF dstRect = new RectF(0, 0, diameter, diameter);

		Bitmap roundedBitmap = Bitmap.createBitmap((int) diameter, (int) diameter, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(roundedBitmap);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(dstRect, diameter / 2, diameter / 2, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, srcRect, dstRect, paint);

		bitmap.recycle();

		return roundedBitmap;
	}

	@Override
	public String name() {
		return "round";
	}

}
