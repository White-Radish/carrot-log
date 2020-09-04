package com.carrot.simplelog.service;

/**
 * @author carrot
 * @date 2020/9/3 17:11
 */
public interface ICarrotLog {
	/**
	 * 基本信息
	 * @param content 日志内容
	 */
	void info(String content);
	/**
	 * 警告信息
	 * @param content 日志内容
	 */
	void warn(String content);
	/**
	 * 程序错误信息
	 * @param content 日志内容
	 */
	void error(String content);

	/**
	 * 程序致命错误信息
	 * @param content 日志内容
	 */
	void fatal(String content);

	/**
	 * 基本信息
	 */
	void info(String msgFormat,Object... params);
	/**
	 * 警告信息
	 */
	void warn(String msgFormat,Object... params);
	/**
	 * 程序错误信息
	 */
	void error(String msgFormat,Object... params);
	/**
	 * 程序错误信息
	 */
	void fatal(String msgFormat,Object... params);

	/**
	 * 程序错误信息
	 */
	void fatal(StackTraceElement[] eles);

}
