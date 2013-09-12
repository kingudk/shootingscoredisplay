package dk.kingu.shootingscoredisplay.service;

import dk.kingu.shootingscoredisplay.datastore.DataStore;
import dk.kingu.shootingscoredisplay.datastore.MemoryBasedDataStore;
import dk.kingu.shootingscoredisplay.datastore.MemoryBasedDataStoreFactory;
import dk.kingu.shootingscoredisplay.event.EventStorer;
import dk.kingu.shootingscoredisplay.event.ScoreEventLogger;
import dk.kingu.shootingscoredisplay.sius.SiusDataSource;

public class ScoringDisplayService {

	private SiusDataSource dataSource;
	private DataStore dataStore;
	
	public ScoringDisplayService() {
		dataStore = MemoryBasedDataStoreFactory.getDataStore();
		dataSource = new SiusDataSource("localhost", "4000");
		//dataSource = new SiusDataSource("192.168.1.13", "4000");
		dataSource.registerEventHandler(new ScoreEventLogger());
		dataSource.registerEventHandler(new EventStorer(dataStore));
		dataSource.start();
	}
	
	public void shutdown() {
		dataSource.close();
	}
}
