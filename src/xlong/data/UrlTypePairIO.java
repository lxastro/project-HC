package xlong.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import xlong.util.MyWriter;

public class UrlTypePairIO {
	public static ArrayList<String[]> read(String filePath) throws IOException {
		ArrayList<String[]> pairs = new ArrayList<String[]>();
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String line;
		while ((line = in.readLine()) != null) {
			String[] pair = line.split(" ");
			if (pair.length == 2) {
				pairs.add(pair);
			}
		}
		in.close();
		return pairs;
	}
	
	public static void write(ArrayList<String[]> pairs, String filePath) {
		MyWriter.setFile(filePath, false);
		for (String[] pair:pairs) {
			MyWriter.writeln(pair[0] + " " +pair[1]);
		}
		MyWriter.close();
	}
}
