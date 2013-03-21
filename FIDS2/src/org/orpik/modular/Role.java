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
public class Role {
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private int roleId = 0;
    private String roleName = "";
    private String sqlString = "";
    private ResultSet resultset = null;    
    private static LogManager logManager = new LogManager();

    public int getRoleId(String roleName) {
        logManager.logInfo("Entering 'getRoleId(String roleName)' method");
        try{
            sqlString = queryBuilder.sqlSelectRoleId(roleName);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
            roleId = resultset.getInt("role_id");
            }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getRoleId(String roleName)' method");
        }        
        logManager.logInfo("Exiting 'getRoleId(String roleName)' method");
        return roleId;
    }

    public void setRoleId(int roleId) {
        logManager.logInfo("Entering and exiting 'setRoleId(int roleId)' method");
        this.roleId = roleId;
    }

    public String getRoleName() {
        logManager.logInfo("Entering and exiting 'getRoleName()' method");
        return roleName;
    }

    public void setRoleName(String roleName) {
        logManager.logInfo("Entering and exiting 'setRoleName(String roleName)' method");
        this.roleName = roleName;
    }    
}
