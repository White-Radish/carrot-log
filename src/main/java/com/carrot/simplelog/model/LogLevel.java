package com.carrot.simplelog.model;

public enum LogLevel {

	INFO(1),
	WARN(2),
	ERROR(3),
	FATAL(4);
	int levelValue;

	LogLevel(int value) {
		levelValue = value;
	}
	
	public int value(){
		return levelValue;
	}
	

}
