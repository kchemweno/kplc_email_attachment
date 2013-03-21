/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.location;

import java.sql.ResultSet;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class District {

    private int districtId = 0;
    private String districtName = "";
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private ResultSet resultset = null;
    private String sqlString = "";
    private static LogManager logManager = new LogManager();

    public int getDistrictId(String districtName) {
        logManager.logInfo("Entering 'getDistrictId(String districtName)' method");
        try {
            sqlString = queryBuilder.selectDistrictId(districtName);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                districtId = resultset.getInt("dist_id");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getDistrictId(String districtName)' method");
        }
        logManager.logInfo("Exiting 'getDistrictId(String districtName)' method");
        return districtId;
    }

    public String getDistrictName() {
        logManager.logInfo("Entering 'getDistrictName()' method");
        try {
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getDistrictName()' method");
        }
        logManager.logInfo("Exiting 'getDistrictName()' method");
        return districtName;
    }
}
