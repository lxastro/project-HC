package xlong.cell.instances;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xlong.cell.instance.Instance;

abstract public class FlatInstances<XInstance extends Instance<?>> extends Instances {
	private List<XInstance> instances;
	private Iterator<XInstance> it;
	private List<String> labels;
	
	public FlatInstances (){
		this.instances = new ArrayList<XInstance>();
		this.labels = new ArrayList<String>();
	}
	
	public int size(){
		return instances.size();
	}
	
	public void startIterator(){
		it = instances.iterator();
	}
	
	public boolean hasNext(){
		return it.hasNext();
	}
	
	public XInstance next(){
		return it.next();
	}
	
	public void addInstance(XInstance instance){
		instances.add(instance);
		labels.addAll(instance.getLabel());
	}

	/**
	 * @return the labels
	 */
	public List<String> getLabels() {
		return labels;
	}
}
