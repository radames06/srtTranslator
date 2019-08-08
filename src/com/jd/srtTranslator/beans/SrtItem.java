package com.jd.srtTranslator.beans;

public class SrtItem {
	private int rowId;
	private String start;
	private String end;
	private String text;
	private String translation;
	
	public SrtItem (int rowId, String start, String end, String text, String translation) {
		this.rowId = rowId;
		this.start = start;
		this.end = end;
		this.text = text;
		this.translation = translation;
	}
	public SrtItem() {

	}
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String toString() {
		return this.rowId + " / " + this.start + " / " + this.end + " / " + this.text + " / " + this.translation; 
	}
	
}
