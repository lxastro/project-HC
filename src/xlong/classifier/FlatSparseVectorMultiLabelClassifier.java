package xlong.classifier;

import java.util.Collection;
import java.util.HashMap;

import xlong.cell.SparseVector;
import xlong.cell.instance.MultiLabelInstance;
import xlong.cell.instances.Instances;
import xlong.cell.instances.FlatSparseVectorMultiLabelInstances;

public class FlatSparseVectorMultiLabelClassifier extends MultiLabelClassifier<SparseVector> {
	
	private int cntLabels;
	private String[] lebals;
	private BinaryClassifier<SparseVector>[] binaryClassifiers;
	private BinaryClassifier<SparseVector> binaryClassifier;
	
	public FlatSparseVectorMultiLabelClassifier(BinaryClassifier<SparseVector> binaryClassifier){
		this.binaryClassifier = binaryClassifier;
	}
	
	@Override
	public Collection<String> classify(MultiLabelInstance<SparseVector> instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train(Instances<MultiLabelInstance<SparseVector>, SparseVector> instances) {
		
		// TODO Auto-generated method stub
		
	}
	
	public void main(){
		FlatSparseVectorMultiLabelInstances instances = new FlatSparseVectorMultiLabelInstances(null);
		train(instances);
	}
	
}
