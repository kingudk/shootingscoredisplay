package dk.kingu.shootingscoredisplay.datasource;

import java.io.IOException;
import java.net.UnknownHostException;

import dk.kingu.shootingscoredisplay.event.ScoreEventHandler;

/**
 * Interface for datasource classes for ShootingScoreDisplay
 */
public interface DataSourceInterface {

	/**
	 * Establish connection to the data source 
	 */
	void connect() throws UnknownHostException, IOException;
	
	/**
	 * Register an ScoreEventHandler that is to receive events from the DataSource
	 * @param handler The event handler that should receive events. 
	 */
	void registerEventHandler(ScoreEventHandler handler);
	
	
}
