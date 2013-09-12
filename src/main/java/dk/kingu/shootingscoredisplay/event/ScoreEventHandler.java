package dk.kingu.shootingscoredisplay.event;


/**
 * Interface for the handling events from a ShootingScoreDataSource 
 */
public interface ScoreEventHandler {
	
	void handleEvent(ScoreEvent event);

}
