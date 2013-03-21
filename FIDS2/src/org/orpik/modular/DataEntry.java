/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.modular;

import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.email.EmailWithAttachment;
import org.orpik.indicators.CategoryRows;
import org.orpik.indicators.IndicatorCategory;
import org.orpik.indicators.IndicatorRows;
import org.orpik.indicators.Indicators;
import org.orpik.location.Market;
import org.orpik.logging.LogManager;
import org.orpik.period.Month;
import org.orpik.period.Year;
import org.orpik.xml.XmlWriter;

/**
 *
 * @author Chemweno
 */
public class DataEntry {

    private ResultSet resultset = null;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private XmlWriter xmlWriter = new XmlWriter();
    private Market market = new Market();
    private Year year = new Year();
    private Month month = new Month();
    private String sqlSelect = "";
    private String sqlInsert = "";
    private ComboTableCellRenderer renderer = new ComboTableCellRenderer();
    public ArrayList<Integer> marketIndicatorCategoryRows = new ArrayList<>();
    public ArrayList<Integer> slimsPart1IndicatorCategoryRows = new ArrayList<>();
    public ArrayList<Integer> slimsPart2IndicatorCategoryRows = new ArrayList<>();
    public ArrayList<Integer> marketIndicatorRows = new ArrayList<>();
    public ArrayList<Integer> slimsOneIndicatorRows = new ArrayList<>();
    public ArrayList<Integer> slimsTwoIndicatorRows = new ArrayList<>();
    public ArrayList<Integer> slimsOneDetailsRows = new ArrayList<>();
    public ArrayList<Integer> slimsOneLookUpRows = new ArrayList<>();
    public ArrayList<Integer> invalidSupplyColumnRows = new ArrayList<>();
    private Indicators indicator = new Indicators();
    private IndicatorCategory indicatorCategory = new IndicatorCategory();
    //private DatabaseConnect databaseConnect = new DatabaseConnect();
    private EmailWithAttachment email = new EmailWithAttachment();
    private SlimsOneIndicatorRows slimsOneRowsList = new SlimsOneIndicatorRows();
    private HashMap<Integer, Integer> slimsOneIndicatorRowCategoryRowMap = new HashMap<>();
    private static LogManager logManager = new LogManager();
    
    //Look up values
    private String agriculturalActivity = "";
    private String nonAgriculturalActivity = "";
    private String transportMeans = "";
    private String transportCommodity = "";
    private String transportOrigin = "";
    private String transportDestination = "";

    public void prepareDataEntryTable(String type, DefaultTableModel tableModel, javax.swing.JTable table, int comboBoxColumnIndex,
            String applicationIds, String[] supplyColumnItems, String... columnNames) {
        logManager.logInfo("Entering 'prepareDataEntryTable(String type, DefaultTableModel tableModel, javax.swing.JTable table, int comboBoxColumnIndex,"
                + " String applicationIds, String[] supplyColumnItems, String... columnNames)' method");
        int indicatorCategoryId = 0;
        int previousIndicatorCategoryId = 0;
        int rowIndex = 0;
        String indicatorCategory = "";
        String previousIndicatorCategory = "";
        String indicator = "";

        try {
            //Reset array list
            marketIndicatorRows.clear();
            //Create SQL executeQuery statement
            sqlSelect = sqlSelectIndicators(applicationIds);
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Create table structure, create columns
            for (String columnName : columnNames) {
                tableModel.addColumn(columnName);
            }
            //Prepare combo box columns
            if (table.getColumnCount() >= comboBoxColumnIndex + 1) {
                addComboBoxValuesToTableCell(comboBoxColumnIndex, table, supplyColumnItems);
            }
            //Process resultset
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //Get category from resultset
                    indicatorCategory = resultset.getString("category_name");
                    //indicatorCategoryId = this.indicatorCategory.getIndicatorCategoryId(indicatorCategory);
                    indicatorCategoryId = resultset.getInt("indicator_category_id");
                    //Get indicator from resultset 
                    indicator = resultset.getString("indicator_business_name");

                    //Insert row if category is different from previous
                    if (!indicatorCategory.equalsIgnoreCase(previousIndicatorCategory) || rowIndex == 0) {
                        //Skip row 0
                        if (type.equalsIgnoreCase("slims part 1") && rowIndex != 0) {
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("LOCATION NAME", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, previousIndicatorCategoryId);
                            rowIndex++;
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("KEY INFORMANT", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, previousIndicatorCategoryId);
                            rowIndex++;
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("TRIANGULATION", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, previousIndicatorCategoryId);
                            rowIndex++;
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("DATA TRUST LEVEL", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, previousIndicatorCategoryId);
                            rowIndex++;
                        }
                        tableModel.insertRow(rowIndex, new Object[]{null});
                        //}
                        //Update row with category name
                        table.setValueAt(indicatorCategory.toUpperCase(), rowIndex, 0);
                        //Update category rows list
                        if (type.equalsIgnoreCase("slims part 1")) {
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            slimsPart1IndicatorCategoryRows.add(rowIndex);
                        } else if (type.equalsIgnoreCase("slims part 2")) {
                            CategoryRows.setSlimsPart2TableRowCategoryId(rowIndex, indicatorCategoryId);
                            slimsPart2IndicatorCategoryRows.add(rowIndex);
                        } else if (type.equalsIgnoreCase("markets")) {
                            CategoryRows.setMarketsTableRowCategoryId(rowIndex, indicatorCategoryId);
                            marketIndicatorCategoryRows.add(rowIndex);
                        }
                        //Increment row index
                        rowIndex++;
                    }

                    tableModel.insertRow(rowIndex, new Object[]{null});
                    //Update row with indicator name
                    table.setValueAt(indicator, rowIndex, 0);
                    if (type.equalsIgnoreCase("slims part 1")) {
                        //System.err.println(rowIndex +","+indicatorCategoryId);
                        CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                        IndicatorRows.setSlimsPart1IndicatorRowsList(slimsOneIndicatorRows);
                        slimsOneIndicatorRows.add(rowIndex);
                        //slimsOneIndicatorRowCategoryRowMap.put(rowIndex, rowIndex);
                    } else if (type.equalsIgnoreCase("markets")) {
                        marketIndicatorRows.add(rowIndex);
                        IndicatorRows.setMarketsIndicatorRowsList(marketIndicatorRows);
                        CategoryRows.setMarketsTableRowCategoryId(rowIndex, indicatorCategoryId);
                    } else if (type.equalsIgnoreCase("slims part 2")) {
                        CategoryRows.setSlimsPart2TableRowCategoryId(rowIndex, indicatorCategoryId);
                        IndicatorRows.setSlimsPart2IndicatorRowsList(slimsTwoIndicatorRows);
                    }
                    //Track indicator rows for use when inserting data to database
                    //marketIndicatorRows.add(rowIndex);
                    rowIndex++;
                    //Check for Average labor rate agricultural, and non-agricultural in slims part 1
                    if (type.equalsIgnoreCase("slims part 1")) {
                        if (indicator.startsWith("Labor Rate")) {
                            //Insert activity row
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("Activity", rowIndex, 0);
                            slimsOneLookUpRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                        } else if (indicator.startsWith("Transport Cost")) {
                            //Insert means row
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("Means (Transported With)", rowIndex, 0);
                            slimsOneLookUpRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                            //Insert commodity row
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("Commodity Transported", rowIndex, 0);
                            slimsOneLookUpRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                            //Insert origin row
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("Origin", rowIndex, 0);
                            slimsOneLookUpRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                            //Insert destination row
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("Destination", rowIndex, 0);
                            slimsOneLookUpRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;

                            //Add ending slims part 1 rows   
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("LOCATION NAME", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("KEY INFORMANT", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("TRIANGULATION", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                            tableModel.insertRow(rowIndex, new Object[]{null});
                            table.setValueAt("DATA TRUST LEVEL", rowIndex, 0);
                            slimsOneDetailsRows.add(rowIndex);
                            CategoryRows.setSlimsPart1TableRowCategoryId(rowIndex, indicatorCategoryId);
                            rowIndex++;
                        }
                    }
                    //Update previous category name
                    previousIndicatorCategory = indicatorCategory;
                    previousIndicatorCategoryId = indicatorCategoryId;
                    resultset.next();
                    //rowIndex++;                    
                    //System.out.println(rowIndex+","+indicatorCategoryId);
                }
            }
        } catch (Exception exception) {
logManager.logError("Exception was thrown and caught in 'prepareDataEntryTable(String type, DefaultTableModel tableModel, javax.swing.JTable table, int comboBoxColumnIndex,"
                + " String applicationIds, String[] supplyColumnItems, String... columnNames)' method");            
        } finally {
            //CategoryRows.getMarketsTableRowCategoryId();
            //CategoryRows.getSlimsPart1TableRowCategoryId();
            //CategoryRows.getSlimsPart2TableRowCategoryId();
        }
        logManager.logInfo("Exiting 'prepareDataEntryTable(String type, DefaultTableModel tableModel, javax.swing.JTable table, int comboBoxColumnIndex,"
                + " String applicationIds, String[] supplyColumnItems, String... columnNames)' method");
    }

    public void prepareMarketAnalysisTable(String type, DefaultTableModel tableModel, javax.swing.JTable table,
            String... columnNames) {
        logManager.logInfo("Entering 'prepareMarketAnalysisTable(String type, DefaultTableModel tableModel, javax.swing.JTable table,"
        + " String... columnNames)' method");        
        int rowIndex = 0;

        try {
            //Create table structure, create columns
            for (String columnName : columnNames) {
                tableModel.addColumn(columnName);
            }

        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'prepareMarketAnalysisTable(String type, DefaultTableModel tableModel, javax.swing.JTable table,"
        + " String... columnNames)' method");   
        }
        logManager.logInfo("Exiting 'prepareMarketAnalysisTable(String type, DefaultTableModel tableModel, javax.swing.JTable table,"
        + " String... columnNames)' method");   
    }

    private void addComboBoxValuesToTableCell(int columnIndex, javax.swing.JTable table, String... comboBoxItems) {
        logManager.logInfo("Entering 'addComboBoxValuesToTableCell(int columnIndex, javax.swing.JTable table, String... comboBoxItems)' method");   
        try {
            TableColumn column = table.getColumnModel().getColumn(columnIndex);
            javax.swing.JComboBox comboBox = new JComboBox();
            //Add Items to combo box
            for (String comboBoxItem : comboBoxItems) {
                comboBox.addItem(comboBoxItem);
            }
            //Create table cell editor for supply column
            TableCellEditor editor = new DefaultCellEditor(comboBox);
            //Set supply column cell editor
            column.setCellEditor(editor);
            //Set supply column cell renderer
            column.setCellRenderer(renderer);

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'addComboBoxValuesToTableCell(int columnIndex, javax.swing.JTable table, String... comboBoxItems)' method");   
        }
        logManager.logInfo("Exiting 'addComboBoxValuesToTableCell(int columnIndex, javax.swing.JTable table, String... comboBoxItems)' method");   
    }

    //Populate market names on combo box
    public void populateMarketNames(javax.swing.JComboBox cboMarkets, String systemId) {
        logManager.logInfo("Entering 'populateMarketNames(javax.swing.JComboBox cboMarkets, String systemId)' method");   
        ResultSet resultset = null;
        String sqlSelect = "";
        String market = "";
        try {
            //Create sql executeQuery statement
            sqlSelect = sqlSelectMarkets(systemId);
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    market = resultset.getString("market");
                    /**
                     * Add market to JComboBox
                     */
                    cboMarkets.addItem(market);
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateMarketNames(javax.swing.JComboBox cboMarkets, String systemId)' method");   
        }
        logManager.logInfo("Exiting 'populateMarketNames(javax.swing.JComboBox cboMarkets, String systemId)' method");   
    }

    //Populate market names on combo box
    public void populateMarketZones(javax.swing.JComboBox... cboMarkets) {
        logManager.logInfo("Entering 'populateMarketZones(javax.swing.JComboBox... cboMarkets)' method");   
        ResultSet resultset = null;
        String sqlSelect = "";
        //this.resultset = null;
        //this.sqlSelect = "";
        String marketZone = "";
        try {
            //Create sql executeQuery statement
            sqlSelect = sqlSelectMarketZones();
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            System.err.println(sqlSelect);
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    marketZone = resultset.getString("market_zone");
                    /**
                     * Add market to JComboBox
                     */
                    for (JComboBox cbo : cboMarkets) {
                        cbo.addItem(marketZone);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateMarketZones(javax.swing.JComboBox... cboMarkets)' method");   
        }
        logManager.logInfo("Exiting 'populateMarketZones(javax.swing.JComboBox... cboMarkets)' method");   
    }

    //Populate field analysts on combo box
    public void populateFieldAnalysts(javax.swing.JComboBox... cboFieldAnalysts) {
        logManager.logInfo("Entering 'populateFieldAnalysts(javax.swing.JComboBox... cboFieldAnalysts)' method");   
        ResultSet resultset = null;
        String sqlSelect = "";
        String fieldAnalyst = "";
        try {
            //Create sql executeQuery statement
            sqlSelect = sqlSelectFieldAnalysts();
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    fieldAnalyst = resultset.getString("field_analyst");
                    /**
                     * Add field analyst to JComboBox
                     */
                    for (JComboBox cbo : cboFieldAnalysts) {
                        cbo.addItem(fieldAnalyst);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateFieldAnalysts(javax.swing.JComboBox... cboFieldAnalysts)' method");   
        }
        logManager.logInfo("Exiting 'populateFieldAnalysts(javax.swing.JComboBox... cboFieldAnalysts)' method");   
    }

    //Populate regions on combo box
    public void populateRegions(javax.swing.JComboBox... cboRegions) {
        logManager.logInfo("Entering 'populateRegions(javax.swing.JComboBox... cboRegions)' method");   
        ResultSet resultset = null;
        String sqlSelect = "";
        String region = "";
        try {
            //Create sql executeQuery statement
            sqlSelect = sqlSelectRegions();
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    region = resultset.getString("region");
                    /**
                     * Add regions to JComboBox
                     */
                    for (JComboBox cbo : cboRegions) {
                        cbo.addItem(region);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateRegions(javax.swing.JComboBox... cboRegions)' method");   
        }
        logManager.logInfo("Exiting 'populateRegions(javax.swing.JComboBox... cboRegions)' method");   
    }

    //Populate market zone markets on JPanel
    public void populateMarkets(String level, int levelId, int systemId, javax.swing.JPanel... pnlMarkets) {
        logManager.logInfo("Entering 'populateMarkets(String level, int levelId, int systemId, javax.swing.JPanel... pnlMarkets)' method");   
        ResultSet resultset = null;
        String sqlSelect = "";
        String markets = "";
        try {
            //Create sql executeQuery statement
            if (level.equalsIgnoreCase("marketZone")) {
                sqlSelect = sqlSelectMarketsForMarketZone(levelId, systemId);
            } else if (level.equalsIgnoreCase("region")) {
                sqlSelect = sqlSelectMarketsForRegion(levelId, systemId);
            } else if (level.equalsIgnoreCase("fieldAnalyst")) {
                sqlSelect = sqlSelectMarketsForFieldAnalyst(levelId, systemId);
            }

            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //System.out.println(sqlSelect);
            //Add "All" JCheckBox in each panel
            for (JPanel pnl : pnlMarkets) {
                //Remove all previous items
                pnl.removeAll();
                // pnl.setLayout(new GridLayout(0, 1));
                pnl.add(new JCheckBox("ALL", true));
            }
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    markets = resultset.getString("market");
                    /**
                     * Add markets to JPanel
                     */
                    for (JPanel pnl : pnlMarkets) {
                        pnl.add(new JCheckBox(markets, true));
                    }
                    resultset.next();
                }
            }
            for (JPanel pnl : pnlMarkets) {
                pnl.repaint();
                pnl.revalidate();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateMarkets(String level, int levelId, int systemId, javax.swing.JPanel... pnlMarkets)' method");   
        }
        logManager.logInfo("Exiting 'populateMarkets(String level, int levelId, int systemId, javax.swing.JPanel... pnlMarkets)' method");   
    }

    //Populate years on JPanel
    public void populateYears(javax.swing.JPanel... pnlYears) {
        logManager.logInfo("Entering 'populateYears(javax.swing.JPanel... pnlYears)' method");   
        this.resultset = null;
        this.sqlSelect = "";
        String year = "";
        try {
            //Create sql executeQuery statement
            sqlSelect = sqlSelectYears();
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Add "All" JCheckBox in each panel
            for (JPanel pnl : pnlYears) {
                //Remove all previous items
                pnl.removeAll();
                pnl.setLayout(new GridLayout(0, 1));
                //pnl.add(new JCheckBox("ALL", true));
            }
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    year = resultset.getString("year");
                    /**
                     * Add markets to JPanel
                     */
                    for (JPanel pnl : pnlYears) {
                        pnl.add(new JCheckBox(year, false));
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateYears(javax.swing.JPanel... pnlYears)' method");   
        }
        logManager.logInfo("Exiting 'populateYears(javax.swing.JPanel... pnlYears)' method");   
    }

    //Populate field analysts on combo box
    public void populateCommodities(String systemId, javax.swing.JComboBox... cboCommodity) {
        logManager.logInfo("Entering 'populateCommodities(String systemId, javax.swing.JComboBox... cboCommodity)' method");   
        this.resultset = null;
        this.sqlSelect = "";
        String commodity = "";
        try {
            //Remove all commodities first
            for (JComboBox cbo : cboCommodity) {
                cbo.removeAllItems();
            }
            //Create sql executeQuery statement
            sqlSelect = sqlSelectCommodities(systemId);
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    commodity = resultset.getString("indicator");
                    /**
                     * Add commodities to JComboBox
                     */
                    for (JComboBox cbo : cboCommodity) {
                        cbo.addItem(commodity);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateCommodities(String systemId, javax.swing.JComboBox... cboCommodity)' method");   
        }
        logManager.logInfo("Exiting 'populateCommodities(String systemId, javax.swing.JComboBox... cboCommodity)' method");   
    }

    //Populate years on combo box
    public void populateYears(javax.swing.JComboBox... cboYears) {
        logManager.logInfo("Entering 'populateYears(javax.swing.JComboBox... cboYears)' method");   
        this.resultset = null;
        this.sqlSelect = "";
        String year = "";
        try {
            //Create sql executeQuery statement
            sqlSelect = sqlSelectYears();
            //Return a resultset object
            resultset = sqlSelectResults(sqlSelect);
            //Process resultset items
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    year = resultset.getString("year");
                    /**
                     * Add regions to JComboBox
                     */
                    for (JComboBox cbo : cboYears) {
                        cbo.addItem(year);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'populateYears(javax.swing.JComboBox... cboYears)' method");   
        }
        logManager.logInfo("Exiting 'populateYears(javax.swing.JComboBox... cboYears)' method");   
    }

    //Return -1 if data exists, 0 if no data is saved and x for x number of records inserted
    public int saveMarketData(javax.swing.JTable table, javax.swing.JComboBox cboYear,
            javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, int applicationId) {
        logManager.logInfo("Entering 'saveMarketData(javax.swing.JTable table, javax.swing.JComboBox cboYear,"
                + " javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, int applicationId)' method");   
        int result = 0;
        int year = 0;
        int monthId = 0;
        int marketId = 0;
        String yearString = "";
        String month = "";
        String marketName = "";
        String querySuffix = "";
        String queryPrefix = "";
        try {
            year = Integer.parseInt(cboYear.getSelectedItem().toString());
            yearString = cboYear.getSelectedItem().toString();
            monthId = cboMonth.getSelectedIndex() + 1;
            month = cboMonth.getSelectedItem().toString();
            marketName = cboMarket.getSelectedItem().toString();
            marketId = market.getMarketId(marketName);
            querySuffix = getInsertDataSuffix(year, monthId, marketId, table, marketIndicatorCategoryRows);
            queryPrefix = queryBuilder.queryInsertMarketdata();
            sqlInsert = queryPrefix + querySuffix;
            System.out.println(sqlInsert);
            //Check if data already exists in database
            if (!dataExists(year, monthId, marketId, applicationId)) {
                result = executeQuery.executeInsert(sqlInsert);
                if (result > 0) {
                    /**
                     * Commit transaction
                     */
                    executeQuery.commit();
                } else {
                    /**
                     * Roll back transaction
                     */
                    executeQuery.rollBack();
                }
                /**
                 * Create xml file
                 */
                xmlWriter.createMarketXmlFile(table, marketName, yearString, month, marketIndicatorCategoryRows);
            } else {
                result = -1;
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'saveMarketData(javax.swing.JTable table, javax.swing.JComboBox cboYear,"
                + " javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'saveMarketData(javax.swing.JTable table, javax.swing.JComboBox cboYear,"
                + " javax.swing.JComboBox cboMonth, javax.swing.JComboBox cboMarket, int applicationId)' method");
        return result;
    }

    public int saveSlimsPart1Data(javax.swing.JTable table, javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth,
            javax.swing.JComboBox cboMarket, javax.swing.JTextArea txaComments, int applicationId) {
        logManager.logInfo("Entering 'saveSlimsPart1Data(javax.swing.JTable table, javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth,"
                + " javax.swing.JComboBox cboMarket, javax.swing.JTextArea txaComments, int applicationId)' method");
        int resultInsertPrice = 0;
        int resultInsertDetails = 0;
        int resultInsertLookup = 0;
        int result = 0;
        int year = 0;
        int monthId = 0;
        int marketId = 0;
        String yearString = "";
        String month = "";
        String marketName = "";
        String comments = "";

        try {
            year = Integer.parseInt(cboYear.getSelectedItem().toString());
            yearString = cboYear.getSelectedItem().toString();
            monthId = cboMonth.getSelectedIndex() + 1;
            month = cboMonth.getSelectedItem().toString();
            marketName = cboMarket.getSelectedItem().toString();
            marketId = market.getMarketId(marketName);
            comments = txaComments.getText().toString();

            if (!dataExists(year, monthId, marketId, applicationId)) {
                //Save Slims Part 1 prices
                resultInsertPrice = saveSlimsPart1Prices(table, year, monthId, marketId, applicationId);
                if (resultInsertPrice > 0) {
                    result = resultInsertPrice;
                    //Save SlimsPart 1 details
                    resultInsertDetails = saveSlimsPart1Details(table, year, monthId, marketId);
                    if (resultInsertDetails > 0) {
                        result = resultInsertDetails;
                        //Save SlimsPart 1 look up values
                        resultInsertLookup = saveSlimsPart1LookUp(table, year, monthId, marketId, comments);
                        if (resultInsertLookup > 0) {
                            result = resultInsertLookup;                          
                            //Create XML file
                            xmlWriter.createSlimsPartOneXmlFile(table, marketName, yearString, month, 
                                    slimsPart1IndicatorCategoryRows, slimsOneDetailsRows, slimsOneLookUpRows, agriculturalActivity, 
                                    nonAgriculturalActivity, transportMeans, transportCommodity, transportOrigin, transportDestination, 
                                    comments);
                            //Commit transactions
                            executeQuery.commit();
                            //Clear table
                            clearTable(table);
                        } else {
                            result = 0;
                            //Rollback transaction
                            executeQuery.rollBack();
                        }
                    } else {
                        result = 0;
                        //Rollback transaction
                        executeQuery.rollBack();
                    }
                } else {
                    result = 0;
                    //Rollback transaction
                    executeQuery.rollBack();
                }
            } else {
                result = -1;
                //Rollback transaction
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart1Data(javax.swing.JTable table, javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth,"
                + " javax.swing.JComboBox cboMarket, javax.swing.JTextArea txaComments, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart1Data(javax.swing.JTable table, javax.swing.JComboBox cboYear, javax.swing.JComboBox cboMonth,"
                + " javax.swing.JComboBox cboMarket, javax.swing.JTextArea txaComments, int applicationId)' method");
        return result;
    }

    //Return -1 if data exists, 0 if no data is saved and x for x number of records inserted
    public int saveSlimsPart1Prices(javax.swing.JTable table, int year, int monthId, int marketId, int applicationId) {
        logManager.logInfo("Entering 'saveSlimsPart1Prices(javax.swing.JTable table, int year, int monthId, int marketId, int applicationId)' method");
        int result = 0;
        String yearString = "";
        String month = "";
        String querySuffixSlimOnePrices = "";
        String queryPrefixSlimOnePrices = "";

        try {
            querySuffixSlimOnePrices = getInsertSuffixSlimOnePrices(year, monthId, marketId, table, slimsOneIndicatorRows);
            queryPrefixSlimOnePrices = queryBuilder.queryInsertSlimOnePrices();
            sqlInsert = queryPrefixSlimOnePrices + querySuffixSlimOnePrices;

            System.out.println(sqlInsert);
            //Check if data already exists in database
            if (!dataExists(year, monthId, marketId, applicationId)) {
                result = executeQuery.executeInsert(sqlInsert);
            } else {
                result = -1;
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart1Prices(javax.swing.JTable table, int year, int monthId, int marketId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart1Prices(javax.swing.JTable table, int year, int monthId, int marketId, int applicationId)' method");
        return result;
    }

    //Return -1 if data exists, 0 if no data is saved and x for x number of records inserted
    public int saveSlimsPart1Details(javax.swing.JTable table, int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'saveSlimsPart1Details(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        int result = 0;
        String querySuffixSlimOneDetails = "";
        String queryPrefixSlimOneDetails = "";

        try {
            querySuffixSlimOneDetails = getInsertSuffixSlimOneDetails(year, monthId, marketId, table, slimsOneDetailsRows,
                    slimsPart1IndicatorCategoryRows);
            queryPrefixSlimOneDetails = queryBuilder.queryInsertSlimOneDetails();
            sqlInsert = queryPrefixSlimOneDetails + querySuffixSlimOneDetails;

            System.out.println(sqlInsert);
            result = executeQuery.executeInsert(sqlInsert);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart1Details(javax.swing.JTable table, int year, int monthId, int marketId)' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'saveSlimsPart1Details(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        return result;
    }

    //Return -1 if data exists, 0 if no data is saved and x for x number of records inserted
    private int saveSlimsPart1LookUp(javax.swing.JTable table, int year, int monthId, int marketId, String comments) {
        logManager.logInfo("Entering 'saveSlimsPart1LookUp(javax.swing.JTable table, int year, int monthId, int marketId, String comments)' method");
        int result = 0;
        String querySuffixSlimOneLookUp = "";
        String queryPrefixSlimOneLookUp = "";

        try {
            querySuffixSlimOneLookUp = getInsertSuffixSlimOneLookUp(year, monthId, marketId, comments, table, slimsOneLookUpRows);
            queryPrefixSlimOneLookUp = queryBuilder.queryInsertSlimOneLookUp();
            sqlInsert = queryPrefixSlimOneLookUp + querySuffixSlimOneLookUp;

            System.out.println(sqlInsert);
            result = executeQuery.executeInsert(sqlInsert);
        } catch (Exception exception) {
           //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart1LookUp(javax.swing.JTable table, int year, int monthId, int marketId, String comments)' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart1LookUp(javax.swing.JTable table, int year, int monthId, int marketId, String comments)' method");
        return result;
    }

    //Create XML file with SLIMS Part one data
    private void createXmlSlimsPartOne(javax.swing.JTable table, String marketName, String yearString,
            String month, ArrayList<Integer> marketIndicatorCategoryRows) {
        logManager.logInfo("Entering 'createXmlSlimsPartOne(javax.swing.JTable table, String marketName, String yearString,"
                + " String month, ArrayList<Integer> marketIndicatorCategoryRows)' method");
        try {
            xmlWriter.createMarketXmlFile(table, marketName, yearString, month, marketIndicatorCategoryRows);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'createXmlSlimsPartOne(javax.swing.JTable table, String marketName, String yearString,"
                + " String month, ArrayList<Integer> marketIndicatorCategoryRows)' method");
        }
        logManager.logInfo("Exiting 'createXmlSlimsPartOne(javax.swing.JTable table, String marketName, String yearString,"
                + " String month, ArrayList<Integer> marketIndicatorCategoryRows)' method");
    }

    private boolean dataExists(int year, int monthId, int marketId, int applicationId) {
        logManager.logInfo("Entering 'dataExists(int year, int monthId, int marketId, int applicationId)' method");
        resultset = null;
        sqlSelect = "";
        boolean dataExists = true;
        int recordCount = 0;
        try {
            sqlSelect = sqlSelectMonthDataIfExists(year, monthId, marketId, applicationId);
            resultset = sqlSelectMonthDataIfExists(sqlSelect);
            if (resultset.next()) {
                recordCount = resultset.getInt("record_count");
                if (recordCount == 0) {
                    dataExists = false;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'dataExists(int year, int monthId, int marketId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'dataExists(int year, int monthId, int marketId, int applicationId)' method");
        return dataExists;
    }

    private String getInsertDataSuffix(int year, int month, int marketId, javax.swing.JTable table,
            ArrayList<Integer> marketCategoryRows) {
        logManager.logInfo("Entering 'getInsertDataSuffix(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> marketCategoryRows)' method");
        int week = 0;
        int indicatorId = 0;
        int supplyId = 0;
        int price = 0;
        String insertDataSuffix = "";
        for (int row = 0; row < table.getRowCount(); row++) {
            /**
             * Skip category rows
             */
            if (!marketCategoryRows.contains(row)) {
                /**
                 * Get row static values, e.g indicator id
                 */
                indicatorId = indicator.getIndicatorId(table.getValueAt(row, 0).toString());
                /**
                 * Traverse table starting from week 1 column to week 5 column,
                 * no need to visit indicators and supply column
                 */
                for (int column = 1; column < 6; column++) {
                    week = column;
                    /*
                     * supplyId = table.getValueAt(row, 6) != null ?
                     * Integer.parseInt(table.getValueAt(row,
                     * 6).toString().substring(1, 2)) : 0;
                     */
                    //supplyId = Integer.parseInt(table.getValueAt(row, 6).toString().substring(1, 2)+1);                            
                    price = table.getValueAt(row, column) != null
                            ? Integer.parseInt(table.getValueAt(row, column).toString().replace(",", "")) : 0;
                    if (price > 0) {
                        //Pick supply only when a valid price exists
                        supplyId = Integer.parseInt(table.getValueAt(row, 6).toString().substring(1, 2)) + 1;
                        insertDataSuffix = insertDataSuffix + ",(" + year + "," + month + "," + week + ","
                                + "" + marketId + "," + indicatorId + "," + supplyId + "," + price + ")";
                    }
                }
            }
        }
        insertDataSuffix = insertDataSuffix.replaceFirst(",", "");
        logManager.logInfo("Exiting 'getInsertDataSuffix(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> marketCategoryRows)' method");
        return insertDataSuffix;
    }

    private String getInsertSuffixSlimOnePrices(int year, int month, int marketId, javax.swing.JTable table,
            ArrayList<Integer> slimOneIndicatorRows) {
        logManager.logInfo("Entering 'getInsertSuffixSlimOnePrices(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> slimOneIndicatorRows)' method");
        int week = 0;
        int indicatorId = 0;
        int price = 0;
        int categoryId = 0;
        int previousCategoryId = 0;
        String insertPriceDataSuffix = "";
        for (int row = 0; row < table.getRowCount(); row++) {
            if (slimsOneIndicatorRows.contains(row)) {
                /**
                 * Get row static values, e.g indicator id
                 */
                indicatorId = indicator.getIndicatorId(table.getValueAt(row, 0).toString());
                /**
                 * Traverse table starting from week 1 column to week 5 column,
                 * no need to visit indicators and supply column
                 */
                for (int column = 1; column < table.getColumnCount(); column++) {
                    week = column;
                    //supplyId = table.getValueAt(row, 6) != null
                    //        ? Integer.parseInt(table.getValueAt(row, 6).toString().substring(1, 2)) : 0;
                    price = table.getValueAt(row, column) != null
                            ? Integer.parseInt(table.getValueAt(row, column).toString().replace(",", "")) : 0;
                    if (price > 0) {
                        insertPriceDataSuffix = insertPriceDataSuffix + ",(" + year + "," + month + "," + week + ","
                                + "" + marketId + "," + indicatorId + "," + price + ")";
                    }
                }
            }
        }
        insertPriceDataSuffix = insertPriceDataSuffix.replaceFirst(",", "");
        logManager.logInfo("Exiting 'getInsertSuffixSlimOnePrices(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> slimOneIndicatorRows)' method");
        return insertPriceDataSuffix;
    }

    /**
     *
     * Validate Supply values on table. If any of week 1, 2,3,4, or 5 cells in a
     * row on the table has data, then supply column cell must not be null
     *
     */
    private String getInsertSuffixSlimOneDetails(int year, int month, int marketId, javax.swing.JTable table,
            ArrayList<Integer> slimOneAdditionalDetailsRows, ArrayList<Integer> slimOneCategoryRows) {
        logManager.logInfo("Entering 'getInsertSuffixSlimOneDetails(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> slimOneAdditionalDetailsRows, ArrayList<Integer> slimOneCategoryRows)' method");
        String insertSuffix = "";
        String locationName = "";
        String keyInformant = "";
        String triangulation = "";
        String dataTrustLevel = "";
        int categoryId = 0;
        int previousCategoryId = 0;
        //int rowIndex = 0
        boolean categoryDataExists = false;
        try {
            for (int row = 0; row < table.getRowCount(); row++) {

                if (slimOneAdditionalDetailsRows.contains(row)) {
                    //Get location name
                    if (table.getValueAt(row, 0).toString().equalsIgnoreCase("LOCATION NAME")) {
                        locationName = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        categoryDataExists = locationName.equals("") ? false : true;
                    }
                    //Get key informant
                    if (table.getValueAt(row, 0).toString().equalsIgnoreCase("KEY INFORMANT")) {
                        keyInformant = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        categoryDataExists = keyInformant.equals("") ? false : true;
                    }
                    //Get triangulation
                    if (table.getValueAt(row, 0).toString().equalsIgnoreCase("TRIANGULATION")) {
                        triangulation = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        categoryDataExists = triangulation.equals("") ? false : true;
                    }
                    //Get data trust level
                    if (table.getValueAt(row, 0).toString().equalsIgnoreCase("DATA TRUST LEVEL")) {
                        dataTrustLevel = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        categoryDataExists = dataTrustLevel.equals("") ? false : true;
                    }
                } else if (slimOneCategoryRows.contains(row)) {
                    categoryId = indicatorCategory.getIndicatorCategoryId(table.getValueAt(row, 0).toString());
                    if (previousCategoryId != 0 && categoryDataExists == true) {
                        //Check if price data for category is filled
                        insertSuffix = insertSuffix + " , (" + year + "," + month + "," + marketId + "," + previousCategoryId + ",'"
                                + locationName + "','" + keyInformant + "','" + triangulation + "','" + dataTrustLevel + "')";
                    }
                    //Update previous category id
                    previousCategoryId = categoryId;
                    //Reset category data exists variable
                    categoryDataExists = false;
                }
            }
            // Update last variable values
            //  insertSuffix = insertSuffix + " , (" + year + "," + month + "," + marketId + "," + categoryId + ",'"
            //  + locationName + "','" + keyInformant + "','" + triangulation + "','" + dataTrustLevel + "')";
            insertSuffix = insertSuffix + " , (" + year + "," + month + "," + marketId + "," + previousCategoryId + ",'"
                    + locationName + "','" + keyInformant + "','" + triangulation + "','" + dataTrustLevel + "')";

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getInsertSuffixSlimOneDetails(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> slimOneAdditionalDetailsRows, ArrayList<Integer> slimOneCategoryRows)' method");
        }
        insertSuffix = insertSuffix.replaceFirst(",", "");
        logManager.logInfo("Exiting 'getInsertSuffixSlimOneDetails(int year, int month, int marketId, javax.swing.JTable table,"
                + " ArrayList<Integer> slimOneAdditionalDetailsRows, ArrayList<Integer> slimOneCategoryRows)' method");
        return insertSuffix;
    }

    private String getInsertSuffixSlimOneLookUp(int year, int month, int marketId, String comments,
            javax.swing.JTable table, ArrayList<Integer> slimOneLookUpRows) {
        logManager.logInfo("Entering 'getInsertSuffixSlimOneLookUp(int year, int month, int marketId, String comments,"
                + " javax.swing.JTable table, ArrayList<Integer> slimOneLookUpRows)' method");
        String insertSuffixSlimOneLookUp = "";
        boolean lookUpPriceExist = false;
        boolean transportCostExists = false;
        boolean agricLaborPriceActivityExists = false;
        boolean nonAgricLaborPriceActivityExists = false;

        try {
            for (int row = 0; row < table.getRowCount(); row++) {
                if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Transport Cost")) {
                    transportCostExists = false;
                    transportCostExists = lookUpCommodityPriceExist(table, row);
                }
                if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Labor Rate Agricultural")) {
                    agricLaborPriceActivityExists = false;
                    agricLaborPriceActivityExists = lookUpCommodityPriceExist(table, row);
                }
                if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Labor Rate Non-Agricultural")) {
                    nonAgricLaborPriceActivityExists = false;
                    nonAgricLaborPriceActivityExists = lookUpCommodityPriceExist(table, row);
                }
                //Check if row is a look up row, skip if otherwise
                if (slimOneLookUpRows.contains(row)) {

                    if (transportCostExists) {
                        //Get means
                        if (table.getValueAt(row, 0).toString().startsWith("Means")) {
                            //Pick value from same row column index 1
                            transportMeans = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        }
                        //Get commodity
                        if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Commodity Transported")) {
                            //Pick value from same row column index 1
                            transportCommodity = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        }
                        //Get origin
                        if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Origin")) {
                            //Pick value from same row column index 1
                            transportOrigin = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        }
                        //Get Destination                            
                        if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Destination")) {
                            //Pick value from same row column index 1
                            transportDestination = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        }
                    }
                    if (agricLaborPriceActivityExists) {
                        //Get whether it is agric or non-agric
                        if (table.getValueAt(row - 1, 0).toString().equalsIgnoreCase("Labor Rate Agricultural")) {
                            agriculturalActivity = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        }
                    }
                    if (nonAgricLaborPriceActivityExists) {
                        if (table.getValueAt(row - 1, 0).toString().equalsIgnoreCase("Labor Rate Non-Agricultural")) {
                            nonAgriculturalActivity = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        }
                    }
                }
            }

            insertSuffixSlimOneLookUp = insertSuffixSlimOneLookUp
                    + ", (" + year + "," + month + "," + marketId + ",'" + agriculturalActivity + "','" + nonAgriculturalActivity
                    + "','" + transportMeans + "','" + transportCommodity + "','" + transportOrigin + "', '"
                    + transportDestination + "','" + comments + "')";

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getInsertSuffixSlimOneLookUp(int year, int month, int marketId, String comments,"
                + " javax.swing.JTable table, ArrayList<Integer> slimOneLookUpRows)' method");
        }
        insertSuffixSlimOneLookUp = insertSuffixSlimOneLookUp.replaceFirst(",", "");
        //System.out.println(insertSuffixSlimOneLookUp);
        logManager.logInfo("Exiting 'getInsertSuffixSlimOneLookUp(int year, int month, int marketId, String comments,"
                + " javax.swing.JTable table, ArrayList<Integer> slimOneLookUpRows)' method");
        return insertSuffixSlimOneLookUp;
    }

    private boolean lookUpCommodityPriceExist(javax.swing.JTable table, int row) {
        logManager.logInfo("Entering 'lookUpCommodityPriceExist(javax.swing.JTable table, int row)' method");
        boolean dataExists = false;
        try {
            for (int column = 1; column < table.getColumnCount(); column++) {
                if (table.getValueAt(row, column) != null) {
                    dataExists = true;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'lookUpCommodityPriceExist(javax.swing.JTable table, int row)' method");
        }
        logManager.logInfo("Exiting 'lookUpCommodityPriceExist(javax.swing.JTable table, int row)' method");
        return dataExists;
    }

    public boolean validateSupplyValues(javax.swing.JTable table) {
        logManager.logInfo("Entering 'validateSupplyValues(javax.swing.JTable table)' method");
        boolean isSupplyColumnValid = true;
        try {
            for (int row = 0; row < table.getRowCount(); row++) {
                if (!marketIndicatorCategoryRows.contains(row)) {
                    for (int column = 1; column < table.getColumnCount(); column++) {
                        if (table.getValueAt(row, 6) == null && table.getValueAt(row, column) != null) {
                            /**
                             * Add row to invalidSupplyColumnRows list
                             */
                            invalidSupplyColumnRows.add(row);
                            isSupplyColumnValid = false;
                        }
                    }
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'validateSupplyValues(javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'validateSupplyValues(javax.swing.JTable table)' method");
        return isSupplyColumnValid;
    }

    private String createSlimsPart2PricesSuffix(javax.swing.JTable table, int year, int month, int marketId) {
        logManager.logInfo("Entering 'createSlimsPart2PricesSuffix(javax.swing.JTable table, int year, int month, int marketId)' method");
        String sqlInsertString = "";
        int indicatorValue = 0;
        int indicatorId = 0;
        int week = 1;
        String civilInsecurityLevel = "";
        try {
            for (int row = 0; row < table.getRowCount(); row++) {
                if (!slimsPart2IndicatorCategoryRows.contains(row)) {
                    //Get indicator id
                    indicatorId = indicator.getIndicatorId(table.getValueAt(row, 0).toString());
                    if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Level Of Civil Insecurity")) {
                        civilInsecurityLevel = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        if (!civilInsecurityLevel.equalsIgnoreCase("")) {
                            //indicatorValue = getCivilInsecurityLevelId(civilInsecurityLevel);
                            indicatorValue = indicator.getCivilInsecurityLevelId(civilInsecurityLevel);
                        } else {
                            indicatorValue = -1;
                        }
                    } else {
                        indicatorValue = table.getValueAt(row, 1) != null ? Integer.parseInt(table.getValueAt(row, 1).toString()) : -1;
                    }
                    if (indicatorValue > 0) {
                        sqlInsertString = sqlInsertString + ",(" + year + "," + month + "," + week + "," + marketId + ","
                                + indicatorId + "," + indicatorValue + ")";
                    }
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'createSlimsPart2PricesSuffix(javax.swing.JTable table, int year, int month, int marketId)' method");
        }
        sqlInsertString = sqlInsertString.replaceFirst(",", "");
        logManager.logInfo("Exiting 'createSlimsPart2PricesSuffix(javax.swing.JTable table, int year, int month, int marketId)' method");
        return sqlInsertString;
    }

    private int saveSlimsPart2Prices(javax.swing.JTable table, int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'saveSlimsPart2Prices(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        int result = 0;
        String querySuffixSlimTwo = "";
        String queryPrefixSlimTwo = "";

        try {
            querySuffixSlimTwo = createSlimsPart2PricesSuffix(table, year, monthId, marketId);
            queryPrefixSlimTwo = queryBuilder.queryInsertSlims2MonthValuePrefix();
            sqlInsert = queryPrefixSlimTwo + querySuffixSlimTwo;

            System.out.println(sqlInsert);
            result = executeQuery.executeInsert(sqlInsert);
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart2Prices(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart2Prices(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        return result;
    }

    private int saveSlimsPart2Details(javax.swing.JTable table, int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'saveSlimsPart2Details(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        int result = 0;
        String querySuffixSlimTwoDetails = "";
        String queryPrefixSlimTwoDetails = "";

        try {
            querySuffixSlimTwoDetails = createSlimsPart2DetailsSuffix(table, year, monthId, marketId);
            queryPrefixSlimTwoDetails = queryBuilder.queryInsertSlims2DetailsPrefix();
            sqlInsert = queryPrefixSlimTwoDetails + querySuffixSlimTwoDetails;

            System.out.println(sqlInsert);
            result = executeQuery.executeInsert(sqlInsert);
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart2Details(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart2Details(javax.swing.JTable table, int year, int monthId, int marketId)' method");
        return result;
    }

    private int saveSlimsPart2Comments(javax.swing.JTextArea txaComments, int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'saveSlimsPart2Comments(javax.swing.JTextArea txaComments, int year, int monthId, int marketId)' method");
        int result = 0;
        String comments = "";

        try {
            comments = txaComments.getText();
            sqlInsert = queryBuilder.queryInsertSlims2Comments(year, monthId, marketId, comments);
            System.out.println(sqlInsert);
            result = executeQuery.executeInsert(sqlInsert);
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart2Comments(javax.swing.JTextArea txaComments, int year, int monthId, int marketId) {' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart2Comments(javax.swing.JTextArea txaComments, int year, int monthId, int marketId)' method");
        return result;
    }

    private String createSlimsPart2DetailsSuffix(javax.swing.JTable table, int year, int month, int marketId) {
        logManager.logInfo("Entering 'createSlimsPart2DetailsSuffix(javax.swing.JTable table, int year, int month, int marketId)' method");
        String sqlInsertString = "";
        int indicatorValue = 0;
        int indicatorId = 0;
        int week = 1;
        String locationName = "";
        String keyInformant = "";
        String triangulation = "";
        String dataTrustLevel = "";
        try {
            for (int row = 0; row < table.getRowCount(); row++) {
                if (!slimsPart2IndicatorCategoryRows.contains(row)) {
                    //Get indicator id
                    indicatorId = indicator.getIndicatorId(table.getValueAt(row, 0).toString());
                    //Skip rows that have no monthly values filled
                    if (table.getValueAt(row, 1) != null) {
                        locationName = table.getValueAt(row, 2) != null ? table.getValueAt(row, 2).toString() : "";
                        keyInformant = table.getValueAt(row, 3) != null ? table.getValueAt(row, 3).toString() : "";
                        triangulation = table.getValueAt(row, 4) != null ? table.getValueAt(row, 4).toString() : "";
                        dataTrustLevel = table.getValueAt(row, 5) != null ? table.getValueAt(row, 5).toString() : "";

                        sqlInsertString = sqlInsertString + ",(" + year + "," + month + "," + marketId + ","
                                + indicatorId + ",'" + locationName + "','" + keyInformant + "','" + triangulation + "','" + dataTrustLevel + "')";
                    }
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'createSlimsPart2DetailsSuffix(javax.swing.JTable table, int year, int month, int marketId)' method");
        }
        sqlInsertString = sqlInsertString.replaceFirst(",", "");
        logManager.logInfo("Exiting 'createSlimsPart2DetailsSuffix(javax.swing.JTable table, int year, int month, int marketId)' method");
        return sqlInsertString;
    }

    public int saveSlimsPart2Data(javax.swing.JTable table, javax.swing.JTextArea txaComments, int year, int monthId, int marketId,
            String marketName, String monthName) {
        logManager.logInfo("Entering 'saveSlimsPart2Data(javax.swing.JTable table, javax.swing.JTextArea txaComments, int year, int monthId, int marketId,"
                + " String marketName, String monthName)' method");
        int result = 0;
        int applicationId = 4;
        String comments = "";
        comments = txaComments.getText();
        try {
            if (dataExists(year, monthId, marketId, applicationId)) {
                result = -1;
            } else {
                result = saveSlimsPart2Prices(table, year, monthId, marketId);
                if (result > 0) {
                    //Data insert succeeded, save details                
                    result = saveSlimsPart2Details(table, year, monthId, marketId);
                    if (result > 0) {
                        //Save comments
                        result = saveSlimsPart2Comments(txaComments, year, monthId, marketId);
                        if (result > 0) {
                            //Create XML File
                            if (createSlimsPart2XmlFile(marketName, year + "", monthName, comments, table, slimsPart2IndicatorCategoryRows)){
                                //Success, commit transaction
                                executeQuery.commit();                                
                            } else {
                                //Failed creating xml file
                                executeQuery.rollBack();
                                System.out.println("createSlimsPart2XmlFile rolled back");
                            }
                        } else {
                            //Fail
                            result = 0;
                            executeQuery.rollBack();
                            System.out.println("saveSlimsPart2Comments rolled back");
                        }
                    } else {
                        //Fail
                        result = 0;
                        executeQuery.rollBack();
                        System.out.println("saveSlimsPart2Details rolled back");
                    }
                } else if (result == 0) {
                    //Error inserting data
                    executeQuery.rollBack();
                    System.out.println("saveSlimsPart2Prices rolled back");
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveSlimsPart2Data(javax.swing.JTable table, javax.swing.JTextArea txaComments, int year, int monthId, int marketId,"
                + " String marketName, String monthName)' method");
        }
        logManager.logInfo("Exiting 'saveSlimsPart2Data(javax.swing.JTable table, javax.swing.JTextArea txaComments, int year, int monthId, int marketId,"
                + " String marketName, String monthName)' method");
        return result;
    }

    private int getCivilInsecurityLevelId(String civilInsecurityLevel) {
        logManager.logInfo("Entering 'getCivilInsecurityLevelId(String civilInsecurityLevel)' method");
        int id = 0;
        try {
            sqlSelect = queryBuilder.querySelectCivilInsecurityLevelId(civilInsecurityLevel);
            resultset = executeQuery.executeSelect(sqlSelect);
            if (resultset.next()) {
                id = resultset.getInt("id");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getCivilInsecurityLevelId(String civilInsecurityLevel)' method");
        }
        logManager.logInfo("Exiting 'getCivilInsecurityLevelId(String civilInsecurityLevel)' method");
        return id;
    }

    /*
     * private String getCivilInsecurityLevelName(int civilInsecurityId) {
     * String level = ""; try { sqlSelect =
     * queryBuilder.querySelectCivilInsecurityLevelName(civilInsecurityId);
     * resultset = executeQuery.executeSelect(sqlSelect); if (resultset.next())
     * { level = resultset.getString("level"); } } catch (Exception exception) {
     * } return level; }
     */
    private String getSlimsPart2Comments(int year, int month, int marketId) {
        logManager.logInfo("Entering 'getSlimsPart2Comments(int year, int month, int marketId)' method");
        String level = "";
        try {
            sqlSelect = queryBuilder.querySelectSlimsPart2Comments(year, month, marketId);
            resultset = executeQuery.executeSelect(sqlSelect);
            if (resultset.next()) {
                level = resultset.getString("comments");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getSlimsPart2Comments(int year, int month, int marketId)' method");
        }
        logManager.logInfo("Exiting 'getSlimsPart2Comments(int year, int month, int marketId)' method");
        return level;
    }

    private boolean createSlimsPart2XmlFile(String market, String yearName, String month, String comments,
            javax.swing.JTable table, ArrayList<Integer> slimsPart2CategoryRowList) {
        logManager.logInfo("Entering 'createSlimsPart2XmlFile(String market, String yearName, String month, String comments,"
                + "  javax.swing.JTable table, ArrayList<Integer> slimsPart2CategoryRowList)' method");

        boolean isFileCreated = false;
        try {
            isFileCreated = xmlWriter.createSlimsPartTwoXmlFile(market, yearName, month, comments, table, slimsPart2CategoryRowList);
        } catch (Exception exception) {
        logManager.logError("Exception was thrown and caught in 'createSlimsPart2XmlFile(String market, String yearName, String month, String comments,"
                + "  javax.swing.JTable table, ArrayList<Integer> slimsPart2CategoryRowList)' method");            
        }
        logManager.logInfo("Exiting 'createSlimsPart2XmlFile(String market, String yearName, String month, String comments,"
                + "  javax.swing.JTable table, ArrayList<Integer> slimsPart2CategoryRowList)' method");
        return isFileCreated;
    }

    public void clearTable(javax.swing.JTable table) {
        logManager.logInfo("Entering 'clearTable(javax.swing.JTable table)' method");
        int row = 0;
        int col = 1;
        try {
            while (col < table.getColumnCount()) {
                row = 0;
                while (row < table.getRowCount()) {
                    table.setValueAt(null, row, col);
                    row++;
                }
                col++;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'clearTable(javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'clearTable(javax.swing.JTable table)' method");
    }

    public void fetchMarketPrices(int year, int monthId, String marketName, javax.swing.JTable table) {
        logManager.logInfo("Entering 'fetchMarketPrices(int year, int monthId, String marketName, javax.swing.JTable table)' method");
        int price = 0;
        int week = 0;
        int marketId = 0;
        String indicatorName = "";
        String tableCommodity = "";
        String supply = "";

        try {
            marketId = market.getMarketId(marketName);
            sqlSelect = sqlSelectMarketPrices(year, monthId, marketId);
            System.out.println(sqlSelect);
            resultset = sqlSelectResults(sqlSelect);

            //Clear table of any existing data
            clearTable(table);
            //Load data on table
            if (resultset.next()) {

                while (!resultset.isAfterLast()) {
                    //Get price from resultset
                    price = resultset.getInt("price");
                    //Get indicator from resultset
                    indicatorName = resultset.getString("indicator");
                    //Get week from resultset
                    week = resultset.getInt("week");
                    //Get previous month varegae price from resultset
                    supply = resultset.getString("supply");

                    for (int row = 0; row < table.getRowCount(); row++) {
                        //Skip category rows
                        if (!marketIndicatorCategoryRows.contains(row)) {
                            //Get the indicator at the current row
                            tableCommodity = table.getValueAt(row, 0).toString();
                            if (tableCommodity.equalsIgnoreCase(indicatorName)) {
                                //Populate table with price since a match has been found
                                table.setValueAt(price, row, week);
                                //Populate table with last month average price                                
                                table.setValueAt(supply, row, 6);
                            }
                        }
                    }

                    resultset.next();
                }
            } else {
                //Resultset returned no records, hence data does not exist for chosen filters
                JOptionPane.showMessageDialog(null, "There is no data saved for the chosen year, month and market", "Data does not exist",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'fetchMarketPrices(int year, int monthId, String marketName, javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'fetchMarketPrices(int year, int monthId, String marketName, javax.swing.JTable table)' method");
    }

    public void fetchMarketPricesWithAverage(int currentYear, int monthId, String marketName, javax.swing.JTable table) {
        logManager.logInfo("Entering 'fetchMarketPricesWithAverage(int currentYear, int monthId, String marketName, javax.swing.JTable table)' method");
        int price = 0;
        int week = 0;
        int previousMonthAveragePrice = 0;
        int previousMonthYear = 0;
        int previousMonth = 0;
        int marketId = 0;
        String indicatorName = "";
        String tableCommodity = "";
        String supply = "";

        try {
            marketId = market.getMarketId(marketName);
            previousMonthYear = year.getPreviousMonthYear(monthId, currentYear);
            previousMonth = month.getPreviousMonth(monthId);
            sqlSelect = sqlSelectMarketPricesWithAverage(currentYear, previousMonthYear, monthId, previousMonth, marketId);
            System.out.println(sqlSelect);
            resultset = sqlSelectResults(sqlSelect);

            //Clear table of any existing data
            clearTable(table);
            //Load data on table
            if (resultset.next()) {

                while (!resultset.isAfterLast()) {
                    //Get price from resultset
                    price = resultset.getInt("price");
                    //Get indicator from resultset
                    indicatorName = resultset.getString("indicator");
                    //Get week from resultset
                    week = resultset.getInt("week");
                    //Get previous month average price from resultset
                    previousMonthAveragePrice = resultset.getInt("average_price");

                    for (int row = 0; row < table.getRowCount(); row++) {
                        //Skip category rows
                        if (!marketIndicatorCategoryRows.contains(row)) {
                            //Get the indicator at the current row
                            tableCommodity = table.getValueAt(row, 0).toString();
                            if (tableCommodity.equalsIgnoreCase(indicatorName)) {
                                if (price != 0) {
                                    //Populate table with price since a match has been found
                                    table.setValueAt(price, row, week);
                                }
                                if (previousMonthAveragePrice != 0) {
                                    //Populate table with last month average price                                
                                    table.setValueAt(previousMonthAveragePrice, row, 6);
                                }
                            }
                        }
                    }
                    resultset.next();
                }
            } else {
                //Resultset returned no records, hence data does not exist for chosen filters
                JOptionPane.showMessageDialog(null, "There is no data saved for the chosen year, month and market", "Data does not exist",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'fetchMarketPricesWithAverage(int currentYear, int monthId, String marketName, javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'fetchMarketPricesWithAverage(int currentYear, int monthId, String marketName, javax.swing.JTable table)' method");
    }

    public void fetchSlimsPart1Data(int year, int monthId, String marketName, javax.swing.JTextArea txacomments,
            String function, javax.swing.JTable table) throws SQLException {
        logManager.logInfo("Entering 'fetchSlimsPart1Data(int year, int monthId, String marketName, javax.swing.JTextArea txacomments,"
                + " String function, javax.swing.JTable table)' method");
        int result = -100;
        //Fetch Slims Part 1 prices
        result = fetchSlimsPart1Prices(year, monthId, marketName, function, table);

        if (result == 1) {
            //Fetch Slims Part 1 details
            fetchSlimsPart1Details(year, monthId, marketName, table);
            //Fetch Slims Part 1 lookup values
            fetchSlimsPart1Lookup(year, monthId, marketName, txacomments, table);
        } else if (result == -1) {
            //Resultset returned no records, hence data does not exist for chosen filters
            JOptionPane.showMessageDialog(null, "There is no data saved for the chosen year, month and market", "Data does not exist",
                    JOptionPane.WARNING_MESSAGE);
        } else if (result == 0) {
            JOptionPane.showMessageDialog(null, "An error occurred fetching saved data", "Error Encountered",
                    JOptionPane.WARNING_MESSAGE);
        }
        logManager.logInfo("Exiting 'fetchSlimsPart1Data(int year, int monthId, String marketName, javax.swing.JTextArea txacomments,"
                + " String function, javax.swing.JTable table)' method");
    }

    private int fetchSlimsPart1Prices(int currentYear, int monthId, String marketName, String function, javax.swing.JTable table)
            throws SQLException {
        logManager.logInfo("Entering 'fetchSlimsPart1Prices(int currentYear, int monthId, String marketName, String function, javax.swing.JTable table)' method");
        int price = 0;
        int week = 0;
        int result = 1;
        int marketId = 0;
        int previousMonthYear = 0;
        int previousMonth = 0;
        int previousMonthAverage = 0;
        String indicatorName = "";
        String tableCommodity = "";

        try {
            previousMonth = month.getPreviousMonth(monthId);
            previousMonthYear = year.getPreviousMonthYear(monthId, currentYear);
            marketId = market.getMarketId(marketName);
            if (function.equalsIgnoreCase("cleaning")) {
                sqlSelect = sqlSelectSlimsPart1PricesWithAverage(currentYear, previousMonthYear, monthId, previousMonth, marketId);
            } else {
                sqlSelect = sqlSelectSlimsPart1Prices(currentYear, monthId, marketId);
            }
            resultset = sqlSelectResults(sqlSelect);
            //Clear table of any existing data
            clearTable(table);
            //Load data on table
            if (resultset.next()) {
                result = 1;
                while (!resultset.isAfterLast()) {
                    //Get price from resultset
                    price = resultset.getInt("price");
                    //Get week from resultset
                    week = resultset.getInt("week");
                    //Get indicator from resultset
                    indicatorName = resultset.getString("indicator");
                    if (function.equalsIgnoreCase("cleaning")) {
                        previousMonthAverage = resultset.getInt("previous_average");
                    }

                    for (int row = 0; row < table.getRowCount(); row++) {
                        //Skip other rows than indicator rows
                        if (slimsOneIndicatorRows.contains(row)) {
                            //Get the indicator at the current row
                            tableCommodity = table.getValueAt(row, 0).toString();
                            if (tableCommodity.equalsIgnoreCase(indicatorName)) {
                                //Populate table with price since a match has been found
                                table.setValueAt(price, row, week);
                                //Populate average
                                if (function.equalsIgnoreCase("cleaning") && previousMonthAverage > 0) {
                                    table.setValueAt(previousMonthAverage, row, 6);
                                }
                            }
                        }
                    }
                    resultset.next();
                }
            } else {
                result = -1;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'fetchSlimsPart1Prices(int currentYear, int monthId, String marketName, String function, javax.swing.JTable table)' method");
            result = 0;
        }
        logManager.logInfo("Exiting 'fetchSlimsPart1Prices(int currentYear, int monthId, String marketName, String function, javax.swing.JTable table)' method");
        return result;
    }

    private void fetchSlimsPart1Details(int year, int monthId, String marketName, javax.swing.JTable table) {
        logManager.logInfo("Entering 'fetchSlimsPart1Details(int year, int monthId, String marketName, javax.swing.JTable table)' method");
        try {
            int marketId = market.getMarketId(marketName);
            int price = 0;
            int detailsColumn = 1;
            String categoryName = "";
            String tableCategory = "";
            String locationName = "";
            String keyInformant = "";
            String triangulation = "";
            String dataTrustLevel = "";
            boolean populate = false;

            sqlSelect = sqlSelectSlimsPart1Details(year, monthId, marketId);
            System.out.println(sqlSelect);
            resultset = sqlSelectResults(sqlSelect);

            //Load data on table
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //Extract details of this category
                    categoryName = resultset.getString("category_name");
                    locationName = resultset.getString("location_name");
                    keyInformant = resultset.getString("key_informant");
                    triangulation = resultset.getString("triangulation");
                    dataTrustLevel = resultset.getString("data_trust_level");

                    for (int row = 0; row < table.getRowCount(); row++) {
                        tableCategory = table.getValueAt(row, 0) != null ? table.getValueAt(row, 0).toString() : "";
                        if (slimsPart1IndicatorCategoryRows.contains(row)) {
                            //Check for matching category name and category name on resultset
                            if (categoryName.equalsIgnoreCase(tableCategory)) {
                                populate = true;
                            } else {
                                populate = false;
                            }
                        } else if (slimsOneDetailsRows.contains(row) && populate) {
                            if (table.getValueAt(row, 0).toString().equalsIgnoreCase("LOCATION NAME")) {
                                table.setValueAt(locationName, row, detailsColumn);
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("KEY INFORMANT")) {
                                table.setValueAt(keyInformant, row, detailsColumn);
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("TRIANGULATION")) {
                                table.setValueAt(triangulation, row, detailsColumn);
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("DATA TRUST LEVEL")) {
                                table.setValueAt(dataTrustLevel, row, detailsColumn);
                            }
                        }
                    }
                    resultset.next();
                }
            } else {
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'fetchSlimsPart1Details(int year, int monthId, String marketName, javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'fetchSlimsPart1Details(int year, int monthId, String marketName, javax.swing.JTable table)' method");
    }

    private void fetchSlimsPart1Lookup(int year, int monthId, String marketName, javax.swing.JTextArea txaComments, javax.swing.JTable table) {
        logManager.logInfo("Entering 'fetchSlimsPart1Lookup(int year, int monthId, String marketName, javax.swing.JTextArea txaComments, javax.swing.JTable table)' method");
        try {
            int marketId = market.getMarketId(marketName);
            int price = 0;
            int lookupColumn = 1;
            sqlSelect = sqlSelectSlimsPart1Lookup(year, monthId, marketId);
            System.out.println(sqlSelect);
            resultset = sqlSelectResults(sqlSelect);
            String tableValue = "";
            String agriculturalActivity = "";
            String nonAgriculturalActivity = "";
            String transportMeans = "";
            String transportCommodity = "";
            String transportOrigin = "";
            String transportDestination = "";
            String comments = "";

            //Load data on table
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //Get values from resultset
                    agriculturalActivity = resultset.getString("agricultural_activity");
                    nonAgriculturalActivity = resultset.getString("non_agricultural_activity");
                    transportMeans = resultset.getString("transport_means");
                    transportCommodity = resultset.getString("transport_commodity");
                    transportOrigin = resultset.getString("transport_origin");
                    transportDestination = resultset.getString("transport_destination");
                    comments = resultset.getString("comments");

                    for (int row = 0; row < table.getRowCount(); row++) {
                        //Skip category rows
                        if (slimsOneLookUpRows.contains(row)) {
                            //Get the indicator at the current row
                            if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Activity")) {
                                if (table.getValueAt(row - 1, 0).toString().equalsIgnoreCase("Labor Rate Agricultural")) {
                                    table.setValueAt(agriculturalActivity, row, lookupColumn);
                                } else if (table.getValueAt(row - 1, 0).toString().equalsIgnoreCase("Labor Rate Non-Agricultural")) {
                                    table.setValueAt(nonAgriculturalActivity, row, lookupColumn);
                                }
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Means (Transported With)")) {
                                //Update Means
                                table.setValueAt(transportMeans, row, lookupColumn);
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Commodity Transported")) {
                                //Update Commodity Transported
                                table.setValueAt(transportCommodity, row, lookupColumn);
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Origin")) {
                                //Update Origin
                                table.setValueAt(transportOrigin, row, lookupColumn);
                            } else if (table.getValueAt(row, 0).toString().equalsIgnoreCase("Destination")) {
                                //Update Destination
                                table.setValueAt(transportDestination, row, lookupColumn);
                            }
                        }
                    }

                    resultset.next();
                }
                //Insert comments
                txaComments.setText(comments);
            } else {
                //Resultset returned no records, hence data does not exist for chosen filters
               /* JOptionPane.showMessageDialog(null, "There is no data saved for the chosen year, month and market", "Data does not exist",
                        JOptionPane.INFORMATION_MESSAGE);*/
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'fetchSlimsPart1Lookup(int year, int monthId, String marketName, javax.swing.JTextArea txaComments, javax.swing.JTable table)' method");
        }
        logManager.logInfo("Exiting 'fetchSlimsPart1Lookup(int year, int monthId, String marketName, javax.swing.JTextArea txaComments, javax.swing.JTable table)' method");
    }

    public void fetchSlimsPart2Prices(int year, int monthId, int marketId, javax.swing.JTable table,
            javax.swing.JTextArea txaComments, String function) {
        logManager.logInfo("Entering 'fetchSlimsPart2Prices(int year, int monthId, int marketId, javax.swing.JTable table,"
                + " javax.swing.JTextArea txaComments, String function)' method");
        try {
            //int marketId = market.getMarketId(marketName);
            int currentMonthValue = 0;
            int previousMonthValue = -1;
            int previousMonthYear = 0;
            int previousMonth = 0;
            String indicatorName = "";
            String tableCommodity = "";
            String locationName = "";
            String keyInformant = "";
            String triangulation = "";
            String dataTrustLevel = "";
            //String comments = "";
            String civilInsecurityLevel = "";
            //Get year in which previous month falls
            previousMonthYear = this.year.getPreviousMonthYear(monthId, year);
            //Get previous month
            previousMonth = this.month.getPreviousMonth(monthId);
            //sqlSelect = queryBuilder.sqlSelectSlimTwoData(year, monthId, marketId);
            sqlSelect = queryBuilder.sqlSelectSlimTwoDataWithPreviousMonth(year, monthId, marketId, previousMonthYear, previousMonth);
            System.out.println(sqlSelect);
            resultset = sqlSelectResults(sqlSelect);

            //Clear table of any existing data
            clearTable(table);
            //Load data on table
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //Get price from resultset
                    currentMonthValue = resultset.getInt("price");
                    //Get indicator from resultset
                    indicatorName = resultset.getString("indicator");
                    //Get location name
                    locationName = resultset.getString("location_name");
                    //Get key informant name
                    keyInformant = resultset.getString("key_informant");
                    //Get triangulation
                    triangulation = resultset.getString("triangulation");
                    //Get data trust level
                    dataTrustLevel = resultset.getString("data_trust_level");
                    //Get previous month value if function is cleaning
                    if (function.equalsIgnoreCase("cleaning")) {
                        previousMonthValue = resultset.getInt("previous_month_value");
                    }

                    for (int row = 0; row < table.getRowCount(); row++) {
                        //Skip category rows
                        if (!slimsPart2IndicatorCategoryRows.contains(row)) {
                            //Get the indicator at the current row
                            tableCommodity = table.getValueAt(row, 0).toString();
                            if (tableCommodity.equalsIgnoreCase(indicatorName)) {
                                //Populate table with price since a match has been found
                                if (tableCommodity.equalsIgnoreCase("Level Of Civil Insecurity")) {
                                    table.setValueAt(indicator.getCivilInsecurityLevelName(currentMonthValue), row, 1);
                                } else {
                                    table.setValueAt(currentMonthValue, row, 1);
                                }
                                //Populate table with location name
                                table.setValueAt(locationName, row, 2);
                                //Populate table with key informant
                                table.setValueAt(keyInformant, row, 3);
                                //Populate table with triangulation
                                table.setValueAt(triangulation, row, 4);
                                //Populate table with data trust level
                                table.setValueAt(dataTrustLevel, row, 5);
                                //Populate table with previous month value if function is cleaning
                                if (function.equalsIgnoreCase("cleaning") && previousMonthValue > -1) {
                                    if (tableCommodity.equalsIgnoreCase("Level Of Civil Insecurity")) {
                                        table.setValueAt(indicator.getCivilInsecurityLevelName(previousMonthValue), row, 6);
                                    } else {
                                        //table.setValueAt(currentMonthValue, row, 6);
                                        table.setValueAt(previousMonthValue, row, 6);
                                  }
                                }
                            }
                        }
                    }
                    resultset.next();
                }
                if (txaComments != null) {
                    //Populate comments text area
                    txaComments.setText(getSlimsPart2Comments(year, monthId, marketId));
                }
            } else {
                //Resultset returned no records, hence data does not exist for chosen filters
                JOptionPane.showMessageDialog(null, "There is no data saved for the chosen year, month and market", "Data does not exist",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'fetchSlimsPart2Prices(int year, int monthId, int marketId, javax.swing.JTable table,"
                + " javax.swing.JTextArea txaComments, String function)' method");
        }
        logManager.logInfo("Exiting 'fetchSlimsPart2Prices(int year, int monthId, int marketId, javax.swing.JTable table,"
                + " javax.swing.JTextArea txaComments, String function)' method");
    }

    private ResultSet sqlSelectResults(String sqlSelectString) {
        logManager.logInfo("Entering 'sqlSelectResults(String sqlSelectString)' method");
        try {
            this.resultset = executeQuery.executeSelect(sqlSelectString);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectResults(String sqlSelectString)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectResults(String sqlSelectString)' method");
        return resultset;
    }

    private ResultSet sqlSelectMonthDataIfExists(String sqlSelectString) {
        logManager.logInfo("Entering 'sqlSelectMonthDataIfExists(String sqlSelectString)' method");
        try {
            this.resultset = executeQuery.executeSelect(sqlSelectString);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMonthDataIfExists(String sqlSelectString)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMonthDataIfExists(String sqlSelectString)' method");
        return resultset;
    }

    private String sqlSelectIndicators(String applicationIds) {
        logManager.logInfo("Entering 'sqlSelectIndicators(String applicationIds)' method");
        try {
            sqlSelect = queryBuilder.querySelectIndicators(applicationIds);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectIndicators(String applicationIds)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectIndicators(String applicationIds)' method");
        return sqlSelect;
    }

    private String sqlSelectMarkets(String systemId) {
        logManager.logInfo("Entering 'sqlSelectMarkets(String systemId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarkets(systemId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarkets(String systemId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarkets(String systemId)' method");
        return sqlSelect;
    }

    private String sqlSelectMarketZones() {
        logManager.logInfo("Entering 'sqlSelectMarketZones()' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketZones();
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketZones()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketZones()' method");
        return sqlSelect;
    }

    private String sqlSelectFieldAnalysts() {
        logManager.logInfo("Entering 'sqlSelectFieldAnalysts()' method");
        try {
            sqlSelect = queryBuilder.querySelectFieldAnalysts();
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectFieldAnalysts()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectFieldAnalysts()' method");
        return sqlSelect;
    }

    private String sqlSelectRegions() {
        logManager.logInfo("Entering 'sqlSelectRegions()' method");
        try {
            sqlSelect = queryBuilder.querySelectRegions();
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectRegions()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectRegions()' method");
        return sqlSelect;
    }

    private String sqlSelectCommodities(String systemId) {
        logManager.logInfo("Entering 'sqlSelectCommodities(String systemId)' method");
        try {
            sqlSelect = queryBuilder.querySelectCommodities(systemId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectCommodities(String systemId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectCommodities(String systemId)' method");
        return sqlSelect;
    }

    private String sqlSelectYears() {
        logManager.logInfo("Entering 'sqlSelectYears()' method");
        try {
            sqlSelect = queryBuilder.querySelectYears();
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectYears()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectYears()' method");
        return sqlSelect;
    }

    private String sqlSelectMarketsAll(int systemId) {
        logManager.logInfo("Entering 'sqlSelectMarketsAll(int systemId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketsAll(systemId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketsAll(int systemId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketsAll(int systemId)' method");
        return sqlSelect;
    }

    private String sqlSelectMarketsForMarketZone(int marketZoneId, int systemId) {
        logManager.logInfo("Entering 'sqlSelectMarketsForMarketZone(int marketZoneId, int systemId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketsForMarketZone(marketZoneId, systemId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketsForMarketZone(int marketZoneId, int systemId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketsForMarketZone(int marketZoneId, int systemId)' method");
        return sqlSelect;
    }

    private String sqlSelectMarketsForRegion(int regionId, int systemId) {
        logManager.logInfo("Entering 'sqlSelectMarketsForRegion(int regionId, int systemId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketsForRegion(regionId, systemId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketsForRegion(int regionId, int systemId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketsForRegion(int regionId, int systemId)' method");
        return sqlSelect;
    }

    private String sqlSelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId) {
        logManager.logInfo("Entering 'sqlSelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketsForFieldAnalyst(fieldAnalystId, systemId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId)' method");
        return sqlSelect;
    }

    private String sqlSelectMonthDataIfExists(int year, int monthId, int marketId, int applicationId) {
        logManager.logInfo("Entering 'sqlSelectMonthDataIfExists(int year, int monthId, int marketId, int applicationId)' method");
        try {
            sqlSelect = queryBuilder.querySelectRecordCount(year, monthId, marketId, applicationId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMonthDataIfExists(int year, int monthId, int marketId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMonthDataIfExists(int year, int monthId, int marketId, int applicationId)' method");
        return sqlSelect;
    }

    private String sqlSelectMarketPrices(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'sqlSelectMarketPrices(int year, int monthId, int marketId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketPrices(year, monthId, marketId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketPrices(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketPrices(int year, int monthId, int marketId)' method");
        return sqlSelect;
    }

    private String sqlSelectSlimsPart1Prices(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'sqlSelectSlimsPart1Prices(int year, int monthId, int marketId)' method");
        try {
            sqlSelect = queryBuilder.querySelectSlimsPart1Prices(year, monthId, marketId);
        } catch (Exception exception) {
            logManager.logError("Exiting was thrown and caught in 'sqlSelectSlimsPart1Prices(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectSlimsPart1Prices(int year, int monthId, int marketId)' method");
        return sqlSelect;
    }

    private String sqlSelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId) {
        logManager.logInfo("Entering 'sqlSelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId)' method");
        try {
            sqlSelect = queryBuilder.querySelectSlimsPart1PricesWithAverage(currentMonthYear, previousMonthYear, currentMonth, previousMonth, marketId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId)' method");
        return sqlSelect;
    }

    private String sqlSelectSlimsPart1Details(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'sqlSelectSlimsPart1Details(int year, int monthId, int marketId)' method");
        try {
            sqlSelect = queryBuilder.querySelectSlimsPart1Details(year, monthId, marketId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectSlimsPart1Details(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectSlimsPart1Details(int year, int monthId, int marketId)' method");
        return sqlSelect;
    }

    private String sqlSelectSlimsPart1Lookup(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'sqlSelectSlimsPart1Lookup(int year, int monthId, int marketId)' method");
        try {
            sqlSelect = queryBuilder.querySelectSlimsPart2Lookup(year, monthId, marketId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectSlimsPart1Lookup(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectSlimsPart1Lookup(int year, int monthId, int marketId)' method");
        return sqlSelect;
    }

    private String sqlSelectCivilInsecurityLevelId(String level) {
        logManager.logInfo("Entering 'sqlSelectCivilInsecurityLevelId(String level)' method");
        try {
            sqlSelect = queryBuilder.querySelectCivilInsecurityLevelId(level);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectCivilInsecurityLevelId(String level)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectCivilInsecurityLevelId(String level)' method");
        return sqlSelect;
    }

    private String sqlSelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId) {
        logManager.logInfo("Entering 'sqlSelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId)' method");
        try {
            sqlSelect = queryBuilder.querySelectMarketPricesWithAverage(currentMonthYear, previousMonthYear, currentMonth, previousMonth, marketId);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonth, int previousMonth, int marketId)' method");
        return sqlSelect;
    }

    /**
     * Send email with attachment
     */
    public boolean sendEmail() {
        logManager.logInfo("Entering 'sendEmail()' method");
        boolean isSent = false;
        try {
            isSent = email.sendEmailWithAttachment(xmlWriter.getXmlFilename().toString());
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sendEmail()' method");
        }
        logManager.logInfo("Exiting 'sendEmail()' method");
        return isSent;
    }

    public void setSlimsOneIndicatorRowsList() {
        logManager.logInfo("Entering and exiting 'setSlimsOneIndicatorRowsList()' method");
        slimsOneRowsList.setSlimsOneRowsList(slimsOneIndicatorRows);
    }
}
