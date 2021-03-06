package xlong.cell.instances;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import xlong.cell.instance.StringMultiLabelInstance;

public class StringMultiLabelFlatInstances extends FlatInstances<StringMultiLabelInstance> {
	
	public StringMultiLabelFlatInstances(Map<String, TreeSet<String>> StringMap) {
		for (Entry<String, TreeSet<String>> en:StringMap.entrySet()) {
			StringMultiLabelInstance instance = new StringMultiLabelInstance(en.getKey(), en.getValue());
			this.addInstance(instance);
		}
	}

	@Override
	public StringMultiLabelInstance newInstance() {
		return new StringMultiLabelInstance(null, null);
	}


}
