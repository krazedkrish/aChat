package com.aChat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChatReceiver extends BroadcastReceiver {
	private final static String TAG = BroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, ChatService.class));
		Log.d(TAG, "onReceived");
	}

}
