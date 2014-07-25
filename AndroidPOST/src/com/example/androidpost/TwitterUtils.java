package com.example.androidpost;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public  class TwitterUtils{

	   String hop(String text) throws TwitterException{
			ConfigurationBuilder builder=new ConfigurationBuilder();
			
		    builder.setApplicationOnlyAuthEnabled(true);

		    // setup
		    Twitter twitter = new TwitterFactory(builder.build()).getInstance();

		    // exercise & verify
		    twitter.setOAuthConsumer("p9KyQo8HwNInvO5Ul4HziZkeC", "TIKepLBusflWw78PJ8GpOnCqFPUK7GeokMHTx9cMXZYcZ1czY0");
		   // OAuth2Token token = twitter.getOAuth2Token();
		    
				twitter.getOAuth2Token();
				
		    Query query = new Query(text);			    
			QueryResult qr = twitter.search(query);	

				
		    return qr.toString();
	   }


	  
	}


