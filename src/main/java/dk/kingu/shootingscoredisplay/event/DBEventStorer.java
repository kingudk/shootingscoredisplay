package dk.kingu.shootingscoredisplay.event;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kingu.shootingscoredisplay.datastore.DIF.TournamentDAO;

public class DBEventStorer implements ScoreEventHandler {

    private Logger log = LoggerFactory.getLogger(getClass());

	private TournamentDAO dao;
	
	public DBEventStorer(TournamentDAO dao) {
	    this.dao = dao;
	}
	
	public void handleEvent(ScoreEvent event) {
	    if(event instanceof ShotEvent) {
	        try {
                dao.addShot((ShotEvent) event);
            } catch (ParseException e) {
                log.info(e.getMessage(), e);
            }
	    }
	}

}
