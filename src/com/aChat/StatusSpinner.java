package com.aChat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class StatusSpinner extends Activity {

	Spinner spinner;
	Button updateButton;
	EditText text2UpdateMyStatus;

	String aChatStatus;
	String aChatStatusMsg;
	String[] userInfo;
	XmppManager xmppManager;

	// private String resultarraStrings[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statusonprofile);

		// Receiving Extra data in sub activity StatusSpinner
		// Intent sender = getIntent();
		// xmppManager = sender.getExtras().getParcelable("xmppManager");
		// userInfo = new String[3];
		// userInfo = xmppManager.getUserInfo();
		// Intent sender = getIntent();
		// String extraData = sender.getExtras().getString("ComingFrom");
		// Log.d("StatusSpinner", "Spinner activity receiving the intent from: "
		// + extraData);

		try {
			spinner = (Spinner) findViewById(R.id.spinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.statusonprofile,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		} catch (NullPointerException e1) {
			e1.printStackTrace();
			Log.d("inside spinner", "exception caught");
		}

		text2UpdateMyStatus = (EditText) findViewById(R.id.text2UpdateMyStatus);
		// text2UpdateMyStatus.setText(userInfo[2]);

		updateButton = (Button) findViewById(R.id.button2updateMyStatus);
		updateButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				Log.d("Inside StatusSpinner", "what could be the fault ??");
				aChatStatusMsg = text2UpdateMyStatus.getText().toString();
				try {

					Intent returnIntent = new Intent();
					returnIntent.putExtra("ComingFrom", "Hello");
					// intent.putExtra("resultArray", resultarraStrings);
					returnIntent.putExtra("aChatStatusMsg", aChatStatusMsg);
					returnIntent.putExtra("aChatStatusTag", aChatStatus);
					setResult(RESULT_OK, returnIntent);
					finish();

					// setResult(RESULT_OK, (new Intent()).putExtra("myStatus",
					// new StatusSpinner(aChatStatus, text2UpdateMyStatus
					// .getText().toString())));
					// intent.putExtra("student", new Student("1","Mike","6"));

				} catch (RuntimeException e) {
					e.printStackTrace();
					Log.d("@ catch block:StatusSpinner",
							"what is the fault man ??");
				}

			}
		});

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Toast.makeText(parent.getContext(),
					"So  you are: " + parent.getItemAtPosition(pos).toString(),
					Toast.LENGTH_LONG).show();
			aChatStatus = parent.getItemAtPosition(pos).toString();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

}
