package com.course.example;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * Code for the map / contact web content and tab host
 * @author williamsullivan
 *
 */
public class MapWebTab extends MapActivity {

	private WebView webView;  //webview object
	private MapView map = null; //map object
	private long lastTouchTimeDown = -1; //for keeping track of long press
	private long lastTouchTimeUp = -1; // for keeping track of long press
	
	private boolean isZoomed = false; //bool to keep track whether the view is zoomed for interface experience
	private Drawable image; // image object that is resusable
	private Intent intent; // re-usable intent
	Thread t; // thread for message system
	int j; //allows tracking for thread
	final int PICK1 = Menu.FIRST + 1; // close app option
	final int PICK2 = Menu.FIRST + 2; // quick directory option
	private SharedPreferences SPrefs; // shared preference for sharing URL
	TextView bannerText; // banner text for the text view
	
	/**
	 * creates handler to interpret thread values for displaying messages, utilizes the thread like a timed clock
	 */
	Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			int j = msg.what;
			if (j==1) { 
				falconToast("Home of the Falcons",0);
				bannerText.setText("tap light bulb for building info");
				
			} else if (j ==11){
				falconToast("MSIT program #2 in New England",0);
			}
			
			if (j==12){

				try {
					t.stop();
				} catch (Exception e) {
					
					
					
				}			
			}
			
		}
	};
	
	/**
	 * creates the thread operation which increments a number, puts thread
	 * to sleep. Basically turns thread into a timed
	 * notification system.
	 */
	Runnable background = new Runnable() {
		public void run() {
			try {
				//every second create a Message object and place on queue
					while (true){
				    Thread.sleep(8000);
				    j +=1;
					Message msg = handler.obtainMessage(j);
					handler.sendMessage(msg);	
					}
					
				
			} catch (InterruptedException e) {System.out.println(e.getMessage());
			}
			
		}
	
	};
	
	/**
	 * create option menu item that allows a user to exit the app.
	 */
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			
			super.onCreateOptionsMenu(menu);	
			MenuItem item4 = menu.add(0, PICK2, Menu.NONE, "Access Campus Phone Book");
			MenuItem item3 = menu.add(0, PICK1, Menu.NONE, "Exit");
			return true;
		}
	 
		/**
		 * create option menu item that allows a user to exit the app.
		 */
		
			public void runWebIntent() {
				
			 intent = new Intent(this, web.class);
				startActivity(intent);
			}
			
			/**
			 * change text view text hint for navigation help
			 */
			
				public void isZoomedViewTextSwitch(String text) {
					
				 bannerText.setText(text);
				}
	    
	 /**
	  * simple statement used to determine the item pressed to exit.
	  */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {

			int id = item.getItemId();

			switch (id) {

			case PICK1: { // close app
				finish();
				return false;
			}

			case PICK2: { // access phonebook
				
				Intent intent = new Intent(this, contact.class);
				startActivity(intent);
				return false;
			}


			

			default: {
				return false;
			}
			}
		}
	
/**
 * specifies tab oncreate features
 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		//Text view, text changes according to thread choice
		bannerText=(TextView)findViewById(R.id.textview);
		
		
		//start thread that will help with navigation pop ups
		t = new Thread(background);
		t.start();
		

		
		// creates the top logo for the app
		ImageView m;
		m = (ImageView) findViewById(R.id.picview);
		m.setImageResource(R.drawable.bentley);
		
		//creates map view widget, sets the location and zoom level to Bentley Campus 
		map = (MapView) findViewById(R.id.map);
		map.getController().setCenter(getPoint(42.386074, -71.221606));
		map.getController().setZoom(17); 

		//creates marker, adds image to marker
		Drawable marker = getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight());

		map.getOverlays().add(new SitesOverlay(marker)); //adds overlay to map
		map.setSatellite(true);// enables satellite view by default

		falconToast("Welcome to Bentley",1);
		

	}

	/**
	 * returns the route displayed as false every time
	 */
	protected boolean isRouteDisplayed() {
		return (false);
	}

	/**
	 * custom image toast w/ bentley falcon
	 */
	private void falconToast(String text, int falconType){
	Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
	View textView = toast.getView();
	LinearLayout l = new LinearLayout(this);
	l.setOrientation(LinearLayout.HORIZONTAL);
	ImageView view = new ImageView(this);
	
	if (falconType == 1){
		view.setImageResource(R.drawable.falcon);
	} else {
		view.setImageResource(R.drawable.smfalc);
	}
	l.addView(view);
	l.addView(textView);
	toast.setView(l);
	toast.show();
	}
	

	/**
	 * Keeps track of when a hardware key S is pressed, to change view between normal and satellite
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			map.setSatellite(!map.isSatellite());
			return (true);
		}
		return (super.onKeyDown(keyCode, event));
	}

	/**
	 * returns the geopoint location of the object
	 * @param lat
	 * @param lon
	 * @return
	 */
	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	/**
	 * creates the SiteOveraly class in order to add items to the overlay for the map view widget
	 * @author williamsullivan
	 *
	 */
	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>(); // create arraylist to contain sites
		private Drawable marker = null; //initialize marker
		private Intent intent; // re-usable intent

		
		/**
		 * Adds the locations to the overlay, along with their positions, title, and respective
		 * website.
		 * @param marker
		 */
		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;

			items.add(new OverlayItem(getPoint(42.387999, -71.219863), // add the geopoint, title, and URL to item array
					"Library", "http://library.bentley.edu/history.asp"));
			
			items.add(new OverlayItem(getPoint(42.388099,-71.220162),
					"Help Desk",
					"http://client-services.bentley.edu/"));

			items.add(new OverlayItem(getPoint(42.387162, -71.21877),
					"Adamian Academic Center",
					"http://www.bentley.edu/vmap/adamian.cfm"));

			items.add(new OverlayItem(getPoint(42.387162, -71.220487),
					"Smith Technology Building",
					"http://www.bentley.edu/vmap/smith.cfm"));

			items.add(new OverlayItem(getPoint(42.388126, -71.220893),
					"Jennison Hall", "http://www.bentley.edu/vmap/jennison.cfm"));

			items.add(new OverlayItem(getPoint(42.38851, -71.221229),
					"Rauch Administration Center",
					"http://www.bentley.edu/vmap/rauch.cfm"));

			items.add(new OverlayItem(getPoint(42.388812, -71.220168),
					"LaCava Campus Center",
					"http://www.bentley.edu/vmap/lacava.cfm"));

			items.add(new OverlayItem(getPoint(42.387964, -71.218983),
					"Morrison Hall", "http://www.bentley.edu/vmap/morison.cfm"));

			items.add(new OverlayItem(getPoint(42.387734, -71.22314),
					"Miller Hall", "http://www.bentley.edu/vmap/miller.cfm"));

			items.add(new OverlayItem(getPoint(42.384119, -71.223137),
					"Fenway Hall", "http://www.bentley.edu/vmap/fenway.cfm"));

			items.add(new OverlayItem(getPoint(42.384959, -71.223148),
					"Copley North", "http://www.bentley.edu/vmap/copley.cfm"));

			items.add(new OverlayItem(getPoint(42.384563, -71.222633),
					"Copley South", "http://www.bentley.edu/vmap/copley.cfm"));

			items.add(new OverlayItem(getPoint(42.383402, -71.224769),
					"Dana Athletic Center",
					"http://www.bentley.edu/vmap/dana.cfm"));

			items.add(new OverlayItem(getPoint(42.385189, -71.224253),
					"Orchard North Apartments",
					"http://www.bentley.edu/vmap/orchard.cfm"));

			items.add(new OverlayItem(getPoint(42.384642, -71.224392),
					"Orchard South Apartments",
					"http://www.bentley.edu/vmap/orchard.cfm"));

			items.add(new OverlayItem(getPoint(42.386006, -71.222858),
					"Student Center",
					"http://www.bentley.edu/vmap/studentcenter.cfm"));

			items.add(new OverlayItem(getPoint(42.38675, -71.222418),
					"Collins Hall", "http://www.bentley.edu/vmap/collins.cfm"));
			items.add(new OverlayItem(getPoint(42.386006, -71.222858),
					"Rhodes Hall", "http://www.bentley.edu/vmap/rhodes.cfm"));
			items.add(new OverlayItem(getPoint(42.386624, -71.224263),
					"Forest Hall", "http://www.bentley.edu/vmap/forest.cfm"));
			items.add(new OverlayItem(getPoint(42.386227, -71.223963),
					"Kresge Hall", "http://www.bentley.edu/vmap/kresge.cfm"));
			items.add(new OverlayItem(getPoint(42.387678, -71.221667),
					"Falcone Apartments",
					"http://www.bentley.edu/vmap/falcone.cfm"));
			items.add(new OverlayItem(getPoint(42.386259, -71.221946),
					"Boylston Apartments",
					"http://www.bentley.edu/vmap/boylston.cfm"));
			items.add(new OverlayItem(getPoint(42.385744, -71.220948),
					"Campus Police", "http://www.bentley.edu/vmap/police.cfm"));
			items.add(new OverlayItem(getPoint(42.385942, -71.220444),
					"Slade Hall", "http://www.bentley.edu/vmap/slade.cfm"));
			items.add(new OverlayItem(getPoint(42.386085, -71.219618),
					"Trees Dorm Complex",
					"http://www.bentley.edu/vmap/trees.cfm"));
	

			populate(); // finalize the overlay information
		}

		/**
		 * allows you to access specific overlay item
		 */
		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));
		}

		/**
		 * creates canvas to which the overlay requires
		 */
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}

		/**
		 * the methods for how tap is used to navigate the interface
		 */
		@Override
		protected boolean onTap(int i) {
			long pressTotalTime = lastTouchTimeUp - lastTouchTimeDown; //tells us the total press time
			if (pressTotalTime > 1500) { //this is a long tap		
				SPrefs = getSharedPreferences("MySPrefs", MODE_PRIVATE);
		        SharedPreferences.Editor editor = SPrefs.edit();
		        editor.putString("id", items.get(i).getTitle());
		        editor.putString("url", items.get(i).getSnippet().toString());
		        editor.commit();
		        falconToast(items.get(i).getTitle(),0);
				pressTotalTime = 0;
				runWebIntent(); // start web activity
			} else if (isZoomed == false) { //if we are not already zoomed, zoom on building
				isZoomedViewTextSwitch("long tap light bulb for more info");
				Toast.makeText(getApplicationContext(),
						items.get(i).getTitle(), Toast.LENGTH_SHORT).show(); //toast building name
				map.getController().setCenter(items.get(i).getPoint()); //set object location to center of view
				map.getController().setZoom(20); // range 1..21 //zoom in on object
				
				falconToast("Tap light bulb to return to campus map",0);
				
				isZoomed = true; //bool now true to indicate we are zoomed

			} else if (isZoomed == true) { //we are zoomed on a building already
				isZoomedViewTextSwitch("tap light bulb for building info");
				map.getController().setCenter(getPoint(42.386074, -71.221606)); //set it back to the normal view of campus
				map.getController().setZoom(17); //zoom out for full campus view
				Toast.makeText(getApplicationContext(), "Campus Map", //toast to the user that we have changed to the campus map view
						Toast.LENGTH_SHORT).show();
				isZoomed = false; //reset bool
			}

			return (true);
		}

		/**
		 * when a user presses, we keep track on when they press down, update the variables, and 
		 * do the same for when they release
		 */
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				lastTouchTimeDown = System.currentTimeMillis();
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				lastTouchTimeUp = System.currentTimeMillis();
			}
			return super.onTouchEvent(event, mapView);
		}

		/**
		 * gives us the size of the items array
		 */
		@Override
		public int size() {
			return (items.size());
		}
		
	}
}