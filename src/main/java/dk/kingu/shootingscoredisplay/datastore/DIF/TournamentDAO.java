package dk.kingu.shootingscoredisplay.datastore.DIF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kingu.shootingscoredisplay.datastore.Shot;
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
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public TournamentDAO(DBConnector connector) {
		this.connector = connector;
	}
	
	/**
	 * Add a shot to the database 
	 * @param shot The ShotEvent from which to take information to store in the database
	 * @throws ParseException if the time in provided ShotEvent cannot be parsed.
	 */
	public void addShot(ShotEvent shot) throws ParseException {
		String insertSql = "INSERT INTO shots "
				+ "(time, seqNumber, xcoord, ycoord, value, decimalvalue, type, competitionid, fireingpoint, caliber)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		DBUtils.executeStatement(connector, insertSql, SIUSUtils.getLogTime(shot.getLogTimeStamp()), 
				shot.getSequenceNumber(), shot.getXcoord(), shot.getYcoord(), shot.getShotValue(), 
				shot.getDecimalShotValue(), SIUSUtils.getShotType(shot.getShotAttr()).ordinal(), 
				shot.getShooterID(), shot.getFireingPointID(), shot.getCaliber());
	}
	
	/**
	 * Method to obtain the shots for a given competion. 
	 * @param competitionID The id of the competition
	 * @param type The type of shots wanted (sighters or competition)
	 * @return List<Shot> the shots registered for the given competitionID and shottype
	 */
	public List<Shot> getShotsByCompetitionID(int competitionID, ShotType type) {
	    List<Shot> shots = null;
		String selectSql = "SELECT xcoord, ycoord, seqNumber, value, decimalvalue, time, caliber"
		        + " FROM shots"
		        + " WHERE competitionid = ?"
		        + " AND type = ?"
		        + " ORDER BY id";
		
		try (Connection conn = connector.getConnection(); ){
            ResultSet dbResult = null;
            PreparedStatement ps = null;
            try {
                ps = DBUtils.makePreparedStatement(conn, selectSql, competitionID, type.ordinal());
                dbResult = ps.executeQuery();
                shots = new ArrayList<Shot>();
                while(dbResult.next()) {
                    Shot shot = new Shot(dbResult.getFloat("xcoord"),
                            dbResult.getFloat("ycoord"),
                            dbResult.getInt("seqNumber"),
                            dbResult.getInt("value"),
                            dbResult.getInt("decimalValue"),
                            dbResult.getDate("time"),
                            dbResult.getInt("caliber"));
                    shots.add(shot);
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
		
		return shots;
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
	
    public Competition getCompetition(int competitionID) {
        String selectSql = "SELECT competitions.id, shooters.name, clubs.club FROM competitions"
                + " JOIN shooters ON competitions.shooter = shooters.id"
                + " JOIN clubs ON shooters.club = clubs.id"
                + " WHERE competitions.id = ?";
        
        Competition competition = null;
        try (Connection conn = connector.getConnection(); ){
            ResultSet dbResult = null;
            PreparedStatement ps = null;
            try {
                ps = DBUtils.makePreparedStatement(conn, selectSql, competitionID);
                dbResult = ps.executeQuery();
                while(dbResult.next()) {
                    competition = new Competition();
                    competition.setCompetitionID(dbResult.getInt(1));
                    Shooter shooter = new Shooter();
                    shooter.setShooterName(dbResult.getString(2));
                    shooter.setClubName(dbResult.getString(3));
                    competition.setShooter(shooter);
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
        
        return competition;
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
	
	public Match getMatch(int matchID) {
       String selectSql = "SELECT id, name, state, competition1a, competition2a, "
                + "competition3a, competition1b, competition2b, competition3b"
                + " FROM matches"
                + " WHERE id = ?";
	    
       Match match = null;
       try (Connection conn = connector.getConnection(); ){
           ResultSet dbResult = null;
           PreparedStatement ps = null;
           try {
               ps = DBUtils.makePreparedStatement(conn, selectSql, matchID);
               dbResult = ps.executeQuery();
               
               while(dbResult.next()) {
                   match = new Match();
                   match.setMatchID(dbResult.getInt("id"));
                   match.setName(dbResult.getString("name"));
                   match.setState(MatchState.fromValue(dbResult.getInt("state")));
                   
                   List<Integer> teamA = new ArrayList<Integer>(3);
                   teamA.add(dbResult.getInt("competition1a"));
                   teamA.add(dbResult.getInt("competition2a"));
                   teamA.add(dbResult.getInt("competition3a"));
               
                   List<Integer> teamB = new ArrayList<Integer>(3);
                   teamB.add(dbResult.getInt("competition1b"));
                   teamB.add(dbResult.getInt("competition2b"));
                   teamB.add(dbResult.getInt("competition3b"));

                   match.setTeamA(teamA);
                   match.setTeamB(teamB);
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
       
	    return match;
	}
	
	public List<Match> getMatches() {
		String selectSql = "SELECT id, name, state, competition1a, competition2a, "
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
                    match.setMatchID(dbResult.getInt("id"));
                	match.setName(dbResult.getString("name"));
                	match.setState(MatchState.fromValue(dbResult.getInt("state")));
                	
                	List<Integer> teamA = new ArrayList<Integer>(3);
                	teamA.add(dbResult.getInt("competition1a"));
                	teamA.add(dbResult.getInt("competition2a"));
                	teamA.add(dbResult.getInt("competition3a"));
                
                	List<Integer> teamB = new ArrayList<Integer>(3);
                	teamB.add(dbResult.getInt("competition1b"));
                	teamB.add(dbResult.getInt("competition2b"));
                	teamB.add(dbResult.getInt("competition3b"));

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
