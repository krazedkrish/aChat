package com.aChat;

import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	Button sendButton;
	EditText writeMsg;
	TextView chatArea, userIdattop;
	TextView mText;
	View scroller;
	Time timeStamp;
	private static final String TAG = "HelloFromChatActivity";
	XmppManager xmppManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	//
	// @Override
	// protected void onStop() {
	//
	// super.onStop();
	// Debug.stopMethodTracing();
	// }

	// messageView.append(Html.fromHtml("<font color='green'><b>("+date+") "+
	// username +":</b></font><br/>"+ message+"<br/>", null, null));
}
