package platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.List;

import l2_lg.LgSession;
import l2_lg.LgSession.VorhabenRekursionExc;
import l2_lg.LgSessionImpl;
import l3_da.DaAufgabe;
import l3_da.DaFactory;
import l3_da.DaFactoryForJPA;
import l3_da.DaSchritt;
import l3_da.DaVorhaben;
import l4_dm.DmAufgabe;
import l4_dm.DmAufgabeStatus;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaLgSessionTest {

	/** Probiert einige Operationen von JPA aus. */

	private final static DaFactory daFactory = new DaFactoryForJPA();
	final LgSession lgSession = new LgSessionImpl(daFactory);

	private DaSchritt daSchritt = daFactory.getSchrittDA();
	private DaVorhaben daVorhaben = daFactory.getVorhabenDA();
	private DaAufgabe daAufgabe = daFactory.getAufgabeDA();

	/*
	 * @Test public void t000Vor() { // Vorbereitung List<DmAufgabe> aufgaben =
	 * new ArrayList<DmAufgabe>();
	 * 
	 * DmVorhaben v1 = new DmVorhaben(); v1.setTitel("Vorhaben nummer 1");
	 * v1.setBeschreibung("rararararara"); v1.setTitel("1234567890123");
	 * 
	 * DmSchritt s1 = new DmSchritt(); s1.setTitel("Erster Schritt");
	 * s1.setBeschreibung("Dies ist der erste Schritt. unerledigt  und neu");
	 * s1.setRestStunden(11); s1.setIstStunden(1);
	 * 
	 * DmVorhaben v2 = new DmVorhaben(); v2.setTitel("Vorhaben nummer 2");
	 * v2.setBeschreibung("lalalala"); v2.setTitel("ramrarmara");
	 * 
	 * DmSchritt s2 = new DmSchritt(); s2.setTitel("zwota Schritt");
	 * s2.setBeschreibung("Dies ist der 2 Schritt. unerledigt  und neu");
	 * s2.setRestStunden(12); s2.setIstStunden(2); s2.setGanzes(v2);
	 * 
	 * DmSchritt s3 = new DmSchritt(); s3.setTitel("Erster Schritt");
	 * s3.setBeschreibung("Dies ist der 3 Schritt. erledigt");
	 * s3.setRestStunden(13); s3.setIstStunden(3); s3.setErledigtZeitpunkt(new
	 * java.sql.Date(420)); s3.setGanzes(v2);
	 * 
	 * DmVorhaben v3 = new DmVorhaben(); v3.setTitel("Vorhaben nummer 3");
	 * v3.setBeschreibung("lalalala"); v3.setTitel("ramrarmara");
	 * 
	 * DmSchritt s4 = new DmSchritt(); s4.setTitel("quarta Schritt");
	 * s4.setBeschreibung("Dies ist der 4 Schritt. unerledigt  und neu");
	 * s4.setRestStunden(1400); s4.setIstStunden(400); s4.setGanzes(v3);
	 * 
	 * DmVorhaben v4 = new DmVorhaben(); v4.setTitel("Vorhaben nummer 4");
	 * v4.setBeschreibung("lalalala"); v4.setTitel("ramrarmara");
	 * v4.setGanzes(v3);
	 * 
	 * DmSchritt s5 = new DmSchritt(); s5.setTitel("quint Schritt");
	 * s5.setBeschreibung("Dies ist der 5 Schritt. unerledigt  und neu");
	 * s5.setRestStunden(1500); s5.setIstStunden(500); s5.setGanzes(v4);
	 * 
	 * DmSchritt s6 = new DmSchritt(); s6.setTitel("sexta Schritt");
	 * s6.setBeschreibung("Dies ist der 6 Schritt. erledigt");
	 * s6.setRestStunden(1600); s6.setIstStunden(600);
	 * s6.setErledigtZeitpunkt(new java.sql.Date(420)); s6.setGanzes(v4);
	 * 
	 * DmVorhaben v5 = new DmVorhaben(); v5.setTitel("Vorhaben nummer 5");
	 * v5.setBeschreibung("lalalala"); v5.setTitel("ramrarmara");
	 * 
	 * DmSchritt s7 = new DmSchritt(); s7.setTitel("sept Schritt");
	 * s7.setBeschreibung("Dies ist der 7 Schritt. unerledigt  und neu");
	 * s7.setRestStunden(1700); s7.setIstStunden(700); s7.setGanzes(v5);
	 * 
	 * DmSchritt s8 = new DmSchritt(); s8.setTitel("Erster Schritt");
	 * s8.setBeschreibung("Dies ist der 8 Schritt. unerledigt n neu");
	 * s8.setRestStunden(1800); s8.setIstStunden(800); s8.setGanzes(v5);
	 * 
	 * assertEquals(1, 1); }
	 */
	@Test
	public void t010speich() { // speichern testen
		DmVorhaben v1 = new DmVorhaben();
		v1.setTitel("Vorhaben nummer 1");
		v1.setBeschreibung("rararararara");
		v1.setTitel("1234567890123");
		v1.setEndTermin(new Date(new Double(2.2075e+14).longValue()));

		DmSchritt s1 = new DmSchritt();
		s1.setTitel("Erster Schritt");
		s1.setBeschreibung("Dies ist der erste Schritt. unerledigt  und neu");
		s1.setRestStunden(11);
		s1.setIstStunden(1);

		lgSession.speichern(v1);
		lgSession.speichern(s1);

		assertEquals(Long.valueOf(1), v1.getId());
		assertEquals(Long.valueOf(2), s1.getId());
	}

	@Test
	public void t011doppeltspeich() { // rausholen,veraendern und wieder
										// reinspeichern testen
		DmVorhaben v1 = (DmVorhaben) lgSession
				.obersteAufgabenUndAlleVorhabenLiefern().aufgaben.get(0);
		assertEquals(Long.valueOf(1), v1.getId());
		lgSession.speichern(v1);
		assertEquals(Long.valueOf(1), v1.getId());
	}

	@Test
	public void t020erle() { // schritt erledigen und vorherige Daten veraendern
		DmSchritt s1 = (DmSchritt) lgSession
				.obersteAufgabenUndAlleVorhabenLiefern().aufgaben.get(1);
		assertEquals(DmAufgabeStatus.inBearbeitung, s1.getStatus());
		lgSession.schrittErledigen(s1);
		assertEquals(DmAufgabeStatus.erledigt, s1.getStatus());
		assertEquals(
				DmAufgabeStatus.inBearbeitung,
				lgSession.obersteAufgabenUndAlleVorhabenLiefern().aufgaben.get(
						1).getStatus());
	}

	@Test
	public void t030alleobe() { // einfacher Baum 1 root mit 2 leaves
		DmVorhaben v2 = new DmVorhaben();
		v2.setTitel("Vorhaben nummer 2");
		v2.setBeschreibung("lalalala");
		v2.setEndTermin(new Date(new Double(2.2075e+14).longValue()));

		DmSchritt s2 = new DmSchritt();
		s2.setTitel("zwota Schritt");
		s2.setBeschreibung("Dies ist der 2 Schritt. erledigt");
		s2.setRestStunden(12);
		s2.setIstStunden(2);
		s2.setGanzes(v2);
		s2.setErledigtZeitpunkt(new java.sql.Date(420));

		DmSchritt s3 = new DmSchritt();
		s3.setTitel("Erster Schritt");
		s3.setBeschreibung("Dies ist der 3 Schritt. erled");
		s3.setRestStunden(13);
		s3.setIstStunden(3);
		s3.setGanzes(v2);
		s3.setErledigtZeitpunkt(new java.sql.Date(420));

		lgSession.speichern(v2);
		lgSession.speichern(s2);
		lgSession.speichern(s3);

		List<DmAufgabe> list = lgSession
				.obersteAufgabenUndAlleVorhabenLiefern().aufgaben;
		DmVorhaben root = (DmVorhaben) list.get(2);

		assertEquals(3, list.size());

		assertEquals(2, root.getAnzahlTeile());
		assertEquals(25, root.getRestStunden());
		assertEquals(5, root.getIstStunden());
		assertEquals(DmAufgabeStatus.erledigt, root.getStatus());
	}

	@Test
	public void t031Baum() { // komplexer baum Root -> 1 leaf & ein Node -> 2
								// leaves
		DmVorhaben v3 = new DmVorhaben();
		v3.setTitel("Vorhaben nummer 3");
		v3.setBeschreibung("lalalala");
		v3.setTitel("ramrarmara");
		v3.setEndTermin(new Date(new Double(2.2075e+14).longValue()));

		DmSchritt s4 = new DmSchritt();
		s4.setTitel("quarta Schritt");
		s4.setBeschreibung("Dies ist der 4 Schritt. unerledigt  und neu");
		s4.setRestStunden(1400);
		s4.setIstStunden(400);
		s4.setGanzes(v3);

		DmVorhaben v4 = new DmVorhaben();
		v4.setTitel("Vorhaben nummer 4");
		v4.setBeschreibung("lalalala");
		v4.setTitel("ramrarmara");
		v4.setGanzes(v3);
		v4.setEndTermin(new Date(new Double(2.2075e+14).longValue()));

		DmSchritt s5 = new DmSchritt();
		s5.setTitel("quint Schritt");
		s5.setBeschreibung("Dies ist der 5 Schritt. erledigt");
		s5.setRestStunden(1500);
		s5.setIstStunden(500);
		s5.setErledigtZeitpunkt(new java.sql.Date(420));
		s5.setGanzes(v4);

		DmSchritt s6 = new DmSchritt();
		s6.setTitel("sexta Schritt");
		s6.setBeschreibung("Dies ist der 6 Schritt. erledigt");
		s6.setRestStunden(1600);
		s6.setIstStunden(600);
		s6.setErledigtZeitpunkt(new java.sql.Date(420));
		s6.setGanzes(v4);

		lgSession.speichern(v3);
		lgSession.speichern(v4);
		lgSession.speichern(s4);
		lgSession.speichern(s5);
		lgSession.speichern(s6);

		List<DmAufgabe> list = lgSession
				.obersteAufgabenUndAlleVorhabenLiefern().aufgaben;
		DmVorhaben root = (DmVorhaben) list.get(3);

		assertEquals(4, list.size());

		assertEquals(2, root.getAnzahlTeile());
		assertEquals(4500, root.getRestStunden());
		assertEquals(1500, root.getIstStunden());
		assertEquals(DmAufgabeStatus.inBearbeitung, root.getStatus());
	}

//	 @Test
//	 public void t030rekfe() { // Rekursionsfehler
//	 DmVorhaben v3 = new DmVorhaben();
//	 v3.setTitel("Vorhaben nummer 3");
//	 v3.setBeschreibung("lalalala");
//	 v3.setTitel("ramrarmara");
//	 v3.setEndTermin(new Date(new Double(2.2075e+14).longValue()));
//	
//	 DmSchritt s4 = new DmSchritt();
//	 s4.setTitel("quarta Schritt");
//	 s4.setBeschreibung("Dies ist der 4 Schritt. unerledigt  und neu");
//	 s4.setRestStunden(1400);
//	 s4.setIstStunden(400);
//	 s4.setGanzes(v3);
//	
//	 DmVorhaben v4 = new DmVorhaben();
//	 v4.setTitel("Vorhaben nummer 4");
//	 v4.setBeschreibung("lalalala");
//	 v4.setTitel("ramrarmara");
//	 v4.setGanzes(v3);
//	 v4.setEndTermin(new Date(new Double(2.2075e+14).longValue()));
//	
//	 DmVorhaben v5 = new DmVorhaben();
//	 v5.setTitel("Vorhaben nummer 5");
//	 v5.setBeschreibung("lalalala");
//	 v5.setTitel("ramrarmara");
//	 v5.setGanzes(v4);
//	 v5.setEndTermin(new Date(new Double(2.2075e+14).longValue()));
//	
//	 v3.setGanzes(v5);
//	 try{
//		 lgSession.speichern(v3);
//		 fail("VorhabenRekursionExc expected");
//	 }catch(VorhabenRekursionExc expected){
//	 }
//	 lgSession.speichern(v4);
//	 lgSession.speichern(v5);
//	 lgSession.speichern(s4);
//	
//	 try{
//	 List<DmAufgabe> list = lgSession.alleOberstenAufgabenLiefern();
//	 }catch(VorhabenRekursionExc exc){
//	 System.out.println("oopsie");
//	 }
	
	
//	 }
	/*
	 * @Test public void t030findAllVorber() { // 3.findAll
	 * daFactory.beginTransaction(); DmVorhaben vorhaben = new DmVorhaben();
	 * vorhaben.setTitel("Gehen"); daVorhaben.save(vorhaben); assertEquals(3L,
	 * vorhaben.getId().longValue()); daFactory.endTransaction(true);// commitet
	 * 
	 * }
	 * 
	 * @Test public void t031findAll() { // 3.findAll
	 * daFactory.beginTransaction(); final List<DmAufgabe> aList =
	 * daAufgabe.findAll(); for (DmAufgabe dA : aList) {
	 * System.out.println(dA.getClass()); System.out.println(dA.getTitel()); }
	 * assertEquals(aList.get(0).getTitel(), "Post abholen");
	 * assertEquals(aList.get(1).getTitel(), "Gehen");
	 * daFactory.endTransaction(false);// commitet
	 * 
	 * }
	 * 
	 * @Test public void t031findAllS() { // 3.findAll
	 * daFactory.beginTransaction(); final List<DmSchritt> aList =
	 * daSchritt.findAll(); assertEquals(aList.get(0).getTitel(),
	 * "Post abholen"); daFactory.endTransaction(false);// commitet
	 * 
	 * }
	 * 
	 * @Test public void t04() { // 4. detach und merge prï¿½fen: final
	 * DmSchritt schritt; { } { }
	 * 
	 * }
	 * 
	 * @Test public void t05() { // 5. Entity wieder mit id holen und prï¿½fen:
	 * }
	 * 
	 * @Test public void t06() { // 6. Alle Schritte mit Java Persistence Query
	 * Language // (JPQL) holen: }
	 * 
	 * @Test public void t07() { // 7. Entity wieder mit id holen und dann
	 * lï¿½schen: }
	 * 
	 * @Test public void t08() { // 8. Entity nach Lï¿½schen holen: }
	 */

	@AfterClass
	public static void after() { // Aufrï¿½umen:

	}

}
