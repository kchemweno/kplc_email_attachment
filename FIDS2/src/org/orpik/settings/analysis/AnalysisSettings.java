/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.settings.analysis;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class AnalysisSettings {
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    public static ArrayList<String> seriesYearsList = new ArrayList<>();
    public static ArrayList<String> averageYearsList = new ArrayList<>();
    private String sqlString = "";
    private ResultSet resultset = null;
    private static LogManager logManager = new LogManager();

    public AnalysisSettings() {
        logManager.logInfo("Entering 'AnalysisSettings()' constructor");
        //Set default series and average years, these will update static variables seriesYearsList and averageYearsList
        setSeriesYearsList();
        setAverageYearsList();
        logManager.logInfo("Exiting 'AnalysisSettings()' constructor");
    }  
       
    public void setSeriesYearsList(){
        logManager.logInfo("Entering 'setSeriesYearsList()' method");
        String seriesYears = "";
        String[] splitYears = {};
        int index = 0;
        try{
            sqlString = queryBuilder.sqlSelectAnalysisSettings();
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
                seriesYears = resultset.getString("series");
            }
            splitYears = seriesYears.split(",");
            while(index < splitYears.length){
                seriesYearsList.add(splitYears[index]);
                index++;
            }
        }catch(Exception exception){  
            logManager.logError("Exception was thrown and caught in 'setSeriesYearsList()' method");
        }     
        logManager.logInfo("Exiting 'setSeriesYearsList()' method");
    }
    
    public ArrayList<String> getSeriesYearsList(){
        logManager.logInfo("Entering and exiting 'getSeriesYearsList()' method");
        return seriesYearsList;
    }
    
    public void setAverageYearsList(){
        logManager.logInfo("Entering 'setAverageYearsList()' method");
        String averageYears = "";
        String[] splitYears = {};
        int index = 0;
        try{
            sqlString = queryBuilder.sqlSelectAnalysisSettings();
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
                averageYears = resultset.getString("average");
            }
            splitYears = averageYears.split(",");
            while(index < splitYears.length){
                averageYearsList.add(splitYears[index]);
                index++;
            }    
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'setAverageYearsList()' method");
        }
        logManager.logInfo("Exiting 'setAverageYearsList()' method");
    }
    
    public ArrayList<String> getAverageYearsList(){
        return averageYearsList;
    }
}
