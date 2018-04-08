// INNEHOLDER VINDUET OG METODER FOR AVTALE REGISTERET, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETTNINGER
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.*;

// HOVEDVINDU
public class MainFrame extends JFrame implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1001L;
    private AgreementReg regp;
    private CandidateDetails cand;
    private Instructordetails instructorDetails;
    private Statistics stat;
    private JTabbedPane tabs;
    private Register reg;
    private Instructorindex dindex;
    private Candidateindex pindex;

    // STRING MED KATEGORIER
    private final static String[] categories =
    {
    	    "Trinn 1 (MC)",
		    "Trinn 2 (MC)",
		    "Trinn 3 (MC)",
		    "Trinn 1 (BIL)",
		    "Trinn 2 (BIL)",
		    "Trinn 3 (BIL)",
		    "Trinn 1 (LASTEBIL)",
		    "Trinn 2 (LASTEBIL)",
	        "Trinn 3 (LASTEBIL)",
		    "Førerprøve (MC)",
		    "Førerprøve (BIL)",
    	    "Førerprøve (LASTEBIL)"
    };

    // KONSTRUKTØR
	public MainFrame()
	{
		super( "Trafikkstasjon" );
		Register.getInstance().load();

		// OPPTRETTER EN PEKER TIL SEG SELV
		this.addWindowListener( new windowHandler() );

 		pindex = Register.getInstance().getCanIndex();
 		dindex = Register.getInstance().getInsIndex();

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		// OPPRETTER PANELER
  		MainFrame reference = this;

		instructorDetails = new Instructordetails( dindex, this );
		regp = new AgreementReg( dindex, pindex, this );
		cand = new CandidateDetails( dindex, pindex, this );
		stat = new Statistics( dindex, pindex, reference );
		Interface testvindu = new Interface( dindex, regp );

		// VINDUSDETALJER
		setSize( 858, 700 );
		setLocation( 0, 0 );
		setVisible( true );


		// OPPRETTER TABS
		tabs = new JTabbedPane();
		tabs.addTab( "Registrer avtale", regp );
		tabs.addTab( "Kjørelærer detaljer", instructorDetails );
		tabs.addTab( "Kandidat detaljer", cand );
		tabs.addTab( "Statistikk", stat );
		Container c = getContentPane();
		c.add( tabs );
	}

	// GET-METODE FOR OG HENTE PEKER
	protected AgreementReg getPointerToRegp()
	{
		return regp;
	}

	// GET-METODE FOR OG HENTE PEKER
	protected Instructordetails getPointerToInstructordetails()
	{
		return instructorDetails;
	}

	// RETURNERER ARRAY MED ALLE KATEGORIENE
	public static String[] getCategories()
	{
		return categories;
	}

	// HOVEDRAMME
	protected MainFrame getInstance()
	{
		return this;
	}

	public Statistics getPointerToStat()
	{
		return stat;
	}

	public CandidateDetails getPointertToCandidateDetails()
	{
		return cand;
	}

	// LAGRER ALT SOM ER GJORT FØR LUKKING AV PROGRAMMET
	private class windowHandler extends WindowAdapter
	{
		public void windowClosing( WindowEvent arg0 )
		{
			Register.getInstance().save();
			System.out.println( "Nå er det lagret" );
		}
	}
}