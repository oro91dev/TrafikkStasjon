// INNEHOLDER STATISTIKK VINDUET, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.RepaintManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1002L;

	private String[] columnNames = { "Kjørelærere", "Jan", "Feb", "Mar", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des", "År" };

	Object[][]data = { { "Kjørelærere", "Jan", "Feb", "Mar", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des", "År" },
    		          { "Kjørelærere", "Jan", "Feb", "Mar", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des", "År"} };
}