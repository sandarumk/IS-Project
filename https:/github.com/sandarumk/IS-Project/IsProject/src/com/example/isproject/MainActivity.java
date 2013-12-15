package com.example.isproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button startService = (Button) findViewById(R.id.buttonStartService);
		Button stopService=(Button) findViewById(R.id.buttonStopService);
		final Intent intent = new Intent(getApplicationContext(), SpeachRecognitionService.class);
		startService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startService(intent);
				
			}
		});
		
		stopService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopService(intent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
