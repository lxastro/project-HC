package xlong.cell.instances;

import java.util.List;

import xlong.cell.SparseVector;
import xlong.cell.instance.MultiLabelInstance;

public class FlatSparseVectorMultiLabelInstances extends FlatInstances<MultiLabelInstance<SparseVector>, SparseVector> {

	public FlatSparseVectorMultiLabelInstances(List<MultiLabelInstance<SparseVector>> instances) {
		super(instances);
	}

}
