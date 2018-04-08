// INNEHOLDER STATISTIKK VINDUET, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETNINGER
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class Statistics extends JPanel implements ActionListener
{

	// DATAFELTER
	private static final long serialVersionUID = 1003L;
	protected Instructorindex ins, help;
	protected Candidateindex can;
	protected TableModelConstructor tmc;
	protected DefaultTableModel dtm;
	protected String[] comboLabelNames = { "Kjøretime navn:", "Kjøretime kategori:", "Kjøretime gruppe:", "Årstall:" };
	protected JTextField[] pregfield;
	protected JButton[] pregbuttons;
	protected String[] pregbuttonNames = { "Print" };

	private int[] years = new int [ 100 ];
	int startyear = 2012;

	private String[] intToString = new String [ years.length ];

	// OPPDATERER JCOMBOBOX MED KJØRETIMER I SYSTEMET I KONSTRUKTØR
	private String[] assignment;

	// EGEN JCOMBOBOX MED KATEGORIER
	private String[] categories;
	private String[] licenses = { "A", "B", "C" };
	private JLabel[] comboLabels;

	// EGEN JCOMBOBOX MED KATEGORIER
	private String[] [] combogroup = { licenses, intToString };
	private JComboBox[] combobox;

	// EGEN JCOMBOBOX MED KATEGORIER/KJØRETIMER
	private JComboBox assignmentbox, categorybox;
	private List <Instructor> inslist = new ArrayList <Instructor> ();

	// OPPDATERER JCOMBOBOX MED KJØRETIMER I SYSTEMET I KONSTRUKTØR
	private String assignmentname = "";
	private String drivename = "";
	private String category = "";
	private char license = ' ';
	private String chosenyear;
	protected JLabel[] preglabels;
	protected JPanel p1, p2, p22, p3, p4;
	protected JTable stat;
	private final int	ASSIGNMENTNAME = 0, PRINT = 0, FIRSTROW = 0,
	CATEGORY = 1, SECONDROW = 1, YEAR = 1,
	DRIVETYPE = 2, THIRDROW = 2,
	INSTRUCTORNAME = 3, SHOW_ALL_DRIVES = 3,
	TEST = 5,
	JANUARY = 1,
	FEBRUARY = 2,
	MARS = 3,
	APRIL = 4,
	MAY = 5,
	JUNE = 6,
	JULY = 7,
	AUGUST = 8,
	SEPTEMBER = 9,
	OCTOBER = 10,
	NOVEMBER = 11,
	DECEMBER = 12;

	private JTextArea display;
	private JScrollPane col;
	private boolean checked = false;
	private Candidate candidate;
	private int lengthJComboBox = 5,
	january = 0,
	february = 0,
	mars = 0,
	april = 0,
	may = 0,
	june = 0,
	july = 0,
	august = 0,
	september = 0,
	october = 0,
	november = 0,
	december = 0;

	// KONSTRUKTØR
	public Statistics( Instructorindex ins, Candidateindex can, MainFrame reference )
	{
		this.ins = ins;
		this.can = can;
		tmc = new TableModelConstructor();
		setLayout( new BorderLayout( 10, 10 ) );

		for( int i = 0; i < years.length; i++ )
		{
			years[ i ] = startyear++;
			intToString[ i ]= "" + years[ i ];
		}

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder( etched, "Kjøretime typer" );
		Border labelborders = BorderFactory.createCompoundBorder();

		p3 = new JPanel( new GridLayout( 2, 4, 1, 1 ) );
		comboLabels = new JLabel[ comboLabelNames.length ];

		for( int i = 0; i < comboLabelNames.length; i++ )
		{
			p3.add( comboLabels[ i ] = new JLabel( comboLabelNames[ i ] ), labelborders );
		}

		p22 = new JPanel( new GridLayout( 2, 4, 1, 1 ) );

		// EGEN JCOMBOBOX MED KATEGORIER/KJØRETIMER
		assignmentbox = new JComboBox ();
		DriveType.getAssignments( assignmentname, assignmentbox );
		assignmentbox.setMaximumRowCount( lengthJComboBox );
		assignmentbox.setSelectedIndex( 0 );
		assignmentbox.addActionListener( this );
		p3.add( assignmentbox );

		categories = reference.getCategories();
		categorybox = new JComboBox( categories );
		categorybox.setMaximumRowCount( lengthJComboBox );
		categorybox.setSelectedIndex( 0 );
		categorybox.addActionListener( this );
		p3.add( categorybox );

		combobox = new JComboBox [ combogroup.length ];

		for( int i = 0; i < combogroup.length; i++ )
		{
			combobox[ i ] = new JComboBox ( combogroup[ i ] );
			combobox[ i ].setMaximumRowCount( lengthJComboBox );
			combobox[ i ].setSelectedIndex( 0 );
			combobox[ i ].addActionListener( this );
			p3.add( combobox[ i ] );
		}

		add( p3, BorderLayout.PAGE_START );
		pregbuttons = new JButton [ pregbuttonNames.length ];
		p2 = new JPanel ( new GridLayout( 2, pregbuttonNames.length, 1, 1 ) );

		display = new JTextArea( 5,40 );
		dtm = new DefaultTableModel( tmc.cell, tmc.columnName );
		stat = new JTable( dtm );

		for( int i = 0; i < pregbuttons.length; i++ )
		{
			pregbuttons[ i ] = new JButton( pregbuttonNames[ i ] );
			pregbuttons[ i ].addActionListener( this );
			p2.add( pregbuttons[ i ] );
		}

		col = new JScrollPane( stat );
		add( col, BorderLayout.CENTER );
		add( display, BorderLayout.PAGE_END );
	}

	// EGEN JCOMBOBOX MED KATEGORIER/KJØRETIMER
	public JComboBox getAssignmentBox()
	{
		return assignmentbox;
	}

	public String getAssignmentName()
	{
		return assignmentname;
	}

	// TREKKER UT MÅNED UT FRA AVTALEN
	private String validateMonth( String text )
	{
		String s = text.substring( 2, 4 );
		return s;
	}

	// TREKKER UT ÅR UT FRA AVTALEN
	private String validateYear( String value )
	{
		String s = value.substring( 4, 6 );
		return s;
	}

	// HENTER KJØRETYPENE FRA JCOMBOBOX
	public JComboBox getDriveBox()
	{
		return combobox[ 0 ];
	}

	// SETTER INN DATA I TABELLEN
	public void addRowToTable()
	{

		int number = dtm.getRowCount();
		for( int i = 0; i < number; i++ )
		{
			dtm.removeRow(0);
		}

		try
		{
			dtm.addRow( new Object[] { combobox [ 0 ].getSelectedItem() } );
			repaint();
		}

		catch( ArrayIndexOutOfBoundsException AIOOE )
		{
			display.setText( "AIOOE" );
		}

	}

	// METODE FOR Å SJEKKE NAVN PÅ KJØRELÆRER
	public boolean validateInstructor( Agreement pre, String instructorName )
	{
		if( instructorName.equalsIgnoreCase( pre.getInstructorName() ) )
		{
			return true;
		}
		return false;
	}

	// RENSER TABELLEN
	private void emptyTable()
	{
		int number = dtm.getRowCount();
		System.out.println( "Ditt nummer er  " + number );

		for( int i = 0; i < number; i++ )
		{
			System.out.println( "fjernet " + i + " linjer" );
			dtm.removeRow( 0 );
		}
	}

	private void statDriveNameFrequency()
	{
		emptyTable();
		ArrayList <Instructor> inss = ins.listAllInss();
		if( inss.isEmpty() )
		{
			display.setText( "Ingen kjørelærer med denne kjøretimen" );
			return;
		}
		Iterator <Instructor> i = inss.iterator();

		while( i.hasNext() )
		{
			dtm.addRow( i.next().getDriveFrequency( chosenyear, drivename ) );
		}
	}

	private void statGroupFrequency()
	{
		emptyTable();
		ArrayList <Instructor> inss = ins.listAllInss();
		if( inss.isEmpty() )
		{
			display.setText( "Ingen kjørelærer med denne kjøretimen" );
			return;
		}
		Iterator <Instructor> i = inss.iterator();

		while( i.hasNext() )
		{
			dtm.addRow( i.next().getGroupFrequency( chosenyear, license ) );
		}
	}

	private void statGroupCategory()
	{
		emptyTable();
		ArrayList <Instructor> inss = ins.listAllInss();
		if( inss.isEmpty() )
		{
			display.setText( "Ingen kjørelærer med denne kjøretimen" );
			return;
		}
		Iterator<Instructor> i = inss.iterator();

		while( i.hasNext() )
		{
			dtm.addRow( i.next().getCategoryFrequency( chosenyear, category ) );
		}
	}

	// LYTTEKLASSE, KNAPPER FOR VINDUET I STATISTIKK
	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource () == pregbuttons[ ASSIGNMENTNAME ] )
		{
		}

		// KUN TO BOKSER IGJEN I LØKKA
		else if( e.getSource() == assignmentbox )
		{
			drivename = ( String )assignmentbox.getSelectedItem();
			String help = ( String )combobox[ YEAR ].getSelectedItem();
			chosenyear =  help.substring( 2, 4 );
			display.setText( chosenyear + ", " + drivename + ", " + help + ", " + help.substring( 2, 4 ) );
			statDriveNameFrequency();
			repaint();
		}

		// KUN TO BOKSER IGJEN I LØKKA
		else if( e.getSource() == 	categorybox )
		{
			category = ( String )categorybox.getSelectedItem();
			String help = ( String )combobox[ YEAR ].getSelectedItem();
			chosenyear =  help.substring( 2, 4 );
			statGroupCategory();
			repaint();
		}

		// KUN TO BOKSER IGJEN I LØKKA
		else if( e.getSource() == combobox[ 0 ] )
		{
			String help = ( String )combobox[ 0 ].getSelectedItem();
			license = help.charAt( 0 );
			String help2 = ( String )combobox[ YEAR ].getSelectedItem();
			chosenyear =  help2.substring( 2, 4 );
			statGroupFrequency();
			repaint();
		}

		// KUN TO BOKSER IGJEN I LØKKA
		else if( e.getSource() == combobox[ 1 ] )
		{
			System.out.println( dtm.getRowCount() );
			int number = dtm.getRowCount();
			System.out.println( "Ditt nummer er  " + number );


			for( int i = 0; i < number; i++ )
			{
				System.out.println( "fjernet " + i + " linjer" );
				dtm.removeRow(0);
			}
			repaint();
		}
	}
}