package xlong.cell.instance;

public class SingleLabelInstance<T> extends Instance<T> {
	private String label;
	public SingleLabelInstance(T property, String label){
		setProperty(property);
		this.label = label;
	}
	public String getLabel(){
		return label;
	}
}
