package com.js.baseballdb;

public class ValuePair {
	private String key, value;
	
	public ValuePair(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public String getValue(){
		return this.value;
	}
}
