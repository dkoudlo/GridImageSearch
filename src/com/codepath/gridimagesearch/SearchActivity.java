package com.codepath.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	private final int SETTINGS_CODE = 20;
	SearchSettings settings;
	public static final String SETTINGS_KEY = "settings";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        settings = new SearchSettings();
        
        gvResults.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
        	
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				customLoadMoreDataFromApi(totalItemsCount);
			}
		});
        gvResults.setAdapter(imageAdapter);
    }

    public void openSettings(MenuItem mi){
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        i.putExtra(SETTINGS_KEY, settings);
        startActivityForResult(i, SETTINGS_CODE);                                
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    
    public void setupViews(){
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	btnSearch = (Button) findViewById(R.id.btnSearch);
    }
    
    public void onImageSearch(View v){
    	imageResults.clear();
    	customLoadMoreDataFromApi(0);
    }
    
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getRequestUrl(offset),
            new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                	JSONArray imageJsonResults = null;
        			try {
        				imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
        				imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
        				Log.d("DEBUG", imageResults.toString());
        			} catch (JSONException e) {
        				e.printStackTrace();
        			}
                }
        	});
    }
    
    private String getRequestUrl(int offset){
    	String query = etQuery.getText().toString();
    	String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&"+
		"start=" + offset + "&v=1.0&q=" + Uri.encode(query);
    	
    	if(!settings.getColor().isEmpty()){
    		url = url + "&imgcolor=" + settings.getColor();
    	}
    	
    	if(!settings.getSize().isEmpty()){
    		url = url + "&imgsz=" + settings.getSize();
    	}
    	
    	if(!settings.getType().isEmpty()){
    		url = url + "&imgtype=" + settings.getType();
    	}
    	
    	if(!settings.getSite().isEmpty()){
    		url = url + "&as_sitesearch=" + settings.getSite();
    	}

    	Log.d("url", url);
    	
    	return url;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == SETTINGS_CODE) {
                settings = (SearchSettings) data.getSerializableExtra(SettingsActivity.SETTINGS_KEY);
        }
    }
}
