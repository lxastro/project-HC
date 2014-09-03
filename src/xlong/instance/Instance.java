package xlong.instance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Instance to store property and labels.
 * @param <P> the Class of Instance property.
 */
public class Instance<P> implements InstanceComponent {
	/** Property of a instance. */
	private final P property;
	/** Labels of a instance. */
	private final TreeSet<Label> labels;
	
	/** Labels pool. */
	private static Map<String, TreeSet<Label>> labelsPool = new HashMap<String, TreeSet<Label>>();
	/**
	 * @param property the property
	 * @param labels the labels
	 */
	public Instance(final P property, final Collection<Label> labels) {
		this.property = property;
		
		String str = Labels.labelsToString(labels);
		TreeSet<Label> labelSet = labelsPool.get(str);
		if (labelSet != null) {
			this.labels = labelSet;
		} else {
			this.labels = new TreeSet<Label>(labels);
			labelsPool.put(str, this.labels);
		}
	}
	
	@Override
	public final int countInstance() {
		return 1;
	}

	@Override
	public final boolean isLeaf() {
		return true;
	}

	/**
	 * @return the property
	 */
	public final P getProperty() {
		return property;
	}

	/**
	 * @return the labels
	 */
	public final Label[] getLabels() {
		return labels.toArray(new Label[0]);
	}


}
