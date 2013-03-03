package com.aChat;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ChatsList extends ListActivity {

	ChatListReceiver receiver;
	Intent intent2;
	private static final String TAG = ChatsList.class.getSimpleName();
	private ArrayList<String> mString = new ArrayList<String>();
	ListView listView;
	private Boolean isTextType = true;
	private ArrayList<String> chatListArray = new ArrayList<String>();

	// private Context context =

	// private String

	private void setListAdapter() {
		//
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, mString);
		setListAdapter(arrayAdapter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.chatbuddylist);
		// mString.add("test bot");
		setListAdapter();
		if (receiver == null) {
			receiver = new ChatListReceiver();
		}
		// receiver to receive broadcast
		registerReceiver(receiver,
				new IntentFilter(ChatService.CHATMSG_BFILTER));
		// registerReceiver(receiver, new Inte);
		listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String string = arg0.getItemAtPosition(arg2).toString();
				Log.d(TAG, "item at array adapter clicked and value is:"
						+ string);
				// //////////////////////////////
				// intent2 = new Intent(ChatsList.this, ChatsTab.class);
				// intent2.putExtra("username", string);
				// //////////////////

				// select text type or finger-paint type message
				if (isTextType.equals(true)) {
					// if (!ChatsTab.flag.booleanValue()) {
					intent2 = new Intent(ChatsList.this, ChatsTab.class);
					intent2.putExtra("username", string);
					// intent2.setFlags(Intent.FLAG_a);
					// intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

					// } else {
					// intent2 = new Intent(ChatsList.this, ChatsTab.class);
					// intent2.putExtra("username", string);
					// }

				} else {
					intent2 = new Intent(ChatsList.this,
							com.graphics.FingerPaint.class);
					intent2.putExtra("username", string);
				}
				startActivity(intent2);

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chatlistmenu, menu);
		return true;

	}

	// Menu stuff
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case R.id.itemPrefs:
		// startActivity(new Intent(this, LoginPreferencesActivity.class));
		// break;
		case R.id.fingerPaint:
			// dbClass.delete();
			isTextType = false;
			Toast.makeText(this, R.string.fingerPaintSel, Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.message:
			isTextType = true;
			Toast.makeText(this, R.string.chatTypeSel, Toast.LENGTH_LONG)
					.show();

		}
		return true;
	}

	public void printNewMsgToast(String user, String msg) {
		// Toast.makeText(, R.string.chatTypeSel, Toast.LENGTH_LONG).show();
		Log.d(TAG, "Printing new message toast");
		Toast.makeText(this, user + ":" + msg, Toast.LENGTH_LONG);
	}

	/**
	 * class to receive broadcasted chat-list
	 * 
	 * @author Rabin
	 * 
	 */

	class ChatListReceiver extends BroadcastReceiver {
		private String gotUser, gotText;

		@Override
		public void onReceive(Context context, Intent intent) {
			// update the current chat-buddy list here
			Log.d(TAG, "new buddy list received.");

			try {
				// mString.add("test user");
				gotUser = intent.getExtras()
						.getString(ChatService.CHATMSG_BFILTER_MSGFRM)
						.toString();
				gotText = intent.getExtras()
						.getString(ChatService.CHATMSG_BFILTER_MSGTXT)
						.toString();

				// print toast for new message received
				printNewMsgToast(gotUser, gotText);

				// update chat list
				if (chatListArray.contains(gotUser)) {
					// do nothing
				} else {
					chatListArray.add(gotUser);
					mString.add(gotUser);
					setListAdapter();

				}
				// startActivity(intent2);
			} catch (Exception e) {

			}
		}
	}

}
