package com.hqu.utils;

import java.util.Random;

public class Salt {
	public static String getRandomString() {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	public static int getTelCode(){
		String baseString = "0123456789";
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		for(int i = 0 ;i <6;i++){
			int number = random.nextInt(baseString.length());
			stringBuffer.append(baseString.charAt(number));
		}
		return Integer.parseInt(stringBuffer.toString());
	}
	public static int getRandomFour(){
		String baseString = "0123456789";
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		for(int i = 0 ;i <4;i++){
			int number = random.nextInt(baseString.length());
			stringBuffer.append(baseString.charAt(number));
		}
		return Integer.parseInt(stringBuffer.toString());
	}
}
