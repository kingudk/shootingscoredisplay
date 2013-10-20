package dk.kingu.shootingscoredisplay.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class DBConnector {

	private static final String DERBY_DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	
	/** The logger.*/
    private Logger log = LoggerFactory.getLogger(getClass());
	/** The url to the database */
	private final String dbUrl;
	/** The connection pool */
    private ComboPooledDataSource connectionPool;

	
	public DBConnector(String dbUrl) {
		this.dbUrl = dbUrl;
        this.connectionPool = new ComboPooledDataSource();
		silenceC3P0Logger();
		initialiseConnectionPool();
	}
	
    /**
     * Initialises the ConnectionPool for the connections to the database.
     */
    private void initialiseConnectionPool() {
        try {
            log.info("Creating the connection to the database '" + dbUrl + "'.");
            connectionPool.setDriverClass(DERBY_DB_DRIVER);
            connectionPool.setJdbcUrl(dbUrl);
        } catch (Exception e) {
            throw new IllegalStateException("Could not connect to the database '" + dbUrl + "'", e);
        }
    }
	
    /**
     * Get a database connection from the connection pool. 
     * @return Connection for the databse
     */
    public Connection getConnection() {
    	try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			throw new IllegalStateException("Failed to obtain database connection from pool", e);
		}
    }
	
    /**
     * Prevent com.mchange.v2 classes from spamming the log
     */
    private void silenceC3P0Logger() {
        Properties p = new Properties(System.getProperties());
        p.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        p.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "OFF"); // or any other
        System.setProperties(p);
    }
    
    /**
     * Cleans up after use.
     */
    public void destroy() {
        try {
            DataSources.destroy(connectionPool);
        } catch (SQLException e) {
            log.error("Could not clean up the database '" + dbUrl + "'.", e);
        }
    }
}
