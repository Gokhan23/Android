package com.example.androidpost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.TwitterException;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class PhotoList extends Activity {
	PackageManager PackageManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		String[] mString = intent.getStringArrayExtra("Images_url_array");
		PackageManager = getPackageManager();
		
	
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tagsphotogrid);
		GridView list = (GridView)findViewById(R.id.gridView2);
	  	final LazyAdapter adapter = new LazyAdapter(this, mString);
        list.setAdapter(adapter);	
        
        
       //** GridView.setOnItemClickListener(new OnItemClickListener() 
       // {
           // public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
           // {
                // this 'mActivity' parameter is Activity object, you can send the current activity.
              //  Intent i = new Intent(mActivity, ActvityToCall.class);
            //    mActivity.startActivity(i);
          //  }
        //});
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            String url= adapter.getImageUrL(position);
            new DownAndUpLoad(PackageManager,getApplicationContext()).execute(url);
           
        
            
            }
        });
	}
	
		
	
	
	
	
	
	
	

	
	

}
