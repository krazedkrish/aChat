//package com.aChat;
//
//import java.util.Collection;
//
//import org.jivesoftware.smack.Chat;
//import org.jivesoftware.smack.ChatManager;
//import org.jivesoftware.smack.ChatManagerListener;
//import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
//import org.jivesoftware.smack.MessageListener;
//import org.jivesoftware.smack.Roster;
//import org.jivesoftware.smack.RosterEntry;
//import org.jivesoftware.smack.SmackConfiguration;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Presence;
//import org.jivesoftware.smack.packet.Presence.Type;
//
//import android.util.Log;
//
//public class XmppManager {
//
//	private static final int packetReplyTimeout = 500; // millis
//
//	private String server;
//	private int port;
//
//	private ConnectionConfiguration config;
//	private XMPPConnection connection;
//
//	private ChatManager chatManager;
//	private MessageListener messageListener;
//
//	public XmppManager(String server, int port) {
//		this.server = server;
//		this.port = port;
//	}
//
//	public int init() {
//		System.out.println(String.format(
//				"Initializing connection to server %1$s port %2$d", server,
//				port));
//		SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
//		//System.out.println("hello upto here is fine \n");
//		config = new ConnectionConfiguration(server, port);
//		config.setSecurityMode(SecurityMode.disabled);
//		connection = new XMPPConnection(config);
//		try {
//			connection.connect();
//			System.out.println("Connected: " + connection.isConnected());
//			chatManager = connection.getChatManager();
//			// connection.getChatManager();
//			connection.getChatManager().addChatListener(
//					new ChatManagerListener() {
//						public void chatCreated(final Chat chat,
//								final boolean createdLocally) {
//							chat.addMessageListener(new MessageListener() {
//								public void processMessage(Chat chat,
//										Message message) {
//									System.out.println("Received message: "
//											+ (message != null ? message
//													.getBody() : "is typing"));
//
//								}
//							});
//						}
//					});
//			messageListener = new MyMessageListener();
//			Log.i("init", "before return");
//			return 1;
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
//
//	}
//
//	public int performLogin(String username, String password) {
//		//chatManager = connection.getChatManager();
//		//messageListener = new MyMessageListener();
//		Log.i("perform login","ksdlksaj");
//		if (connection != null && connection.isConnected()) {
//			try {
//				connection.login(username, password);
//				Log.i("xmpp", "logged in as:" + connection.getUser());
//				return 1;
//			} catch (XMPPException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.i("xmpp", "Error logging in.");
//				return 0;
//			}
//		}
//		return 0;
//	}
//
//	public void setStatus(boolean available, String status) {
//
//		Presence.Type type = available ? Type.available : Type.unavailable;
//		Presence presence = new Presence(type);
//
//		presence.setStatus(status);
//		connection.sendPacket(presence);
//
//	}
//
//	public void destroy() {
//		if (connection != null && connection.isConnected()) {
//			connection.disconnect();
//		}
//	}
//
//	public void printRoster() throws Exception {
//		Roster roster = connection.getRoster();
//		Collection<RosterEntry> entries = roster.getEntries();
//		for (RosterEntry entry : entries) {
//			System.out.println(String.format("Buddy:%1$s - Status:%2$s",
//					entry.getName(), entry.getStatus()));
//		}
//	}
//
//	public void sendMessage(String message, String buddyJID)
//			throws XMPPException {
//		System.out.println(String.format("Sending mesage '%1$s' to user %2$s",
//				message, buddyJID));
//		Chat chat = chatManager.createChat(buddyJID, messageListener);
//		chat.sendMessage(message);
//	}
//
//	public void createEntry(String user, String name) throws Exception {
//		System.out.println(String.format(
//				"Creating entry for buddy '%1$s' with name %2$s", user, name));
//		Roster roster = connection.getRoster();
//		roster.createEntry(user, name, null);
//	}
//
//	class MyMessageListener implements MessageListener {
//
//		// @Override
//		public void processMessage(Chat chat, Message message) {
//			String from = message.getFrom();
//			String body = message.getBody();
//			System.out.println(String.format(
//					"Received message '%1$s' from %2$s", body, from));
//		}
//
//	}
//
//}

package com.aChat;

//package com.javacodegeeks.xmpp;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class XmppManager implements Parcelable {

	private static final String TAG = XmppManager.class.getSimpleName();
	private String server;
	private int port;
	static String passWord;

	private ConnectionConfiguration config;
	private XMPPConnection connection;

	private ChatManager chatManager;
	private MessageListener messageListener;
	// private Presence.Type type;
	private Presence presence;
	private boolean available;
	private String service;
	// MyMessageListener myListener=new MyMessageListener();
	private Message msg;

	/**
	 * XmppManager Consturcter
	 * 
	 * @param server
	 * @param port
	 */
	public XmppManager(String server, int port) {
		this.server = server;
		this.port = port;

	}

	/**
	 * Initialize Xmpp connection Object with config=server,port) true return
	 * huncha if conxn is initialized
	 * 
	 * @return boolean
	 */
	public boolean init() {

		config = new ConnectionConfiguration(server, port);
		connection = new XMPPConnection(config);

		try {
			connection.connect();
			Log.d(TAG, "connected to:" + connection.getHost());
			return true;
		} catch (XMPPException e) {

			e.printStackTrace();
			Log.d(TAG, "error connecting");
			return false;
		}

	}

	/**
	 * Get XmppConnction
	 * 
	 * @return connection
	 */
	public XMPPConnection returnConnection() {
		return this.connection;
	}

	/**
	 * perform login with Username and Password So, if(connection is successful)
	 * true is returned )
	 * 
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean performLogin(String username, String password) {
		passWord = password;
		if (connection != null && connection.isConnected()) {
			try {
				connection.login(username, password);
				Log.d(TAG, "Logged in as: " + connection.getUser());

				return true;
			} catch (XMPPException e) {

				e.printStackTrace();
				Log.d(TAG, "Login Failure Error...");
				// return false;
			}
		}
		return false;

	}

	/**
	 * yaha bata we can Get uSers information form a string status[3]
	 * Username----status[0], Online/Offline ----status[1] and
	 * UserStatus------status[2]
	 * 
	 * @return string array: status
	 */
	public String[] getUserInfo() {

		String[] status = new String[3];
		if (connection != null && connection.isConnected()) {
			status[0] = connection.getUser();
			status[0] = status[0].substring(0, status[0].indexOf("/"));
			available = presence.isAvailable();

			if (available)
				status[1] = "online";
			else
				status[1] = "offline";

			status[2] = presence.getStatus();
		}
		return status;

	}

	/**
	 * we use it to set our Status : avalilabe or not
	 * 
	 * @param available
	 * @param status
	 */
	public void setStatus(boolean available, String status) {
		Presence.Type type = available ? Type.available : Type.unavailable;
		presence = new Presence(type);
		presence.setStatus(status);
		connection.sendPacket(presence);
	}

	public Roster getMeRoster() {
		return connection.getRoster();

	}

	public void destroy() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
		}
	}

	public void sendMessage(String message, String buddyJID)
			throws XMPPException {
		Log.d("sendmessage", "message send to:" + buddyJID);
		msg = new Message(buddyJID, Message.Type.chat);
		msg.setBody(message);
		// XML xml = new XML();
		// xml = msg.toXML();
		connection.sendPacket(msg);
	}

	public void createEntry(String user, String name) throws Exception {
		Roster roster = connection.getRoster();
		roster.createEntry(user, name, null);
	}

	// class MyMessageListener implements MessageListener {
	//
	// public void processMessage(Chat chat, Message message) {
	// // String[] myChatString;
	// // myChatString[0] = message.getFrom();
	// // myChatString[1] = message.getBody();
	//
	// System.out.println(String.format(
	// "Received message '%1$s' from %2$s",
	// message.getFrom(),message.getBody()));
	// // return myChatString;
	// }
	//
	// }

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(server);
		dest.writeInt(port);
		dest.writeValue(config);
		dest.writeValue(connection);
		dest.writeValue(chatManager);
		dest.writeValue(messageListener);
		dest.writeValue(presence);
		dest.writeValue(available);

	}

	public void printXmppManagerObjectData(Context context) {

		Log.d(TAG,
				String.format(
						"User is logged In into serverName: %s...@serverPort: %s...whose UserName: %s and Password: %s ",
						server, port, getUserInfo()[0], passWord));

	}

}
