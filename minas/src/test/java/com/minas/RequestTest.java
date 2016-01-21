package com.minas;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author armansimonyan
 */
public class RequestTest {

	private Minas minas;
	private String url;
	private String key;
	private int width;
	private int height;
	private Transformation transformation;
	private Downloader downloader;
	private Bitmap bitmap;

	@Before
	public void setUp() throws Exception {
		minas = Mockito.mock(Minas.class);
		RamLruCache ramCache = Mockito.mock(RamLruCache.class);
		DiskLruCache diskCache = Mockito.mock(DiskLruCache.class);
		Logger logger = Mockito.mock(Logger.class);
		Mockito.when(minas.ramCache()).thenReturn(ramCache);
		Mockito.when(minas.diskCache()).thenReturn(diskCache);
		Mockito.when(minas.logger()).thenReturn(logger);
		url = "http://mock.url";
		key = "key";
		width = 10;
		height = 20;
		transformation = Mockito.mock(Transformation.class);
		downloader = Mockito.mock(Downloader.class);

		bitmap = Mockito.mock(Bitmap.class);
	}

	@Test
	public void testGetSource_RAM() throws Exception {
		Mockito.when(minas.ramCache().get(key)).thenReturn(bitmap);
		Request request = new Request(minas, url, key, width, height, downloader, minas.logger(), new ArrayList<Transformation>());
		Response response = request.execute();

		assertEquals(Source.RAM, response.getSource());
		assertEquals(bitmap, response.getBitmap());
	}

	@Test
	public void testGetSource_DISK() throws Exception {
		Mockito.when(minas.diskCache().get(key)).thenReturn(bitmap);
		Request request = new Request(minas, url, key, width, height, downloader, minas.logger(), new ArrayList<Transformation>());
		Response response = request.execute();

		assertEquals(Source.DISK, response.getSource());
		assertEquals(bitmap, response.getBitmap());
	}

	@Test
	public void testGetSource_NETWORK() throws Exception {
		Mockito.when(downloader.download(url)).thenReturn(bitmap);
		ResizeTransformation rt = Mockito.mock(ResizeTransformation.class);
		Mockito.when(rt.transform(bitmap)).thenReturn(bitmap);
		Mockito.when(transformation.transform(bitmap)).thenReturn(bitmap);
		Request request = new Request(minas, url, key, width, height, downloader, minas.logger(), new ArrayList<Transformation>());
		Response response = request.execute();

		assertEquals(Source.NETWORK, response.getSource());
		assertEquals(bitmap, response.getBitmap());
	}

}
