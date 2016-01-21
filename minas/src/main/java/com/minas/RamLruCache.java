package com.minas;

import android.graphics.Bitmap;
import android.os.Build;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author armansimonyan
 */
public class RamLruCache implements LruCache {

	private LinkedHashMap<String, Bitmap> map = new LinkedHashMap<>();

	private int size;

	private final Object lock = new Object();

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	private int maxSize;

	public RamLruCache(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void put(String key, Bitmap bitmap) {
		synchronized (lock) {
			Bitmap b = map.get(key);
			if (b != null) {
				int s = sizeOf(b);
				size -= s;
			}
			size += sizeOf(bitmap);
			map.put(key, bitmap);
			clean();
		}
	}

	@Override
	public Bitmap get(String key) {
		synchronized (lock) {
			Bitmap bitmap = map.get(key);
			update(key);
			return bitmap;
		}
	}

	public int size() {
		return size;
	}

	public static int sizeOf(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return bitmap.getAllocationByteCount();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}

	private void clean() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				synchronized (lock) {
					for (Iterator<Map.Entry<String, Bitmap>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
						if (size < 0) {
							throw new AssertionError("size < 0");
						}
						if (size < maxSize) {
							break;
						}
						Bitmap bitmap = iterator.next().getValue();
						if (bitmap != null) {
							size -= sizeOf(bitmap);
						}
						iterator.remove();
					}
				}
			}
		});
	}

	private void update(final String key) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = map.remove(key);
				map.put(key, bitmap);
			}
		});
	}

}
