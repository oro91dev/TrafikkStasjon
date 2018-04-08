// INNEHOLDER VINDUET OG METODER FOR AVTALE REGISTERET, REDIGERT 10.05.12 AV MARIUSZ

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
import java.util.Iterator;
import javax.swing.JComboBox;

public class DriveType implements Serializable
{
	// DATAFELTER
	private static final long serialVersionUID = 1009L;
	private String units,drivename,drivecategory;
	private char driveGroup;
	private double quantity;
	private static List <DriveType> drives;

	static
	{
		Register.getInstance().load();
		if( drives == null )
			drives = new ArrayList <DriveType> ();
	}

    // KONSTRUKTØR
	private DriveType( String drivename, char driveGroup, String drivecategory, double quantity , String units )
	{
		this.drivename = drivename;
		this.driveGroup = Character.toUpperCase( driveGroup );
		this.quantity = quantity;
		this.units = units;
		this.drivecategory = drivecategory;
	}

	// METODEN SØRGER FOR AT BARE EN AV HVER TYPE( NAVN, KJØRETIMEGRUPPE, KATEGORI, OSV) BLIR OPPRETTET EN GANG
	public static DriveType add( String drivename, char driveGroup, String drivecategory, double quantity, String units )
	{
		Iterator <DriveType> i = drives.iterator();
		DriveType d;
		while( i.hasNext() )
		{
			d = i.next();
			if( d.getdriveName().equalsIgnoreCase( drivename ) )
				return d;
		}
		d = new DriveType( drivename, driveGroup, drivecategory, quantity , units );
		drives.add( d );
		return d;
	}

	// STATISK METODE
	public static DriveType getInstance( String drivename )
	{
		Iterator <DriveType> i = drives.iterator();
		DriveType d;
		while(i.hasNext() )
		{
			d = i.next();
			if( d.getdriveName().equalsIgnoreCase( drivename ) )
				return d;
		}
		return null;
	}

	// OPPDATERER COMBOBOKS MED KJØRETIME NAVN
	public static void getAssignments( String partOfDriveName, JComboBox list )
	{
		String lowerCase = partOfDriveName.toLowerCase();
		Iterator <DriveType> i = drives.iterator();
		DriveType d;
		list.removeAllItems();
		while( i.hasNext() )
		{
			d = i.next();
			if( d.getdriveName().startsWith( lowerCase ) )
				list.addItem(d.getdriveName() );
		}
	}

    // GET-METODE KJØRETYPE NAVN
	public String getdriveName()
	{
		return drivename;
	}

	// GET-METODE KJØRETYPE GRUPPE
	public char getdriveGroup()
	{
		return driveGroup;
	}

	// GET-METODE KJØRETYPE KATEGORI
	public String getDriveCategory()
	{
		return drivecategory;
	}

	// GET-METODE FOR MENGDE
	public double getQuantity()
	{
		return quantity;
	}

	// GET-METODE FOR ENHETER
	public String getUnit()
	{
		return units;
	}

	// TO-STRING METODE SOM RETURNERER INFO
	public String toString()
	{
		System.out.println( "" + drives.size() );
		return "Kjøretøy: "
			+ drivename
	    	+ "\n"
	        + "Gruppe: "
	        + driveGroup
	        + "\n"
	        + "Kategori: "
	        + drivecategory
	        + "\n"
            + "Tid/Lengde: "
            + quantity
            + "\n"
            + "Enhet i: "
            + units
            + "\n\n";
	}

	// LAGRER KJØRETYPE
	public static Object getMemento()
	{
        return( Object ) drives;
	}

	@SuppressWarnings( "unchecked" )
	public static void setMemento( Object memento )
	{
        drives = ( List <DriveType> ) memento;
	}
}