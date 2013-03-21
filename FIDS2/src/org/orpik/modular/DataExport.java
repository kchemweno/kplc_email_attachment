/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;
import org.orpik.period.Month;

/**
 *
 * @author chemweno
 */
public class DataExport {

    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private Month month = new Month();
    private static ResultSet resultset = null;
    private static String sqlString = "";
    private static LogManager logManager = new LogManager();

    public void fetchData(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel, int fromYear,
            int toYear, int allDataYear, int month, String exportOption, String exportType) {
        logManager.logInfo("Entering 'fetchData(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel, int fromYear,"
                + " int toYear, int allDataYear, int month, String exportOption, String exportType)' method");

        try {
            //Reset table columns and rows
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            //Load data on table
            if (exportType.equalsIgnoreCase("all")) {
                loadDataAllDataByMonthOnTable(month, allDataYear, exportOption, table, tableModel);
            } else if (exportType.equalsIgnoreCase("range")) {
                loadRangeDataByMonthOnTable(fromYear, toYear, exportOption, table, tableModel);
            }
        } catch (Exception exception) {
            logManager.logError("Excepton was thrown and caught in 'fetchData(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel, int fromYear,"
                + " int toYear, int allDataYear, int month, String exportOption, String exportType)' method");
        }
        logManager.logInfo("Exiting 'fetchData(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel, int fromYear,"
                + " int toYear, int allDataYear, int month, String exportOption, String exportType)' method");
    }

    private String getAllDataByMonthSqlSelect(String exportOption, int month, int year) {
        logManager.logInfo("Entering 'getAllDataByMonthSqlSelect(String exportOption, int month, int year)' method");
        String sqlString = "";
        try {
            sqlString = queryBuilder.querySelectAllDataByMonth(exportOption, month, year);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getAllDataByMonthSqlSelect(String exportOption, int month, int year)' method");
        }
        logManager.logInfo("Exiting 'getAllDataByMonthSqlSelect(String exportOption, int month, int year)' method");
        return sqlString;
    }

    private String getRangeDataByMonthSqlSelect(String exportOption, int fromYear, int toYear) {
        logManager.logInfo("Entering 'getRangeDataByMonthSqlSelect(String exportOption, int fromYear, int toYear)' method");
        String sqlString = "";
        try {
            sqlString = queryBuilder.querySelectRangeDataByMonth(exportOption, fromYear, toYear);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getRangeDataByMonthSqlSelect(String exportOption, int fromYear, int toYear)' method");
        }
        logManager.logInfo("Exiting 'getRangeDataByMonthSqlSelect(String exportOption, int fromYear, int toYear)' method");
        return sqlString;
    }

    //Load data for specific month in a year
    private void loadDataAllDataByMonthOnTable(int month, int year,  String exportOption, javax.swing.JTable table,
            javax.swing.table.DefaultTableModel tableModel) {     
        logManager.logInfo("Entering 'loadDataAllDataByMonthOnTable(int month, int year,  String exportOption, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tableModel)' method");
        int rowIndex = -2;
        int columnIndex = -1;
        String indicator = "";
        String monthValue = "";
        String region = "";
        String district = "";
        String market = "";
        String marketType = "";
        String monthName = "";

        try {
            
            sqlString = getAllDataByMonthSqlSelect(exportOption, month, year);
            //Get resultset
            resultset = executeQuery.executeSelect(sqlString);
            //System.out.pr
            System.out.println(sqlString);
            if (resultset.next()) {
                //Create standard columns
                prepareDataExportTable(table, tableModel);
                //Add other columns and values
                while (!resultset.isAfterLast()) {
                    monthValue = resultset.getString("month_value");
                    indicator = resultset.getString("indicator");
                    market = resultset.getString("market_name");
                    region = resultset.getString("region_name");
                    district = resultset.getString("district_name");
                    marketType = resultset.getString("market_type");
                    year = resultset.getInt("year_name");
                    //month = resultset.getInt("month_id");
                    monthName = this.month.getMonthName(month);

                    columnIndex = indicatorColumnIndex(tableModel, indicator);

                    if (columnIndex == -1) {
                        //Add column
                        tableModel.addColumn(indicator);
                        columnIndex = tableModel.getColumnCount() - 1;
                    }

                    rowIndex = marketRowIndex(tableModel, table, market);
                    if (rowIndex == -1) {
                        //Add row
                        tableModel.addRow(new Object[]{null});
                        rowIndex = tableModel.getRowCount() - 1;
                        //Insert region
                        table.setValueAt(region, rowIndex, 0);
                        //Insert district
                        table.setValueAt(district, rowIndex, 1);
                        //Insert market
                        table.setValueAt(market, rowIndex, 2);
                        //Insert market type
                        table.setValueAt(marketType, rowIndex, 3);
                        //Insert year name
                        table.setValueAt(year, rowIndex, 4);
                        //Insert month name
                        table.setValueAt(monthName, rowIndex, 5);
                    }
                    table.setValueAt(monthValue, rowIndex, columnIndex);
                    resultset.next();
                    //Reset rowindex
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadDataAllDataByMonthOnTable(int month, int year,  String exportOption, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tableModel)' method");
        }
        logManager.logInfo("Exiting 'loadDataAllDataByMonthOnTable(int month, int year,  String exportOption, javax.swing.JTable table,"
                + " javax.swing.table.DefaultTableModel tableModel)' method");
    }

//Load data for a range of years
    private void loadRangeDataByMonthOnTable(int fromYear, int toYear, String exportOption, javax.swing.JTable table, 
            javax.swing.table.DefaultTableModel tableModel) {
        logManager.logInfo("Entering 'loadRangeDataByMonthOnTable(int fromYear, int toYear, String exportOption, javax.swing.JTable table, "
                + " javax.swing.table.DefaultTableModel tableModel)' method");
        int year = 0;
        int month = 0;
        int rowIndex = -2;
        int columnIndex = -1;
        String indicator = "";
        String monthValue = "";
        String region = "";
        String district = "";
        String market = "";
        String marketType = "";
        String monthName = "";

        try {
            //Check if range is valid
            if(isRangeValid(fromYear, toYear)){
            sqlString = getRangeDataByMonthSqlSelect(exportOption, fromYear, toYear);
            //Get resultset
            resultset = executeQuery.executeSelect(sqlString);
            System.out.println(sqlString);
            if (resultset.next()) {
                //Create standard columns
                prepareDataExportTable(table, tableModel);
                //Add other columns and values
                while (!resultset.isAfterLast()) {
                    monthValue = resultset.getString("month_value");
                    indicator = resultset.getString("indicator");
                    market = resultset.getString("market_name");
                    region = resultset.getString("region_name");
                    district = resultset.getString("district_name");
                    marketType = resultset.getString("market_type");
                    year = resultset.getInt("year_name");
                    month = resultset.getInt("month_id");
                    monthName = this.month.getMonthName(month);

                    columnIndex = indicatorColumnIndex(tableModel, indicator);

                    if (columnIndex == -1) {
                        //Add column
                        tableModel.addColumn(indicator);
                        columnIndex = tableModel.getColumnCount() - 1;
                    }

                    //rowIndex = marketRowIndex(tableModel, table, market); 
                    rowIndex = rangeDataMarketRowIndex(tableModel, table, market, monthName, year);
                    if (rowIndex == -1) {
                        //Add row
                        tableModel.addRow(new Object[]{null});
                        rowIndex = tableModel.getRowCount() - 1;
                        //Insert region
                        table.setValueAt(region, rowIndex, 0);
                        //Insert district
                        table.setValueAt(district, rowIndex, 1);
                        //Insert market
                        table.setValueAt(market, rowIndex, 2);
                        //Insert market type
                        table.setValueAt(marketType, rowIndex, 3);
                        //Insert year name
                        table.setValueAt(year, rowIndex, 4);
                        //Insert month name
                        table.setValueAt(monthName, rowIndex, 5);
                    }
                    table.setValueAt(monthValue, rowIndex, columnIndex);
                    resultset.next();
                }
            }
        }else{
                JOptionPane.showMessageDialog(null, "Range of years chosen is not valid.\nPlease chose a \"To Year\" that "
                        + "is higher or same as \"From Year\" ", "", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadRangeDataByMonthOnTable(int fromYear, int toYear, String exportOption, javax.swing.JTable table, "
                + " javax.swing.table.DefaultTableModel tableModel)' method");
        }
        logManager.logInfo("Exiting 'loadRangeDataByMonthOnTable(int fromYear, int toYear, String exportOption, javax.swing.JTable table, "
                + " javax.swing.table.DefaultTableModel tableModel)' method");
    }
    
private boolean isRangeValid(int fromYear, int toYear){
    logManager.logInfo("Entering 'isRangeValid(int fromYear, int toYear)' method");
    boolean validRange = false;
    try{
        if(fromYear <= toYear){
            validRange = true;
        }
    }catch(Exception exception){
    logManager.logError("Exception was thrown and caught in 'isRangeValid(int fromYear, int toYear)' method");
    }
    logManager.logInfo("Exiting 'isRangeValid(int fromYear, int toYear)' method");
    return validRange;
}
    private void loadDataRangeDataByMonthOnTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel) {
        logManager.logInfo("Entering 'loadDataRangeDataByMonthOnTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel)' method");
        try {
            //Get resultset
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                //Create standard columns
                prepareDataExportTable(table, tableModel);
                //Add other columns and values
                while (!resultset.isAfterLast()) {

                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadDataRangeDataByMonthOnTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel)' method");
        }
        logManager.logInfo("Exiting 'loadDataRangeDataByMonthOnTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel)' method");
    }

    private void prepareDataExportTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel) {
        logManager.logInfo("Entering 'prepareDataExportTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel)' method");
        try {
            //Create standard columns
            tableModel.addColumn("REGION");
            tableModel.addColumn("DISTRICT");
            tableModel.addColumn("MARKET");
            tableModel.addColumn("MARKET TYPE");
            tableModel.addColumn("YEAR");
            tableModel.addColumn("MONTH");
        } catch (Exception excetion) {
            logManager.logInfo("Exception was thrown and caught in 'prepareDataExportTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel)' method");
        }
        logManager.logInfo("Exiting 'prepareDataExportTable(javax.swing.JTable table, javax.swing.table.DefaultTableModel tableModel)' method");
    }

    //Method returns -1 if indicator is not on table and the column number if it exists
    private int indicatorColumnIndex(javax.swing.table.DefaultTableModel tableModel, String indicator) {
        logManager.logInfo("Entering 'indicatorColumnIndex(javax.swing.table.DefaultTableModel tableModel, String indicator)' method");
        int columnIndex = -1;
        try {
            for (int index = 6; index < tableModel.getColumnCount(); index++) {
                if (tableModel.getColumnName(index).equalsIgnoreCase(indicator)) {
                    columnIndex = tableModel.getColumnName(index).equalsIgnoreCase(indicator) ? index : -1;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'indicatorColumnIndex(javax.swing.table.DefaultTableModel tableModel, String indicator)' method");
        }
        logManager.logInfo("Exiting 'indicatorColumnIndex(javax.swing.table.DefaultTableModel tableModel, String indicator)' method");
        return columnIndex;
    }

    //Method returns -1 if market is not on table and the row number if it exists
    private int marketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, String market) {
        logManager.logInfo("Entering 'marketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, String market)' method");
        int rowIndex = -1;        
        try {
            for (int index = 0; index < tableModel.getRowCount(); index++) {
                if (table.getValueAt(index, 2) != null) {
                    rowIndex = table.getValueAt(index, 2).toString().equalsIgnoreCase(market) ? index : - 1;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'marketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, String market)' method");
        }
        logManager.logInfo("Exiting 'marketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, String market)' method");
        return rowIndex;
    }
    
    //Method returns -1 if market, year and month are not on table and the row number if it exists
    private int rangeDataMarketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, 
            String market, String month, int year) {
        logManager.logInfo("Entering 'rangeDataMarketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, "
                + "  String market, String month, int year)' method");
        int rowIndex = -1;  
        int yearOnTable = 0;
        String monthOnTable = "";
        String marketOnTable = "";
    
        try {
            for (int index = 0; index < tableModel.getRowCount(); index++) {
                if (table.getValueAt(index, 2) != null && table.getValueAt(index, 4) != null && table.getValueAt(index, 5) !=null) {
                    //Get market on table
                    marketOnTable = table.getValueAt(index, 2).toString();                    
                    //Get year on table
                    yearOnTable = Integer.parseInt(table.getValueAt(index, 4).toString());
                    //Get month on table
                    monthOnTable = table.getValueAt(index, 5).toString();
                    rowIndex = market.equalsIgnoreCase(marketOnTable) && month.equalsIgnoreCase(monthOnTable) && year==yearOnTable ?
                            index : - 1;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'rangeDataMarketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, "
                + "  String market, String month, int year)' method");
        }
        logManager.logInfo("Exiting 'rangeDataMarketRowIndex(javax.swing.table.DefaultTableModel tableModel, javax.swing.JTable table, "
                + "  String market, String month, int year)' method");
        return rowIndex;
    }   
}
