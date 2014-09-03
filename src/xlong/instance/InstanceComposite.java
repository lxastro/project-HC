package xlong.instance;

import java.util.Vector;

/**
 * 
 *
 */
public class InstanceComposite implements InstanceComponent {
	/** */
	private final Vector<InstanceComponent> components;
	/** */
	private final Label label;
	
	/**
	 * @param label the label
	 */
	public InstanceComposite(final Label label) {
		components = new Vector<InstanceComponent>();
		this.label = label;
	}
	
	@Override
	public final int countInstance() {
		int size = 0;
		for (InstanceComponent component:components) {
			size += component.countInstance();
		}
		return size;
	}

	@Override
	public final boolean isLeaf() {
		return false;
	}
	
	/**
	 * 
	 * @param instance instance to add
	 * @return success or not
	 */
	public final boolean addInstance(final Instance<?> instance) {
		// TODO
		return false;
	}

}
