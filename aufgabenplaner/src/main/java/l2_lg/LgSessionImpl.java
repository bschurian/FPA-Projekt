package l2_lg;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.derby.iapi.util.ReuseFactory;

import l3_da.DaFactory;
import l4_dm.DmAufgabe;
import l4_dm.DmAufgabeStatus;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;
import multex.MultexUtil;

public class LgSessionImpl implements LgSession {

	private final DaFactory daFactory;

	private List<DmAufgabe> alleAufgaben;

	public LgSessionImpl(final DaFactory daFactory) {
		this.daFactory = daFactory;
	}

	@Override
	public <A extends DmAufgabe> A speichern(final A aufgabe) throws TitelExc,
			RestStundenExc, IstStundenExc, EndTerminExc, VorhabenRekursionExc {
		if (10 > aufgabe.getTitel().length()
				&& aufgabe.getTitel().length() > 200) {
			throw new TitelExc();
		}
		if (0 > aufgabe.getIstStunden()) {
			throw new IstStundenExc();
		}
		if (aufgabe.getClass() == DmVorhaben.class
				&& ((DmVorhaben) aufgabe).getEndTermin().before(
						new java.util.Date(System.currentTimeMillis()))) {
			throw new EndTerminExc();
		}
		if (0 > aufgabe.getIstStunden()) {
			throw new IstStundenExc();
		}
		rekursionsFehler(aufgabe);
		
		LgTransaction<A> lgta = new LgTransaction<A>() {
			@Override
			protected A action() {
				daFactory.getAufgabeDA().save(aufgabe);
				return aufgabe;
			}
		};
		return lgta.getResult();
	}

	@Override
	public DmSchritt schrittErledigen(final DmSchritt schritt) throws TitelExc,
			IstStundenExc {
		if (10 > schritt.getTitel().length()
				&& schritt.getTitel().length() > 200) {
			throw new TitelExc();
		}
		if (0 > schritt.getIstStunden()) {
			throw new IstStundenExc();
		}
		schritt.setErledigtZeitpunkt(new Date(System.currentTimeMillis()));
		return schritt;
	}

	@Override
	public AufgabenUndVorhaben obersteAufgabenUndAlleVorhabenLiefern() {
		return new LgTransaction<AufgabenUndVorhaben>(){
			@Override
			protected AufgabenUndVorhaben action() {
				alleAufgaben = daFactory.getAufgabeDA().findAll();
				AufgabenUndVorhaben aUV = new AufgabenUndVorhaben(new ArrayList<DmAufgabe>(),new ArrayList<DmVorhaben>());
				for (DmAufgabe dmAufgabe : alleAufgaben) {
					if (dmAufgabe.getGanzes() == null) {
						transienteDatenRekursivBerechnen(dmAufgabe);
						aUV.aufgaben.add(dmAufgabe);
						if(dmAufgabe instanceof DmVorhaben){
							aUV.vorhaben.add((DmVorhaben)dmAufgabe);
						}
					}
				}
				return aUV;			
			}
		}.getResult();
	}
	
	@Override
	public List<DmAufgabe> alleOberstenAufgabenLiefern() {
		return new LgTransaction<List<DmAufgabe>>(){
			@Override
			protected List<DmAufgabe> action() {
				alleAufgaben = daFactory.getAufgabeDA().findAll();
				List<DmAufgabe> aUV = new ArrayList<DmAufgabe>();
				for (DmAufgabe dmAufgabe : alleAufgaben) {
					if (dmAufgabe.getGanzes() == null) {
						transienteDatenRekursivBerechnen(dmAufgabe);
						aUV.add(dmAufgabe);
					}
				}
				return aUV;			
			}
		}.getResult();
	}

	private DmAufgabe transienteDatenRekursivBerechnen(DmAufgabe aufgabe)
			throws VorhabenRekursionExc {
		if (aufgabe.getClass() == DmSchritt.class) {
			return aufgabe;
		}
		if (aufgabe.getClass() == DmVorhaben.class) {
			// teile fuellen
			rekursionsFehler(aufgabe);
			
			for (DmAufgabe dmAufgabe : alleAufgaben) {
				if (dmAufgabe.getGanzes() == aufgabe) {
					((DmVorhaben) aufgabe).getTeile().add(
							transienteDatenRekursivBerechnen(dmAufgabe));
				}
			}

			// attribute entscheiden
			if (aufgabe.getAnzahlTeile() == 0) {
				if (aufgabe.getClass() == DmVorhaben.class) {
					((DmVorhaben) aufgabe).setStatus(DmAufgabeStatus.neu);
				}
			} else {// automatisch DmVorhaben mit List mit mind. einem Element
				// status
				DmAufgabeStatus statusQuo = aufgabe.getTeile().get(0).getStatus();
				if (inBearbeitung(aufgabe.getTeile(), statusQuo)) {
					((DmVorhaben) aufgabe).setStatus(DmAufgabeStatus.inBearbeitung);
				} else {
					((DmVorhaben) aufgabe).setStatus(statusQuo);
				}
			}
			// istH und restH
			int istHSum = 0;
			int restHSum = 0;
			for (DmAufgabe teil : aufgabe.getTeile()) {
				istHSum += teil.getIstStunden();
				restHSum += teil.getRestStunden();
			}
			((DmVorhaben) aufgabe).setIstStunden(istHSum);
			((DmVorhaben) aufgabe).setRestStunden(restHSum);

		}
		return aufgabe;
	}

	private boolean inBearbeitung(final List<DmAufgabe> Teile,
			final DmAufgabeStatus firstStatus) {
		for (DmAufgabe teil : Teile) {
			if (firstStatus == DmAufgabeStatus.inBearbeitung
					|| teil.getStatus() != firstStatus) {
				return true;
			}
		}
		return false;
	}

	private boolean rekursionsFehler(final DmAufgabe aufgabe) throws VorhabenRekursionExc{
		DmAufgabe parent = aufgabe.getGanzes();
		while (parent != null) {
			if (parent == aufgabe) {
				throw MultexUtil.create(VorhabenRekursionExc.class, aufgabe.getId(), aufgabe.getTitel(), parent.getId());
			}
			parent = parent.getGanzes();
		}
		return false;
	}

	private abstract class LgTransaction<RESULT> {
		protected abstract RESULT action();

		public RESULT getResult() {
			boolean ok = false;
			try {
				daFactory.beginTransaction();
				RESULT res = action();
				ok = true;
				return res;
			} finally {
				daFactory.endTransaction(ok);
			}
		}
	}

}
