/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.data.importt;

import org.orpik.data.ExecuteQuery;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class InsertXmlData {
    private String sqlInsertMarketXmlData = "";
    //private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private int insertDataResult  = 0;
    private static LogManager logManager = new LogManager();
    
    public int saveData(String queryPrefix, String querySuffix){
        logManager.logInfo("Entering 'saveData(String queryPrefix, String querySuffix)' method");
        try{
            //Combine sql string parts
            sqlInsertMarketXmlData = queryPrefix + querySuffix;//.replaceFirst(",", "");
            if(sqlInsertMarketXmlData.matches("VALUES ,") || sqlInsertMarketXmlData.contains("VALUES ,")){
                 sqlInsertMarketXmlData = sqlInsertMarketXmlData.replaceAll("VALUES ,", "VALUES ");
            }
            System.out.println(sqlInsertMarketXmlData);
            //Insert data to database
            insertDataResult = executeQuery.executeInsert(sqlInsertMarketXmlData);
            //System.out.println(sqlInsertMarketXmlData);
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'saveData(String queryPrefix, String querySuffix)' method");
        }
        logManager.logInfo("Exiting 'saveData(String queryPrefix, String querySuffix)' method");
        return insertDataResult;
    }
    
    //Commit in case there are no errors saving data
    public void commitTransaction(){     
        logManager.logInfo("Entering 'commitTransaction()' method");
        try{
            executeQuery.commit();
        }catch(Exception exception){
        logManager.logInfo("Exception was thrown and caught in 'commitTransaction()' method");
        }   
        logManager.logInfo("Exiting 'commitTransaction()' method");
    } 
    
    //Roll back in case of errors saving data
    public void rollBackTransaction(){  
        logManager.logInfo("Entering 'rollBackTransaction()' method");
        try{
            executeQuery.rollBack();
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'rollBackTransaction()' method");
        } 
        logManager.logInfo("Exiting 'rollBackTransaction()' method");
    }     
}
