package dk.kingu.shootingscoredisplay.datastore;

import dk.kingu.shootingscoredisplay.event.ShotEvent;

public class Shot {

	private final float XCoord;
	private final float YCoord;
	private final int seqNumber;
	private final int shotValue;
	private final int decimalValue;
	private final String logTimeStamp;	
	private final int caliber;
	
	public Shot(ShotEvent event) {
		XCoord = event.getXcoord();
		YCoord = event.getYcoord();
		seqNumber = event.getSequenceNumber();
		shotValue = event.getShotValue();
		decimalValue = event.getDecimalShotValue();
		logTimeStamp = event.getLogTimeStamp();
		caliber = event.getCaliber();
	}

	public float getXCoord() {
		return XCoord;
	}

	public float getYCoord() {
		return YCoord;
	}

	public int getShotValue() {
		return shotValue;
	}

	public int getDecimalValue() {
		return decimalValue;
	}
	
	public int getCaliber() {
		return caliber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(XCoord);
		result = prime * result + Float.floatToIntBits(YCoord);
		result = prime * result + caliber;
		result = prime * result + decimalValue;
		result = prime * result
				+ ((logTimeStamp == null) ? 0 : logTimeStamp.hashCode());
		result = prime * result + seqNumber;
		result = prime * result + shotValue;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shot other = (Shot) obj;
		if (Float.floatToIntBits(XCoord) != Float.floatToIntBits(other.XCoord))
			return false;
		if (Float.floatToIntBits(YCoord) != Float.floatToIntBits(other.YCoord))
			return false;
		if (caliber != other.caliber)
			return false;
		if (decimalValue != other.decimalValue)
			return false;
		if (logTimeStamp == null) {
			if (other.logTimeStamp != null)
				return false;
		} else if (!logTimeStamp.equals(other.logTimeStamp))
			return false;
		if (seqNumber != other.seqNumber)
			return false;
		if (shotValue != other.shotValue)
			return false;
		return true;
	}
	
	
}
