package xlong.data.filter;

import xlong.data.Entity;

public class ExistUrlFilter extends EntityFilter {

	
	public ExistUrlFilter() {
		super(null);
	}
	
	public ExistUrlFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(Entity en) {
		return en.getUrls() != null;
	}

}
