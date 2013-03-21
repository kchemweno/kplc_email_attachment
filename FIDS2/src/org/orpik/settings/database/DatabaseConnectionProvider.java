/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.settings.database;

import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class DatabaseConnectionProvider {    
    
    public static DatabaseConnect databaseConnect = null;
    private static LogManager logManager = new LogManager();
    
    public static void  connectToDatabase(){
        logManager.logInfo("Entering 'connectToDatabase()' method");
        databaseConnect = new DatabaseConnect();
        logManager.logInfo("Exiting 'connectToDatabase()' method");
    }
}
