package l1_ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UI_Schritt extends UI_DialogFenster {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTextField erledigtTextField;
	
    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
             public void run() {
                 new UI_Schritt(0);
             }
         });
     }
	
	public UI_Schritt(int id) {
		super("Schritt erfassen/aendern");
	}

	/**
	 * 
	 */
	protected void setEditables() {
		statusTextField.setEditable(false);
		erledigtTextField.setEditable(false);
	}

	@Override
	protected void addButtons(JPanel buttonPanel) {
		JButton erfassenButton = new JButton("Erfassen");
	    buttonPanel.add(erfassenButton);
	    JButton aendernButton = new JButton("Aendern");
	    buttonPanel.add(aendernButton);
	    JButton erledigenButton = new JButton("Erledigen");
	    buttonPanel.add(erledigenButton);
	}

	@Override
	protected void addLastTextField(JPanel fieldPanel) {
	    fieldPanel.add(new JLabel("Erledigt-Zeitpunkt"), c);	    
	    erledigtTextField= new JTextField();
	    fieldPanel.add(erledigtTextField, c);
	}

	@Override
	protected void setEingabenExample() {
		setEingaben("0","aufgabe", "dies \n ist ein bsp für die TextArea", new String[]{"1","2"}, "14", "1", "neu");
		erledigtTextField.setText("ich hab probleme mit 1)");
	}

}
