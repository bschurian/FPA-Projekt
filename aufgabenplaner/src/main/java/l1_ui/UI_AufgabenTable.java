package l1_ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import l4_dm.DmAufgabe;
import l4_dm.DmAufgabeStatus;
import l4_dm.DmSchritt;
import l4_dm.DmVorhaben;


public class UI_AufgabenTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7876404650463742602L;

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final UI_AufgabenTable table = new UI_AufgabenTable(null);
				table.fillWithTestData();
				final JDialog thisDialog = new JDialog();
				thisDialog.setTitle("Test UiAufgabenTable");
				final Container container = thisDialog.getContentPane();
				final JScrollPane scrollPane = new JScrollPane(table);
				container.add(scrollPane, BorderLayout.CENTER);
				thisDialog.pack();
				thisDialog.setVisible(true);
			}
		});
	}

	public UI_AufgabenTable(final DmAufgabe[] data) {
		final TableModel tModel = new AufgabenTableModel(data);
		setModel(tModel);
	}

	protected void fillWithTestData() {
		final DmAufgabe dm1 = new DmSchritt(){
			@Override
			public Long getId() {
				return 1L;
			}
		};
		final DmAufgabe dm2 = new DmVorhaben(){
			@Override
			public Long getId() {
				return 2L;
			}
			@Override
			public DmAufgabeStatus getStatus() {
				return DmAufgabeStatus.inBearbeitung;
			}
			@Override
			public int getAnzahlTeile() {
				return 23;
			}
		};
		final DmAufgabe dm3 = new DmSchritt(){
			@Override
			public Long getId() {
				return 3L;
			}
		};
		
		dm1.setTitel("Schritt 1: Git");
		dm2.setTitel("Vorhaben 1: Tipping");
		dm3.setTitel("Schritt 2: Gud");

		final DmAufgabe[] data= new DmAufgabe[] {dm1,dm2,dm3};
		
//		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnitName");
//		final EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();
//		for(DmAufgabe aufgabe:data){
//			em.persist(aufgabe);
//		}
		
		((AufgabenTableModel)getModel()).setData(data);;
		
//		em.getTransaction().rollback();
//		em.close();
	}

}
