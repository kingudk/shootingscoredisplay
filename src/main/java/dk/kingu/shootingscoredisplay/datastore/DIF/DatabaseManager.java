package dk.kingu.shootingscoredisplay.datastore.DIF;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kingu.shootingscoredisplay.utils.DBConnector;
import dk.kingu.shootingscoredisplay.utils.DBUtils;


/**
 * Class to manage a database, this includes creating it if a connection to it cannot be obtained. 
 * That is, if it's possible. 
 * Currently only Derby databases is supported, so it should be possible to create it. 
 * Provided that the destination is writable. 
 */
public class DatabaseManager {

    private Logger log = LoggerFactory.getLogger(getClass());
	private static final String DB_SCHEMA = "shootingscoredisplay-schema.sql";
	private final String dbUrl;
	private DBConnector connector;
	
	/**
	 * Create a instance of DatabaseManager for a database on the URL 
	 */
	public DatabaseManager(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	
	/**
	 * Get the DBConnector, triggers DB creation if a connection cannot be obtained. 
	 */
	public DBConnector getConnector() {
		if(connector == null) {
			try {
				obtainConnector();
			} catch (IllegalStateException e) {
				createDB();
			}
			obtainConnector();
		}
		return connector;
	}
	
	private void obtainConnector() throws IllegalStateException {
		connector = new DBConnector(dbUrl);
		Connection conn = connector.getConnection();
		try {
			conn.close();
		} catch (SQLException e) {
			log.warn("Failed to close connection", e);
		}
	}
	
	
	private void createDB() {
		DBUtils.createDB(dbUrl + ";create=true", DB_SCHEMA);
	}
}
