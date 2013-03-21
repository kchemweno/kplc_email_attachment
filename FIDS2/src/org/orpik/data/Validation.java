/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.data;

import java.sql.ResultSet;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Validation {
    private boolean dataExists = true;
    private ResultSet resultset = null;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private String sqlSelect = "";
     private static LogManager logManager = new LogManager();
    
    public boolean dataExists(int year, int month, int market, int applicationId){
        logManager.logInfo("Entering 'dataExists(int year, int month, int market, int applicationId)' method");
        int rowCount = 0;
        try{
            sqlSelect = queryBuilder.queryCheckIfDataExists(year, month, market, applicationId);
            resultset = executeQuery.executeSelect(sqlSelect);
            if(resultset.next()){                
                rowCount = resultset.getInt("data_row_count");
            }
            dataExists = rowCount > 0 ? true : false ;
        }catch(Exception exception){   
            logManager.logError("Exception was thrown and caught in 'dataExists(int year, int month, int market, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'dataExists(int year, int month, int market, int applicationId)' method");
       return dataExists;
    }
    
}
