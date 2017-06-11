package com.hqu.utils;

import java.security.MessageDigest;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 
 */
public class CipherUtil {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 加密密码
	 * 
	 * @param password
	 *            用户输入的密码
	 * @param salt
	 *            数据库加密盐
	 * @return
	 */
	public static String generatePassword(String password, String salt) {
		String password_salt_Md5 = new Md5Hash(password, salt, 1).toString();
		return password_salt_Md5;
	}

	/**
	 * validate password
	 * 
	 * @param password
	 * @param inputString
	 * @return
	 */
	public static boolean validatePassword(String password, String inputString) {
		if (password.equals(encodeByMD5(inputString))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * encode
	 * 
	 * @param originString
	 * @return
	 */
	public static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(originString.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * change the Byte[] to hex string
	 * 
	 * @param b
	 * @return
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * change a byte to hex string
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String[] args) {		
		String pwd2 = null;
		CipherUtil cipher = new CipherUtil();
		String saltString = Salt.getRandomString();
		saltString = "111111";
		System.out.println(saltString);
		pwd2 = cipher.generatePassword("123456", saltString);
		System.out.println("加密后密码" + pwd2);

	}
}