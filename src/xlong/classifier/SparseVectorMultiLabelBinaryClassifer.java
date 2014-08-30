package xlong.classifier;

import xlong.cell.SparseVector;
import xlong.cell.instance.SparseVectorMultiLabelInstance;

public class SparseVectorMultiLabelBinaryClassifer extends BinaryClassifier<SparseVectorMultiLabelInstance, SparseVector> {

	public SparseVectorMultiLabelBinaryClassifer(String positiveLabel) {
		super(positiveLabel);
	}

	@Override
	public void train(SparseVectorMultiLabelInstance instance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean classify(SparseVector property) {
		// TODO Auto-generated method stub
		return false;
	}

}
