package dk.kingu.shootingscoredisplay.event;

/**
 * Interface definition of scoring events from a data source  
 */
public interface ScoreEvent {

	public enum ScoreEventType {
		NAME, 
		PRACTICE, 
		GROUP,
		SHOT
	}
	
	ScoreEventType getType();
}
