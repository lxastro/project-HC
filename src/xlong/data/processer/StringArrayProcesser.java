package xlong.data.processer;

public abstract class StringArrayProcesser {
	StringArrayProcesser father;
	
	public StringArrayProcesser(StringArrayProcesser father){
		this.father = father;
	}
	abstract public String[] processTriple(String[] in);

	public String[] pipeProcessTriple(String[] in) {
		if (father == null) {
			return processTriple(in);
		} else {
			return processTriple(father.pipeProcessTriple(in));
		}
	}
}
