package xlong.cell.instances;

import xlong.cell.instance.SparseVectorMultiLabelInstance;

public class SparseVectorMultiLabelFlatInstances extends FlatInstances<SparseVectorMultiLabelInstance> {
	
	public SparseVectorMultiLabelFlatInstances() {
	}

	@Override
	public SparseVectorMultiLabelInstance newInstance() {
		return new SparseVectorMultiLabelInstance(null, null);
	}
}
