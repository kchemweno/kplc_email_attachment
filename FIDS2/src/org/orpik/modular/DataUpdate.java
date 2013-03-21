/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.location.Market;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class DataUpdate {

    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private Market market = new Market();
    
    private String sqlUpdate = "";
    private int result = 0;
    private int year = 0;
    private int monthId = 0;
    private int marketId = 0;
    private static LogManager logManager = new LogManager();
    //Update market data

    //Update market or slim part 1 price
    public int updateMarketData(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,
            int indicatorId, int newValue, int week) {
        logManager.logInfo("Entering 'updateMarketData(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + " int indicatorId, int newValue, int week)' method");
        year = Integer.parseInt(cboYear.getSelectedItem().toString());
        monthId = cboMonth.getSelectedIndex() + 1;
        marketId = market.getMarketId(cboMarket.getSelectedItem().toString());
        try {
            //sqlUpdate = queryBuilder.queryUpdateMarketdata(year, monthId, marketId, indicatorId, newValue, week);
            sqlUpdate = queryBuilder.queryInsertUpdateMarketdata(year, monthId, marketId, indicatorId, newValue, week);
            System.out.println(sqlUpdate);
            //Check if data already exists in database
                result = executeQuery.executeInsert(sqlUpdate);
                if (result > 0) {
                    /**
                     * Commit transaction
                     */
                    executeQuery.commit();
                } else {
                    /**
                     * Roll back transaction
                     */
                    executeQuery.rollBack();
                }
        } catch (Exception exception) {
        logManager.logError("Exception was thrown and caught in 'updateMarketData(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + " int indicatorId, int newValue, int week)' method");
        }
        logManager.logInfo("Exiting 'updateMarketData(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + " int indicatorId, int newValue, int week)' method");
        return result;
    }
    
    //Update details value, one of {location name, triangulation, key informant, data trust level}
    public int updateSlimPart1Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,
            int categoryId, String newValue, String detail) {
        logManager.logInfo("Entering 'updateSlimPart1Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + "  int categoryId, String newValue, String detail)' method");
        year = Integer.parseInt(cboYear.getSelectedItem().toString());
        monthId = cboMonth.getSelectedIndex() + 1;
        marketId = market.getMarketId(cboMarket.getSelectedItem().toString());
        try {
            sqlUpdate = queryBuilder.queryUpdateSlimsPart1Details(year, monthId, marketId, categoryId, newValue, detail);
            System.out.println(sqlUpdate);
            //Check if data already exists in database
                result = executeQuery.executeInsert(sqlUpdate);
                if (result > 0) {
                    /**
                     * Commit transaction
                     */
                    executeQuery.commit();
                } else {
                    /**
                     * Roll back transaction
                     */
                    executeQuery.rollBack();
                }
        } catch (Exception exception) {
        logManager.logError("Exception was thrown and caught in 'updateSlimPart1Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + "  int categoryId, String newValue, String detail)' method");
        }
        logManager.logInfo("Exiting 'updateSlimPart1Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + "  int categoryId, String newValue, String detail)' method");
        return result;
    }  
    
    //Update lookup value, one of {location name, triangulation, key informant, data trust level}
    public int updateSlimPart1Lookup(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,
            String newValue, String detail) {
        logManager.logInfo("Entering 'updateSlimPart1Lookup(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + " String newValue, String detail)' method");
        year = Integer.parseInt(cboYear.getSelectedItem().toString());
        monthId = cboMonth.getSelectedIndex() + 1;
        marketId = market.getMarketId(cboMarket.getSelectedItem().toString());
        try {
            sqlUpdate = queryBuilder.queryUpdateSlimsPart1Lookup(year, monthId, marketId, newValue, detail);
            System.out.println(sqlUpdate);
            //Check if data already exists in database
                result = executeQuery.executeInsert(sqlUpdate);
                if (result > 0) {
                    /**
                     * Commit transaction
                     */
                    executeQuery.commit();
                } else {
                    /**
                     * Roll back transaction
                     */
                    executeQuery.rollBack();
                }
        } catch (Exception exception) {
        logManager.logError("Exception was thrown and caught in 'updateSlimPart1Lookup(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + " String newValue, String detail)' method");
        }
        logManager.logInfo("Exitijg 'updateSlimPart1Lookup(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,"
                + " String newValue, String detail)' method");
        return result;
    }
    
    //Update details value, one of {location name, triangulation, key informant, data trust level}
    public int updateSlimPart2Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket,
            int indicatorId, String newValue, String tableColumn) {
        logManager.logInfo("Entering 'updateSlimPart2Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, "
                + "int indicatorId, String newValue, String tableColumn)' method");
        year = Integer.parseInt(cboYear.getSelectedItem().toString());
        monthId = cboMonth.getSelectedIndex() + 1;
        marketId = market.getMarketId(cboMarket.getSelectedItem().toString());
        try {
            sqlUpdate = queryBuilder.queryUpdateSlimsPart2Details(year, monthId, marketId, indicatorId, newValue, tableColumn);
            System.out.println(sqlUpdate);
            //Check if data already exists in database
                result = executeQuery.executeInsert(sqlUpdate);
                if (result > 0) {
                    /**
                     * Commit transaction
                     */
                    executeQuery.commit();
                } else {
                    /**
                     * Roll back transaction
                     */
                    executeQuery.rollBack();
                }
        } catch (Exception exception) {
        logManager.logError("Exception was thrown and caught in 'updateSlimPart2Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, "
                + "int indicatorId, String newValue, String tableColumn)' method");
        }
        logManager.logInfo("Exiting 'updateSlimPart2Details(javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, "
                + "int indicatorId, String newValue, String tableColumn)' method");
        return result;
    }      
}
