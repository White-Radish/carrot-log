package com.carrot.simplelog.model;

public enum LogLevel {
	DEBUG(1),
	INFO(2),
	WARN(3),
	ERROR(4),
	FATAL(5);
	int levelValue;

	LogLevel(int value) {
		levelValue = value;
	}
	
	public int value(){
		return levelValue;
	}
	

}
