package com.aChat;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UnicodeTestActivity extends Activity {
	/** Called when the activity is first created. */
	EditText myEdittext;
	TextView myTextView;
	Typeface typeFace;
	Button button;
	String string;
	int i;
	int ini_pos, final_pos;
	String temp;
	HashMap<String, String> codeTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unicodetest);

		// myTextView = (TextView) findViewById(R.id.welcometextview);
		// myTextView.append("\u0915" +"\u0916");

		// myTextView.setTypeface(typeFace);

		myTextView = (TextView) findViewById(R.id.unicodeTextView);
		// myTextView.append(" \u0915sdf" + " \u0916sdfas ");
		typeFace = Typeface.createFromAsset(getAssets(), "DroidHindi.ttf");
		myTextView.setTypeface(typeFace);

		myEdittext = (EditText) findViewById(R.id.unicodeEditText);
		button = (Button) findViewById(R.id.unicodeButton);

		setCodeTable();

		// myEdittext.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// ini_pos = myEdittext.getSelectionStart();
		// }
		// });
		myEdittext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// // TODO Auto-generated method stub

				ini_pos = myEdittext.getSelectionStart();

			}
		});

		//
		myEdittext.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.d("KEY_CODE", " ->" + keyCode);
				if (keyCode == 62)// if key pressed is space
				{
					final_pos = myEdittext.getSelectionStart();
					temp = myEdittext.getText().toString()
							.substring(ini_pos, final_pos);
					// ini_pos = final_pos;
					Log.d("INI_POS", ".." + ini_pos);

					Log.d("INI_POS", ".." + myEdittext.getSelectionStart());
					check(temp);
				} else if (keyCode == 67 || keyCode == 21 || keyCode == 22) {
					ini_pos = myEdittext.getSelectionStart();
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

			public boolean check(String temp) {
				// TODO Auto-generated method stub

				Log.d("SubString Temp -:", "|" + temp + "|");
				String subtemp = "";
				int size = temp.length();
				int i_pos = 0, f_pos = 1;
				string = myEdittext.getText().toString();
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
					myEdittext.setText(string);
					myEdittext.setTypeface(typeFace);
					int cur_pos = (int) myEdittext.getText().length();
					Log.d("cur_pos", "  ->" + cur_pos);
					myEdittext.setSelection(cur_pos);

					Log.d("strring is no empty", "cusros changed");
				}
				// ini_pos = final_pos;
				return false;
			}

		});

		// button.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		// view = (TextView) findViewById(R.id.textView1);
		// view.setText("test");
		// typeFace = Typeface.createFromAsset(getAssets(),
		// "DroidHindi.ttf");
		// view.setTypeface(typeFace);
		//

		// myEdittext.setOnKeyListener(new View.OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // TODO Auto-generated method stub
		// // text.setTypeface(typeFace);gggg
		// if (myEdittext.getText().toString() == "a") {
		// Log.d("TAG", "INSIDE A");
		// myEdittext.append("\u0915sdf");
		// } else {
		// Log.d("TAG", "outside A");
		// }
		// myEdittext.setTypeface(typeFace);
		// myTextView.setText(myEdittext.getText().toString());
		// myTextView.setTypeface(typeFace);
		//
		// return false;
		// }
		// });
		// text.setOnKeyListener(new View.OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // TODO Auto-generated method stub
		// return false;
		// }
		// })

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
		codeTable.put("khi", "\u0916" + "\u093F");
		codeTable.put("gi", "\u0917" + "\u093F");
		codeTable.put("ghi", "\u0918" + "\u093F");
		codeTable.put("ngi", "\u0919" + "\u093F");
		codeTable.put("chi", "\u091A" + "\u093F");
		codeTable.put("chhi", "\u091B" + "\u093F");
		codeTable.put("ji", "\u091C" + "\u093F");
		codeTable.put("jhi", "\u091D" + "\u093F");
		codeTable.put("Ti", "\u091F" + "\u093F");
		codeTable.put("Thi", "\u0920" + "\u093F");
		codeTable.put("Di", "\u0921" + "\u093F");
		codeTable.put("Dhi", "\u0922" + "\u093F");
		codeTable.put("ti", "\u0924" + "\u093F");
		codeTable.put("thi", "\u0925" + "\u093F");
		codeTable.put("di", "\u0926" + "\u093F");
		codeTable.put("dhi", "\u0927" + "\u093F");
		codeTable.put("ni", "\u0928" + "\u093F");
		codeTable.put("pi", "\u092A" + "\u093F");
		codeTable.put("phi", "\u092B" + "\u093F");
		codeTable.put("fi", "\u092B" + "\u093F");
		codeTable.put("bi", "\u092C" + "u093F");
		codeTable.put("bhi", "\u092D" + "\u093F");
		codeTable.put("mi", "\u092E" + "\u093F");
		codeTable.put("yi", "\u092F" + "\u093F");
		codeTable.put("ri", "\u0930" + "\u093F");
		codeTable.put("li", "\u0932" + "\u093F");
		codeTable.put("vi", "\u0935" + "\u093F");
		codeTable.put("shi", "\u0936" + "\u093F");
		codeTable.put("si", "\u0938" + "\u093F");
		codeTable.put("hi", "\u0939" + "\u093F");

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
}

/*
 * 
 * if (myEdittext.getText().toString().contains("mero")) { string =
 * myEdittext.getText().toString().replaceAll("mero", "\u0915"); // myEdittext.
 * // myEdittext.append("\u0915"); //myEdittext.myEdittext.setText(string);
 * myEdittext.setTypeface(typeFace); //myTextView.append("\u0916" + "0917");
 * //myTextView.setTypeface(typeFace); //Log.d("tag",
 * myTextView.getText().toString());
 * 
 * System.out.println("the text is:"+ myTextView.getText().toString()); } //else
 * if (myEdittext.getText().toString().charAt(0) == 'a') //{ // //string =
 * myEdittext.getText().toString().re // //
 * 
 * myEdittext.setText(string); //
 * 
 * myEdittext.setTypeface(typeFace); //} else if
 * (myEdittext.getText().toString().contains("naam")) { string =
 * myEdittext.getText().toString().replaceAll("naam", "\u0917");
 * myEdittext.setText(string); myEdittext.setTypeface(typeFace);
 * 
 * //
 * 
 * myTextView.append("\u0915"); // myTextView.setTypeface(typeFace); } else if
 * (myEdittext.getText().toString().contains("aa")) { string
 * =myEdittext.getText().toString().replaceAll("aa", "\u0906");
 * myEdittext.setText(string); myEdittext.setTypeface(typeFace); } else if
 * 
 * (myEdittext.getText().toString().contains("au")) { string =
 * 
 * myEdittext.getText().toString()
 * 
 * 
 * .replaceAll("au", "\u0914");
 * 
 * 
 * myEdittext.setText(string);
 * 
 * 
 * myEdittext.setTypeface(typeFace); } else if
 * 
 * (myEdittext.getText().toString().contains("aama")) { string =
 * 
 * myEdittext.getText().toString()
 * 
 * 
 * .replaceAll("aama", "\u0916" + "\u0914");
 * 
 * 
 * myEdittext.setText(string);
 * 
 * 
 * myEdittext.setTypeface(typeFace); } else if
 * 
 * (myEdittext.getText().toString().contains("ki")) { string =
 * 
 * myEdittext.getText().toString()
 * 
 * 
 * .replaceAll("ki", "\u093F" + "\u0915");
 * 
 * 
 * myEdittext.setText(string);
 * 
 * 
 * myEdittext.setTypeface(typeFace);
 * 
 * } else if
 * 
 * (myEdittext.getText().toString().contains("sho")) { string =
 * 
 * myEdittext.getText().toString()
 * 
 * 
 * .replaceAll("sho", "\u0936" + "\u094B");
 * 
 * 
 * myEdittext.setText(string);
 * 
 * 
 * myEdittext.setTypeface(typeFace); } }
 * 
 * try{ int
 * 
 * cur_pos = (int)myEdittext.getText().length(); Log.d
 * 
 * ("cur_pos", "  ->"+cur_pos);
 * 
 * 
 * myEdittext.setSelection(cur_pos); } catch (Exception
 * 
 * e) { //
 * 
 * TODO: handle exception Log.d
 * 
 * ("setselection Error", e.getMessage()); //Log.d
 * 
 * ("setselection", e.prin); } //
 * 
 * myEdittext.selectAll(); //
 * 
 * myEdittext.bringToFront(); return false; }
 */