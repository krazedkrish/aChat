package com.aChat;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class LoginPreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);

	}

}
