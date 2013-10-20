package dk.kingu.shootingscoredisplay.utils;

import java.util.Date;

import dk.kingu.shootingscoredisplay.datastore.DIF.ShotType;

public class SIUSUtils {
	
	public static Date getLogTime(String siusLogTime) {
		return new Date();
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
