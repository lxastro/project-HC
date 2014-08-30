package xlong.cell.instance;

import java.util.Collection;

public abstract class Instance<T> {
	private T property;
	public T getProperty(){
		return property;
	}
	public void setProperty(T property){
		this.property = property;
	}
	abstract public Collection<String> getLabel();
}
