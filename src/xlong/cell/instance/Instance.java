package xlong.cell.instance;

public abstract class Instance<T> {
	private T property;
	public T getProperty(){
		return property;
	}
	public void setProperty(T property){
		this.property = property;
	}
}
