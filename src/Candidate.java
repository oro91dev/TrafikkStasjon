// INNEHOLDER METODER FOR KANDIDATER, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Candidate implements Serializable
{
	// DATAFELTER
	private final String datePattern = "ddMMyy";
	private static final long  serialVersionUID = 1013L;
	private String name;
	private String dateofbirth;
	public Candidate next;
	public Agreement head;

	// KONSTRUKTØR
	public Candidate( String name, String date )
	{
		this.name = name;
		dateofbirth = date;
	}

    // HENTER UT AVTALE SOM ER KNYTTET TIL KANDIDAT
	public String getAgreement()
	{
		Agreement pointer = head;
		String s = "";
		if( pointer == null )
			return "Kandidaten er ikke tilknyttet noen avtale";

		while( pointer != null)
		{
			s += pointer.allInfo() + "\n";
			pointer = pointer.nextCanP;
		}
		return s;
	}

    // LISTE MED KATEGORIER
	public String listAgreementsCategory( String category )
	{
		Agreement pointer = head;
		String s = "";
		if( head == null)
			return null;

		while( pointer != null)
		{
			if( pointer.containsThisDriveCategory( category ) )
			{
				s += pointer.toString() + "\n";
			}
			pointer = pointer.nextCanP;
		}
		return s;
	}

	/* HENTER UT AVTALE SOM ER AVTALT
	   RETURNERER NULL OM KANDIDATEN IKKE HAR AVTALE */
	public String getAgreement( char a )
	{
		Agreement pointer = head;
		String s = "";
		if( pointer == null)
			return "Ingen avtaler";

		while( pointer != null )
		{
			if( pointer.containsThisDriveGroup( a ) )
			{
				s += pointer.toString();
			}
			pointer = pointer.nextCanP;
		}
		return s;
	}

	// SETTER NAVN PÅ KANDIDATEN
	public void setName( String n )
	{
		name = n;
	}

    // SETTER FØDSELSDATOEN PÅ KANDIDATEN
	public void setDateOfBirth( String date )
	{
		dateofbirth = date;
	}

    // HENTER UT NAVN
	public String getName()
	{
		return name;
	}

    // HENTER UT FØDSELSDATOEN
	public String getDateOfBirth()
	{
		return dateofbirth;
	}

    // FINNER RIKTIG PERSON OBJEKT
	public boolean findCorrectPerson( String date, String name )
	{
		if( this.dateofbirth.equalsIgnoreCase( date ) && this.name.equalsIgnoreCase( name ) )
			return true;
		return false;
	}

	public boolean newAgreement( Agreement pointer )
	{
		if( head == null && pointer != null )
		{
			head = pointer;
			return true;
		}

		if( pointer != null )
		{
			pointer.nextCanP = head;
			head = pointer;
			return true;
		}
		return false;
	}

	// FINNER UT OM KANDIDATEN ER KORREKTE BASERT PÅ KANDIDATNUMMER
	public boolean getCandidateSsn( String ssn)
	{
		if( this.dateofbirth.equalsIgnoreCase( ssn ) )
			return true;
		return false;
	}

	// FINNER UT OM KANDIDATEN ER DEN KORREKTE BASERT PÅ NAVNET
	public boolean getCandidateName( String name )
	{
		if( this.name.equalsIgnoreCase( name ) )
			return true;
		return false;
	}

    // FINNER UT OM KANDIDATEN HAR AVTALE
	public boolean containAssignment( String assignment )
	{
		Agreement pointer = head;
		if( head == null )
			return false;

		while( pointer != null )
		{
			if( pointer.containsThisDrive( assignment ) )
				return true;
			pointer = pointer.nextCanP;
		}
		return false;
	}

	// RETURNERER INFO OM ALLE AVTALER
	public String findAgreementInfo()
	{
		String s = "";
		Agreement pointer = head;
		if( pointer == null )
		{
			return "Kandidaten har ingen avtaler";
		}
		while( pointer != null )
		{
			s += pointer.toString();
			pointer = pointer.nextCanP;
		}
		return s;
	}

	// RETURNERER EN STRENG SOM INNEHOLDER INFO OM ALLE KANDIDATENS AVTALER SOM INNEHOLDER KJØRETIMER INNEN EN BESTEMT GRUPPE
	public String listDriveGroup( char drug )
	{
		if( head == null )
			return "Ingen avtaler registrert på kandidat " + name;
		String drugGroupList = "";
		Agreement next = head;
		while( next != null )
		{
			if( next.containsThisDriveGroup( drug ) )
				drugGroupList += next.allInfo() + "\n";
			next = next.nextCanP;
		}
		return drugGroupList;
	}

	// RETURNERER INFO OM ALLE KANDIDATENS AVTALER INNEN GITT KATEGORI
	public String listGroupCategory( String category)
	{
		if( head == null )
			return "Ingen avtaler registrert på kandidat " + name;
		String categoryList = "";
		Agreement next = head;
		while( next != null )
		{
			if( next.containsThisDriveCategory( category ) )
				categoryList += next.allInfo() + "\n";
			next = next.nextCanP;
		}
		return categoryList;
	}

	// FINNER UT OM KANDIDATEN HAR NOEN AVTALE
	public String listAssignment( String assignment )
	{
		if( head == null )
			return "Ingen avtaler er registrert på " + name;
		String assignmentList = "";
		Agreement next = head;
		while( next != null )
		{
			if( next.containsThisDrive( assignment ) )
				assignmentList += next.allInfo()	+ "\n";
			next = next.nextCanP;
		}
		return assignmentList;
	}

	// RETURNERER NAVN PÅ KANDIDATEN OG FØDSELSDATOEN
	public String toString()
	{
		return name + ", " + dateofbirth;
	}

	// HENTER UT ALLE DAGENS AVTALER
	public String getAllTodaysAgreements()
	{
		String toDaysAgreements = "";
	    Agreement p = head;
	    if( p == null )
	    	return toDaysAgreements;
	    SimpleDateFormat df = new SimpleDateFormat( datePattern );
	    String today =  df.format( new Date() );
	    while( p != null )
	    {
			if( today.equals( p.getDatestamp() ) )
	        	toDaysAgreements += ( p.allInfo() + "\n\n" );
	           p = p.nextCanP;
	    }
	    return toDaysAgreements;
	}
}