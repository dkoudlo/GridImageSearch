package com.codepath.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	
	Spinner spImgSize;
    Spinner spImgType;
    Spinner spImgColor;
    EditText etSiteFilter;
    Button btnSave;
    SearchSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setupViews();
	}

	private void setupViews() {
		spImgSize = (Spinner) findViewById(R.id.spImgSize);
        spImgType = (Spinner) findViewById(R.id.spImageType);
        spImgColor = (Spinner) findViewById(R.id.spColorFilter);
        etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
        btnSave = (Button) findViewById(R.id.btnSave);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void saveSettings(View v) { 
		
		settings.setColor(spImgColor.getSelectedItem().toString());
		settings.setSite(etSiteFilter.getText().toString());
		settings.setSize(spImgSize.getSelectedItem().toString());
		settings.setType(spImgType.getSelectedItem().toString());
		
		Intent i = new Intent();                
        
        i.putExtra("settings", settings);                
                        
        setResult(RESULT_OK, i);                
        finish();                        
	}
}
