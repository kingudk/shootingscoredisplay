package dk.kingu.shootingscoredisplay.service;

import dk.kingu.shootingscoredisplay.datastore.DIF.TournamentDAO;
import dk.kingu.shootingscoredisplay.event.DBEventStorer;
import dk.kingu.shootingscoredisplay.event.ScoreEventLogger;
import dk.kingu.shootingscoredisplay.sius.SiusDataSource;

public class ScoringDisplayService {

	private SiusDataSource dataSource;
	private TournamentDAO dao;
	
	public ScoringDisplayService(TournamentDAO dao) {
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
