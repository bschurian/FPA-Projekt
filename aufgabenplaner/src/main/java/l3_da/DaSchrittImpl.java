package l3_da;

import javax.persistence.EntityManager;

import l4_dm.DmSchritt;

public class DaSchrittImpl extends DaGenericImpl<DmSchritt> implements DaSchritt{

	public DaSchrittImpl(final Class<DmSchritt> managedClass, final EntityManager entityManager) {
		super(managedClass, entityManager);
	}
	
}
