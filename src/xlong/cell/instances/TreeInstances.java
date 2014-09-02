package xlong.cell.instances;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;


import xlong.cell.OntologyTree;
import xlong.cell.instance.Instance;

abstract public class TreeInstances<XInstance extends Instance<?>> extends Instances {
	private List<XInstance> instances;
	private Map<String, TreeInstances<XInstance>> subTreeMap;
	private Map<String, TreeInstances<XInstance>> subNodeMap;
	private String name;
	private Iterator<XInstance> it;
	private Iterator<TreeInstances<XInstance>> subIt;
	private TreeInstances<XInstance> itSubTree;

	public TreeInstances () {
		
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void buildTree (OntologyTree ontology){
		this.name = ontology.getTypeName();
		this.instances = new ArrayList<XInstance>();
		this.subTreeMap = new TreeMap<String, TreeInstances<XInstance>>();
		this.subNodeMap = new TreeMap<String, TreeInstances<XInstance>>();
		this.subNodeMap.put(this.name, this);
		for (OntologyTree subTree:ontology.getSons()){
			Class<? extends TreeInstances> myClass = this.getClass();
			try {
				Constructor<? extends TreeInstances> myConstructor = myClass.getConstructor();
				TreeInstances<XInstance> subTreeInstance = myConstructor.newInstance();
				subTreeInstance.buildTree(subTree);
				this.subTreeMap.put(subTree.getTypeName(), subTreeInstance);
				this.subNodeMap.put(subTree.getTypeName(), subTreeInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.subTreeMap.putAll(this.getSubTree(subTree.getTypeName()).subTreeMap);
		}
	}
	
	protected void add(XInstance instance) {
		this.instances.add(instance);
	}
	
	abstract public void addInstance(XInstance instance);
	
	public int size(){
		int size = instances.size();
		for (Entry<String, TreeInstances<XInstance>> en:subTreeMap.entrySet()){
			size += en.getValue().size();
		}
		return size;
	}
	
	public void startIterator(){
		it = instances.iterator();
		subIt = subNodeMap.values().iterator();
		itSubTree = null;
	}
	
	public boolean hasNext(){
		if (it.hasNext()) {
			return true;
		} else {
			if (itSubTree == null || itSubTree.hasNext() == false) {
				if (subIt.hasNext()) {
					itSubTree = subIt.next();
					itSubTree.startIterator();
					return itSubTree.hasNext();
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
	}
	
	public XInstance next(){
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		if (it.hasNext()) {
			return it.next();
		} else {
			return itSubTree.next();
		}
	}
	
	public TreeInstances<XInstance> getNode(String typeName) {
		return subNodeMap.get(typeName);
	}
	
	public TreeInstances<XInstance> getSubTree(String typeName) {
		return subTreeMap.get(typeName);
	}
	
	public Map<String, TreeInstances<XInstance>> getSubTreeMap() {
		return this.subTreeMap;
	}
	
	public List<XInstance> getInstances() {
		return instances;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
