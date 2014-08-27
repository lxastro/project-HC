package xlong.data.filter;

import xlong.data.SimpleEntity;

public abstract class EntityFilter {
	EntityFilter father;
	
	public EntityFilter (EntityFilter father){
		this.father = father;
	}
	
	abstract public boolean filt(SimpleEntity en);
	
	public boolean pipeFilt(SimpleEntity en) {
		if (father == null) {
			return filt(en);
		} else {
			return filt(en) && father.pipeFilt(en);
		}
	}
}
