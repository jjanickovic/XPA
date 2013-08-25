package org.xpa.example.activity;

import org.xpa.example.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	
	private void addSpec(TabHost host, String tag, Class<?> activityClass) {
		TabSpec spec = host.newTabSpec(tag);
		spec.setIndicator(tag);
		Intent intent = new Intent(this, activityClass);
		spec.setContent(intent);
		host.addTab(spec);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		TabHost tabHost = getTabHost();
		
		addSpec(tabHost, "From Xml", XmlDeserializationActivity.class);
		addSpec(tabHost, "To Xml", XmlSerializationActivity.class);
		addSpec(tabHost, "JSON", JsonActivity.class);
		addSpec(tabHost, "XPA", XPAActivity.class);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_about:
			//TODO
			break;
		default:
			return super.onOptionsItemSelected(item);
		}

		return true;
	}
}
