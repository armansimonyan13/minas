package com.minas;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * @author armansimonyan
 */
public class RamLruCacheTest {

	private RamLruCache cache;

	@Before
	public void setUp() throws Exception {
		cache = new RamLruCache(10 * 1024 * 4);
	}

	@Test
	public void testPut_exceed() throws Exception {
		// 4 * 1 * 1024
		Bitmap bitmap0 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap0.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap0.getWidth()).thenReturn(1024);
		Mockito.when(bitmap0.getHeight()).thenReturn(4);
		// 4 * 3 * 1024
		Bitmap bitmap1 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap1.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap1.getWidth()).thenReturn(1024);
		Mockito.when(bitmap1.getHeight()).thenReturn(3);
		// 4 * 2 * 1024
		Bitmap bitmap2 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap2.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap2.getWidth()).thenReturn(1024);
		Mockito.when(bitmap2.getHeight()).thenReturn(2);
		// 4 * 3 * 1024;
		Bitmap bitmap3 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap3.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap3.getWidth()).thenReturn(1024);
		Mockito.when(bitmap3.getHeight()).thenReturn(3);

		cache.put("bitmap0", bitmap0);
		cache.put("bitmap1", bitmap1);
		cache.put("bitmap2", bitmap2);
		cache.put("bitmap3", bitmap3);

		Thread.sleep(2000);

		assertEquals(null, cache.get("bitmap0"));
		assertEquals(bitmap1, cache.get("bitmap1"));
		assertEquals(bitmap2, cache.get("bitmap2"));
		assertEquals(bitmap3, cache.get("bitmap3"));
	}

	@Test
	public void testPut_exceed_1() throws Exception {
		// 4 * 1 * 1024
		Bitmap bitmap0 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap0.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap0.getWidth()).thenReturn(1024);
		Mockito.when(bitmap0.getHeight()).thenReturn(4);
		// 4 * 3 * 1024
		Bitmap bitmap1 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap1.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap1.getWidth()).thenReturn(1024);
		Mockito.when(bitmap1.getHeight()).thenReturn(3);
		// 4 * 2 * 1024
		Bitmap bitmap2 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap2.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap2.getWidth()).thenReturn(1024);
		Mockito.when(bitmap2.getHeight()).thenReturn(2);
		// 4 * 9 * 1024;
		Bitmap bitmap3 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap3.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap3.getWidth()).thenReturn(1024);
		Mockito.when(bitmap3.getHeight()).thenReturn(9);

		cache.put("bitmap0", bitmap0);
		cache.put("bitmap1", bitmap1);
		cache.put("bitmap2", bitmap2);
		cache.put("bitmap3", bitmap3);

		Thread.sleep(2000);

		assertEquals(null, cache.get("bitmap0"));
		assertEquals(null, cache.get("bitmap1"));
		assertEquals(null, cache.get("bitmap2"));
		assertEquals(bitmap3, cache.get("bitmap3"));
	}

	@Test
	public void testPut_fit() throws Exception {
		// 4 * 4 * 1024
		Bitmap bitmap0 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap0.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap0.getWidth()).thenReturn(1024);
		Mockito.when(bitmap0.getHeight()).thenReturn(4);
		// 4 * 3 * 1024
		Bitmap bitmap1 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap1.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap1.getWidth()).thenReturn(1024);
		Mockito.when(bitmap1.getHeight()).thenReturn(3);
		// 4 * 2 * 1024
		Bitmap bitmap2 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap2.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap2.getWidth()).thenReturn(1024);
		Mockito.when(bitmap2.getHeight()).thenReturn(2);

		cache.put("bitmap0", bitmap0);
		cache.put("bitmap1", bitmap1);
		cache.put("bitmap2", bitmap2);

		Thread.sleep(2000);

		assertEquals(bitmap0, cache.get("bitmap0"));
		assertEquals(bitmap1, cache.get("bitmap1"));
		assertEquals(bitmap2, cache.get("bitmap2"));
	}

	@Test
	public void testPut_same_key__size() throws Exception {
		// 4 * 4 * 1024
		Bitmap bitmap0 = Mockito.mock(Bitmap.class);
		Mockito.when(bitmap0.getRowBytes()).thenReturn(4 * 1024);
		Mockito.when(bitmap0.getWidth()).thenReturn(1024);
		Mockito.when(bitmap0.getHeight()).thenReturn(4);

		cache.put("bitmap0", bitmap0);
		cache.put("bitmap0", bitmap0);

		Thread.sleep(2000);

		assertEquals(bitmap0, cache.get("bitmap0"));
		assertEquals(RamLruCache.sizeOf(bitmap0), cache.size());
	}

	@Test
	public void testGet() throws Exception {


	}

}
