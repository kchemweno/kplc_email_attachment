/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import java.sql.ResultSet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class ManageMarkets {
    private ResultSet resultset = null;
    private String sqlString = "";
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private static LogManager logManager = new LogManager();
    
    //Insert new market
    public int insertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId){
        logManager.logInfo("Entering 'insertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        int result = 0;
        try{
            sqlString = queryBuilder.queryInsertMarket(marketName, fieldAnalystId, districtId, marketTypeId);
           result = executeQuery.executeInsert(sqlString);
           if(result > 0){
               //Commit transaction
               executeQuery.commit();
           }else{
               //Rollback transaction
               executeQuery.rollBack();
           }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'insertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        }
        logManager.logInfo("Exiting 'insertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        return result;
    } 
    
    //Update market
    //Insert new market
    public int updateMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId, int marketId){
        logManager.logInfo("Entering 'updateMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        int result = 0;
        try{
            sqlString = queryBuilder.queryEditMarket(marketName, fieldAnalystId, districtId, marketTypeId,marketId);
            System.out.println(sqlString);
            
           result = executeQuery.executeInsert(sqlString);
           if(result > 0){
               executeQuery.commit();
           }else{
               executeQuery.rollBack();
           }
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'updateMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        }
        logManager.logInfo("Exiting 'updateMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        return result;
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
    
    //Add field analyst 
    public int addFieldAnalyst(int staffId) {
        logManager.logInfo("Entering 'addFieldAnalyst(int staffId)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertFieldAnalyst(staffId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'addFieldAnalyst(int staffId)' method");
        }
        logManager.logInfo("Exiting 'addFieldAnalyst(int staffId)' method");
        return result;
    }  
    
    //Add staff 
    public int addStaff(String staffName) {
        logManager.logInfo("Entering 'addStaff(String staffName)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertStaff(staffName);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'addStaff(String staffName)' method");
        }
        logManager.logInfo("Exiting 'addStaff(String staffName)' method");
        return result;
    }      
    
    //Get staff id
    public int getStaffId(String staffName){
        int staffId = 0;
        try{
            sqlString = queryBuilder.selectStaffId(staffName);
            resultset = executeQuery.executeSelect(sqlString);            
            if(resultset.next()){
                staffId = resultset.getInt("staff_id");
            }
        }catch(Exception exception){}
        return staffId;
    }
       
    //Load field analysts
    public void loadFieldAnalysts(JComboBox... cboFieldAnalysts) {
        logManager.logInfo("Entering 'loadFieldAnalysts(JComboBox... cboFieldAnalysts)' method");
        String fieldAnalyst = "";
        try {
            resultset = getFieldAnalysts();
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    fieldAnalyst = resultset.getString("staff_name");
                    for (JComboBox cboFieldAnalyst : cboFieldAnalysts) {
                        cboFieldAnalyst.addItem(fieldAnalyst);                        
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadFieldAnalysts(JComboBox... cboFieldAnalysts)' method");
        }
        logManager.logInfo("Exiting 'loadFieldAnalysts(JComboBox... cboFieldAnalysts)' method");
    }    

    //Get field analysts 
    public ResultSet getFieldAnalysts() {
        logManager.logInfo("Entering 'getFieldAnalysts()' method");
        try {
            sqlString = queryBuilder.sqlSelectFieldAnalysts();
            resultset = executeQuery.executeSelect(sqlString);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getFieldAnalysts()' method");
        }
        logManager.logInfo("Exiting 'getFieldAnalysts()' method");
        return resultset;
    }      
    
    //Load districts 
    public void loadDistricts(JComboBox... cboDistricts) {
        logManager.logInfo("Entering 'loadDistricts(JComboBox... cboDistricts)' method");
        String district = "";
        try {
            resultset = getDistricts();
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    district = resultset.getString("dist_name");
                    for (JComboBox cboDistrict : cboDistricts) {
                        cboDistrict.addItem(district);                        
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadDistricts(JComboBox... cboDistricts)' method");
        }
        logManager.logInfo("Exiting 'loadDistricts(JComboBox... cboDistricts)' method");
    } 

    //Get districts 
    public ResultSet getDistricts() {
        logManager.logInfo("Entering 'getDistricts()' method");
        try {
            sqlString = queryBuilder.sqlSelectDistricts();
            resultset = executeQuery.executeSelect(sqlString);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getDistricts()' method");
        }
        logManager.logInfo("Exiting 'getDistricts()' method");
        return resultset;
    }    

    //Get marketId
    public int getMarketId(javax.swing.JTable tbl) {
        logManager.logInfo("Entering 'getMarketId(javax.swing.JTable tbl)' method");
        int marketId = 0;
        try {
            marketId = Integer.parseInt(tbl.getValueAt(tbl.getSelectedRow(), 0).toString());
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getMarketId(javax.swing.JTable tbl)' method");
        }
        logManager.logInfo("Exiting 'getMarketId(javax.swing.JTable tbl)' method");
        return marketId;
    } 
    
    //Delete market
    public int deleteMarket(int marketId) {
        logManager.logInfo("Entering 'deleteMarket(int marketId)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlUpdateMarketSetInactive(marketId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                executeQuery.commit();
            } else {
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'deleteMarket(int marketId)' method");
        }
        logManager.logInfo("Exiting 'deleteMarket(int marketId)' method");
        return result;
    } 
    
    //Load all indicators
    public void loadMarkets(javax.swing.JTable tblMarkets, javax.swing.table.DefaultTableModel tbmMarkets) {
        logManager.logInfo("Entering 'loadMarkets(javax.swing.JTable tblMarkets, javax.swing.table.DefaultTableModel tbmMarkets)' method");
        String marketName = "";
        String district = "";
        String marketType = "";
        String fieldAnalyst = "";
        int marketId = 0;
        int marketIndex = 0;
       // boolean isActive = false;
        
        try {
            sqlString = queryBuilder.querySelectMarkets();
            resultset = executeQuery.executeSelect(sqlString);
            //Add columns to model
            insertColumnsToModel(tbmMarkets, "Number", "Market Name", "District", "Market Type", "Field Analyst");
            //Set row count to 0
            tbmMarkets.setRowCount(0);
            
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    marketId = resultset.getInt("market_id");//index 0
                    marketName = resultset.getString("market");//index 1
                    marketType = resultset.getString("system");//index 2
                    district = resultset.getString("district_name");//index 3
                    fieldAnalyst = resultset.getString("staff_name");//index 4
                   // isActive = !resultset.getString("active").equalsIgnoreCase("0") ? true : false;//index 5
                    //Insert row
                    tbmMarkets.insertRow(marketIndex, new Object[]{null});
                    //Populate rows
                    tblMarkets.setValueAt(marketId, marketIndex, 0);
                    tblMarkets.setValueAt(marketName, marketIndex, 1);
                    tblMarkets.setValueAt(district, marketIndex, 2);
                    tblMarkets.setValueAt(marketType, marketIndex, 3);
                    tblMarkets.setValueAt(fieldAnalyst, marketIndex, 4);
                    //tblMarkets.setValueAt(new JCheckBox("", isActive), marketIndex, 5);                    
                    resultset.next();
                    marketIndex++;
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadMarkets(javax.swing.JTable tblMarkets, javax.swing.table.DefaultTableModel tbmMarkets)' method");
        }
        logManager.logInfo("Exiting 'loadMarkets(javax.swing.JTable tblMarkets, javax.swing.table.DefaultTableModel tbmMarkets)' method");
    }

    //Add columns to tablemodel
    public void insertColumnsToModel(javax.swing.table.DefaultTableModel tbmMarkets, String... columnNames) {
        logManager.logInfo("Entering 'insertColumnsToModel(javax.swing.table.DefaultTableModel tbmMarkets, String... columnNames)' method");
        try {
            //Remove all previous columns
            tbmMarkets.setColumnCount(0);
            //Add columns to table model
            for (String columnName : columnNames) {
                tbmMarkets.addColumn(columnName);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'insertColumnsToModel(javax.swing.table.DefaultTableModel tbmMarkets, String... columnNames) {' method");
        }
        logManager.logInfo("Exiting 'insertColumnsToModel(javax.swing.table.DefaultTableModel tbmMarkets, String... columnNames)' method");
    }  
    
    //Load market details
    public void loadMarkets(int marketId, JTextField txtMarketName, JComboBox cboFieldAnalyst, 
            JComboBox cboDistrict, JRadioButton rbtMarkets, JRadioButton rbtSlims) {
        logManager.logInfo("Entering 'loadMarkets(int marketId, JTextField txtMarketName, JComboBox cboFieldAnalyst, "
                + " JComboBox cboDistrict, JRadioButton rbtMarkets, JRadioButton rbtSlims)' method");
        
        int systemId = 0;
        //int marketId = 0;
        String marketName = "";
        String district = "";
        String fieldAnalyst = "";        
        
        try {
            //marketId = getMarketId(tbl)
            sqlString = queryBuilder.sqlSelectMarket(marketId);
            resultset = executeQuery.executeSelect(sqlString);
            
            if (resultset.next()) {
                marketName = resultset.getString("market");
                district = resultset.getString("district_name");
                fieldAnalyst = resultset.getString("staff_name");                              
                systemId = resultset.getInt("system");

                //Load market details
                txtMarketName.setText(marketName);
                cboDistrict.setSelectedItem(district);
                cboFieldAnalyst.setSelectedItem(fieldAnalyst);

                //load system id
                if(systemId == 2){
                    rbtSlims.setSelected(true);
                }else if(systemId == 1){
                    rbtMarkets.setSelected(true);
                }              
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadMarkets(int marketId, JTextField txtMarketName, JComboBox cboFieldAnalyst, "
                + " JComboBox cboDistrict, JRadioButton rbtMarkets, JRadioButton rbtSlims)' method");
        }
        logManager.logInfo("Exiting 'loadMarkets(int marketId, JTextField txtMarketName, JComboBox cboFieldAnalyst, "
                + " JComboBox cboDistrict, JRadioButton rbtMarkets, JRadioButton rbtSlims)' method");
    }    
    
    //Clear all components
  public void clearComponents(JTextField txtMarketName, JComboBox cboDistrict, JComboBox cboFieldAnalyst, 
          JRadioButton rbtMarkets, JRadioButton rbtSlims){
      logManager.logInfo("Entering 'clearComponents(JTextField txtMarketName, JComboBox cboDistrict, JComboBox cboFieldAnalyst, "
              + " JRadioButton rbtMarkets, JRadioButton rbtSlims)' method");
      try{
          //Clear market name
          txtMarketName.setText("");
          //Set selected ditrict to first item on combo box
          cboDistrict.setSelectedIndex(0);
          //Set selected field analyst to first item on combo box
          cboFieldAnalyst.setSelectedIndex(0);
          //Deselect markets radio button
          rbtMarkets.setSelected(false);
          //Deselect slims radio button
          rbtSlims.setSelected(false);
      }catch(Exception exception){
      logManager.logError("Exception was thrown and caught in 'clearComponents(JTextField txtMarketName, JComboBox cboDistrict, JComboBox cboFieldAnalyst, "
              + " JRadioButton rbtMarkets, JRadioButton rbtSlims)' method");
      }
      logManager.logInfo("Exiting 'clearComponents(JTextField txtMarketName, JComboBox cboDistrict, JComboBox cboFieldAnalyst, "
              + " JRadioButton rbtMarkets, JRadioButton rbtSlims)' method");
  }
}
