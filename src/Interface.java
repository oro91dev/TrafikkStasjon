// INNEHOLDER ADMINISTRATORVINDUET, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETNINGER
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.border.Border;

public class Interface extends JFrame implements ActionListener, Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1005L;
	private String[] fieldnames = { "Navn", "Kontoradresse" };
	private String[] buttonames ={ "Registrer ny", "Endre sertifikat", "Vis alle", "Slett Kjørelærer" };
	private String listname = "Tillatelse";
	private JButton[] button;
	private JTextField[] field;
	private JComboBox licenceList;
	private JTextArea display;
	private JScrollPane scrollfield;
	private Instructorindex index;
	private JLabel[]label;
	private JLabel infoLabel;
	private JLabel listLabel;
	private AgreementReg regp;

	private JPanel p1, p2, p3;
	private final int TEXTFIELD_SIZE = 10, NAME = 0, ADRESS = 1, LICENSE = 2,
	REG_NEW = 0, CHANGE_LI = 1, DISPLAY_ALL = 2, DELETE = 3;
	String hjelp = "( I = Ingen sertifikat )";


	// KONSTRUKTØR
	public Interface( Instructorindex index, AgreementReg regp )
	{
		super( "Trafikkstasjon/Administrator" );
		this.index = index;
		this.regp = regp;

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		Border etched = BorderFactory.createEtchedBorder();
	    Border titled = BorderFactory.createTitledBorder( etched, "Kjørelærer" );

	    // TEKSTFELTER FOR VINDUET OG GRIDLAYOUT FOR DET LILLE VINDUET
		p1 = new JPanel( new GridLayout( fieldnames.length +2, 2, 1, 1 ) );
		p1.setBorder( titled );
		field = new JTextField[ fieldnames.length ];
		label = new JLabel[ fieldnames.length ];
		for( int i = 0; i < fieldnames.length; i++ )
		{
			 field[ i ] = new JTextField( TEXTFIELD_SIZE );
			 label[ i ] = new JLabel( fieldnames[ i ] );
			 p1.add( label[ i ] );
			 p1.add( field[ i ] );
		}

		listLabel = new JLabel( listname );
		p1.add(listLabel);
		licenceList = new JComboBox(Main.avaiableLicences);
		p1.add(licenceList);

		infoLabel = new JLabel( hjelp );
		p1.add ( infoLabel );

		// TELLER OPP OG LEGGER TIL KNAPPER
		p2 = new JPanel( new FlowLayout() );
		button = new JButton[ buttonames.length ];
		for( int i = 0; i < buttonames.length; i++ )
		{
			button[ i ] = new JButton( buttonames[ i ] );
			button[ i ].addActionListener( this );
			p2.add( button[ i ] );
		}


		// FYLLER RAMMEN
		p3 = new JPanel( new BorderLayout( 6,6 ) );
		p3.add( p1,BorderLayout.NORTH );
		p3.add( p2,BorderLayout.SOUTH );
		c.add(  p3,BorderLayout.NORTH );
		display = new JTextArea( 10,10 );
		scrollfield = new JScrollPane( display );
		c.add( scrollfield, BorderLayout.CENTER );
		this.setSize( 858, 700 );
		setLocation( 895, 0 );
		this.setVisible( true );
	}

	// RENSER TEKSTFELTENE
	private void clearAllFields()
	{
		for( int i = 0; i < field.length; i++ )
		field[ i ].setText( "" );
	}

	// SJEKKER AT ALLE FELTER ER FYLT UT
	private boolean textInputOk( int CHOICE )
	{
		switch( CHOICE )
		{
			case REG_NEW:
			for( int i = 0; i < field.length - 1 ; i ++ )
			{
				if( field[ i ].getText().length() < 1 )
					return false;
	        }
		    return true;

		    case LICENSE:
		    for( int i = 0; i < field.length ; i ++ )
		    {
				if( field[i].getText().length() < 1 )
					return false;
            }
	        return true;
		}
		return false;
	}

	// DROPPER DOBBEL REGISTRERING
	private void registerNewInstructor()
	{
		Instructor n = new Instructor( field[ NAME ].getText(), field[ ADRESS ].getText(), licenceList.getSelectedItem().toString());
		index.registerNewInstructor( n );
		display.setText( "Instructor " + field[ NAME ].getText() + " ble registrert!" );
 		regp.putRightInssInList();
	}

	// ENDRER GITT KJØRELÆRENS SERTIFIKAT
	private void changeInstructorsLicense()
	{

		Instructor d;
		String a = field[ ADRESS ].getText(), n = field[ NAME ].getText();
		if( ( d = index.findInstructor( a, n ) ) == null )
		{
			clearAllFields();
			return;
		}

		String license = licenceList.getSelectedItem().toString();

		d.setLicence( license );
		display.setText( "Endret sertifikat på kjørelærer " + n );
		regp.putRightInssInList();
	}

	// SLETTER KJØRELÆRER UTEN REGISTRERTE AVTALER
	public void deleteInstructor()
	{
		if ( !textInputOk(REG_NEW) )
		{
			display.setText( "Minst en bokstav i hvert felt" );
			return;
		}
		String a = field[ ADRESS ].getText(), n = field[ NAME ].getText();
		if ( index.deleteInstructor(a, n) )
		{
			clearAllFields();
			display.setText( "Kjørelæreren er slettet fra systemet" );
		}
		regp.putRightInssInList();
	}

    // VISER ALLE REGISTRERTE KJØRELÆRERE SIN toString()
    private void displayAllInstructors()
    {
    	display.setText( index.listInstructors() );
    }

    // LYTTEKLASSE, KNAPPER FOR VINDUET I ADMINISTRASJON/TRAFIKKSTASJON
    public void actionPerformed( ActionEvent e )
    {
		if( e.getSource() == button[ REG_NEW ] )
		{
			registerNewInstructor();
		}
		else if( e.getSource() == button[ CHANGE_LI ] )
		{
			changeInstructorsLicense();
		}
		else if( e.getSource() == button[ DISPLAY_ALL ] )
		{
			clearAllFields();
			displayAllInstructors();
		}
		else if( e.getSource() == button[ DELETE ] )
		{
			deleteInstructor();
			clearAllFields();
		}
		clearAllFields();
	}
}