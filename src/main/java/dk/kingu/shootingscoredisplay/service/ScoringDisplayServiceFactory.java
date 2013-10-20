package dk.kingu.shootingscoredisplay.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import dk.kingu.shootingscoredisplay.datastore.DIF.DatabaseManager;
import dk.kingu.shootingscoredisplay.datastore.DIF.TournamentDAO;


/**
 * Factory class to provide access to ScoringDisplayService and related objects
 */
public class ScoringDisplayServiceFactory {

	private static final String DATABASE_URL_PROPERTY = "dk.kingu.shootingscoredisplay.dburl";
	private static final String SIUS_DATA_HOST = "dk.kingu.shootingscoredisplay.siusdatahost";
	private static final String SIUS_DATA_PORT = "dk.kingu.shootingscoredisplay.siusdataport";

	
	private static ScoringDisplayService service;
	private static DatabaseManager dbm;
	private static TournamentDAO dao;
	private static Properties properties;
	
	/**
	 * Get the service it self 
	 */
	public static synchronized ScoringDisplayService getScoringDisplayService() {
		if(service == null) {
			service = new ScoringDisplayService(getTournamentDAO());
		}
		return service;
	}
	
	/**
	 * Get the database manager 
	 */
	public static synchronized DatabaseManager getDatabaseManager() {
		if(dbm == null) {
			dbm = new DatabaseManager(properties.getProperty(DATABASE_URL_PROPERTY));
		}
		return dbm;
		
	}
	
	/**
	 * Get the TournamentDAO instance 
	 */
	public static synchronized TournamentDAO getTournamentDAO() {
		if(dao == null) {
			dao = new TournamentDAO(getDatabaseManager().getConnector());
		}
		return dao;
	}
	
	/**
	 * Obtain the SIUS data host (from config) 
	 */
	public static String getSiusDataHost() {
		return properties.getProperty(SIUS_DATA_HOST);
	}
	
	/**
	 * Obtain the SIUS data port (from config) 
	 */
	public static String getSiusDataPort() {
		return properties.getProperty(SIUS_DATA_PORT);
	}
	
	/**
	 * Initialize the factory with the config 
	 */
	public static synchronized void init(String config) throws IOException {
		properties = new Properties();
		BufferedReader reader = new BufferedReader(new FileReader(config));
        properties.load(reader);
	}
}
