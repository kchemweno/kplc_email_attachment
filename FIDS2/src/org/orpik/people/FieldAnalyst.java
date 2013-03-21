/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.people;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class FieldAnalyst extends Analyst{
    private ResultSet resultset = null;
    private static LogManager logManager = new LogManager();
    
    private int fieldAnalystId = 0;
    public int getFieldAnalystId(String fieldAnalystName) {  
        logManager.logInfo("Entering 'getFieldAnalystId(String fieldAnalystName)' method");
        try{
        this.resultset = super.getResultset(super.queryBuilder.querySelectFieldAnalystId(fieldAnalystName));
        if(resultset.next()){
            fieldAnalystId = resultset.getInt("field_analyst_id");
        }
        }catch(SQLException sqlException){}
        catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getFieldAnalystId(String fieldAnalystName)' method");
        }
        logManager.logInfo("Exiting 'getFieldAnalystId(String fieldAnalystName)' method");
        return fieldAnalystId;
    }    
}
