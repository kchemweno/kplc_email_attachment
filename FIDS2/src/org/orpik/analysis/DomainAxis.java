/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import java.text.SimpleDateFormat;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.orpik.logging.LogManager;
import org.orpik.settings.analysis.ChartSettings;
import org.orpik.ui.font.FontUtilities;

/**
 *
 * @author Chemweno
 */
public class DomainAxis extends Chart {
    private FontUtilities fontUtilities = new FontUtilities();
    private DateAxis domainAxis = null;
    private static LogManager logManager = new LogManager();

    public DateAxis getDomainAxis(String axisLabel) {
        logManager.logInfo("Entering 'getDomainAxis(String axisLabel)' method");
        try {
            //Initialize Date axis
            domainAxis = new DateAxis();
            domainAxis.setTickLabelsVisible(true);                    
            domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
            //Set tick lables to appear vertically
            domainAxis.setVerticalTickLabels(true);
            //Show or hide tick labels
            if(ChartSettings.domainAxisShowTickLabels == 1){
                domainAxis.setTickLabelsVisible(true);
            }else if(ChartSettings.domainAxisShowTickLabels == 0){
                domainAxis.setTickLabelsVisible(false);
            }
            //Show or hide tick marks
            if(ChartSettings.domainAxisShowTickMarks== 1){
                domainAxis.setTickMarksVisible(true);
            }else if(ChartSettings.domainAxisShowTickMarks == 0){
                domainAxis.setTickMarksVisible(false);
            }             
            //Set tick labels font
           // domainAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
             domainAxis.setTickLabelFont(fontUtilities.getFont(ChartSettings.domainAxisTickLabelFontName, 
                     ChartSettings.domainAxisTickLabelFontBold, ChartSettings.domainAxisTickLabelFontItalic, 
                     ChartSettings.domainAxisTickLabelFontSize));
            //Set date format to short date format
            domainAxis.setDateFormatOverride(new SimpleDateFormat("MMM"));

        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'getDomainAxis(String axisLabel)' method");
        }
        logManager.logInfo("Exiting 'getDomainAxis(String axisLabel)' method");
        return domainAxis;
    }
}
