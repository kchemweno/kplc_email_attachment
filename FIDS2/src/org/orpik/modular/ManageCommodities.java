/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import java.sql.ResultSet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class ManageCommodities {

    private ResultSet resultset = null;
    private String sqlString = "";
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private static LogManager logManager = new LogManager();

    //Load all indicators
    public void loadIndicators(javax.swing.JTable tblIndicators, javax.swing.table.DefaultTableModel tbmIndicators) {
        logManager.logInfo("Entering 'loadIndicators(javax.swing.JTable tblIndicators, javax.swing.table.DefaultTableModel tbmIndicators)' method");
        String indicatorBusinessName = "";
        String indicatorCategory = "";
        String indicatorUnit = "";
        String indicatorAvailability = "";
        boolean indicatorActive = false;
        int indicatorNumber = 0;
        int indicatorIndex = 0;
        int indicatorId = 0;
        boolean isActive = false;
        
        try {
            sqlString = queryBuilder.sqlSelectAllIndicators();
            resultset = executeQuery.executeSelect(sqlString);
            //Add columns to model
            insertColumnsToModel(tblIndicators, tbmIndicators, "Number", "Indicator Business Name", "Category",
                    "Unit", "Availability", "Active");
            //Set row count to 0
            tbmIndicators.setRowCount(0);
            
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    indicatorId = resultset.getInt("ind_id");//index 0
                    indicatorBusinessName = resultset.getString("indicator");//index 1
                    indicatorCategory = resultset.getString("category");//index 2
                    indicatorUnit = resultset.getString("unit") != null ? resultset.getString("unit") : "";//index 3
                    indicatorAvailability = resultset.getString("availability");//index 4
                    indicatorActive = !resultset.getString("active").equalsIgnoreCase("0") ? true : false;//index 5
                    //Insert row
                    tbmIndicators.insertRow(indicatorIndex, new Object[]{null});
                    //Populate rows
                    //tblIndicators.setValueAt(++indicatorNumber, indicatorIndex, 0);
                    tblIndicators.setValueAt(indicatorId, indicatorIndex, 0);
                    tblIndicators.setValueAt(indicatorBusinessName, indicatorIndex, 1);
                    tblIndicators.setValueAt(indicatorCategory, indicatorIndex, 2);
                    tblIndicators.setValueAt(indicatorUnit, indicatorIndex, 3);
                    tblIndicators.setValueAt(indicatorAvailability, indicatorIndex, 4);
                    tblIndicators.setValueAt(new JCheckBox("", indicatorActive), indicatorIndex, 5);                    
                    resultset.next();
                    indicatorIndex++;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadIndicators(javax.swing.JTable tblIndicators, javax.swing.table.DefaultTableModel tbmIndicators)' method");
        }
        logManager.logInfo("Exiting 'loadIndicators(javax.swing.JTable tblIndicators, javax.swing.table.DefaultTableModel tbmIndicators)' method");
    }

    //Add columns to tablemodel
    public void insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames) {
        logManager.logInfo("Entering 'insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames)' method");
        try {
            //Remove all previous columns
            tbmUsers.setColumnCount(0);
            //Add columns to table model
            for (String columnName : columnNames) {
                tbmUsers.addColumn(columnName);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames)' method");
        }
        logManager.logInfo("Exiting 'insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames)' method");
    }

    //Get indicatorId
    public int getIndicatorId(javax.swing.JTable tbl) {
        logManager.logInfo("Entering 'getIndicatorId(javax.swing.JTable tbl)' method");
        int indicatorId = 0;
        try {
            indicatorId = Integer.parseInt(tbl.getValueAt(tbl.getSelectedRow(), 0).toString());
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getIndicatorId(javax.swing.JTable tbl)' method");
        }
        logManager.logInfo("Exiting 'getIndicatorId(javax.swing.JTable tbl)' method");
        return indicatorId;
    }

    //Get indicatorId
    public int getIndicatorId(String indicatorBusinessName) {
        logManager.logInfo("Entering 'getIndicatorId(String indicatorBusinessName)' method");
        int userId = 0;
        try {
            sqlString = queryBuilder.sqlSelectUserId(indicatorBusinessName);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                userId = resultset.getInt("user_id");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getIndicatorId(String indicatorBusinessName)' method");
        }
        logManager.logInfo("Exiting 'getIndicatorId(String indicatorBusinessName)' method");
        return userId;
    }

    //Get indicator business name
    public String getUsername(int userId) {
        logManager.logInfo("Entering 'getUsername(int userId)' method");
        String username = "";
        try {
            sqlString = queryBuilder.sqlSelectUsername(userId);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                username = resultset.getString("usr");
            }
        } catch (Exception exception) {
            logManager.logError("Excepton was thrown and caught in 'getUsername(int userId)' method");
        }
        logManager.logInfo("Exiting 'getUsername(int userId)' method");
        return username;
    }

    //Get indicator units
    public ResultSet getIndicatorUnits() {
        logManager.logInfo("Entering 'getIndicatorUnits()' method");
        String units = "";
        try {
            sqlString = queryBuilder.sqlSelectIndicatorUnits();
            resultset = executeQuery.executeSelect(sqlString);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getIndicatorUnits()' method");
        }
        logManager.logInfo("Exiting 'getIndicatorUnits()' method");
        return resultset;
    }

    //Get indicator units
    public ResultSet getIndicatorCategories() {
        logManager.logInfo("Entering 'getIndicatorCategories()' method");
        try {
            sqlString = queryBuilder.sqlSelectIndicatorCategories();
            resultset = executeQuery.executeSelect(sqlString);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getIndicatorCategories()' method");
        }
        logManager.logInfo("Exiting 'getIndicatorCategories()' method");
        return resultset;
    }
    
    //Load units
    public void loadIndicatorUnits(JComboBox... cboIndicatorUnits) {
        logManager.logInfo("Entering 'loadIndicatorUnits(JComboBox... cboIndicatorUnits)' method");
        String unit = "";
        try {
            resultset = getIndicatorUnits();
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    unit = resultset.getString("unit");
                    for (JComboBox cboIndicatorUnit : cboIndicatorUnits) {
                        cboIndicatorUnit.addItem(unit);                        
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadIndicatorUnits(JComboBox... cboIndicatorUnits)' method");
        }
        logManager.logInfo("Exiting 'loadIndicatorUnits(JComboBox... cboIndicatorUnits)' method");
    }

    //Load indicator categories
    public void loadIndicatorCategories(JComboBox... cboIndicatorCategories) {
        logManager.logInfo("Entering 'loadIndicatorCategories(JComboBox... cboIndicatorCategories)' method");
        String category = "";
        try {
            resultset = getIndicatorCategories();
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    category = resultset.getString("cat_name");
                    for (JComboBox cboIndicatorCategory : cboIndicatorCategories) {
                        cboIndicatorCategory.addItem(category);                        
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadIndicatorCategories(JComboBox... cboIndicatorCategories)' method");
        }
        logManager.logInfo("Exception was thrown and caught in 'loadIndicatorCategories(JComboBox... cboIndicatorCategories)' method");
    }
    
    //Load indicators
    public void loadIndicators(int indicatorId, JTextField txtIndicatorName, JTextField txtIndicatorBusinessName,
            JComboBox cboIndicatorCategory, JComboBox cboIndicatorUnit, JCheckBox chkMarkets, JCheckBox chkSlims1,
            JCheckBox chkSlims2) {
        logManager.logInfo("Entering 'loadIndicators(int indicatorId, JTextField txtIndicatorName, JTextField txtIndicatorBusinessName,"
                + " JComboBox cboIndicatorCategory, JComboBox cboIndicatorUnit, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2)' method");
        
        String indicatorBusinessName = "";
        String indicatorName = "";
        String indicatorCategory = "";
        String indicatorUnit = "";
        int availability = 0;
        
        try {
            sqlString = queryBuilder.sqlSelectIndicator(indicatorId);
            resultset = executeQuery.executeSelect(sqlString);
            
            if (resultset.next()) {
                indicatorBusinessName = resultset.getString("indicator");
                indicatorName = resultset.getString("indic_name");
                indicatorCategory = resultset.getString("category");
                indicatorUnit = resultset.getString("unit");                
                availability = resultset.getInt("availability");

                //Load indicator details
                txtIndicatorBusinessName.setText(indicatorBusinessName);
                txtIndicatorName.setText(indicatorName);
                cboIndicatorCategory.setSelectedItem(indicatorCategory);
                cboIndicatorUnit.setSelectedItem(indicatorUnit);
                //load availability
                checkAvailability(availability, chkMarkets, chkSlims1, chkSlims2);                
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadIndicators(int indicatorId, JTextField txtIndicatorName, JTextField txtIndicatorBusinessName,"
                + " JComboBox cboIndicatorCategory, JComboBox cboIndicatorUnit, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2)' method");
        }
        logManager.logInfo("Exiting 'loadIndicators(int indicatorId, JTextField txtIndicatorName, JTextField txtIndicatorBusinessName,"
                + " JComboBox cboIndicatorCategory, JComboBox cboIndicatorUnit, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2)' method");
    }

    //Check availability checkboxes
    private void checkAvailability(int availability, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2) {
        logManager.logInfo("Entering 'checkAvailability(int availability, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2)' method");
        try {
            //Uncheck all checkboxes
            chkMarkets.setSelected(false);
            chkSlims1.setSelected(false);
            chkSlims2.setSelected(false);
            
            if (availability == 1) {
                chkMarkets.setSelected(true);
            }
            if (availability == 2) {
                chkSlims1.setSelected(true);
            }
            if (availability == 3) {
                chkMarkets.setSelected(true);
                chkSlims1.setSelected(true);
            }
            if (availability == 4) {
                chkSlims2.setSelected(true);
            }
            if (availability == 5) {
                chkSlims1.setSelected(true);
                chkSlims2.setSelected(true);
            }
            if (availability == 6) {
                chkMarkets.setSelected(true);
                chkSlims2.setSelected(true);
            }
            if (availability == 7) {
                chkMarkets.setSelected(true);
                chkSlims1.setSelected(true);
                chkSlims2.setSelected(true);
            }            
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'checkAvailability(int availability, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2)' method");
        }
        logManager.logInfo("Exiting 'checkAvailability(int availability, JCheckBox chkMarkets, JCheckBox chkSlims1, JCheckBox chkSlims2)' method");
    }
    
    //Insert new indicator
    public int insertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId){
        logManager.logInfo("Entering 'insertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId)' method");
        int result = 0;
        try{
            sqlString = queryBuilder.queryInsertIndicator(indicatorName, indicatorBusinessName, unitId, categoryId, applicationId);
           result = executeQuery.executeInsert(sqlString);
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'insertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'insertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId)' method");
        return result;
    }
    
    //Get indicator unit id
    public int getIndicatorUnitId(String unit){
        logManager.logInfo("Entering 'getIndicatorUnitId(String unit)' method");
        int unitId = 0;
        try{
            sqlString = queryBuilder.selectIndicatorUnitId(unit);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
                unitId = resultset.getInt("unit_id");
            }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getIndicatorUnitId(String unit)' method");
        }
        logManager.logInfo("Exiting 'getIndicatorUnitId(String unit)' method");
        return unitId;
    }
        
    //Get indicator category id
    public int getIndicatorCategoryId(String category){
        logManager.logInfo("Entering 'getIndicatorCategoryId(String category)' method");
        int categoryId = 0;
        try{
            sqlString = queryBuilder.selectIndicatorCategoryId(category);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
                categoryId = resultset.getInt("cat_id");
            }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'getIndicatorCategoryId(String category)' method");
        }
        logManager.logInfo("Entering 'getIndicatorCategoryId(String category)' method");
        return categoryId;
    }
    
    //Launch dialog
    public void launchDialog(javax.swing.JDialog dialog, int width, int height) {
        logManager.logInfo("Entering 'launchDialog(javax.swing.JDialog dialog, int width, int height)' method");
        try {
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'launchDialog(javax.swing.JDialog dialog, int width, int height)' method");            
        }
        logManager.logInfo("Exiting 'launchDialog(javax.swing.JDialog dialog, int width, int height)' method");
    }   
    
    //Add indicator category 
    public int addIndicatorCategory(String indicatorCategoryName, String description) {
        logManager.logInfo("Entering 'addIndicatorCategory(String indicatorCategoryName, String description)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertIndicatorCategory(indicatorCategoryName, description);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'addIndicatorCategory(String indicatorCategoryName, String description)' method");
        }
        logManager.logInfo("Exiting 'addIndicatorCategory(String indicatorCategoryName, String description)' method");
        return result;
    }
    
    //Add indicator unit 
    public int addIndicatorUnit(String indicatorUnitName, String description) {
        logManager.logInfo("Entering 'addIndicatorUnit(String indicatorUnitName, String description)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertIndicatorUnit(indicatorUnitName, description);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'addIndicatorUnit(String indicatorUnitName, String description)' method");
        }
        logManager.logInfo("Exiting 'addIndicatorUnit(String indicatorUnitName, String description)' method");
        return result;
    } 
    
    //Delete indicator
    public int deleteIndicator(int indicatorId) {
        logManager.logInfo("Entering 'deleteIndicator(int indicatorId)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlUpdateIndicatorSetInactive(indicatorId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                executeQuery.commit();
            } else {
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logInfo("Exception was thrown and caught in 'deleteIndicator(int indicatorId)' method");
        }
        logManager.logInfo("Exiting 'deleteIndicator(int indicatorId)' method");
        return result;
    }    
}
