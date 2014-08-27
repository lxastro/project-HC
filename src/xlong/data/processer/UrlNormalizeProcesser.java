package xlong.data.processer;

public class UrlNormalizeProcesser extends StringArrayProcesser {

	public UrlNormalizeProcesser() {
		super(null);
	}
	public UrlNormalizeProcesser(StringArrayProcesser father) {
		super(father);
	}

	@Override
	public String[] processTriple(String[] in) {
		String[] out = new String[in.length];
		for (int i = 0; i < in.length; i++) {
			out[i] = UrlNormalizer.normalize(in[i]);
		}
		return out;
	}
}
