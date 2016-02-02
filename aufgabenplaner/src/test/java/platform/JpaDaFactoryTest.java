package platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import l3_da.DaAufgabe;
import l3_da.DaFactory;
import l3_da.DaFactoryForJPA;
import l3_da.DaSchritt;
import l3_da.DaVorhaben;
import l4_dm.DmAufgabe;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaDaFactoryTest {

	/** Probiert einige Operationen von JPA aus. */

	private final static DaFactory daFactory = new DaFactoryForJPA();
	
	private DaSchritt daSchritt = daFactory.getSchrittDA();
	private DaVorhaben daVorhaben = daFactory.getVorhabenDA();
	private DaAufgabe daAufgabe = daFactory.getAufgabeDA();
	
	private final DmSchritt erwateterSchritt = new DmSchritt() {
		@Override
		public Long getId() {
			return (long) 1L;
		}

		@Override
		public String getTitel() {
			return "Post abholen";
		}

		@Override
		public int getRestStunden() {
			return 0;
		}

		@Override
		public int getIstStunden() {
			return 3;
		}
	};

	@Test
	public void t010Comm() { // 1. Entity persistieren in autom. Transaktion mit
		// "transaction-scoped persistence context" ohne Rollback:
		daFactory.beginTransaction();
		final DmSchritt schritt = new DmSchritt();
		schritt.setTitel("Post abholen");
		daSchritt.save(schritt);//persistiert
		assertEquals( 1L, schritt.getId().longValue());
		daFactory.endTransaction(true);//commitet
	}
	
	@Test
	public void t012ReComm() { // rausholen und wieder speichern
		daFactory.beginTransaction();
		final DmSchritt schritt = daSchritt.find(1L);
		daSchritt.save(schritt);
		daFactory.endTransaction(true);//commitet
	}

	@Test
	public void t020Rollback() { // 2. Wirkung des rollback ausprobieren:
		daFactory.beginTransaction();
		final DmSchritt schritt = new DmSchritt();
		schritt.setTitel("XYASD");
		daSchritt.save(schritt);//persistiert
		assertEquals( Long.valueOf(2), schritt.getId());
		daFactory.endTransaction(false);//rollbacked
	}

	@Test
	public void t021Rollback() { // 2. Wirkung des rollback ausprobieren:
		daFactory.beginTransaction();
		final DmSchritt schritt = daSchritt.find( 2L);
		assertNull(schritt);
		daFactory.endTransaction(false);//commitet
	}

	@Test
	public void t030findAllVorber() { // 3.findAll
		daFactory.beginTransaction();
		DmVorhaben vorhaben = new DmVorhaben();
		vorhaben.setTitel("Gehen");
		daVorhaben.save(vorhaben);
		assertEquals(3L,vorhaben.getId().longValue());
		daFactory.endTransaction(true);//commitet

	}
 
	@Test
	public void t031findAll() { // 3.findAll
		daFactory.beginTransaction();
		final List<DmAufgabe> aList = daAufgabe.findAll();
		for(DmAufgabe dA : aList){
			System.out.println(dA.getClass());
			System.out.println(dA.getTitel());
		}
		assertEquals(aList.get(0).getTitel(), "Post abholen");
		assertEquals(aList.get(1).getTitel(), "Gehen");
		daFactory.endTransaction(false);//commitet

	}

	@Test
	public void t031findAllS() { // 3.findAll
		daFactory.beginTransaction();
		final List<DmSchritt> aList = daSchritt.findAll();
		assertEquals(aList.get(0).getTitel(), "Post abholen");
		daFactory.endTransaction(false);//commitet

	}

	@Test
	public void t04() { // 4. detach und merge prï¿½fen:
		final DmSchritt schritt;
		{
		}
		{
		}

	}

	@Test
	public void t05() { // 5. Entity wieder mit id holen und prï¿½fen:
	}

	@Test
	public void t06() { // 6. Alle Schritte mit Java Persistence Query Language
						// (JPQL) holen:
	}

	@Test
	public void t07() { // 7. Entity wieder mit id holen und dann lï¿½schen:
	}

	@Test
	public void t08() { // 8. Entity nach Lï¿½schen holen:
	}

	@AfterClass
	public static void after() { // Aufrï¿½umen:
		
	}

}
