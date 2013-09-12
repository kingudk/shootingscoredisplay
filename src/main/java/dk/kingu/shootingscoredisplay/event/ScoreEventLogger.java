package dk.kingu.shootingscoredisplay.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScoreEventLogger implements ScoreEventHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	public void handleEvent(ScoreEvent event) {
		log.info("Got event: " + event);
	}

}
