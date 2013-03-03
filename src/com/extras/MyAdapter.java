package com.extras;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Spanned> {

	Typeface typeface;
	private final Context context;
	private List<Spanned> objects; // obviously don't use object, use whatever

	public MyAdapter(Context context, int textViewResourceId,
			List<Spanned> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.objects = objects;
		typeface = Typeface.createFromAsset(context.getAssets(),
				"DroidHindi.ttf");
		// TODO Auto-generated constructor stub
	}

	// you really want

	public int getCount() {
		return objects.size();
	}

	public Spanned getItem(int position) {
		return objects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v = super.getView(position, convertView, parent);
		((TextView) v).setTypeface(typeface);
		return v;

	}
}
