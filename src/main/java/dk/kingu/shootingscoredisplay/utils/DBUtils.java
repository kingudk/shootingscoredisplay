package dk.kingu.shootingscoredisplay.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtils {

	private static final String DERBY_DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	/** The logger.*/
    private static Logger log = LoggerFactory.getLogger(dk.kingu.shootingscoredisplay.utils.DBUtils.class);
	
	/**
	 * Create a new database on a given url with a given schema
	 * @param databaseUrl The url to create the database
	 * @param databaseSchema The path to the database schema
	 */
	public static void createDB(String databaseUrl, String databaseSchema) {
		try (Connection conn = getDBConnection(databaseUrl);) {
	        SqlScriptRunner scriptRunner = new SqlScriptRunner(conn, false, true);
	        scriptRunner.runScript(databaseSchema);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Obtain a connection for the database, use for unpooled dataabase connections.
	 */
	protected static Connection getDBConnection(String dbUrl) throws ClassNotFoundException, SQLException {
		Class.forName(DERBY_DB_DRIVER);
		Connection conn = DriverManager.getConnection(dbUrl);
		return conn;
	}
	
    /**
     * Executing a given statement, not returning any results.
     * 
     * @param dbConnector For connecting to the database.
     * @param query The SQL query to execute.
     * @param args The arguments for the SQL statement.
     */
    public static void executeStatement(DBConnector dbConnector, String query, Object... args) {
        ArgValidator.checkNotNull(dbConnector, "DBConnector dbConnector");
        ArgValidator.checkNotNullOrEmpty(query, "String query");
        ArgValidator.checkNotNull(args, "Object... args");
        
        log.debug("Executing statement with query: '" + query + "', and arguments: '" + Arrays.asList(args) + "'");
        
        PreparedStatement ps = null; 
        ResultSet res = null;
        Connection conn = null;
        try {
            try {
                conn = dbConnector.getConnection();
                ps = makePreparedStatement(conn, query, args);
                ps.executeUpdate();
                conn.commit();
            } finally {
                if(res != null) {
                    res.close();
                }
                if(ps != null) {
                    ps.close();
                }
                if(conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            throw failedExecutionOfStatement(e, conn, query, args);
        }
    }
    
    /**
     * Prepare a statement given a query string and some args.
     * @param conn The connection to the database.
     * @param query a query string  (must not be null or empty)
     * @param args some args to insert into this query string (must not be null)
     * @return the prepared statement
     * @throws SQLException If unable to make the PreparedStatement
     */
    public static PreparedStatement makePreparedStatement(Connection conn, String query, Object... args)
            throws SQLException {
        log.trace("Preparing the statement: '" + query + "' with arguments '" + Arrays.asList(args) + "'");
        PreparedStatement s = conn.prepareStatement(query);
        int i = 1;
        for (Object arg : args) {
            if (arg instanceof String) {
                s.setString(i, (String) arg);
            } else if (arg instanceof Integer) {
                s.setInt(i, (Integer) arg);
            } else if (arg instanceof Long) {
                s.setLong(i, (Long) arg);
            } else if (arg instanceof Boolean) {
                s.setBoolean(i, (Boolean) arg);
            } else if (arg instanceof Date) {
                s.setTimestamp(i, new Timestamp(((Date) arg).getTime()));
            } else {
                if(arg == null) {
                    throw new IllegalStateException("Cannot handle a null as argument for SQL query. We can only "
                            + "handle string, int, long, date or boolean args for query: " + query);                    
                } else  {
                    throw new IllegalStateException("Cannot handle type '" + arg.getClass().getName() + "'. We can only "
                            + "handle string, int, long, date or boolean args for query: " + query);
                }
            }
            i++;
        }

        return s;
    }
    
    /**
     * Util method for wrapping the act of formatting and throwing an exception for a failure for executing a statement.
     * 
     * @param e The exception for the execution to fail.
     * @param dbConnection The connection to the database, where the failure occurred.
     * @param query The SQL query for the statement, which caused the failure.
     * @param args The arguments for the statement, which caused the failure.
     * @throws IllegalStateException Always, since it is intended for this method to report the failure.
     */
    private static IllegalStateException failedExecutionOfStatement(Throwable e, Connection dbConnection, 
            String query, Object... args) {
        return new IllegalStateException("Could not execute the query '" + query + "' with the arguments '" 
                + Arrays.asList(args) + "' on database '" + dbConnection + "'", e);
    }
}


