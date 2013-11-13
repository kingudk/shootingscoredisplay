package dk.kingu.shootingscoredisplay.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dk.kingu.shootingscoredisplay.datastore.Lane;
import dk.kingu.shootingscoredisplay.datastore.Shot;
import dk.kingu.shootingscoredisplay.datastore.DIF.Competition;
import dk.kingu.shootingscoredisplay.datastore.DIF.Match;
import dk.kingu.shootingscoredisplay.datastore.DIF.Shooter;
import dk.kingu.shootingscoredisplay.datastore.DIF.ShotType;
import dk.kingu.shootingscoredisplay.utils.TargetUtils;

@Path("/public")
public class TournamentReadAccess {

	private ScoringDisplayService service;
	
	public TournamentReadAccess() {
		service = ScoringDisplayServiceFactory.getScoringDisplayService();
	}
	
	@GET
	@Path("/getClubs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getClubs() {
		return service.getTournamentDAO().getClubs();
	}
	
	@GET
	@Path("/getShooters")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Shooter> getShooters() {
		return service.getTournamentDAO().getShooters();
	}
	
	@GET
	@Path("/getCompetitions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Competition> getCompetitions() {
		return service.getTournamentDAO().getCompetitions();
	}
	
	@GET
	@Path("/getCompetition")
	@Produces(MediaType.APPLICATION_JSON)
	public Competition getCompetition( @QueryParam("competitionID") int competitionID) {
	    return service.getTournamentDAO().getCompetition(competitionID);
	}
	
	@GET
	@Path("/getMatches")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Match> getMatches() {
		return service.getTournamentDAO().getMatches();
	}
	
	@GET
	@Path("/getMatch")
	@Produces(MediaType.APPLICATION_JSON)
	public Match getMatch(@QueryParam("matchID") int matchID) {
	    return service.getTournamentDAO().getMatch(matchID);
	}
	
    @GET
    @Path("/getScoringTarget") 
    @Produces("image/svg+xml")
    public String getScoringTarget(@QueryParam("competitionID") int competitionID) {
        List<Shot> shots = service.getTournamentDAO().getShotsByCompetitionID(competitionID, 
                ShotType.COMPETITION);
        //TODO add some handling of sighters
        return TargetUtils.makeTarget(shots);
    }
	
    @GET
    @Path("/getShots")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getShots(@QueryParam("competitionID") int competitionID) {
        List<Integer> shotList = new ArrayList<Integer>();
        //TODO Add some handling of sighters
        
        List<Shot> shots = service.getTournamentDAO().getShotsByCompetitionID(competitionID, 
                ShotType.COMPETITION);
        
        for(Shot shot : shots) {
            shotList.add(shot.getShotValue());
        }
        
        return shotList;
        
    }
    
    @GET
    @Path("/getDecimalShots")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getDecimalShots(@QueryParam("competitionID") int competitionID) {
        List<Integer> shotList = new ArrayList<Integer>();
        //TODO Add some handling of sighters
        
        List<Shot> shots = service.getTournamentDAO().getShotsByCompetitionID(competitionID, 
                ShotType.COMPETITION);
        
        for(Shot shot : shots) {
            shotList.add(shot.getDecimalValue());
        }
        
        return shotList;
        
    }
    
}
