package com.graphics;

//import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;

public class CoordinateArray {
	private JSONArray jsonArrayXs;
	private JSONArray jsonArrayYs;

	private JSONObject jSONObject;
	
	private int color;
	private boolean embossFlag;
	private boolean blurFlag;
	private boolean eraserFlag;
	private boolean scratchFlag;
	
	public void setColor( int color )
	{
		this.color = color;
	}
	
	public int getColor () {
		return color;
	}
	
	public void setEmbossFlag( boolean embossFlag)
	{
		this.embossFlag = embossFlag;
	}
	
	public boolean getEmbossFlag () {
		return embossFlag;
	}
	
	public void setBlurFlag( boolean blur)
	{
		this.blurFlag = blur;
	}
	
	public boolean getBlurFlag( )
	{
		return blurFlag;
	}
	
	public void setEraserFlag( boolean erase )
	{
		this.eraserFlag = erase;
	}
	
	public boolean getEraserFlag()
	{
		return eraserFlag;
	}
	
	public void setScratchFlag( boolean scratch)
	{
		this.scratchFlag = scratch;
	}
	
	public boolean getScratchFlag()
	{
		return scratchFlag;
	}
	public CoordinateArray() {
		jsonArrayXs = new JSONArray();
		jsonArrayYs = new JSONArray();
		// jsonArray = new JSONArray();
	}

	public void addXnY(double x, double y) {
		try {
			addX(x);
			addY(y);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addX(double x) throws JSONException {
		jsonArrayXs.put(x);
	}

	public double getXAt(int pos) throws JSONException {
		return jsonArrayXs.getDouble(pos);
	}

	public void addY(double y) throws JSONException {
		jsonArrayYs.put(y);
	}

	public double getYAt(int pos) throws JSONException {
		return jsonArrayYs.getDouble(pos);
	}

	public void getJSONObjectFromMembers() throws JSONException {

		jSONObject = new JSONObject();
		jSONObject.put("X", jsonArrayXs);
		jSONObject.put("Y", jsonArrayYs);
		jSONObject.put("color", color);
		jSONObject.put("embossFlag", embossFlag);
		jSONObject.put("blurFlag", blurFlag);
		jSONObject.put("eraseFlag", eraserFlag);
		jSONObject.put("scratchFlag", scratchFlag);
		// return jsonArray;
	}

	public String getXMLString() {
		String str;
		try {
			getJSONObjectFromMembers();
			str = XML.toString(jSONObject);
			return str;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean setOjbectsFromXMLString(String XMLstring) {
		try {
			jSONObject = XML.toJSONObject(XMLstring);

			jsonArrayXs = (JSONArray) jSONObject.get("X");
			jsonArrayYs = (JSONArray) jSONObject.get("Y");

			color = jSONObject.getInt("color");
			embossFlag = jSONObject.getBoolean("embossFlag");
			blurFlag = jSONObject.getBoolean("blurFlag");
			eraserFlag = jSONObject.getBoolean("eraseFlag");
			scratchFlag = jSONObject.getBoolean("scratchFlag");
//			jSONObject.put("color", color);
//			jSONObject.put("embossFlag", embossFlag);
//			jSONObject.put("blurFlag", blurFlag);
//			jSONObject.put("eraseFlag", eraserFlag);
//			jSONObject.put("scratchFlag", scratchFlag);
			return true;
			// jsonArrayXs = jsonArray.getJSONArray(0);
			//
			// jsonArrayYs = jsonArray.getJSONArray(1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
