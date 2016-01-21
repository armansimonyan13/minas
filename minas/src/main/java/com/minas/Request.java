package com.minas;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Container for the all necessary information to download the image
 *
 * @author armansimonyan
 */
public class Request {

	private static final boolean DEBUG = true;

	private Minas minas;
	private String url;
	private String key;
	private int width;
	private int height;
	private Downloader downloader;
	private Logger logger;
	private List<Transformation> transformations;

	private volatile boolean isCanceled;

	private Source source;

	public Request(Minas minas, String url, String key, int width, int height, Downloader downloader, Logger logger, List<Transformation> transformations) {
		this.minas = minas;
		this.url = url;
		this.key = key;
		this.width = width;
		this.height = height;
		this.downloader = downloader;
		this.logger = logger;
		this.transformations = transformations;
	}

	public Minas minas() {
		return minas;
	}

	public Response execute() {
		try {
			Bitmap bitmap = load();
			if (bitmap != null) {
				return new Response(bitmap, source);
			} else {
				return new Response(new NullPointerException("bitmap is null"));
			}
		} catch (LoadException e) {
			return new Response(e);
		}
	}

	public Bitmap load() throws LoadException {
		long startTimeMillis;
		if (DEBUG) {
			startTimeMillis = System.currentTimeMillis();
		}
		try {
			source = null;

			String key = getKey();
			if (isCanceled) {
				return null;
			}
			Bitmap bitmap = minas.ramCache().get(key);

			if (isCanceled) {
				return null;
			}
			if (bitmap == null) {
				bitmap = minas.diskCache().get(key);
			} else {
				if (source == null) {
					source = Source.RAM;
				}
			}
			if (isCanceled) {
				return null;
			}
			if (bitmap == null) {
				bitmap = downloader.download(url);
				for (Transformation transformation : transformations) {
					if (bitmap != null) {
						bitmap = transformation.transform(bitmap);
					}
				}
			} else {
				if (source == null) {
					source = Source.DISK;
				}
			}
			if (bitmap != null) {
				if (source == null) {
					source = Source.NETWORK;
				}
				if (source != Source.RAM) {
					if (isCanceled) {
						return null;
					}
					minas.ramCache().put(key, bitmap);
				}
				if (source != Source.DISK) {
					if (isCanceled) {
						return null;
					}
					minas.diskCache().put(key, bitmap);
				}
			}
			return bitmap;
		} catch (Exception e) {
			throw new LoadException(e);
		} finally {
			if (DEBUG) {
				logger.log("Loader: load() finished in " + (System.currentTimeMillis() - startTimeMillis) + "ms");
			}
		}
	}

	public String getKey() {
		if (key != null) {
			return key;
		}
		String ts = "";
		for (Transformation t : transformations) {
			ts += "_" + t.name();
		}
		return url.replace("/", "_") + "_" + width + "x" + height + ts;
	}

	public void cancel() {
		isCanceled = true;
		downloader.cancel();
	}

}
