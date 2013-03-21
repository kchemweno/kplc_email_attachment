/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.settings.analysis;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import org.jfree.chart.JFreeChart;
import org.orpik.analysis.CombinedChart;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class ChartSettings {

    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private String sqlQuery = "";
    private ResultSet resultset = null;
    private static LogManager logManager = new LogManager();
    //Get chart settings
    //private static ResultSet chartSettingsResultset = null;
    //End of chart settings   
    public static String chartTitleName = "";
    public static int isShowTitle = 0;
    public static String titleFontName = "SansSerif";
    public static int titleFontSize = 18;
    public static int titleFontBold = 1;
    public static int titleFontItalic = 0;
    public static String titleFontColor = "";
    public static String domainAxisLabelName = "";
    public static int domainAxisFontSize = 0;
    public static int domainAxisFontBold = 0;
    public static int domainAxisFontItalic = 1;
    public static String domainAxisFontColor = "";
    public static String domainAxisPaint = "";
    public static int domainAxisShowTickLabels = 0;
    public static int domainAxisShowTickMarks = 0;
    public static String domainAxisTickLabelFontName = "";
    public static int domainAxisTickLabelFontSize = 0;
    public static int domainAxisTickLabelFontBold = 0;
    public static int domainAxisTickLabelFontItalic = 1;
    public static String domainAxisTickLabelFontColor = "";
    public static String rangeAxisLabelName = "";
    public static String rangeAxisFontName = "";
    public static int rangeAxisFontSize = 0;
    public static int rangeAxisFontBold = 0;
    public static int rangeAxisFontItalic = 1;
    public static String rangeAxisFontColor = "";
    public static String rangeAxisPaint = "";
    public static int rangeAxisShowTickLabels = 0;
    public static int rangeAxisShowTickMarks = 0;
    public static String rangeAxisTickLabelFontName = "";//New
    public static int rangeAxisTickLabelFontSize = 0;
    public static int rangeAxisTickLabelFontBold = 0;
    public static int rangeAxisTickLabelFontItalic = 1;
    public static String rangeAxisTickLabelFontColor = "";
    public static String rangeAxisLabelFontName = "";
    public static int rangeAxisLabelFontSize = 0;
    public static int rangeAxisLabelFontBold = 0;
    public static int rangeAxisLabelFontItalic = 1;
    public static String rangeAxisLabelFontColor = "";
    public static int appearanceOutlineStroke = 0;
    public static String appearanceOutlinePaint = "";
    public static String appearanceBackroundPaint = "255,255,255";
    public static String appearanceOrientation = "";
    public static int otherDrawAntialiasing = 0;
    public static String otherBackgroundPaint = "255,255,255";
    //Set title of chart

    public void setChartTitle(String title) {
        logManager.logInfo("Entering 'setChartTitle(String title)' method");
        try {
            CombinedChart.chart.setTitle(title);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'setChartTitle(String title)' method");
        }
        logManager.logInfo("Exiting 'setChartTitle(String title)' method");
    }

    //Set show title of chart
    public void showTitle(boolean isShow, javax.swing.JTextField txtChartTitle) {
        logManager.logInfo("Entering 'showTitle(boolean isShow, javax.swing.JTextField txtChartTitle)' method");
        try {
            txtChartTitle.setEnabled(isShow);
            CombinedChart.chart.setTitle(txtChartTitle.getText());
            if (!isShow) {
                CombinedChart.chart.setTitle("");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'showTitle(boolean isShow, javax.swing.JTextField txtChartTitle)' method");
        }
        logManager.logInfo("Exiting 'showTitle(boolean isShow, javax.swing.JTextField txtChartTitle)' method");
    }
    //Set chart title on text field

    public void setChartTitleOnTextField(javax.swing.JTextField txtChartTitle) {
        logManager.logInfo("Entering 'setChartTitleOnTextField(javax.swing.JTextField txtChartTitle)' method");
        String chartTitle = "";
        try {
            chartTitle = CombinedChart.chart.getTitle() != null ? CombinedChart.chart.getTitle().getText().toString() : "";
            txtChartTitle.setText(chartTitle);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'setChartTitleOnTextField(javax.swing.JTextField txtChartTitle)' method");
        }
        logManager.logInfo("Exiting 'setChartTitleOnTextField(javax.swing.JTextField txtChartTitle)' method");
    }

    //Load font names
    public void loadFontNames(javax.swing.JList lstFontNames) {
        logManager.logInfo("Entering 'loadFontNames(javax.swing.JList lstFontNames)' method");
        try {
            Font[] fonts = graphicsEnvironment.getAllFonts();
            for (Font font : fonts) {
                font.getFontName();
                //   lstFontNames.add(font.getFontName())
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadFontNames(javax.swing.JList lstFontNames)' method");
        }
        logManager.logInfo("Exiting 'loadFontNames(javax.swing.JList lstFontNames)' method");
    }

    //Save chart settings changes
    public int saveChartSettingsChanges(int userId) {
        logManager.logInfo("Entering 'saveChartSettingsChanges(int userId)' method");
        int result = 0;
        try {
            sqlQuery = queryBuilder.sqlSelectChartSettings(userId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveChartSettingsChanges(int userId)' method");
        }
        logManager.logInfo("Exiting 'saveChartSettingsChanges(int userId)' method");
        return result;
    }

    //create default chart settings
    public int saveDefaultChartSettings(int userId) {
        logManager.logInfo("Entering 'saveDefaultChartSettings(int userId)' method");
        int result = 0;
        try {
            sqlQuery = queryBuilder.sqlInsertDefaultChartSettings(userId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveDefaultChartSettings(int userId)' method");
        }
        logManager.logInfo("Exiting 'saveDefaultChartSettings(int userId)' method");
        return result;
    }
    //Get all chart settings in a resultset

    public ResultSet getChartSettings(int userId) {
        logManager.logInfo("Entering 'getChartSettings(int userId)' method");
        try {
            resultset = executeQuery.executeSelect(queryBuilder.sqlSelectChartSettings(userId));
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getChartSettings(int userId)' method");
        }
        logManager.logInfo("Exiting 'getChartSettings(int userId)' method");
        return resultset;
    }

    //Set all chart settings 
    public void setChartSettings(int userId) {
        logManager.logInfo("Entering 'setChartSettings(int userId)' method");
        try {
            //ChartSettings.chartSettingsResultset = getChartSettings(userId);
            resultset = getChartSettings(userId);
            //Move cursor to first row
            if (resultset.next()) {
                //Set chart settings of all parameters, these settings can be override later 
                chartTitleName = resultset.getString("title_name");//Don't check during compare

                isShowTitle = resultset.getInt("show_title"); //Check during compare
                titleFontName = resultset.getString("title_font_name"); //Check during compare
                titleFontSize = resultset.getInt("title_font_size");//Check during compare
                titleFontBold = resultset.getInt("title_font_bold");//Check during compare
                titleFontItalic = resultset.getInt("title_font_italic");//Check during compare
                titleFontColor = resultset.getString("title_font_color");//Check during compare **

                domainAxisLabelName = resultset.getString("domain_axis_label_name");//Check during compare
                domainAxisFontSize = resultset.getInt("domain_axis_font_size");//Check during compare
                domainAxisFontBold = resultset.getInt("domain_axis_font_bold");//Check during compare
                domainAxisFontItalic = resultset.getInt("domain_axis_font_italic");//Check during compare
                domainAxisFontColor = resultset.getString("domain_axis_font_color");//Check during compare
                domainAxisPaint = resultset.getString("domain_axis_paint");//Check during compare
                domainAxisShowTickLabels = resultset.getInt("domain_axis_show_tick_labels");//Check during compare
                domainAxisShowTickMarks = resultset.getInt("domain_axis_show_tick_marks");//Check during compare
                domainAxisTickLabelFontName = resultset.getString("domain_axis_tick_label_font_name");//Check during compare
                domainAxisTickLabelFontSize = resultset.getInt("domain_axis_tick_label_font_size");//Check during compare
                domainAxisTickLabelFontBold = resultset.getInt("domain_axis_tick_label_font_bold");//Check during compare
                domainAxisTickLabelFontItalic = resultset.getInt("domain_axis_tick_label_font_italic");//Check during compare
                domainAxisTickLabelFontColor = resultset.getString("domain_axis_tick_label_font_color");//Check during compare

                rangeAxisLabelName = resultset.getString("range_axis_label_name");//Check during compare
                rangeAxisFontName = resultset.getString("range_axis_font_name");//Check during compare
                rangeAxisFontSize = resultset.getInt("range_axis_font_size");//Check during compare
                rangeAxisFontBold = resultset.getInt("range_axis_font_bold");//Check during compare
                rangeAxisFontItalic = resultset.getInt("range_axis_font_italic");//Check during compare
                rangeAxisFontColor = resultset.getString("range_axis_font_color");//Check during compare
                rangeAxisPaint = resultset.getString("range_axis_paint");//Check during compare

                rangeAxisShowTickLabels = resultset.getInt("range_axis_show_tick_labels");//Check during compare
                rangeAxisShowTickMarks = resultset.getInt("range_axis_show_tick_marks");//Check during compare
                rangeAxisTickLabelFontName = resultset.getString("range_axis_tick_label_font_name");//Check during compare
                rangeAxisTickLabelFontSize = resultset.getInt("range_axis_tick_label_font_size");//Check during compare
                rangeAxisTickLabelFontBold = resultset.getInt("range_axis_tick_label_font_bold");//Check during compare
                rangeAxisTickLabelFontItalic = resultset.getInt("range_axis_tick_label_font_italic");//Check during compare
                rangeAxisTickLabelFontColor = resultset.getString("range_axis_tick_label_font_color");//Check during compare

                rangeAxisLabelFontName = resultset.getString("range_axis_label_font_name");//Check during compare
                rangeAxisLabelFontSize = resultset.getInt("range_axis_label_font_size");//Check during compare
                rangeAxisLabelFontBold = resultset.getInt("range_axis_label_font_bold");//Check during compare
                rangeAxisLabelFontItalic = resultset.getInt("range_axis_label_font_italic");//Check during compare
                rangeAxisLabelFontColor = resultset.getString("range_axis_label_font_color");//Check during compare

                appearanceOutlineStroke = resultset.getInt("appearance_outline_stroke");//Check during compare
                appearanceOutlinePaint = resultset.getString("appearance_outline_paint");//Check during compare
                appearanceBackroundPaint = resultset.getString("appearance_background_paint");//Check during compare
                appearanceOrientation = resultset.getString("appearance_orientation");//Check during compare
                otherDrawAntialiasing = resultset.getInt("other_draw_antialiasing");//Check during compare
                otherBackgroundPaint = resultset.getString("other_background_paint");//Check during compare
            }
        } catch (Exception exception) {
           //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'setChartSettings(int userId)' method");
        }
        logManager.logInfo("Exiting 'setChartSettings(int userId)' method");
    }

    //Compare chart to copy created before any edits were done to its properties
    public int comparePreviousToCurrentSettings(JFreeChart chart, JFreeChart copy) {
        logManager.logInfo("Entering 'comparePreviousToCurrentSettings(JFreeChart chart, JFreeChart copy)' method");
        int response = -12;
        try {
            //if(!CombinedChart.chart.equals(CombinedChart.copyChart)){
            chart.setAntiAlias(false);
            copy.setAntiAlias(true);
            if (!chart.equals(copy)) {
                response = JOptionPane.showConfirmDialog(null, "Save chart settings changes?", "Chart Settings Changed",
                        JOptionPane.YES_NO_OPTION);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'comparePreviousToCurrentSettings(JFreeChart chart, JFreeChart copy)' method");
        }
        logManager.logInfo("Exiting 'comparePreviousToCurrentSettings(JFreeChart chart, JFreeChart copy)' method");
        return response;
    }

    public boolean comparePreviousToCurrentSettings() {
        logManager.logInfo("Entering 'comparePreviousToCurrentSettings()' method");
        boolean response = false;
        int result = 10;
        try {
            if (isShowTitleChanged()) {
                response = true;
                //return true;
            } else if (isTitleFontnameChanged()) {
                response = true;
            } else if (isTitleFontsizeChanged()) {
                response = true;
            } else if (isTitleFontboldChanged()) {
                response = true;
            } else if (isTitleFontitalicChanged()) {
                response = true;
            }

            if (response) {
                result = JOptionPane.showConfirmDialog(null, "Save chart settings changes?", "Chart Settings Changed",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    //Save settings changes here
                    JOptionPane.showMessageDialog(null, "Changes will be saved", "Chart Settings",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }/* else {
                JOptionPane.showMessageDialog(null, "No changes done to chart", "Chart Settings",
                        JOptionPane.INFORMATION_MESSAGE);
            }*/
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'comparePreviousToCurrentSettings()' method");
        }
        logManager.logInfo("Exiting 'comparePreviousToCurrentSettings()' method");
        return response;
    }
 
    //Get show title changes
    private boolean isShowTitleChanged() {
        logManager.logInfo("Entering 'isShowTitleChanged()' method");
        boolean isTitleVisible = false;
        boolean isTitleVisibilityChanged = false;
        try {
            if (isShowTitle == 0) {
                isTitleVisible = false;
            } else {
                isTitleVisible = true;
            }
            if (CombinedChart.chart.getTitle() != null) {
                if (CombinedChart.chart.getTitle().isVisible() == isTitleVisible) {
                    isTitleVisibilityChanged = false;
                } else {
                    isTitleVisibilityChanged = true;
                }
            } else {
                if (isTitleVisible) {
                    isTitleVisibilityChanged = true;
                } else {
                    isTitleVisibilityChanged = false;
                }
            }
        } catch (Exception exception) {
            logManager.logInfo("Exception was thrown and caught in 'isShowTitleChanged()' method");
        }
        logManager.logInfo("Exiting 'isShowTitleChanged()' method");
        return isTitleVisibilityChanged;
    }

    //Get title font name changes
    private boolean isTitleFontnameChanged() {
        logManager.logInfo("Entering 'isTitleFontnameChanged()' method");
        boolean isChanged = false;
        try {
            if (titleFontName.equalsIgnoreCase(CombinedChart.chart.getTitle().getFont().getFamily())) {
                isChanged = false;
            } else {
                isChanged = true;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'isTitleFontnameChanged()' method");
        }
        logManager.logInfo("Exiting 'isTitleFontnameChanged()' method");
        return isChanged;
    }

    //Get show title font size changes
    private boolean isTitleFontsizeChanged() {
        logManager.logInfo("Entering 'isTitleFontsizeChanged()' method");
        boolean isChanged = false;
        try {
            if (titleFontSize == CombinedChart.chart.getTitle().getFont().getSize()) {
                isChanged = false;
            } else {
                isChanged = true;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'isTitleFontsizeChanged()' method");
        }
        logManager.logInfo("Exiting 'isTitleFontsizeChanged()' method");
        return isChanged;
    }

    //Get show title font bold changes
    private boolean isTitleFontboldChanged() {
        logManager.logInfo("Entering 'isTitleFontboldChanged()' method");
        boolean isChanged = false;
        boolean isFontBold = false;
        try {
            if (titleFontBold != 0) {
                isFontBold = true;
            }

            if (isFontBold == CombinedChart.chart.getTitle().getFont().isBold()) {
                isChanged = false;
            } else {
                isChanged = true;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'isTitleFontboldChanged()' method");
        }
        logManager.logInfo("Exiting 'isTitleFontboldChanged()' method");
        return isChanged;
    }

    //Get show title font italic changes
    private boolean isTitleFontitalicChanged() {
        logManager.logInfo("Entering 'isTitleFontitalicChanged()' method");
        boolean isChanged = false;
        boolean isFontItalic = false;
        try {
            if (titleFontItalic != 0) {
                isFontItalic = true;
            }

            if (isFontItalic == CombinedChart.chart.getTitle().getFont().isItalic()) {
                isChanged = false;
            } else {
                isChanged = true;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'isTitleFontitalicChanged()' method");
        }
        logManager.logInfo("Exiting 'isTitleFontitalicChanged()' method");
        return isChanged;
    }
    
    //Get show title font color changes
   /* private boolean isTitleFontcolorChanged() {
        boolean isChanged = false;
        boolean isFontItalic = false;
        try {
            if (titleFontColor.equals(CombinedChart.chart.getTitle().getFont().) ){
            }
            
            {
                isFontItalic = true;
            }
            if (isFontItalic == CombinedChart.chart.getTitle().getFont().isItalic()) {
                isChanged = false;
            } else {
                isChanged = true;
            }
        } catch (Exception exception) {
        }
        return isChanged;
    }*/

/*
                domainAxisLabelName = resultset.getString("domain_axis_label_name");//Check during compare
                domainAxisFontSize = resultset.getInt("domain_axis_font_size");//Check during compare
                domainAxisFontBold = resultset.getInt("domain_axis_font_bold");//Check during compare
                domainAxisFontItalic = resultset.getInt("domain_axis_font_italic");//Check during compare
                domainAxisFontColor = resultset.getString("domain_axis_font_color");//Check during compare
                domainAxisPaint = resultset.getString("domain_axis_paint");//Check during compare
                domainAxisShowTickLabels = resultset.getInt("domain_axis_show_tick_labels");//Check during compare
                domainAxisShowTickMarks = resultset.getInt("domain_axis_show_tick_marks");//Check during compare
                domainAxisTickLabelFontName = resultset.getString("domain_axis_tick_label_font_name");//Check during compare
                domainAxisTickLabelFontSize = resultset.getInt("domain_axis_tick_label_font_size");//Check during compare
                domainAxisTickLabelFontBold = resultset.getInt("domain_axis_tick_label_font_bold");//Check during compare
                domainAxisTickLabelFontItalic = resultset.getInt("domain_axis_tick_label_font_italic");//Check during compare
                domainAxisTickLabelFontColor = resultset.getString("domain_axis_tick_label_font_color");//Check during compare

                rangeAxisLabelName = resultset.getString("range_axis_label_name");//Check during compare
                rangeAxisFontName = resultset.getString("range_axis_font_name");//Check during compare
                rangeAxisFontSize = resultset.getInt("range_axis_font_size");//Check during compare
                rangeAxisFontBold = resultset.getInt("range_axis_font_bold");//Check during compare
                rangeAxisFontItalic = resultset.getInt("range_axis_font_italic");//Check during compare
                rangeAxisFontColor = resultset.getString("range_axis_font_color");//Check during compare
                rangeAxisPaint = resultset.getString("range_axis_paint");//Check during compare

                rangeAxisShowTickLabels = resultset.getInt("range_axis_show_tick_labels");//Check during compare
                rangeAxisShowTickMarks = resultset.getInt("range_axis_show_tick_marks");//Check during compare
                rangeAxisTickLabelFontName = resultset.getString("range_axis_tick_label_font_name");//Check during compare
                rangeAxisTickLabelFontSize = resultset.getInt("range_axis_tick_label_font_size");//Check during compare
                rangeAxisTickLabelFontBold = resultset.getInt("range_axis_tick_label_font_bold");//Check during compare
                rangeAxisTickLabelFontItalic = resultset.getInt("range_axis_tick_label_font_italic");//Check during compare
                rangeAxisTickLabelFontColor = resultset.getString("range_axis_tick_label_font_color");//Check during compare

                rangeAxisLabelFontName = resultset.getString("range_axis_label_font_name");//Check during compare
                rangeAxisLabelFontSize = resultset.getInt("range_axis_label_font_size");//Check during compare
                rangeAxisLabelFontBold = resultset.getInt("range_axis_label_font_bold");//Check during compare
                rangeAxisLabelFontItalic = resultset.getInt("range_axis_label_font_italic");//Check during compare
                rangeAxisLabelFontColor = resultset.getString("range_axis_label_font_color");//Check during compare

                appearanceOutlineStroke = resultset.getInt("appearance_outline_stroke");//Check during compare
                appearanceOutlinePaint = resultset.getString("appearance_outline_paint");//Check during compare
                appearanceBackroundPaint = resultset.getString("appearance_background_paint");//Check during compare
                appearanceOrientation = resultset.getString("appearance_orientation");//Check during compare
                otherDrawAntialiasing = resultset.getInt("other_draw_antialiasing");//Check during compare
                otherBackgroundPaint = resultset.getString("other_background_paint");//Check during compare 
 */
        
    //Get domain axis label name changes
    private boolean isDomainAxisLableNameChanged() {
        logManager.logInfo("Entering 'isDomainAxisLableNameChanged()' method");
        boolean isChanged = false;
        boolean isFontItalic = false;
        try {
            if(CombinedChart.chart.getXYPlot().getDomainAxis().getLabel() != null){
            if (domainAxisLabelName.equalsIgnoreCase(CombinedChart.chart.getXYPlot().getDomainAxis().getLabel())){                
                isChanged = false;
            } else {
                isChanged = true;
            }
             }else{
                if(domainAxisLabelName.equals("")){
                   isChanged = true; 
                }else{
                    isChanged = false;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'isDomainAxisLableNameChanged()' method");
        }
        logManager.logInfo("Exiting 'isDomainAxisLableNameChanged()' method");
        return isChanged;
    } 
    
    //Get domain axis label name changes
    /*private boolean isDomainAxisFontsizeChanged() {
        boolean isChanged = false;
        boolean isFontItalic = false;
        try {
            if (domainAxisFontSize == CombinedChart.chart.getXYPlot().getDomainAxis().){         
                CombinedChart.chart.getXYPlot().getDomainAxis().
                isChanged = false;
            } else {
                isChanged = true;
            }
        } catch (Exception exception) {
        }
        return isChanged;
    } */   
}
