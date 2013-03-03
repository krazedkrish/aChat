package com.aChat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aChat.DbClass.DbHelper;
import com.graphics.CoordinateArray;

public class ChatService extends Service {

	private static final String TAG = ChatService.class.getSimpleName();
	DbClass dbClass = new DbClass(this);
	private MsgListener msgListener;
	private Boolean isRunnning = false;
	public static final String CHATLIST_BFILTER = "NewChatList";
	public static final String CHATMSG_BFILTER = "NewChatMsg";
	public static final String CHATMSG_BFILTER_MSGFRM = "newChatMsgFrm";
	public static final String CHATMSG_BFILTER_MSGTXT = "newChatMsgTxt";

	private ArrayList<String> chatBuddyList = new ArrayList<String>();
	private JSONObject jsonObject;
	private XML jsonXml;
	CoordinateArray recCArray;
	public static final String ROSTER_CHG_BRODCAST_INTENT_FILTER = "RosterUpdated";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "on created");
		msgListener = new MsgListener();

		// /////////////////
		jsonXml = new XML();
		// ////////
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if (!this.isRunnning) {
			msgListener.start();
			Log.d(TAG, "on started");
			this.isRunnning = true;
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (this.isRunnning) {
			msgListener.stop();
			Log.d(TAG, "on destroyed");
			this.isRunnning = false;
		}

	}

	class MsgListener extends Thread {
		MyaChatApplication myaChatApplication;
		XmppManager xmppManager;
		XMPPConnection connection;
		PacketFilter filter;
		Date date;
		String msgRec;
		String msgFrm;
		ContentValues contentValues;
		SimpleDateFormat dateFormat;
		String username;

		public void msgDisplay(Message message) {

			// ///////////
			long rowId;
			msgFrm = message.getFrom();
			// msgFrm.indexOf("@");

			msgFrm = msgFrm.substring(0, msgFrm.indexOf("/"));

			// create message to be written to dabatase
			contentValues = new ContentValues();
			contentValues.clear();
			contentValues.put(DbHelper.A_CREATED_AT, dateFormat.format(date));
			contentValues.put(DbHelper.A_LOGGEDIN_USER, username);
			contentValues.put(DbHelper.A_SECOND_USER, msgFrm);
			contentValues.put(DbHelper.A_MSG, msgRec);
			contentValues.put(DbHelper.A_TYPE, "received");

			// insert into database
			rowId = dbClass.insertOrIgnore(contentValues);
			// if ((rowId = dbClass.insertOrIgnore(contentValues)) > 0) {

			Log.d(TAG, "new message rowid is:" + rowId);
			Log.d(TAG, "inserted into db");
			// so _id of newly inserted(received) message id
			// rowId
			// }
			// if (!chatBuddyList.contains(msgFrm)) {
			// chatBuddyList.add(msgFrm);
			// Intent intent = new Intent();
			// intent.setAction(CHATLIST_BFILTER);
			// // intent.putExtra("test_key", "test_value");
			// intent.putExtra("chatlist", msgFrm);
			// intent.putExtra("", )
			// // send broadcast to update chatlist with parameter
			// // msgFrm
			// sendBroadcast(intent);
			// send
			// sendBroadcast(new Intent(CHATLIST_BFILTER));
			// }

			Intent chatMsgIntent = new Intent();
			chatMsgIntent.setAction(CHATMSG_BFILTER);
			// intent.putExtra("test_key", "test_value");
			chatMsgIntent.putExtra(CHATMSG_BFILTER_MSGFRM, msgFrm);
			chatMsgIntent.putExtra(CHATMSG_BFILTER_MSGTXT, msgRec);
			// send broadcast to update current chat msg adapter on
			// chats activity
			sendBroadcast(chatMsgIntent);

			// broadcast notification for new message received
			// Intent newMessageIntent = new Intent();
			// newMessageIntent.setAction(action)

			Log.i("XMPPClient", "Got text [" + msgRec + "] from [" + msgFrm
					+ "]");

		}

		@Override
		public void run() {
			myaChatApplication = (MyaChatApplication) getApplication();
			xmppManager = myaChatApplication.getXmppManager();
			username = (xmppManager.getUserInfo())[0];// "milans";
			connection = xmppManager.returnConnection();
			filter = new MessageTypeFilter(Message.Type.chat);
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					// Log.d(TAG, "new any type message received");
					Message message = (Message) packet;

					// ///////////////////

					// String msgBody = ms
					// ////////////
					// receivedMsg = mess
					if (message.getBody() != null) {
						Log.d(TAG, "new any type message received");
						// get current date time with Date()
						date = new Date();
						msgRec = message.getBody();

						recCArray = new CoordinateArray();
						if (recCArray.setOjbectsFromXMLString(msgRec)) {
							Log.d(TAG, "coordinate array received" + msgRec);
							// ///// do nothing because coordinate array
							// received

						} else {
							Log.d(TAG, "message received:" + msgRec);
							msgDisplay(message);
						}

						// ///////////
						// try {
						// // / fingerpaint block if no error
						// Log.d(TAG, "message received:"+msgRec);
						// jsonObject = jsonXml.toJSONObject(msgRec);
						// if (jsonObject.get("Path") != null) {
						// Log.d(TAG,
						// "fingerpaint message with Path tag received");
						// }
						// Log.d(TAG, "so fingerpaint message received");
						//
						// } catch (JSONException e) {
						// // normal message block
						// // TODO Auto-generated catch block
						// Log.d(TAG, "normal message received");
						// e.printStackTrace();
						//
						// }

					}
				}
			}, filter);

		}

	}

}
