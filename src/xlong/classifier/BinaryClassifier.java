package xlong.classifier;

import xlong.cell.instance.SingleLabelInstance;

abstract public class BinaryClassifier<T> extends Classifier<SingleLabelInstance<T>, T>{
	public abstract boolean classify(SingleLabelInstance<T> instance);
	public abstract BinaryClassifier<T>[] newClassifiers(int n);
}
