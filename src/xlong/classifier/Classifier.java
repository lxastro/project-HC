package xlong.classifier;

import java.util.Map;

public abstract class Classifier {
	public abstract String classify(Map<Integer, Double> vector);
}
