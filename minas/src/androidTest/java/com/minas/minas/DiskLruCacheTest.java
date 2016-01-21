package com.minas.minas;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.test.AndroidTestCase;

import com.minas.DiskLruCache;
import com.minas.Logger;
import com.minas.RamLruCache;

import java.io.File;
import java.util.Random;

/**
 * @author armansimonyan
 */
public class DiskLruCacheTest extends AndroidTestCase {

	private DiskLruCache cache;

	public void setUp() throws Exception {
		Logger logger = new Logger() {
			@Override
			public void log(String message) {

			}
		};
		cache = new DiskLruCache(getContext(), 10 * 4 * 1024, logger);
		File cacheDir = new File(getContext().getFilesDir(), "MINAS_CACHE");
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			cacheDir.delete();
		}
		Thread.sleep(2000);
	}

	public void testPut() throws Exception {
		int[] data0 = new int[4 * 1024];
		Random random = new Random();
		for (int i = 0; i < data0.length; i++) {
			data0[i] = Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
		}
		Bitmap bitmap0 = Bitmap.createBitmap(data0, 2, 1024, Bitmap.Config.ARGB_8888);
		cache.put("bitmap0", bitmap0);

		int[] data1 = new int[3 * 1024];
		for (int i = 0; i < data1.length; i++) {
			data1[i] = Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
		}
		Bitmap bitmap1 = Bitmap.createBitmap(data1, 3, 1024, Bitmap.Config.ARGB_8888);
		cache.put("bitmap1", bitmap1);

		int[] data2 = new int[2 * 1024];
		for (int i = 0; i < data2.length; i++) {
			data2[i] = Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
		}
		Bitmap bitmap2 = Bitmap.createBitmap(data2, 2, 1024, Bitmap.Config.ARGB_8888);
		cache.put("bitmap2", bitmap2);

		int[] data3 = new int[3 * 1024];
		for (int i = 0; i < data3.length; i++) {
			data3[i] = Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
		}
		Bitmap bitmap3 = Bitmap.createBitmap(data3, 3, 1024, Bitmap.Config.ARGB_8888);
		cache.put("bitmap3", bitmap3);

		Thread.sleep(3000);

		Bitmap newBitmap0 = cache.get("bitmap0");
		Bitmap newBitmap1 = cache.get("bitmap1");
		Bitmap newBitmap2 = cache.get("bitmap2");
		Bitmap newBitmap3 = cache.get("bitmap3");

		assertEquals(null, newBitmap0);
		assertEquals(RamLruCache.sizeOf(bitmap1), RamLruCache.sizeOf(newBitmap1));
		assertEquals(RamLruCache.sizeOf(bitmap2), RamLruCache.sizeOf(newBitmap2));
		assertEquals(RamLruCache.sizeOf(bitmap3), RamLruCache.sizeOf(newBitmap3));
	}

	public void testGet() throws Exception {

	}

}
