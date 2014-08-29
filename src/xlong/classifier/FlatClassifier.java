package xlong.classifier;

import java.util.HashMap;
import java.util.HashSet;

public abstract class FlatClassifier extends Classifier {
	public abstract void train(HashMap<String, HashSet<String>> typeMap);
}
