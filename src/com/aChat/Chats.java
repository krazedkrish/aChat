package com.aChat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aChat.DbClass.DbHelper;
import com.extras.MyAdapter;

public class Chats extends ListActivity implements OnClickListener,
		OnKeyListener {
	private static final String TAG = Chats.class.getSimpleName();
	HashMap<String, String> codeTable;
	MyaChatApplication aChatApp;

	// declarations for unicode
	Typeface typeFace;
	// Button button;
	String string;
	int i;
	int ini_pos, final_pos;
	String temp;

	// BroadcastReceiver broadcastReceiver;
	Button sendButton;
	EditText writeMsg;
	TextView chatArea, userIdattop;
	View scroller;
	Time timeStamp;
	XmppManager xmppManager;
	Message message;
	Chat chat;
	private XMPPConnection connection;
	private ContentValues contentValues;
	SimpleDateFormat dateFormat;// = new
	Date date;
	DbClass dbClass;
	Cursor cursor;
	private ArrayList<Spanned> mSpannes = new ArrayList<Spanned>();
	static String[] type = { "send" };
	private String username;// "milans";
	private String msg;
	private PacketFilter filter;
	private Handler handler = new Handler();
	String secondUser;// = "rabin555@rabin-pc"; // =
						// getIntent().getExtras().getString("username").toString();
	NewMsgReceiver newMsgReceiver;
	private MyAdapter arrayAdapter;

	private Boolean isUnicode = false;

	// TextView textView;

	public Chats() {
		// TODO Auto-generated constructor stub
		// super();
		// this.secondUser = "rabin555@rabin-pc";
	}

	// create DbHelper class
	private void setListAdapter() {
		//
		// ArrayAdapter<Spanned> arrayAdapter = new ArrayAdapter<Spanned>(this,
		// android.R.layout.simple_expandable_list_item_1, mSpannes);
		arrayAdapter = new MyAdapter(this,
				android.R.layout.simple_expandable_list_item_1, mSpannes);
		ListView listView = new ListView(this);
		// listView.sett
		listView.setAdapter(arrayAdapter);
		TextView textView = new TextView(this);
		textView.setTypeface(typeFace);
		// textView.se
		// textView.setlist

		// arrayAdapter.

		// listItemAdapter = new ArrayAdapter<MenuItem>(this, R.layout.listitem,
		// menuItems);
		//
		// Typeface font = Typeface.createFromAsset(getAssets(),
		// "chantelli_antiqua.ttf");
		// TextView v = (TextView)listItemAdapter.getView(0, null, null);
		// v.setTypeface(font);

		setListAdapter(arrayAdapter);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "oncreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.achat);
		secondUser = getIntent().getExtras().getString("username").toString();
		sendButton = (Button) findViewById(R.id.sendButton);

		// //////////////////////
		writeMsg = (EditText) findViewById(R.id.writeMsg);
		typeFace = Typeface.createFromAsset(getAssets(), "DroidHindi.ttf");
		// writeMsg.setTypeface(typeFace);

		setCodeTable();

		writeMsg.setOnClickListener(new View.OnClickListener() {

			// @Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isUnicode) {

					ini_pos = writeMsg.getSelectionStart();
				}
			}
		});

		//
		writeMsg.setOnKeyListener(new View.OnKeyListener() {

			// @Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (isUnicode) {
					Log.d("KEY_CODE", " ->" + keyCode);
					if (keyCode == 62)// if key pressed is space
					{
						final_pos = writeMsg.getSelectionStart();
						temp = writeMsg.getText().toString()
								.substring(ini_pos, final_pos);
						// ini_pos = final_pos;
						Log.d("INI_POS", ".." + ini_pos);

						Log.d("INI_POS", ".." + writeMsg.getSelectionStart());
						check(temp);
					} else if (keyCode == 67 || keyCode == 21 || keyCode == 22) {
						ini_pos = writeMsg.getSelectionStart();
					}
					// else if((ucValue>=48 && ucValue<=57)||(ucValue>=97 &&
					// ucValue<=122)||(ucValue>=65 && ucValue<=90))
					// {
					// key=String.fromCharCode(ucValue);
					// word+=key;
					// }
					else {

					}
					return false;// return true if you want to escape the key
									// pressed
				}
				return false;
			}

			public boolean check(String temp) {
				// TODO Auto-generated method stub

				Log.d("SubString Temp -:", "|" + temp + "|");
				String subtemp = "";
				int size = temp.length();
				int i_pos = 0, f_pos = 1;
				string = writeMsg.getText().toString();
				String unicode = "";
				String strtemp = string;
				// for ( i = ini_pos; i<final_pos; )
				// for ( ; i_pos <size ;)
				while (i_pos < size) {
					// try {
					// subtemp = temp.substring(i_pos, f_pos);
					// String unicode = codeTable.get(subtemp);
					// if ( unicode != null )
					{
						int ii_pos = i_pos;
						int ff_pos = f_pos;
						if (i_pos >= size)
							break;
						do {
							Log.d("start end length", "i_pos" + i_pos
									+ " f_pos " + f_pos + "  size" + size
									+ "    ff_pos" + ff_pos);
							subtemp = temp.substring(ii_pos, ff_pos);
							unicode = codeTable.get(subtemp);
							Log.d("SubString subTemp ---:", "|" + subtemp + "|");

							if (unicode != null) {

								strtemp = string.replaceAll(subtemp, unicode);
								f_pos = ff_pos;

								Log.d("SubString subTemp Match found:", "|"
										+ unicode + "|");
							}
							// string =
							// myEdittext.getText().toString().replaceAll("a",
							// "\u0905");

							// myEdittext.setTypeface(typeFace);
						} while (ff_pos++ < size);
						string = strtemp;
						Log.d("out of do loop", "i_pos" + i_pos + " f_pos "
								+ f_pos + "  size" + size + "    ff_pos"
								+ ff_pos);
						i_pos = f_pos;
						f_pos++;

						Log.d("UniCODE for subtemp", subtemp);
						Log.d("UniCODE NOT NULL", "   " + unicode);

					}
					// else
					// {
					// f_pos++;
					//
					// }
					if (f_pos > size) {

						Log.d("before break", "i_pos" + i_pos + " f_pos "
								+ f_pos + "  size" + size);
						break;

					}

					// } catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					// break;
					// }
				}
				;

				Log.d("start end length", "i_pos" + i_pos + " f_pos " + f_pos
						+ "  size" + size);
				if (!string.equals("")) {
					Log.d("strring is no empty",
							"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
					writeMsg.setText(string);
					writeMsg.setTypeface(typeFace);
					int cur_pos = (int) writeMsg.getText().length();
					Log.d("cur_pos", "  ->" + cur_pos);
					writeMsg.setSelection(cur_pos);

					Log.d("strring is no empty", "cusros changed");
				}
				// ini_pos = final_pos;
				return false;
			}

		});

		sendButton.setOnClickListener(this);
		// writeMsg.setOnKeyListener(this);
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dbClass = new DbClass(this);
		aChatApp = (MyaChatApplication) getApplication();
		xmppManager = aChatApp.getXmppManager();
		connection = xmppManager.returnConnection();
		username = (xmppManager.getUserInfo())[0];// "milans";

		// secondUser =
		// getIntent().getExtras().getString("username").toString();
		Log.d(TAG, "loggedin user:" + username + " seconduser:" + secondUser);

		// for receiving message form other user
		if (connection != null) {
			Log.d(TAG, "before cursor");
			// Intent sender = getIntent();
			Log.d(TAG, "the got value is:" + secondUser);
			// String strings[] = new String[1];
			// strings[0]= string;
			// string.
			// print messages from database
			cursor = dbClass.query(username, secondUser);
			Log.d(TAG, "after cursor" + cursor.getCount());
			final int typeColumnIndex = cursor.getColumnIndex(DbHelper.A_TYPE);
			final int secondUserColumnIndex = cursor
					.getColumnIndex(DbHelper.A_SECOND_USER);
			final int dataColumnIndex = cursor.getColumnIndex(DbHelper.A_MSG);
			// userColumnIndex = cursor.getColumnIndex("user");
			String secondUser;
			String text;
			String type;
			while (cursor.moveToNext()) {
				secondUser = cursor.getString(secondUserColumnIndex);
				type = cursor.getString(typeColumnIndex);
				text = cursor.getString(dataColumnIndex);
				text = parseEmo(text);
				if (type.equals("send")) {
					mSpannes.add(Html.fromHtml("<font color='red'><b>"
							+ username + ":</b></font><br/>" + "\t" + text
							+ "<br/>", new SmajlGetter(getResources()), null));
				} else {
					mSpannes.add(Html.fromHtml("<font color='blue'><b>"
							+ secondUser + ":</b></font><br/>" + "\t" + text
							+ "<br/>", new SmajlGetter(getResources()), null));
				}
			}
			setListAdapter();
			//
			// filter = new MessageTypeFilter(Message.Type.chat);
			//
			// // Add a packet listener to get messages sent to us
			// connection.addPacketListener(new PacketListener() {
			// public void processPacket(Packet packet) {
			// Message message = (Message) packet;
			// String msgFrm = message.getFrom();
			// String msgRec = message.getBody();
			// msgRec = parseEmo(msgRec);
			// if (msgRec.contentEquals("")) {
			// Toast.makeText(Chats.this, R.string.tChatHint,
			// Toast.LENGTH_LONG).show();
			// } else {
			//
			// Log.d(TAG, "before sending append ");
			// //
			// // mSpannes.add(Html.fromHtml("<font color='red'><b>"
			// // + msgFrm + ":</b></font><br/>" + "\t" + msgRec
			// // + "<br/>", new SmajlGetter(getResources()),
			// // null));
			// // handler.post(new Runnable() {
			// // public void run() {
			// // setListAdapter();
			// // }
			// // });
			//
			// Log.d(TAG, "after sending append");
			// }
			//
			// }
			// }, filter);
			//
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onresume");

		if (newMsgReceiver == null) {
			newMsgReceiver = new NewMsgReceiver();
		}
		// receiver to receive broadcast
		registerReceiver(newMsgReceiver, new IntentFilter(
				ChatService.CHATMSG_BFILTER));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onpause");
		unregisterReceiver(newMsgReceiver);
		// cursor.close();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onstop");
		// close the activity
		// finish();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG, "onrestart");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "ondestroy");
	}

	public void onClick(View v) {

		sendMsg();
	}

	public void sendMsg() {
		try {

			msg = writeMsg.getText().toString();
			// String string = "\u0915";
			// Log.d(TAG, string);
			writeMsg.setText(null);
			if (msg.contentEquals("")) {
				Toast.makeText(Chats.this, R.string.tChatHint,
						Toast.LENGTH_LONG).show();

			} else {

				xmppManager.sendMessage(msg, secondUser);
				// get current date time with Date()
				date = new Date();
				// create message to be written to dabatase
				contentValues = new ContentValues();
				contentValues.clear();
				contentValues.put(DbHelper.A_CREATED_AT,
						dateFormat.format(date));
				contentValues.put(DbHelper.A_LOGGEDIN_USER, username);
				contentValues.put(DbHelper.A_SECOND_USER, secondUser);
				contentValues.put(DbHelper.A_MSG, msg);
				contentValues.put(DbHelper.A_TYPE, "send");
				// insert into database
				dbClass.insertOrIgnore(contentValues);
				msg = parseEmo(msg);
				mSpannes.add(Html.fromHtml("<font color='red'><b>" + username
						+ ":</b></font><br/>" + "\t" + msg + "<br/>",
						new SmajlGetter(getResources()), null));
				// mSpannes.add(Html.fromHtml(string));
				setListAdapter();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chatmenu, menu);
		return true;
	}

	// Menu stuff
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case R.id.itemPrefs:
		// startActivity(new Intent(this, LoginPreferencesActivity.class));
		// break;

		case R.id.nepMsgType:
			isUnicode = true;
			Toast.makeText(this, R.string.nepaliMsg, Toast.LENGTH_LONG).show();
			break;
		case R.id.engMsgType:
			isUnicode = false;
			Toast.makeText(this, R.string.simpleMsg, Toast.LENGTH_LONG).show();
			break;
		case R.id.deleteMessage:
			dbClass.delete();
			Toast.makeText(this, R.string.deleteMessageNotification,
					Toast.LENGTH_LONG).show();
			break;

		}
		return true;
	}

	/**
	 * function to convert send/received message to equivalent HTML form for
	 * retrieving image
	 * 
	 * @param message
	 * @return
	 */
	public String parseEmo(String message) {
		// HTML musime zlikvidovat, nevime, co prislo...
		// message = message.replaceAll("&","&amp;");
		// message = message.replaceAll("\"", "&quot;");
		// message = message.replaceAll("'", "&apos;");

		message = message.replaceAll(">", "&gt;");
		message = message.replaceAll("<", "&lt;");
		// konce radek nahradime za <br/>
		message = message.replaceAll("\n", "<br/>");
		// clickable urls
		message = message.replaceAll("(?:ht|f)tp://\\S+(?<![!.?])",
				"<a href=\"$0\">$0</a>");
		// smajl test
		message = message.replaceAll("&gt;:-\\)", "<img src=\"smile_devil\">");
		message = message.replaceAll("&gt;:\\)", "<img src=\"smile_devil\">");
		message = message.replaceAll(":-\\)", "<img src=\"smile_smile\">");
		message = message.replaceAll(":\\)", "<img src=\"smile_smile\">");
		// message=message.replaceAll(":)","<img src=\"smile_smile\">");
		message = message.replaceAll(";-\\)", "<img src=\"smile_wink\">");
		message = message.replaceAll(";\\)", "<img src=\"smile_wink\">");
		message = message.replaceAll(":-\\(", "<img src=\"smile_unhappy\">");
		message = message.replaceAll(":\\(", "<img src=\"smile_unhappy\">");
		message = message.replaceAll("\\]:->", "<img src=\"smile_devil\">");
		message = message
				.replaceAll("B-\\)", "<img src=\"smile_coolglasses\">");
		message = message
				.replaceAll("8-\\)", "<img src=\"smile_coolglasses\">");
		message = message.replaceAll(":-D", "<img src=\"smile_biggrin\">");
		message = message.replaceAll(":-&gt;", "<img src=\"smile_biggrin\">");
		message = message.replaceAll(":D", "<img src=\"smile_biggrin\">");
		message = message.replaceAll(":&gt;", "<img src=\"smile_biggrin\">");
		message = message.replaceAll("xD", "<img src=\"smile_biggrin\">");
		message = message.replaceAll(":'-\\(", "<img src=\"smile_cry\">");
		message = message.replaceAll(":'\\(", "<img src=\"smile_cry\">");
		message = message.replaceAll(";-\\(", "<img src=\"smile_cry\">");
		message = message.replaceAll(";\\(", "<img src=\"smile_cry\">");
		message = message.replaceAll(":-/", "<img src=\"smile_frowning\">");
		// message=message.replaceAll(":/","<img src=\"smile_frowning\">");
		message = message.replaceAll(":-S", "<img src=\"smile_frowning\">");
		message = message.replaceAll(":S", "<img src=\"smile_frowning\">");
		message = message.replaceAll(":-\\$", "<img src=\"smile_blush\">");
		message = message.replaceAll(":\\$", "<img src=\"smile_blush\">");
		message = message.replaceAll(":-@", "<img src=\"smile_angry\">");
		message = message.replaceAll(":@", "<img src=\"smile_angry\">");
		message = message.replaceAll(":-\\[", "<img src=\"smile_bat\">");
		message = message.replaceAll(":\\[", "<img src=\"smile_bat\">");
		message = message.replaceAll(":-\\*", "<img src=\"smile_kiss\">");
		message = message.replaceAll(":\\*", "<img src=\"smile_kiss\">");
		message = message.replaceAll(":-P", "<img src=\"smile_tongue\">");
		message = message.replaceAll(":P", "<img src=\"smile_tongue\">");
		message = message.replaceAll(":-p", "<img src=\"smile_tongue\">");
		message = message.replaceAll(":p", "<img src=\"smile_tongue\">");
		message = message.replaceAll(":-O", "<img src=\"smile_oh\">");
		message = message.replaceAll(":O", "<img src=\"smile_oh\">");
		message = message.replaceAll("&lt;3", "<img src=\"smile_heart\">");
		// MSN smajliky
		message = message.replaceAll("\\(@\\)", "<img src=\"smile_pussy\">");
		message = message.replaceAll("\\(%\\)", "<img src=\"smile_cuffs\">");
		message = message.replaceAll("\\(S\\)", "<img src=\"smile_moon\">");
		message = message.replaceAll("\\(I\\)", "<img src=\"smile_lamp\">");
		message = message.replaceAll("\\(8\\)", "<img src=\"smile_music\">");
		message = message.replaceAll("\\(B\\)", "<img src=\"smile_beer\">");
		message = message.replaceAll("\\(L\\)", "<img src=\"smile_heart\">");
		message = message.replaceAll("\\(6\\)", "<img src=\"smile_devil\">");
		message = message.replaceAll("\\(W\\)", "<img src=\"smile_brflower\">");
		message = message.replaceAll("\\(Z\\)", "<img src=\"smile_boy\">");
		message = message.replaceAll("\\(X\\)", "<img src=\"smile_girl\">");
		message = message.replaceAll("\\(E\\)", "<img src=\"smile_mail\">");
		message = message
				.replaceAll("\\(N\\)", "<img src=\"smile_thumbdown\">");
		message = message.replaceAll("\\(P\\)", "<img src=\"smile_photo\">");
		message = message.replaceAll("\\(K\\)", "<img src=\"smile_kiss\">");
		message = message.replaceAll("\\(Y\\)", "<img src=\"smile_thumbup\">");
		message = message
				.replaceAll("\\(\\}\\)", "<img src=\"smile_hugleft\">");
		message = message.replaceAll("\\(U\\)", "<img src=\"smile_brheart\">");
		message = message.replaceAll("\\(F\\)", "<img src=\"smile_flower\">");
		message = message.replaceAll("\\(H\\)",
				"<img src=\"smile_coolglasses\">");
		message = message.replaceAll("\\(D\\)", "<img src=\"smile_drink\">");
		message = message.replaceAll("\\(T\\)", "<img src=\"smile_phone\">");
		message = message.replaceAll("\\(C\\)", "<img src=\"smile_coffee\">");
		message = message.replaceAll("\\(\\{\\)",
				"<img src=\"smile_hugright\">");
		message = message.replaceAll("\\(\\*\\)", "<img src=\"smile_star\">");
		message = message.replaceAll("\\(R\\)", "<img src=\"smile_rainbow\">");
		// message = message.replaceAll(":)", "<img src=\"smile_smile\">");
		// message = message.replaceAll(":P","<img src=\"smile_tongue\">");
		// vratime sracku :-D
		return message;
	}

	public void setCodeTable() {
		codeTable = new HashMap<String, String>();

		codeTable.put("a", "\u0905");
		codeTable.put("aa", "\u0906");
		codeTable.put("i", "\u0907");
		codeTable.put("ii", "\u0908");
		codeTable.put("u", "\u0909");
		codeTable.put("e", "\u090F");
		codeTable.put("ai", "\u0910");
		codeTable.put("au", "\u0914");
		codeTable.put("c", "\u0938" + "\u0940");
		codeTable.put("q", "\u0915" + "\u094D" + "\u092F" + "\u0941");
		codeTable.put("x", "\u090F" + "\u0915" + "\u094D" + "\u0938");
		codeTable.put("w", "\u0935");
		codeTable.put("z", "\u091C" + "\u0947" + "\u0921");
		codeTable.put("k", "\u0915" + "\u094D");
		codeTable.put("kh", "\u0916" + "\u094D");
		codeTable.put("g", "\u0917" + "\u094D");
		codeTable.put("gh", "\u0919" + "\u094D");
		codeTable.put("ng", "\u0915" + "\u094D");
		codeTable.put("ch", "\u091A" + "\u094D");
		codeTable.put("chh", "\u091B" + "\u094D");
		codeTable.put("j", "\u091C" + "\u094D");
		codeTable.put("jh", "\u091D" + "\u094D");
		codeTable.put("T", "\u091F" + "\u094D");
		codeTable.put("Th", "\u0920" + "\u094D");
		codeTable.put("Dh", "\u0922" + "\u094D");
		codeTable.put("t", "\u0924" + "\u094D");
		codeTable.put("th", "\u0925" + "\u094D");
		codeTable.put("d", "\u0926" + "\u094D");
		codeTable.put("dh", "\u0927" + "\u094D");
		codeTable.put("n", "\u0928" + "\u094D");
		codeTable.put("p", "\u092A" + "\u094D");
		codeTable.put("ph", "\u092B" + "\u094D");
		codeTable.put("f", "\u092B" + "\u094D");
		codeTable.put("b", "\u092C" + "\u094D");
		codeTable.put("bh", "\u092D" + "\u094D");
		codeTable.put("m", "\u092E" + "\u094D");
		codeTable.put("y", "\u092F" + "\u094D");
		codeTable.put("r", "\u0930" + "\u094D");
		codeTable.put("l", "\u0932" + "\u094D");
		codeTable.put("v", "\u0935" + "\u094D");
		codeTable.put("sh", "\u0936" + "\u094D");
		codeTable.put("s", "\u0938" + "\u094D");
		codeTable.put("h", "\u0939" + "\u094D");

		codeTable.put("ka", "\u0915");
		codeTable.put("kha", "\u0916");
		codeTable.put("ga", "\u0917");
		codeTable.put("gha", "\u0918");
		codeTable.put("nga", "\u0919");
		codeTable.put("cha", "\u091A");
		codeTable.put("chha", "\u091B");
		codeTable.put("ja", "\u091C");
		codeTable.put("jha", "\u091D");
		codeTable.put("Ta", "\u091F");
		codeTable.put("Tha", "\u0920");
		codeTable.put("Da", "\u0921");
		codeTable.put("Dha", "\u0922");
		codeTable.put("ta", "\u0924");
		codeTable.put("tha", "\u0925");
		codeTable.put("da", "\u0926");
		codeTable.put("dha", "\u0927");
		codeTable.put("na", "\u0928");
		codeTable.put("pa", "\u092A");
		codeTable.put("pha", "\u092B");
		codeTable.put("fa", "\u092B");
		codeTable.put("ba", "\u092C");
		codeTable.put("bha", "\u092D");
		codeTable.put("ma", "\u092E");
		codeTable.put("ya", "\u092F");
		codeTable.put("ra", "\u0930");
		codeTable.put("la", "\u0932");
		codeTable.put("va", "\u0935");
		codeTable.put("sha", "\u0936");
		codeTable.put("sa", "\u0938");
		codeTable.put("ha", "\u0939");

		codeTable.put("kaa", "\u0915" + "\u093E");
		codeTable.put("khaa", "\u0916" + "\u093E");
		codeTable.put("gaa", "\u0917" + "\u093E");
		codeTable.put("ghaa", "\u0918" + "\u093E");
		codeTable.put("ngaa", "\u0919" + "\u093E");
		codeTable.put("chaa", "\u091A" + "\u093E");
		codeTable.put("chhaa", "\u091B" + "\u093E");
		codeTable.put("jaa", "\u091C" + "\u093E");
		codeTable.put("jhaa", "\u091D" + "\u093E");
		codeTable.put("Taa", "\u091F" + "\u093E");
		codeTable.put("Thaa", "\u0920" + "\u093E");
		codeTable.put("Daa", "\u0921" + "\u093E");
		codeTable.put("Dhaa", "\u0922" + "\u093E");
		codeTable.put("taa", "\u0924" + "\u093E");
		codeTable.put("thaa", "\u0925" + "\u093E");
		codeTable.put("daa", "\u0926" + "\u093E");
		codeTable.put("dhaa", "\u0927" + "\u093E");
		codeTable.put("naa", "\u0928" + "\u093E");
		codeTable.put("paa", "\u092A" + "\u093E");
		codeTable.put("phaa", "\u092B" + "\u093E");
		codeTable.put("faa", "\u092B" + "\u093E");
		codeTable.put("baa", "\u092C" + "\u093E");
		codeTable.put("bhaa", "\u092D" + "\u093E");
		codeTable.put("maa", "\u092E" + "\u093E");
		codeTable.put("yaa", "\u092F" + "\u093E");
		codeTable.put("raa", "\u0930" + "\u093E");
		codeTable.put("laa", "\u0932" + "\u093E");
		codeTable.put("vaa", "\u0936" + "\u093E");
		codeTable.put("shaa", "\u0919" + "\u093E");
		codeTable.put("chaa", "\u091A" + "\u093E");
		codeTable.put("chhaa", "\u091B" + "\u093E");
		codeTable.put("saa", "\u0938" + "\u093E");
		codeTable.put("haa", "\u0939" + "\u093E");

		codeTable.put("ki", "\u093F" + "\u0915");
		codeTable.put("kii", "\u0915" + "\u0940");
		codeTable.put("khi", "\u093F" + "\u0916");
		codeTable.put("gi", "\u093F" + "\u0917");
		codeTable.put("ghi", "\u093F" + "\u0918");
		codeTable.put("ngi", "\u093F" + "\u0919");
		codeTable.put("chi", "\u093F" + "\u091A");
		codeTable.put("chhi", "\u093F" + "\u091B");
		codeTable.put("ji", "\u093F" + "\u091C");
		codeTable.put("jhi", "\u093F" + "\u091D");
		codeTable.put("Ti", "\u093F" + "\u091F");
		codeTable.put("Thi", "\u093F" + "\u0920");
		codeTable.put("Di", "\u093F" + "\u0921");
		codeTable.put("Dhi", "\u093F" + "\u0922");
		codeTable.put("ti", "\u093F" + "\u0924");
		codeTable.put("thi", "\u093F" + "\u0925");
		codeTable.put("di", "\u093F" + "\u0926");
		codeTable.put("dhi", "\u093F" + "\u0927");
		codeTable.put("ni", "\u093F" + "\u0928");
		codeTable.put("pi", "\u093F" + "\u092A");
		codeTable.put("phi", "\u093F" + "\u092B");
		codeTable.put("fi", "\u093F" + "\u092B");
		codeTable.put("bi", "\u093F" + "u092C");
		codeTable.put("bhi", "\u093F" + "\u092D");
		codeTable.put("mi", "\u093F" + "\u092E");
		codeTable.put("yi", "\u093F" + "\u092F");
		codeTable.put("ri", "\u093F" + "\u0930");
		codeTable.put("li", "\u093F" + "\u0932");
		codeTable.put("vi", "\u093F" + "\u0935");
		codeTable.put("shi", "\u093F" + "\u0936");
		codeTable.put("si", "\u093F" + "\u0938");
		codeTable.put("hi", "\u093F" + "\u0939");

		codeTable.put("ku", "\u0915" + "\u0941");
		codeTable.put("khu", "\u0916" + "\u0941");
		codeTable.put("gu", "\u0917" + "\u0941");
		codeTable.put("ghu", "\u0918" + "\u0941");
		codeTable.put("ngu", "\u0919" + "\u0941");
		codeTable.put("chu", "\u091A" + "\u0941");
		codeTable.put("chhu", "\u091B" + "\u0941");
		codeTable.put("ju", "\u091C" + "\u093F");
		codeTable.put("jhu", "\u091D" + "\u0941");
		codeTable.put("Tu", "\u091F" + "\u0941");
		codeTable.put("Thu", "\u0920" + "\u0941");
		codeTable.put("Du", "\u0921" + "\u0941");
		codeTable.put("Dhu", "\u0922" + "\u0941");
		codeTable.put("tu", "\u0924" + "u0941");
		codeTable.put("thu", "\u0925" + "\u0941");
		codeTable.put("du", "\u0926" + "\u0941");
		codeTable.put("dhu", "\u0927" + "\u0941");
		codeTable.put("nu", "\u0928" + "\u0941");
		codeTable.put("pu", "\u092A" + "\u0941");
		codeTable.put("phu", "\u0935" + "\u0941");
		codeTable.put("bu", "\u092C" + "\u0941");
		codeTable.put("bhu", "\u092D" + "\u0941");
		codeTable.put("mu", "\u092E" + "\u0941");
		codeTable.put("yu", "\u092F" + "\u0941");
		codeTable.put("ru", "\u0930" + "u0941");
		codeTable.put("lu", "\u0932" + "\u0941");
		codeTable.put("vu", "\u0935" + "\u0941");
		codeTable.put("shu", "\u0936" + "\u0941");
		codeTable.put("su", "\u0938" + "\u0941");
		codeTable.put("hu", "\u0939" + "\u0941");

		codeTable.put("ke", "\u0915" + "\u0947");
		codeTable.put("khe", "\u0916" + "\u0947");
		codeTable.put("ge", "\u0917" + "\u0947");
		codeTable.put("ghe", "\u0918" + "\u0947");
		codeTable.put("nge", "\u0919" + "\u0947");
		codeTable.put("che", "\u091A" + "\u0947");
		codeTable.put("chhe", "\u091B" + "\u0947");
		codeTable.put("je", "\u091C" + "\u0947");
		codeTable.put("jhe", "\u091D" + "\u0947");
		codeTable.put("Te", "\u091F" + "\u0947");
		codeTable.put("The", "\u0920" + "\u0947");
		codeTable.put("De", "\u0921" + "\u0947");
		codeTable.put("Dhe", "\u0922" + "\u0947");
		codeTable.put("te", "\u0924" + "\u0947");
		codeTable.put("the", "\u0925" + "\u0947");
		codeTable.put("de", "\u0926" + "\u0947");
		codeTable.put("dhe", "\u0927" + "\u0947");
		codeTable.put("ne", "\u0928" + "\u0947");
		codeTable.put("pe", "\u092A" + "\u0947");
		codeTable.put("phe", "\u092B" + "\u0947");
		codeTable.put("fe", "\u092B" + "\u0947");
		codeTable.put("be", "\u092C" + "\u0947");
		codeTable.put("bhe", "\u092D" + "\u0947");
		codeTable.put("me", "\u092E" + "\u0947");
		codeTable.put("ye", "\u092F");
		codeTable.put("re", "\u0930" + "\u0947");
		codeTable.put("le", "\u0932" + "\u0947");
		codeTable.put("ve", "\u0935" + "\u0947");
		codeTable.put("she", "\u0936" + "\u0947");
		codeTable.put("he", "\u0939" + "\u0947");

		codeTable.put("kai", "\u0915" + "\u0948");
		codeTable.put("khai", "\u0916" + "\u0948");
		codeTable.put("gai", "\u0917" + "\u0948");
		codeTable.put("ghai", "\u0918" + "\u0948");
		codeTable.put("chai", "\u091A" + "\u0948");
		codeTable.put("chhai", "\u091B" + "\u0948");
		codeTable.put("jai", "\u091C" + "\u0948");
		codeTable.put("jhai", "\u091D" + "\u0948");
		codeTable.put("Tai", "\u091F" + "\u0948");
		codeTable.put("Thai", "\u0920" + "\u0948");
		codeTable.put("Dai", "\u0921" + "\u0948");
		codeTable.put("Dhai", "\u0922" + "\u0948");
		codeTable.put("tai", "\u0924" + "\u0948");
		codeTable.put("thai", "\u0925" + "\u0948");
		codeTable.put("dai", "\u0926" + "\u0948");
		codeTable.put("dhai", "\u0927" + "\u0948");
		codeTable.put("nai", "\u0928" + "\u0948");
		codeTable.put("pai", "\u092A" + "\u0948");
		codeTable.put("phai", "\u092B" + "\u0948");
		codeTable.put("fai", "\u092B" + "\u0948");
		codeTable.put("bai", "\u092C" + "\u0948");
		codeTable.put("bhai", "\u092D" + "\u0948");
		codeTable.put("mai", "\u092E" + "\u0948");
		codeTable.put("yai", "\u092F" + "\u0948");
		codeTable.put("rai", "\u0930" + "\u0948");
		codeTable.put("lai", "\u0932" + "\u0948");
		codeTable.put("vai", "\u0935" + "\u0948");
		codeTable.put("shai", "\u0936" + "\u0948");
		codeTable.put("sai", "\u0938" + "\u0948");
		codeTable.put("hai", "\u0939" + "\u0948");

		codeTable.put("ko", "\u0915" + "\u094B");
		codeTable.put("kho", "\u0916" + "\u094B");
		codeTable.put("go", "\u0917" + "\u094B");
		codeTable.put("gho", "\u0918" + "\u094B");
		codeTable.put("cho", "\u091A" + "\u094B");
		codeTable.put("chho", "\u091B" + "\u094B");
		codeTable.put("jo", "\u091C" + "\u094B");
		codeTable.put("jho", "\u091D" + "\u094B");
		codeTable.put("To", "\u091F" + "\u094B");
		codeTable.put("Tho", "\u0920" + "\u094B");
		codeTable.put("Do", "\u0921" + "\u094B");
		codeTable.put("Dho", "\u0922" + "\u094B");
		codeTable.put("to", "\u0924" + "\u094B");
		codeTable.put("tho", "\u0925" + "\u094B");
		codeTable.put("do", "\u0926" + "\u094B");
		codeTable.put("dho", "\u0927" + "\u094B");
		codeTable.put("no", "\u0928" + "\u094B");
		codeTable.put("po", "\u092A" + "\u094B");
		codeTable.put("pho", "\u092B" + "\u094B");
		codeTable.put("fo", "\u092B" + "\u094B");
		codeTable.put("bo", "\u092C" + "\u094B");
		codeTable.put("bho", "\u092D" + "\u094B");
		codeTable.put("mo", "\u092E" + "\u094B");
		codeTable.put("yo", "\u092F" + "\u094B");
		codeTable.put("ro", "\u0930" + "\u094B");
		codeTable.put("lo", "\u0932" + "\u094B");
		codeTable.put("wo", "\u0935" + "\u094B");
		codeTable.put("sho", "\u0936" + "\u094B");
		codeTable.put("so", "\u0938" + "\u094B");
		codeTable.put("ho", "\u0939" + "\u094B");

		codeTable.put("kau", "\u0915" + "\u094C");
		codeTable.put("khau", "\u0916" + "\u094C");
		codeTable.put("gau", "\u0917" + "\u094C");
		codeTable.put("ghau", "\u0918" + "\u094C");
		codeTable.put("chau", "\u091A" + "\u094C");
		codeTable.put("chhau", "\u091B" + "\u094C");
		codeTable.put("jau", "\u091C" + "\u094C");
		codeTable.put("jhau", "\u091D" + "\u094C");
		codeTable.put("Tau", "\u091F" + "\u094C");
		codeTable.put("Thau", "\u0920" + "\u094C");
		codeTable.put("Dau", "\u0921" + "\u094C");
		codeTable.put("Dhau", "\u0922" + "\u094C");
		codeTable.put("tau", "\u0924" + "\u094C");
		codeTable.put("thau", "\u0925" + "\u094C");
		codeTable.put("dau", "\u0926" + "\u094C");
		codeTable.put("dhau", "\u0927" + "\u094C");
		codeTable.put("nau", "\u0928" + "\u094C");
		codeTable.put("pau", "\u092A" + "\u094C");
		codeTable.put("phau", "\u092B" + "\u094C");
		codeTable.put("fau", "\u092B" + "\u094C");
		codeTable.put("bau", "\u092C" + "\u094C");
		codeTable.put("bhau", "\u092D" + "\u094C");
		codeTable.put("mau", "\u092E" + "\u094C");
		codeTable.put("yau", "\u092F" + "\u094C");
		codeTable.put("rau", "\u0930" + "\u094C");
		codeTable.put("lau", "\u0932" + "\u094C");
		codeTable.put("wau", "\u0935" + "\u094C");
		codeTable.put("shau", "\u0936" + "\u094C");
		codeTable.put("sau", "\u0938" + "\u094C");
		codeTable.put("hau", "\u0939" + "\u094C");

	}

	class SmajlGetter implements ImageGetter {
		private Resources r;

		public SmajlGetter(Resources res) {
			r = res;
		}

		public Drawable getDrawable(String arg0) {
			// R.drawable.
			// Drawable drawable = r.getDrawable(r.getIdentifier(arg0, null,
			// null));
			Drawable d = r.getDrawable(r.getIdentifier(arg0, "drawable",
					"com.aChat"));
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			// BitmapDrawable b = new BitmapDrawable(r);
			// b.createFromPath(arg0);
			return d;
			// return new BitmapDrawable(arg0);
		}
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				sendMsg();
				return true;
			}
		}
		return false;
	}

	/**
	 * class to receive broadcast about new message notification for chats class
	 * 
	 * @author Rabin
	 * 
	 */
	class NewMsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "newmsgreceiver broadcast message received");
			String user1 = intent.getExtras()
					.getString(ChatService.CHATMSG_BFILTER_MSGFRM).toString();
			Log.d(TAG, "new msg from:" + user1);
			if (secondUser.equals(user1)) {
				// do something here
				mSpannes.clear();
				// print messages from database
				cursor = dbClass.query(username, secondUser);
				Log.d(TAG, "after cursor" + cursor.getCount());
				final int typeColumnIndex = cursor
						.getColumnIndex(DbHelper.A_TYPE);
				final int secondUserColumnIndex = cursor
						.getColumnIndex(DbHelper.A_SECOND_USER);
				final int dataColumnIndex = cursor
						.getColumnIndex(DbHelper.A_MSG);
				// userColumnIndex = cursor.getColumnIndex("user");
				String secondUser;
				String text;
				String type;
				while (cursor.moveToNext()) {
					secondUser = cursor.getString(secondUserColumnIndex);
					type = cursor.getString(typeColumnIndex);
					text = cursor.getString(dataColumnIndex);
					text = parseEmo(text);
					if (type.equals("send")) {
						mSpannes.add(Html.fromHtml("<font color='red'><b>"
								+ username + ":</b></font><br/>" + "\t" + text
								+ "<br/>", new SmajlGetter(getResources()),
								null));
					} else {
						mSpannes.add(Html.fromHtml("<font color='blue'><b>"
								+ secondUser + ":</b></font><br/>" + "\t"
								+ text + "<br/>", new SmajlGetter(
								getResources()), null));
					}
				}
				setListAdapter();
			} else {
				Log.d(TAG, "the intent and broadcast users not same");
				Log.d(TAG, "Intent user:" + secondUser + "Broadcast user:"
						+ user1);
			}

		}

	}

}
