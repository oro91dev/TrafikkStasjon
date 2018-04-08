// INNEHOLDER METODER FOR KJØRELÆRER, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETNINGER
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Instructor implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1008L;
	private String name;
	private String adress;
	private String licence = "";
	Agreement headAgreement;
	private List <Agreement> agreementlist = new ArrayList <Agreement> ();

	// KONSTRUKTØR
	public Instructor( String name, String adress, String licence )
	{
		this.name = name;
		this.adress = adress;
		this.licence = licence;
	}

	public Object[] getDriveFrequency( String year, String drivename)
	{
		Agreement p = headAgreement;
		int[] frequency = new int[ 13 ];
		Object[] freq = new Object[ frequency.length + 1 ];

		freq[ 0 ] = this;

		for( int i = 0, j = 1; i < frequency.length; i++, j++ )
		{
			freq[ j ] = new Integer( frequency[ i ] );
		}

		if( p == null )
			return freq;

		while( p != null )
		{
			if( p.containsThisDrive( drivename ) && year.equals( p.getDatestamp().subSequence( 4, 6 ) ) )
			{
				frequency[ Integer.parseInt( p.getDatestamp().substring( 2, 4 ) ) -1 ]++;
				frequency[ frequency.length -1 ] ++;
			}
			p = p.nextInsP;
		}

		for( int i = 0, j = 1; i < frequency.length; i++, j++ )
		{
			freq[ j ] = new Integer( frequency[ i ] );
		}
		return freq;
	}

	// HENTER UT KATEGORI
	public Object[] getCategoryFrequency( String year, String category)
	{
		Agreement p = headAgreement;
		int[] frequency = new int[ 13 ];
		Object[] freq = new Object[ frequency.length + 1 ];
		freq[ 0 ] = this;

		for( int i = 0, j = 1; i < frequency.length; i++, j++ )
		{
			freq[ j ] = new Integer( frequency[ i ] );
		}

		if( p == null )
			return freq;

		while( p != null )
		{
			if( year.equals( p.getDatestamp().subSequence( 4, 6) ) )
			{
				frequency[ Integer.parseInt( p.getDatestamp().substring( 2, 4 ) ) -1 ] += p.getCategoryFrequency( category );
				frequency[ frequency.length -1 ] += p.getCategoryFrequency( category );
			}
			p = p.nextInsP;
		}

		for( int i = 0, j = 1; i < frequency.length; i++, j++ )
		{
			freq[ j ] = new Integer( frequency[ i ] );
		}
		return freq;
	}

	public Object[] getGroupFrequency( String year, char license )
	{
		Agreement p = headAgreement;
		int[] frequency = new int[ 13 ];
		Object[] freq = new Object[ frequency.length + 1 ];
		freq[ 0 ] = this;

		for( int i = 0, j = 1; i < frequency.length; i++, j++ )
		{
			freq[ j ] = new Integer( frequency[ i ] );
		}

		if( p == null )
			return freq;

		while( p != null )
		{
			if( year.equals( p.getDatestamp().substring( 4, 6 ) ) )
			{
				frequency[ Integer.parseInt( p.getDatestamp().substring( 2, 4) ) -1 ] += p.getGroupFrequency( license );
				frequency[ frequency.length -1 ] += p.getGroupFrequency( license );
			}
			p = p.nextInsP;
		}

		for( int i = 0, j = 1; i < frequency.length; i++, j++ )
		{
			freq[ j ] = new Integer( frequency[ i ] );
		}
		return freq;
	}

	// SET-METODE FOR SERTIFIKAT
	public boolean setLicence(String licence)
	{
		for(int i=0; i < Main.avaiableLicences.length; i++  )
		{
			if (licence == Main.avaiableLicences[i])
			{
				this.licence = licence;
				return true;
			}
		}
		return false;

	}

	// GET-METODE FOR SERTIFIKAT
	public String getLicence()
	{
		return this.licence;
	}

	// LEGGER INN DEN NYE AVTALEN I AVTALE LISTE
	public void newAgreement( Agreement np )
	{
		if( np != null )
		{
			np.nextInsP = headAgreement;
			headAgreement = np;
		}
	}

	// RETURNERER ARRAYLISTE MED ALLE AVTALE OBJEKTER INNEN GITT KATEGORI
	public ArrayList <Agreement> listAgreementsCategory( String category )
	{
		ArrayList <Agreement> agreementCategory = new ArrayList <Agreement> ();
		Agreement p = headAgreement;
		while( p != null )
		{
			if( p.containsThisDriveCategory ( category ) )
				agreementCategory.add( p );
			p = p.nextInsP;
		}
		return agreementCategory;
	}

	// GET-METODE FOR NAVN
	public String getName()
	{
		return name;
	}

	// GET-METODE FOR ADRESSE
	public String getAdress()
	{
		return adress;
	}

	// RETURNERER PEKER TIL KJØRELÆRENS AVTALER
	public Agreement getAgreement()
	{
		return headAgreement;
	}

	// RETURNERER EN STRENG MED KJØRELÆREREN SOM HAR SKREVET UT AVTALE PÅ EN BESTEMT KJØRETIME
	public boolean listDriveInstructors( String drivename )
	{
		if( headAgreement == null )
	    	return false;
		Agreement next = headAgreement;
		while( next != null )
		{
			if( next.containsThisDrive( drivename ) )
			return true;
			next = next.nextInsP;
		}
		return false;
	}

	// RETURNERER ARRAYLISTE MED ALLE AVTALE OBJEKTER INNEN GITT GRUPPE/SERTIFIKAT
	public ArrayList <Agreement> listAgreementsGroups( char drive )
	{
		ArrayList <Agreement> agreementLicense = new ArrayList <Agreement> ();
		Agreement p = headAgreement;
		while( p != null )
		{
			if( p.containsThisDriveGroup( drive ) )
				agreementLicense.add( p );
			p = p.nextInsP;
		}
		return agreementLicense;
	}

	// LAGRER DISSE FELTENE
	public void collectTheseStamps( String drivename, ArrayList <Agreement> tstamps )
	{
		Agreement p = headAgreement;
		while( p != null )
		{
			if( p.containsThisDrive( drivename ) )
				tstamps.add( p );
			p = p.nextInsP;
		}
	}

	// RETURNERER INFORMASJON OM KJØRELÆREREN
	public String AllInfo()
	{
		return "Kjørelærer: " + name + "\n Kontor: " + adress + "\n"
			    + ( (getLicence()== "" ) ? "Har ikke sertifikatbevilgning" :"Har sertifikat: " + getLicence())  + "\n\n";
	}

	// TOSTRING METODE FOR NAVN
	public String toString()
	{
		return  name;
	}
}