/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import org.jfree.chart.axis.NumberAxis;
import org.orpik.logging.LogManager;
import org.orpik.settings.analysis.ChartSettings;
import org.orpik.ui.font.FontUtilities;

/**
 *
 * @author Chemweno
 */
public class RangeAxis {
    private org.jfree.chart.axis.ValueAxis valueAxis = null;
    private FontUtilities fontUtilities = new FontUtilities();
    private static LogManager logManager = new LogManager();
    
    public org.jfree.chart.axis.ValueAxis getRangeAxis(String axisLabel){
        try{
            logManager.logInfo("Entering 'getRangeAxis(String axisLabel)' method");
            //Instantiate x-axis 
            valueAxis = new NumberAxis(axisLabel);
            //Set tick labels font
            //valueAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
            valueAxis.setTickLabelFont(fontUtilities.getFont(ChartSettings.rangeAxisTickLabelFontName, 
                    ChartSettings.rangeAxisTickLabelFontBold, ChartSettings.rangeAxisTickLabelFontItalic,  
                    ChartSettings.rangeAxisTickLabelFontSize));
            
            //valueAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 16));
            valueAxis.setLabelFont(fontUtilities.getFont(ChartSettings.rangeAxisLabelFontName, 
                    ChartSettings.rangeAxisLabelFontBold, ChartSettings.rangeAxisLabelFontItalic, 
                    ChartSettings.rangeAxisLabelFontSize));
            
            //Show or hide range axis labels
            if(ChartSettings.rangeAxisShowTickLabels == 1){
                valueAxis.setTickLabelsVisible(true);
            }else if(ChartSettings.rangeAxisShowTickLabels == 0){
                valueAxis.setTickLabelsVisible(false);
            }
            //Show or hide tick marks
            if(ChartSettings.rangeAxisShowTickMarks== 1){
                valueAxis.setTickMarksVisible(true);
            }else if(ChartSettings.rangeAxisShowTickMarks == 0){
                valueAxis.setTickMarksVisible(false);
            }             
           
        }catch(Exception exception){
            logManager.logError("Exception thrown and caught in 'getRangeAxis(String axisLabel)' method");
        }
        logManager.logInfo("Exiting 'getRangeAxis(String axisLabel)' method");
        return valueAxis;
    }
}
