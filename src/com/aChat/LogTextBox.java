package com.aChat;

import android.widget.TextView;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.text.method.MovementMethod;
import android.text.Editable;
import android.util.AttributeSet;

/**
 * This is a TextView that is Editable and by default scrollable, like EditText
 * without a cursor.
 * 
 * <p>
 * <b>XML attributes</b>
 * <p>
 * See {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 */

public class LogTextBox extends TextView {
	public LogTextBox(Context context) {
		this(context, null);
	}

	public LogTextBox(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public LogTextBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected MovementMethod getDefaultMovementMethod() {
		return ScrollingMovementMethod.getInstance();
	}

	@Override
	public Editable getText() {
		return (Editable) super.getText();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, BufferType.EDITABLE);
	}
}
