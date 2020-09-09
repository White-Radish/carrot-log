package com.carrot.simplelog.utils;

import com.carrot.simplelog.config.CarrotLogProperties;
import com.carrot.simplelog.model.LogLevel;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author carrot
 * @date 2020/9/3 20:39
 */
public class LogFileWriteUtil {
	private static DateFormat m_dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
	private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final ByteBuffer CRLF = ByteBuffer.wrap(new byte[]{'\r', '\n'});
	private static boolean isPrintToConsole = true;
	private static String logFilePath = ".";
	private static FileChannel logFileChannel = null;
	private static java.nio.charset.Charset charset = null;
	private static LogLevel defaultLevel = LogLevel.INFO;
	private static int writeFileLevel = LogLevel.INFO.value();
	private static int maxExceptionStackDepth = 20;
	private static String logFilePrefix = "";

	static {
		String localCharset = EnvironmentUtil.getLocalCharset();
		charset = java.nio.charset.Charset.forName(localCharset);
		Properties props = System.getProperties();
		props.put("file.encoding", localCharset);
		props.put("sun.jnu.encoding", localCharset);
		System.out.printf("defaultCharset=%s  \n", localCharset);
		System.out.printf("file.encoding=%s   \n", System.getProperty("file.encoding"));
		System.out.printf("sun.jnu.encoding=%s \n", System.getProperty("sun.jnu.encoding"));
		CRLF.position(2);
	}

	// / java.util.concurrent.

	private static boolean checkLog() {
		if (!isPrintToConsole && logFilePath == null) {
			return false;
		}
		if (logFilePath != null) {
			boolean bCreateNew = false;
			String tempPath = logFilePath;
			Date t2 = new Date();
			String datetmp = dateTimeFormat.format(t2);
			tempPath = tempPath + File.separator + dateTimeFormat.format(t2);
			String fileName = String.format("%s/%s", tempPath, logFilePrefix);
			if(!Paths.get(tempPath).toFile().exists()){
				try {
					//文件夹不存在就创建目录
					Files.createDirectories(Paths.get(tempPath));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			//判断日志文件是否存在
			Path p = Paths.get(fileName);
			if (!p.toFile().exists()) {
				try {
					Files.createFile(p);
					bCreateNew=true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (!bCreateNew && logFileChannel!=null) {
				return true;
			}
			if (logFileChannel != null) {
				try {
					logFileChannel.close();
				} catch (Exception e) {

				}
			}
			try {
				logFileChannel = FileChannel.open(p,
						StandardOpenOption.APPEND,
						StandardOpenOption.CREATE,
						StandardOpenOption.DSYNC);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return true;
	}


	private static String now() {
		return m_dateTimeFormat.format(java.util.Calendar.getInstance().getTime());
	}


	private static synchronized void writeBuffer(LogLevel level, ByteBuffer buffer) {
		if (logFileChannel != null && level.value() >= writeFileLevel) {
			try {
				logFileChannel.write(buffer);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		if (isPrintToConsole) {
			PrintStream ps = System.out;
			if (level.value() > LogLevel.WARN.value()) {
				ps = System.err;
			}
			try {
				byte[] bb = buffer.array();
				ps.write(bb, 0, buffer.position());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	private synchronized static void trace(LogLevel level, String message) {

		ByteBuffer bTime = null;
		ByteBuffer bMsg = null;

		try {
			bTime = charset.encode(now());
			if (message != null) {
				bMsg = charset.encode(message);

			}
		} catch (Exception e) {
			e.printStackTrace();
			bTime = charset.encode(now());
			if (message != null) {
				bMsg = charset.encode(message);

			}
		}
		writeBuffer(level, bTime);
		writeBuffer(level, bMsg);
		bTime = null;
		bMsg = null;

	}

	private synchronized static  void trace(StackTraceElement[] eles) {
		trace(eles, false);
	}

	private synchronized static void traceReverse(StackTraceElement[] eles) {
		trace(eles, true);
	}

	private synchronized static void trace(StackTraceElement[] eles, boolean bReverse) {
		if (eles == null || eles.length < 1) {
			return;
		}
		checkLog();
		StringBuffer stringbuffer = new WeakReference<>(new StringBuffer()).get();
		int i = 0;
		String s ;
		int start ;
		int end ;
		int step ;
		if (bReverse) {
			start = eles.length - 1;
			end = -1;
			step = -1;
		} else {
			start = 0;
			end = eles.length;
			step = 1;
		}
		for (int idx = start; idx != end; idx += step) {
			StackTraceElement el = eles[idx];
			s = el.toString();
			stringbuffer.append(s);
			stringbuffer.append("\r\n");
			s = null;
			i++;
			if (i > maxExceptionStackDepth) {
				break;
			}
		}

		if (eles.length > maxExceptionStackDepth) {

			stringbuffer.append("......\r\n");
		}
		ByteBuffer bTime = charset.encode(now());
		s = stringbuffer.toString();
		ByteBuffer bMsg = charset.encode(s);
		writeBuffer(LogLevel.FATAL, bTime);
		writeBuffer(LogLevel.FATAL, bMsg);

		writeBuffer(LogLevel.FATAL, CRLF);
		s = null;
		stringbuffer = null;
		bTime = null;
		bMsg = null;
	}

	synchronized static public void trace(Throwable logException) {
		if (logException == null) {
			return;
		}
		checkLog();
		StringBuffer stringbuffer = new WeakReference<StringBuffer>(
				new StringBuffer()).get();
		String s = logException.toString();
		stringbuffer.append(s);
		stringbuffer.append("\r\n");
		s = null;
		Throwable cause = logException.getCause();
		if (cause != null) {
			s = cause.getMessage();
			stringbuffer.append(s);
			stringbuffer.append("\r\n");
			s = null;

		}
		cause = null;
		int i = 0;
		StackTraceElement[] eles = logException.getCause() == null ? logException
				.getStackTrace() : logException.getCause().getStackTrace();
		for (StackTraceElement el : eles) {

			s = el.toString();
			stringbuffer.append(s);
			stringbuffer.append("\r\n");
			s = null;

			i++;
			if (i > maxExceptionStackDepth) {
				break;
			}
		}

		if (eles.length > maxExceptionStackDepth) {

			stringbuffer.append("......\r\n");
		}

		ByteBuffer bTime = null;
		ByteBuffer bMsg = null;

		try {
			bTime = charset.encode(now());
			if (stringbuffer.length() > 0) {
				s = stringbuffer.toString();
				bMsg = charset.encode(s);
				s = null;
				stringbuffer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bTime = charset.encode(now());
			if (stringbuffer.length() > 0) {
				s = stringbuffer.toString();
				bMsg = charset.encode(s);
				s = null;
				stringbuffer = null;
			}
		}

		writeBuffer(LogLevel.FATAL, bTime);
		writeBuffer(LogLevel.FATAL, bMsg);

		writeBuffer(LogLevel.FATAL, CRLF);
		bTime = null;
		bMsg = null;

	}

	private synchronized static void printf(LogLevel level, String msgFormat,
											Object... params) {
		if (msgFormat == null) {
			return;
		}
		// CHECK LEVEL
		checkLog();
		String s = new WeakReference<String>(String.format(msgFormat, params)).get();

		trace(level, s);
		s = null;
	}

	private synchronized static void println(LogLevel level, String s) {
		if (s == null) {
			return;
		}
		checkLog();
		s = appendClassName(s);
		trace(level, s);
		writeBuffer(level, CRLF);
	}

	private synchronized static void print(LogLevel level, String message) {
//		checkLog();
		println(level, message);
	}

	private synchronized static String appendClassName(String str) {

		StackTraceElement[] stackTraceElements = new WeakReference<>(
				new Throwable().getStackTrace()).get();

		int i = 0;
		assert stackTraceElements != null;
		for (i = 0; i < stackTraceElements.length; i++) {
			if (stackTraceElements[i].getClassName().equals(LogFileWriteUtil.class.getName())) {
				break;
			}
		}
		StringBuffer stringbuffer = new WeakReference<>(
				new StringBuffer()).get();
		stringbuffer.append("[").append( stackTraceElements[i + 1].getClassName());
		stringbuffer.append(": ").append(stackTraceElements[i + 1].getLineNumber()).append("] ");
		stringbuffer.append(str);
		stringbuffer.append("\r\n");
		return stringbuffer.toString();
	}


	public synchronized static void carrotInfo(String message) {
		print(LogLevel.INFO, message);
	}

	public synchronized static void carrotWarn(String message) {
		print(LogLevel.WARN, message);
	}

	public synchronized static void carrotError(String message) {
		print(LogLevel.ERROR, message);
	}

	public synchronized static void carrotFatal(String message) {
		print(LogLevel.FATAL, message);
	}

	public synchronized static void carrotPrint(String message) {
		print(defaultLevel, message);
	}


	public synchronized static void carrotInfo(String msgFormat, Object... params) {

		printf(LogLevel.INFO, msgFormat, params);
	}

	public synchronized static void carrotWarn(String msgFormat, Object... params) {
		printf(LogLevel.WARN, msgFormat, params);
	}

	public synchronized static void carrotError(String msgFormat, Object... params) {
		printf(LogLevel.ERROR, msgFormat, params);
	}

	public synchronized static void carrotFatal(String msgFormat, Object... params) {
		printf(LogLevel.FATAL, msgFormat, params);
	}

	public synchronized static void carrotFatal(StackTraceElement[] eles) {
		trace(eles);
	}

	public synchronized static void carrotPrint(String msgFormat, Object... params) {
		printf(defaultLevel, msgFormat, params);
	}

	public static void setRule(CarrotLogProperties properties) {
		if (!StringUtils.isEmpty(properties.getLogFileName())) {
			if (properties.getLogFileName().endsWith(".log")) {
				logFilePrefix = properties.getLogFileName();
			} else {
				logFilePrefix = properties.getLogFileName() + ".log";
			}
		}

		if (!StringUtils.isEmpty(properties.getLogFilePath())) {
			logFilePath = properties.getLogFilePath();
		}
		if (!StringUtils.isEmpty(properties.isPrintConsole())) {
			isPrintToConsole = properties.isPrintConsole();
		}
	}


}
