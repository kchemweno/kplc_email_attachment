/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.period;

import java.sql.ResultSet;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Month {

    private String monthName = "";
    private int monthId = 0;
    private ResultSet resultset = null;
    private ExecuteQuery select = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private String sqlString = "";
    private static LogManager logManager = new LogManager();

    public int getMonthId(String monthName) {
        logManager.logInfo("Entering 'getMonthId(String monthName)' method");
        //Reset month id
        this.monthId = 0;
        try {
            switch (monthName) {
                case "January":
                    monthId = 1;
                    break;
                case "February":
                    monthId = 2;
                    break;
                case "March":
                    monthId = 3;
                    break;
                case "April":
                    monthId = 4;
                    break;
                case "May":
                    monthId = 5;
                    break;
                case "June":
                    monthId = 6;
                    break;
                case "July":
                    monthId = 7;
                    break;
                case "August":
                    monthId = 8;
                    break;
                case "September":
                    monthId = 9;
                    break;
                case "October":
                    monthId = 10;
                    break;
                case "November":
                    monthId = 11;
                    break;
                case "December":
                    monthId = 12;
                    break;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getMonthId(String monthName)' method");
        }
        logManager.logInfo("Exiting 'getMonthId(String monthName)' method");
        return monthId;
    }

    public String getMonthName(int monthId) {
        logManager.logInfo("Entering 'getMonthName(int monthId)' method");
        //Reset month id
        monthName = "";
        try {
            switch (monthId) {
                case 1:
                    monthName = "January";
                    break;
                case 2:
                    monthName = "February";
                    break;
                case 3:
                    monthName = "March";
                    break;
                case 4:
                    monthName = "April";
                    break;
                case 5:
                    monthName = "May";
                    break;
                case 6:
                    monthName = "June";
                    break;
                case 7:
                    monthName = "July";
                    break;
                case 8:
                    monthName = "August";
                    break;
                case 9:
                    monthName = "September";
                    break;
                case 10:
                    monthName = "October";
                    break;
                case 11:
                    monthName = "November";
                    break;
                case 12:
                    monthName = "December";
                    break;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getMonthName(int monthId)' method");
        }
        logManager.logInfo("Exiting 'getMonthName(int monthId)' method");
        return monthName;
    }
    //Get previous month

    public int getPreviousMonth(int currentMonth) {
        logManager.logInfo("Entering 'getPreviousMonth(int currentMonth)' method");
        try {
            //If current month is January, then previous month December, else it is current month - 1 
            monthId = currentMonth == 1 ? 12 : currentMonth - 1;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getPreviousMonth(int currentMonth)' method");
        }
        logManager.logInfo("Exiting 'getPreviousMonth(int currentMonth)' method");
        return monthId;
    }
}
