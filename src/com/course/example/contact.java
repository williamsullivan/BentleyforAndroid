package com.course.example;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import android.util.Log;

/**
 * Class houses the contact tab featuring a list activity
 * @author williamsullivan
 *
 */

public class contact extends ListActivity {
EditText text;
	
    ArrayList<String> list;
	ArrayList<phoneEntry> entries;
	ArrayAdapter<String> aa;
	private OutputStreamWriter out;
	private final String file = "dir.txt";
	TextView bannerText;
	int listPosition;
	int counter = 0;
	boolean updateTrigger = false;
	final int PICK1 = Menu.FIRST + 1;
	final int PICK2 = Menu.FIRST + 2;
	final int PICK3 = Menu.FIRST + 3;
	final int PICK4 = Menu.FIRST + 4;
	final int PICK5 = Menu.FIRST + 5;
	final int PICK6 = Menu.FIRST + 6;
	
	/**
	 * oncreate 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		ImageView m;
		m = (ImageView) findViewById(R.id.picview);
		m.setImageResource(R.drawable.bentley); //adds the bentley logo
		list = new ArrayList<String>(); //inst. the list var
		entries = new ArrayList<phoneEntry>(); // inst. the entries arraylist
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		setListAdapter(aa);
		text = (EditText) findViewById(R.id.selection);
		text.setHint("add a number");
		
		doThatCheck(); //checks for existing file
		
		// if there are no items, put these default items in
		if (list.size() == 0){
			
			phoneEntry a = new phoneEntry("Campus Police","7818912201");
			entries.add(a);
			list.add(a.getName());
			phoneEntry b = new phoneEntry("Help Desk","7818913176");
			entries.add(b);
			list.add(b.getName());
			phoneEntry c = new phoneEntry("Health Services","7818912222");
			entries.add(c);
			list.add(c.getName());
			phoneEntry d = new phoneEntry("Registrar","7818912297");
			entries.add(d);
			list.add(d.getName());
			phoneEntry e = new phoneEntry("Academic Services","7818912803");
			entries.add(e);
			list.add(e.getName());	
			
		}
		
		
		
		}
		
	//create menu
		public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, PICK1, Menu.NONE, "Save List");
		menu.add(0, PICK2, Menu.NONE, "Close App");
		menu.add(0, PICK3, Menu.NONE, "Add Entry");
		menu.add(0, PICK4, Menu.NONE, "Delete Entry");
		menu.add(0, PICK5, Menu.NONE, "Update Entry");
		menu.add(0, PICK6, Menu.NONE, "Dial");
		return true;
	}

		//commands for when an option is selected
public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {

		case PICK1: {
			miracleOutputer(); //save file
			return false;
		}

		case PICK2: { // close app
			miracleOutputer(); // save file
			finish(); //exit activity
			return false;
		}

		case PICK3: { // add new list item
			counter = 0;
			
			String str = text.getText().toString();
			entries.add(new phoneEntry(str,str)); // add phone entry
			list.add(str); //add to list view
			aa.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(), str + " added",
					Toast.LENGTH_SHORT).show();
			text.setText("");
			text.setHint("add a number or name");
			
			return false;
		}

		case PICK4: { // remove list item

			if (entries.size() != 0) { // if there are items to remove, remove them
				String str = entries.get(listPosition).toString();
				entries.remove(listPosition);
				list.remove(listPosition);
				counter = listPosition;
				aa.notifyDataSetChanged();
				text.setText("");
				text.setHint("add a number or edit a name");
				updateTrigger = true;

				return false;
			}
			return false;

		}

		case PICK5: { // updates list item

			if (counter != listPosition) { // if the list position is not the same as counter
				if (updateTrigger == false){
					String str = text.getText().toString();
					entries.set(listPosition, new phoneEntry(str,str)); // add new phone entry
					list.set(listPosition, str); //update list name
					aa.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "name updated",
							Toast.LENGTH_SHORT).show();
					text.setText("");
					text.setHint("edit a name or add a number");	
					
				} else {
				String str = text.getText().toString();
				entries.get(listPosition).setName(str); // just set the name only
				list.set(listPosition, str); // update the listview's item
				aa.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "name updated",
						Toast.LENGTH_SHORT).show();
				text.setText("");
				text.setHint("edit a name or add a number");
				}
				

				return false;
			} else if (counter == listPosition && entries.size() >= 1) {

				String str = text.getText().toString();
				entries.set(listPosition, new phoneEntry(str,str)); // add the phone entry
				aa.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "name updated",
						Toast.LENGTH_SHORT).show();
				text.setText("");
				text.setHint("edit a name or add a number");

				return false;
			} else {
				return false;
			}
		}
		
		case PICK6: { // dial item
			try {
			  Intent newintent;
	          Uri u= Uri.parse("tel:" + entries.get(listPosition).getNumber().toString());
	   		  newintent = new Intent(Intent.ACTION_CALL, u);
	          startActivity(newintent);
			} catch (Exception e){
				
			}

				return false;
			}

		default: {
			return false;
		}
		}
	}
	/**
	 * output to text file
	 */
	public void miracleOutputer() {

		// open output stream
		try {
			out = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
		} catch (IOException e) {
		}

		try {

			
			for (int i = 0; i < entries.size(); i++) {
				String m = entries.get(i).getName();
				String x = entries.get(i).getNumber();
				String zz = (m+":"+x);
				
				out.write((zz + " \n"));
				
				
			}
			;

			Toast.makeText(getApplicationContext(), "List saved.",
					Toast.LENGTH_SHORT).show();

			// close output stream
			out.close();
		
		} catch (IOException e) {
			Log.e("IOTest", e.getMessage());
			Toast.makeText(getApplicationContext(), "List could not be saved.",
					Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * check for existing file
	 */
	public void doThatCheck() {
		// open stream for reading from file
		try {
			InputStream in = openFileInput(file);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(isr);
			String str = null;

			while ((str = reader.readLine()) != null) {
				
				String[] temp = str.split(":");
				phoneEntry p = new phoneEntry(temp[0],temp[1]);
				entries.add(p);
				list.add(p.getName());
			}
			
			// close input stream
			reader.close();
			

		} catch (IOException e) {
			Log.e("IOTest", e.getMessage());
		}
		
	}
	/**
	 * when user clicks, update tht position. turn trigger to true so that a user is just updating a name only
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		listPosition = position;
		updateTrigger = true;

	}



}