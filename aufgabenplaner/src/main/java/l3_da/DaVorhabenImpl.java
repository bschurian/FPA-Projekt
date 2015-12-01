package l3_da;

import java.util.List;

import javax.persistence.EntityManager;

import l4_dm.DmVorhaben;

public class DaVorhabenImpl extends DaGenericImpl<DmVorhaben> implements
		DaVorhaben {

	public DaVorhabenImpl(Class<DmVorhaben> managedClass,
			EntityManager entityManager) {
		super(managedClass, entityManager);
	}
}
