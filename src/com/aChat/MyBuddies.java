package com.aChat;

import java.util.Collection;
import java.util.Vector;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;

import com.aChat.ChatsList.ChatListReceiver;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBuddies extends ListActivity {
	private static String TAG = MyBuddies.class.getSimpleName();
	Collection<RosterEntry> entries;
	Roster roster;
	Presence presence;
	MyaChatApplication aChatApp;
	XmppManager xmppManager;
	RosterReceiver rosterReceiver;
	ListAdapter buddyAdapter;
	MyaChatApplication aChatapp;
	Vector<String> usernames;
	private RosterReceiver receiver;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// TextView textview = new TextView(this);
		// textview.setText("This is the MyBuddies tab");
		// setContentView(textview);

		usernames = new Vector<String>(0, 1); // initial size of list i.e,
												// vector is 0
		// and can be incremented by 1
		Log.i(TAG, "inside Mybuddies tab");
		if (receiver == null) {
			receiver = new RosterReceiver();
		}
		registerReceiver(receiver,
				new IntentFilter(ChatService.CHATMSG_BFILTER));

		// get achat app object and xmppManager Connection
		aChatApp = (MyaChatApplication) getApplication();
		xmppManager = aChatApp.getXmppManager();
		Log.d(TAG, String.format(
				"hmmm... ... status of the loggged in user: %s : %s ",
				xmppManager.getUserInfo()[0], xmppManager.getUserInfo()[1]));

		// // getting roster entries
		// Roster roster = xmppManager.getMeRoster();
		// Collection<RosterEntry> entries = roster.getEntries();
		// Presence presence = null;
		// String statusMsg = null;
		// String username = null;
		//
		// for (RosterEntry entry : entries) {
		//
		// Log.d(TAG, "Inside the foreach loop");
		//
		// try {
		// presence = roster.getPresence(entry.getUser());
		// username = entry.getUser();
		// usernames.add(username);
		//
		// Log.d(TAG, username);
		//
		// if ((entry.getStatus()) != null) {
		// statusMsg = entry.getStatus().toString();
		// Log.d(TAG, statusMsg);
		// } else {
		// statusMsg = "No Status Defined Yet";
		// }
		//
		// } catch (NullPointerException e) {
		// e.printStackTrace();
		// Log.d(TAG,
		// "I think you didn't handel the NULLPOINTER exception... regarding the statusMsg=null");
		// }
		//
		// // setup Adapter
		// setListAdapter(new ArrayAdapter<String>(MyBuddies.this,
		// android.R.layout.simple_list_item_1, usernames));
		//
		// }
		//
		// }
		//
		// @Override
		// protected void onListItemClick(ListView l, View v, int position, long
		// id) {
		// super.onListItemClick(l, v, position, id);
		// }
		//
		// }

		// Register the BroadcastReceiver to get the BroadCasted Intent with
		// filter ROSTER_CHG_BRODCAST_INTENT_FILTER
		// registerReceiver(rosterReceiver, new IntentFilter(
		// ChatService.ROSTER_CHG_BRODCAST_INTENT_FILTER));

		// getting roster entries
		Roster roster = xmppManager.getMeRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		Presence presence = null;
		Presence.Mode presenceMode;
		String statusMsg = null;
		String username = null;
		String available = null;
		int totalBuddesCount = 0;

		// for each loop to get all the users in the Buddy list i.e, Roster
		for (RosterEntry entry : entries) {

			++totalBuddesCount;
			Log.d(TAG, "Inside the foreach loop");

			try {
				// get roster entry's all User's UserName
				username = entry.getUser();
				usernames.add(username);
				Log.d(TAG, username);

				// get the present presence of the all Users in the roster
				presence = roster.getPresence(entry.getUser());
				// presenceMode = presence.getMode();
				// Log.d(TAG, presence.toString());
				// Log.d(TAG, presenceMode.toString());

				// extract the present status of the user
				if (presence.getStatus() != null) {
					statusMsg = presence.getStatus();
					usernames.add(statusMsg);
					Log.d(TAG, statusMsg);
				} else {
					statusMsg = ""; // No Status Defined Yet
					usernames.add(statusMsg);
					Log.d(TAG, statusMsg);
				}

				if (presence.isAvailable()) {
					// if (presenceMode.equals(PresenceState.online))
					available = "online";
					// else if (presenceMode.equals(PresenceState.away))
					// available = "away";
					// else if (presenceMode.equals(PresenceState.chat))
					// available = "Free to chat";
					// else if (presenceMode.equals(PresenceState.dnd))
					// available = "dnd";
					// else if (presenceMode.equals(PresenceState.xa))
					// available = "Away for an extended period of time.";
					usernames.add(available);
				} else
					// available="offline";
					usernames.add("offline");

			} catch (NullPointerException e) {
				e.printStackTrace();
				Log.d(TAG,
						"I think you didn't handel the NULLPOINTER exception... regarding the statusMsg=null");
			}

			// setup Adapter
			setListAdapter(new EfficientAdapter(this, usernames,
					totalBuddesCount));
			// setListAdapter(new EfficientAdapter(this, presence));
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.d(TAG, String.format(
				"hold your breath ... you chicked at buddy @ location...%d",
				position));

		// getListAdapter().getView(position, v, null).getTag().toString();
		String userIwannaChatWith = ((EfficientAdapter) getListAdapter())
				.getUserIamChatingWith(position);

		Log.d(TAG, "item at array adapter clicked and value is:"
				+ userIwannaChatWith);

		Intent intentForChatList = new Intent();
		intentForChatList.setAction(ChatService.CHATLIST_BFILTER);
		intentForChatList.putExtra("chatlist", userIwannaChatWith);
		sendBroadcast(intentForChatList);

		Intent intent = new Intent(this, ChatsTab.class);
		intent.putExtra("username", userIwannaChatWith);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	public void printNewMsgToast(String user, String msg) {
		// Toast.makeText(, R.string.chatTypeSel, Toast.LENGTH_LONG).show();
		Log.d(TAG, "Printing new message toast");
		Toast.makeText(this, "New message received:" + msg + ".From:" + user,
				Toast.LENGTH_LONG).show();

	}

	// Sub Class to make an ListAdapter
	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		private Bitmap aIconOnline;
		private Bitmap aIconOffline;
		private Bitmap aIconAway;
		private Bitmap aIconBusy;
		private int achat_buddyListSize;
		private Vector<String> userInfos;
		private Presence presence;

		/**
		 * Constructer
		 * 
		 * @param context
		 */
		public EfficientAdapter(Context context, Vector<String> userInfos,
				int buddyListSize) {
			// public EfficientAdapter(Context context, Presence presense) {

			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			// get the userInfos
			// this.presence=presence;
			this.achat_buddyListSize = buddyListSize;
			this.userInfos = userInfos;

			// Icons bound to the rows.
			aIconOnline = BitmapFactory.decodeResource(context.getResources(),
					android.R.drawable.presence_online);// R.drawable.icon48x48_1
			aIconOffline = BitmapFactory.decodeResource(context.getResources(),
					android.R.drawable.presence_offline);// R.drawable.icon48x48_1
			aIconAway = BitmapFactory.decodeResource(context.getResources(),
					android.R.drawable.presence_away);
			aIconBusy = BitmapFactory.decodeResource(context.getResources(),
					android.R.drawable.presence_busy);
		}

		/**
		 * The number of items in the list is determined by the number of roster
		 * entries
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			// How many items are in the data set represented by this Adapter.
			// four buddies in the list means four adapters.
			return achat_buddyListSize;
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * Sufficient to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			// Get the data item associated with the specified position in the
			// data set.
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			// Get the row id associated with the specified position in the
			// list.
			return position;
		}

		/**
		 * Get a View that displays the data at the specified position in the
		 * data set.
		 * 
		 * Make a view to hold each row. i.e,
		 * 
		 * 
		 * @return View Object that we designed with seting tag on view Object
		 *         as convertView.setTag(holder) where holder is all xml-view
		 *         resources holding object.
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.row_adapter, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();

				// bind the data to the holder
				holder.textUserName = (TextView) convertView
						.findViewById(R.id.textUsernameOnRowAdapter);
				holder.iconPresence = (ImageView) convertView
						.findViewById(R.id.icon_roster_presence);
				holder.textStatusMsg = (TextView) convertView
						.findViewById(R.id.text_Status_On_RowAdapter);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			Log.d(TAG, userInfos.toArray().toString());

			// Bind the data efficiently with the holder.
			int size = userInfos.size() / 2;
			Log.d(TAG, userInfos.toString());
			// for (int i = 0; i < size; i++) {

			Log.d(TAG, String.format("%d", position));

			int index = 3 * position;

			holder.textUserName.setText(userInfos.elementAt(index));
			++index;
			holder.textStatusMsg.setText(userInfos.elementAt(index));
			++index;
			// adjust Presence icon... online, away,busy, offline
			if (userInfos.elementAt(index).equals("online"))
				holder.iconPresence.setImageBitmap(aIconOnline);

			else if (userInfos.elementAt(index).equals("away")) {
				holder.iconPresence.setImageBitmap(aIconAway);
			} else if (userInfos.elementAt(index).equals("Free to chat")) {
				holder.iconPresence.setImageBitmap(aIconOnline);
			}

			else if (userInfos.elementAt(index).equals("dnd")) {
				holder.iconPresence.setImageBitmap(aIconBusy);
			} else {
				// else if (userInfos.elementAt(++index).equals("offline")) {
				holder.iconPresence.setImageBitmap(aIconOffline);
			}
			// Bind the data efficiently with the holder.
			// holder.text.setText(DATA[position]);
			// holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 :
			// mIcon2);
			// }
			return convertView;
		}

		public String getUserIamChatingWith(int index) {
			// Log.d(TAG, userInfos.toString());
			index *= 3;
			return userInfos.elementAt(index);

		}

		/**
		 * ViewHolder is the sub Class with in the EfficientAdapter class. It is
		 * used to keep references to children views to avoid unnessary calls to
		 * findViewById() on each row
		 * 
		 * Here we used it for the three Android views... i.e. 3-TextViews
		 * 
		 * @author aChat Developers Referencing the API DEMOS 14.Efficient
		 *         Adapter view
		 * 
		 */
		static class ViewHolder {
			TextView textUserName;
			TextView textStatusMsg;
			ImageView iconPresence;

		}
	}

	// subClass to receive the Roster BroadCast
	public class RosterReceiver extends BroadcastReceiver {
		String gotUser, gotText;

		@Override
		public void onReceive(Context context, Intent intent) {
			// update the current chat-buddy list here
			Log.d(TAG, "implementing the new roster listener...");

			gotUser = intent.getExtras()
					.getString(ChatService.CHATMSG_BFILTER_MSGFRM).toString();
			gotText = intent.getExtras()
					.getString(ChatService.CHATMSG_BFILTER_MSGTXT).toString();

			// print toast for new message received
			// handler.post(new Runnable() {
			//
			// public void run() {
			// // TODO Auto-generated method stub
			printNewMsgToast(gotUser, gotText);
			//
			// }
			// });

		}
	}

}
