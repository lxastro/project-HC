package xlong.data.filter;

import xlong.data.Entity;

public abstract class EntityFilter {
	EntityFilter father;
	
	public EntityFilter (EntityFilter father){
		this.father = father;
	}
	
	abstract public boolean filt(Entity en);
	
	public boolean pipeFilt(Entity en) {
		if (father == null) {
			return filt(en);
		} else {
			return filt(en) && father.pipeFilt(en);
		}
	}
}
