package dk.kingu.shootingscoredisplay.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.kingu.shootingscoredisplay.event.NameEvent;
import dk.kingu.shootingscoredisplay.event.ShotEvent;

public class MemoryBasedDataStore implements DataStore {

	Map<Integer, Lane> lanes;
	
	public MemoryBasedDataStore() {
		lanes = new HashMap<Integer, Lane>();
	}
	
	public Lane getCurrentLaneState(int laneId) {
		if(lanes.containsKey(laneId)) {
			return lanes.get(laneId);
			//FIXME Return a deep copy of the lane,
			//		or otherwise make sure we won't 
			//		run into concurrency issues
		}
		return null;
	}

	public List<Lane> getHistoricLaneStage(int laneId) {
		// TODO Implement when we know how to handle historic data.
		return null;
	}

	public void addShot(ShotEvent event) {
		if(lanes.containsKey(event.getFireingPointID())) {
			lanes.get(event.getFireingPointID()).addShot(event);
		}
	}

	public void updateName(NameEvent event) {
		int laneNumber = event.getFireingPoint();
		if(!lanes.containsKey(laneNumber)) {
			lanes.put(laneNumber, new Lane(laneNumber));
		}
		lanes.get(laneNumber).setShooterName(event);
	}

	public void startNewCompetition(int laneId) {
		if(lanes.containsKey(laneId)) {
			moveLaneToHistory(laneId);
		}
		
		Lane lane = new Lane(laneId);
		lane.startSighters();
		lanes.put(laneId, lane);
	}

	public void changeToCountingShots(int laneId) {
		if(lanes.containsKey(laneId)) {
			lanes.get(laneId).startCompetition();
		}
		
	}
	
	private void moveLaneToHistory(int laneId) {
		//TODO Figure out how to handle history
	}

}
