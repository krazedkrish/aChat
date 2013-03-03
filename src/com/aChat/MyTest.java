package com.aChat;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MyTest extends TabActivity {
	private static String TAG = MyTest.class.getSimpleName();
	private String username;
	private String password;
	private XmppManager xmppManager;
	private XMPPConnection connection;
	private ChatManager chatManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		// start service
		startService(new Intent(this, ChatService.class));

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab // Create an Intent to
						// launch an Activity for the tab (to be reused)
						// intent = new Intent().setClass(this,
						// Me.class).putExtra("myobject",xmppManager);
		intent = new Intent().setClass(this, MyBuddies.class);
		// getTabHost().updateViewLayout(view, params)

		// Initialize a
		// TabSpec for
		// each
		// tab and add
		// it to
		// the TabHost
		spec = tabHost
				.newTabSpec("buddies")
				.setIndicator("Buddies",
						res.getDrawable(R.drawable.ic_tab_artists))
				.setContent(intent);

		tabHost.addTab(spec);

		// Do the same for the other tabs
		// intent = new Intent().setClass(this, Chats.class);
		// spec = tabHost
		// .newTabSpec("chats")
		// .setIndicator("Chats",
		// res.getDrawable(R.drawable.ic_tab_artists))
		// .setContent(intent);
		//
		// tabHost.addTab(spec);

		intent = new Intent().setClass(this, Me.class);

		spec = tabHost.newTabSpec("myinfo")
				.setIndicator("Me", res.getDrawable(R.drawable.ic_tab_artists))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ChatsList.class);

		spec = tabHost
				.newTabSpec("Chat List")
				.setIndicator("Chat",
						res.getDrawable(R.drawable.ic_tab_artists))
				.setContent(intent);
		tabHost.addTab(spec);

		// TextView title = (TextView) tabHost.getTabWidget().getChildAt(0)
		// .findViewById(android.R.id.title);
		// title.setText("xyz");

		// tabHost.get

		/*
		 * A reference to the TabHost is first captured with getTabHost(). Then,
		 * for each tab, a TabHost.TabSpec is created to define the tab
		 * properties. The newTabSpec(String) method creates a new
		 * TabHost.TabSpec identified by the given string tag. For each
		 * TabHost.TabSpec, setIndicator(CharSequence, Drawable) is called to
		 * set the text and icon for the tab, and setContent(Intent) is called
		 * to specify the Intent to open the appropriate Activity. Each
		 * TabHost.TabSpec is then added to the TabHost by calling
		 * addTab(TabHost.TabSpec).
		 */
		tabHost.setCurrentTab(2);
		// setCurrentTab(int) opens the tab to be displayed by
		// default, specified by the index position of the tab.
	}

	// //// Menu Stuff

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, LoginPreferencesActivity.class));

			break;

		default:
			break;
		}
		return true;
	}

}
