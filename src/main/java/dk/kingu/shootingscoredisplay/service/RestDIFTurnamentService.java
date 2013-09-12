package dk.kingu.shootingscoredisplay.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dk.kingu.shootingscoredisplay.datastore.DIF.DIFMatch;
import dk.kingu.shootingscoredisplay.datastore.DIF.DIFTurnamentStore;
import dk.kingu.shootingscoredisplay.datastore.DIF.DIFTurnamentStoreFactory;

@Path("/DIF")
public class RestDIFTurnamentService {
	
	private DIFTurnamentStore store;
	
	public RestDIFTurnamentService() {
		store = DIFTurnamentStoreFactory.getDIFTurnamentStore();
	}
	
	@GET
	@Path("/getMatches/")
    @Produces(MediaType.APPLICATION_JSON)
	public String getMatches() throws JSONException {
		List<String> matches = store.getMatchIDs();
		JSONArray array = new JSONArray();
		for(String match : matches) {
			JSONObject obj = new JSONObject();
			obj.put("matchID", match);
			obj.put("matchName", store.getMatch(match).getMatchName());
			array.put(obj);
		}
		
		return array.toString();
	}
	
	@GET
	@Path("/getMatch")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMatch(@QueryParam("matchID") String matchID) throws JSONException {
		DIFMatch match = store.getMatch(matchID);
		JSONObject obj = new JSONObject();
		obj.put("place", match.getPlace());
		obj.put("date", match.getDate());
		obj.put("team1", match.getTeam1());
		obj.put("team2", match.getTeam2());	
		
		JSONArray lanes = new JSONArray();
		for(String lane : match.getTeam1Lanes()) {
			/*JSONObject obj2 = new JSONObject();
			obj2.put("item", lane);
			lanes.put(obj2);*/
			lanes.put(lane);
		}
		obj.put("team1lanes", lanes);
		
	    lanes = new JSONArray();
		for(String lane : match.getTeam2Lanes()) {
		/*	JSONObject obj2 = new JSONObject();
			obj2.put("item", lane);
			lanes.put(obj2);*/
			lanes.put(lane);
		}
		obj.put("team2lanes", lanes);

		return obj.toString();
	}
	
	@POST
    @Consumes("application/x-www-form-urlencoded")
	@Path("/addMatch")
	public void addMatch(
			@FormParam ("team1") String team1, 
			@FormParam ("team2") String team2, 
			@FormParam ("team1lane1") String team1lane1,
			@FormParam ("team1lane2") String team1lane2,
			@FormParam ("team1lane3") String team1lane3,
			@FormParam ("team2lane1") String team2lane1,
			@FormParam ("team2lane2") String team2lane2,
			@FormParam ("team2lane3") String team2lane3,
			@FormParam ("date") String date) {
		
		List<String> team1lanes = new ArrayList<String>();
		team1lanes.add(team1lane1);
		team1lanes.add(team1lane2);
		team1lanes.add(team1lane3);
		List<String> team2lanes = new ArrayList<String>();
		team2lanes.add(team2lane1);
		team2lanes.add(team2lane2);
		team2lanes.add(team2lane3);
		store.addMatch(team1, team2, team1lanes, team2lanes, date);
		
	
	}
	
}
