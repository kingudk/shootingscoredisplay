package dk.kingu.shootingscoredisplay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs a sql script as a sequence of JDBC statements.
 *
 * Slightly modified version of the com.ibatis.common.jdbc.SqlScriptRunner class
 * from the iBATIS Apache project. Only removed dependency on Resource class
 * and a constructor
 */
public class SqlScriptRunner {
    private static final String DEFAULT_DELIMITER = ";";
    private static Logger log = LoggerFactory.getLogger(SqlScriptRunner.class);

    private Connection connection;

    private boolean stopOnError;
    private boolean autoCommit;

    private String delimiter = DEFAULT_DELIMITER;
    private boolean fullLineDelimiter = false;

    /**
     *
     * @param connection The connection to use
     * @param autoCommit Enable autocommit
     * @param stopOnError Stop running the script, if a statement fails.
     */
    public SqlScriptRunner(Connection connection, boolean autoCommit, boolean stopOnError) {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    /**
     * @param delimiter The statement delimiter, eg. ';' for mysql.
     * @param fullLineDelimiter <code>true</code> if the delimiter used to distinguise between lines.
     */
    public void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    /**
     * Method to run a sql script based on a file that exists either on the file system or can be found in the classpath
     * @param sqlScript The sql script to run. 
     */
    public void runScript(String sqlScript) throws IOException, SQLException {
    	InputStream is;
    	File scriptFile = new File(sqlScript);
    	if(scriptFile.exists()) {
    		is = new FileInputStream(scriptFile);
    	} else {
    		is = SqlScriptRunner.class.getClassLoader().getResourceAsStream(sqlScript);
    	}
    	
    	if(is == null) {
    		throw new RuntimeException("Failed to find sql script file on: " + sqlScript);
    	}
    	
    	Reader reader = new BufferedReader(new InputStreamReader(is));
    	
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            if (originalAutoCommit != this.autoCommit) {
                connection.setAutoCommit(this.autoCommit);
            }
            runScript(connection, reader);
            connection.setAutoCommit(originalAutoCommit);
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }
    
    /**
     * Runs an SQL script (read in using the Reader parameter)
     *
     * @param reader
     *            - the source of the script
     */
    public void runScript(Reader reader) throws IOException, SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            if (originalAutoCommit != this.autoCommit) {
                connection.setAutoCommit(this.autoCommit);
            }
            runScript(connection, reader);
            connection.setAutoCommit(originalAutoCommit);
        } catch (Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the
     * connection passed in
     *
     * @param conn the connection to use for the script.
     * @param reader the source of the script.
     * @throws SQLException if any SQL errors occur.
     * @throws IOException if there is an error reading from the Reader.
     */
    private void runScript(Connection conn, Reader reader) throws IOException,
            SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--") ||
                        trimmedLine.startsWith("//")) {
                    // Ignore comment line
                } else if (trimmedLine.length() == 0) {
                    // Ignore empty line.
                } else if (trimmedLine.contains("connect")) {
                    // Ignore connect statement as this is handled in the supplied connection.
                } else if (!fullLineDelimiter
                        && trimmedLine.endsWith(getDelimiter())
                        || fullLineDelimiter
                        && trimmedLine.equals(getDelimiter())) {

                    command.append(trimLineForComment(line).substring(0, line
                            .lastIndexOf(getDelimiter())));
                    command.append(" ");

                    Statement statement = conn.createStatement();

                    log.debug("Executing statement: " + command.toString());

                    boolean hasResults = false;
                    if (stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (SQLException e) {
                            e.fillInStackTrace();
                            log.error("Error executing: " + command, e);
                        }
                    }

                    if (autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    ResultSet rs = statement.getResultSet();
                    StringBuilder resultSB = new StringBuilder();
                    if (hasResults && rs != null) {
                        ResultSetMetaData md = rs.getMetaData();
                        int cols = md.getColumnCount();
                        for (int i = 0; i < cols; i++) {
                            String name = md.getColumnLabel(i);
                            resultSB.append(name + "\t");
                        }
                        resultSB.append("\n");
                        while (rs.next()) {
                            for (int i = 0; i < cols; i++) {
                                String value = rs.getString(i);
                                resultSB.append(value + "\t");
                            }
                            resultSB.append("\n");
                        }
                        log.info("Result: " + resultSB.toString());
                    }

                    command = null;
                    try {
                        statement.close();
                    } catch (Exception e) {
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                    Thread.yield();
                } else {
                    command.append(trimLineForComment(line));
                    command.append(" ");
                }
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    private String trimLineForComment(String line) {
        if (line.contains("--")) {
            return line.substring(0, line.indexOf("--"));
        } else {
            return line;
        }
    }

    private String getDelimiter() {
        return delimiter;
    }
}
