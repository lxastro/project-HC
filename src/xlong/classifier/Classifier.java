package xlong.classifier;

import xlong.cell.instance.Instance;
import xlong.cell.instances.Instances;

public abstract class Classifier<XInstance extends Instance<T>, T> {
	public abstract void train(Instances<XInstance, T> instances);
}
