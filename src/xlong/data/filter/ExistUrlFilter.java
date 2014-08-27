package xlong.data.filter;

import xlong.data.SimpleEntity;

public class ExistUrlFilter extends EntityFilter {

	
	public ExistUrlFilter() {
		super(null);
	}
	
	public ExistUrlFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(SimpleEntity en) {
		return en.getUrls() != null;
	}

}
