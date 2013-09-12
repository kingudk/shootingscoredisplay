package dk.kingu.shootingscoredisplay.event;

public class GroupEvent implements ScoreEvent {

	public static enum FireingType {
		SIGHTERS(0), 
		SINGLE_SHOT(1), 
		RAPID_FIRE(2);
		
		private int type;
		
		FireingType(int type) {
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
		
		public static FireingType fromInt(int type) {
			for(FireingType fireingType : FireingType.values()) {
				if(fireingType.getType() == type) {
					return fireingType;
				}
			}
			return null;
		}
	}
	
	private int laneID;
	private int fireingPointID;
	private int shooterID;
	private int sequenceNumber; 
	private String logTimeStamp;
	private int eventType;
	private int groupOrdinal;
	private FireingType fireingType;
	private int expectedNumberOfShots;
	
	public GroupEvent(int laneID, int fireingPointID, int shooterID, int sequenceNumber, 
			String logTimeStamp, int eventType, int groupOrdinal, int fireingType, int expectedNumberOfShots) {
		this.laneID = laneID;
		this.fireingPointID = fireingPointID;
		this.shooterID = shooterID;
		this.sequenceNumber = sequenceNumber;
		this.logTimeStamp = logTimeStamp;
		this.eventType = eventType;
		this.groupOrdinal = groupOrdinal;
		this.fireingType = FireingType.fromInt(fireingType);
		this.expectedNumberOfShots = expectedNumberOfShots;
	}
	
	public ScoreEventType getType() {
		return ScoreEvent.ScoreEventType.GROUP;
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

	public int getScequenceNumber() {
		return sequenceNumber;
	}

	public String getLogTimeStamp() {
		return logTimeStamp;
	}

	public int getEventType() {
		return eventType;
	}

	public int getGroupOrdinal() {
		return groupOrdinal;
	}

	public FireingType getFiringType() {
		return fireingType;
	}

	public int getExpectedNumberOfShots() {
		return expectedNumberOfShots;
	}

	@Override
	public String toString() {
		return "GroupEvent [laneID=" + laneID + ", fireingPointID="
				+ fireingPointID + ", shooterID=" + shooterID
				+ ", scequenceNumber=" + sequenceNumber + ", logTimeStamp="
				+ logTimeStamp + ", eventType=" + eventType + ", groupOrdinal="
				+ groupOrdinal + ", firingType=" + fireingType
				+ ", expectedNumberOfShots=" + expectedNumberOfShots + "]";
	}

}
