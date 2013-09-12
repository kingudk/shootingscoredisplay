package dk.kingu.shootingscoredisplay.datastore.DIF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DIFTurnamentStore {
	
	/** Mapping between a match identifier and the Match */
	private Map<String, DIFMatch> matches;
	
	public DIFTurnamentStore() {
		matches = new HashMap<String, DIFMatch>();
	}
	
	public List<String> getMatchIDs() {
		return new ArrayList<String>(matches.keySet());
	}
	
	public DIFMatch getMatch(String matchID) {
		if(matches.containsKey(matchID)) {
			return matches.get(matchID);
		} else {
			return null;
		}
	}
	
	public void addMatch(String team1, String team2, List<String> team1lanes, List<String> team2lanes, 
			String date) {
		DIFMatch match = new DIFMatch(team1, team2, team1lanes, team2lanes, date);
		matches.put(makeKey(), match);
	}
	
	private String makeKey() {
		return UUID.randomUUID().toString();
	}
	
}
