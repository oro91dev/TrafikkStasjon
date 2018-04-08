// INNEHOLDER METODER FOR VINDUET FOR AVTALE REGISTERET, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Instructordetails extends JPanel implements ActionListener, ListSelectionListener, KeyListener, Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1007L;
	private Instructorindex index;
	private JTextArea display;
	private JScrollPane scrollfield;
	private JPanel p11, p12, p13, p14;

	// DEKLARERER SØKEFELT
	private String[] fieldnames = { "   Søk på kjørelærens navn:", "   Søk på kontoradresse:", "   Søk kjøretime:" };
	private JTextField[] field;
	private String partofinsname;
	private String partofadress;
	private String partofassignmentname = "";
	private Instructor updateinstructor;

	// DEKLARERER COMBOBOKSER OG LYTTE DATAFELT
	private String[] assignments = { "" };
	private String[] licenses = { "A", "B", "C", "Ingen sertifikat" };
	private String[] categories;
	private String[] [] combogroup = { assignments, licenses };
	private String[] comboboxes = { "   Finn kjøretime:", "   Velg sertifikat:" };
	private JComboBox categoryBox;
	private JComboBox[] combobox;
	private JLabel categoryLabel;
	private JLabel[] label, combolabel;
	private int lengthJComboBox = 7;
	private String assignmentname = "";
	private String category = "";
	private char license = ' ';
	private  final int TEXTFIELD_SIZE = 15,
	NAME = 0, ADRESS = 1, ASSIGNMENT = 2,
	ASSIGNMENT_LIST = 0, ASSIGNMENT_LICENCE = 1;

	// DEKLARERER KNAPPER
	private JButton[] button;
	private String[] buttonames = {  "Alle kjørelærere",
									 "Kjørelærere med sertifikat",
									 "Kjørelærere valgt kjøretime",
									 "Kjørelærere valgt sertifikat",
									 "Avtaler kategori/kjørelærer",
									 "Avtaler sertifikat/kjørelærer" };

	private final int 			     ALL_INSTRUCTORS = 0,
									 ALL_LICENCED_INSTRUCTORS = 1,
									 ALL_INSTRUCTORS_CHOSEN_ASSIGNMENT = 2,
									 ALL_INSTRUCTORS_CHOSEN_LICENCE = 3,
									 ALL_AGREEMENTS_CHOSEN_CATEGORY= 4,
									 ALL_AGREEMENTS_CHOSEN_LICENSE= 5;

	// DEKLARERER LISTER
	JList inslist, agreementlist;
	private JScrollPane listinstructorscroll, listagreementscroll;
	DefaultListModel modelins, modelagreement;
	private int lengthJList = 8;
	String[] ins = { "   " };
	String[] agreement = { "   " };
	private String insname;
	private Agreement agreementname;
	private final int INSTRUCTORNAMES = 0, AGREEMENTS = 1;

	// KONSTRUKTØR
	public Instructordetails( Instructorindex index, MainFrame reference )
	{
		this.index = index;
	    setLayout( new BorderLayout( 10,10 ) );
	    Border etched = BorderFactory.createEtchedBorder();
	    Border searchfunctions = BorderFactory.createTitledBorder( etched, "Kjørelærer" );
	    Border buttons = BorderFactory.createTitledBorder( etched, "Informasjon" );
	    Border choices = BorderFactory.createTitledBorder( etched, "Valgte kjørelærere og kjørelarens avtaler" );

		// TEKST FELTER
		p11 = new JPanel( new GridLayout( fieldnames.length, 2, 1, 1 ) );
		p11.setBorder( searchfunctions );
		field = new JTextField[ fieldnames.length ];
		label = new JLabel[ fieldnames.length ];

		for( int i = 0; i < fieldnames.length; i++ )
		{
			field[ i ] = new JTextField( TEXTFIELD_SIZE );
			label[ i ] = new JLabel( fieldnames[ i ] );
			field[ i ].addKeyListener( this );
			field[ i ].setFocusTraversalKeysEnabled( false );
			field[ i ].setFocusable( true );
			if( i == NAME )
		   	field [ NAME ].requestFocusInWindow();
			p11.add( label[ i ] );
			p11.add( field[ i ] );
		}

		categories = reference.getCategories();
		categoryBox = new JComboBox( categories );
		categoryLabel = new JLabel ( "   Velg kategori:" );
		combobox = new JComboBox [ comboboxes.length ];
		combolabel = new JLabel[ comboboxes.length ];

		for( int i = 0; i < comboboxes.length; i++ )
		{
			if( i == 1 )
			{
				categoryBox.addActionListener( this );
				p11.add( categoryLabel );
				p11.add( categoryBox );
			}
				combolabel[ i ] = new JLabel( comboboxes[ i ] );
				combobox[ i ] = new JComboBox( combogroup [ i ] );
				combobox[ i ].setMaximumRowCount( lengthJComboBox );
				combobox[ i ].setSelectedIndex( 0 );
				combobox[ i ].addActionListener ( this );
				p11.add( combolabel[ i ] );
				p11.add( combobox[ i ] );
		}

		// KNAPPER
		p12 = new JPanel( new GridLayout( fieldnames.length, 2, 1, 1 ) );
		p12.setBorder( buttons );
		button = new JButton[ buttonames.length ];

		for( int i = 0; i < buttonames.length; i++ )
		{
			button[ i ] = new JButton( buttonames[ i ] );
			button[ i ].addActionListener( this );
			p12.add( button[ i ] );
		}

		// LISTER
		p13 = new JPanel( new GridLayout( 1, 2, 1, 1 ) );
		p13.setBorder( choices );
        modelins = new DefaultListModel();
		modelagreement = new DefaultListModel();
		inslist = new JList( modelins );
		agreementlist = new JList( modelagreement );

		inslist.setVisibleRowCount( lengthJList );
		inslist.setSelectedIndex( 0 );
		inslist.addListSelectionListener( this );
		agreementlist.setVisibleRowCount( lengthJList );
		agreementlist.setSelectedIndex( 0 );
		agreementlist.addListSelectionListener( this );

		listinstructorscroll = new JScrollPane( inslist );
		listagreementscroll = new JScrollPane( agreementlist );

		for( int i = 0; i < ins.length; i++ )
		{
			modelins.add( i, ins[ i ] );
			modelagreement.add( i, agreement[ i ] );
		}

		p13.add( listinstructorscroll );
		p13.add( listagreementscroll );

		// FYLLER PANEL
		p14 = new JPanel( new BorderLayout( 6, 6 ) );
		p14.add( p11, BorderLayout.NORTH );
		p14.add( p12, BorderLayout.CENTER );
		p14.add( p13, BorderLayout.SOUTH );

		add( p14, BorderLayout.NORTH );
		display = new JTextArea( 10, 10 );
		scrollfield = new JScrollPane( display );
		add( scrollfield , BorderLayout.CENTER );
		this.setSize( 900, 600 );
		this.setVisible( true );
	}

	// RENSER TEKSTFELTENE
	private void clearAllFields()
	{
		for( int i = 0; i < field.length; i++ )
		field[ i ].setText( "" );
	}

	// METODENE SOM GIR INFORMASJON OM KJØRELÆRER OG AVTALENE TIL VALGT KJØRELÆRER

	// VISER ALLE KJØRELÆRERE SIN toString()
	private void allInstructors()
	{
		ArrayList <Instructor> inss = index.listAllInss();

	    if( inss.isEmpty() )
		{
			display.setText( "Ingen kjørelærer registrert" );
				return;
		}

		Iterator <Instructor> i = inss.iterator();
		modelins.clear();

		while( i.hasNext() )
		{
			modelins.addElement( i.next() );
     	}
		inslist.validate();
		display.setText ( "" );
	}

    // VISER ALLE REGISTRERTE KJØRELÆRERE SIN toString()
	private void allLicencedInstructors()
	{
		ArrayList <Instructor> inssWithLicence = index.listInssWithLicense();
	    if( inssWithLicence.isEmpty() )
	    {
			display.setText( "Ingen kjørelærer registrert" );
			return;
		}
	    Iterator <Instructor> i = inssWithLicence.iterator();
		modelins.clear();

		while( i.hasNext() )
		{
			modelins.addElement( i.next() );
		}
		inslist.validate();
	    display.setText ( "" );
	}

	// VISER ALLE KJØRELÆRERE MED VALGT SERTIFIKAT SIN toString()
	private void allInstructorsChosenLicence()
	{
		ArrayList <Instructor> inssChosenLicence = index.listInssChosenLicense( Character.toString(license) );
	    if( inssChosenLicence.isEmpty() )
	    {
			display.setText( "Ingen kjørelærer registrert" );
			modelins.clear();
			return;
		}
	    Iterator <Instructor> i = inssChosenLicence.iterator();
		modelins.clear();

		while( i.hasNext() )
		{
			modelins.addElement( i.next() );
		}
		inslist.validate();
		display.setText( "" );
	    }

   // VISER ALLE KJØRELÆRERE SOM HAR GITT AVTALE MED VALGT KJØRETIME toString()
   private void allInstructorsChosenAssignment()
   {
	   ArrayList <Instructor> inssWithDrive = index.listInssWithDrive( assignmentname );
	   Iterator <Instructor> i = inssWithDrive.iterator();
	   modelins.clear();

	   while( i.hasNext() )
	   {
		   modelins.addElement( i.next() );
	   }
	   inslist.validate();
	   return;
   }

   // VISER ALLE AVTALER PÅ VALGT KJØRELÆRE
   private void AllAgreementsChosenInstructor()
   {
	   Agreement p = updateinstructor.headAgreement;
	   modelagreement.clear();

	   while( p != null )
	   {
		   modelagreement.addElement( p );
		   p = p.nextInsP;
	   }
	   agreementlist.validate();
   }

   // VISER ALLE AVTALER INNEN GITT KATEGORI PÅ VALGT KJØRELÆRE
   private void AllAgreementChosenCategory()
   {
	   if( updateinstructor == null )
	   {
		   display.setText( "Velg en kjørelærer" );
		   return;
	   }
	   ArrayList <Agreement> agreementsChosenCategory = updateinstructor.listAgreementsCategory( category );

	   if( agreementsChosenCategory.isEmpty() )
	   {
		   display.setText( "Ingen avtaler innen denne kategorien registrert" );
		   return;
	   }
	   Iterator <Agreement> i = agreementsChosenCategory.iterator();
	   modelagreement.clear();

	   while( i.hasNext() )
	   {
		   modelagreement.addElement( i.next() );
	   }
	   agreementlist.validate();
	}


   // VISER ALLE AVTALER INNEN GITT SERTIFIKAT PÅ VALGT KJØRELÆRER
   private void AllAgreementChosenLicense()
   {
	   if (updateinstructor == null)
	   {
		   display.setText( "Velg en kjørelærer" );
    	   return;
       }
	   ArrayList <Agreement> AgreementsChosenLicence = updateinstructor.listAgreementsGroups( license );

	   if( AgreementsChosenLicence.isEmpty() )
	   {
		   display.setText( "Ingen avtaler med denne sertifikaten registrert" );
		   return;
	   }
	   Iterator<Agreement> i = AgreementsChosenLicence.iterator();
	   modelagreement.clear();

	   while( i.hasNext() )
	   {
		   modelagreement.addElement( i.next() );
	   }
		   agreementlist.validate();
   }

   // LYTTEKLASSE, KNAPPER FOR VINDUET I KJØRELÆRERDETALJER
   public void actionPerformed( ActionEvent e )
   {
	   // KNAPPE FUNKSJONER FOR Å HENTE INFORMASJON OM KJØRELÆRERE OG AVTALER
	   if( e.getSource() == button[ ALL_INSTRUCTORS ] )
	   {
		   allInstructors();
       }

       else if( e.getSource() == button[ ALL_LICENCED_INSTRUCTORS ] )
       {
		   allLicencedInstructors();
   	   }

   	   else if( e.getSource() == button[ ALL_INSTRUCTORS_CHOSEN_ASSIGNMENT ] )
       {
		   assignmentname = ( String ) combobox[ ASSIGNMENT_LIST ].getSelectedItem ();
	 	   allInstructorsChosenAssignment();
       }

       else if( e.getSource() == button[ ALL_INSTRUCTORS_CHOSEN_LICENCE ] )
       {
		   allInstructorsChosenLicence();
       }

       else if( e.getSource() == button[ ALL_AGREEMENTS_CHOSEN_CATEGORY ] )
       {
		   AllAgreementChosenCategory();
       }

       else if( e.getSource() == button[ ALL_AGREEMENTS_CHOSEN_LICENSE ] )
       {
		   AllAgreementChosenLicense();
       }

       // OPPDATERER DATATYPER FRA COMBOBOXER TIL BRUK I ANDRE METODER
       else if( e.getSource() == combobox[ ASSIGNMENT_LIST ] )
       {
		   assignmentname = ( String )combobox[ ASSIGNMENT_LIST ].getSelectedItem();
       }

       else if( e.getSource() == categoryBox )
       {
		   category = ( String )categoryBox.getSelectedItem();
       }

       else if( e.getSource() == combobox[ ASSIGNMENT_LICENCE ] )
       {
		   String help = ( String )combobox[ ASSIGNMENT_LICENCE ].getSelectedItem();
	       license = help.charAt( 0 );
       }
   }

   // OPPDATERER KJØRELÆRER OG AVTALER OBJEKTET SOM ER VALGT I GUI
   public void valueChanged( ListSelectionEvent e )
   {
	   if( !e.getValueIsAdjusting() && e.getSource() == inslist )
	   {
		   updateinstructor = ( Instructor )inslist.getSelectedValue();
	       if( updateinstructor == null )
	       return;

		   // OPPDATERER JLIST ALLE AVTALER TIL VALGT KJØRELÆRER
	       AllAgreementsChosenInstructor();
	       display.setText (updateinstructor.AllInfo());
	    }

	    else if( !e.getValueIsAdjusting() && e.getSource() == agreementlist )
	    {
			agreementname = ( Agreement )agreementlist.getSelectedValue();
	        if( agreementname == null )
	    		return;
	    	display.setText( agreementname.allInfo() );
	    }
    }

    // LYTTEKLASSE, OG LYTTER PÅ TEKSTFELTENE OG OPPDATERER JLISTENE FORTLØPENDE
	public void keyTyped( KeyEvent e )
    {
    }

    public void keyPressed( KeyEvent e )
    {
    }

    public void keyReleased( KeyEvent e )
    {
	    if( e.getSource() == field[ NAME ] )
	    {
	 	    partofinsname =  field[ NAME ].getText();
	   	    if( partofinsname == null )
	   	    return;
	   	    index.listInssWithName( partofinsname, inslist, modelins );
   	    }

   	    if( e.getSource() == field[ ADRESS ] )
   	    {
		    partofadress = field[ ADRESS ].getText();
	        if( partofadress == null )
	        return;
	        index.listInssWithAdress( partofadress, inslist, modelins );
        }

        if( e.getSource() == field[ ASSIGNMENT ] )
        {
		    partofassignmentname =  field[ ASSIGNMENT ].getText();
	        if( partofassignmentname == null )
	    	    return;
	        DriveType.getAssignments( partofassignmentname, combobox[ ASSIGNMENT_LIST ] );
        }
    }
}