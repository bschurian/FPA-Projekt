package l2_lg;

import java.util.List;

import l4_dm.DmAufgabe;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;

/** Eine Logik-Sitzung: Service-Objekt, welches Zugriff auf die Geschäftslogik der Aufgabenverwaltung bietet. 
 * Jede Methode dieses Interfaces muss als Transaktion (ganz oder gar nicht) durchgeführt werden.*/
public interface LgSession {

	/**Speichert die übergebene Aufgabe (Schritt oder Vorhaben).
	 * Wenn die übergebene Aufgabe noch keine ID hat, wird eine neue ID durch das System vergeben.
	 * Wirft vor dem Speichern die angegebenen fachlichen Ausahmen bei Anwendbarkeit. 
	 * Die jeweiligen Bedingungen sind im Hauptkommentar der Ausnahmeklasse dokumentiert,
	 * welcher gleichzeitig der zugehörige Meldungstext ist.
	 * @return dasselbe Aufgaben-Objekt mit gesetzter ID */
	public <A extends DmAufgabe> A speichern(
		A aufgabe
	) throws TitelExc, RestStundenExc, IstStundenExc, EndTerminExc, VorhabenRekursionExc;
	
	/**Setzt im übergebenen Schritt die restStunden auf 0 und den erledigtZeitpunkt auf das aktuelle Datum+Uhrzeit. Speichert ihn dann mit der Methode speichern. */
	public DmSchritt schrittErledigen (DmSchritt schritt) 
	throws TitelExc, IstStundenExc;

	/**Liest alle Aufgaben aus der Datenbank und liefert diese in Waldform als Liste von obersten Aufgaben.
	 * Nur diese Methode liefert eine konsistente Sicht auf die Aufgaben im System.
	 * Sie muss also vor jeder Anzeige der Aufgabenliste aufgerufen werden.
	 * Eine Aufgabe ist oberste Aufgabe, wenn sie nicht Teil von einem Vorhaben ist, d.h. nicht auf ein Vorhaben als Ganzes verweist.
	 * Alle transienten Attribute aller Aufgaben werden dabei aus den anderen Aufgaben gefüllt. Nach dem Füllen gilt:
	 * Ein Vorhaben enthält als Teile alle die Aufgaben, die das Vorhaben als Ganzes angeben.
	 * Ein Vorhaben enthält als Reststunden die Summe aller Reststunden aller seiner Teile.
	 * Ein Vorhaben enthält als Iststunden die Summe aller Iststunden aller seiner Teile.
	 * Ein Vorhaben enthält als Status Folgendes:
	 * DmAufgabeStatus.neu, wenn es keine Teile enthält oder alle seineTeile den Status neu haben. 
	 * DmAufgabeStatus.erledigt, wenn alle seineTeile den Status erledigt haben. 
	 * DmAufgabeStatus.inBearbeitung andernfalls.
	 * @deprecated ersetzt durch {@link LgSession#obersteAufgabenUndAlleVorhabenLiefern()} 2015-12-27. 
	 * War Teil von Aufgabe 6, wird in Aufgabe 7 etwas modifiziert benötigt. Siehe folgende Methode.
	 * */
	public List<DmAufgabe> alleOberstenAufgabenLiefern();
	
	/**Wertobjekt zur Übergabe einer Liste von Aufgaben und einer Liste von Vorhaben.
	 * @since 2016-01-08 FPA-Aufgabe 7 Anbindung Oberfläche -> Logikschicht*/
	public class AufgabenUndVorhaben {
		public final List<DmAufgabe> aufgaben;
		public final List<DmVorhaben> vorhaben;
		public AufgabenUndVorhaben(final List<DmAufgabe> aufgaben, final List<DmVorhaben> vorhaben){
			this.aufgaben = aufgaben;
			this.vorhaben = vorhaben;
		}
	}

	/**Liest alle Aufgaben aus der Datenbank und liefert diese in zwei Ansichten mittels eines Wertobjekts des Typs {@link LgSession#AufgabenUndVorhaben}.
	 * <ol>
	 *   <li>a) in Waldform als Liste von obersten Aufgaben. Diese sollen bei Programmstart oder nach speichern in der Oberfläche angezeigt werden.</li>
	 *   <li>b) in Waldform als Liste aller Vorhaben. Diese sollen bei der Bearbeitung einer Aufgabe als Ganzes-Verweis auswählbar sein.</li>
	 * </ol>
	 *  
	 * Nur diese Methode liefert eine konsistente Sicht auf die Aufgaben im System.
	 * Sie muss also vor jeder Anzeige der Aufgabenliste aufgerufen werden.
	 * Eine Aufgabe ist oberste Aufgabe, wenn sie nicht Teil von einem Vorhaben ist, d.h. nicht auf ein Vorhaben als Ganzes verweist.
	 * Alle transienten Attribute aller Aufgaben werden dabei aus den anderen Aufgaben gefüllt. Nach dem Füllen gilt:
	 * Ein Vorhaben enthält als Teile alle die Aufgaben, die das Vorhaben als Ganzes angeben.
	 * Ein Vorhaben enthält als Reststunden die Summe aller Reststunden aller seiner Teile.
	 * Ein Vorhaben enthält als Iststunden die Summe aller Iststunden aller seiner Teile.
	 * Ein Vorhaben enthält als Status Folgendes:
	 * DmAufgabeStatus.neu, wenn es keine Teile enthält oder alle seine Teile den Status neu haben. 
	 * DmAufgabeStatus.erledigt, wenn alle seine Teile den Status erledigt haben. 
	 * DmAufgabeStatus.inBearbeitung andernfalls.
	 * @since 2016-01-08 FPA-Aufgabe 7 Anbindung Oberfläche -> Logikschicht*/
	public AufgabenUndVorhaben obersteAufgabenUndAlleVorhabenLiefern();

	/**Titel muss zwischen 10 und 200 Zeichen lang sein! Länge: {0}, Titel: {1}*/
	public static class TitelExc extends multex.Exc {}

	/**Rest-Stundenanzahl darf nicht negativ sein! Wert: {0} Stunden*/
	public static class RestStundenExc extends multex.Exc {}

	/**Ist-Stundenanzahl darf nicht negativ sein! Wert: {0} Stunden*/
	public static class IstStundenExc extends multex.Exc {}

	/**Das in der Aufgabe angegebene Ganzes-Vorhaben mit ID {0} und Titel "{1}" ist seinerseits direkt oder indirekt Teil dieser Aufgabe mit ID {2}. Solche Rekursion ist verboten!*/
	public static class VorhabenRekursionExc extends multex.Exc {}

	/**Der angegebene End-Termin {0, date, short} liegt in der Vergangenheit.*/
	public static class EndTerminExc extends multex.Exc {}

}