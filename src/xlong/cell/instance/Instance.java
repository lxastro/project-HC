package xlong.cell.instance;

import java.util.Collection;


public abstract class Instance<T> {
	
	protected Instance(){
		
	}
	
	public void load(String propertyLine, String labelLine){
		this.loadProperty(propertyLine);
		this.loadLabel(labelLine);
	}
	
	private T property;
	public T getProperty(){
		return property;
	}
	public void setProperty(T property){
		this.property = property;
	}
	abstract public Collection<String> getLabel();
	
	abstract public String propertyString();
	
	abstract public String labelString();
	
	abstract public void loadProperty(String line);
	
	abstract public void loadLabel(String line);
	
	@Override
	public String toString(){
		return propertyString() + "\n" + labelString();
	}
}
