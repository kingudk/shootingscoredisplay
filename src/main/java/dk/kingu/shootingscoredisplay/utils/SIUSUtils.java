package dk.kingu.shootingscoredisplay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.kingu.shootingscoredisplay.datastore.DIF.ShotType;

public class SIUSUtils {
	
	public static Date getLogTime(String siusLogTime) throws ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SS");
	    return sdf.parse(siusLogTime);
	}
	
	public static String getLogTimeFromDate(Date date) {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SS");
	    return sdf.format(date);
	}
	
	public static ShotType getShotType(int shotAttr) {
		ShotType type = ShotType.UNKNOWN;
		if((shotAttr & 0x0020) == 0) {
			type = ShotType.COMPETITION;
		} else if((shotAttr & 0x0020) == 1) {
			type = ShotType.SIGHTERS;
		}
		
		return type;
	}
}
