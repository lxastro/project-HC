package xlong.data.filter;

import xlong.data.SimpleEntity;

public class MultipleTypeFilter extends EntityFilter {

	public MultipleTypeFilter() {
		super(null);
	}
	public MultipleTypeFilter(EntityFilter father) {
		super(father);
	}

	@Override
	public boolean filt(SimpleEntity en) {
		return en.getTypes() != null && en.getTypes().size() > 1;
	}

}
