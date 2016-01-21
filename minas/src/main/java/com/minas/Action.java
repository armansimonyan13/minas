package com.minas;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.Future;

/**
 * Creates Runnable and executes with ExecutorService
 *
 * @author armansimonyan
 */
public class Action {

	private final Minas minas;
	private Request request;
	private final int preLoadResource;
	private final int errorResource;
	private WeakReference<ImageView> imageViewWeakReference;

	private Future future;

	private volatile boolean isCanceled;

	private static final int ANIMATION_DURATION = 150;

	public Action(Minas minas, Request request, int preLoadResource, int errorResource, ImageView imageView) {
		this.minas = minas;
		this.request = request;
		this.preLoadResource = preLoadResource;
		this.errorResource = errorResource;
		this.imageViewWeakReference = new WeakReference<>(imageView);
	}

	public void execute() {
		ImageView imageView = imageViewWeakReference.get();
		if (imageView != null) {
			imageView.clearAnimation();
		}
		minas.handler().post(new Runnable() {
			@Override
			public void run() {
				ImageView imageView = imageViewWeakReference.get();
				if (imageView != null) {
					imageView.setImageResource(preLoadResource);
				}
			}
		});
		future = minas.executor().submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							if (isCanceled) {
								return;
							}
							final Response response = request.execute();
							if (isCanceled) {
								return;
							}
							minas.handler().post(new Runnable() {
								@Override
								public void run() {
									if (response.getException() != null) {
										if (isCanceled) {
											return;
										}
										Animation fadeOutAnimation = new AlphaAnimation(1f, 0.5f);
										fadeOutAnimation.setDuration(ANIMATION_DURATION);
										fadeOutAnimation.setAnimationListener(new AnimationListenerAdapter() {

											@Override
											public void onAnimationEnd(Animation animation) {
												if (isCanceled) {
													return;
												}
												ImageView imageView = imageViewWeakReference.get();
												if (imageView != null) {
													imageView.setImageResource(errorResource);
													Animation fadeInAnimation = new AlphaAnimation(0.5f, 1f);
													fadeInAnimation.setDuration(ANIMATION_DURATION);
													fadeInAnimation.setAnimationListener(new AnimationListenerAdapter() {
														@Override
														public void onAnimationEnd(Animation animation) {
															ImageView imageView = imageViewWeakReference.get();
															minas.removeAction(imageView);
														}
													});
													imageView.startAnimation(fadeInAnimation);
												}
											}

										});
										ImageView imageView = imageViewWeakReference.get();
										if (imageView != null) {
											imageView.startAnimation(fadeOutAnimation);
											response.getException().printStackTrace();
										}
									} else {
										if (isCanceled) {
											return;
										}
										ImageView imageView = imageViewWeakReference.get();
										if (imageView != null) {
											if (response.getSource() == Source.RAM) {
												imageView.setImageBitmap(response.getBitmap());
												minas.removeAction(imageView);
											} else {
												Animation fadeOutAnimation = new AlphaAnimation(1f, 0.5f);
												fadeOutAnimation.setDuration(ANIMATION_DURATION);
												fadeOutAnimation.setAnimationListener(new AnimationListenerAdapter() {

													@Override
													public void onAnimationEnd(Animation animation) {
														if (isCanceled) {
															return;
														}
														ImageView imageView = imageViewWeakReference.get();
														if (imageView != null) {
															imageView.setImageBitmap(response.getBitmap());
															Animation fadeInAnimation = new AlphaAnimation(0.5f, 1f);
															fadeInAnimation.setDuration(ANIMATION_DURATION);
															fadeInAnimation.setAnimationListener(new AnimationListenerAdapter() {
																@Override
																public void onAnimationEnd(Animation animation) {
																	ImageView imageView = imageViewWeakReference.get();
																	minas.removeAction(imageView);
																}
															});
															imageView.startAnimation(fadeInAnimation);
														}
													}

												});
												imageView.startAnimation(fadeOutAnimation);
											}
										}
									}
								}
							});
						}
					});
					thread.start();
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					request.cancel();
				}
			}

		});
	}

	public void cancel() {
		isCanceled = true;
		future.cancel(true);
	}

}
