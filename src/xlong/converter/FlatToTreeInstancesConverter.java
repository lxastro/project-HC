package xlong.converter;

import xlong.cell.OntologyTree;
import xlong.cell.instance.Instance;
import xlong.cell.instances.FlatInstances;
import xlong.cell.instances.TreeInstances;

public class FlatToTreeInstancesConverter {
	
	public static <XInstance extends Instance<?>> void convert(FlatInstances<XInstance> flat, TreeInstances<XInstance> tree, OntologyTree ontology){
		tree.buildTree(ontology);
		flat.startIterator();
		while (flat.hasNext()) {
			tree.addInstance(flat.next());
		}
	}
	
	public static void main() {
		
	}
}
