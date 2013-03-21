package org.orpik.settings.database;

/**
 *
 * @author Kiprotich
 */
import java.sql.*;
import javax.swing.JOptionPane;
import org.orpik.logging.LogManager;
import org.orpik.settings.global.Properties;

public class DatabaseConnect extends javax.swing.JFrame {

    private String dbServer = "";
    private String postgresDbName = "";
    private String mysqlDbName = "";
    private String postgresDbUser = "";
    private String mysqlDbUser = "";
    private String postgresDbPassword = "";
    private String mysqlDbPassword = "";
    private String postgresDbHost = "";
    private String mysqlDbHost = "";
    private String postgresDbPort = "";
    private String mysqlDbPort = "";
    public String Error = "";
    public int insertSuccess = 0;
    private Connection conn = null;
    private String propertiesFileName = "fids.properties";
    private Properties properties = new Properties();   
    public static Connection globalConnection = null;
    private static LogManager logManager = new LogManager();

    public DatabaseConnect() {
        logManager.logInfo("Entering 'DatabaseConnect()' constructor");
        //Variable to be used to monitor db connection status
        boolean isConnected = false;
        //Initialize connection to database
        try {
            //Get connection credentials from loaded properties file
                dbServer = properties.getDbServer();
                mysqlDbHost = properties.getMysqlDbHost();
                postgresDbHost = properties.getPostgresDbHost();
                mysqlDbPort = properties.getMysqlDbPort();
                postgresDbPort = properties.getPostgresDbPort();
                mysqlDbName = properties.getMysqlDbName();
                postgresDbName = properties.getPostgresDbName();
                mysqlDbUser = properties.getMysqlDbUser();
                postgresDbUser = properties.getPostgresDbUser();
                mysqlDbPassword = properties.getMysqlDbPassword();
                postgresDbPassword = properties.getPostgresDbPassword();

                //Determine the server to be used
                if (!dbServer.equals("")) {
                    if (dbServer.equalsIgnoreCase("mysql")) {
                        //Connect to mysql database server
                        mysqlDatabaseConnection();
                        System.out.println("Connection established to MySQL database server");
                    }else{
                        //Connect to postgresql database server
                        postgresDatabaseConnection();
                        System.out.println("Connection established to Postgres database server");
                    }
                }else{
                    System.err.println("Database server property is empty, update with mysql or postgres");
                }
                //Set connection status to true
                isConnected = true;
                //Update global connection object
                globalConnection = conn;
        } catch (Exception exception) {
            Error = exception.getMessage();
            //System.err.println("General Error: \n" + exception.toString());
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'DatabaseConnect()' constructor");
        } finally {
            //Check if connection attemp succeeded
            try {
                if (!isConnected) {
                    //Since there exists no db connection, default to local db
                   // dBConnectLocal();
                }
            } catch (Exception exception) {
                //exception.printStackTrace();
                logManager.logError("Exception was thrown and caught in finally section of 'DatabaseConnect()' constructor");
            }
        }
        logManager.logInfo("Exiting 'DatabaseConnect()' constructor");
    }

    /*
     * This method is used where mysql has been chosen in the fids.properties file as the connection
     */
    private void mysqlDatabaseConnection() {
        logManager.logInfo("Entering 'mysqlDatabaseConnection()' method");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + mysqlDbHost + ":" + mysqlDbPort + "/"
                    + mysqlDbName + "?user=" + mysqlDbUser + "&password=" + mysqlDbPassword);
            //Set the auto-commit property to false to allow roll backs to be done
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException cnfException) {
            //cnfException.printStackTrace();
            logManager.logError("ClassNotFoundException was thrown and caught in 'mysqlDatabaseConnection()' method");
        } catch (SQLException sqlException) {
            //sqlException.printStackTrace();
            logManager.logError("SQLException was thrown and caught in 'mysqlDatabaseConnection()' method");
        }catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'mysqlDatabaseConnection()' method");
        }
        logManager.logInfo("Exiting 'mysqlDatabaseConnection()' method");
    }
    
    //This method is used where postgresql has been chosen in the fids.properties file as the connection
    private void postgresDatabaseConnection() {
        logManager.logInfo("Entering 'postgresDatabaseConnection()' method");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + postgresDbHost + ":" + postgresDbPort + "/"
                    + postgresDbName + "?user=" + postgresDbUser + "&password=" + postgresDbPassword);
            //Set the auto-commit property to false to allow roll backs to be done
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException cnfException) {
            //cnfException.printStackTrace();
            logManager.logError("ClassNotFoundException was thrown and caught in 'postgresDatabaseConnection()' method");
        } catch (SQLException sqlException) {
            //sqlException.printStackTrace();
            logManager.logError("SQLException was thrown and caught in 'postgresDatabaseConnection()' method");
        }catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'postgresDatabaseConnection()' method");
        }
        logManager.logInfo("Entering 'postgresDatabaseConnection()' method");
    }
    
    /* This method is temporarily retired till futher notice
    private void dBConnectLocal() {
        try {
            //Change the dbHost variable value to localhost and db user to root that acts as a failover connection
            dbHost = "localhost";
            dbUser = "root";
            //Connect to the failover database server
            conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/"
                    + dbName + "?user=" + databaseUser + "&password=" + databasePassword);
            //Set the auto-commit property to false to allow roll backs to be done
            conn.setAutoCommit(false);
            System.err.println("Database host server name : " + dbHost);
        } catch (SQLException sqlEx) {
            Error = sqlEx.getMessage();
            //Error connecting to localhost after defaulting                
            System.err.println("SQL Error: \n" + sqlEx.toString());
            JOptionPane.showMessageDialog(this, Error, "Database Connection Error. "
                    + "Ensure that you have database server installed on this computer", JOptionPane.ERROR_MESSAGE);
            System.err.println("URL :jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?user=" + dbUser + "&password=" + dbPass);
        } catch (Exception ex) {
            Error = ex.getMessage();
            System.err.println("General Error: \n" + ex.toString());
        }
    }
    */
    //For use both with INSERT and UPDATE
    public int sqlInsert(String sqlInsertQuery) {
        logManager.logInfo("Entering 'sqlInsert(String sqlInsertQuery)' method");
        try {
            System.out.println(sqlInsertQuery);
            if (conn != null) { //Check whether or not the connection is null to avod Null Pointer Exception
                Statement statement = conn.createStatement();
                //Insert the data in to database table
                insertSuccess = statement.executeUpdate(sqlInsertQuery);
                } else //Connection to database does not exist
            {
                System.err.println("Sorry,connection to database has not been made");
                JOptionPane.showMessageDialog(null, "There is no active connection to database",
                        "Database connection Absent", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException sqlException) {
            //System.err.println("SQL INSERT statement execute resulted in a failure");
            //System.out.println(sqlInsertQuery);            
            //System.err.println("SQL Error: \n" + sqlException.toString());            
            //sqlException.printStackTrace();
            logManager.logError("SQLException was thrown and caught in 'sqlInsert(String sqlInsertQuery)' method");
        } catch (NullPointerException nullPointerException) {
            //System.err.println("SQL INSERT statement execute resulted in a failure");
            //System.err.println("Null Pointer Error: \n" + nullPointerException.toString());
            //nullPointerException.printStackTrace();
            logManager.logError("NullPointerException was thrown and caught in 'sqlInsert(String sqlInsertQuery)' method");
        } catch (Exception exception) {
            //System.err.println("SQL INSERT statement execute resulted in a failure");
            //System.err.println("General Error: \n" + exception.toString());
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'sqlInsert(String sqlInsertQuery)' method");
        }
        logManager.logInfo("Exiting 'sqlInsert(String sqlInsertQuery)' method");
        return insertSuccess;
    }

    //This is an overloaded method for the above method
    public String sqlInsert(String sqlInsertQuery, int notUseful) {
        logManager.logInfo("Entering 'sqlInsert(String sqlInsertQuery, int notUseful)' method");
        String errorMessage = "";
        try {
            //Check whether or not the connection is null to avod Null Pointer Exception
            if (conn != null) {
                Statement statement = conn.createStatement();
                //Insert the data in the staging database
                insertSuccess = statement.executeUpdate(sqlInsertQuery);
                //System.out.println("SQL INSERT statement executed successfully");
                //insertSuccess = 1;
            } else //Connection to database does not exist
            {
                //System.err.println("Sorry,connection to database has not been made");
                JOptionPane.showMessageDialog(null, "There is no active connection to "
                        + "database", "Database connection Absent", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException sqlException) {
            logManager.logError("SQLException was thrown and caught in 'sqlInsert(String sqlInsertQuery, int notUseful)' method");
            errorMessage = sqlException.getMessage();
        } catch (NullPointerException nullPointerException) {
            logManager.logError("NullPointerException was thrown and caught in 'sqlInsert(String sqlInsertQuery, int notUseful)' method");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsert(String sqlInsertQuery, int notUseful)' method");
            errorMessage = exception.getMessage();
        }
        logManager.logError("Exiting 'sqlInsert(String sqlInsertQuery, int notUseful)' method");
        return errorMessage;
    }

    public void sqlExecute(String sqlQuery) {
        logManager.logInfo("Entering 'sqlExecute(String sqlQuery)' method");
        try {
            //Check whether or not the connection is null to avod Null Pointer Exception
            if (conn != null) {
                Statement statement = conn.createStatement();
                //Execute statement that returns no results, we need not get the boolean variable returned 
                statement.execute(sqlQuery);
            } else //Connection to database does not exist
            {
                System.err.println("Sorry,connection to database has not been made");
                JOptionPane.showMessageDialog(null, "There is no active connection to database",
                        "Database connection Absent", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException sqlException) {
            //System.err.println("SQL EXECUTE statement resulted in a failure");
            //System.out.println(sqlInsertQuery);            
            //System.err.println("SQL Error: \n" + sqlException.toString());
            //sqlException.printStackTrace();
            logManager.logError("SQLException was thrown and caught in 'sqlExecute(String sqlQuery)' method");
        } catch (NullPointerException nullPointerException) {
            //System.err.println("SQL EXECUTE statement resulted in a failure");
            //System.err.println("Null Pointer Error: \n" + nullPointerException.toString());
            //nullPointerException.printStackTrace();
            logManager.logError("NullPointerException was thrown and caught in 'sqlExecute(String sqlQuery)' method");
        } catch (Exception exception) {
            //System.err.println("SQL EXECUTE statement  resulted in a failure");
            //System.err.println("General Error: \n" + exception.toString());
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'sqlExecute(String sqlQuery)' method");
        }
        logManager.logInfo("Exiting 'sqlExecute(String sqlQuery)' method");
    }

    /**
     * Auto commit is set to false, therefore, this method must be called to commit transactions to the database
     */
    public void commitTransaction() {
        logManager.logInfo("Entering 'commitTransaction()' method");
        try {
            conn.commit();
        } catch (SQLException sqlException) {
            logManager.logError("SQLException was thrown and caught in 'commitTransaction()' method");
            JOptionPane.showMessageDialog(this, "Error occurred when rolling back transaction",
                    "Rollback error", JOptionPane.ERROR_MESSAGE);
            //System.err.println("Commit error: " + sqlException.getMessage());
        }
        logManager.logInfo("Exiting 'commitTransaction()' method");
    }

    public void rollBackTransaction() {
        logManager.logInfo("Entering 'rollBackTransaction()' method");
        try {
            conn.rollback();
        } catch (SQLException sqlException) {
            logManager.logError("SQLException was thrown and caught in 'rollBackTransaction()' method");
            JOptionPane.showMessageDialog(this, "Error occurred when commiting transaction",
                    "Commit error", JOptionPane.ERROR_MESSAGE);
            //System.err.println("Rollback error: " + sqlException.getMessage());
        }
        logManager.logInfo("Exiting 'rollBackTransaction()' method");
    }

    //For select statements
    public ResultSet sqlSelect(String sqlSelectQuery) {
        //initialize resultset to null
        logManager.logInfo("Entering 'sqlSelect(String sqlSelectQuery)' method");
        ResultSet resultset = null;
        if (conn == null) {
            System.err.println("Connection to database does not exist!");
        }
        try {
            Statement statement = conn.createStatement();
            //pass the sqll select query for execution
            resultset = statement.executeQuery(sqlSelectQuery);
        } catch (SQLException sqlException) {
            //System.err.println("SQL error occurred: " + sqlException.getMessage());
            sqlException.printStackTrace();//to be deleted
            System.err.println(sqlException.getMessage());
            System.err.println(sqlException.getCause());
            logManager.logInfo("SQLException was thrown and caught in 'sqlSelect(String sqlSelectQuery)' method");
        } catch (Exception exception) {
            System.err.println("SQL Select Error: \n" + exception.getMessage());
            exception.printStackTrace();
            logManager.logInfo("Exception was thrown and caught in 'sqlSelect(String sqlSelectQuery)' method");
        }
        logManager.logInfo("Exiting 'sqlSelect(String sqlSelectQuery)' method");
        return resultset;
    }
    
    //For statemets with create and drop 
    public void sqlExecuteUpdate(String[] sqlSelectQuery) {
        //initialize resultset to null
        logManager.logInfo("Entering 'sqlExecuteUpdate(String sqlSelectQuery)' method");
       // ResultSet resultset = null;
        if (conn == null) {
            System.err.println("Connection to database does not exist!");
        }
        try {
            Statement statement = conn.createStatement();
            //Add statements to statement from bacth
            for(String sql : sqlSelectQuery){
                statement.addBatch(sql);
            }
            //pass the sqll select query for execution
             statement.executeBatch();
        } catch (SQLException sqlException) {
            //System.err.println("SQL error occurred: " + sqlException.getMessage());
            sqlException.printStackTrace();//to be deleted
            System.err.println(sqlException.getMessage());
            System.err.println(sqlException.getCause());
            logManager.logInfo("SQLException was thrown and caught in 'sqlExecuteUpdate(String sqlSelectQuery)' method");
        } catch (Exception exception) {
            System.err.println("SQL Select Error: \n" + exception.getMessage());
            exception.printStackTrace();
            logManager.logInfo("Exception was thrown and caught in 'sqlExecuteUpdate(String sqlSelectQuery)' method");
        }
        logManager.logInfo("Exiting 'sqlExecuteUpdate(String sqlSelectQuery)' method");
       // return resultset;
    }    
    
    /*
     * Close the connection if it is still open     
     */
    public void closeConnection() {
        logManager.logInfo("Entering 'closeConnection()' method");
        try {
            if (conn.isClosed() == false) {
                conn.close();
            }
        } catch (SQLException sqlException) {
            //System.err.println("SQL error occurred: " + sqlException.getMessage());
            //sqlException.printStackTrace();
            logManager.logError("SQLException was thrown and caught in 'closeConnection()' method");
        } catch (Exception exception) {
            //System.err.println("General Error: \n" + exception.getMessage());
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'closeConnection()' method");
        }
        logManager.logInfo("Exiting 'closeConnection()' method");
    }

    /*
     * Garbage collector method for this class     
     */
    protected void finalize() throws Throwable {
        logManager.logInfo("Entering 'finalize()' method");
        try {
            if (!conn.isClosed()) {
                closeConnection(); //close open database collection
            }
        } finally {
            super.finalize();
        }
        logManager.logInfo("Exiting 'finalize()' method");
    }
}
