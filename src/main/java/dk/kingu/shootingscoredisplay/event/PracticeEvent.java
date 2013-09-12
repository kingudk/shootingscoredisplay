package dk.kingu.shootingscoredisplay.event;

public class PracticeEvent implements ScoreEvent {

	private int laneID;
	private int fireingPointID;
	private int shooterID;
	private int sequenceNumber;
	private String logTimeStamp;
	private int eventType;
	private int practiceSequenceNumber;
	private int shootCode;
	private int practiceCode;
	
	
	public ScoreEventType getType() {
		return ScoreEvent.ScoreEventType.PRACTICE;
	}
	
	public PracticeEvent(int laneID, int fireingPointID, int shooterID, int sequenceNumber,
			String logTimeStamp, int eventType, int practiceSequenceNumber, int shootCode, 
			int practiceCode) {
		this.laneID = laneID;
		this.fireingPointID = fireingPointID;
		this.shooterID = shooterID;
		this.sequenceNumber = sequenceNumber;
		this.logTimeStamp = logTimeStamp;
		this.eventType = eventType;
		this.practiceSequenceNumber = practiceSequenceNumber;
		this.shootCode = shootCode;
		this.practiceCode = practiceCode;
	}
	
	public int getLaneID() {
		return laneID;
	}
	
	public int getFireingPointID() {
		return fireingPointID;
	}

}
