package dk.kingu.shootingscoredisplay.event;

public class NameEvent implements ScoreEvent {

	private int laneID;
	private int fireingPointID;
	private int shooterID;
	private String shooterName;
	
	public NameEvent(int lane, int fireingPoint, int shooterID, String shooterName) {
		this.laneID = lane;
		this.fireingPointID = fireingPoint;
		this.shooterID = shooterID;
		this.shooterName = shooterName;
	}
	
	
	public ScoreEventType getType() {
		return ScoreEvent.ScoreEventType.NAME;
	}

	public int getLaneID() {
		return laneID;
	}
	
	public int getFireingPoint() {
		return fireingPointID;
	}
	
	public int getShooterID() {
		return shooterID;
	}
	
	public String getName() {
		return shooterName;
	}
	
}
