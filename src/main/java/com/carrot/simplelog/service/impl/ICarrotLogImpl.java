package com.carrot.simplelog.service.impl;

import com.carrot.simplelog.config.CarrotLogProperties;
import com.carrot.simplelog.service.ICarrotLog;
import com.carrot.simplelog.utils.LogFileWriteUtil;

/**
 * @author carrot
 * @date 2020/9/3 17:11
 */
public class ICarrotLogImpl implements ICarrotLog {
	CarrotLogProperties properties;

	public ICarrotLogImpl(CarrotLogProperties properties) {
		this.properties=properties;
		LogFileWriteUtil.setRule(properties);
	}


	/**
	 * 基本信息
	 *
	 * @param content 日志内容
	 */
	@Override
	public void info(String content) {
		LogFileWriteUtil.carrotInfo(content);
	}

	/**
	 * 警告信息
	 *
	 * @param content 日志内容
	 */
	@Override
	public void warn(String content) {
		LogFileWriteUtil.carrotWarn(content);
	}

	/**
	 * 程序错误信息
	 *
	 * @param content 日志内容
	 */
	@Override
	public void error(String content) {
		LogFileWriteUtil.carrotError(content);
	}

	/**
	 * 程序致命错误信息
	 *
	 * @param content 日志内容
	 */
	@Override
	public void fatal(String content) {
		LogFileWriteUtil.carrotFatal(content);
	}

	/**
	 * 基本信息
	 *
	 * @param msgFormat
	 * @param params
	 */
	@Override
	public void info(String msgFormat, Object... params) {
		LogFileWriteUtil.carrotInfo(msgFormat,params);
	}

	/**
	 * 警告信息
	 *
	 * @param msgFormat
	 * @param params
	 */
	@Override
	public void warn(String msgFormat, Object... params) {
		LogFileWriteUtil.carrotWarn(msgFormat,params);
	}

	/**
	 * 程序错误信息
	 *
	 * @param msgFormat
	 * @param params
	 */
	@Override
	public void error(String msgFormat, Object... params) {
		LogFileWriteUtil.carrotError(msgFormat,params);
	}

	/**
	 * 程序错误信息
	 *
	 * @param msgFormat
	 * @param params
	 */
	@Override
	public void fatal(String msgFormat, Object... params) {
		LogFileWriteUtil.carrotFatal(msgFormat,params);
	}

	/**
	 * 程序错误信息
	 *
	 * @param eles
	 */
	@Override
	public void fatal(StackTraceElement[] eles) {
		LogFileWriteUtil.carrotFatal(eles);
	}
}
