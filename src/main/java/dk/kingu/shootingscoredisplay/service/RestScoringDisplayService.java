package dk.kingu.shootingscoredisplay.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;

import dk.kingu.shootingscoredisplay.datastore.DataStore;
import dk.kingu.shootingscoredisplay.datastore.Lane;
import dk.kingu.shootingscoredisplay.datastore.MemoryBasedDataStoreFactory;
import dk.kingu.shootingscoredisplay.datastore.Shot;
import dk.kingu.shootingscoredisplay.utils.TargetUtils;

@Path("/DataStore")
public class RestScoringDisplayService {

	private DataStore store;
	
	public RestScoringDisplayService() {
		store = MemoryBasedDataStoreFactory.getDataStore();
	}
	
	@GET
	@Path("/getShooterName")
	@Produces(MediaType.APPLICATION_JSON)
	public String getShooterName(@QueryParam("laneID") int laneID) {
		Lane lane = store.getCurrentLaneState(laneID);
		if(lane == null) {
			return "";
		} else {
			return lane.getShooterName();
		}
	}
	
	@GET
	@Path("/getCompetitionShots")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCompetitionShots(@QueryParam("laneID") int laneID) {
		Lane lane = store.getCurrentLaneState(laneID);
		if(lane == null) {
			return "";
		}
		JSONArray array = new JSONArray();
		for(Shot shot : lane.getCompetitionShots()) {			
			array.put(shot.getShotValue());
		}
		
		return array.toString();
	}
	
	@GET
	@Path("/getSightersShots")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSightersShots(@QueryParam("laneID") int laneID) {
		Lane lane = store.getCurrentLaneState(laneID);
		if(lane == null) {
			return "";
		}
		JSONArray array = new JSONArray();
		for(Shot shot : lane.getSighters()) {			
			array.put(shot.getShotValue());
		}
		
		return array.toString();
	}
	
	@GET
	@Path("/getLaneState")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLaneState(@QueryParam("laneID") int laneID) {
		Lane lane = store.getCurrentLaneState(laneID);
		if(lane == null) {
			return "unknown";
		} else {
			return lane.getState().toString();
		}
	}
	
	@GET
	@Path("/getScoringTarget") 
	@Produces("image/svg+xml")
	public String getScoringTarget(@QueryParam("laneID") int laneID, 
			@QueryParam("state") String state) {
		Lane lane = store.getCurrentLaneState(laneID);
		List<Shot> shots = null;
		if(state.toUpperCase().equals("SIGHTERS")) {
			shots = lane.getSighters();
		} else if(state.toUpperCase().equals("COUNTING")) {
			shots = lane.getCompetitionShots();
		}
		return TargetUtils.makeTarget(shots);
	}
	
}
