package dk.kingu.shootingscoredisplay.sius;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kingu.shootingscoredisplay.event.GroupEvent;
import dk.kingu.shootingscoredisplay.event.NameEvent;
import dk.kingu.shootingscoredisplay.event.PracticeEvent;
import dk.kingu.shootingscoredisplay.event.ScoreEvent;
import dk.kingu.shootingscoredisplay.event.ShotEvent;

public class SiusMessageParser {
	
    private static Logger log = LoggerFactory.getLogger(SiusMessageParser.class);
	private static final String SHOT_MNEMONIC = "_SHOT";
	private static final String GROUP_MNEMONIC = "_GRPH";
	private static final String NAME_MNEMONIC = "_NAME";
	private static final String PRACTICE_MNEMONIC = "_PRCH";
	
	
	public static ScoreEvent parseMessage(String message) {
		String eventType = message.split(";")[0];
		if(eventType.equals(SHOT_MNEMONIC)) {
			return makeShotEvent(message);
		} else if(eventType.equals(NAME_MNEMONIC)) {
			return makeNameEvent(message);
		} else if(eventType.equals(GROUP_MNEMONIC)) {
			return makeGroupEvent(message);
		} else if(eventType.equals(PRACTICE_MNEMONIC)) {
			return makePracticeEvent(message);
		} else {
		    log.info("Can't parse event, message was: '" + message + "'");
			return null;
		}
	}
	
	private static GroupEvent makeGroupEvent(String message) {
		String[] tokens = message.split(";");
		if(tokens.length == 15) {
			return new GroupEvent(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]),
					Integer.parseInt(tokens[5]),
					tokens[6],
					Integer.parseInt(tokens[7]), 
					Integer.parseInt(tokens[9]), 
					Integer.parseInt(tokens[10]), 
					Integer.parseInt(tokens[11]));
		}
		return null;
	}
	
	private static NameEvent makeNameEvent(String message) {
		String[] tokens = message.split(";");
		if(tokens.length == 6) {
			return new NameEvent(Integer.parseInt(tokens[1]), 
					Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]), tokens[5]);
		}
		return null;
	}
	
	private static PracticeEvent makePracticeEvent(String message) {
		String[] tokens = message.split(";");
		if(tokens.length == 26) {
			return new PracticeEvent(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]), 
					Integer.parseInt(tokens[3]), 
					Integer.parseInt(tokens[5]), tokens[6],
					Integer.parseInt(tokens[7]), 
					Integer.parseInt(tokens[11]), 
					Integer.parseInt(tokens[13]), 
					Integer.parseInt(tokens[14]));
		} else {
			return null;	
		}
	}
	
	private static ShotEvent makeShotEvent(String message) {
		String[] tokens = message.split(";");
		ShotEvent event = null;
		if(tokens.length == 24) {
			event = new ShotEvent();
			event.setLaneID(Integer.parseInt(tokens[1]));
			event.setFireingPointID(Integer.parseInt(tokens[2]));
			event.setShooterID(Integer.parseInt(tokens[3]));
			event.setSequenceNumber(Integer.parseInt(tokens[5]));
			event.setLogTimeStamp(tokens[6]);
			event.setEventType(Integer.parseInt(tokens[7]));
			event.setShotAttr(Integer.parseInt(tokens[9]));
			event.setShotValue(Integer.parseInt(tokens[10]));
			event.setDecimalShotValue(Integer.parseInt(tokens[11]));
			event.setShotID(Integer.parseInt(tokens[13]));
			event.setXcoord(Float.parseFloat(tokens[14]));
			event.setYcoord(Float.parseFloat(tokens[15]));
			event.setTimeStamp(Long.parseLong(tokens[20]));
			event.setCaliber(Integer.parseInt(tokens[22]));
		/*	
			return new ShotEvent(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]),
					Integer.parseInt(tokens[3]),
					Integer.parseInt(tokens[4]),
					tokens[6],
					Integer.parseInt(tokens[7]),
					Integer.parseInt(tokens[9]),
					Integer.parseInt(tokens[10]),
					Integer.parseInt(tokens[11]),
					Integer.parseInt(tokens[13]),
					Float.parseFloat(tokens[14]),
					Float.parseFloat(tokens[15]),
					Long.parseLong(tokens[20]),
					Integer.parseInt(tokens[21]),
					Integer.parseInt(tokens[22]));*/
		} 
		
		return event;
	}
}
