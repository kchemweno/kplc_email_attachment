/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class ChartRenderers {

    private XYBarRenderer xyBarRenderer = null;
    private XYSplineRenderer xySplineRenderer =  null;   
    private static LogManager logManager = new LogManager();

    public XYBarRenderer getXyBarRenderer(int seriesIndex, Color color) {
        logManager.logInfo("Entering 'getXyBarRenderer(int seriesIndex, Color color)' method");
        xyBarRenderer = new XYBarRenderer(0.5);
        try {
            //Remove shadows on bars
            xyBarRenderer.setShadowVisible(false);
            logManager.logInfo("Running xyBarRenderer.setBaseToolTipGenerator in 'getXyBarRenderer(int seriesIndex, Color color)' method");
            //Set color of series
            xyBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                    new SimpleDateFormat("MMM"), new DecimalFormat("0")));
            logManager.logInfo("Setting seriesIndex "+seriesIndex+" seriesPaint to "+color+" in 'getXyBarRenderer(int seriesIndex, Color color)' method");
            xyBarRenderer.setSeriesPaint(seriesIndex, color);           
            
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'getXyBarRenderer(int seriesIndex, Color color)' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'getXyBarRenderer(int seriesIndex, Color color)' method");
        return xyBarRenderer;
    }

    public XYSplineRenderer getXySplineRenderer(int seriesIndex, Color color) {
        logManager.logInfo("Entering 'getXySplineRenderer(int seriesIndex, Color color)' method");
        xySplineRenderer = new XYSplineRenderer();        
        try {   
            logManager.logInfo("Running xySplineRenderer.setBaseToolTipGenerator in 'getXySplineRenderer(int seriesIndex, Color color)' method");
            xySplineRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                    new SimpleDateFormat("MMM"), new DecimalFormat("0")));            
            //Set color of series
            //xySplineRenderer.setSeriesPaint(seriesIndex, color);
            logManager.logInfo("Setting seriesIndex 0 seriesPaint to "+color+" in 'getXySplineRenderer(int seriesIndex, Color color)' method");
            xySplineRenderer.setSeriesPaint(0, color);
            //Show shapes in lines
            xySplineRenderer.setBaseShapesVisible(true);                                        
                    
        } catch (Exception exception) {   
            logManager.logError("Exception thrown and caught in 'getXySplineRenderer(int seriesIndex, Color color)' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'getXySplineRenderer(int seriesIndex, Color color)' method");
        return xySplineRenderer;
    }
}
