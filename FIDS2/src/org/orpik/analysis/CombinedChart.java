/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.orpik.logging.LogManager;
import org.orpik.settings.analysis.ChartSettings;
import org.orpik.ui.Common;
import org.orpik.ui.font.FontUtilities;

/**
 *
 * @author Chemweno
 */
public class CombinedChart extends Chart {

    //private RangeAxis rangeAxis = new RangeAxis();
    private RangeAxis rangeAxis = new RangeAxis();
    private DomainAxis domainAxis = new DomainAxis();
    private ChartDataset chartDataset = new ChartDataset();
    private ChartRenderers chartRenderers = new ChartRenderers();
    private SeriesPaint seriesPaint = new SeriesPaint();
    private Common common = new Common();
    public static JFreeChart chart = null;
    public static JFreeChart copyChart = null;//This object wil be used to check whether chart object above has been changed 
    public JFreeChart copiedChart = null;//This object wil be used to check whether chart object above has been changed 
    private XYPlot xyPlot = null;
    private int tableRow = -1;
    private String[] splitRgbColor = {};
    private int rColor = 0;
    private int gColor = 0;
    private int bColor = 0;
    private ChartSettings chartSettings = new ChartSettings();
    private FontUtilities fontUtilities = new FontUtilities();
    private static LogManager logManager = new LogManager();

    private XYPlot createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,
            int startMonth, String chartTitle, String yAxisLabel) {

        logManager.logInfo("Entering 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
        int datasetIndex = 0;
        int nextYear = 0;
        String datasetLabel = "";
        String year = "";
        String legendLabel = "";
        IntervalXYDataset averageDataset;
        XYDataset seriesDataset;
        DateAxis xAxis = domainAxis.getDomainAxis(super.chartXaxisLabel);
        ValueAxis yAxis = rangeAxis.getRangeAxis(yAxisLabel);
        try {

            xyPlot = new XYPlot();
            xyPlot.setDomainAxis(xAxis);
            xyPlot.setRangeAxis(yAxis);

            //Add average series if user selected it to be shown
            logManager.logInfo("Checking whether showAverage is true in "
                    + "'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            if (showAverage) {
                //xyPlot.add();
                legendLabel = getLegendTitle(Common.averageYears);
                //Get row on table where 5 year average values are found
                logManager.logInfo("Getting tableRow in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
                tableRow = common.getRowIndex(super.fiveYearAverageColumnZeroText, table);
                logManager.logInfo("Getting averageDataset in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
                averageDataset = chartDataset.getTimeSeriesCollection(table, startMonth, tableRow, legendLabel);
                
                logManager.logInfo("Running xyPlot.setDataset(datasetIndex, averageDataset) in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, "
                        + "javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
                xyPlot.setDataset(datasetIndex, averageDataset);
                logManager.logInfo("Running xyPlot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD) in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, "
                        + "javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
                //Set rendering order
                xyPlot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
                logManager.logInfo("Running xyPlot.setRenderer(datasetIndex, chartRenderers.getXyBarRenderer(datasetIndex, seriesPaint.getSeriesColor(datasetIndex))) in 'createXyPlot(ArrayList<Integer> seriesYears,"
                        + " boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
                //Set series color
                xyPlot.setRenderer(datasetIndex, chartRenderers.getXyBarRenderer(datasetIndex, seriesPaint.getSeriesColor(datasetIndex)));
            }

            //Add all other series             
            for (int index = 0; index < seriesYears.size(); index++) {
                //Determine label for dataset, if it starts from jan, then use year e.g 2003 else use 2003/2004                 
                year = seriesYears.get(index) + "";
                if (startMonth == 1) {                    
                    //Use the year of arraylist as legend label
                    datasetLabel = year;
                } else {
                    nextYear = seriesYears.get(index)+1;     
                    //Use the year of arraylist and next as legend label
                    datasetLabel = year + "/" +nextYear;
                }
                logManager.logInfo("Getting tableRow in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, "
                    + "javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
                //Get row index 
                tableRow = common.getRowIndex(year, table);
                //Create dataset
                //seriesDataset = chartDataset.getSeriesDataset(table, startMonth, tableRow, year);
                seriesDataset = chartDataset.getSeriesDataset(table, startMonth, tableRow, datasetLabel);
                //Add dataset to plot, 1 is added to take care of the already added 5 year average (XYBar) dataset
                xyPlot.setDataset(index + 1, seriesDataset);
                //Set renderer of dataset at index
                xyPlot.setRenderer(index + 1, chartRenderers.getXySplineRenderer(index + 1, seriesPaint.getSeriesColor(index + 1)));
            }
            logManager.logInfo("Running xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD) in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, "
                    + "javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
            logManager.logInfo("Running xyPlot.setOrientation(PlotOrientation.VERTICAL) in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, "
                    + "javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            xyPlot.setOrientation(PlotOrientation.VERTICAL);

        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'createXyPlot(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
        return xyPlot;
    }

    private JFreeChart createChart(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,
            int startMonth, String chartTitle, String yAxisLabel) {
        logManager.logInfo("Entering 'createChart(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,int startMonth, String chartTitle, String yAxisLabel)' method");
        java.awt.Font chartTitleFont = null;//new Font("SansSerif", Font.BOLD, 18);
        XYPlot plot = null;
        
        /*
         * chartTitleFont = new Font(ChartSettings.titleFontName,
         * getFontStyle(ChartSettings.titleFontBold,
         * ChartSettings.titleFontItalic), ChartSettings.titleFontSize);
         */
        chartTitleFont = fontUtilities.getFont(ChartSettings.titleFontName, ChartSettings.titleFontBold,
                ChartSettings.titleFontItalic, ChartSettings.titleFontSize);
        
        try {
            logManager.logInfo("Running  plot = createXyPlot(seriesYears, showAverage, table, startMonth, chartTitle, yAxisLabel) in "
                    + "'createChart(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,int startMonth, String chartTitle, String yAxisLabel)' method");
            plot = createXyPlot(seriesYears, showAverage, table, startMonth, chartTitle, yAxisLabel);
            logManager.logInfo("Running  CombinedChart.chart = new JFreeChart(chartTitle, chartTitleFont, plot, true) in "
                    + "'createChart(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,int startMonth, String chartTitle, String yAxisLabel)' method");
            CombinedChart.chart = new JFreeChart(chartTitle, chartTitleFont, plot, true);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in  'createChart(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,int startMonth, String chartTitle, String yAxisLabel)' method");
        }
        logManager.logInfo("Exiting 'createChart(ArrayList<Integer> seriesYears, boolean showAverage, javax.swing.JTable table,int startMonth, String chartTitle, String yAxisLabel)' method");
        return CombinedChart.chart;
    }

    private void customizeChart(JFreeChart chart) {
        logManager.logInfo("Entering 'customizeChart(JFreeChart chart)' method");
        chart.setBackgroundPaint(new java.awt.Color(255, 255, 255));
        try {
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'customizeChart(JFreeChart chart)' method");
        }
        logManager.logInfo("Exiting 'customizeChart(JFreeChart chart)' method");
    }

    public void showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,
            boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel) {
        try {
            logManager.logInfo("Entering 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Attempt customization
            chart = createChart(seriesYears, showAverage, table, startMonth, chartTitle, yAxisLabel);
            //Set chart background color (encircling area of chart), default is white
            //chart.setBackgroundPaint(new java.awt.Color(255, 255, 255));

            //Get user settings
            
            //Pick background color set by user in his/her chart settings
            logManager.logInfo("Setting chart background paint 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            chart.setBackgroundPaint(new java.awt.Color(getRColor(ChartSettings.otherBackgroundPaint),
                    getGColor(ChartSettings.otherBackgroundPaint), getBColor(ChartSettings.otherBackgroundPaint)));

            logManager.logInfo("Running chartPanel = new ChartPanel(chart, true, true, true, true, true) in  'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            chartPanel = new ChartPanel(chart, true, true, true, true, true);

            logManager.logInfo("Validating chartPanel in  'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            chartPanel.validate();
            logManager.logInfo("Setting chartPanel preferred size in  'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //chartPanel.setPreferredSize(new java.awt.Dimension(630, 390));
            chartPanel.setPreferredSize(new java.awt.Dimension(570, 370));
            logManager.logInfo("Repainting Chart Panel in  'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Repaint the panel
            chartPanel.repaint();
            logManager.logInfo("Setting chartPanel layout to FlowLayout in 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Set layout
            chartPanel.setLayout(new FlowLayout());
            logManager.logInfo("Repainting panel in  'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Repaint the panel
            panel.repaint();
            logManager.logInfo("Setting panel layout to FlowLayout in 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Set layout
            panel.setLayout(new FlowLayout());
            logManager.logInfo("Removing all components from panel in 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Remove all existing components on the panel if any
            panel.removeAll();
            logManager.logInfo("Adding chartPanel to panel in 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //Show the chart on the reports tot middle panel
            panel.add(chartPanel);
            logManager.logInfo("Validating panel in 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
            //pnlReportsMiddleChart.repaint();
            panel.validate();
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
        }
        logManager.logInfo("Exiting 'showChart(javax.swing.JPanel panel, ArrayList<Integer> seriesYears,"
                    + "boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
    }
    //Show chart properties dialog to perform edits

    public void editChartProperies() {
        logManager.logInfo("Entering 'editChartProperies()' method");
        chartPanel.doEditChartProperties();
        logManager.logInfo("Exiting 'editChartProperies()' method");
    }

    private String[] splitRgbCode(String rgbColor) {
        try {
            logManager.logInfo("Entering 'splitRgbCode(String rgbColor)' method");
            splitRgbColor = rgbColor.split(",");
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'splitRgbCode(String rgbColor)' method");
        }
        logManager.logInfo("Entering 'splitRgbCode(String rgbColor)' method");
        return splitRgbColor;
    }

    private int getRColor(String rgbColor) {
        try {
            logManager.logInfo("Entering 'getRColor(String rgbColor)' method");
            rColor = Integer.parseInt(rgbColor.split(",")[0]);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'getRColor(String rgbColor)' method");
        }
        logManager.logInfo("Exiting 'getRColor(String rgbColor)' method");
        return rColor;
    }

    private int getGColor(String rgbColor) {
        logManager.logInfo("Entering 'getGColor(String rgbColor)' method");
        try {
            gColor = Integer.parseInt(rgbColor.split(",")[1]);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'getGColor(String rgbColor)' method");
        }
        logManager.logInfo("Exiting 'getGColor(String rgbColor)' method");
        return gColor;
    }

    private int getBColor(String rgbColor) {
        try {
            logManager.logInfo("Entering 'getBColor(String rgbColor)' method");
            bColor = Integer.parseInt(rgbColor.split(",")[2]);
        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'getBColor(String rgbColor)' method");
        }
        logManager.logInfo("Exiting 'getBColor(String rgbColor)' method");
        return bColor;
    }

    private int getFontStyle(int bold, int italic) {
        logManager.logInfo("Entering 'getFontStyle(int bold, int italic)' method");
        int style = 0;
        try {
            if (bold == 0 && italic == 0) {
                style = Font.PLAIN;
            } else if (bold == 0 && italic == 1) {
                style = Font.ITALIC;
            } else if (bold == 1 && italic == 0) {
                style = Font.BOLD;
            } else if (bold == 1 && italic == 1) {
                style = Font.BOLD | Font.ITALIC;
            }
        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'getFontStyle(int bold, int italic)' method");
        }
        logManager.logInfo("Exiting 'getFontStyle(int bold, int italic)' method");
        return style;
    }
}
