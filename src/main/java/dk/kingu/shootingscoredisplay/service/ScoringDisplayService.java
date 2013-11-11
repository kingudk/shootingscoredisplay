package dk.kingu.shootingscoredisplay.service;

import dk.kingu.shootingscoredisplay.datastore.DataStore;
import dk.kingu.shootingscoredisplay.datastore.MemoryBasedDataStore;
import dk.kingu.shootingscoredisplay.datastore.MemoryBasedDataStoreFactory;
import dk.kingu.shootingscoredisplay.datastore.DIF.TournamentDAO;
import dk.kingu.shootingscoredisplay.event.DBEventStorer;
import dk.kingu.shootingscoredisplay.event.EventStorer;
import dk.kingu.shootingscoredisplay.event.ScoreEventLogger;
import dk.kingu.shootingscoredisplay.sius.SiusDataSource;

public class ScoringDisplayService {

	private SiusDataSource dataSource;
	private DataStore dataStore;
	private TournamentDAO dao;
	
	public ScoringDisplayService(TournamentDAO dao) {
		dataStore = MemoryBasedDataStoreFactory.getDataStore();
		//dataSource = new SiusDataSource("localhost", "4000");
		//dataSource = new SiusDataSource("192.168.1.13", "4000");
		dataSource = new SiusDataSource(ScoringDisplayServiceFactory.getSiusDataHost(), 
		        ScoringDisplayServiceFactory.getSiusDataPort());
		dataSource.registerEventHandler(new ScoreEventLogger());
		dataSource.registerEventHandler(new DBEventStorer(dao));
		this.dao = dao;
		dataSource.start();
	}
	
	public TournamentDAO getTournamentDAO() {
		return dao;
	}
	
	public void shutdown() {
		//dataSource.close();
	}
}
