package l3_da;

import javax.persistence.EntityManager;

import l4_dm.DmAufgabe;

public class DaAufgabeImpl extends DaGenericImpl<DmAufgabe> implements
		DaAufgabe {

	public DaAufgabeImpl(Class<DmAufgabe> managedClass, EntityManager entityManager) {
		super(managedClass, entityManager);
	}
	
}
