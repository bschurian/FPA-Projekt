package l3_da;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import l4_dm.DmAufgabe;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

import org.hibernate.ejb.EntityManagerImpl;

public class DaFactoryForJPA implements DaFactory {
	
	private final static String persistenceUnitName = "aufgabenplaner";
	private final EntityManagerFactory entityManagerFactory ;
	private final EntityManager entityManager;
	
	public DaFactoryForJPA(){ 
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
	}
	
	@Override
	public DaAufgabe getAufgabeDA() {
		return new DaAufgabeImpl(DmAufgabe.class, entityManager);
	}

	@Override
	public DaSchritt getSchrittDA() {
		return new DaSchrittImpl(DmSchritt.class, entityManager);
	}

	@Override
	public DaVorhaben getVorhabenDA() {
		return new DaVorhabenImpl(DmVorhaben.class, entityManager);
	}

	@Override
	public void beginTransaction() {
		entityManager.getTransaction().begin();
	}

	@Override
	public void endTransaction(boolean ok) {
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			if (!transaction.isActive())
				return;
			if (ok) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
		} finally { // wird auch bei Erfolg oder return ausgef�hrt!
			entityManager.clear();
		}

	}

}
