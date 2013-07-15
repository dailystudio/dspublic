package com.dailystudio.app.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

	public static Integer[] toIntegerArray(int[] array) {
		if (array == null) {
			return null;
		}
		
		List<Integer> newArray = new ArrayList<Integer>();
		
		final int N = array.length;
		
		for (int i = 0; i < N; i++) {
			newArray.add(array[i]);
		}
		
		return newArray.toArray(new Integer[0]);
	}
	
	public static String byteArrayToHex(byte[] a) {
		return byteArrayToHex(a, false);
	}

	public static String byteArrayToHex(byte[] a, boolean withSplitter) {
		if (a == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		
		final int N = a.length;
		
		for(int i = 0; i < N; i++) {
			sb.append(String.format("%02x", a[i]&0xff));
			if (withSplitter && i != (N - 1)) {
				sb.append(':');
			}
		}
		
		return sb.toString();
	}

}
