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

/**
 *
 * @author Chemweno
 */
public class Market {

    private int marketId = 0;
    private String marketName = "";
    private ResultSet resultset = null;
    private ExecuteQuery select = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private String sqlString = "";
    private static LogManager logManager = new LogManager();

    public int getMarketId(String marketName) {
        logManager.logInfo("Entering 'getMarketId(String marketName)' method");
        try {
            sqlString = queryBuilder.querySelectMarketId(marketName);
            resultset = select.executeSelect(sqlString);
            if (resultset.next()) {
                marketId = resultset.getInt("id");
            }
        } catch (SQLException exception) {
        logManager.logError("SQLException was thrown and caught in 'getMarketId(String marketName)' method");
        } 
        catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getMarketId(String marketName)' method");
        }
        logManager.logInfo("Exiting 'getMarketId(String marketName)' method");
        return marketId;
    }

    public void setMarketId(int marketId) {
        logManager.logInfo("Entering and exiting 'setMarketId(int marketId)' method");
        this.marketId = marketId;
    }

    public String getMarketName() {
        logManager.logInfo("Entering and exiting 'getMarketName()' method");
        return marketName;
    }

    public void setMarketName(String marketName) {
        logManager.logInfo("Entering and exiting 'setMarketName(String marketName)' method");
        this.marketName = marketName;
    }
}
