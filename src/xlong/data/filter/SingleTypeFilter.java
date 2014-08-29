package xlong.data.filter;

import xlong.data.Entity;

public class SingleTypeFilter extends EntityFilter {

	public SingleTypeFilter() {
		super(null);
	}
	public SingleTypeFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(Entity en) {
		return en.getTypes() != null && en.getTypes().size() == 1;
	}

}
