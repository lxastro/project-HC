package xlong.instance;

/**
 * 
 * @author longx
 *
 */
public class Text implements Property {
	/** */
	private final String text;
	
	/**
	 * @param s the text
	 */
	Text(final String s) {
		this.text = s;
	}
	
	/**
	 * @return text;
	 */
	public final String getText() {
		return text;
	}
	
	@Override
	public final String toString() {
		return text;
	}

	@Override
	public final String getOneLineString() {
		return text;
	}
	
}
