package com.aChat;

import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegistration extends Activity {

	private EditText userName, password1, password2, fullname, email, server;
	private Button submit;
	private XmppManager xmppManager;
	private XMPPConnection connection;
	protected AccountManager accountManager;
	protected HashMap<String, String> uDetails;
	String userNameStr, password1Str, password2Str, fullnameStr, emailStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userregistration);
		Log.i("test", "inside uerregistration");

		userName = (EditText) findViewById(R.id.userreg_username);
		password1 = (EditText) findViewById(R.id.userreg_pass1);
		password2 = (EditText) findViewById(R.id.userreg_pass2);
		fullname = (EditText) findViewById(R.id.userreg_fullname);
		email = (EditText) findViewById(R.id.userreg_email);
		server = (EditText) findViewById(R.id.userreg_server);
		submit = (Button) findViewById(R.id.submitreg);

		submit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				xmppManager = new XmppManager(server.getText().toString(), 5222);
				xmppManager.init();
				connection = xmppManager.returnConnection();
				accountManager = new AccountManager(connection);
				userNameStr = userName.getText().toString();
				password1Str = password1.getText().toString();
				password2Str = password2.getText().toString();
				fullnameStr = fullname.getText().toString();
				emailStr = email.getText().toString();

				if (password1Str.equals(password2Str)) {

					if (accountManager.supportsAccountCreation()) {
						// if this server supports account creation
						// textView.setText("this server supports account creation.");

						// create map to save new user details to uDetails
						// variable
						uDetails = new HashMap<String, String>();
						uDetails.put("name", fullnameStr);
						uDetails.put("email", emailStr);
						// create account
						try {
							accountManager.createAccount(userNameStr,
									password1Str, uDetails);
							xmppManager.destroy();
							// redirect to login page

							Intent myIntent = new Intent(UserRegistration.this,
									UsernamePassword.class);
							Toast.makeText(
									UserRegistration.this,
									R.string.hurrayNewUserCreated + userNameStr,
									Toast.LENGTH_LONG).show();
							Thread.sleep(2000);
							startActivity(myIntent);

						} catch (XMPPException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						// if this server doesn't support account creation
						Toast.makeText(
								UserRegistration.this,
								"this server doesn't support account creation.",
								Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(UserRegistration.this,
							R.string.passwordMismatch, Toast.LENGTH_LONG)
							.show();
				}

			}
		});

	}

	@Override
	protected void onPause() {

		super.onPause();
		finish();
	}

}
