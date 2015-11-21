package l1_ui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UI_Vorhaben extends UI_Aufgabe{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final JTextField endTerminTextField;

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
             public void run() {
                 new UI_Vorhaben();
             }
         });
     }
  	public UI_Vorhaben() {
		super("Vorhaben erfassen/aendern");
	    endTerminTextField= new JTextField();
		addLastTextField();
	    openUIAufgabe();
	    setEingabenExample();
	    setEditables();
	    erfassenButton.setAction(new ExceptionReportingSwingAction("Erfassen") {
			
			@Override
			public void actionPerformedWithThrows(ActionEvent ev) throws Exception {
				System.out.println("Erfassen des Vorhabens "+titelTextField.getText());
			}
		});
	    
	    aendernButton.setAction(new ExceptionReportingSwingAction("Aendern") {
			
			@Override
			public void actionPerformedWithThrows(ActionEvent ev) throws Exception {
				System.out.println("Aendern des Vorhabens "+titelTextField.getText());
			}
		});
	}

	/**
	 * 
	 */
	protected void setEditables() {
		restHTextField.setEditable(false);
		istHTextField.setEditable(false);
	}


	protected void addLastTextField() {
	    fieldPanel.add(new JLabel("Erledigt-Zeitpunkt"), c);	    
	    fieldPanel.add(endTerminTextField, c);
	}
	@Override
	protected void setEingabenExample() {
		setEingaben("0","aufgabe", "dies \n ist ein bsp für die TextArea", new String[]{"1","2"}, "14", "1", "neu");
		endTerminTextField.setText("23.10.2015");
	}

}
