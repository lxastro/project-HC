package xlong.cell;

import java.util.Map;
import java.util.Map.Entry;

public class SparseVector {
	private int size;
	private int[] indexs;
	private double[] values;
	
	public SparseVector (Map<Integer, Double> vector) {
		size = vector.size();
		indexs = new int[size];
		values = new double[size];
		int i = 0;
		for (Entry<Integer, Double> en:vector.entrySet()) {
			indexs[i] = en.getKey();
			values[i] = en.getValue();
			i++;
		}
	}
	
	public SparseVector(String line) {
		String[] parts = line.split(" ");
		size = parts.length/2;
		indexs = new int[size];
		values = new double[size];
		for (int i = 0; i < size; i++){
			indexs[i] = Integer.parseInt(parts[2 * i]);
			values[i] = Double.parseDouble(parts[2 * i + 1]);
		}
	}
	
	public int[] getIndexs() {
		return indexs;
	}
	
	public double[] getValues() {
		return values;
	}
	
	@Override
	public String toString(){
		String str = indexs[0] + " " + values[0];
		for (int i = 1; i < size; i++){
			str += " " + indexs[i] + " " + values[i];
		}
		return str;
	}
}
