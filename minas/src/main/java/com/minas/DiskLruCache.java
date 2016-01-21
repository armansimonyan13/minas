package com.minas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author armansimonyan
 */
public class DiskLruCache implements LruCache {

	private static final boolean DEBUG = true;

	private static final String CACHE_DIR_NAME = "MINAS_CACHE";

	private File cacheDir;

	private int maxSize;
	private final Logger logger;

	private final Object lock = new Object();

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	public DiskLruCache(Context context, int maxSize, Logger logger) {
		cacheDir = new File(context.getFilesDir(), CACHE_DIR_NAME);
		this.maxSize = maxSize;
		this.logger = logger;
	}

	@Override
	public void put(final String key, final Bitmap bitmap) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				long startTimeMillis = System.currentTimeMillis();
				try {
					synchronized (lock) {
						FileOutputStream fos = null;
						try {
							File file = new File(cacheDir, key);
							if (!cacheDir.exists()) {
								cacheDir.mkdirs();
							}
							fos = new FileOutputStream(file);
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
							fos.flush();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						clean();
					}
				} finally {
					logger.log("DiskLruCache: put() finished in " + (System.currentTimeMillis() - startTimeMillis) + "ms");
				}
			}
		});
	}

	@Override
	public Bitmap get(String key) {
		long startTimeMillis;
		if (DEBUG) {
			startTimeMillis = System.currentTimeMillis();
		}
		try {
			synchronized (lock) {
				File[] files = cacheDir.listFiles();
				if (files == null) {
					return null;
				}
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().equals(key)) {
						try {
							File file = new File(cacheDir, key);
							update(key);
							return BitmapFactory.decodeStream(new FileInputStream(file));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				return null;
			}
		} finally {
			if (DEBUG) {
				logger.log("DiskLruCache: get() finished in " + (System.currentTimeMillis() - startTimeMillis) + "ms");
			}
		}
	}

	private void clean() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				long startTimeMillis = System.currentTimeMillis();
				try {
					synchronized (lock) {
						long oldestFileDate = Long.MAX_VALUE;
						int oldestFileIndex = -1;
						int size = 0;
						File[] files = cacheDir.listFiles();
						for (int i = 0; i < files.length; i++) {
							size += files[i].length();
						}
						while (size > maxSize) {
							for (int i = 0; i < files.length; i++) {
								if (files[i].lastModified() < oldestFileDate) {
									oldestFileDate = files[i].lastModified();
									oldestFileIndex = i;
								}
							}
							if (oldestFileIndex != -1) {
								File file = files[oldestFileIndex];
								size -= file.length();
								file.delete();
							}
						}
					}
				} finally {
					logger.log("DiskLruCache: clean() finished in " + (System.currentTimeMillis() - startTimeMillis) + "ms");
				}
			}
		});
	}

	private void update(final String key) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				long startTimeMillis = System.currentTimeMillis();
				try {
					synchronized (lock) {
						long newLastModifiedDate = System.currentTimeMillis();
						File[] files = cacheDir.listFiles();
						for (int i = 0; i < files.length; i++) {
							if (files[i].getName().equals(key)) {
								files[i].setLastModified(newLastModifiedDate);
							}
						}
					}
				} finally {
					logger.log("DiskLruCache: update() finished in " + (System.currentTimeMillis() - startTimeMillis) + "ms");
				}
			}
		});
	}

}
