package com.minas;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.view.animation.Animation;
import android.widget.ImageView;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.ExecutorService;

/**
 * @author armansimonyan
 */
public class ActionTest {

	@Test
	public void testExecute() throws Exception {
		Minas minas = Mockito.mock(Minas.class);
		Handler handler = Mockito.mock(Handler.class);
		final ArgumentCaptor<Runnable> runnableArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
		Mockito.when(handler.post(runnableArgumentCaptor.capture())).then(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				runnableArgumentCaptor.getValue().run();
				return null;
			}
		});
		Mockito.when(minas.handler()).thenReturn(handler);
		ExecutorService executor = Mockito.mock(ExecutorService.class);
		Mockito.when(minas.executor()).thenReturn(executor);
		final ArgumentCaptor<Runnable> submitArgumentCaptor = ArgumentCaptor.forClass(Runnable.class);
		Mockito.when(executor.submit(submitArgumentCaptor.capture())).then(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				submitArgumentCaptor.getValue().run();
				return null;
			}
		});
		Request request = Mockito.mock(Request.class);
		Response response = Mockito.mock(Response.class);
		Mockito.when(request.execute()).thenReturn(response);
		Bitmap bitmap = Mockito.mock(Bitmap.class);
		Mockito.when(response.getBitmap()).thenReturn(bitmap);
		Mockito.when(response.getSource()).thenReturn(Source.RAM);
		@DrawableRes
		int preLoadResource = 1;
		int errorResource = 2;
		ImageView imageView = Mockito.mock(ImageView.class);

		Action action = new Action(minas, request, preLoadResource, errorResource, imageView);
		action.execute();

		ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
		Mockito.verify(imageView).setImageResource(integerArgumentCaptor.capture());
		Assert.assertEquals(1, integerArgumentCaptor.getValue().intValue());
		ArgumentCaptor<Bitmap> bitmapArgumentCaptor = ArgumentCaptor.forClass(Bitmap.class);
		Mockito.verify(imageView).setImageResource(integerArgumentCaptor.capture());
		Mockito.verify(imageView).setImageBitmap(bitmapArgumentCaptor.capture());
		Assert.assertEquals(bitmap, bitmapArgumentCaptor.getValue());
		Mockito.verify(request).execute();
	}

	@Test
	@Ignore
	public void testCancel() throws Exception {
		Minas minas = Mockito.mock(Minas.class);
		Request request = Mockito.mock(Request.class);
		int preLoadResource = 1;
		int errorResource = 2;
		ImageView imageView = Mockito.mock(ImageView.class);

		Action action = new Action(minas, request, preLoadResource, errorResource, imageView);
		action.execute();
		action.cancel();
	}

}
