/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import java.sql.ResultSet;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class Organisation {
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private int organisationId = 0;
    private String organisationName = "";
    private String sqlString = "";
    private ResultSet resultset = null;
    private static LogManager logManager = new LogManager();

    public int getOrganisationId(String organisationName) {
        logManager.logInfo("Entering 'getOrganisationId(String organisationName)' method");
        try{
            sqlString = queryBuilder.sqlSelectOrganisationId(organisationName);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
            organisationId = resultset.getInt("org_id");
            }
        }catch(Exception exception){
            logManager.logError("Exception was thrown and caught in 'getOrganisationId(String organisationName)' method");
        }
        logManager.logInfo("Exiting 'getOrganisationId(String organisationName)' method");        
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        logManager.logInfo("Entering and exiting 'setOrganisationId(int organisationId)' method");
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        logManager.logInfo("Entering and exiting 'getOrganisationName()' method");
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        logManager.logInfo("Entering and exiting 'setOrganisationName(String organisationName)' method");
        this.organisationName = organisationName;
    }  
}
