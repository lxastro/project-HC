package xlong.cell.instance;

import java.util.Collection;

public class MultiLabelInstance<T> extends Instance<T> {
	private Collection<String> labels;
	public MultiLabelInstance(T property, Collection<String> labels){
		setProperty(property);
		this.labels = labels;
	}
	public Collection<String> getLabels(){
		return labels;
	}
}
