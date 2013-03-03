package com.aChat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbClass {
	private Context context;
	String table = "chatmessage";
	DbHelper dbHelper;

	DbClass(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context);
	}

	class DbHelper extends SQLiteOpenHelper {

		private static final String db_name = "message.db";
		private static final int db_version = 12;
		static final String A_ID = "_id";
		static final String A_CREATED_AT = "time";
		static final String A_MSG = "message";
		static final String A_LOGGEDIN_USER = "loggedin";
		static final String A_SECOND_USER = "seconduser";
		static final String A_TYPE = "type"; // send or received
		// static final String table = "messages";
		static final String TAG = "DATABASE";
		private String sql;

		public DbHelper(Context context) {
			super(context, db_name, null, db_version);

		}

		// called only once when db is created
		@Override
		public void onCreate(SQLiteDatabase db) {

			// sql = String.format(
			// "create table %s (%s TEXT, %s TEXT, %s TEXT, %s TEXT )",
			// table, A_TYPE, A_USER, A_CREATED_AT, A_MSG);
			sql = String
					.format("create table %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT , %s TEXT, %s TEXT, %s TEXT )",
							table, A_ID, A_TYPE, A_LOGGEDIN_USER,
							A_SECOND_USER, A_CREATED_AT, A_MSG);
			db.execSQL(sql);
			Log.i(TAG, "oncreate method: " + sql);

		}

		// called when newversion != oldversion
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + table); // drops the old
															// database
			Log.d(TAG, "onUpdated");
			onCreate(db); // run onCreate to get new database
		}

	}

	// String database;
	static final String TAG = "DbClass";

	SQLiteDatabase db;

	/**
	 * insert values in message table of database
	 * 
	 * @param values
	 */
	public long insertOrIgnore(ContentValues values) {
		Log.d(TAG, "insertOrIgnore on " + values);
		// open database
		db = dbHelper.getWritableDatabase();
		long l;
		try {
			l = db.insertOrThrow(table, null, values);
		} catch (SQLException e) {
			l = -1;
		} finally {
			// close database
			db.close();
		}
		return l;

	}

	/**
	 * query db for reteriving messages
	 * 
	 * @param type
	 */

	public Cursor query(String user1, String user2) {
		// String[] string = {"rabin555@rabin-pc","sdf"};
		// open database
		// Log.d(TAG, "got text is:"+user);
		user1 = "'" + user1 + "'"; // logged in user
		user2 = "'" + user2 + "'";
		db = dbHelper.getReadableDatabase();

		Log.d(TAG, "before query");
		// get the data
		// Cursor cursor = db.query(table, new String[] { "type", "message" },
		// "loggedin like " + user1 + " and seconduser like " + user2,
		// null, null, null, null, null);
		Cursor cursor = db
				.query(table, new String[] { "type", "message", "seconduser" },
						"loggedin like " + user1 + " and seconduser like "
								+ user2, null, null, null, null, "10");
		Log.d(TAG, "after query");
		return cursor;

	}

	// public Cursor query() {
	// // String[] string = {"sdf","sadf"};
	// // open database
	// db = dbHelper.getReadableDatabase();
	//
	// // get the data
	// Cursor cursor = db.query(table, null, null, null, null, null, null);
	// return cursor;
	//
	// }

	// delete all entries from database
	public void delete() {
		// open database
		db = dbHelper.getWritableDatabase();

		// delete all entries from database
		db.delete(table, null, null);

		// close database
		db.close();
	}
}
