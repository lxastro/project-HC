package xlong.cell.instances;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xlong.cell.instance.Instance;

abstract public class FlatInstances<XInstance extends Instance<T>, T> extends Instances<XInstance, T> {
	private List<XInstance> instances;
	private Iterator<XInstance> it;
	
	public FlatInstances (List<XInstance> instances){
		this.instances = instances;
	}
	
	public FlatInstances (){
		this(new ArrayList<XInstance>());
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
	}
}
