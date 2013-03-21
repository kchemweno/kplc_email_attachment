/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.people;

import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import java.sql.ResultSet;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Analyst {
    protected QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private int analystId = 0;
    private String analystName = "";
    private static LogManager logManager = new LogManager();

    public int getAnalystId() {
        logManager.logInfo("Entering and exiting 'getAnalystId()' method");
        return analystId;
    }

    public void setAnalystId(int analystId) {
        logManager.logInfo("Entering and exiting 'setAnalystId(int analystId)' method");
        this.analystId = analystId;
    }

    public String getAnalystName() {
        logManager.logInfo("Entering and exiting 'getAnalystName()' method");
        return analystName;
    }

    public void setAnalystName(String analystName) {
        logManager.logInfo("Entering and exiting 'setAnalystName(String analystName)' method");
        this.analystName = analystName;
    }  
    public ResultSet getResultset(String sqlSelect){
        logManager.logInfo("Entering 'getResultset(String sqlSelect)' method");
        ResultSet resultset = null;
        try{
           resultset = executeQuery.executeSelect(sqlSelect); 
        }catch(Exception exception){
        logManager.logError("Exception thrown and caught in 'getResultset(String sqlSelect)' method");
        }
        logManager.logInfo("Exiting 'getResultset(String sqlSelect)' method");
        return resultset;
    }    
}
