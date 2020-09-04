package com.carrot.simplelog.utils;

/**
 * @author carrot
 * @date 2020/9/4 15:52
 */
public class EnvironmentUtil {
	public static String getLocalCharset() {
		String localCharset = null;
		String lang = System.getenv("LANG");

		if (lang != null) {
			System.out.printf("System Env LANG=%s \n", lang);
			int idx = lang.lastIndexOf('.');
			if (idx > -1) {

				localCharset = lang.substring(idx + 1);
			} else {
				localCharset = lang;
			}
		} else {
			System.out.printf("System Env LANG=NULL \n");
			String name = System.getProperty("sun.jnu.encoding");
			if (name != null) {
				System.out.printf("sun.jnu.encoding=%s \n", name);
				localCharset = name;
			} else {
				localCharset = java.nio.charset.Charset.defaultCharset().name();
				System.out.printf("sun.jnu.encoding=NULL \n");

			}
		}
		localCharset = localCharset.toLowerCase();
		return localCharset;
	}
}
