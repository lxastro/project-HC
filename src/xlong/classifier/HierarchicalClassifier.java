package xlong.classifier;

import xlong.data.HierarchicalTree;

public abstract class HierarchicalClassifier extends Classifier {
	public abstract void train(HierarchicalTree htree);
}
