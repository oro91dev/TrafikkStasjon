// INNEHOLDER KANDIDATVINDU, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.*;

public class CandidateDetails extends JPanel implements ActionListener,Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1012L;

	protected Instructorindex doc;
	protected Candidateindex can;
	protected String[] pregfieldnames = { "Navn", "Fødselsdato", "Kjøretime navn", "Kjøretime gruppe", "Kjøretime kategori" };
	protected JTextField[] pregfield;
	protected JButton[] pregbuttons;
	protected String[] pregbuttonNames = { "Ny kandidat", "Slett kandidat","Vis alle kandidater", "Finn kandidat", "Vis kandidatinfo",
										   "Vis Kjøretime kategori", "Vis Kjøretime ", "Vis Kjøretime gruppe", "Endre kandidatinfo", "OK", "Angre" };

	protected JLabel[] preglabels;
	protected JPanel p1, p2;
	private final int	REG_CANDIDATE = 0, NAME = 0,
						DELETE_CANDIDATE = 1,D_OF_BIRTH = 1,
						SHOW_ALL_CANDIDATES = 2, ASSIGNMENTNAME = 2,
						FIND_CANDIDATE = 3, DRIVEGROUP = 3,
						LIST_ALL_AGREEMENTS_OF_CANDIDATE = 4, CATEGORY = 4,
						FIND_CANDIDATE_WITH_CATEGORY = 5,
						FIND_CANDIDATE_WITH_ASSIGNMENT = 6,
						FIND_CANDIDATE_WITH_DRIVE_GROUP = 7,
						CHANGE_CANDIDATE_INFO = 8,
						CONFIRM = 9,
						CANCEL = 10;
	private JTextArea display;
	private boolean checked = false;
	private String pname, pDayOfBirth;
	private Candidate candidate;
	private AgreementReg pr;

	private String[] assignment, categories;
	private String[] licenses = { "A", "B", "C" };

	private JComboBox categoryBox, groupBox, assignmentBox;

	private String assignmentname = "";

	private int lengthJComboBox = 5;

	// KONSTRUKTØR
	public CandidateDetails( Instructorindex doc, Candidateindex can, MainFrame reference )
	{
		this.doc = doc;
		this.can = can;
		this.setLayout( new BorderLayout( 10,10 ) );
		pr = reference.getPointerToRegp();

		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder( etched, "Kandidat" );

		preglabels = new JLabel[ pregfieldnames.length ];
		pregfield = new JTextField[ pregfieldnames.length ];
		p1 = new JPanel( new GridLayout( pregfieldnames.length, 2 ,1,1 ) );
		p1.setBorder( titled );

		assignmentBox = new JComboBox ();
		DriveType.getAssignments ( assignmentname, assignmentBox );
		assignmentBox.setMaximumRowCount( lengthJComboBox );
		assignmentBox.setSelectedIndex( 0 );
		assignmentBox.addActionListener( this );

		categories = reference.getCategories();
		categoryBox = new JComboBox( categories );
		categoryBox.setMaximumRowCount( lengthJComboBox );
		categoryBox.setSelectedIndex( 0 );
		categoryBox.addActionListener( this );

		groupBox = new JComboBox( licenses );
		groupBox.setMaximumRowCount( lengthJComboBox );
		groupBox.setSelectedIndex( 0 );
		groupBox.addActionListener( this );

		for( int i = 0; i < pregfieldnames.length; i++ )
		{
			if( i == 0 || i == 1 )
			{
				pregfield[ i ] = new JTextField();
				preglabels[ i ] = new JLabel( pregfieldnames[ i ] );
				p1.add( preglabels[ i ] );
				p1.add( pregfield[ i ] );
			}

			if( i == 2 )
			{
				preglabels[ i ] = new JLabel( pregfieldnames[ i ] );
				p1.add( preglabels[ i ] );
				p1.add( assignmentBox );
			}

			if( i == 3 )
			{
				preglabels[ i ] = new JLabel( pregfieldnames[ i ] );
				p1.add( preglabels[ i ] );
				p1.add( groupBox );
			}

			if( i == 4 )
			{
				preglabels[ i ] = new JLabel( pregfieldnames[ i ] );
				p1.add( preglabels[ i ] );
				p1.add( categoryBox );
			}
		}

		pregbuttons = new JButton[ pregbuttonNames.length ];
		p2 = new JPanel( new GridLayout( 2, pregbuttonNames.length, 1, 1 ) );

		for( int i = 0; i < pregbuttons.length; i++ )
		{
			pregbuttons[ i ] = new JButton( pregbuttonNames [ i ] );
			pregbuttons[ i ].addActionListener( this );
			p2.add( pregbuttons[ i ] );
		}
		display = new JTextArea();

		JScrollPane scrollDisplay = new JScrollPane( display );

		add( scrollDisplay, BorderLayout.CENTER );
		add( p1, BorderLayout.PAGE_END );
		add( p2, BorderLayout.PAGE_START );

		pregbuttons[ pregbuttonNames.length -1 ].setEnabled( false );
		pregbuttons[ pregbuttonNames.length -2 ].setEnabled( false );
	}

    // JCOMBOBOX FOR AVTALER
	public JComboBox getDriveBox()
	{
		return assignmentBox;
	}

	// TESTER OM FELTER ER FYLT UT
	private boolean isCanFieldsOK()
	{
		return pregfield[ NAME ].getText().matches( "\\D{1,100}" )
			   && pregfield[ D_OF_BIRTH ].getText().matches( "\\d{6}" );
	}

	// RENSER FELTER
	private void cleanFields()
	{
		for( int i = 0; i <= D_OF_BIRTH; i++ )
			pregfield[ i ].setText( "" );
	}

	// REGISTRERER EN NY KANDIDAT
	private void registerCandidate()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Utfyllingen av navne feltet er ikke ok" );
			return;
		}
		Candidate n = new Candidate( pregfield[ NAME ].getText(),pregfield[ D_OF_BIRTH ].getText() );
		display.setText( can.newCandidate( n ) );
		cleanFields();
		showAll();
		pr.updateCandidates2ComboBox();
	}

	// SLETTER KANDIDAT UTEN AVTALER
	private void deleteCandidate()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Utfyllingen av felter er ufullstendig!" );
			return;
		}
		display.setText( can.deleteCandidate( can.findCandidate( pregfield[ D_OF_BIRTH ].getText(),
					pregfield[ NAME ].getText() ) ) );
		pr.updateCandidates2ComboBox();
	}

	// VISER SAMTLIGE KANDIDATER
	private void showAll()
	{
		display.setText( can.listAllCandidates() );
	}

	// FINNER BESTEMT KANDIDAT
	private void findCandidate()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Vennligst fyll ut nødvendige datafelt" );
			return;
		}

		try
		{
			display.setText( can.findCandidate( pregfield[ D_OF_BIRTH ].getText(),
									 pregfield[ NAME ].getText() ).toString() );
		}

		catch( NullPointerException nfe )
		{
			display.setText( "Finnes ingen kandidat med dette navn og fødselsdato" );
		}

	}

    // FINNER UT OM KANDIDATEN HAR AVTALER OG VISER DEM
	private void showAllCandidateAgreements()
	{
		Candidate p;
		if( !( ( p = ( can.findCandidate( pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ) ) ) != null) )
		{
			display.setText( "Finnes ingen kandidat med dette navn og fødselsdato" );
			return;
		}
		display.setText( p.getAgreement() );
	}

    // FINNER OG VISER ALLE AVTALER MED DENNE KATEGORIEN
	private void showAllAgreementsWithCategory()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Vennligst fyll ut nødvendige datafelt" );
			return;
		}
		display.setText( can.listGroupCategory( ( String )categoryBox.getSelectedItem() , pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ) );

		if( can.listGroupCategory( (String)categoryBox.getSelectedItem() , pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ).matches( "$" ) )
				display.setText( "Ingen avtaler registrert med denne kategorien" );
	}

	private void showAllAgreementsWithAssignment()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Vennligst fyll ut nødvendige datafelt" );
			return;
		}
		display.setText( can.listCandidateAssignment( ( String )assignmentBox.getSelectedItem(), pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ) );
	}

    // VISER ALLE AVTALE FOR KJØRETYPE GRUPPEN
	private void showAllAgreementsWithDriveGroup()
	{
		display.setText( "showAllAgreementsWithDriveGroup()" );

		if( !isCanFieldsOK() )
		{
			display.setText( "Vennligst fyll inn Navn og fødselsdato" );
			return;
		}

		try
		{
			String temp = ( String )groupBox.getSelectedItem();
			char s = temp.charAt( 0 );
			display.setText( can.listDriveGroup( s, pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ) );

			if( can.listDriveGroup( s, pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ).matches( "$" ) )
				display.setText( "Ingen avtaler registrert med denne kjøretime gruppen" );
		}

		catch( IndexOutOfBoundsException iobe )
		{
			display.setText( "Vennligst skriv inn en kjøretimegruppe A, B, eller C" );
		}
	}

    // ENDRING AV KANDIDAT INFO
	private void changeCandidateInfo()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Fødselsdato og Navn er ikke tilfredsstilt fyllt ut" );
			return;
		}

		if( !( ( candidate = ( can.findCandidate(pregfield[ D_OF_BIRTH ].getText(), pregfield[ NAME ].getText() ) ) ) != null) )
		{
			display.setText( "Finnes ingen kandidat" );
			return;
		}
		display.setText( "Skriv inn den oppdaterte informasjonen og klikk på \"OK\" for å lagre endret informasjon" );

		for( int i = 0; i < ( pregbuttonNames.length -1 ); i++ )
		{
			pregbuttons[ i ].setEnabled( false );
		}
		pregbuttons[ pregbuttonNames.length -2 ].setEnabled( true );
		pregbuttons[ pregbuttonNames.length -1 ].setEnabled( true );
	}

    // BEKREFTER ENDRINGEN
	private void confirm()
	{
		if( !isCanFieldsOK() )
		{
			display.setText( "Feltene for fødselsdato og navn er ikke tilfredsstilt fyllt ut" );
			return;
		}

		if( candidate.getName().equalsIgnoreCase( pregfield[ NAME ].getText() ) && candidate.getDateOfBirth().equalsIgnoreCase( pregfield[ D_OF_BIRTH ].getText() ) )
		{
			display.setText( "Ingen endring utført. Handling avsluttet" );
			return;
		}

		candidate.setName(pregfield[ NAME ].getText() );
		candidate.setDateOfBirth(pregfield[ D_OF_BIRTH ].getText() );
		display.setText( "Oppdatering av kandidat var vellykket" );

		for( int i = 0; i < ( pregbuttonNames.length -2 ); i++ )
		{
			pregbuttons[ i ].setEnabled( true );
		}
		pr.updateCandidates2ComboBox();
		pregbuttons[ pregbuttonNames.length -1 ].setEnabled( false );
		pregbuttons[ pregbuttonNames.length -2 ].setEnabled( false );
	}

    // AVBRYTER ENDRINGEN
	private void cancel()
	{
		candidate = null;
		display.setText( "Endringer kansellert" );

		for( int i = 0; i < ( pregbuttonNames.length -2 ); i++)
		{
			pregbuttons[ i ].setEnabled( true );
		}
		pregbuttons[ pregbuttonNames.length -1 ].setEnabled( false );
		pregbuttons[ pregbuttonNames.length -2 ].setEnabled( false );
		return;
	}

	// LYTTEKLASSE, KNAPPER FOR VINDUET I KANDIDATDETALJER
	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource () == pregbuttons[ REG_CANDIDATE ] )
			registerCandidate ();
		else if( e.getSource () == pregbuttons [ DELETE_CANDIDATE ] )
			deleteCandidate ();
		else if( e.getSource() == pregbuttons[ SHOW_ALL_CANDIDATES ] )
			showAll();
		else if( e.getSource() == pregbuttons[ FIND_CANDIDATE ] )
			findCandidate();
		else if( e.getSource() == pregbuttons[ LIST_ALL_AGREEMENTS_OF_CANDIDATE ] )
			showAllCandidateAgreements();
		else if( e.getSource() == pregbuttons[ FIND_CANDIDATE_WITH_CATEGORY ] )
			showAllAgreementsWithCategory();
		else if( e.getSource() == pregbuttons[ FIND_CANDIDATE_WITH_ASSIGNMENT ] )
			showAllAgreementsWithAssignment();
		else if( e.getSource() == pregbuttons[ FIND_CANDIDATE_WITH_DRIVE_GROUP ] )
			showAllAgreementsWithDriveGroup();
		else if( e.getSource() == pregbuttons[ CHANGE_CANDIDATE_INFO ] )
			changeCandidateInfo();
		else if( e.getSource() == pregbuttons[ CONFIRM ] )
			confirm();
		else if( e.getSource() == pregbuttons[ CANCEL ] )
			cancel();
	}
}