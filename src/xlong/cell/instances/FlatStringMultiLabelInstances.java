package xlong.cell.instances;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import xlong.cell.instance.MultiLabelInstance;

public class FlatStringMultiLabelInstances extends FlatInstances<MultiLabelInstance<String>, String> {
	public FlatStringMultiLabelInstances(List<MultiLabelInstance<String>> instances) {
		super(instances);
	}
	
	public FlatStringMultiLabelInstances() {
		super();
	}
	
	public FlatStringMultiLabelInstances(Map<String, TreeSet<String>> StringMap) {
		super();
		for (Entry<String, TreeSet<String>> en:StringMap.entrySet()) {
			MultiLabelInstance<String> instance = new MultiLabelInstance<String>(en.getKey(), en.getValue());
			this.addInstance(instance);
		}
	}

}
