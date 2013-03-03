package com.aChat;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

public class BaseaChatActivity extends Activity {
	MyaChatApplication aChatApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		aChatApp = (MyaChatApplication) getApplication();
	}

}
