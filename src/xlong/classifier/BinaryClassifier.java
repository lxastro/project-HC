package xlong.classifier;

import xlong.cell.instance.Instance;

public abstract class BinaryClassifier<XInstance extends Instance<T>, T> extends Classifier {
	
	private String positiveLabel;
	
	public void setPositiveLabel(String label) {
		this.positiveLabel = label;
	}
	
	public String getPositiveLabel() {
		return this.positiveLabel;
	}
	
	public boolean isPositive(String label) {
		return this.positiveLabel == label;
	}
	
	public abstract void train(XInstance instance);
	
	public abstract boolean classify(T property);
	
	public abstract double score(T property);


}
