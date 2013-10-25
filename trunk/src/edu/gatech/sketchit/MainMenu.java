package edu.gatech.sketchit;

//import edu.gatech.sketchit.activities.CalibrationActivity;

import edu.gatech.sketchit.activities.CalibrationActivity;
import edu.gatech.sketchit.activities.SketchActivity;
import edu.gatech.sketchit.activities.SketchActivityJCPT;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainMenu extends Activity {
	    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Create Objects
        ImageButton home_logo = new ImageButton(this);
        ImageButton home_new = new ImageButton(this);
        ImageButton home_open = new ImageButton(this);
        ImageButton home_calibrate = new ImageButton(this);
        ImageButton home_settings = new ImageButton(this);
        //Variablize images
        Bitmap img_logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_big);
        Bitmap img_new = BitmapFactory.decodeResource(getResources(), R.drawable.home_new);
        Bitmap img_open = BitmapFactory.decodeResource(getResources(), R.drawable.home_open);
        Bitmap img_calibrate = BitmapFactory.decodeResource(getResources(), R.drawable.home_calibrate);
        Bitmap img_settings = BitmapFactory.decodeResource(getResources(), R.drawable.home_settings);
        //Set and Id Objects
        home_logo.setImageBitmap(img_logo);
        home_logo.setId(99);
        home_new.setImageBitmap(img_new);
        home_new.setId(100);
        home_open.setImageBitmap(img_open);
        home_open.setId(101);
        home_calibrate.setImageBitmap(img_calibrate);
        home_calibrate.setId(102);
        home_settings.setImageBitmap(img_settings);
        home_settings.setId(103);
        
        //Create Layouts 
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams layout_logo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        		ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_new = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_open = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_calibrate = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layout_settings = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //Logo
        layout_logo.addRule(RelativeLayout.ALIGN_TOP);
        layout_logo.setMargins(0, 0, 0, 0);
        home_logo.setLayoutParams(layout_logo);
        home_logo.setBackground(null);
        rl.addView(home_logo);
        //New
        layout_new.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layout_new.addRule(RelativeLayout.BELOW, home_logo.getId());
        layout_new.setMargins((int)(img_new.getWidth()*1.5), 0, 0, 0);
        home_new.setLayoutParams(layout_new);
        home_new.setBackground(null);
        rl.addView(home_new);
        //Open
        layout_open.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layout_open.addRule(RelativeLayout.BELOW, home_new.getId());
        layout_open.setMargins((int)(img_open.getWidth()*1.5), 0, 0, 0);
        home_open.setLayoutParams(layout_open);
        home_open.setBackground(null);
        rl.addView(home_open);
        //Calibrate
        layout_calibrate.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout_calibrate.addRule(RelativeLayout.BELOW, home_logo.getId());
        layout_calibrate.setMargins(0, 0, (int)(img_calibrate.getWidth()*1.5), 0);
        home_calibrate.setLayoutParams(layout_calibrate);
        home_calibrate.setBackground(null);
        rl.addView(home_calibrate);
        //Settings
        layout_settings.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout_settings.addRule(RelativeLayout.BELOW, home_calibrate.getId());
        layout_settings.setMargins(0, 0, (int)(img_settings.getWidth()*1.5), 0);
        home_settings.setLayoutParams(layout_settings);
        home_settings.setBackground(null);
        rl.addView(home_settings);
        //OnClickListeners
        home_logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d("MainMenu.java", "Logo Clicked");
            }
        });
        home_new.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d("MainMenu.java", "New Clicked");
            	Intent intent = new Intent(MainMenu.this, SketchActivityJCPT.class);
                startActivity(intent);      
            }
        });
        home_open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: home_open onclicklistener
            	Log.d("MainMenu.java", "Open Clicked");
            	Intent intent = new Intent(MainMenu.this, SketchActivity.class);
                startActivity(intent); 
            }
        });
        home_calibrate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.d("MainMenu.java", "Calibrate Clicked");
            	Intent intent = new Intent(MainMenu.this, CalibrationActivity.class);
                startActivity(intent);      
            }
        });
        home_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: home_settings onclicklistener
            	Log.d("MainMenu.java", "Settings Clicked");
            }
        });
        
        //Output
        rl.setBackgroundResource(R.drawable.background);
        setContentView(rl);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
    
}
