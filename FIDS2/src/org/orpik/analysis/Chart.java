/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Chart {

    public String chartTitle;
    public String chartXaxisLabel;
    public String chartYaxisLabel = "SoSh";//TO DO: Get label from database
    public ChartPanel chartPanel;
    private String legendTitle;// = "Average";
    public String fiveYearAverageColumnZeroText = "Monthly Average";
    public static int averageStartYear;
    public static int averageEndYear;
    public static String commodityName;
    private static LogManager logManager = new LogManager();

    public String getLegendTitle(ArrayList<Integer> averageYears) {
        logManager.logInfo("Entering 'getLegendTitle(ArrayList<Integer> averageYears)' method");
        int maxYear = 0;
        int minYear = 0;
        try {
            
            maxYear = averageYears.get(averageYears.size() - 1);
            minYear = averageYears.get(0);
            logManager.logInfo("Creating legend title");
            //TO DO: Figure how the legend label will be picked from user settings and used here
            legendTitle = "Average (" + minYear + " - " + maxYear + ")";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'getLegendTitle(ArrayList<Integer> averageYears)' method");
        }
        logManager.logInfo("Exiting 'getLegendTitle(ArrayList<Integer> averageYears)' method");
        return legendTitle;
    }
}
