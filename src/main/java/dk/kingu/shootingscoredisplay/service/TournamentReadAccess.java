package dk.kingu.shootingscoredisplay.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dk.kingu.shootingscoredisplay.datastore.DIF.Competition;
import dk.kingu.shootingscoredisplay.datastore.DIF.Shooter;

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
}
