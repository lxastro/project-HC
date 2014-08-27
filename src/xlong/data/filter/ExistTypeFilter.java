package xlong.data.filter;

import xlong.data.SimpleEntity;

public class ExistTypeFilter extends EntityFilter {

	
	public ExistTypeFilter() {
		super(null);
	}
	
	public ExistTypeFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(SimpleEntity en) {
		return en.getTypes() != null;
	}

}
