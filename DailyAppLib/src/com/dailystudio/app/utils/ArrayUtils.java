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
	
}
