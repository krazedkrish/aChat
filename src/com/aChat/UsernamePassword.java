package com.aChat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UsernamePassword extends BaseaChatActivity implements
		OnClickListener {
	private static String TAG = UsernamePassword.class.getSimpleName();

	private MediaPlayer loginsound;
	private EditText username, password, server;
	private Button signInButton;
	private XmppManager xmppManager;
	private SharedPreferences preferences;
	private TextView registerNewUser;
	ProgressDialog dialog;
	TextView text;

	private static final int DIALOG_KEY = 7;
	private static final int DIALOG_KEY_ABOUT_ME = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		text = (TextView) findViewById(R.id.textView1);
		// String string = (String) Character.UnicodeBlock.forName("\u0915");
		// Character.UnicodeBlock.forName("\u0915");
		// text.append(text);
		char ch1 = '\u0915';

		Character.UnicodeBlock block = Character.UnicodeBlock.of(ch1);
		text.setText(block.toString());
		int i = 915;

		block = Character.UnicodeBlock.of(i);
		text.append(block.toString());

		block = Character.UnicodeBlock.of(ch1);
		text.setText(block.toString());

		// get resources
		loginsound = MediaPlayer.create(UsernamePassword.this,
				R.raw.splashsound0);
		loginsound.start();

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		server = (EditText) findViewById(R.id.editTextServer);
		signInButton = (Button) findViewById(R.id.singIn);
		registerNewUser = (TextView) findViewById(R.id.iDoNotHaveAaChatAccoutYet);

		signInButton.setOnClickListener(this);

		registerNewUser.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(UsernamePassword.this,
						UserRegistration.class));
			}
		});
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

	}

	// //// Dialog Stuff
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_KEY: {
			dialog = new ProgressDialog(this);
			dialog.setMessage(UsernamePassword.this
					.getString(R.string.logingInToaChat));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		case DIALOG_KEY_ABOUT_ME: {

			dialog = new ProgressDialog(this);
			dialog.setTitle(getString(R.string.about_aChat));
			dialog.setMessage(UsernamePassword.this
					.getString(R.string.about_aChat));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);

			return dialog;

		}
		}
		return null;

	}

	// / Menu Stuffs
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_welcome_screen, menu);
		return true;

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_About_Me:
			showDialog(DIALOG_KEY_ABOUT_ME);

			break;

		default:
			break;
		}
		return true;
	}

	// signIn Button listener
	public void onClick(View v) {
		showDialog(DIALOG_KEY);

		String usersLoginPreferences[] = new String[3];
		usersLoginPreferences[0] = username.getText().toString();
		usersLoginPreferences[1] = password.getText().toString();
		usersLoginPreferences[2] = server.getText().toString();

		new InitiliseXmppConnection().execute(usersLoginPreferences);
		Log.d(TAG, "Getting out of the AsyncTask thread");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onresume method");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onpause method");
		loginsound.stop();
		finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onstop method");
		finish();
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

	/**
	 * Sub class to implement the AsyncTask Thread...
	 * 
	 * @author home
	 * 
	 */
	private class InitiliseXmppConnection extends
			AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... usersLoginPreferences) {
			Log.d(TAG, "wao ... awesome !! on using the Asychronous Task");
			try {
				Log.d(TAG, String.format(
						"loging in as: %s  with passWrd:%s  @server:%s",
						usersLoginPreferences[0], usersLoginPreferences[1],
						usersLoginPreferences[2]));
				preferences
						.edit()
						.putString("temporary_username",
								usersLoginPreferences[0]).commit();
				preferences
						.edit()
						.putString("temporary_password",
								usersLoginPreferences[1]).commit();
				preferences
						.edit()
						.putString("temporary_server", usersLoginPreferences[2])
						.commit();

				// MyaChatApplication aChatApp = ((MyaChatApplication)
				// UsernamePassword.this
				// .getApplication());
				xmppManager = aChatApp.getXmppManager();
				if (xmppManager != null) {
					xmppManager
							.printXmppManagerObjectData(UsernamePassword.this);
					Log.d(TAG, "we got the XmppManager");
					// result = UsernamePassword.this
					// .getString(R.string.loginSucessfulMsg);
					// return result;
					// return getResources()
					// .getInteger(R.string.loginSucessfulMsg);
					return 1;
				}

			} catch (Exception e) {
				e.printStackTrace();
				// result = UsernamePassword.this
				// .getString(R.string.logoinFailureMsg);
				Log.d(TAG, UsernamePassword.this
						.getString(R.string.loginFailureMsg));
				Toast.makeText(
						UsernamePassword.this,
						UsernamePassword.this
								.getString(R.string.loginSucessfulMsg),
						Toast.LENGTH_LONG).show();
			}
			// return UsernamePassword.this.getResources().getInteger(
			// R.string.loginFailureMsg);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG_KEY);

			switch (result) {
			case 0:
				Toast.makeText(
						UsernamePassword.this,
						UsernamePassword.this
								.getString(R.string.loginFailureMsg),
						Toast.LENGTH_LONG).show();

				break;
			case 1:
				Intent myIntent = new Intent(UsernamePassword.this,
						MyTest.class);

				startActivity(myIntent);
				break;
			default:
				break;
			}
		}
	}
}
