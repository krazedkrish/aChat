package com.aChat;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyaChatApplication extends Application implements
		OnSharedPreferenceChangeListener {
	private static final String TAG = MyaChatApplication.class.getSimpleName();
	SharedPreferences prefs;
	private XmppManager xmppManager = null;

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	/**
	 * Lazily initializes the connection to server
	 * 
	 * @return XmppManager object representing connection
	 */
	public synchronized XmppManager getXmppManager() {

		if (xmppManager == null) {
			// extract the temporary user Inputs : username ,pass, server
			// pre_initialised during AsyncTask login inside doInBackground()
			// method...
			String username = prefs.getString("temporary_username", "");
			String password = prefs.getString("temporary_password", "");
			String server = prefs.getString("temporary_server", "");

			try {
				xmppManager = new XmppManager(server, 5222);
				if (xmppManager.init()) {
					if (xmppManager.performLogin(username, password)) {
						xmppManager.setStatus(true, "Hello EveryOne !!");
						// Thread.sleep(5000);
					} else {
						Log.d(TAG, "error in login");
						return null;
					}
				} else {
					Log.d(TAG, "error in connectioin initialization");
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		// Log.d(TAG, "Login Process Failed...");

		return xmppManager;
	}

	// Called when LoginPreferences is changed...
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// Invalidate xmppManager
		xmppManager = null;
	}

}
