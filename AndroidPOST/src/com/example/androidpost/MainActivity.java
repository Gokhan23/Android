package com.example.androidpost;


import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.TwitterException;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class MainActivity extends Activity implements
ActionBar.OnNavigationListener{
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	
	// action bar
		private ActionBar actionBar;
	
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter navadapter;
	
	
	Context my_service = this;
	GridView list;
    LazyAdapter adapter;
    PackageManager PackageManager;
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		PackageManager = getPackageManager();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		asyncJsonForPop(this.findViewById(android.R.id.content));
	  
		
		actionBar = getActionBar();

		// Hide the action bar title
		actionBar.setDisplayShowTitleEnabled(false);
		
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		

		Button mButton = (Button)findViewById(R.id.btnConnect);
	
		
		mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 EditText edit_text = (EditText) findViewById(R.id.editText1);
            	 CharSequence edit_text_value = edit_text.getText();                
            	 asyncJson(v,edit_text_value);
            	 new HopAsync().execute();
            	 
            }
        });
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list navadapter
		navadapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(navadapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.stub, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
			
	}
		
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}
	
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new FindPeopleFragment();
			break;
		case 2:
			fragment = new PhotosFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	
	
	public void asyncJsonForPop(View view) {
	
		AQuery popular= new AQuery(view);
		String popURL="https://api.instagram.com/v1/media/popular?client_id=7c83ce5e425346e2b5a6cc337751ab65&count=1000&max_id=1000";
		popular.ajax(popURL,JSONObject.class,this,"JsonCallbackForPop");
	}
	
	
	public void JsonCallbackForPop(String url, JSONObject json, AjaxStatus status) throws JSONException{
			
		
		
		
			if(json != null){   
				
	        	JSONArray data = json.getJSONArray("data");       	       	      	
	        	String[] mStrings = new String[data.length()];
	        	for (int i = 0; i < data.length(); i++) {
	        		
	        		JSONObject c = data.getJSONObject(i);	        	
					JSONObject user= c.getJSONObject("images");					
					JSONObject images = user.getJSONObject("standard_resolution");					
					String image_url = images.getString("url");																			
					mStrings[i] = image_url;						
	        	}
	        	
	        	list=(GridView)findViewById(R.id.gridView1);
		        adapter=new LazyAdapter(this, mStrings);
		        list.setAdapter(adapter);
		        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {	            
		            	String url= adapter.getImageUrL(position);
		 	            DownAndUpLoad asd= new DownAndUpLoad(PackageManager,getApplicationContext());
		 	            asd.AccessFunction(url, PackageManager, getApplicationContext());
		 	           	        	            
		            }
		        });
		       
			}else{
				Log.d("JsonCallbackForPop", "error");  //
			}
			
			
		
		
		}
	
	public void asyncJson(View view, CharSequence edit_text_value){
							
		if(edit_text_value.toString()!=""){
		AQuery aq = new AQuery(view);		      		     
        String url = "https://api.instagram.com/v1/tags/"+ edit_text_value.toString().trim() + "/media/recent?client_id=4c69121b39fa4ceb93cc79ad09d37928&count=1000&max_id=1000";                     
		aq.ajax(url, JSONObject.class, this, "jsonCallback");
		}else{
			
			Toast.makeText(getApplicationContext(), "Please Fill Textbox",
					   Toast.LENGTH_LONG).show();
		}
		}

	public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException{
        
        if(json != null){               
        	 
        	
        	JSONArray data = json.getJSONArray("data");       	       	      	
        	String[] mStrings = new String[data.length()];
        	
        	for (int i = 0; i < data.length(); i++) {
        		
        		JSONObject c = data.getJSONObject(i);
        		
				JSONObject images= c.getJSONObject("images");
				JSONObject resolution = images.getJSONObject("standard_resolution");
				String image_url = resolution.getString("url");	
		
				mStrings[i] = image_url;
        	}
        		Intent myIntent = new Intent(MainActivity.this, PhotoList.class);
       	 		myIntent.putExtra("Images_url_array", mStrings);
		        MainActivity.this.startActivity(myIntent);      		
        	
        }else{
      
        	Log.d("jsonCallback", "error");  //
        }
        
}
	
	
	
		


		
		private class HopAsync extends  AsyncTask<String, String, String>
		{

			@Override
			protected String doInBackground(String... params) {
				TwitterUtils TwitterUtils = new TwitterUtils();
				
					try {
						EditText edit_text = (EditText) findViewById(R.id.editText1);
		            	 String edit_text_value = edit_text.getText().toString();		                 
						TwitterUtils.hop(edit_text_value);
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}								
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}
			
		}



		/**
		 * Handling intent data
		 */
		private void handleIntent(Intent intent){
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
				String query = intent.getStringExtra(SearchManager.QUERY);

				/**
				 * Use this query to display search results like 
				 * 1. Getting the data from SQLite and showing in listview 
				 * 2. Making webrequest and displaying the data 
				 * For now we just display the query only
				 */
				asyncJson(findViewById(android.R.id.content),query);
				new HopAsync().execute();
				return;
			}
			return;

		}
		
		
		
		/**
		 * On selecting action bar icons
		 * */
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			// Take appropriate action for each action item click
			switch (item.getItemId()) {
			case R.id.action_search:
				// search action
				handleIntent(getIntent());
				return true;		
			default:
				return super.onOptionsItemSelected(item);
			}
		}

		@Override
		public boolean onNavigationItemSelected(int arg0, long arg1) {
			// TODO Auto-generated method stub
			return false;
		}
		

	
	
}
