package xlong.data.filter;

import xlong.data.SimpleEntity;

public class SingleTypeFilter extends EntityFilter {

	public SingleTypeFilter() {
		super(null);
	}
	public SingleTypeFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(SimpleEntity en) {
		return en.getTypes() != null && en.getTypes().size() == 1;
	}

}