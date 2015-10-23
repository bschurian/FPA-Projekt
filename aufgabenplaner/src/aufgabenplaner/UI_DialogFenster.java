package aufgabenplaner;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class UI_DialogFenster extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final JPanel fieldPanel ;
	public final GridBagConstraints c;

	protected final JTextField idTextfield;
	protected final JTextField titelTextField;
	protected final JTextArea beschreibungTextArea;
	protected final JComboBox<String> vorhabenComboBox;
	protected final JTextField restHTextField;
	protected final JTextField istHTextField;
	protected final JTextField statusTextField;
	
	protected final JPanel buttonPanel;
	
 
	public UI_DialogFenster(String name) {
		super();
		setTitle(name);
		
		setMinimumSize(new Dimension(400, 400));
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		fieldPanel = new JPanel(new GridBagLayout());
		
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty=1.0;
        c.weightx=1.0;
		
	    fieldPanel.add(new JLabel("ID:"), c);    
	    idTextfield = new JTextField();
	    fieldPanel.add(idTextfield, c);
	    fieldPanel.add(new JLabel("Titel:"), c);
	    titelTextField= new JTextField();
	    fieldPanel.add(titelTextField, c);
	    fieldPanel.add(new JLabel("Beschreibung:"), c);	    
	    beschreibungTextArea= new JTextArea(4, 0);
	    fieldPanel.add(beschreibungTextArea, c);
	    fieldPanel.add(new JLabel("Teil von Vorhaben:"), c);	    
	    vorhabenComboBox= new JComboBox<String>();
	    fieldPanel.add(vorhabenComboBox, c);
	    fieldPanel.add(new JLabel("Rest Stunden zu arbeiten:"), c);	    
	    restHTextField= new JTextField();
	    fieldPanel.add(restHTextField, c);
	    fieldPanel.add(new JLabel("Ist-stunden gearbeitet"), c);	    
	    istHTextField= new JTextField();
	    fieldPanel.add(istHTextField, c);
	    fieldPanel.add(new JLabel("Status"), c);	    
	    statusTextField= new JTextField();
	    fieldPanel.add(statusTextField, c);
	    addLastTextField(fieldPanel);

	    setEingabenExample();
	    
	    idTextfield.setEditable(false);
	    vorhabenComboBox.setEditable(false);

	    setEditables();
	     
	    container.add(fieldPanel, BorderLayout.CENTER);
		
	    buttonPanel = new JPanel(new FlowLayout());
	    addButtons(buttonPanel);	    
	    container.add(buttonPanel, BorderLayout.SOUTH);
	    
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    setSize(600, 400);
	    setVisible(true);
	}
		
	protected void setEingaben(String id,String titel,String beschreibung,String[] vorhaben,String restH,String istH,String status){
		idTextfield.setText(id);
		titelTextField.setText(titel);
		beschreibungTextArea.setText(beschreibung);
		for(String item: vorhaben){
			vorhabenComboBox.addItem(item);
		}
		restHTextField.setText(restH);
		istHTextField.setText(istH);
		statusTextField.setText(status);
	}
	
	protected abstract void setEingabenExample();
	
	protected abstract void setEditables() ;
	
	protected abstract void addLastTextField(JPanel fieldPanel);
    protected abstract void addButtons(JPanel buttonPanel);
	
}
