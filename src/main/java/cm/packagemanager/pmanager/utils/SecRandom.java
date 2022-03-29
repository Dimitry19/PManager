package cm.packagemanager.pmanager.utils;

import java.security.SecureRandom;

public class SecRandom {

	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	private static final int startChar = (int) '!';
	private static final int endChar = (int) '~';



	public static String randomString(int len){
		StringBuilder sb = new StringBuilder(len);
		for(int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public static String randomStringJava8(final int maxLength) {
		final int length = rnd.nextInt(maxLength + 1);
		return rnd.ints(length, startChar, endChar + 1)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}
}
