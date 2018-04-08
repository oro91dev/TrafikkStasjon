// INNEHOLDER REGISTERET LAGRE METODER, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Register implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1004L;
    private static Register INSTANCE = new Register();
    private Object drivesMemento;
    private Instructorindex insindex;
    private Candidateindex canindex;

    // KONSTRUKTØR
    private Register()
    {
    	canindex = new Candidateindex();
    	insindex = new Instructorindex();
    }

    // RETURNERER INSTANCE, EN GET-METODE
    public static Register getInstance()
    {
    	return INSTANCE;
    }
    
    // GET-METODE FOR KANDIDAT INDEKS
    public Candidateindex getCanIndex()
    {
    	return canindex;
    }

    // GET-METODE FOR KJØRELÆRER INDEKS
    public Instructorindex getInsIndex()
    {
    	return insindex;
    }

    // LAGRE METODE
    public void save()
    {
		drivesMemento = DriveType.getMemento();
		try
    	{
			ObjectOutputStream utfil = new ObjectOutputStream( new FileOutputStream( "Memory.data" ) );
      		utfil.writeObject( this );
      		utfil.flush();
      		utfil.close();
    	}

    	catch( NotSerializableException nse )
    	{
			System.out.println( nse.getMessage() );
    	}

    	catch( IOException ioe )
    	{
    		System.out.println( ioe.getMessage() );
    	}
  	}

    // HENTER DATA FILEN, HVIS DEN IKKE FINNER NOEN MEMORY.DATA SÅ OPPRETTER DEN NY LISTE
	public void load()
  	{
		try
	  	{
			ObjectInputStream innfil = new ObjectInputStream( new FileInputStream( "Memory.data" ) );
	        INSTANCE = ( Register )innfil.readObject();
	        DriveType.setMemento( drivesMemento );
	        innfil.close();
	    }

	    catch( ClassNotFoundException cnfe )
	    {
			System.out.println( cnfe.getMessage() );
	      	System.out.println( "\nOppretter tom liste.\n" );
	    }

	    catch( FileNotFoundException fne )
	    {
			System.out.println( "Finner ikke datafil. Oppretter tom liste.\n" );
	    }

	    catch( IOException ioe )
	    {
			System.out.println( "Innlesingsfeil. Oppretter tom liste.\n" );
	    }
	}
}