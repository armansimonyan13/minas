package com.minas;

import android.content.Context;
import android.test.mock.MockContext;
import android.widget.ImageView;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * @author armansimonyan
 */
public class RequestBuilderTest {

	@Test
	public void testLoad() throws Exception {
		Context context = new MockContext();
		Minas minas = Mockito.mock(Minas.class);
		RequestBuilder requestBuilder = new RequestBuilder(minas);
		String url = "http://my.mock.url";

		assertEquals(requestBuilder.load(url), requestBuilder);
	}

	@Test
	public void testInto() throws Exception {
		Context context = new MockContext();
		Minas minas = Mockito.mock(Minas.class);
		RequestBuilder requestBuilder = new RequestBuilder(minas);
		ImageView imageView = new ImageView(context);
//		Request request = requestBuilder.into(imageView);
//
//		assertEquals(request.getImageView(), imageView);

	}

}
