package xlong.classifier;

import xlong.cell.instances.TreeInstances;

public abstract class TreeClassifier<XInstances extends TreeInstances<?>> extends Classifier {
	public abstract void train(XInstances instances);

}
