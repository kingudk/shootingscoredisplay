package dk.kingu.shootingscoredisplay.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/admin")
public class TournamentAdmin {
	
	private ScoringDisplayService service;
	
	public TournamentAdmin() {
		service = ScoringDisplayServiceFactory.getScoringDisplayService();
	}
	
	@POST
    @Consumes("application/x-www-form-urlencoded")
	@Path("/addClub")
	public void addClub(@FormParam ("clubName") String clubName) {
		service.getTournamentDAO().addClub(clubName);
	}
	
	@POST
    @Consumes("application/x-www-form-urlencoded")
	@Path("/addShooter")
	public void addShooter(@FormParam ("shooterName") String shooterName, 
			@FormParam ("clubName") String clubName) {
		service.getTournamentDAO().addShooter(shooterName, clubName);
	}
	
	@POST
    @Consumes("application/x-www-form-urlencoded")
	@Path("/addCompetition")
	public void addCompetition(@FormParam ("competitionID") int competitionID, 
			@FormParam ("shooterID") int shooterID) {
		service.getTournamentDAO().addCompetition(competitionID, shooterID);
	}
	
	@POST
    @Consumes("application/x-www-form-urlencoded")
	@Path("/addMatch")
	public void addMatch(@FormParam ("matchName") String matchName, 
			@FormParam ("teamAcompetition1") int teamAcompetition1,
			@FormParam ("teamAcompetition2") int teamAcompetition2,
			@FormParam ("teamAcompetition3") int teamAcompetition3,
			@FormParam ("teamBcompetition1") int teamBcompetition1,
			@FormParam ("teamBcompetition2") int teamBcompetition2,
			@FormParam ("teamBcompetition3") int teamBcompetition3) {
		List<Integer> teamA = new ArrayList<Integer>(3);
		teamA.add(teamAcompetition1);
		teamA.add(teamAcompetition2);
		teamA.add(teamAcompetition3);
		List<Integer> teamB = new ArrayList<Integer>(3);
		teamB.add(teamBcompetition1);
		teamB.add(teamBcompetition2);
		teamB.add(teamBcompetition3);
		service.getTournamentDAO().addMatch(matchName, teamA, teamB);
	}
	
}