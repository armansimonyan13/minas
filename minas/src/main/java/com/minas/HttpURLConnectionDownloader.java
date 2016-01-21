package com.minas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author armansimonyan
 */
public class HttpURLConnectionDownloader implements Downloader {

	private HttpURLConnection connection = null;
	private InputStream inputStream = null;

	private volatile boolean isCanceled;

	@Override
	public Bitmap download(String url) throws DownloadException {
		Bitmap bitmap = null;
		try {
			connection = getHttpURLConnection(url);
			if (isCanceled) {
				close();
				return null;
			}
			connection.connect();

			final int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			} else {
				inputStream = connection.getErrorStream();
				throw new IOException("Connection received error code: " + responseCode);
			}
		} catch (IOException e) {
			throw new DownloadException(e);
		} finally {
			close();
		}
		return bitmap;
	}

	@Override
	public void cancel() {
		isCanceled = true;
		close();
	}

	protected HttpURLConnection getHttpURLConnection(String url) throws IOException {
		return (HttpURLConnection) new URL(url).openConnection();
	}

	private void close() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			connection.disconnect();
		}
	}

}
