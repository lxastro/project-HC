package xlong.data.filter;

import xlong.data.Entity;

public class ExistTypeFilter extends EntityFilter {

	
	public ExistTypeFilter() {
		super(null);
	}
	
	public ExistTypeFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(Entity en) {
		return en.getTypes() != null;
	}

}
