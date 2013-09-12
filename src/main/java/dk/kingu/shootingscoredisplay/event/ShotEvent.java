package dk.kingu.shootingscoredisplay.event;


public class ShotEvent implements ScoreEvent {
	
	private int laneID;
	private int fireingPointID;
	private int shooterID;
	private int sequenceNumber;
	private String logTimeStamp;
	private int eventType;
	private int shotAttr;
	private int shotID;
	private float Xcoord;
	private float Ycoord;
	private long timeStamp;
	private int caliber;
	private int shotValue;
	private int decimalShotValue;
	
	public ShotEvent() {}
	
	public ShotEvent(int laneID, int fireingPointID, int shooterID, int scequenceNumber,
			String logTimeStamp, int eventType, int shotAttr, int shotValue, int decimalValue, int shotID, 
			float XCoord, float YCoord, long timeStamp, int targetFigureIndent, int caliber) {
		this.laneID = laneID;
		this.fireingPointID = fireingPointID;
		this.shooterID = shooterID;
		this.sequenceNumber = scequenceNumber;
		this.logTimeStamp = logTimeStamp;
		this.eventType = eventType;
		this.shotAttr = shotAttr;
		this.shotValue = shotValue;
		this.decimalShotValue = decimalValue;
		this.shotID = shotID;
		this.Xcoord = XCoord;
		this.Ycoord = YCoord;
		this.timeStamp = timeStamp;
		this.caliber = caliber;
	}
	
	public ScoreEventType getType() {
		return ScoreEvent.ScoreEventType.SHOT;
	}

	public int getLaneID() {
		return laneID;
	}

	public int getFireingPointID() {
		return fireingPointID;
	}

	public int getShooterID() {
		return shooterID;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public String getLogTimeStamp() {
		return logTimeStamp;
	}

	public int getEventType() {
		return eventType;
	}

	public int getShotAttr() {
		return shotAttr;
	}

	public int getShotID() {
		return shotID;
	}

	public float getXcoord() {
		return Xcoord;
	}

	public float getYcoord() {
		return Ycoord;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public int getCaliber() {
		return caliber;
	}
	
	public int getShotValue() {
		return shotValue;
	}
	
	public int getDecimalShotValue() {
		return decimalShotValue;
	}

	@Override
	public String toString() {
		return "ShotEvent [laneID=" + laneID + ", fireingPointID="
				+ fireingPointID + ", shooterID=" + shooterID
				+ ", scequenceNumber=" + sequenceNumber + ", logTimeStamp="
				+ logTimeStamp + ", eventType=" + eventType + ", shotAttr="
				+ shotAttr + ", shotID=" + shotID + ", Xcoord=" + Xcoord
				+ ", Ycoord=" + Ycoord + ", timeStamp=" + timeStamp
				+ ", caliber=" + caliber + ", shotValue=" + shotValue + "]";
	}

	public void setLaneID(int laneID) {
		this.laneID = laneID;
	}

	public void setFireingPointID(int fireingPointID) {
		this.fireingPointID = fireingPointID;
	}

	public void setShooterID(int shooterID) {
		this.shooterID = shooterID;
	}

	public void setSequenceNumber(int scequenceNumber) {
		this.sequenceNumber = scequenceNumber;
	}

	public void setLogTimeStamp(String logTimeStamp) {
		this.logTimeStamp = logTimeStamp;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public void setShotAttr(int shotAttr) {
		this.shotAttr = shotAttr;
	}

	public void setShotID(int shotID) {
		this.shotID = shotID;
	}

	public void setXcoord(float xcoord) {
		Xcoord = xcoord;
	}

	public void setYcoord(float ycoord) {
		Ycoord = ycoord;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setCaliber(int caliber) {
		this.caliber = caliber;
	}

	public void setShotValue(int shotValue) {
		this.shotValue = shotValue;
	}

	public void setDecimalShotValue(int decimalShotValue) {
		this.decimalShotValue = decimalShotValue;
	}

}
