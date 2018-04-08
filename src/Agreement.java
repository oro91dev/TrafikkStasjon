// INNEHOLDER METODER FOR AVTALER FOR VINDUET AGREEMENTREG, REDIGERT 10.05.12 AV OLAV

// IMPORT SETNINGER
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Agreement implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1015L;

    // KJØRETYPE ARRAY
	private DriveType[] drivelist;

    // AVTALE LINKER
	Agreement nextInsP;
	Agreement nextCanP;

	// VIKTIGE STRING-FELTER
	private String insname,canname,pbirthDate,insAdress,datestamp,insorders;
	private final String datePattern = "ddMMyy";

    // KONSTRUKTØR MED NAVN, DATO ADRESSE OSV.
	public Agreement( String pname, String pD_of_b, String dname, String dadress, DriveType[] drives, String orders )
	{
		setDate( new Date() );
		canname = pname;
		pbirthDate = pD_of_b;
		insname = dname;
		insAdress = dadress;
		drivelist = drives;
		insorders = orders;
	}

	// RETURNERER SANT ELLER USANT FOR KJØRETYPENAVNET FOR (AVTALEN)
	public boolean containsThisDrive( String drivename )
	{
		for( int i = 0; i < drivelist.length; i++ )
		{
			if( drivelist[ i ] != null && drivelist[ i ].getdriveName().equalsIgnoreCase( drivename ) )
	            return true;
	    }
	    return false;
	}

	// RETURNERER SANT ELLER USANT FOR KJØRETYPEGRUPPE FOR (AVTALEN)
	public boolean containsThisDriveGroup( char c )
	{
		for( int i = 0; i < drivelist.length; i++ )
		{
			if( drivelist[ i ] != null && drivelist[ i ].getdriveGroup() == c )
	            return true;
	    }
	    return false;
	}

	// RETURNERER SANT ELLER USANT FOR KATEGORI FOR (AVTALEN)
	public boolean containsThisDriveCategory( String category )
	{
		 for( int i = 0; i < drivelist.length; i++ )
		 {
			 if( drivelist[ i ] != null && drivelist[ i ].getDriveCategory().equalsIgnoreCase( category ) )
	            return true;
	     }
	     return false;
	}

	// SETTER DATO NÅR EN NY AVTALE BLIR LAGRET
	private void setDate( Date d )
	{
        SimpleDateFormat df = new SimpleDateFormat( datePattern );
		datestamp =  df.format( d );
	}

	// RETURNERER DATOEN
	public String getDatestamp()
	{
		return datestamp;
	}

	// RETURNERER KJØRELÆRERNAVN
	public String getInstructorName()
	{
		return insname;
    }

	// RETURNERER KANDIDATNAVN
	public String getCandidateName()
	{
		return canname;
	}

	// RETURNERER KANDIDATENS FØDSELSDATO
	public String getCandidatesBirthDate()
	{
		return pbirthDate;
	}

	// RETURNERER GENERELL INFORMASJON
	public String extendedPrescInfo()
	{
		 return "Utstedt den"
		 	+ datestamp
		    + "\n"
            + "Kandidat: "
            + canname
            + ", Fødselsdato: "
            + pbirthDate
            + "\n"
            + "Kjørelærer: "
            + insname
            + "\n"
            + "Kontor (adr): "
            + insAdress
            + "\n";
	}

	// RETURNERER GENERELL INFORMASJON STRING METODE
	public String allInfo()
	{
		System.out.println( super.toString() );
		String s = "Utstedt den "
	               + datestamp
	               + ",\n"
	               + "Kandidat: "
	               + canname
	               + ",   "
	               + "Fødselsdato: "
	               + pbirthDate
	               + ",\n"
	               + "Kjørelærer: "
	               + insname
	               + ",   "
	               + "Kontor adresse: "
	               + insAdress
	               + "\n"
	               + "\n";
		s+="\n";

		for( int i = 0;( drivelist != null ) && ( i < drivelist.length ); i++)
		{
			s += ( i+1 )
				+ ")\n"
				+ drivelist[ i ]
			    + "\n";
		}
		s += insorders + "\n";
		return s;
	}

	// TO-STRING FOR AVTALEN OG NÅR DEN BLE INNGÅTT
	public String toString()
	{
		return "Avtale utstedet den " + datestamp + "\n";
	}

	// ARRAY SOM TELLER OPP KATEGORIENE OG RETURNERER SUMMEN
	public int getCategoryFrequency( String category )
	{
		int sum = 0;

		for( int i = 0; i < drivelist.length; i++ )
		{
			if( drivelist[ i ].getDriveCategory().equalsIgnoreCase( category ) )
				sum++;
		}
		return sum;
	}

	// ARRAY SOM TELLER OPP GRUPPENE OG RETURNERER SUMMEN
	public int getGroupFrequency( char license )
	{
		int sum = 0;

		for( int i = 0; i < drivelist.length; i++ )
		{
			if( drivelist[ i ].getdriveGroup() ==  license  )
				sum++;
		}
		return sum;
	}
}