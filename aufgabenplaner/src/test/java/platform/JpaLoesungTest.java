package platform;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import l4_dm.DmSchritt;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaLoesungTest {

	/** Probiert einige Operationen von JPA aus. */

	private final static String persistenceUnitName = "aufgabenplaner";
	private final static EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory(persistenceUnitName);
	private final DmSchritt erwateterSchritt = new DmSchritt(){
		@Override
		public Long getId() {return (long)1L;}
		@Override
		public String getTitel() {return "Post abholen";}
		@Override
		public int getRestStunden() {return 0;}
		@Override
		public int getIstStunden() {return 3;}
	};

	@Test
	public void t01() { // 1. Entity persistieren in manueller Transaktion mit
		// "transaction-scoped persistence context" ohne Rollback:
		final EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		final DmSchritt schritt = new DmSchritt();
		schritt.setTitel("Post abholen");
		assertNull(schritt.getId());
		assertFalse(entityManager.contains(schritt));
		// Hierdurch wird schritt zu einer "managed entity":
		entityManager.persist(schritt);
		assertEquals((long) 1L, (long) schritt.getId());
		assertTrue(entityManager.contains(schritt));
		// Hierdurch werden alle "managed entities" in die Datenbank
		// geschrieben:
		transaction.commit();
		// Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
		entityManager.close();
	}

	@Test
	public void t020Rollback() { // 2. Wirkung des rollback ausprobieren:
		final EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		final DmSchritt schritt = new DmSchritt();
		schritt.setTitel("Ein problematischer Schritt");
		entityManager.persist(schritt);
		assertEquals((long) 2L, (long) schritt.getId());
		final Long idOfFailed = schritt.getId();
		// Hierdurch werden alle Ver�nderungen an "managed entities" seit
		// dem letzten commit in der Datenbank r�ckabgewickelt.
		// Im Arbeitsspeicher aber bleiben sie bestehen. Die Transaktion
		// wird beendet.
		transaction.rollback();
		// Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
		entityManager.close();
		// Ab etzt alle Transaktionen redundanzfrei mit untenstehender
		// Hilfsklasse EMTransaction:
		final EMTransaction tx = new EMTransaction();
		final DmSchritt schritt2 = tx.em.find(DmSchritt.class, idOfFailed);
		assertNull(schritt2);
		tx.close(true);
	}
	
	@Test
	public void t021() { // 2.1. was fuer eine ID wird denn jetzt gespeichert?:
		final EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		final EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		final DmSchritt schritt = new DmSchritt();
		schritt.setTitel("Post abholen2");
		// Hierdurch wird schritt zu einer "managed entity":
		entityManager.persist(schritt);
		//obwohl der Schritt mit id=2L gerollbackt wurde, ist die id=2L belegt
		assertEquals((long) 3L, (long) schritt.getId());
		assertEquals("Post abholen2", schritt.getTitel());
		// Hierdurch werden alle "managed entities" in die Datenbank
		// geschrieben:
		transaction.commit();
		// Macht alle verwalteten Entities DETACHED vom Persistenzkontext:
		entityManager.close();
	}

	@Test
	public void t03() { // 3. Entity mit id holen und �ndern:
		EMTransaction tx = new EMTransaction();
		DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
		assertEquals((long) 1L, (long) schritt.getId());
		assertEquals("Post abholen", schritt.getTitel());
		schritt.setRestStunden(0);
		schritt.setIstStunden(2);
		// Die �nderungen sollen durch das commit gespeichert werden:
		tx.close(true);
		// Alle Entities sind jetzt DETACHED: Weitere �nderungen werden
		// nicht mehr automatisch gespeichert:
		assertEquals((long) 1L, (long) schritt.getId());
		// Diese �nderungen werden nicht mehr gespeichert:
		schritt.setIstStunden(1000);
		tx = new EMTransaction();
		schritt = tx.em.find(DmSchritt.class, 1L);
		assertEquals((long) 1L, (long) schritt.getId());
		assertEquals("Post abholen", schritt.getTitel());
		assertEquals(2, schritt.getIstStunden());
		tx.close(true);
	}

	@Test
	public void t04() { // 4. detach und merge pr�fen:
		final DmSchritt schritt;
		{
			final EMTransaction tx = new EMTransaction();
			schritt = tx.em.find(DmSchritt.class, 1L);
			assertEquals((long) 1L, (long) schritt.getId());
			assertEquals("Post abholen", schritt.getTitel());
			assertEquals(2, schritt.getIstStunden());
			tx.close(true);
			// Alle Entities sind jetzt DETACHED: Weitere �nderungen
			// werden nicht mehr automatisch gespeichert:
		}
		// Diese �nderungen werden normalerweise nicht mehr gespeichert:
		schritt.setIstStunden(3);
		{
			final EMTransaction tx = new EMTransaction();
			// Die Entity schritt wieder MANAGED machen. Aktueller Zustand
			// von ihr wird bei Transaktionsende gespeichert:
			tx.em.merge(schritt);
			assertEquals(3, schritt.getIstStunden());
			tx.close(true);
		}

	}

	@Test
	public void t05() { // 5. Entity wieder mit id holen und pr�fen:
		final EMTransaction tx = new EMTransaction();
		final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
		assertEquals((long) 1L, (long) schritt.getId());
		assertEquals("Post abholen", schritt.getTitel());
		assertEquals(3, schritt.getIstStunden());
		tx.close(true);
	}

	@Test
	public void t06() { // 6. Alle Schritte mit Java Persistence Query Language
						// (JPQL) holen:
		final EMTransaction tx = new EMTransaction();
		// TypedQuery<T> wurde in JPA 2 eingef�hrt, um den Cast nach T zu
		// vermeiden.
		final TypedQuery<DmSchritt> q = tx.em.createQuery("SELECT o FROM "
				+ DmSchritt.class.getName() + " o ORDER BY id DESC",
				DmSchritt.class);
		final List<DmSchritt> results = q.getResultList();
		tx.close(true);
		for (final DmSchritt s : results) {
			 assertEquals("der Schritt mit der id "+s.getId()+" equalt erwartererSchritt nicht", s, erwateterSchritt);
		}
	}

	@Test
	public void t07() { // 7. Entity wieder mit id holen und dann l�schen:
		final EMTransaction tx = new EMTransaction();
		final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
		assertEquals((long) 1L, (long) schritt.getId());
		assertEquals("Post abholen", schritt.getTitel());
		tx.em.remove(schritt);
		assertEquals((long) 1L, (long) schritt.getId());
		assertEquals("Post abholen", schritt.getTitel());
		tx.close(true);
	}

	@Test
	public void t08() { // 8. Entity nach L�schen holen:
		final EMTransaction tx = new EMTransaction();
		final DmSchritt schritt = tx.em.find(DmSchritt.class, 1L);
		assertNull(schritt);
		tx.close(true);
	}

	@AfterClass
	public static void after() { // Aufr�umen:
		entityManagerFactory.close();
	}

	/**
	 * Kapselt einen {@link EntityManager} mit einem
	 * "transaction-scoped persistence context" und einer
	 * {@link EntityTransaction} und startet diese Transaktion. Zum Vorgehen bei
	 * manueller Transaktionsverwaltung siehe
	 * https://docs.jboss.org/hibernate/entitymanager
	 * /3.5/reference/en/html/transactions
	 * .html#transactions-demarcation-nonmanaged
	 */
	private class EMTransaction {
		/**
		 * Der {@link EntityManager} mit Transaction Scope f�r die weiteren
		 * Datenzugriffsoperationen.
		 */
		public final EntityManager em = entityManagerFactory
				.createEntityManager();
		private final EntityTransaction transaction = em.getTransaction();
		{
			transaction.begin();
		}

		/**
		 * Schlie�t die {@link EMTransaction} mit entweder commit oder
		 * rollback.
		 * 
		 * @param ok
		 *            die Transaktion war bis zum Ende fehlerfrei => commit,
		 *            andernfalls rollback.
		 */
		public void close(final boolean ok) {
			try {
				if (!transaction.isActive())
					return;
				if (ok) {
					transaction.commit();
				} else {
					transaction.rollback();
				}
			} finally { // wird auch bei Erfolg oder return ausgef�hrt!
				em.close();
			}
		}
	}

}
