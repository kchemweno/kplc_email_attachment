/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import org.jfree.data.time.Month;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class ChartDataset extends Chart {

    private TimeSeriesCollection timeSeriesCollection = null;
    private TimeSeries timeSeries = null;
    private static LogManager logManager = new LogManager();

    //Create dataset for five years average chosen
    public IntervalXYDataset getTimeSeriesCollection(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel) {
        logManager.logInfo("Entering 'getTimeSeriesCollection(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel)' method");
        /*
         * controlYear variable is : Used to control startMonth, when the
         * current month is in the current year, we use 2009, when it is in the
         * next year, we use 2010 this will help the chart to start plotting
         * values from the start month chosen.
         */
        logManager.logInfo("Setting 2009 as the current year");
        int controlYear = 2009;

        timeSeriesCollection = null;
        timeSeries = null;
        timeSeries = new TimeSeries(legendLabel);
        try {
            //Get row index in table where text in column 0 match fiveYearAverageColumnZeroText string
            //averageRow = common.getRowIndex(super.fiveYearAverageColumnZeroText, table);
            //Traverse table building dataset, start with start month to dec then begin from jan to start month
            for (int column = startMonth; column < table.getColumnCount(); column++) {
                if (table.getValueAt(tableRow, column) != null) {
                    timeSeries.add(new Month(column, controlYear), Double.parseDouble(table.getValueAt(tableRow, column).toString()));
                }
            }

            //Traverse table buidind dataset, begin from jan to start month, do this if start month is not jan
            if (startMonth != 1) {
                controlYear = 2010;
                for (int column = 1; column < startMonth; column++) {
                    if (table.getValueAt(tableRow, column) != null) {
                        timeSeries.add(new Month(column, controlYear), Double.parseDouble(table.getValueAt(tableRow, column).toString()));
                    }
                }
            }
            logManager.logInfo("Creating time series collection");
            //Create Time series collection using 
            timeSeriesCollection = new TimeSeriesCollection(timeSeries);
            //Important. This setting anchors the  shapes on the grid lines
            timeSeriesCollection.setXPosition(TimePeriodAnchor.MIDDLE);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught");
        }
        logManager.logInfo("Exiting  'getTimeSeriesCollection(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel)' method");
        return timeSeriesCollection;
    }

//Create dataset for series years chosen
    public XYDataset getSeriesDataset(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel) {
        logManager.logInfo("Entering 'getSeriesDataset(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel)'  method");
        //int averageRow = -1;
        /*
         * controlYear variable is : Used to control startMonth, when the
         * current month is in the current year, we use 2009, when it is in the
         * next year, we use 2010 this will help the chart to start plotting
         * values from the start month chosen.
         */
        int controlYear = 2009;
        double monthValue;
        timeSeriesCollection = null;
        timeSeries = null;
        timeSeries = new TimeSeries(legendLabel);
        try {
            //Get row index in table where text in column 0 match fiveYearAverageColumnZeroText string
            //averageRow = common.getRowIndex(super.fiveYearAverageColumnZeroText, table);
            //Traverse table building dataset, start with start month to dec then begin from jan to start month
            for (int column = startMonth; column < table.getColumnCount(); column++) {
                if (table.getValueAt(tableRow, column) != null) {
                    timeSeries.add(new Month(column, controlYear), Double.parseDouble(table.getValueAt(tableRow, column).toString()));
                }
            }

            //Traverse table buiding dataset, begin from jan to start month, do this if start month is not jan
            if (startMonth != 1) {
                controlYear = 2010;
                for (int column = 1; column < startMonth; column++) {
                        //Get the value in the next row (i.e next month of next year e.g for feb 2003/2004, select feb 2004)                    
                    if (table.getValueAt(tableRow+1, column) != null) {
                        timeSeries.add(new Month(column, controlYear), Double.parseDouble(table.getValueAt(tableRow+1, column).toString()));                        
                    }
                }
            }
            //Create Time series collection using 
            timeSeriesCollection = new TimeSeriesCollection(timeSeries);
            //Important. This setting anchors the  shapes on the grid lines
            timeSeriesCollection.setXPosition(TimePeriodAnchor.MIDDLE);           
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in  'getSeriesDataset(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel)' method");
        }
        logManager.logInfo("Exiting  'getSeriesDataset(javax.swing.JTable table, int startMonth, int tableRow, String legendLabel)' method");
        return timeSeriesCollection;
    }
}
