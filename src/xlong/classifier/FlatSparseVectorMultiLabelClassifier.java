package xlong.classifier;

import java.util.Collection;

import xlong.cell.SparseVector;
import xlong.cell.instance.MultiLabelInstance;
import xlong.cell.instances.Instances;
import xlong.cell.instances.FlatSparseVectorMultiLabelInstances;

public class FlatSparseVectorMultiLabelClassifier extends MultiLabelClassifier<SparseVector> {

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
