/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.indicators;

import java.sql.ResultSet;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class IndicatorCategory {
    private int indicatorCategoryId = 0;
    private String indicatorCategoryName = "";
    private String sqlString = "";
    private ResultSet resultset = null;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private static LogManager logManager = new LogManager();

    public int getIndicatorCategoryId(String categoryName) {
        logManager.logInfo("Entering 'getIndicatorCategoryId(String categoryName)' method");
        try{
            sqlString = queryBuilder.querySelectIndicatorCategoryId(categoryName);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                indicatorCategoryId = resultset.getInt("id");
            }            
        }catch(Exception exception){
        logManager.logInfo("Exception was thrown and caught in 'getIndicatorCategoryId(String categoryName)' method");
        }
        logManager.logInfo("Exiting 'getIndicatorCategoryId(String categoryName)' method");
        return indicatorCategoryId;
    }     
}
