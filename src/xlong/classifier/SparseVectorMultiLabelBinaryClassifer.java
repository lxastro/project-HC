package xlong.classifier;

import xlong.cell.SparseVector;
import xlong.cell.instance.SparseVectorMultiLabelInstance;

public abstract class SparseVectorMultiLabelBinaryClassifer extends BinaryClassifier<SparseVectorMultiLabelInstance, SparseVector> {

	abstract public SparseVectorMultiLabelBinaryClassifer[] getInstances(int cnt);

}
