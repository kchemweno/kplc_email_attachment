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
public class Language {
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private int languageId = 0;
    private String languageName = "";
    private String sqlString = "";
    private ResultSet resultset = null;
    private static LogManager logManager = new LogManager();

    public int getLanguageId(String languageName) {
        logManager.logInfo("Entering 'getLanguageId(String languageName)' method");
        try{
            sqlString = queryBuilder.sqlSelectLanguageId(languageName);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
            languageId = resultset.getInt("lang_id");
            }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getLanguageId(String languageName)' method");
        }      
        logManager.logInfo("Exiting 'getLanguageId(String languageName)' method");
        return languageId;
    }

    public void setLanguageId(int languageId) {
        logManager.logInfo("Entering and exiting 'setLanguageId(int languageId)' method");
        this.languageId = languageId;
    }

    public String getLanguageName() {
        logManager.logInfo("Entering and exiting 'getLanguageName()' method");
        return languageName;
    }

    public void setLanguageName(String languageName) {
        logManager.logInfo("Entering and exiting 'setLanguageName(String languageName)' method");
        this.languageName = languageName;
    }    
}
