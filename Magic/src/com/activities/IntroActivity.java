package com.activities;

import com.example.magic.R;
import com.example.magic.R.layout;
import com.example.magic.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class IntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.intro, menu);
		return true;
	}

}
