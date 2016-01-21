package com.minas;

import android.support.annotation.DrawableRes;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the Request
 *
 * @author armansimonyan
 */
public class RequestBuilder {

	private Minas minas;
	private String url;
	private String key;
	private int width;
	private int height;
	private Transformation transformation;
	private Downloader downloader;
	private int preLoadResource;
	private int errorResource;

	public RequestBuilder(Minas minas) {
		this.minas = minas;
	}

	public RequestBuilder load(String url) {
		this.url = url;

		return this;
	}

	public RequestBuilder key(String key) {
		this.key = key;

		return this;
	}

	public RequestBuilder width(int width) {
		this.width = width;

		return this;
	}

	public RequestBuilder height(int height) {
		this.height = height;

		return this;
	}

	public RequestBuilder transformation(Transformation transformation) {
		this.transformation = transformation;

		return this;
	}

	public RequestBuilder downloader(Downloader downloader) {
		this.downloader = downloader;

		return this;
	}

	public RequestBuilder preLoadResource(@DrawableRes int preLoadResource) {
		this.preLoadResource = preLoadResource;

		return this;
	}

	public RequestBuilder errorResource(@DrawableRes int errorResource) {
		this.errorResource = errorResource;

		return this;
	}

	public Action into(ImageView imageView) {
		ResizeTransformation resizeTransformation = new ResizeTransformation(width, height);
		List<Transformation> transformations = new ArrayList<>();
		transformations.add(resizeTransformation);
		transformations.add(transformation);
		Request request = new Request(minas, url, key, width, height, downloader, minas.logger(), transformations);
		Action action = new Action(minas, request, preLoadResource, errorResource, imageView);
		if (minas.containsKey(imageView)) {
			Action oldAction = minas.getAction(imageView);
			oldAction.cancel();
		}
		minas.putAction(imageView, action);
		action.execute();
		return action;
	}

}
