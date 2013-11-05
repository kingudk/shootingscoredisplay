package dk.kingu.shootingscoredisplay.datastore.DIF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dk.kingu.shootingscoredisplay.event.ShotEvent;
import dk.kingu.shootingscoredisplay.utils.ArgValidator;
import dk.kingu.shootingscoredisplay.utils.DBConnector;
import dk.kingu.shootingscoredisplay.utils.DBUtils;
import dk.kingu.shootingscoredisplay.utils.SIUSUtils;

/**
 * Class for accessing the database storing received shots and relevant information pertaining to a DIF tournament 
 */
public class TournamentDAO {

	private final DBConnector connector;
	
	public TournamentDAO(DBConnector connector) {
		this.connector = connector;
	}
	
	/**
	 * Add a shot to the database 
	 * @param shot The ShotEvent from which to take information to store in the database
	 */
	public void addShot(ShotEvent shot) {
		String insertSql = "INSERT INTO shots "
				+ "(time, xcoord, ycoord, value, decimalvalue, type, competitionid, fireingpoint)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		DBUtils.executeStatement(connector, insertSql, SIUSUtils.getLogTime(shot.getLogTimeStamp()), 
				shot.getXcoord(), shot.getYcoord(), shot.getShotValue(), shot.getDecimalShotValue(), 
				SIUSUtils.getShotType(shot.getShotAttr()).value(), shot.getShooterID(), shot.getFireingPointID());
	}
	
	/**
	 * Add a club to the database 
	 * @param clubName String with the name of the club
	 */
	public void addClub(String clubName) {
		String insertSql = "INSERT INTO clubs (club) VALUES (?)";
		DBUtils.executeStatement(connector, insertSql, clubName);
	}
	
	/**
	 * Get the list of clube registered in the database 
	 */
	public List<String> getClubs() {
		String selectSql = "SELECT club FROM clubs";
		List<String> clubs = new ArrayList<String>();
        try (Connection conn = connector.getConnection(); ){
            ResultSet dbResult = null;
            PreparedStatement ps = null;
            try {
                ps = DBUtils.makePreparedStatement(conn, selectSql, new Object[0]);
                dbResult = ps.executeQuery();
                
                while(dbResult.next()) {
                	clubs.add(dbResult.getString(1));
                }
            } finally {
                if(dbResult != null) {
                    dbResult.close();
                }
                if(ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve the clubs for with the SQL '"
                    + selectSql + "'.", e);
        }
        return clubs;
	}

	
	/**
	 * Add a shooter to the database. 
	 * @param shooterName The name of the shooter
	 * @param clubName The name of the club that the shooter is shooting for, the club must exist in the database.
	 */
	public void addShooter(String shooterName, String clubName) {
		String insertSql = "INSERT INTO shooters (name, club) "
				+ "VALUES ( ? , (SELECT id FROM clubs WHERE club = ?))";
		
		DBUtils.executeStatement(connector, insertSql, shooterName, clubName);
	}
	
	/**
	 * Gets the registered shooters present in the database, along with the name of the club they belong to. 
	 */
	public List<Shooter> getShooters() {
		String selectSql = "SELECT shooters.id, shooters.name, clubs.club FROM shooters JOIN clubs ON shooters.club = clubs.id";
		List<Shooter> shooters = new ArrayList<Shooter>();
        try (Connection conn = connector.getConnection(); ){
            ResultSet dbResult = null;
            PreparedStatement ps = null;
            try {
                ps = DBUtils.makePreparedStatement(conn, selectSql, new Object[0]);
                dbResult = ps.executeQuery();
                
                while(dbResult.next()) {
                	Shooter shooter = new Shooter();
                	shooter.setId(dbResult.getInt(1));
                	shooter.setShooterName(dbResult.getString(2));
                	shooter.setClubName(dbResult.getString(3));
                	shooters.add(shooter);
                }
            } finally {
                if(dbResult != null) {
                    dbResult.close();
                }
                if(ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve the clubs for with the SQL '"
                    + selectSql + "'.", e);
        }
        return shooters;
	}

	
	/**
	 * Add a new competition to the database. 
	 * @param shooterName Name of the shooter, must exist in the database
	 * @param clubName Name of the club that the shooter is shooting for, 
	 * 		must exist in the database, and be related to the shooter
	 * @param competitionID The id of the competition, the same id which is set for the SIUS lane.
	 */
	public void addCompetition(String shooterName, String clubName, int competitionID) {
		String insertSql = "INSERT INTO competitions (id, shooter) VALUES (?, ("
				+ "SELECT id FROM shooters "
					+ "JOIN clubs ON shooters.club = clubs.id "
					+ "WHERE shooters.name = ? "
					+ "AND clubs.club = ?))";
		
		DBUtils.executeStatement(connector, insertSql, competitionID, shooterName, clubName);
	}
	
	public void addCompetition(int competitionID, int shooterID) {
		String insertSql = "INSERT INTO competitions (id, shooter) VALUES (?, ?)";
		DBUtils.executeStatement(connector, insertSql, competitionID, shooterID);
	}
	
	/**
	 * Gets the list of competitions registered in the database 
	 */
	public List<Competition> getCompetitions() {
		String selectSql = "SELECT competitions.id, shooters.name, clubs.club FROM competitions "
				+ "JOIN shooters ON competitions.shooter = shooters.id "
				+ "JOIN clubs ON shooters.club = clubs.id";
		List<Competition> competitions = new ArrayList<Competition>();
        try (Connection conn = connector.getConnection(); ){
            ResultSet dbResult = null;
            PreparedStatement ps = null;
            try {
                ps = DBUtils.makePreparedStatement(conn, selectSql, new Object[0]);
                dbResult = ps.executeQuery();
                
                while(dbResult.next()) {
                	Competition competition = new Competition();
                	competition.setCompetitionID(dbResult.getInt(1));
                	Shooter shooter = new Shooter();
                	shooter.setShooterName(dbResult.getString(2));
                	shooter.setClubName(dbResult.getString(3));
                	competition.setShooter(shooter);
                	competitions.add(competition);
                }
            } finally {
                if(dbResult != null) {
                    dbResult.close();
                }
                if(ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve the clubs for with the SQL '"
                    + selectSql + "'.", e);
        }
		return competitions;
		
	}
	
	public void addMatch(String name, List<Integer> teamA, List<Integer> teamB) {
		ArgValidator.checkNotNullOrEmpty(name, "name");
		ArgValidator.checkNotNullAndLength(teamA, 3, "teamA");
		ArgValidator.checkNotNullAndLength(teamB, 3, "teamB");
		String insertSql = "INSERT INTO matches (name, "
				+ "competition1a, "
				+ "competition1b, "
				+ "competition2a, "
				+ "competition2b, "
				+ "competition3a, "
				+ "competition3b) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
		
		DBUtils.executeStatement(connector, insertSql, name, teamA.get(0), teamB.get(0), 
				teamA.get(1), teamB.get(1), teamA.get(2), teamB.get(2));
	}
	
	public List<Match> getMatches() {
		String selectSql = "SELECT name, state, competition1a, competition2a, "
				+ "competition3a, competition1b, competition2b, competition3b "
				+ "FROM matches";
		List<Match> matches = new ArrayList<Match>();
        try (Connection conn = connector.getConnection(); ){
            ResultSet dbResult = null;
            PreparedStatement ps = null;
            try {
                ps = DBUtils.makePreparedStatement(conn, selectSql, new Object[0]);
                dbResult = ps.executeQuery();
                
                while(dbResult.next()) {
                	Match match = new Match();
                	match.setName(dbResult.getString(1));
                	match.setState(MatchState.fromValue(dbResult.getInt(2)));
                	List<Integer> teamA = new ArrayList<Integer>(3);
                	for(int i = 0; i<3; i++) {
                		teamA.add(dbResult.getInt(3 + i));
                	}
                	List<Integer> teamB = new ArrayList<Integer>(3);
                	for(int i = 0; i<3; i++) {
                		teamB.add(dbResult.getInt(6 + i));
                	}
                	match.setTeamA(teamA);
                	match.setTeamB(teamB);
                	matches.add(match);
                }
            } finally {
                if(dbResult != null) {
                    dbResult.close();
                }
                if(ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not retrieve the clubs for with the SQL '"
                    + selectSql + "'.", e);
        }

		return matches;
	}
	
}
