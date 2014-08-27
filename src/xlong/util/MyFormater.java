package xlong.util;

import java.util.ArrayList;

public class MyFormater {
	public static <T> StringBuilder formatArray(T[] ss) {
		StringBuilder str = new StringBuilder();
		for (T s:ss) {
			str.append(s + " ");
		}
		return str;
	}
	
	public static <T> StringBuilder formatArray(ArrayList<T[]> list) {
		StringBuilder str = new StringBuilder();
		for (T[] item:list) {
			str.append(formatArray(item));
			str.append("\n");
		}
		return str;
	}
	
	public static void main(String[] args) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[] {"a","b"});
		list.add(new String[] {"c","d"});
		System.out.println(formatArray(list));
	}

}
