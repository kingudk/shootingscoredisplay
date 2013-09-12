package dk.kingu.shootingscoredisplay.datastore;

import java.util.List;

import dk.kingu.shootingscoredisplay.event.NameEvent;
import dk.kingu.shootingscoredisplay.event.ShotEvent;

public interface DataStore {

	public Lane getCurrentLaneState(int laneId);
	
	public List<Lane> getHistoricLaneStage(int laneId);
	
	public void addShot(ShotEvent event);
	
	public void updateName(NameEvent event);
	
	public void startNewCompetition(int laneId);
	
	public void changeToCountingShots(int laneId);
}
