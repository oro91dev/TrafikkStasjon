// INNEHOLDER KANDIDATINDEKS, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class Candidateindex implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1011L;
	public Candidate head;

	public String newCandidate( Candidate newCandidate )
	{
		if( head == null )
		{
			head = newCandidate;
			return "Kandidaten er lagt til i listen";
		}

		newCandidate.next = head;
		head = newCandidate;
		return "Kandidaten er lagt til f�rst i listen";
	}

	// METODE FOR � SLETTE KANDIDAT UTEN AVTALE
	public String deleteCandidate( Candidate can )
	{
		if( can == null )
			return "Kandidaten eksisterer ikke i systemet";
		if( can.head != null )
			return "Denne kandidaten er tilknyttet en eller flere avtaler og kan derfor "
				   + "ikke slettes fra programmet";

		Candidate pointer = head;

		if( pointer == null )
			return "Kandidatlisten er tom";
		if( pointer.getName().equalsIgnoreCase(can.getName() ) )
		{
			head = pointer.next;
			return "Sist lagrede kandidat er fjernet fra systemet";
		}

		while( pointer.next != null )
		{
			if( pointer.next.getName().equalsIgnoreCase( can.getName() )
				&& pointer.next.getDateOfBirth().equalsIgnoreCase( can.getDateOfBirth() ) )
				{
					pointer.next = pointer.next.next;
					return "Kandidaten er fjernet fra systemet";
				}
				pointer = pointer.next;
		}
		return "Kunne ikke finne kandidaten i systemet";
	}

	// METODE FOR � FINNE KANDIDAT UT I FRA F�DSELSDATO OG NAVN
	public Candidate findCandidate( String dateofbirth, String name )
	{
		 Candidate pointer = head;
		 if( pointer == null )
		 	return null;

		 while( pointer != null )
		 {
			 if( pointer.getCandidateSsn( dateofbirth ) && pointer.getCandidateName( name ) )
				return pointer;
			 pointer = pointer.next;
		 }
		 return null;
	}

	// LISTER UT ALLE KANDIDATENE
	public String listAllCandidates()
	{
		String s = "";
		Candidate pointer = head;
		 if( pointer == null )
		 	return "Finnes ingen kandidater i listen";

		 while( pointer != null )
		 {
			 s += pointer.toString() + "\n\n";
			 pointer = pointer.next;
		 }
		 return s;

	}

	// HENTER UT KANDIDATENE
	public String getCandidate()
	{
		String s = "";
		Candidate pointer = head;
		if( pointer == null )
			return null;

		while( pointer != null )
		{
			s += pointer.getName();
			pointer = pointer.next;
		}

		return s;

	}

    // LISTER UT KANDIDATENE MED AVTALE BASERT P� NAVN OG F�DSELSDATO
	public String listCandidateAgreements( String dateofbirth, String name )
	{
		Candidate p;
		if( ( p = findCandidate( dateofbirth, name ) ) != null )
			return p.findAgreementInfo();
		return "Finner ingen kandidat med kj�retime i denne kategorien";
	}

	// LISTER UT KANDIDATEN FOR HVILKET KATEGORI BASERT P� NAVN, F�DSELSDATO OG KATEGORI
	public String listGroupCategory( String category, String dateofbirth, String name )
	{
		Candidate p;
		if( ( p = findCandidate( dateofbirth, name ) ) != null )
			return p.listGroupCategory( category );
		return "Finner ingen kandidat med kj�retime i denne kategorien";
	}

	// S�KER ETTER KJ�RETIME NAVN I AVTALE LISTA OG RETURNERER TO-STRING FOR AVTALEN
	public String listCandidateAssignment( String assignment, String dateofbirth, String name )
	{
		Candidate p;
		String s = "";
		if( ( p = findCandidate( dateofbirth, name ) ) != null )
			if( p.containAssignment( assignment ) )
			{
				s += p.listAssignment( assignment );
				return s;
			}
			return "Finner ingen kandidat med denne kj�retimen";
	}

	// LISTER UT KANDIDATEN SOM TILH�RER KJ�RETIMEGRUPPEN
	public String listDriveGroup( char driveGroup, String dateofbirth, String name )
	{
		Candidate p;
		if( ( p = findCandidate( dateofbirth, name ) ) != null )
			return p.listDriveGroup( driveGroup );
		return "Finner ikke kandidat med denne kj�retimegruppen";
	}

	// LISTER UT KANDIDAT MED KJ�RETIME
	public String listCandidatesWithDrive( String drive )
	{
		String s = "";
		Candidate pointer = head;
		 if( pointer == null )
		 	return "Finnes ingen kandidater i listen";

		 while( pointer != null )
		 {
			if( pointer.containAssignment( drive ) )
				s += pointer.getName() + "\n";

			pointer = pointer.next;
		 }
		 return s;
	}

    // HENTER UT KANDIDATEN MED DENNE KJ�RETIMEKATEGORIEN
	public String getCandidatesWithDriveCategory( char a )
	{
		String s = "";
		Candidate pointer = head;
		if( pointer == null )
			return "Kandidatlisten er tom";

		while( pointer != null )
		{
			if( pointer.getAgreement( a ).equalsIgnoreCase( "" ) )
				s+= pointer.toString();
		}

		return s;
	}

	// LEGGER TIL KANDIDATEN I LISTEN FORNAVN
	public void listCansWithName( String input, JComboBox list)
	{
		Candidate p = head;
		if( p == null )return;
		input = input.toLowerCase();
		list.removeAllItems();
		while( p != null )
		{
			String name = p.getName().toLowerCase();
			if( name.startsWith( input ) )
				list.addItem( p );
			p = p.next;
		}
	}

	// LEGGER TIL KANDIDATEN I LISTEN ETTERNAVN
	public void listCansWithDateOfBirth( String input, JComboBox list )
	{
		Candidate p = head;
		if( p == null )
			return;
		list.removeAllItems();
		while( p != null )
		{
			String dOfB = p.getDateOfBirth();
			if( dOfB.startsWith( input ) )
				list.addItem( p );
			p = p.next;
		}
	}

	// COMBOBOKS OG LISTE METODE
	public ArrayList <Candidate> listCandidates()
	{
		ArrayList <Candidate> candidates  = new ArrayList <Candidate> ();
		Candidate pointer = head;
		while( pointer != null )
		{
			candidates.add( pointer );
			pointer = pointer.next;
		}
		return candidates;
	}

	// METODE TIL COMBOBOKS
	public void listCandidates( JComboBox cbox )
	{
		  Candidate p = head;
		  cbox.removeAllItems();
		  while( p != null )
		  {
			  cbox.addItem( p );
			  p = p.next;
		  }
	}

	// FINNER KANDIDATEN MED KJ�RETIME FRA LISTEN
	public List <Candidate> findcandidateWithAssignment( String assignment )
	{
		List <Candidate> candidatesWithAssignment  = new ArrayList <Candidate> ();
		Candidate pointer = head;
		if( pointer.toString().equalsIgnoreCase( assignment ) )
		candidatesWithAssignment.add( pointer );

		return candidatesWithAssignment;
	}
}