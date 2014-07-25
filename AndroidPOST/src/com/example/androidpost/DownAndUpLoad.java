package com.example.androidpost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownAndUpLoad extends  AsyncTask<String, String, String>
{

	PackageManager manager;
	Context context;
	
	public DownAndUpLoad(PackageManager packageManager,Context Appcontext) {
		manager=packageManager;
		context=Appcontext;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String doInBackground(String... params) {
		try {
					
			String_to_File(params[0]);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return params[0];
	}

	@Override
	protected void onPostExecute(String result) {

				
 		String[] separated = result.split("/");
 		ShareInstagram a = new ShareInstagram(context);
 		a.shareInstagram(Uri.parse("file:///"+ Environment.getExternalStorageDirectory() + "/Pictures/" + separated[5].trim()),manager);
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}



	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	public void AccessFunction(String url,PackageManager PackageManager,Context context){
//		new DownAndUpLoad(PackageManager).execute(url);		
		DownAndUpLoad a = new DownAndUpLoad(PackageManager,context);
		a.execute(url);
	}
	
	public String String_to_File(final String img_url) throws IOException {
		
		  // Long running operation
		    	URL url = null;
				try {
					url = new URL (img_url);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InputStream input = null;
				try {
					input = url.openStream();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				try {
				//The sdcard directory e.g. '/sdcard' can be used directly, or 
				//more safely abstracted with getExternalStorageDirectory()
				String[] separated = img_url.split("/");			
				File storagePath = Environment.getExternalStorageDirectory();
				
				Log.d("Burayakaydet", storagePath + "/" + separated[5].trim());
				Log.d("Kayýtlink", storagePath + "/myImage.jpg");
				Log.d("adresvergelelim",storagePath + "/Pictures/" + separated[5].trim());
							
				OutputStream output = new FileOutputStream (storagePath + "/Pictures/" + separated[5].trim());

				try {
				    byte[] buffer = new byte[1024];
				    int bytesRead = 0;
				    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
				        try {
							output.write(buffer, 0, bytesRead);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
				    try {
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				String[] separated = img_url.split("/");
				String url_path = Environment.getExternalStorageDirectory() + "/" + separated[5].trim();
				
				return  url_path;
			
		    }	
	
}
