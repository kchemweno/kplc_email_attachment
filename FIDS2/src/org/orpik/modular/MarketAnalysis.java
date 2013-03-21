/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import java.awt.Component;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.orpik.analysis.CombinedChart;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.indicators.Indicators;
import org.orpik.location.Market;
import org.orpik.location.MarketZone;
import org.orpik.logging.LogManager;
import org.orpik.period.Month;
import org.orpik.settings.analysis.AnalysisSettings;
import org.orpik.settings.analysis.ChartSettings;
import org.orpik.ui.Common;

/**
 *
 * @author chemweno
 */
public class MarketAnalysis {

    private Market market = new Market();
    private MarketZone marketZone = new MarketZone();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private Indicators indicator = new Indicators();
    private CombinedChart combinedChart = new CombinedChart();
    private ChartSettings chartSettings = new ChartSettings();
    public static ArrayList<Integer> marketUpdateCategoryRowsList = new ArrayList<>();
    private Common common = new Common();
    private AnalysisSettings analysisSettings = new AnalysisSettings();
    private Month month = new Month();
    private String marketIds = "";
    private String sqlSelect = "";
    private ResultSet resultset = null;
    private ArrayList<Integer> seriesYears;
    private ArrayList<Integer> fiveYearsAverageYears;
    private String marketUpdateIndicatorIds = "";
    private static LogManager logManager = new LogManager();

    //In passing parameters, system {markets,slims} is ignored since it only influences market/node and commodity. 
    //Also, {market zone, field analyst, region} are ignored since they only affect market/node which are passed as parameters
    public void monthlyAnalysis(boolean showAverageOnGraph, int startYear, int endYear, javax.swing.JTable table,
            javax.swing.table.DefaultTableModel tbmModel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,
            javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo) {
        logManager.logInfo("Entering 'monthlyAnalysis(boolean showAverageOnGraph, int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo)' method");
        try {
            //Populate month averaged values
            if (cboCommodityTwo != null) {
                //For tot analysis
                populateMonthlyAnalysisTotTable(startYear, endYear, table, tbmModel, pnlSeriesYears, pnlAverageYears,
                        pnlMarkets, cboCommodityOne, cboCommodityTwo);

            } else {
                //For market analysis
                populateMonthlyAnalysisTable(startYear, endYear, table, tbmModel, pnlSeriesYears, pnlAverageYears,
                        pnlMarkets, cboCommodityOne);
            }

            //Update current value row variable
            Common.currentValueRow = table.getRowCount() - 1;
            //Add blank row to separate month averaged values from statistical values
            common.addRowToTable(tbmModel);
            /**
             * Add statistical values to table
             */
            //Add monthly average values on table
            calculateMonthlyAverage(pnlAverageYears, table, tbmModel, "Monthly Average");
            //Add percentage preeding month values on table
            calculatePercentagePreceedingMonth(table, tbmModel, "% Preceeding Month");
            //Add percentage same month previous year
            calculatePercentagePreviousYear(table, tbmModel, "% of Same Month Previous Year");
            //Add percentage of average
            calculatePercentageOfAverage(table, tbmModel, "% of Average");
            //Add 12 month maximum on table
            getTwelveMonthMaximum(table, tbmModel, "12 Month MAX");
            //Add 12 month minimum on table
            getTwelveMonthMinimum(table, tbmModel, "12 Month MIN");
            //Add 6 month maximum on table
            getSixMonthMaximum(table, tbmModel, "6 Month MAX");
            //Add 6 month minimum on table
            getSixMonthMinimum(table, tbmModel, "6 Month MIN");
            //Add value as percentage of previous 6 months 
            calculatePercentageOfPreviousSixMonths(table, tbmModel, "% of Previous 6 Months");
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'monthlyAnalysis(boolean showAverageOnGraph, int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo)' method");
        }
        logManager.logInfo("Exiting 'monthlyAnalysis(boolean showAverageOnGraph, int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo)' method");
    }

    //Create a string of market ids
    public String getMarketIds(ArrayList<String> marketNames) {
        logManager.logInfo("Entering 'getMarketIds(ArrayList<String> marketNames)' method");
        int arrayIndex = 0;
        int marketId = 0;
        String marketName = "";
        try {
            marketIds = "";
            while (arrayIndex < marketNames.size()) {
                //Get market name from array list
                marketName = marketNames.get(arrayIndex);
                //Get market id 
                marketId = market.getMarketId(marketName);
                marketIds = marketIds + "," + marketId;
                arrayIndex++;
            }
            //Replace first comma
            marketIds = marketIds.replaceFirst(",", "");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getMarketIds(ArrayList<String> marketNames)' method");
        }
        logManager.logInfo("Exiting 'getMarketIds(ArrayList<String> marketNames)' method");
        return marketIds;
    }

    private void populateMonthlyAnalysisTable(int startYear, int endYear, javax.swing.JTable table,
            javax.swing.table.DefaultTableModel tbmModel,
            javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, javax.swing.JPanel pnlMarkets,
            javax.swing.JComboBox cboCommodity) {
        logManager.logInfo("Entering 'populateMonthlyAnalysisTable(int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel,javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, "
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodity)' method");

        int previousYear = 0;
        int yearName = 0;
        int monthValue = 0;
        int monthId = 0;
        int rowIndex = -1;
        int indicatorId = 0;
        String marketIds = "";
        String indicatorName = "";
        try {
            indicatorName = cboCommodity.getSelectedItem().toString();
            indicatorId = indicator.getIndicatorId(indicatorName);
            marketIds = getMarketIds(common.getPanelCheckBoxItems(pnlMarkets, true));
            resultset = calculateMonthlyAnalysis(startYear, endYear, indicatorId, marketIds);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    monthValue = resultset.getInt("month_value");
                    yearName = resultset.getInt("year_name");
                    monthId = resultset.getInt("month_id");
                    if (yearName != previousYear) {
                        tbmModel.addRow(new Object[]{null});
                        //Track rowIndex
                        rowIndex++;
                        //Populate year column (column 0) on created row
                        table.setValueAt(yearName, rowIndex, 0);
                        //Update previous year variable
                        previousYear = yearName;
                    }
                    //Populate month columns
                    table.setValueAt(monthValue, rowIndex, monthId);
                    resultset.next();;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'populateMonthlyAnalysisTable(int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel,javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, "
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodity)' method");
        }
        logManager.logInfo("Exiting 'populateMonthlyAnalysisTable(int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel,javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, "
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodity)' method");
    }

    private void populateMonthlyAnalysisTotTable(int startYear, int endYear, javax.swing.JTable table,
            javax.swing.table.DefaultTableModel tbmModel,
            javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, javax.swing.JPanel pnlMarkets,
            javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo) {
        logManager.logInfo("Entering 'populateMonthlyAnalysisTotTable(int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel,javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, "
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo)' method");

        int previousYear = 0;
        int yearName = 0;
        int indicatorOneValue = 0;
        int indicatorTwoValue = 0;
        int monthId = 0;
        int rowIndex = -1;
        int indicatorOneId = 0;
        int indicatorTwoId = 0;
        double price = 0;
        int totValue = 0;
        String marketIds = "";
        String indicatorOneName = "";
        String indicatorTwoName = "";
        try {
            indicatorOneName = cboCommodityOne.getSelectedItem().toString();
            indicatorOneId = indicator.getIndicatorId(indicatorOneName);

            indicatorTwoName = cboCommodityTwo.getSelectedItem().toString();
            indicatorTwoId = indicator.getIndicatorId(indicatorTwoName);

            marketIds = getMarketIds(common.getPanelCheckBoxItems(pnlMarkets, true));           
                      
            resultset = calculateMonthlyAnalysisTot(startYear, endYear, indicatorOneId, indicatorTwoId, marketIds);

            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    indicatorOneValue = resultset.getInt("commodity_one_value");
                    indicatorTwoValue = resultset.getInt("commodity_two_value");
                    yearName = resultset.getInt("yr");
                    monthId = resultset.getInt("mi");
                    if (yearName != previousYear) {
                        tbmModel.addRow(new Object[]{null});
                        //Track rowIndex
                        rowIndex++;
                        //Populate year column (column 0) on created row
                        table.setValueAt(yearName, rowIndex, 0);
                        //Update previous year variable
                        previousYear = yearName;
                    }
                    //Calculate tot value
                   /* if (indicatorOneValue != 0 && indicatorTwoValue != 0) {
                        totValue = indicatorOneValue / indicatorTwoValue;
                        //Populate month columns
                        if (totValue > 0) {
                            table.setValueAt((int) Double.parseDouble(Integer.toString(totValue)), rowIndex, monthId);
                        }
                    }*/
                    
                        if (indicatorOneValue != 0 && indicatorTwoValue != 0) {
                        price = Double.parseDouble(Integer.toString(indicatorOneValue)) / Double.parseDouble(Integer.toString(indicatorTwoValue));              
                        
                        //Populate month columns
                        if (price > 0) {
                            //table.setValueAt((int) price, rowIndex, monthId);
                            table.setValueAt(Math.round(price), rowIndex, monthId);
                        }
                    }
                    resultset.next();;
                }
            }
        } catch (Exception exception) {
           // exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'populateMonthlyAnalysisTotTable(int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel,javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, "
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo)' method");
        }
        logManager.logInfo("Exiting 'populateMonthlyAnalysisTotTable(int startYear, int endYear, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel,javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears, "
                + " javax.swing.JPanel pnlMarkets, javax.swing.JComboBox cboCommodityOne, javax.swing.JComboBox cboCommodityTwo)' method");
    }

    private ResultSet calculateMonthlyAnalysis(int startYear, int endYear, int indicatorId, String marketIds) {
        logManager.logInfo("Entering 'calculateMonthlyAnalysis(int startYear, int endYear, int indicatorId, String marketIds)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketPricesBetweenYears(startYear, endYear, indicatorId, marketIds);
            resultset = executeQuery.executeSelect(sqlSelect);
        } catch (Exception exception) {
            exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'calculateMonthlyAnalysis(int startYear, int endYear, int indicatorId, String marketIds)' method");
        }
        logManager.logInfo("Exiting 'calculateMonthlyAnalysis(int startYear, int endYear, int indicatorId, String marketIds)' method");
        return resultset;
    }

    private ResultSet calculateMonthlyAnalysisTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds) {
        logManager.logInfo("Entering 'calculateMonthlyAnalysisTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        String[] sqlBatch;
        try {
            // Drop and Create temporary tables
            sqlBatch = queryBuilder.queryDropCreateTemporaryTablesTot(startYear, endYear, indicatorOneId, indicatorTwoId, marketIds);
            executeQuery.executeUpdate(sqlBatch);
            sqlSelect = "";
            sqlSelect = queryBuilder.querySelectMarketPricesBetweenYearsTot();                       
            resultset = executeQuery.executeSelect(sqlSelect);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'calculateMonthlyAnalysisTot(int startYear, int endYear, "
                    + " int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        }
        logManager.logInfo("Exiting 'calculateMonthlyAnalysisTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        return resultset;
    }

    private void calculateMonthlyAverage(javax.swing.JPanel pnlSelectedAverageYears, javax.swing.JTable table,
            javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'calculateMonthlyAverage(javax.swing.JPanel pnlSelectedAverageYears, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int average = 0;
        int index = 0;
        int count = 0;
        int row = 0;
        int averageRow = 0;
        ArrayList<Integer> averageRowsList;
        try {
            //Add a row for populating average values
            common.addRowToTable(tbmModel);
            averageRow = table.getRowCount() - 1;
            //Update column 0
            table.setValueAt(rowName, averageRow, 0);
            averageRowsList = common.getRowIndices(pnlSelectedAverageYears, table);
            //Calculate average
            for (int column = 1; column < table.getColumnCount(); column++) {
                //Reset all averages, sums and counts
                average = 0;
                count = 0;
                index = 0;
                while (index < averageRowsList.size()) {
                    row = averageRowsList.get(index);
                    if (table.getValueAt(row, column) != null) {
                        average = Integer.parseInt(table.getValueAt(row, column).toString()) + average;
                        count++;
                    }
                    index++;
                }
                if (average > 0 && count > 0) {
                    average = average / count;
                }
                //Populate table with average value
                table.setValueAt(average, averageRow, column);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'calculateMonthlyAverage(javax.swing.JPanel pnlSelectedAverageYears, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'calculateMonthlyAverage(javax.swing.JPanel pnlSelectedAverageYears, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

    //Calculate current price as percentage of preceeding month
    private void calculatePercentagePreceedingMonth(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'calculatePercentagePreceedingMonth(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int percentagePreceedingValueRow = 0;
        int previousValueRow = 0;
        float currentValue = 0;
        float previousValue = 0;
        int percentagePreceedingValue = 0;
        int previousValueColumn = 0;
        float percentage = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get percentagePreceedingValueRow 
            percentagePreceedingValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, percentagePreceedingValueRow, 0);
            //Traverse table columns
            for (int column = 1; column < table.getColumnCount(); column++) {
                previousValueRow = common.getPeviousMonthRow(currentValueRow, column);
                previousValueColumn = common.getPeviousMonthColumn(column);

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Float.parseFloat(table.getValueAt(currentValueRow, column).toString()) : 0;

                previousValue = table.getValueAt(previousValueRow, previousValueColumn) != null
                        ? Float.parseFloat(table.getValueAt(previousValueRow, previousValueColumn).toString()) : 0;

                //Calculate % age                              
                //Update table with % preceeding month
                if (currentValue != 0 && previousValue != 0) {
                    percentage = currentValue / previousValue * 100;
                    percentagePreceedingValue = Math.round(percentage);
                    table.setValueAt(percentagePreceedingValue, percentagePreceedingValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'calculatePercentagePreceedingMonth(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'calculatePercentagePreceedingMonth(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

    //Calculate current price as percentage of same month previous year
    private void calculatePercentagePreviousYear(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'calculatePercentagePreviousYear(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int percentagePreviousValueRow = 0;
        int previousValueRow = 0;
        float currentValue = 0;
        float previousValue = 0;
        int percentagePreceedingValue = 0;
        int previousValueColumn = 0;
        float percentage = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get percentagePreceedingValueRow 
            percentagePreviousValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, percentagePreviousValueRow, 0);
            //Traverse table columns
            for (int column = 1; column < table.getColumnCount(); column++) {
                previousValueRow = currentValueRow - 1;
                previousValueColumn = column;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Float.parseFloat(table.getValueAt(currentValueRow, column).toString()) : 0;

                previousValue = table.getValueAt(previousValueRow, previousValueColumn) != null
                        ? Float.parseFloat(table.getValueAt(previousValueRow, previousValueColumn).toString()) : 0;

                //Calculate % age                              
                //Update table with % preceeding month
                if (currentValue != 0 && previousValue != 0) {
                    percentage = currentValue / previousValue * 100;
                    percentagePreceedingValue = Math.round(percentage);
                    table.setValueAt(percentagePreceedingValue, percentagePreviousValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logInfo("Exception was thrown and caught in 'calculatePercentagePreviousYear(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'calculatePercentagePreviousYear(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

    //Calculate current price as percentage of same month previous year
    private void calculatePercentageOfAverage(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'calculatePercentageOfAverage(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int percentagePreviousValueRow = 0;
        int previousValueRow = 0;
        float currentValue = 0;
        float previousValue = 0;
        int percentagePreceedingValue = 0;
        int previousValueColumn = 0;
        float percentage = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get percentagePreceedingValueRow , the just created row
            percentagePreviousValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, percentagePreviousValueRow, 0);
            //Traverse table columns
            for (int column = 1; column < table.getColumnCount(); column++) {
                //Previous value row is the average row
                previousValueRow = currentValueRow + 2;
                previousValueColumn = column;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Float.parseFloat(table.getValueAt(currentValueRow, column).toString()) : 0;

                //Previous value is five year average
                previousValue = table.getValueAt(previousValueRow, previousValueColumn) != null
                        ? Float.parseFloat(table.getValueAt(previousValueRow, previousValueColumn).toString()) : 0;

                //Calculate % age                              
                //Update table with % preceeding month
                if (currentValue != 0 && previousValue != 0) {
                    percentage = currentValue / previousValue * 100;
                    percentagePreceedingValue = Math.round(percentage);
                    table.setValueAt(percentagePreceedingValue, percentagePreviousValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'calculatePercentageOfAverage(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'calculatePercentageOfAverage(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

    //Calculate twelve month maximum
    private void getTwelveMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'getTwelveMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int maximumValueRow = 0;
        int currentValue = 0;
        int maximumValue = 0;
        int previousValue = 0;
        int countRow = 0;
        int countColumn = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get percentagePreceedingValueRow , the just created row
            maximumValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, maximumValueRow, 0);
            //Traverse table columns
            //for (int column = 1; column < table.getColumnCount(); column++) {
            for (int column = table.getColumnCount() - 1; column > 0; column--) {
                //Reset values
                countRow = 0;
                countColumn = 0;
                maximumValue = 0;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Integer.parseInt(table.getValueAt(currentValueRow, column).toString()) : 0;

                //maximumValue = currentValue > maximumValue ? currentValue : maximumValue;

                for (int count = 1; count < 12; count++) {

                    countRow = column - count < 1 ? currentValueRow - 1 : currentValueRow;
                    countColumn = column - count < 1 ? column - count + 12 : column - count;

                    previousValue = table.getValueAt(countRow, countColumn) != null
                            ? Integer.parseInt(table.getValueAt(countRow, countColumn).toString()) : 0;

                    maximumValue = maximumValue > previousValue ? maximumValue : previousValue;
                }
                //Update table with maximum value
                if (maximumValue != 0) {
                    table.setValueAt(maximumValue, maximumValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'getTwelveMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'getTwelveMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

//Calculate twelve month minimum
    private void getTwelveMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'getTwelveMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int minimumValueRow = 0;
        int currentValue = 0;
        int minimumValue = 999999999;
        int previousValue;//= 0;
        int countRow = 0;
        int countColumn = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get minimum value row, the just created row
            minimumValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, minimumValueRow, 0);
            //Traverse table columns
            //for (int column = 1; column < table.getColumnCount(); column++) {
            for (int column = table.getColumnCount() - 1; column > 0; column--) {
                //Reset values
                countRow = 0;
                countColumn = 0;
                minimumValue = 999999999;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Integer.parseInt(table.getValueAt(currentValueRow, column).toString()) : 0;

                //minimumValue = currentValue > minimumValue ? currentValue : minimumValue;

                for (int count = 1; count < 12; count++) {

                    countRow = column - count < 1 ? currentValueRow - 1 : currentValueRow;
                    countColumn = column - count < 1 ? column - count + 12 : column - count;

                    previousValue = table.getValueAt(countRow, countColumn) != null
                            ? Integer.parseInt(table.getValueAt(countRow, countColumn).toString()) : 0;

                    minimumValue = minimumValue < previousValue ? minimumValue : previousValue;
                }
                //Update table with minimum value
                if (minimumValue != 0) {
                    table.setValueAt(minimumValue, minimumValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'getTwelveMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'getTwelveMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

    //Calculate twelve month maximum
    private void getSixMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'getSixMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int maximumValueRow = 0;
        int currentValue = 0;
        int maximumValue = 0;
        int previousValue = 0;
        int countRow = 0;
        int countColumn = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get percentagePreceedingValueRow , the just created row
            maximumValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, maximumValueRow, 0);
            //Traverse table columns
            //for (int column = 1; column < table.getColumnCount(); column++) {
            for (int column = table.getColumnCount() - 1; column > 0; column--) {
                //Reset values
                countRow = 0;
                countColumn = 0;
                maximumValue = 0;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Integer.parseInt(table.getValueAt(currentValueRow, column).toString()) : 0;

                //maximumValue = currentValue > maximumValue ? currentValue : maximumValue;

                for (int count = 1; count < 6; count++) {

                    countRow = column - count < 1 ? currentValueRow - 1 : currentValueRow;
                    countColumn = column - count < 1 ? column - count + 12 : column - count;

                    previousValue = table.getValueAt(countRow, countColumn) != null
                            ? Integer.parseInt(table.getValueAt(countRow, countColumn).toString()) : 0;

                    maximumValue = maximumValue > previousValue ? maximumValue : previousValue;
                }
                //Update table with maximum value
                if (maximumValue != 0) {
                    table.setValueAt(maximumValue, maximumValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'getSixMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'getSixMonthMaximum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

//Calculate six month minimum
    private void getSixMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName) {
        logManager.logInfo("Entering 'getSixMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        int currentValueRow = 0;
        int minimumValueRow = 0;
        int currentValue = 0;
        int minimumValue = 999999999;
        int previousValue;//= 0;
        int countRow = 0;
        int countColumn = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get minimum value row, the just created row
            minimumValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, minimumValueRow, 0);
            //Traverse table columns
            //for (int column = 1; column < table.getColumnCount(); column++) {
            for (int column = table.getColumnCount() - 1; column > 0; column--) {
                //Reset values
                countRow = 0;
                countColumn = 0;
                minimumValue = 999999999;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Integer.parseInt(table.getValueAt(currentValueRow, column).toString()) : 0;

                //minimumValue = currentValue > minimumValue ? currentValue : minimumValue;

                for (int count = 1; count < 6; count++) {

                    countRow = column - count < 1 ? currentValueRow - 1 : currentValueRow;
                    countColumn = column - count < 1 ? column - count + 12 : column - count;

                    previousValue = table.getValueAt(countRow, countColumn) != null
                            ? Integer.parseInt(table.getValueAt(countRow, countColumn).toString()) : 0;

                    minimumValue = minimumValue < previousValue ? minimumValue : previousValue;
                }
                //Update table with minimum value
                if (minimumValue != 0) {
                    table.setValueAt(minimumValue, minimumValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'getSixMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
        }
        logManager.logInfo("Exiting 'getSixMonthMinimum(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel, String rowName)' method");
    }

    //Calculate current price as percentage of same month previous year
    private void calculatePercentageOfPreviousSixMonths(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel,
            String rowName) {
        logManager.logInfo("Entering 'calculatePercentageOfPreviousSixMonths(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel,"
                + " String rowName)' method");
        int currentValueRow = 0;
        int percentagePreceedingValueRow = 0;
        int previousValueRow = 0;
        float currentValue = 0;
        float previousValue = 0;
        int percentagePreceedingValue = 0;
        int previousValueColumn = 0;
        float percentage = 0;

        try {
            //Add a row for populating values being computed
            common.addRowToTable(tbmModel);
            //Get percentagePreceedingValueRow 
            percentagePreceedingValueRow = table.getRowCount() - 1;
            currentValueRow = Common.currentValueRow;

            //Update column 0
            table.setValueAt(rowName, percentagePreceedingValueRow, 0);
            //Traverse table columns
            for (int column = 1; column < table.getColumnCount(); column++) {
                //previousValueRow = common.getPeviousMonthRow(currentValueRow, column);     
                //previousValueColumn = common.getPeviousMonthColumn(column);

                previousValueColumn = column - 5 > 0 ? column - 5 : column + 7;
                previousValueRow = column - 5 > 0 ? currentValueRow : currentValueRow - 1;

                currentValue = table.getValueAt(currentValueRow, column) != null
                        ? Float.parseFloat(table.getValueAt(currentValueRow, column).toString()) : 0;

                previousValue = table.getValueAt(previousValueRow, previousValueColumn) != null
                        ? Float.parseFloat(table.getValueAt(previousValueRow, previousValueColumn).toString()) : 0;

                //Calculate % age                              
                //Update table with % preceeding month
                if (currentValue != 0 && previousValue != 0) {
                    percentage = currentValue / previousValue * 100;
                    percentagePreceedingValue = Math.round(percentage);
                    table.setValueAt(percentagePreceedingValue, percentagePreceedingValueRow, column);
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'calculatePercentageOfPreviousSixMonths(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel,"
                + " String rowName)' method");
        }
        logManager.logInfo("Exiting 'calculatePercentageOfPreviousSixMonths(javax.swing.JTable table, javax.swing.table.DefaultTableModel tbmModel,"
                + " String rowName)' method");
    }

    public String getPreviousYearCurrentMonth(String month, int year) {
        logManager.logInfo("Entering 'getPreviousYearCurrentMonth(String month, int year)' method");
        String previousYearCurrentMonth = "";
        try {
            previousYearCurrentMonth = month.substring(0, 3) + "-" + year + "";

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getPreviousYearCurrentMonth(String month, int year)' method");
        }
        logManager.logInfo("Exiting 'getPreviousYearCurrentMonth(String month, int year)' method");
        return previousYearCurrentMonth;
    }

    public String getCurrentYearPreviousMonth(String month, int year) {
        logManager.logInfo("Entering 'getCurrentYearPreviousMonth(String month, int year)' method");
        String currentYearPreviousMonth = "";
        try {
            currentYearPreviousMonth = month.substring(0, 3) + "-" + year + "";

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getCurrentYearPreviousMonth(String month, int year)' method");
        }
        logManager.logInfo("Exiting 'getCurrentYearPreviousMonth(String month, int year)' method");
        return currentYearPreviousMonth;
    }

    private int getTableRowWithItem(int columnToCheck, String itemToCheckFor, javax.swing.JTable table) {
        logManager.logInfo("Entering 'getTableRowWithItem(int columnToCheck, String itemToCheckFor, javax.swing.JTable table)' method");
        int row = 0;
        try {
            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                if (table.getValueAt(row, columnToCheck) != null
                        && table.getValueAt(row, columnToCheck).toString().equalsIgnoreCase(itemToCheckFor)) {
                    row = rowIndex;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getTableRowWithItem(int columnToCheck, String itemToCheckFor, javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'getTableRowWithItem(int columnToCheck, String itemToCheckFor, javax.swing.JTable table)' method");
        return row;
    }

    //Launch chart
    public void showChart(javax.swing.JPanel panel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,
            boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel) {
        logManager.logInfo("Entering 'showChart(javax.swing.JPanel panel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,"
                + " boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
        try {
            //Update selected average years
            Common.averageYears = common.getPanelCheckBoxIntegerItems(pnlAverageYears, true);
            seriesYears = common.getPanelCheckBoxIntegerItems(pnlSeriesYears, true);

            combinedChart.showChart(panel, seriesYears, showAverage, table, startMonth, chartTitle, yAxisLabel);
            //Pick chart properties

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'showChart(javax.swing.JPanel panel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,"
                + " boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
        }
        logManager.logInfo("Exiting 'showChart(javax.swing.JPanel panel, javax.swing.JPanel pnlSeriesYears, javax.swing.JPanel pnlAverageYears,"
                + " boolean showAverage, javax.swing.JTable table, int startMonth, String chartTitle, String yAxisLabel)' method");
    }

    public void equalizeCharts() {
        logManager.logInfo("Entering 'equalizeCharts()' method");
        try {
            CombinedChart.copyChart = CombinedChart.chart;
            combinedChart.copiedChart = CombinedChart.chart;
        } catch (Exception exception) {
            logManager.logError("Exiting 'equalizeCharts()' method");
        }
        logManager.logInfo("Exiting 'equalizeCharts()' method");
    }

    //Change column names
    private void renameMarketUpdateColumns(int fiveYearAverageStart, int fiveYearAverageEnd, String lastSeason, 
            javax.swing.JPanel pnlMarkets,            javax.swing.table.DefaultTableModel tbmMarketUpdate, javax.swing.JTable tblMarketUpdate) {
        try {
        } catch (Exception exception) {
        }
    }

    //Populate market update indicators
    public void populateMarketUpdateIndicators(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,
            String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate,
            javax.swing.JTable tblMarketUpdate) {
        logManager.logInfo("Entering 'populateMarketUpdateIndicators(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,"
                + "  String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate,"
                + "  javax.swing.JTable tblMarketUpdate)' method");
        int marketZoneId = 0;
        int rowIndex = 0;
        String categoryName = "";
        String indicatorName = "";
        try {
            marketZoneId = marketZone.getMarketZoneId(marketZoneName);
            sqlSelect = queryBuilder.getMarketUpdateIndicators(marketZoneId);
            System.out.println(sqlSelect);
            resultset = executeQuery.executeSelect(sqlSelect);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //Add row to table
                    tbmMarketUpdate.addRow(new Object[]{null});
                    //Add indicator or category
                    if (categoryName.equalsIgnoreCase(resultset.getString("category_name"))) {
                        categoryName = resultset.getString("category_name");
                        //marketUpdateCategoryRowsList.add(rowIndex);
                        indicatorName = resultset.getString("indicator");
                        //Build indicatorIds
                        marketUpdateIndicatorIds = marketUpdateIndicatorIds + "," + resultset.getInt("indicator_id");
                        //Add indicator
                        tblMarketUpdate.setValueAt(indicatorName, rowIndex, 0);
                        rowIndex++;
                    } else {
                        //Add row to table
                        tbmMarketUpdate.addRow(new Object[]{null});
                        categoryName = resultset.getString("category_name");
                        //Add category 
                        //tblMarketUpdate.setValueAt(categoryName, rowIndex, 0);
                        tblMarketUpdate.setValueAt(categoryName.toUpperCase(), rowIndex, 0);
                        marketUpdateCategoryRowsList.add(rowIndex);
                        rowIndex++;
                        indicatorName = resultset.getString("indicator");
                        //Build indicatorIds
                        marketUpdateIndicatorIds = marketUpdateIndicatorIds + "," + resultset.getInt("indicator_id");
                        //Add indicator
                        tblMarketUpdate.setValueAt(indicatorName, rowIndex, 0);
                        rowIndex++;
                    }
                    resultset.next();
                }
                //Remove first comma
                marketUpdateIndicatorIds = marketUpdateIndicatorIds.replaceFirst(",", "");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in populateMarketUpdateIndicators(int year, int month, int fiveYearStart,"
                    + " int fiveYearEnd, String lastSeason,String marketZoneName, javax.swing.JPanel pnlMarkets,"
                    + " javax.swing.table.DefaultTableModel tbmMarketUpdate, javax.swing.JTable tblMarketUpdate)' method");
        }
        logManager.logInfo("Exiting 'populateMarketUpdateIndicators(int year, int month, int fiveYearStart, int fiveYearEnd, "
                + "String lastSeason,String marketZoneName, javax.swing.JPanel pnlMarkets, "
                + " javax.swing.table.DefaultTableModel tbmMarketUpdate,"
                + "  javax.swing.JTable tblMarketUpdate)' method");
    }

    private String getFiveYearsAverage(int fiveYearStart, int fiveYearEnd) {
        logManager.logInfo("Entering 'getFiveYearsAverage(int fiveYearStart, int fiveYearEnd)' method");
        String fiveYearsAverage = "";
        try {
            for (int index = fiveYearStart; index <= fiveYearEnd; index++) {
                fiveYearsAverage = fiveYearsAverage + "," + index;
            }
            fiveYearsAverage = fiveYearsAverage.replaceFirst(",", "");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getFiveYearsAverage(int fiveYearStart, int fiveYearEnd)' method");
        }
        logManager.logInfo("Exiting 'getFiveYearsAverage(int fiveYearStart, int fiveYearEnd)' method");
        return fiveYearsAverage;
    }

    private void populateMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd,
            javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate) {
        logManager.logInfo("Entering 'populateMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate)' method");
        
        String fiveYearsAverage = "";
        //int monthValue = 0;
        String monthValue = "";
        int yearOnResultset = 0;
        int previousYear = year - 1;
        int monthOnResultset = 0;
        int previousMonth = this.month.getPreviousMonth(month);
        String indicatorName = "";
        String indicatorNameOnResultset = "";
        ArrayList<Integer> fiveYearAverageList;

        try {
            fiveYearsAverage = getFiveYearsAverage(fiveYearStart, fiveYearEnd);
            marketIds = getMarketIds(common.getPanelCheckBoxItems(pnlMarkets, true));
            sqlSelect = queryBuilder.querySelectMarketUpdateValues(year, month, marketIds, fiveYearsAverage, marketUpdateIndicatorIds);
            resultset = executeQuery.executeSelect(sqlSelect);
            fiveYearAverageList = marketUpdateFiveYearAverageList(fiveYearStart, fiveYearEnd);

            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //monthValue = resultset.getInt("month_value");
                    monthValue = resultset.getString("month_value");
                    yearOnResultset = resultset.getInt("year_name");
                    monthOnResultset = resultset.getInt("month_id");
                    indicatorNameOnResultset = resultset.getString("indicator");
                    for (int row = 0; row < tblMarketUpdate.getRowCount(); row++) {
                        indicatorName = tblMarketUpdate.getValueAt(row, 0) != null ? tblMarketUpdate.getValueAt(row, 0).toString() : "";
                        //Match indicator on table column 0 and on resultset
                        if (indicatorName.equalsIgnoreCase(indicatorNameOnResultset)) {
                            //Load value for current month previous year on table
                            if (monthOnResultset == month && yearOnResultset == previousYear) {
                                tblMarketUpdate.setValueAt(monthValue, row, 2);
                            }
                            //Load value for previous month current year on table
                            if(previousMonth != 12){
                            if (monthOnResultset == previousMonth && yearOnResultset == year) {
                                tblMarketUpdate.setValueAt(monthValue, row, 4);
                            }
                            //If previous month is in the previous year
                            }else{
                            if (monthOnResultset == previousMonth && yearOnResultset == previousYear) {
                                tblMarketUpdate.setValueAt(monthValue, row, 4);
                            }                                
                            }
                            //Load value for  current month current year on table
                            if (monthOnResultset == month && yearOnResultset == year) {
                                tblMarketUpdate.setValueAt(monthValue, row, 5);
                            }
                            /**
                             * Load five year average values on table
                             */
                            //Load value for first five year average year on table
                            if (monthOnResultset == month && yearOnResultset == fiveYearAverageList.get(0)) {
                                tblMarketUpdate.setValueAt(monthValue, row, 11);
                            }
                            //Load value for second five year average year on table
                            if (monthOnResultset == month && yearOnResultset == fiveYearAverageList.get(1)) {
                                tblMarketUpdate.setValueAt(monthValue, row, 12);
                            }
                            //Load value for third five year average year on table
                            if (monthOnResultset == month && yearOnResultset == fiveYearAverageList.get(2)) {
                                tblMarketUpdate.setValueAt(monthValue, row, 13);
                            }
                            //Load value for fourth five year average year on table
                            if (monthOnResultset == month && yearOnResultset == fiveYearAverageList.get(3)) {
                                tblMarketUpdate.setValueAt(monthValue, row, 14);
                            }
                            //Load value for fifth five year average year on table
                            if (monthOnResultset == month && yearOnResultset == fiveYearAverageList.get(4)) {
                                tblMarketUpdate.setValueAt(monthValue, row, 15);
                            }
                        }
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'populateMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate)' method");
        }
        logManager.logInfo("Exiting 'populateMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate)' method");
    }
    //Populate five year average values on market update table

    private void populateMarketUpdateFiveYearAverageValues(int year, int month, int fiveYearStart, int fiveYearEnd,
            javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate) {
        logManager.logInfo("Entering 'populateMarketUpdateFiveYearAverageValues(int year, int month, int fiveYearStart, int fiveYearEnd,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate)' method");
        
        String fiveYearsAverage = "";
        //int monthValue = 0;
        String fiveYearAverage = "";
        String indicatorName = "";
        String indicatorNameOnResultset = "";
        ArrayList<Integer> fiveYearAverageList;

        try {
            fiveYearsAverage = getFiveYearsAverage(fiveYearStart, fiveYearEnd);
            marketIds = getMarketIds(common.getPanelCheckBoxItems(pnlMarkets, true));
            sqlSelect = queryBuilder.querySelectMarketUpdateFiveYearAverageValues(year, month, marketIds,
                    fiveYearsAverage, marketUpdateIndicatorIds);
            resultset = executeQuery.executeSelect(sqlSelect);
            fiveYearAverageList = marketUpdateFiveYearAverageList(fiveYearStart, fiveYearEnd);

            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //monthValue = resultset.getInt("month_value");
                    fiveYearAverage = resultset.getString("five_year_average");
                    indicatorNameOnResultset = resultset.getString("indicator");
                    for (int row = 0; row < tblMarketUpdate.getRowCount(); row++) {
                        indicatorName = tblMarketUpdate.getValueAt(row, 0) != null ? tblMarketUpdate.getValueAt(row, 0).toString() : "";
                        //Match indicator on table column 0 and on resultset
                        if (indicatorName.equalsIgnoreCase(indicatorNameOnResultset)) {
                            //Load value for current month previous year on table                            
                            tblMarketUpdate.setValueAt(fiveYearAverage, row, 1);
                        }
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'populateMarketUpdateFiveYearAverageValues(int year, int month, int fiveYearStart, int fiveYearEnd,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate)' method");
        }
        logManager.logInfo("Exiting 'populateMarketUpdateFiveYearAverageValues(int year, int month, int fiveYearStart, int fiveYearEnd,"
                + " javax.swing.JPanel pnlMarkets, javax.swing.JTable tblMarketUpdate)' method");
    }

    //Get a list of years selected for five years average calculation in market update
    private ArrayList<Integer> marketUpdateFiveYearAverageList(int fiveYearAverageStart, int fiveYearAverageEnd) {
        logManager.logInfo("Entering 'marketUpdateFiveYearAverageList(int fiveYearAverageStart, int fiveYearAverageEnd)' method");
        fiveYearsAverageYears = new ArrayList<>();
        try {
            for (int index = fiveYearAverageStart; index <= fiveYearAverageEnd; index++) {
                fiveYearsAverageYears.add(index);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'marketUpdateFiveYearAverageList(int fiveYearAverageStart, int fiveYearAverageEnd)' method");
        }
        logManager.logInfo("Exiting 'marketUpdateFiveYearAverageList(int fiveYearAverageStart, int fiveYearAverageEnd)' method");
        return fiveYearsAverageYears;
    }

    public void getMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,
            String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate,
            javax.swing.JTable tblMarketUpdate) {
        logManager.logInfo("Entering 'getMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,"
                + " String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate, "
                + " javax.swing.JTable tblMarketUpdate)' method");
        
        try {
            //Load market update indicators on table 
            populateMarketUpdateIndicators(year, month, fiveYearStart, fiveYearEnd,
                    lastSeason, marketZoneName, pnlMarkets, tbmMarketUpdate, tblMarketUpdate);

            //Load market update values on table
            populateMarketUpdateValues(year, month, fiveYearStart, fiveYearEnd, pnlMarkets, tblMarketUpdate);
            //Populate five year average data on 
           // populateMarketUpdateFiveYearAverageValues(year, month, fiveYearStart, fiveYearEnd, pnlMarkets, tblMarketUpdate);
            calculateFiveYearAverage(tblMarketUpdate);
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'getMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,"
                + " String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate, "
                + " javax.swing.JTable tblMarketUpdate)' method");
        }
        logManager.logInfo("Exiting 'getMarketUpdateValues(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,"
                + " String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate, "
                + " javax.swing.JTable tblMarketUpdate)' method");
    }
private void calculateFiveYearAverage(javax.swing.JTable tbl){
        int count = 0;
        float sum = 0.0F;
        int average = 0;
        DecimalFormat thousandSeparator = new DecimalFormat("#,###");
    try{
            for (int i = 0; i < tbl.getRowCount(); i++) {
                sum = 0.0F;
                count = 0;
                average = 0;
             //   if (!this.marketUpdateCategoryRows.contains(Integer.valueOf(i))) {
                    for (int j = 11; j < tbl.getColumnCount(); j++) {
                       // if(!tbl.getValueAt(i, j).equals("")){
                        if (tbl.getValueAt(i, j) != null ) {
                            sum += Float.parseFloat(tbl.getValueAt(i, j).toString().replace(",", ""));
                            count++;
                        }
                        //System.out.println(i);
                      // }
                    }

                    average = Math.round(sum / count);
                    average = (int) Math.floor(sum / count);

                    if (average != 0) {
                        tbl.setValueAt(thousandSeparator.format(Integer.valueOf(average)), i, 1);
                    }
            //    }
            }        
        
    }catch(Exception exception){
        exception.printStackTrace();
    }
}
    //Show/hide chart settings panel
    public void showChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer, boolean isShow) {
        logManager.logInfo("Entering 'showChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer, boolean isShow)' method");
        try {
            if (!isShow) {
                //Hide
                pnlContainer.remove(pnlChartSettings);
            } else {
                //Show
                pnlContainer.add(pnlChartSettings);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'showChartSettingsPanel(javax.swing.JPanel pnlChartSettings, "
                    + " javax.swing.JPanel pnlContainer, boolean isShow)' method");
        } finally {
            //Repaint            
            pnlChartSettings.revalidate();
            pnlChartSettings.repaint();

            pnlContainer.revalidate();
            pnlContainer.repaint();
        }
        logManager.logInfo("Exiting 'showChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer, boolean isShow)' method");
    }

    //Show/hide chart settings panel
    public void displayChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer,
            boolean isShow, int width, int height) {
        logManager.logInfo("Entering 'displayChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer,"
                + "  boolean isShow, int width, int height)' method");
        
        int settingsPanelHeight = 0;
        try {
            settingsPanelHeight = pnlChartSettings.getHeight();
            if (isShow) {
                //Hide settings panel
                pnlChartSettings.setSize(width, 0);
                //Expand chart pane;
                //pnlContainer.setSize(width, pnlContainer.getHeight() + settingsPanelHeight);
                pnlContainer.setSize(width, pnlContainer.getHeight() + 200);
            } else {
                //Show settings panel
                //pnlChartSettings.setSize(width, settingsPanelHeight);
                pnlChartSettings.setSize(width, 200);
                //Expand chart pane;
                //pnlContainer.setSize(width, pnlContainer.getHeight() -  settingsPanelHeight);                
                pnlContainer.setSize(width, pnlContainer.getHeight() - 200);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'displayChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer,"
                + "  boolean isShow, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'displayChartSettingsPanel(javax.swing.JPanel pnlChartSettings, javax.swing.JPanel pnlContainer,"
                + "  boolean isShow, int width, int height)' method");
    }

    //Scroll panel scroll pane to bottom of panel
    public void scrollPanelToBottom(javax.swing.JPanel pnl, javax.swing.JScrollPane scp) {
        logManager.logInfo("Entering 'scrollPanelToBottom(javax.swing.JPanel pnl, javax.swing.JScrollPane scp)' method");
        int width = 0;
        int height = 0;
        int x = 0;
        int y = 0;
        try {
            width = scp.getPreferredSize().width;
            height = scp.getPreferredSize().height;
            y = pnl.getPreferredSize().height - (height - pnl.getPreferredSize().height);
            pnl.scrollRectToVisible(new java.awt.Rectangle(x, y, width, height));
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'scrollPanelToBottom(javax.swing.JPanel pnl, javax.swing.JScrollPane scp)' method");
        }
        logManager.logInfo("Exiting 'scrollPanelToBottom(javax.swing.JPanel pnl, javax.swing.JScrollPane scp)' method");
    }

    //Get whether at least one checkbox has been selected in a panel
    public boolean isCheckboxSelected(javax.swing.JPanel pnl) {
        logManager.logInfo("Entering 'isCheckboxSelected(javax.swing.JPanel pnl)' method");
        int index = 0;
        boolean checkBoxSelected = false;
        JCheckBox[] checkBoxes = null;
        try {
            checkBoxes = (JCheckBox[]) pnl.getComponents();
            while (index < checkBoxes.length) {
                if (checkBoxes[index].isSelected()) {
                    checkBoxSelected = true;
                }
                index++;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'isCheckboxSelected(javax.swing.JPanel pnl)' method");
        }
        logManager.logInfo("Exiting 'isCheckboxSelected(javax.swing.JPanel pnl)' method");
        return checkBoxSelected;
    }

    //Resize panel height by subtracting another panels height
    public int getPanelHeight(int oldHeight, int subtractedHeight) {
        logManager.logInfo("Entering 'getPanelHeight(int oldHeight, int subtractedHeight)' method");
        int newHeight = 0;
        try {
            newHeight = oldHeight - subtractedHeight;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getPanelHeight(int oldHeight, int subtractedHeight)' method");
        }
        logManager.logInfo("Exiting 'getPanelHeight(int oldHeight, int subtractedHeight)' method");
        return newHeight;
    }

    //Save chart as png file
    public void saveChartAsPng(java.io.File chartFile, int width, int height) {
        logManager.logInfo("Entering 'saveChartAsPng(java.io.File chartFile, int width, int height)' method");
        JFreeChart chart = null;
        try {
            chart = CombinedChart.chart;
            ChartUtilities.saveChartAsPNG(chartFile, chart, width, height);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveChartAsPng(java.io.File chartFile, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'saveChartAsPng(java.io.File chartFile, int width, int height)' method");
    }

    //Select default years in a checkbox panel
    public void selectDefaultYears(int mode, javax.swing.JPanel... panels) {
        logManager.logInfo("Entering 'selectDefaultYears(int mode, javax.swing.JPanel... panels)' method");
        int index = 0;
        Component[] components = null;
        JCheckBox chk = null;
        for (javax.swing.JPanel panel : panels) {
            index = 0;
            components = panel.getComponents();
            while (index < components.length) {
                chk = (JCheckBox) components[index];
                if (mode == 1) {
                    // if(AnalysisSettings.seriesYearsList.contains(chk.getText())){
                    if (analysisSettings.getSeriesYearsList().contains(chk.getText())) {
                        chk.setSelected(true);
                    }
                } else {
                    // if(AnalysisSettings.averageYearsList.contains(chk.getText())){
                    if (analysisSettings.getAverageYearsList().contains(chk.getText())) {
                        chk.setSelected(true);
                    }
                }
                index++;
            }
        }
        logManager.logInfo("Exiting 'selectDefaultYears(int mode, javax.swing.JPanel... panels)' method");
    }
    //Show dialog for editing chart settings

    public void editChartProperties() {
        logManager.logInfo("Entering and exiting 'editChartProperties()' method");
        combinedChart.editChartProperies();
    }

    //
    public int savechartChangesIfAny(javax.swing.JDialog dlgChart) {
        logManager.logInfo("Entering and exiting 'savechartChangesIfAny(javax.swing.JDialog dlgChart)' method");
        int response = 0;
        try { 
            
            //response = chartSettings.comparePreviousToCurrentSettings(CombinedChart.chart, combinedChart.copiedChart);
            chartSettings.comparePreviousToCurrentSettings();
            /*if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Changes will be saved", "Chart Settings Changed",
                        JOptionPane.INFORMATION_MESSAGE);
                //Save changes here
            } else {
                JOptionPane.showMessageDialog(null, "Changes will NOT be saved", "Chart Settings Changed",
                        JOptionPane.INFORMATION_MESSAGE);
            }*/
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'savechartChangesIfAny(javax.swing.JDialog dlgChart)' method");
        }
        logManager.logInfo("Exiting and exiting 'savechartChangesIfAny(javax.swing.JDialog dlgChart)' method");
        return JDialog.DISPOSE_ON_CLOSE;
    }
}
