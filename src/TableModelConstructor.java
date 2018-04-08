// INNEHOLDER STATISTIKK VINDUET, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETNINGER
import javax.swing.JTable;
import javax.swing.RepaintManager;
import javax.swing.table.AbstractTableModel;

public class TableModelConstructor extends AbstractTableModel
{
	protected String[] columnName = { "Kjørelærer", "Jan", "Feb", "Mar", "Apr", "Mai", "Jun",
			                             "Jul", "Aug", "Sep", "Okt", "Nov", "Des","Sum År" };

	protected Object[][] cell = {};

    // RETURNERER ANTALL KOLLONNER
	public int getColumnCount()
	{
		return cell[ 0 ].length;
	}

    // RETURNERER ANTALL RADER
	public int getRowCount()
	{
		return cell.length;
	}

    // SETTER DATA INN I TABELLEN
	public void setValueAt( int rowIndex, int columnIndex, Object value )
	{
		cell[ rowIndex ][ columnIndex ] = value;
		fireTableCellUpdated( rowIndex, columnIndex );
	}

    // OPPDATERER KJØRELÆRERDATA I TABELLEN
	public void refreshInstorColumn( int rows  )
	{
		cell = new Object[ rows ][ columnName.length ];
	}

    // HENTER UT VERDI FRA TABELLEN
	public Object getValueAt( int rowIndex, int columnIndex )
	{
		return cell[ rowIndex ][ columnIndex ];
	}

	// HENSIKTEN ER Å FÅ EN UTSKRIFT AV TABELLEN
	public void printTable()
	{
	}

    // HENTER UT KOLLONNE NAVNET
	public String getColumnName ( int columnIndex )
	{
		return columnName[ columnIndex ];
	}

    // RETURNERER TYPEN AV KLASSE
	public Class getColumnClass( int columnIndex )
	{
		return cell[ 0 ][ columnIndex ].getClass();
	}
}