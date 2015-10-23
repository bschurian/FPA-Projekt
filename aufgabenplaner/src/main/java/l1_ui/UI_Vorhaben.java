package l1_ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UI_Vorhaben extends UI_DialogFenster{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTextField endTerminTextField;

    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
             public void run() {
                 new UI_Vorhaben(0);
             }
         });
     }
  	public UI_Vorhaben(int id) {
		super("Vorhaben erfassen/aendern");
	}

	/**
	 * 
	 */
	protected void setEditables() {
		restHTextField.setEditable(false);
		istHTextField.setEditable(false);
		statusTextField.setEditable(false);
	}

	@Override
	protected void addButtons(JPanel buttonPanel) {
		JButton erfassenButton = new JButton("Erfassen");
	    buttonPanel.add(erfassenButton);
	    JButton aendernButton = new JButton("Aendern");
	    buttonPanel.add(aendernButton);
	}

	@Override
	protected void addLastTextField(JPanel fieldPanel) {
	    fieldPanel.add(new JLabel("Erledigt-Zeitpunkt"), c);	    
	    endTerminTextField= new JTextField();
	    fieldPanel.add(endTerminTextField, c);
	}
	@Override
	protected void setEingabenExample() {
		setEingaben("0","aufgabe", "dies \n ist ein bsp für die TextArea", new String[]{"1","2"}, "14", "1", "neu");
		endTerminTextField.setText("23.10.2015");
	}

}
