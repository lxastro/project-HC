package xlong.cell.instance;

import java.util.Collection;

import xlong.cell.SparseVector;

public class SparseVectorMultiLabelInstance extends MultiLabelInstance<SparseVector> {

	public SparseVectorMultiLabelInstance(SparseVector property, Collection<String> labels) {
		super(property, labels);
	}

}
