package xlong.cell.instances;

import xlong.cell.OntologyTree;
import xlong.cell.instance.SparseVectorMultiLabelInstance;

public class SparseVectorMultiLabelTreeInstances extends TreeInstances<SparseVectorMultiLabelInstance> {

	public SparseVectorMultiLabelTreeInstances() {
		
	}
	
	public SparseVectorMultiLabelTreeInstances(OntologyTree ontology) {
		super();
		this.buildTree(ontology);
	}

	@Override
	public void addInstance(SparseVectorMultiLabelInstance instance) {
		for (String label:instance.getLabel()) {
			if (this.getNode(label) != null) {
				this.getNode(label).add(instance);
			}
		}
	}

}
