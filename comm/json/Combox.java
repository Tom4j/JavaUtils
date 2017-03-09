package com.siweidg.comm.json;

public class Combox {
	
	private String id;
	
	private String text;

	private String code;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Combox(String id, String text, String code) {
		this.id = id;
		this.text = text;
		this.code = code;
	}
	
	public Combox(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public Combox() {
		
	}
}
