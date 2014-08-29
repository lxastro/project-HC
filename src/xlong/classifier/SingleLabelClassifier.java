package xlong.classifier;

import xlong.cell.instance.SingleLabelInstance;

abstract public class SingleLabelClassifier<T> extends Classifier<SingleLabelInstance<T>, T>{
	public abstract String classify(SingleLabelInstance<T> instance);
}
