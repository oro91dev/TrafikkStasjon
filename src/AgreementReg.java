// INNEHOLDER METODER FOR VINDUET FOR AVTALE REGISTERET, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETNINGER
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class AgreementReg extends JPanel implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1014L;

	// VIKTIGE HENVISNINGER
	private Candidateindex can;
	private Instructorindex ins;
	private MainFrame frame;
	private ActionEventHandler aHandler;
	private ItemEventHandler iHandler;

	// MIDLERTIDIGE HENVISNINGER
	private Instructor instructor = null;
	private Candidate candidate = null;
	private Agreement agreement = null;
	private ArrayList <DriveType> dlist = new ArrayList <DriveType> ();

	// VINDUS KOMPONENTER
	private JLabel[] dpFieldLabels, dpRadioLabels, mLabels, cLabels;
	private JLabel insOrdLabel;
	private JButton[] allButtons;
	private JTextField[] dpTextField, mTextField;
	private JComboBox[] dpListBox;
	private JComboBox[] cComboBox;
	private ButtonGroup[] radioButtonGroup;
	private JRadioButton[] radioButton;
	private JPanel p1, p2, p3, p4, p5, p6, p7, p8;
	private JPanel[] radioPanel;
	private JPanel[] buttonPanel;
	private JTextArea display, insOrders;
	private JScrollPane scroll, mScroll;


	// KONSTANTER OG STRENGER FOR VINDUET
	private String []
	 dpFieldLabelNames     = { "Kjørelærer navn:", "Kandidatens navn/fødsdato:" }
	,dpRadioLabelNames     = { "Kjørelærer med sertifikat:", "Velg kandidat ut i fra:" }
    ,mLabelNames           = { "Kjøretime navn:", "Gruppe (A, B, C):", "Tid/Lengde (Kun siffer):", "Enhet (min, km):" }
	,cLabelNames           = { "Kategori:"}
	,radioButtonNames      = { "A", "B", "C", "Navn", "Føds.dato:" }
	,dpButtonNames         = { "OK", "Angre" }
	,mButtonNames          = { "Registrer", "Angre" }
	,fButtonNames          = { "Fullfør", "Avbryt" }
	,dpErrorMessages       = { "Matcher ikke utrykket for kjørelærer!", "Matcher ikke utrykket for kandidater!" };
	private String[][]
	 buttonNames           = { dpButtonNames,mButtonNames,fButtonNames };
	private String[][]
	 cComboBoxValues        = { MainFrame.getCategories() };
	private final int
	 DP_GRIDCOLUMNS        = 4, DP_GRIDROWS = 2, MAX_NR_OF_B = 3, NR_OF_COMB_BOX = 2
	                        ,TOTAL_NR_BUTTONS = dpButtonNames.length + mButtonNames.length + fButtonNames.length
	                        ,B_PANEL1 = 0,B_PANEL2 = 1,B_PANEL3 = 2,CAN = 1,INS = 0, A=0, B=1, C=2;
	private boolean groupA = true, groupB = false, groupC = false, nameMode = true, dateOfBirthMode = false;
	private String pickInsRegX = "\\D*", dpDayOfBirth = "\\d{0,6}", canName = "\\D*";
	private char a = 'A', b = 'B', c = 'C';
	private String insOrdLabelName       = "Kjørelærens kommentar: "
		          ,firstPBordTitle       = " 1.Velg kjørelærer og kandidat "
		          ,secondPBordTitle      = " 2.Legg til kjørelæremiddler "
		          ,thirdPBordTitle       = " 3.Kontroller og fullfør "
		          ,section1StartInfo     = " 1.Velg kandidaten avtalen skal registreres på.\n" +
		          	                       "     Velg deretter kjørelæren som har skrevet ut avtalen.\n" +
		                                   "     Trykk ok når du har funnet riktig kjørelærer og kandidat!\n"
		          ,section2StartInfo     = " 2.Du kan registrere en eller flere kjøretimer ved å fylle ut feltene og trykke registrer.\n"
		          ,section3StartInfo     = "3.Avtalen er registrert:\n";

	// KONSTRUKTØR
	public AgreementReg( Instructorindex ins, Candidateindex can, MainFrame reference )
	{

		// INITIALISERING AV HENVISNINGENE
		this.ins = ins;
		this.can = can;
		frame = reference;
		aHandler = new ActionEventHandler();
		iHandler = new ItemEventHandler();

	  	// SETTER LAYOUTEN I PANELET
		setLayout( new BorderLayout() );
		Border standard = BorderFactory.createEtchedBorder();
	    Border firstMainPanel = BorderFactory.createTitledBorder( standard, firstPBordTitle );

		// BYGGER KNAPPEPANEL
		buttonPanel = new JPanel[ buttonNames.length ];
		allButtons = new JButton[ TOTAL_NR_BUTTONS ];
		for( int i = 0,k = 0; i < buttonNames.length; i++ )
		{
			buttonPanel[ i ] = new JPanel();
			for( int j = 0; j < buttonNames[ i ].length; j++,k++)
			{
				buttonPanel[ i ].add( allButtons[ k ] = new JButton(buttonNames[ i ][ j ] ) );
				allButtons[ k ].addActionListener( aHandler );
			}
		}

		// BYGGER UT FØRSTE DEL AV PANELET
		p1 = new JPanel( new GridLayout( DP_GRIDROWS, DP_GRIDCOLUMNS, 10, 2 ) );
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel( new BorderLayout() );
		radioPanel = new JPanel[ dpFieldLabelNames.length ];
		dpFieldLabels = new JLabel[ dpFieldLabelNames.length ];
		dpTextField = new JTextField[ dpFieldLabelNames.length ];
		dpListBox = new JComboBox[ dpFieldLabelNames.length ];
	    dpRadioLabels = new JLabel[ dpRadioLabelNames.length ];
		radioButton = new JRadioButton[ radioButtonNames.length ];
		radioButtonGroup = new ButtonGroup[ dpFieldLabelNames.length ];

		for( int i = 0,j = 0; i < dpFieldLabelNames.length; i++ )
		{
			p1.add( dpFieldLabels[ i ] = new JLabel( dpFieldLabelNames[ i ] ) );
			p1.add( dpTextField[ i ] = new JTextField() );
			dpTextField[ i ].addActionListener( aHandler );
			dpListBox[ i ] = new JComboBox();
			radioButtonGroup[ i ] = new ButtonGroup();
			radioPanel[ i ] = new JPanel();
			p1.add( dpRadioLabels[ i ] = new JLabel( dpRadioLabelNames[ i ] ) );
			boolean defaultButton = false;

			for( int k = 0; k < MAX_NR_OF_B && j < radioButtonNames.length ; k++,j++ )
			{
			    if( k == 0 )
			    	defaultButton = true;
			    else defaultButton = false;
				radioPanel[ i ].add( radioButton[ j ] = new JRadioButton(radioButtonNames[ j ], defaultButton ) );
				radioButtonGroup[ i ].add( radioButton[ j ] );
				radioButton[ j ].addItemListener( iHandler );
			}
			p1.add( radioPanel[ i ] );
			p1.add( dpListBox[ i ] );
		}
		p4.add( p1,BorderLayout.CENTER );
		p4.add( buttonPanel[ this.B_PANEL1 ], BorderLayout.PAGE_END );
		p4.setBorder( firstMainPanel );
		add( p4,BorderLayout.PAGE_START );

		// BYGGER UT ANDRE DEL AV PANELET
		Border secondMainPanel = BorderFactory.createTitledBorder( standard, secondPBordTitle );
		p5 = new JPanel( new BorderLayout() );
		p5.add( new JLabel( "     " ), BorderLayout.LINE_START );
		p5.add( new JLabel( "     " ), BorderLayout.LINE_END );
		p6 = new JPanel( new BorderLayout() );
		p7 = new JPanel( new GridBagLayout() );
		p8 = new JPanel( new GridLayout( 5, 2, 1, 1 ) );
		insOrders = new JTextArea( 4, 20 );
		mScroll = new JScrollPane( insOrders );
		mLabels = new JLabel[ mLabelNames.length ];
		cLabels = new JLabel[ cLabelNames.length ];
		mTextField = new JTextField[ mLabelNames.length ];
		cComboBox = new JComboBox[ cLabelNames.length ];

		for( int i = 0; i < mLabelNames.length; i++ )
		{
			p8.add( mLabels[ i ] = new JLabel( mLabelNames[ i ] ) );
			p8.add( mTextField[ i ] = new JTextField() );
			mTextField[i].addActionListener( aHandler );
		}
		
		for( int i = 0; i < cLabelNames.length; i++ )
		{
			p8.add( cLabels[ i ] = new JLabel( cLabelNames[ i ] ) );
			p8.add( cComboBox[ i ] = new JComboBox(cComboBoxValues[i]) );
			cComboBox[i].addActionListener( aHandler );
		}

		p7.add( insOrdLabel = new JLabel( insOrdLabelName ) );
		p7.add( mScroll );
		p6.add( p7, BorderLayout.CENTER );
		p6.add( p8, BorderLayout.PAGE_START );
		p5.add( p6, BorderLayout.CENTER );
		p5.add( buttonPanel[ 1 ],BorderLayout.PAGE_END );
		p5.setBorder( secondMainPanel );
		add( p5,BorderLayout.CENTER );

		// BYGGER UT TREDJE DEL AV PANELET
		Border thirdMainPanel = BorderFactory.createTitledBorder( standard, thirdPBordTitle );
		p2 = new JPanel( new BorderLayout() );
		p2.add( new JLabel( "     " ), BorderLayout.LINE_START );
		p2.add( new JLabel( "     " ), BorderLayout.LINE_END );
		display = new JTextArea( 10, 10 );
		scroll = new JScrollPane(display );
		p2.add( scroll, BorderLayout.CENTER );
		p2.add( buttonPanel[ 2 ], BorderLayout.PAGE_END );
		p2.setBorder( secondMainPanel );
		p2.setBorder( thirdMainPanel );
		add( p2, BorderLayout.PAGE_END );
		userGuidence();
	}

	// LEGGER TIL VALGT KJØRELÆRER INN I RIKTIG SERTIFIKAT LISTE
	public void putRightInssInList()
	{
		String input = dpTextField[ INS ].getText();
		if( !input.matches( pickInsRegX ) )
		{
			display.setText( dpErrorMessages[ INS ] );
			return;
		}

		if( groupA )
		{
			ins.listInssWithParameter( a, input, dpListBox[ INS ] );
		}

		else if( groupB )
		{
			ins.listInssWithParameter( b, input, dpListBox[ INS ] );
		}

		else if( groupC )
		{
			ins.listInssWithParameter( c, input, dpListBox[ INS ] );
		}
	}

	// LEGGER TIL VALGTE KANDIDATER INN UNDER SAMME LISTE
	public void putRightCansInList()
	{
		String input = dpTextField[ CAN ].getText();
	 	if( nameMode )
	 	{
	 		if( !input.matches( canName ) )
	 		{
	 			display.setText( dpErrorMessages[ CAN ] );
	 			return;
	 		}
	 		can.listCansWithName( input, dpListBox[ CAN ] );
	 	}

	 	else if( dateOfBirthMode )
	 	{
	 		if( !input.matches( dpDayOfBirth ) )
	 		{
	 			display.setText( dpErrorMessages[ CAN ] );
	            return;
	 		}
	 		can.listCansWithDateOfBirth(input,dpListBox[ CAN ] );
	 	}
	}

	// LAGRER VALGTE KANDIDATER OG INSTRUKTØRER
	private void rememberInsAndCandidate()
	{
		candidate = ( Candidate )dpListBox[ CAN ].getSelectedItem();
		instructor = ( Instructor )dpListBox[ INS ].getSelectedItem();
	    display.setText( ( candidate == null ) ? "Du må velge en kandidat før du går videre!\n" : candidate + "Valgt\n" );
		display.append( ( instructor == null ) ? "Du må velge en kjørelærer før du går videre!\n" : instructor +"Valgt\n" );

		if( candidate == null || instructor == null )
			return;
		dpDisableEditable();
		userGuidence();
	}

	// METODE SOM GLEMMER KJØRELÆRERE OG KANDIDATER
	private void forgetInsAndCandidate()
	{
		candidate = null;
		instructor = null;
		dpEnableEditable();
	}

	// AKTIVERER PANELET
	private void dpEnableEditable()
	{
		for( int i = 0; i < dpTextField.length; i++ )
		{
			dpTextField[ i ].setEditable( true );
			dpListBox[ i ].setEnabled( true );
		}

		for( int i = 0; i < radioButton.length;i++ )
		{
			radioButton[ i ].setEnabled( true );
		}
		allButtons[ 0 ].setEnabled( true );
	}

	// DEAKTIVERER PANELET
	private void dpDisableEditable()
	{
		for( int i = 0; i < dpTextField.length; i++ )
		{
			dpTextField[ i ].setEditable( false );
			dpListBox[ i ].setEnabled( false );
		}

		for( int i = 0; i < radioButton.length; i++ )
		{
			radioButton[ i ].setEnabled( false );
		}
		allButtons[ 0 ].setEnabled( false );
	}

	// FJERNER DET SOM ER INNE I TEXTFELTENE
	private void clean_dpFields()
	{
		for( int i = 0; i < dpTextField.length; i++ )
			dpTextField[ i ].setText( "" );
	}


	// VALIDERER INPUT-FELTENE INN FOR AVTALEN MELLOM KJØRERLÆRER OG KANDIDAT
	private boolean medInputOK()
	{
		DriveType d;

		if( instructor == null || candidate == null )
			return false;

		if( !mTextField[ 0 ].getText().matches( "\\D{1,100}" ) )
		{
			display.setText( "du må taste inn Kjøretimenavn" );
		    return false;
		}
		d = DriveType.getInstance( mTextField[ 0 ].getText() );

		if( !mTextField[1].getText().matches( "[a,A,b,B,c,C]{1}" ) )
		{
			display.setText( "du må taste inn kjøretimegruppe a,b eller c!" );
		    return false;
		}

		if( d != null && d.getdriveGroup() < instructor.getLicence().charAt(0) )
		{
			display.setText( mTextField[ 0 ].getText() + " Kjøretøyet er allerede registrert med i systemet under gruppe: " + d.getdriveGroup()
													   + "\n Kjørelæreren du har valgt har ikke annledning til og skrive ut denne kjøretimen" );
			return false;
		}

		if( !isCategoryValid() )
		{
			display.setText( "du må taste inn gyldig kjøretimekategori!" );
		    return false;
		}

		if( !mTextField[ 4 ].getText().matches( "[m,M,i,I,n,N,k,K]{2,3}" ) )
		{
			display.setText( "du må taste inn minutter!" );
		    return false;
		}
		return true;
	}

	// TESTET OM KATEGORI ER RIKTIG KATEGORI
	private boolean isCategoryValid()
	{
        String s = mTextField[ 2 ].getText();
	    if( s.length() == 0 )
	    	return false;
		String[] categories = frame.getCategories();
		for( int i = 0; i < categories.length; i++ )
		{
			if( categories[ i ].equalsIgnoreCase( s ) )
				return true;
		}
		return false;
	}

	// LAGRER KJØRETIMEN TIL AVTALEN
	private void registerNewDrive()
	{
		if( instructor == null || candidate == null )
		{
			display.setText( "Du må velge kjørelærer og kandidat før du kan registrere kjørelærermidler på avtalen!" );
			return;
		}

		if( instructor.getLicence() == "I" )
		{
			display.setText( instructor + " har ikke sertifikat, denne avtalen kan ikke registreres!" );
			return;
		}
		double quantity;
		String mName,mCat,mUnit;
		char mGroup;
		try
		{
			quantity = Double.parseDouble( mTextField[ 3 ].getText() );
		}

		catch( NumberFormatException nfe )
		{
			display.setText( "Du må skrive inn antall minutter i tid/lengde feltet" );
			return;
		}

		if( !medInputOK() )
			return;
		mName = mTextField[ 0 ].getText();
		mGroup = mTextField[ 1 ].getText().charAt( 0 );
		mGroup = Character.toUpperCase( mGroup );

		if( mGroup < instructor.getLicence().charAt(0) )
		{
			display.setText( instructor + " har ikke sertifikat for gruppe " + mGroup + ". " +
											"Registreringen kan ikke gjennomføres!" );
			return;
		}
		mCat = mTextField[ 2 ].getText();
		mUnit = mTextField[ 4 ].getText();

		DriveType temp = DriveType.add( mName, mGroup,mCat, quantity ,mUnit );
		temporaryDriveStorage( temp );
		clearMedFields();
		display.setText( dlist.toString() );
		JComboBox j = frame.getPointerToStat().getDriveBox();
		j.addItem( temp.getdriveName() );
	}

	// FJERNER SISTE KJØRETIME I LISTEN (TEMPDRIVELIST)
	private void removeLastDrive()
	{
		if( dlist.size() == 0 )
		{
			display.setText( "Ingen kjøretimer på avtalen!" );
			return;
		}
		dlist.remove( dlist.size()-1 );
		display.setText( "Du har lagt til følgende:\n\n" + dlist.toString() );
	}

    // GIR MELDING OM FLERE AVTALER PÅ SAMME PERSON BLIR REGISTRERT
	private void userGuidence()
	{
		if( instructor == null || candidate == null )
		{
			display.setText( section1StartInfo );
			return;
		}

		String info = candidate.getAllTodaysAgreements();

		if( info != null && info.length() > 0)
		{
			display.setText( section2StartInfo );
			display.append( candidate.getName() + " har registrert ut flere kjøretimer tiligere i dag!\n" );
			display.append( info );
		    display.setCaretPosition( 0 );
		    return;
		}

		if( dlist.size() == 0 )
		{
			display.setText( section2StartInfo );
            return;
		}
		display.setText( section3StartInfo );
	}

	// RENSER UT ALT FRA MIDTFELTET
	private void clearMedFields()
	{
		for( int i = 0; i < mTextField.length; i++ )
		{
			mTextField[ i ].setText( "" );
		}
	}

	// GODKJENNER AVTALEN FOR REGISTRASJON
	private void completeAgreement()
	{
		if( candidate == null )
		{
			display.append( "1.Velg en kandidat fra listen" );
			return;
		}

		if( instructor == null )
		{
			display.append( "1.Velg en kjørelærer fra listen" );
			return;
		}

		if( dlist.isEmpty() )
		{
			display.append( "2.Du må velge minst en kjøretime for og kunne registrere en avtale" );
		}

		String orders;

		try
		{
			orders = insOrders.getText();
		}

		catch( NullPointerException np )
		{
			display.setText( "NullpointerException ved forsøk på henting av tekst fra anvendelses felt!" );
			orders = "Følg pakningsvedlegg";
			return;
		}

		if( dlist.size() == 0 )
		{
			System.out.println( "Listen er tom" );
			return;
		}

		DriveType[] drives = dlist.toArray( new DriveType[ dlist.size() ] );
		agreement = new Agreement( candidate.getName(), candidate.getDateOfBirth(), instructor.getName(), instructor.getAdress(), drives, orders );
		instructor.newAgreement( agreement );
		candidate.newAgreement( agreement );
		userGuidence();
		insOrders.setText( "" );
		forgetTempReferences();
		dpEnableEditable();

		// OPPDATERER KJØRETIMENE FOR COMBOBOX I STATISTIKKEN
		Statistics stat = frame.getPointerToStat();
		DriveType.getAssignments( stat.getAssignmentName(), stat.getAssignmentBox() );
	}

	// FORSIKRER OM MULTIPLE KJØRETYPER PÅ AVTALEN
	private void temporaryDriveStorage( DriveType d )
	{
		int i = dlist.indexOf( d );
		if( dlist.size() == 0 || i == -1 )
		{
			dlist.add( d );
			return;
		}
		dlist.set( i, d );
	}

	// SLETTER ALT (GLEMMER ALT)
	private void forgetTempReferences()
	{
		candidate = null;
		instructor = null;
		agreement = null;
		dlist.removeAll( dlist );
	}

	// COMBOBOKS METODE OPPDATERER KJØRELÆRERE
	public void updateInstructors2ComboBox()
	{
		ArrayList <Instructor> inssWithLicence = ins.listInssWithLicense();
		Iterator <Instructor> i = inssWithLicence.iterator();
		dpListBox[ INS ].removeAllItems();
		while( i.hasNext() )
		{
			dpListBox[ INS ].addItem( i.next() );
		}
		dpListBox[ INS ].validate();
	}


	// COMBOBOKS METODE OPPDATERER KANDIDATENE
	public void updateCandidatesComboBox()
	{
		ArrayList <Candidate> candidates = can.listCandidates();
		Iterator <Candidate> i = candidates.iterator();
		dpListBox[ CAN ].removeAllItems();
		while( i.hasNext() )
		{
			dpListBox[ CAN ].addItem( i.next() );
		}
	}

	// COMBOBOKS METODE OPPDATERE KANDIDATENE.2
	public void updateCandidates2ComboBox()
	{
		can.listCandidates( dpListBox[ CAN ] );
	}

	// KNAPPER FOR DET FØRSTE VINDUET NR 1 KJØRELÆRER OG KANDIDAT (BUTTON HANDLER)
	private class ItemEventHandler implements ItemListener
	{
		public void itemStateChanged( ItemEvent event )
		{
			if( event.getSource() == radioButton[ A ] )
			{
				groupA = true;
				groupB = false;
				groupC = false;
				putRightInssInList();
				display.setText( "Valgt kjørelærer med A sertifikat!" );
			}

			else if( event.getSource() == radioButton[ B ] )
			{
				groupA = false;
				groupB = true;
				groupC = false;
				putRightInssInList();
				display.setText( "Valgt kjørelærer med B sertifikat!" );
			}

			else if( event.getSource() == radioButton[ C ] )
			{
				groupA = false;
				groupB = false;
				groupC = true;
				putRightInssInList();
				display.setText( "Valgt kjørelærer med C sertifikat!" );
			}

			else if( event.getSource() == radioButton[ 3 ] )
			{
				nameMode = true;
				dateOfBirthMode = false;
				putRightCansInList();
				display.setText( "Navne modul på!" );
			}

			else if( event.getSource() == radioButton[ 4 ] )
			{
				nameMode = false;
				dateOfBirthMode = true;
				putRightCansInList();
				display.setText( "Fødselsdato modul på!" );
			}
		}
	}

	// LYTTEKLASSE, KNAPPER FOR VINDUET I AVTALEREGISTERET
	private class ActionEventHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			if( e.getSource() == allButtons[ 0 ] )
			{
				rememberInsAndCandidate();

			}

			else if( e.getSource() == allButtons[ 1 ] )
			{
				forgetInsAndCandidate();
				userGuidence();
			}

			else if( e.getSource() == dpTextField[ INS ] )
			{
				putRightInssInList();
			}

			else if( e.getSource() == dpTextField[ CAN ] )
			{
				putRightCansInList();
			}

			else if( e.getSource() == allButtons[ 2 ] )
			{
				display.setText( "Trykket OK  knapp i med panel!" );
				if( medInputOK() )
					registerNewDrive();

			}

			else if( e.getSource() == allButtons[ 3 ] )
			{
				removeLastDrive();

			}

			else if( e.getSource() == allButtons[ 4 ] )
			{
				completeAgreement();
			}

			else if( e.getSource() == allButtons[ 5 ] )
			{
				forgetTempReferences();
				dpEnableEditable();
			}
		}
	}
}