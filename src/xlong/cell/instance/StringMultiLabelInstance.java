package xlong.cell.instance;

import java.util.Collection;


public class StringMultiLabelInstance extends MultiLabelInstance<String> {

	public StringMultiLabelInstance(String property, Collection<String> labels) {
		super(property, labels);
	}

	@Override
	public String propertyString() {
		return getProperty();
	}

	@Override
	public void loadProperty(String line) {
		setProperty(line);
	}

}
