package com.minas;

import android.graphics.Bitmap;

/**
 * @author armansimonyan
 */
public interface Downloader {

	Bitmap download(String url) throws DownloadException;

	void cancel();

}
