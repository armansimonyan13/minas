package com.minas.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minas.HttpURLConnectionDownloader;
import com.minas.Minas;
import com.minas.RoundTransformation;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		((TextView) findViewById(R.id.text_0)).setText(getString(R.string.url_0, getString(R.string.ip)));
		((TextView) findViewById(R.id.text_1)).setText(getString(R.string.url_1, getString(R.string.ip)));
		((TextView) findViewById(R.id.text_2)).setText(getString(R.string.url_2, getString(R.string.ip)));
		((TextView) findViewById(R.id.text_3)).setText(getString(R.string.url_3, getString(R.string.ip)));

		findViewById(R.id.text_0).setOnClickListener(this);
		findViewById(R.id.text_1).setOnClickListener(this);
		findViewById(R.id.text_2).setOnClickListener(this);
		findViewById(R.id.text_3).setOnClickListener(this);

		imageView = (ImageView) findViewById(R.id.image);

	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			TextView textView = (TextView) v;
			String url = textView.getText().toString();
			Minas.with(this)
					.load(url)
					.downloader(new HttpURLConnectionDownloader() {
						@Override
						protected HttpURLConnection getHttpURLConnection(String url) throws IOException {
							HttpURLConnection connection = super.getHttpURLConnection(url);
							if (connection instanceof HttpsURLConnection) {
								HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
								httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
									@Override
									public boolean verify(String hostname, SSLSession session) {
										return true;
									}
								});
							}
							return connection;
						}
					})
//					.key(Uri.parse(url).getPath())
					.width(100)
					.height(100)
					.transformation(new RoundTransformation())
					.preLoadResource(R.drawable.preload)
					.errorResource(R.drawable.error)
					.into(imageView);
		}
	}

}
