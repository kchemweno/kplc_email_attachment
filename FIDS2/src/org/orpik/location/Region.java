/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.location;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;
import org.orpik.modular.DataEntry;

/**
 *
 * @author Chemweno
 */
public class Region {
    
    private int regionId = 0;
    private String regionName = "";
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private DataEntry dataEntry = new DataEntry();
    private ResultSet resultset = null;    
    private static LogManager logManager = new LogManager();

    public int getRegionId(String region) {
        logManager.logInfo("Entering 'getRegionId(String region)' method");
        try{
        resultset = getResultset(queryBuilder.querySelectRegionId(region));
        if(this.resultset.next()){
            regionId = this.resultset.getInt("region_id");
        }
        }catch(SQLException sqlException){
        logManager.logError("SQLException was thrown and caught in 'getRegionId(String region)' method");
        }
        catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getRegionId(String region)' method");
        }
        logManager.logInfo("Exiting 'getRegionId(String region)' method");
        return regionId;
    }

    public void setRegionId(int regionId) {
        logManager.logInfo("Entering and exiting 'setRegionId(int regionId)' method");
        this.regionId = regionId;
    }

    public String getRegionName() {
        logManager.logInfo("Entering and exiting 'getRegionName()' method");
        return regionName;
    }

    public void setRegionName(String regionName) {
        logManager.logInfo("Entering and exiting 'setRegionName(String regionName)' method");
        this.regionName = regionName;
    }    
    private ResultSet getResultset(String sqlSelect){
        logManager.logInfo("Entering 'getResultset(String sqlSelect)' method");
        ResultSet resultset = null;
        try{
           resultset = executeQuery.executeSelect(sqlSelect); 
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getResultset(String sqlSelect)' method");
        }
        logManager.logInfo("Exiting 'getResultset(String sqlSelect)' method");
        return resultset;
    }     
}
