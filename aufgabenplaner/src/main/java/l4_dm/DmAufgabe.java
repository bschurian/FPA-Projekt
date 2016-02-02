package l4_dm;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

/**Datenmodellklasse für eine Aufgabe im Allgemeinen. Ohne Prüfungen und Folgeaktionen.*/
/*@MappedSuperclass*/@Entity @Inheritance(strategy=InheritanceType.SINGLE_TABLE) @Table(name="aufgabe") 
public abstract class DmAufgabe {

	/**Konstante für das Attribut id mit der Bedeutung, dass das Objekt noch nicht persistiert wurde*/
    /*package*/ static final Long createdId = null;

	/**Konstante für das Attribut id mit der Bedeutung, dass das Objekt in der Datenbank gelöscht wurde, 
	 * aber im Arbeitsspeicher noch vorhanden ist*/
    /*package*/ static final Long deletedId = new Long(0);

    //Die Attribute sind redundanzfrei nur bei den Gettern dokumentiert.
    
    @Id @GeneratedValue
    private Long id = createdId;	
	private String titel;
	private String beschreibung;
	
	@ManyToOne
	private DmVorhaben ganzes;

    /**Liefert die eindeutige ObjektIDentifikationsnummer >= 1 für jedes persistierte Objekt dieser Klassenhierarchie.*/
	public Long getId(){return id;}
	
	/**Liefert den einzeiligen Titel der Aufgabe.*/
	public String getTitel() {return titel;}
	
	/**Liefert die mehrzeilige Beschreibung der Aufgabe.*/
	public String getBeschreibung() {return beschreibung;}
	
	/**Liefert eine Referenz auf das ganze Vorhaben, von dem diese Aufgabe ein Teil ist.*/
	public DmVorhaben getGanzes() {return ganzes;}
	
	/**Liefert die noch an der Aufgabe inklusive aller Teilaufgaben zu arbeitenden Stunden.*/
	public abstract int getRestStunden();
	
	/**Liefert die schon an der Aufgabe inklusive aller Teilaufgaben gearbeiteten Stunden.*/
	public abstract int getIstStunden();
	
	/**Liefert die Anzahl von Teilaufgaben dieser Aufgabe. Bei einem Schritt ist diese 0.*/
	public abstract int getAnzahlTeile();
	
	/**Liefert die Teilaufgaben von dieser Aufgabe. Bei einem Schritt ist die Ergebnisliste leer und nicht änderbar.
	 * @since 2015-12-03*/
	public abstract List<DmAufgabe> getTeile();
	
	/**Liefert den Status der Aufgabe. Bei einem Vorhaben sollte er aus den Statuswerten der Teil-Aufgaben abgeleitet sein.*/
	public abstract DmAufgabeStatus getStatus();
	
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public void setGanzes(DmVorhaben ganzes) {
		this.ganzes = ganzes;
	}
	
	/**Liefert eine knappe String-Darstellung dieses Objekts mit Klassennamen (ohne Dm), id und titel des Objekts.
	 * @since 2015-12-03*/
	@Override
	public String toString(){return getClass().getSimpleName().substring(2) + "(" + getId() + ", " + getTitel() + ")";}

}
