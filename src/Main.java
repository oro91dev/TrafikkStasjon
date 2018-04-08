// INNEHOLDER VINDUET OG METODER FOR AVTALE REGISTERET, REDIGERT 10.05.12 AV MARIUSZ

// IMPORT SETNINGER
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JFrame;

public class Main implements Serializable
{
	/* DATAFELTER OG METODE FOR UTVIDELSER AV SERTIFIKATER,
	   MED AVAIABLELICENCES KAN DU LEGGE ET BOKSTAV TIL SOM C1 OG
	   OPPRETTE NY TYPE SERTIFIKAT SOM ER EN GANSKE GREI METODE FOR
	   Å UTVIDE MED FLERETYPER SERTIFIKATER */
	private static final long serialVersionUID = 1000L;
	public static String avaiableLicences[] = { "A", "B", "C", "I" };
	public static void main( String[] args )
	{
		EventQueue.invokeLater( new Runnable()
	    {
			public void run()
	        {
				makeNewDriveList();
	        	MainFrame hoved = new MainFrame();
	        }
	    } );
	}

	/* LAGER LISTE FOR TILHØRENDE KJØRETYPE SOM TILHØRER VALGTE TRINN OG TID/LENGDE
	   SOM SKAL TILHØRE SOM EN SPESIEL KJØRETIMELISTE */
	private static void makeNewDriveList()
	{
		DriveType.add( "Langkjøring", 'B', "Trinn 3 (BIL)", 90, "min" );
		DriveType.add( "Mørkekjøring", 'B', "Trinn 1 (BIL)", 120, "min" );
		DriveType.add( "Førerprøve", 'B',	"Førerprøve (BIL)", 60, "min" );
		DriveType.add( "Førerprøve", 'B',	"Førerprøve (BIL)", 50, "km" );
		DriveType.add( "Teorikurs", 'B', "Trinn 1 (BIL)", 600, "min" );
		DriveType.add( "Glattkjøring", 'B', "Trinn 3 (BIL)", 90, "min" );
		DriveType.add( "Bykjøring", 'B', "Trinn 2 (BIL)", 90, "min" );
		DriveType.add( "Bykjøring", 'B', "Trinn 2 (BIL)", 50, "km" );
		DriveType.add( "Teori", 'B', "Trinn 2 (BIL)", 60, "min" );

		DriveType.add( "Teorikurs", 'A', "Trinn 1 (MC)", 400, "min" );
		DriveType.add( "Langkjøring", 'A', "Trinn 3 (MC)", 90, "min" );
		DriveType.add( "Langkjøring", 'A', "Trinn 3 (MC)", 40, "min" );
		DriveType.add( "Bykjøring", 'A', "Trinn 2 (MC)", 90, "min" );
		DriveType.add( "Bykjøring", 'A', "Trinn 2 (MC)", 40, "km" );
		DriveType.add( "Mørkekjøring", 'A', "Trinn 1 (MC)", 60, "min" );
		DriveType.add( "Førerprøve", 'A',	"Førerprøve (MC)", 60, "min" );
		DriveType.add( "Førerprøve", 'A',	"Førerprøve (MC)", 40, "km" );

		DriveType.add( "Teorikurs", 'C', "Trinn 1 (LASTEBIL)", 600, "min" );
		DriveType.add( "Glattkjøring", 'C', "Trinn 3 (LASTEBIL)", 90, "min" );
		DriveType.add( "Bykjøring", 'C', "Trinn 2 (LASTEBIL)", 90, "min" );
		DriveType.add( "Bykjøring", 'C', "Trinn 2 (LASTEBIL)", 40, "km" );
		DriveType.add( "Teoriprøve", 'C', "Trinn 2 (LASTEBIL)", 60, "min" );
		DriveType.add( "Ulykkesberedskap", 'C', "Trinn 3 (LASTEBIL)", 400, "min" );
		DriveType.add( "Lastsikringskurs", 'C', "Trinn 3 (LASTEBIL)", 400, "min" );
		DriveType.add( "Førerprøve", 'C', "Førerprøve (LASTEBIL)", 60, "min" );
		DriveType.add( "Førerprøve", 'C', "Førerprøve (LASTEBIL)", 40, "km" );
	}

    // NOEN DATA SOM SKAL LIGGE I SYSTEMET
	private static String[] agreementDetails()
	{
		String[] data = { "Jonas Soares", "Høyenhallveien", "03049023451", "Kenneth Guneriussen", "Mortensrud, Mortensrudveien", "0012343" };
		return data;
	}

	private static String[] agreementDetails3()
	{
		String[] data = { "Omid Hosseini", "Elingsrudåsenveien", "02039023413", "Jonathan Bergmann", "Torshov, Torshovveien", "0012344" };
		return data;
	}

	private static String[] agreementDetails2()
	{
		String[] data = { "Raouf Samada", "Bislettveien", "01059025423", "Salah Gaga", "Brynseng, Brynsengveien 15", "0012345" };
		return data;
	}

	// INGEN FORANDRING
	private static String[] fieldnamesFromGui()
	{
		String[] fields = { "Navn: ", "Adresse: ", "Personnr: ", "Kjørelærer: ", "Kjørelærerkontor: ", "Kjørelærerid: " };
		return fields;
	}
}