package xlong.cell.instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xlong.cell.instance.Instance;
import xlong.util.MyWriter;

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

	@Override
	public void write(String filePath) {
		MyWriter.setFile(filePath, false);
		for (XInstance instance:instances) {
			MyWriter.writeln(instance.toString());
		}
		MyWriter.close();
	}

	@Override
	public void load(String filePath) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String pline,lline;
		while ((pline = in.readLine()) != null) {
			lline = in.readLine();
			XInstance newXInstance = newInstance();
			newXInstance.load(pline, lline);
			this.addInstance(newXInstance);
		}
		in.close();
	}
	
	abstract public XInstance newInstance();
}
