package l1_ui;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import multex.Exc;

public class UI_Schritt extends UI_Aufgabe {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final JTextField erledigtTextField;
	JButton erledigenButton;
	
	
    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                 new UI_Schritt();
            }
         });
     }
	
	public UI_Schritt() {
		super("Schritt erfassen/aendern");
		erledigtTextField= new JTextField();
		addLastTextField();
		erledigenButton= new JButton("Erledigen");
		buttonPanel.add(erledigenButton);
		openUIAufgabe();
		setEingabenExample();
		setEditables();
		
	    erfassenButton.setAction(new ExceptionReportingSwingAction("Erfassen") {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformedWithThrows(ActionEvent ev) throws Exception {
				System.out.println("Erfassen des Schritts "+titelTextField.getText());
			}
		});
	    
	    aendernButton.setAction(new ExceptionReportingSwingAction("Aendern") {
			
			@Override
			public void actionPerformedWithThrows(ActionEvent ev) throws Exception {
				System.out.println("Aendern des Schritts "+titelTextField.getText());
			}
		});

	    erledigenButton.setAction(new ExceptionReportingSwingAction("Erledigen") {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformedWithThrows(ActionEvent ev) throws Exception {
				System.out.println("Erledigen des Schritts "+titelTextField.getText());
				throw new Exc("Diese Aufgabe ist zu schwer.");
			}
		});

	}

	/**
	 * 
	 */
	protected void setEditables() {
		erledigtTextField.setEditable(false);
	}

	protected void addLastTextField() {
	    fieldPanel.add(new JLabel("Erledigt-Zeitpunkt"), c);	    
	    fieldPanel.add(erledigtTextField, c);
	}

	protected void setEingabenExample() {
		setEingaben("0","aufgabe", "dies \n ist ein bsp für die TextArea", new String[]{"1","2"}, "14", "1", "neu");
		erledigtTextField.setText("ich hab probleme mit 1)");
	}

}
