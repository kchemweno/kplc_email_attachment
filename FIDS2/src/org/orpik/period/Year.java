/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.period;

import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class Year {
    private int year = 0;
    private static LogManager logManager = new LogManager();
    
    //Get previous month's year
    public int getPreviousMonthYear(int currentMonth, int currentYear){
        logManager.logInfo("Entering 'getPreviousMonthYear(int currentMonth, int currentYear)' method");
        try{
            //If current month is January, then previous month's year is last year (this year - 1)
            year = currentMonth == 1 ? currentYear - 1 : currentYear;
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getPreviousMonthYear(int currentMonth, int currentYear)' method");
        }
        logManager.logInfo("Exiting 'getPreviousMonthYear(int currentMonth, int currentYear)' method");
        return year;
    }
}
