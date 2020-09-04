package com.carrot.simplelog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Carrot
 * @date 2020/9/3 17:15
 */
@ConfigurationProperties(prefix = "carrot.log")
@Component
public class CarrotLogProperties {
	private String logFilePath;
	private String logFileName;
	private boolean printConsole;
	private boolean enabled;

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public boolean isPrintConsole() {
		return printConsole;
	}

	public void setPrintConsole(boolean printConsole) {
		this.printConsole = printConsole;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "CarrotLogProperties{" +
				"logFilePath='" + logFilePath + '\'' +
				", logFileName='" + logFileName + '\'' +
				", enabled=" + enabled +
				'}';
	}
}
