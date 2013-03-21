/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.access.control;

import java.sql.ResultSet;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class AuthenticateUser {
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private static LogManager logManager = new LogManager();
    private ResultSet resultset;
    private String sqlSelect = "";
    
    public boolean isUserallowedAcess(String userName, String userPassword){
        boolean isUserAuthenticated = false;        
        int usersCount = 0;
        try{
            sqlSelect = queryBuilder.querySelectUser(userName, userPassword);            
            resultset = executeQuery.executeSelect(sqlSelect);            
            if(resultset.next()){
                logManager.logInfo("Getting number of people with supplied username and password");
                //Get if there exist user with name and password
                usersCount = resultset.getInt("number_of_users");
                if(usersCount > 0){
                    isUserAuthenticated = true;
                }
            }
        }catch(Exception exception){
            //exception.printStackTrace();
            logManager.logError("Exception is thrown and caught");
        }
        return isUserAuthenticated;
    }
    
    public int getUserId(String username){
        int userId = 0;                
        try{
            sqlSelect = queryBuilder.querySelectUserId(username);
            resultset = executeQuery.executeSelect(sqlSelect);
            if(resultset.next()){
                logManager.logInfo("Getting userid from database table");
                userId = resultset.getInt("user_id");
            }
        }catch(Exception exception){
            logManager.logError("Exception is thrown and caught");
        }
        return userId;
    }
}
