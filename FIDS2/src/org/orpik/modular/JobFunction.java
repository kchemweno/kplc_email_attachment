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
public class JobFunction {
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private int jobFunctionId = 0;
    private String jobFunctionName = "";
    private String sqlString = "";
    private ResultSet resultset = null;  
    private static LogManager logManager = new LogManager();

    public int getJobFunctionId(String jobFunctionName) {
        logManager.logInfo("Entering 'getJobFunctionId(String jobFunctionName)' method");
        try{
            sqlString = queryBuilder.sqlSelectJobFunctionId(jobFunctionName);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
            jobFunctionId = resultset.getInt("job_funct_id");
            }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getJobFunctionId(String jobFunctionName)' method");
        }   
        logManager.logInfo("Exiting 'getJobFunctionId(String jobFunctionName)' method");
        return jobFunctionId;
    }

    public void setJobFunctionId(int jobFunctionId) {
        logManager.logInfo("Entering and exiting 'setJobFunctionId(int jobFunctionId)' method");
        this.jobFunctionId = jobFunctionId;
    }

    public String getJobFunctionName() {
        logManager.logInfo("Entering and exiting 'getJobFunctionName()' method");
        return jobFunctionName;
    }

    public void setJobFunctionName(String jobFunctionName) {
        logManager.logInfo("Entering and exiting 'setJobFunctionName(String jobFunctionName)' method");
        this.jobFunctionName = jobFunctionName;
    }    
}
