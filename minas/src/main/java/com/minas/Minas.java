package com.minas;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Main class
 * Named after the great armenian painter Minas Avetisyan
 *
 * @author armansimonyan
 *
 */
public class Minas {

	static volatile Minas instance;

	private final Map<ImageView, Action> inProgressRequests = new WeakHashMap<>();

	private final Handler handler;
	private final ExecutorService executor;
	private final LruCache diskCache;
	private final LruCache ramCache;
	private final Logger logger;

	public static RequestBuilder with(Context context) {
		if (instance == null) {
			synchronized (Minas.class) {
				if (instance == null) {
					Logger logger = new DefaultLogger();
					instance = new Minas(context,
							new Handler(Looper.getMainLooper()),
							Executors.newFixedThreadPool(4),
							new DiskLruCache(context, 70 * 1024 * 1024, logger),
							new RamLruCache(70 * 1024 * 1024),
							logger);
				}
			}
		}
		return new RequestBuilder(instance);
	}

	Minas(Context context,
		  Handler handler,
		  ExecutorService executor,
		  LruCache diskLruCache,
		  LruCache ramLruCache,
		  Logger logger) {
		this.handler = handler;
		this.executor = executor;
		this.diskCache = diskLruCache;
		this.ramCache = ramLruCache;
		this.logger = logger;
	}

	public Handler handler() {
		return handler;
	}

	public ExecutorService executor() {
		return executor;
	}

	public LruCache diskCache() {
		return diskCache;
	}

	public LruCache ramCache() {
		return ramCache;
	}

	public Logger logger() {
		return logger;
	}

	public void shutDown() {
		inProgressRequests.clear();
		executor.shutdown();
	}

	public void putAction(ImageView imageView, Action action) {
		inProgressRequests.put(imageView, action);
	}

	public Action getAction(ImageView imageView) {
		return inProgressRequests.get(imageView);
	}

	public void removeAction(ImageView imageView) {
		inProgressRequests.remove(imageView);
	}

	public boolean containsKey(ImageView imageView) {
		return inProgressRequests.containsKey(imageView);
	}

}
