package xlong.cell.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract public class SingleLabelInstance<T> extends Instance<T> {
	private String label;
	public SingleLabelInstance(T property, String label){
		setProperty(property);
		this.label = label;
	}

	public Collection<String> getLabel(){
		List<String> labels = new ArrayList<String>();
		labels.add(label);
		return labels;
	}
	
	@Override
	public String labelString() {
		return label;
	}
	
	@Override
	public void loadLabel(String line) {
		this.label = line;
	}
}
