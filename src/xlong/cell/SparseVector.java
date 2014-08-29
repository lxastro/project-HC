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
		}
	}
	
	public int[] getIndexs() {
		return indexs;
	}
	
	public double[] getValues() {
		return values;
	}
}
