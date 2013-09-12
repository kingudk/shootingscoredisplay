package dk.kingu.shootingscoredisplay.datastore;

import java.util.ArrayList;
import java.util.List;

import dk.kingu.shootingscoredisplay.event.NameEvent;
import dk.kingu.shootingscoredisplay.event.ShotEvent;

/**
 * Class representing a shooting lane contains relevant state information. 
 * One instance of a Lane relates to a specific competition. 
 * Relevant information includes:
 * - Sighter shots registered on the lane during the current competition
 * - Competition shots registered on the lane during the current competition
 * - The name of the shooter
 * - The nationality of the shooter (not implemented yet) 
 * - The lanes identification number
 * - The state at which the lane is in  
 */
public class Lane {

	public static enum LANE_STATE_TYPE {
		UNKNOWN,
		SIGHTERS,
		COUNTING
	}
	
	private List<Shot> sighters;
	private List<Shot> shots;
	private LANE_STATE_TYPE state;
	private String shooterName;
	private String nationality;
	private final int laneId;
	
	/**
	 * Create a new lane given it's lane identifier.
	 */
	public Lane(int laneId) {
		sighters = new ArrayList<Shot>();
		shots = new ArrayList<Shot>();
		state = LANE_STATE_TYPE.UNKNOWN;
		shooterName = "Unknown";
		nationality = "Unknown";
		this.laneId = laneId;
	}
	
	/**
	 * Handle the addition of a shot event 
	 */
	public void addShot(ShotEvent event) {
		if(event.getFireingPointID() == laneId) {
			Shot shot = new Shot(event);
			switch(state) {
			case SIGHTERS:
				// Check that the shot is reported to be sighters
				//if((event.getShotAttr() & 0x0020) == 1) {
					if(!sighters.contains(shot)) {
						sighters.add(shot);
					}
				//} else {
					// TODO Do some error handling
				//}
				break;
			case COUNTING:
				// Check that the shot is not reported to be a sighter
				//if((event.getShotAttr() & 0x0020) == 0) {
					if(!shots.contains(shot)) {
						shots.add(shot);
					}
				//} else {
					// TOTO Do some error handling
				//}
				break;
			case UNKNOWN:
			default:
				//This ain't good
				break;
			}
		}
	}
	
	public void setShooterName(NameEvent event) {
		if(event.getFireingPoint() == laneId) {
			shooterName = event.getName();
		}
	}

	public LANE_STATE_TYPE getState() {
		return state;
	}
	
	public String getShooterName() {
		return shooterName;
	}
	
	public List<Shot> getSighters() {
		return sighters;
	}
	
	public List<Shot> getCompetitionShots() {
		return shots;
	}
	
	public void startSighters() {
		if(state.equals(LANE_STATE_TYPE.UNKNOWN)) {
			state = LANE_STATE_TYPE.SIGHTERS;
		}
	}
	
	public void startCompetition() {
		if(state.equals(LANE_STATE_TYPE.UNKNOWN) || state.equals(LANE_STATE_TYPE.SIGHTERS)) {
			state = LANE_STATE_TYPE.COUNTING;
		}
	}
	
}
