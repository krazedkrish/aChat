package com.aChat;

import com.aChat.MyBuddies.RosterReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Me extends Activity {

	// Definition of the one requestCode we use for receiving resuls could be
	// assigned any value.
	protected static final int REQUEST_CODE_100 = 100;
	private Button statusUpdateButton;
	private TextView userID;
	private String[] userInfo;
	private MyaChatApplication aChatApp;
	private XmppManager xmppManager;
	private static final String TAG = Me.class.getSimpleName();
	private NewMsgReceiver msgReceiver;

	boolean available;

	public void printNewMsgToast(String user, String msg) {
		// Toast.makeText(, R.string.chatTypeSel, Toast.LENGTH_LONG).show();
		Log.d(TAG, "Printing new message toast");
		Toast.makeText(this, "New message received:" + msg + ".From:" + user,
				Toast.LENGTH_LONG).show();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutme);

		// get the XmppConnection Object with the MyaChatApplication Activity
		aChatApp = (MyaChatApplication) getApplication();
		xmppManager = aChatApp.getXmppManager();

		if (msgReceiver == null) {
			msgReceiver = new NewMsgReceiver();
		}
		registerReceiver(msgReceiver, new IntentFilter(
				ChatService.CHATMSG_BFILTER));

		String[] userInfo = xmppManager.getUserInfo();

		userID = (TextView) findViewById(R.id.username);
		userID.setText(userInfo[0]);

		statusUpdateButton = (Button) findViewById(R.id.statusUpdateButton);
		statusUpdateButton.setText(userInfo[2]);

		if (userInfo[1].contentEquals("online")) {
			statusUpdateButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					android.R.drawable.presence_online, 0);
		} else {
			statusUpdateButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					android.R.drawable.presence_offline, 0);
		}

		/**
		 * onClick'ng Button call the StatusSpinner Activity to get new User
		 * Status...
		 */
		statusUpdateButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Toast.makeText(Me.this,
						"creating a new activity for update feeds... ",
						Toast.LENGTH_LONG).show();

				Intent myIntent = new Intent(Me.this, StatusSpinner.class);
				startActivityForResult(myIntent, REQUEST_CODE_100);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// You can use the requestCode to select between multiple child
		// activities you may have started. Here there is only one thing
		// we launch.
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_100 || (data != null)) {
			// This is a standard resultCode that is sent back if the
			// activity doesn't supply an explicit result. It will also
			// be returned if the activity failed to launch.
			String extraData = data.getStringExtra("ComingFrom");
			Log.d("@ Me activity", "returning back to the me activity from: "
					+ extraData);

			String aChatStatusMsg = data.getStringExtra("aChatStatusMsg");
			String aChatStatus = data.getStringExtra("aChatStatusTag");
			statusUpdateButton.setText(aChatStatusMsg);

			xmppManager.setStatus(available, aChatStatusMsg);

			if (aChatStatus.contentEquals("online")) {
				statusUpdateButton.setCompoundDrawablesWithIntrinsicBounds(
						android.R.drawable.presence_online, 0, 0, 0);
			} else if (aChatStatus.contentEquals("away")) {
				statusUpdateButton.setCompoundDrawablesWithIntrinsicBounds(
						android.R.drawable.presence_away, 0, 0, 0);
			}

			else if (aChatStatus.contentEquals("busy")) {
				statusUpdateButton.setCompoundDrawablesWithIntrinsicBounds(
						android.R.drawable.presence_busy, 0, 0, 0);
			} else {
				statusUpdateButton.setCompoundDrawablesWithIntrinsicBounds(
						android.R.drawable.presence_offline, 0, 0, 0);
			}

			// for setting current status to server
			if (aChatStatus.contentEquals("online")) {
				available = true;
			} else
				available = false;

		}
		//
		if (resultCode == RESULT_CANCELED) {
			// text.append("(cancelled)");
			// // Our protocol with the sending activity is that it will send
			// // text in'data' as its result.
		} else {
			// // text.append("(okay ");
			// // text.append(Integer.toString(resultCode));
			// // text.append(") ");
		}
		// if (data != null) {
		// // text.append(data.getAction());
		// // statusUpdateButton.setText(myStatus.aChatStatusMsg);
		//
		// }
	}

	class NewMsgReceiver extends BroadcastReceiver {
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

				// startActivity(intent2);
			} catch (Exception e) {

			}
		}
	}
}
