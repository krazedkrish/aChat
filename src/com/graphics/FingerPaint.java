/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.graphics;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.aChat.MyaChatApplication;
import com.aChat.XmppManager;

public class FingerPaint extends GraphicsActivity implements
		ColorPickerDialog.OnColorChangedListener {
	private static final String TAG = FingerPaint.class.getSimpleName();
	// //////////////
	MyaChatApplication aChatApp;
	XmppManager xmppManager;
	Message message;
	Chat chat;
	private XMPPConnection connection;
	String secondUser;
	private JSONObject recJsonObject;
	private XML jsonXml;
	String msgRec;
	PacketFilter filter;
	private Path recPath;
	MyView myView;
	private Paint mPaint;
	private Paint recPaint;
	private MaskFilter mEmboss;
	private MaskFilter mBlur;
	CoordinateArray recCArray;
	
//	private int coArrayColor;
	
	
	
	LocalCoArray localCoArray;

	// BroadcastReceiver broadcastReceiver;
	// Button sendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// /////////////
		aChatApp = (MyaChatApplication) getApplication();
		xmppManager = aChatApp.getXmppManager();
		connection = xmppManager.returnConnection();
		secondUser = getIntent().getExtras().getString("username").toString();
		Log.d(TAG, "got user is:" + secondUser);
		// start listening to new msg

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFFFF0000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);

		mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, 3.5f);

		mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
		// setContentView(new MyView(this));
		myView = new MyView(this);
		// final MyView myView = new MyView(this);
		// myView.fingerPaintMsgListener();
		setContentView(myView);

		// startListenter();

		
		
		localCoArray = new LocalCoArray( mPaint.getColor() );


		// //////////////////

	}

	public void colorChanged(int color) {
		mPaint.setColor(color);
	}

	public class MyView extends View {

		private static final float MINP = 0.25f;
		private static final float MAXP = 0.75f;

		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;
		Handler handler = new Handler();

		// ////////////
		CoordinateArray coArray;

		// ////////////

		public MyView(Context c) {
			super(c);

			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
			startListenter();
		}

		public void startListenter() {
			filter = new MessageTypeFilter(Message.Type.chat);
			// for finger paint message listener
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {

					Message message = (Message) packet;
					// Path recPath;s

					if (message.getBody() != null) {
						Log.d(TAG, "new any type message received");
						msgRec = message.getBody();
						recCArray = new CoordinateArray();
						if (recCArray.setOjbectsFromXMLString(msgRec)) {
							Log.d(TAG, "coordinate array received" + msgRec);
							// ////////
							handler.post(new Runnable() {
								public void run() {
									// myView.recDrawprint(recCArray);
									recDrawprint(recCArray);
								}
							});
							// /////////////
							// myView.recDrawprint(recCArray);

						} else {
							Log.d(TAG, "message received:" + msgRec);
						}

					}
				}
			}, filter);

		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFFAAAAAA);

			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;

			// ////////////////////////
			coArray = new CoordinateArray();
			coArray.addXnY(x, y);
			// /////////////////////
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;

				coArray.addXnY(x, y);
			}
		}

		private void touch_up() {
			Log.d(TAG, "inside touch_up");
			mPath.lineTo(mX, mY);
			// ////////////////////////////
			// sendMessagePath(mPath);
			sendMessagePath(coArray);
			// ////////////////////////////
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}

		public void recDrawprint(CoordinateArray cArray) {
			Log.d(TAG, "inside the recDrawinprint method");


			recPaint = new Paint();;
			
			Path recPath = new Path();
			// touch start
			recPath.reset();
			float lx = 0, ly = 0;
			float recX, recY;
			try {
				lx = (float) cArray.getXAt(0);
				ly = (float) cArray.getYAt(0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			recPath.moveTo(lx, ly);
			recX = lx;
			recY = ly;

			// touch move
			for (int i = 1;; i++) {
				try {
					lx = (float) cArray.getXAt(i);
					ly = (float) cArray.getYAt(i);
					recPath.quadTo(recX, recY, (lx + recX) / 2, (ly + recY) / 2);
					recX = lx;
					recY = ly;

					Log.d(TAG, "---Recieved Emboss"+cArray.getEmbossFlag());
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					Log.d(TAG, "..JSONException"+e.getLocalizedMessage());
					e.printStackTrace();
					// if ( e.getMessage().equals(object))
					break;
				}
			}
			Log.d(TAG, "finished drawing and painting remaning");

			
			{

				recPaint.setAntiAlias(true);
				recPaint.setDither(true);
//				recPaint.setColor(0xFFFF0000);
				recPaint.setStyle(Paint.Style.STROKE);
				recPaint.setStrokeJoin(Paint.Join.ROUND);
				recPaint.setStrokeCap(Paint.Cap.ROUND);
				recPaint.setStrokeWidth(12);
				
				
				recPaint.setColor(cArray.getColor());
				
				if ( cArray.getEmbossFlag() == true )
				{
					if (recPaint.getMaskFilter() != mEmboss) {
						recPaint.setMaskFilter(mEmboss);
					}
				}
				else if ( cArray.getBlurFlag() == true )
				{

					if (recPaint.getMaskFilter() != mBlur) {
						recPaint.setMaskFilter(mBlur);
					}	
				}
				else
				{
					recPaint.setMaskFilter(null);
				}
				
				if ( cArray.getEraserFlag() == true )
				{
					Log.d(TAG,"Erase Flag is true");
					recPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
				}
				
				if ( cArray.getScratchFlag()== true )
				{

					recPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
					recPaint.setAlpha(0x80);
				}
			}
			
			
			
			
			
			
			// touch up
			recPath.lineTo(recX, recY);
			// commit the path to our offscreen
			mCanvas.drawPath(recPath, recPaint);
			// kill this so we don't double draw
			recPath.reset();
			Log.d(TAG, "canvas complete");

			invalidate();
			// touch_up();

			// mCanvas.drawPath(path, mPaint);

			// mCanvas.drawColor(0xFFAAAAAA);

			// mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			// mCanvas.drawPath(mPath, mPaint);

		}

	}// end of MyView class

	private static final int COLOR_MENU_ID = Menu.FIRST;
	private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
	private static final int BLUR_MENU_ID = Menu.FIRST + 2;
	private static final int ERASE_MENU_ID = Menu.FIRST + 3;
	private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
		menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
		menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
		menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
		menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

		/****
		 * Is this the mechanism to extend with filter effects? Intent intent =
		 * new Intent(null, getIntent().getData());
		 * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		 * menu.addIntentOptions( Menu.ALTERNATIVE, 0, new ComponentName(this,
		 * NotesList.class), null, intent, 0, null);
		 *****/
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mPaint.setXfermode(null);
		mPaint.setAlpha(0xFF);

		localCoArray.eraserFlag = false;
		localCoArray.scratchFlag= false;
		switch (item.getItemId()) {
		case COLOR_MENU_ID:
			new ColorPickerDialog(this, this, mPaint.getColor()).show();
			return true;
		case EMBOSS_MENU_ID:
			if (mPaint.getMaskFilter() != mEmboss) {
				mPaint.setMaskFilter(mEmboss);
				localCoArray.embossFlag = true;
				localCoArray.blurFlag = false;
			} else {
				mPaint.setMaskFilter(null);
				localCoArray.embossFlag = false;
			}
			return true;
		case BLUR_MENU_ID:
			if (mPaint.getMaskFilter() != mBlur) {
				mPaint.setMaskFilter(mBlur);
				localCoArray.embossFlag = false;
				localCoArray.blurFlag = true;
			} else {
				mPaint.setMaskFilter(null);
				localCoArray.blurFlag = false;
			}
			return true;
		case ERASE_MENU_ID:
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			localCoArray.eraserFlag = true;
			localCoArray.scratchFlag= false;
			return true;
		case SRCATOP_MENU_ID:
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
			mPaint.setAlpha(0x80);
			localCoArray.scratchFlag= true;
			localCoArray.eraserFlag = false;
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// /////////////////////////
	public void sendMessagePath(CoordinateArray coArray) {
		Log.d(TAG, "inside sendMessagePath");
		// JSONObject mPathJsonObject = new JSONObject();

		try {
			// mPathJsonObject.put("Path", mPath);
			// String str = XML.toString(mPathJsonObject);
			// Log.d(TAG, "message send:"+str);

			localCoArray.color = mPaint.getColor();
			coArray.setBlurFlag(localCoArray.blurFlag);
			coArray.setColor(localCoArray.color);
			coArray.setEmbossFlag(localCoArray.embossFlag);
			coArray.setEraserFlag(localCoArray.eraserFlag);
			coArray.setScratchFlag(localCoArray.scratchFlag);
			
			String str = coArray.getXMLString();
			xmppManager.sendMessage(str, secondUser);
			Log.d(TAG, "message send" + str);
			Log.d(TAG, "---Emboss"+localCoArray.embossFlag);

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "caught exception2");
		}
	}

	private class LocalCoArray
	{
		public int color;
		public boolean embossFlag;
		public boolean blurFlag;
		public boolean eraserFlag;
		public boolean scratchFlag;
		
		public LocalCoArray( int color )
		{
			this.color = color;
			embossFlag = false;
			blurFlag = false;
			eraserFlag = false;
			scratchFlag = false;
		}
	}
}
