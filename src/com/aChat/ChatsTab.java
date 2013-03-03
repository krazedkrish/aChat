package com.aChat;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class ChatsTab extends TabActivity {

	private static final String TAG = ChatsTab.class.getSimpleName();
	Resources res;// = getResources(); // Resource object to get Drawables
	TabHost tabHost;// = getTabHost(); // The activity TabHost
	TabHost.TabSpec spec; // Resusable TabSpec for each tab
	Intent intent; // Reusable Intent for each tab // Create an Intent to
	// ArrayList<Chats> chatArrayList = new ArrayList<Chats>();
	// Chats chats;
	public String userName = "userName";
	// to store list of current users in tab
	private ArrayList<String> tabbedUserList = new ArrayList<String>();
	// private String sendingUser;
	private Button buttonChatBack, buttonClose;
	private int tabCounter = 0;
	static Boolean flag = false;

	// chatArrayList.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "oncreate method");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chattab);

		res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost

		// what if close button is pressed: simple->>> close the chatting buddy
		buttonClose = (Button) findViewById(R.id.buttonClose);
		buttonClose.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				deleteFriendEntry();

			}
		});

		// button to go back to the chatlist
		buttonChatBack = (Button) findViewById(R.id.buttonChatBack);
		buttonChatBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(ChatsTab.this, MyTest.class)
						.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				// addFriendEntry("newuser" + Integer.toString(+tabCounter));
			}
		});

	}

	private void doForTheFirstTime(String sendUser) {

		Log.d(TAG, "Inside doForTheFirstTime  with username " + sendUser);
		Intent myFirstChatTabIntent = new Intent();
		myFirstChatTabIntent.setClass(ChatsTab.this, Chats.class);
		myFirstChatTabIntent.putExtra("username", sendUser);
		String cString = Intent.CATEGORY_LAUNCHER;
		Log.d("**********************************\nIntent create'd type\n",
				cString);
		tabHost.addTab(tabHost.newTabSpec("InitialChat").setIndicator(sendUser)
				.setContent(myFirstChatTabIntent));
		flag = true;
	}

	private void addFriendEntry(String UserImChatingWith) {

		Log.d(TAG, "Inside addFriendEntry  with username " + UserImChatingWith);
		Intent myFirstChatTabIntent = new Intent();
		myFirstChatTabIntent.setClass(ChatsTab.this, Chats.class);
		myFirstChatTabIntent.putExtra("username", UserImChatingWith);
		tabHost.addTab(tabHost.newTabSpec("SecondChat")
				.setIndicator(UserImChatingWith)
				.setContent(myFirstChatTabIntent));
		Log.d("tabCounter", Integer.toString(tabCounter));
		++tabCounter;

	}

	private void deleteFriendEntry() {

		// Since we can't really delete a TAB
		// We hide it

		int position = tabHost.getCurrentTab();
		Log.d("Position", Integer.toString(position));

		// if (position != 0 ) {
		//
		// tabHost.getCurrentTabView().setVisibility(1);
		// tabHost.setCurrentTab(position-1);
		//
		// }
		// else if(position== z){
		// tabHost.getCurrentTabView().setVisibility(1);
		// tabHost.setCurrentTab(position+1);
		// }
		Log.d("Total number of counted tabs ", Integer.toString(tabCounter));
		if (position > 0) {
			tabHost.getCurrentTabView().setVisibility(View.GONE);
			tabHost.setCurrentTab(position + 1);
			tabCounter -= 1;
			if (tabCounter < 0)
				tabCounter = 0;
		} else if (position == 0) {
			tabHost.getCurrentTabView().setVisibility(View.GONE);
			tabHost.setCurrentTab(position + 1);
			tabCounter = 0;
		} else if (position == tabCounter) {
			tabHost.getCurrentTabView().setVisibility(View.GONE);
			tabHost.setCurrentTab(tabCounter - 1);
			Log.d("tabCounter value in final", "lol");
			Log.d("Pos", Integer.toString(position));
			Log.d("tabCounter pos", Integer.toString(tabCounter));

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onstart method");
		String sendingUser = null;
		sendingUser = getIntent().getExtras().getString("username").toString();

		Log.d("user name @onStart is :", sendingUser);

		if (!flag.booleanValue()) {

			Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++\n "
					+ "doForTheFirstTime() job carried on"
					+ "\n+++++++++++++++++++++++++++++++++++++++++++++" +

					"and flag value is: " + flag.toString());
			doForTheFirstTime(sendingUser);
		} else {
			Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++\n "
					+ "addFriendEntry() job carried on"
					+ "\n+++++++++++++++++++++++++++++++++++++++++++++" +

					"and flag value is: " + flag.toString());
			addFriendEntry(sendingUser);
		}
		// if (!tabbedUserList.contains(sendUser)) {
		// if new user is selected from chat list then do blah
		// chatArrayList.add(new Chats());
		// chats = new Chats();
		// intent = new Intent().setClass(this,
		// (chatArrayList.get(0)).getClass()).putExtra(userName,
		// sendUser);
		// tabbedUserList.add(sendUser);
		// intent = new Intent().setClass(this, Chats.class).putExtra(
		// userName, sendUser);
		//
		// spec = tabHost
		// .newTabSpec("buddies")
		// .setIndicator(sendUser,
		// res.getDrawable(R.drawable.ic_tab_artists))
		// .setContent(intent);
		// tabHost.addTab(tabHost.addTab(tabHost.newTabSpec("InitialChat")
		// .setIndicator(sendUser).setContent(intent)));
		// // tabHost.newTabSpec(InitialChat)addTab(spec
		// // tabHost.addTab(tabHost.newTabSpec("Main")
		// // .setIndicator(getHost(DEFAULT_URL)).setContent(openBrowser));
		// }
		// tabHost.setCurrentTab(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (!flag.booleanValue()) {
		// doForTheFirstTime();
		// } else
		// addFriendEntry();
		Log.d(TAG, "onresume method");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onpause method");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onstop method");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "ondestroy method");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG, "onrestart method");
	}
	// private Chats chats[] = new Chats

}
