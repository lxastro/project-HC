package xlong.classifier;

import xlong.cell.instances.FlatInstances;

public abstract class FlatClassifier<XInstances extends FlatInstances<?>> extends Classifier {
	public abstract void train(XInstances instances);

}
