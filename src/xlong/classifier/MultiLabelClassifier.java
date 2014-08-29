package xlong.classifier;

import java.util.Collection;

import xlong.cell.instance.MultiLabelInstance;

public abstract class MultiLabelClassifier<T> extends Classifier<MultiLabelInstance<T>, T> {
	public abstract Collection<String> classify(MultiLabelInstance<T> instance);

}
