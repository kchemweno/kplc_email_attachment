/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.data;

import java.sql.ResultSet;
import org.orpik.logging.LogManager;
import org.orpik.settings.database.DatabaseConnectionProvider;

/**
 *
 * @author Chemweno
 */
public class ExecuteQuery {

    // private  DatabaseConnect databaseConnect;   
    private ResultSet resultset;
    private int result = 0;
    private String errorMessage = "";
    private static LogManager logManager = new LogManager();
    //private static DatabaseConnect databaseConnect;   

    public ExecuteQuery() {
        logManager.logInfo("Entering 'ExecuteQuery()' method");
        /*
         * databaseConnect = null; databaseConnect = new DatabaseConnect();
         */
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running  DatabaseConnectionProvider.connectToDatabase() in 'ExecuteQuery()' method");
            DatabaseConnectionProvider.connectToDatabase();
        }
        logManager.logInfo("Exiting 'ExecuteQuery()' method");
    }

    /*
     * Select data from a table and return a resultset
     */
    public ResultSet executeSelect(String SqlSelectQuery) {
        logManager.logInfo("Entering 'executeSelect(String SqlSelectQuery)' method");
        try {
            /*
             * if(databaseConnect == null){ databaseConnect = new
             * DatabaseConnect(); } resultset = databaseConnect.sqlSelect(SqlSelectQuery);
             */
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running DatabaseConnectionProvider.connectToDatabase() in 'executeSelect(String SqlSelectQuery)' method");
            DatabaseConnectionProvider.connectToDatabase();
        }
        logManager.logInfo("Running resultset = DatabaseConnectionProvider.databaseConnect.sqlSelect(SqlSelectQuery) 'executeSelect(String SqlSelectQuery)' method");
            resultset = DatabaseConnectionProvider.databaseConnect.sqlSelect(SqlSelectQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'executeSelect(String SqlSelectQuery)' method");
           //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'executeSelect(String SqlSelectQuery)' method");
        return resultset;
    }
    
    /*
     * Select data from a table and return a resultset after dropping table
     */
    public boolean executeUpdate(String[] SqlSelectQuery) {
        boolean isSuccessful = false;
        logManager.logInfo("Entering 'executeUpdate(String SqlSelectQuery)' method");
        try {
            /*
             * if(databaseConnect == null){ databaseConnect = new
             * DatabaseConnect(); } resultset = databaseConnect.sqlSelect(SqlSelectQuery);
             */
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running DatabaseConnectionProvider.connectToDatabase() in 'executeUpdate(String SqlSelectQuery)' method");
            DatabaseConnectionProvider.connectToDatabase();
        }
        logManager.logInfo("Running resultset = DatabaseConnectionProvider.databaseConnect.sqlSelect(SqlSelectQuery) 'executeUpdate(String SqlSelectQuery)' method");
           DatabaseConnectionProvider.databaseConnect.sqlExecuteUpdate(SqlSelectQuery);
           isSuccessful = true;
        } catch (Exception exception) {            
            logManager.logError("Exception thrown and caught in 'executeUpdate(String SqlSelectQuery)' method");
           //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'executeUpdate(String SqlSelectQuery)' method");     
        return isSuccessful;
    }    

    /*
     * Select data from a table and return a resultset
     */
    public int executeInsert(String SqlInsertQuery) {
        logManager.logInfo("Entering 'executeInsert(String SqlInsertQuery)' method");
        try {
            /*if (databaseConnect == null) {
                databaseConnect = new DatabaseConnect();
            }
            result = databaseConnect.sqlInsert(SqlInsertQuery);*/
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running DatabaseConnectionProvider.connectToDatabase() in 'executeInsert(String SqlInsertQuery)' method");
            DatabaseConnectionProvider.connectToDatabase();
        }
        result = DatabaseConnectionProvider.databaseConnect.sqlInsert(SqlInsertQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'executeInsert(String SqlInsertQuery)' method");
            //exception.printStackTrace();
        } finally {
        }
        logManager.logInfo("Entering 'executeInsert(String SqlInsertQuery)' method");
        return result;
    }

    /**
     * Commit transaction
     */
    public void commit() {
        logManager.logInfo("Entering 'commit()' method");
        try {
            /*
            if (databaseConnect == null) {
                databaseConnect = new DatabaseConnect();
            }
            databaseConnect.commitTransaction();*/
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running DatabaseConnectionProvider.connectToDatabase() in 'commit()' method");
            DatabaseConnectionProvider.connectToDatabase();
        }  
        logManager.logInfo("Running DatabaseConnectionProvider.databaseConnect.commitTransaction() in 'commit()' method");
        DatabaseConnectionProvider.databaseConnect.commitTransaction();
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'commit()' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'commit()' method");
    }

    /**
     * Roll back transaction
     */
    public void rollBack() {
        logManager.logInfo("Entering 'rollBack()' method");
        try {
            /*if (databaseConnect == null) {
                databaseConnect = new DatabaseConnect();
            }
            //databaseConnect.commitTransaction();*/
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running DatabaseConnectionProvider.connectToDatabase() in 'rollBack()' method");
            DatabaseConnectionProvider.connectToDatabase();
        }                       
        DatabaseConnectionProvider.databaseConnect.commitTransaction();
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'rollBack()' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'rollBack()' method");
    }

    /*
     * Close connection and set databaseConnect object to null
     */
    public void closeConnection() {
        logManager.logInfo("Entering 'closeConnection()' method");
        try {
            /*if (databaseConnect == null) {
                databaseConnect = new DatabaseConnect();
            }
            databaseConnect.closeConnection();*/
        if (DatabaseConnectionProvider.databaseConnect == null) {
            logManager.logInfo("Running DatabaseConnectionProvider.connectToDatabase() in 'closeConnection()' method");
            DatabaseConnectionProvider.connectToDatabase();
        }     
        DatabaseConnectionProvider.databaseConnect.closeConnection();
        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'closeConnection()' method");
        } finally {
            logManager.logInfo("Setting  DatabaseConnectionProvider.databaseConnect = null in finally of 'closeConnection()' method");
            //databaseConnect = null;
            DatabaseConnectionProvider.databaseConnect = null;
        }
        logManager.logInfo("Exiting 'closeConnection()' method");
    }
}
