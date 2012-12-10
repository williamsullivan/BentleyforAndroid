package com.course.example;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

/**
 * create the web activity showing web view and back button as well as a dialer
 * @author williamsullivan
 *
 */

public class web extends Activity implements OnClickListener {

	private Button button; // back button
	private Button button2; // dial button
	private WebView webView;  //webview object
	private SharedPreferences SPrefs; //shared preferences to get the webpage
	private String number; // store number for dialer intent
	private boolean trig = false; // used for conditional creation of dialer listener
	final int PICK1 = Menu.FIRST + 1; // close app option
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        
        SPrefs = getSharedPreferences("MySPrefs", MODE_PRIVATE);
        String id = SPrefs.getString("id", "Foobar");
        String url = SPrefs.getString("url", "Foobar");
        
        
        // couple of if's to see if we need a dialer button visible
        
        if (id.equals("Campus Police")){
        	button2 = (Button) findViewById(R.id.dial);
        	button2.setVisibility(View.VISIBLE);
        	button2.setText("Call " + "CP");
        	number = "7818912201";
        	trig = true;
        	
        } else if (id.equals("Help Desk")){
        	button2 = (Button) findViewById(R.id.dial);
        	button2.setVisibility(View.VISIBLE);
        	button2.setText("Call " + "Help Desk");
        	number = "7818913176";
        	trig = true;
        	
        } else if (id.equals("Library")){
        	button2 = (Button) findViewById(R.id.dial);
        	button2.setVisibility(View.VISIBLE);
        	button2.setText("Call " + "Library");	
        	number = "7818912231";
        	trig = true;
        } 
        
        if(trig==true){
        button2.setOnClickListener(new OnClickListener(){
         
         /**
          * when a person clicks the dial, it dials the number
          */
        	public void onClick(View arg0){
        		
        		try {
          Intent newintent;
          Uri u= Uri.parse("tel:" + number);
   		  newintent = new Intent(Intent.ACTION_CALL, u);
          startActivity(newintent);
        		} catch (Exception e){
        			
        		}
         }
        } );
        
        }
        
        //set the logo
        ImageView m;
		m = (ImageView) findViewById(R.id.picview);
		m.setImageResource(R.drawable.bentley);
		
		
		   		   

        
        //tie in the button
        button = (Button) findViewById(R.id.back);
		
		button.setOnClickListener(new OnClickListener(){
	        // when user presses the button it finishes 
	         public void onClick(View arg0){
	        	 finish();
	         }
	        } );
		webView = (WebView) findViewById(R.id.web); //creates webview
		webView.getSettings().setJavaScriptEnabled(true); //enables javascript
		// webView.setInitialScale(1);   would normally put the whole page in view, allow for zoom but ugly


		webView.loadUrl(url); //snags url from the point
  	   
  	 
    }
    
    
    /**
	 * create option menu item that allows a user to exit the app.
	 */
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			
			super.onCreateOptionsMenu(menu);
			MenuItem item4 = menu.add(0, PICK1, Menu.NONE, "Exit");	
			return true;
		}
    
    
    /**
	  * simple statement used to determine the item pressed to exit.
	  */
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {		
			    
			    int itemID = item.getItemId();  //get id of menu item picked
			    
			    switch (itemID) {
			    
			    case PICK1 : finish();
			    
			    default: super.onOptionsItemSelected(item);
			    }
			   		   
		    return false;
		}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}