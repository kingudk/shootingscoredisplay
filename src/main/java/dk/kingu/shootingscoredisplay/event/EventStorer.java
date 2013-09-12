package dk.kingu.shootingscoredisplay.event;

import dk.kingu.shootingscoredisplay.datastore.DataStore;
import dk.kingu.shootingscoredisplay.datastore.Lane.LANE_STATE_TYPE;
import dk.kingu.shootingscoredisplay.event.GroupEvent.FireingType;

public class EventStorer implements ScoreEventHandler {

	private DataStore dataStore;
	
	public EventStorer(DataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	public void handleEvent(ScoreEvent event) {
		if(event instanceof NameEvent) {
			dataStore.updateName((NameEvent) event);
		} else if(event instanceof GroupEvent) {
			GroupEvent groupEvent = (GroupEvent) event;
			if(groupEvent.getFiringType() == FireingType.SINGLE_SHOT && 
					dataStore.getCurrentLaneState(groupEvent.getFireingPointID()).getState() == LANE_STATE_TYPE.SIGHTERS) {
				dataStore.changeToCountingShots(groupEvent.getFireingPointID());
			}
		} else if(event instanceof PracticeEvent) {
			dataStore.startNewCompetition(((PracticeEvent) event).getFireingPointID());
		} else if(event instanceof ShotEvent) {
			dataStore.addShot((ShotEvent) event);
		}
	}

}
