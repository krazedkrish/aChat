package com.aChat;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

public class Splash extends Activity {

	MediaPlayer splashSound;
	protected int REQUEST_CODE_100 = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		splashSound = MediaPlayer.create(Splash.this, R.raw.splashsound0);
		splashSound.start();
		Intent splashIntent = new Intent(Splash.this, UsernamePassword.class);
		// myIntent.putExtra("ComingFrom", "Me");
		Log.i("splash", "intent created @ splash class succed...");
		startActivity(splashIntent);

		// Thread thread = new Thread() {
		//
		// public void run() {
		//
		// }
		//
		// };
		// thread.start();

	}

	@Override
	protected void onPause() {
		super.onPause();
		splashSound.release();
		finish();
	}

}
