package xlong.data.processer;

public class SimplifyProcesser extends StringArrayProcesser {
	
	
	public SimplifyProcesser() {
		super(null);
	}
	public SimplifyProcesser(StringArrayProcesser father) {
		super(father);
	}

	@Override
	public String[] processTriple(String[] in) {
		return new String[] {in[0].substring(28), in[1]};
	}
}
