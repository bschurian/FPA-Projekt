package l1_ui;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import l4_dm.DmAufgabe;

public class AufgabenTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private String[] columnNames;
	private DmAufgabe[] data;

	public AufgabenTableModel(DmAufgabe[] newData) {
		this.columnNames = new String[] { "ID", "Titel", "#Teile", "Status" };
		setData(newData);
	}

	public AufgabenTableModel(){
		this(null);
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	public void setData(DmAufgabe[] newData) {
		this.data = newData;
		fireTableDataChanged();
	}
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return true;
	}
	
	public Object getValueAt(int row, int col) {
		final DmAufgabe aufgabe = data[row];
		switch (col) {
		case 0:
			return aufgabe.getId(); 
		case 1:
			return aufgabe.getTitel(); 				
		case 2:
			return aufgabe.getAnzahlTeile();
		case 3:
			return aufgabe.getStatus(); 
		default:
			return null;
		}
	}

//	public Class getColumnClass(int c) {
//		return getValueAt(0, c).getClass();
//	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}
}
