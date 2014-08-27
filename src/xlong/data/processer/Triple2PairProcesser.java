package xlong.data.processer;

public class Triple2PairProcesser extends StringArrayProcesser {

	public Triple2PairProcesser() {
		super(null);

	}
	public Triple2PairProcesser(StringArrayProcesser father) {
		super(father);
	}

	@Override
	public String[] processTriple(String[] in) {
		return new String[] {in[0], in[2]};
	}

}
