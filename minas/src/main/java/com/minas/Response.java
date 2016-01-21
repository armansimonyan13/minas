package com.minas;

import android.graphics.Bitmap;

/**
 * The result of execution of class @link{com.x.minas.Response}
 *
 * @author armansimonyan
 */
public class Response {

	private Bitmap bitmap;
	private Exception exception;
	private Source source;

	public Response(Bitmap bitmap, Source source) {
		if (bitmap == null) {
			throw new NullPointerException("bitmap is null");
		}
		if (source == null) {
			throw new NullPointerException("source is null");
		}
		this.bitmap = bitmap;
		this.source = source;
	}

	public Response(Exception exception) {
		if (exception == null) {
			throw new NullPointerException("exception is null");
		}
		this.exception = exception;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public Source getSource() {
		return source;
	}

	public Exception getException() {
		return exception;
	}

}
