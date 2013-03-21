/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.indicators;

import java.sql.ResultSet;
import java.util.HashMap;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Indicators {
    private int indicatorId = 0;
    private String indicatorName = "";
    private String sqlString = "";
    private ResultSet resultset = null;
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private static LogManager logManager = new LogManager();

    public int getIndicatorId(String indicatorName) {
        logManager.logInfo("Entering 'getIndicatorId(String indicatorName)' method");
        try {
            sqlString = queryBuilder.querySelectIndicatorId(indicatorName);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                indicatorId = resultset.getInt("id");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getIndicatorId(String indicatorName)' method");
        }
        logManager.logInfo("Exiting 'getIndicatorId(String indicatorName)' method");
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        logManager.logInfo("Entering and exiting 'setIndicatorId(int indicatorId)' method");
        this.indicatorId = indicatorId;
    }

    public String getIndicatorName() {
        logManager.logInfo("Entering and exiting 'getIndicatorName()' method");
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        logManager.logInfo("Entering and exiting 'setIndicatorName(String indicatorName)' method");
        this.indicatorName = indicatorName;
    } 
    private HashMap<Integer, Integer> getIndicatorCategoryMap(){
        HashMap<Integer, Integer> indicatorCategoryMap = new HashMap<Integer, Integer>();
        try{
            
        }catch(Exception exception){}
        return indicatorCategoryMap;
    }
    
    public int getCivilInsecurityLevelId(String civilInsecurityLevel) {
        logManager.logInfo("Entering 'getCivilInsecurityLevelId(String civilInsecurityLevel)' method");
        int id = 0;
        try {
            sqlString = queryBuilder.querySelectCivilInsecurityLevelId(civilInsecurityLevel);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                id = resultset.getInt("id");
            }
        } catch (Exception exception) {
        logManager.logError("Exception was throwna and caught in 'getCivilInsecurityLevelId(String civilInsecurityLevel)' method");
        }
        logManager.logInfo("Exiting 'getCivilInsecurityLevelId(String civilInsecurityLevel)' method");
        return id;
    } 
    public String getCivilInsecurityLevelName(int civilInsecurityId) {
        logManager.logInfo("Entering 'getCivilInsecurityLevelName(int civilInsecurityId)' method");
        String level = "";
        try {
            sqlString = queryBuilder.querySelectCivilInsecurityLevelName(civilInsecurityId);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                level = resultset.getString("level");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getCivilInsecurityLevelName(int civilInsecurityId)' method");
        }
        logManager.logInfo("Exiting 'getCivilInsecurityLevelName(int civilInsecurityId)' method");
        return level;
    }    
}
