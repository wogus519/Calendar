package com.example.wogus.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by wogus on 2018-10-31.
 */

public class IntroActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				while((System.currentTimeMillis()-start)/1000.0<1.3){}
				Intent intent = new Intent(IntroActivity.this, CalendarActivity.class);
				startActivity(intent);
				finish();
			}
		}).start();

	}
}
