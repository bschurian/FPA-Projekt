package l3_da;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import l4_dm.DmAufgabe;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

public class DaFactoryForJPA implements DaFactory {

	private final static String persistenceUnitName = "aufgabenplaner";
	private final EntityManagerFactory entityManagerFactory;
	private final EntityManager entityManager;

	public DaFactoryForJPA(final boolean fileBased){
//		try {
			final String persistenceUnitName = "aufgabenplaner";
			final Map<String,String> fileBasedProperties = new TreeMap<String,String>(){{
				 put("javax.persistence.jdbc.url", "jdbc:derby:directory:aufgabe-DB;create=true");
			}};
			this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, fileBased ? fileBasedProperties : null);
			this.entityManager = entityManagerFactory.createEntityManager();
//		}
	}
	
	public DaFactoryForJPA(){
		this(false);
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
