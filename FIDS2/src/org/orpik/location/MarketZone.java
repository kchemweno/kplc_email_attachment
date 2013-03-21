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
public class MarketZone {
    
    private int marketZoneId = 0;
    private String marketZoneName = "";
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private DataEntry dataEntry = new DataEntry();
    private ResultSet resultset = null;
    private static LogManager logManager = new LogManager();

    public int getMarketZoneId(String marketZone) {
        logManager.logInfo("Entering 'getMarketZoneId(String marketZone)' method");
        //int marketZoneId = 0;
        try{
        this.resultset = getResultset(queryBuilder.querySelectMarketZoneId(marketZone));
        if(this.resultset.next()){
            this.marketZoneId = this.resultset.getInt("market_zone_id");
        }
        }catch(SQLException sqlException){
        logManager.logError("SQLException was thrown and caught in 'getMarketZoneId(String marketZone)' method");
        }
        catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getMarketZoneId(String marketZone)' method");
        }
        logManager.logInfo("Exiting 'getMarketZoneId(String marketZone)' method");
        return this.marketZoneId;
    }

    public void setMarketZoneId(int marketZoneId) {
        logManager.logInfo("Entering and exiting 'setMarketZoneId(int marketZoneId)' method");
        this.marketZoneId = marketZoneId;
    }
    
    public String getMarketZoneName() {
        logManager.logInfo("Entering and exiting 'getMarketZoneName()' method");
        return marketZoneName;
    }

    public void setMarketZoneName(String marketZoneName) {
        logManager.logInfo("Entering and exiting 'setMarketZoneName(String marketZoneName)' method");
        this.marketZoneName = marketZoneName;
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
