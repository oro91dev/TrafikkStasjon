// INNEHOLDER METODER FOR KJ�REL�RER, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.io.Serializable;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class Instructorindex implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1006L;
	private List <Instructor> inslist = new ArrayList <Instructor> ();

	// LISTE FOR ALLE KJ�REL�RERE
	public ArrayList <Instructor> listAllInss()
	{
		ArrayList <Instructor> inssWithLicense  = new ArrayList <Instructor> ();
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			inssWithLicense.add( d );
		}
		return inssWithLicense;
	}

	// SETTER INN NY KJ�REL�RER I LISTEN
	public void registerNewInstructor( Instructor n )
	{
		if( n == null )
			return;
		inslist.add( n );
	}

    // RETURNERER STRING MED ALLE KJ�REL�RERE
	public String listInstructors( )
	{
		String list = "";
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			list += d.AllInfo() + "\n";
		}
		return( ( list.length() == 0 ) ? "Ingen kj�rel�rer er registrert" : list );
	}

	// LISTE MED ALLE KJ�REL�RERE MED KJ�RETIME
	public ArrayList <Instructor> listInssWithDrive( String drivename )
	{
		ArrayList <Instructor> inssWithDrive  = new ArrayList <Instructor> ();
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			if( d.listDriveInstructors( drivename ) )
			inssWithDrive.add( d );
		}
		return inssWithDrive;
	}

	// REGISTRERER AVTALE METODE
	public void registerAgreement( Agreement n, String name, String adress )
	{
		Instructor d;
		if( ( d = findInstructor( adress, name ) ) != null )
		{
			d.newAgreement( n );
		}
	}

	// SLETTER KJ�REL�RER
	public boolean deleteInstructor( String adress, String name )
	{
		Instructor d;
		if( ( d = findInstructor( adress, name ) ) != null && d.headAgreement == null )
		{
			return inslist.remove( d );
		}
		return false;
	}

    // FINNER KJ�REL�RER
	public Instructor findInstructor( String adress, String name )
	{
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			if( d.getName().equalsIgnoreCase( name ) && d.getAdress().equalsIgnoreCase(adress) )
				return d;
		}
		return null;
	}

	// COMBOBOKS FOR � LISTE UT ALLE REGISTRERTE KJ�REL�RERE
	public ArrayList <Instructor> listInssWithLicense()
	{
		ArrayList <Instructor> inssWithLicense  = new ArrayList <Instructor> ();
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			if( d.getLicence() != "I" )
				inssWithLicense.add( d );
		}
		return inssWithLicense;
	}

	// ARRAYLISTE MED ALLE KJ�REL�RERE MED VALGT SERTIFIKAT
	public ArrayList <Instructor> listInssChosenLicense( String license )
	{
		ArrayList <Instructor> inssChosenLicense  = new ArrayList <Instructor> ();
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			if( d.getLicence() == license )
				inssChosenLicense.add( d );
		}
		return inssChosenLicense;
	}

	// REDEFINERT SAMLING
	public Agreement[] collecRelevantDatstamps( String drivename )
	{
		ArrayList <Agreement> stamps = new ArrayList <Agreement> ();
		Iterator <Instructor> i = inslist.iterator();
		while( i.hasNext() )
		{
			Instructor d = i.next();
			d.collectTheseStamps( drivename, stamps );
		}
		Agreement[] dateStamps  = stamps.toArray( new Agreement[ stamps.size() ] );
		return dateStamps;
	}

	// LEGGER TIL ALLE SPESIFISERTE KJ�REL�RERE INN I EN LIST
	public void listInssWithParameter( char licence, String partOfname, JComboBox list )
	{
		String inssname;
		String lowerCase = partOfname.toLowerCase();
		list.removeAllItems();
		for( Instructor d:inslist )
		{
			inssname = d.getName().toLowerCase();
			if( inssname.startsWith( lowerCase ) && d.getLicence().charAt(0) <= licence )
				list.addItem( d );
		}
	}

	// KEYLISTENER AV KJ�REL�RENS NAVNEFELT SOM OPPDATERER KJ�REL�RER JLIST FORTL�PENDE
	public void listInssWithName( String partOfname, JList list, DefaultListModel modelins )
	{
		String inssname;
		String lowerCase = partOfname.toLowerCase();
		modelins.removeAllElements();
		for( Instructor d:inslist )
		{
			inssname = d.getName().toLowerCase();
			if( inssname.startsWith( lowerCase ) )
				modelins.addElement( d );
		}
		list.validate();
	}

	// KEYLISTENER AV KJ�REL�RENS ADRESSEFELT SOM OPPDATERER KJ�REL�RER JLIST FORTL�PENDE
	public void listInssWithAdress( String partOfadress, JList list, DefaultListModel modelins )
	{
		String inssadress;
		String lowerCase = partOfadress.toLowerCase();
		modelins.removeAllElements();
		for( Instructor d:inslist )
		{
			inssadress = d.getAdress().toLowerCase();
			if( inssadress.startsWith( lowerCase ) )
				modelins.addElement( d );
		}
		list.validate();
	}

	// KEYLISTENER AV KJ�RETIME FELT SOM OPPDATERER KJ�RETIME BOKSEN FORTL�PENDE
	public void listAssignments( String partofassignmentname, JComboBox assignmentbox )
	{
		String inssname;
		System.out.println( partofassignmentname );
		String lowerCase = partofassignmentname.toLowerCase();
		assignmentbox.removeAll();
	}
}