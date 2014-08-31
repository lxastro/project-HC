package xlong.cell.instance;

import java.util.Arrays;
import java.util.Collection;

abstract public class MultiLabelInstance<T> extends Instance<T> {
	private Collection<String> labels;
	public MultiLabelInstance(T property, Collection<String> labels){
		setProperty(property);
		this.labels = labels;
	}

	@Override
	public Collection<String> getLabel() {
		return labels;
	}


	@Override
	public String labelString() {
		String str = "";
		boolean first = true;
		for (String label:labels){
			if (!first) {
				str += " ";
			} else {
				first =false;
			}
			str += label;
		}
		return str;
	}

	@Override
	public void loadLabel(String line) {
		String[] parts = line.split(" ");
		labels = Arrays.asList(parts);
	}
}
