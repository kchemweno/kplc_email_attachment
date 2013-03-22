/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.helper;

import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;

/**
 *
 * @author chemweno
 */
public class HelperStock {
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ResultSet resultset = null;
    private String sqlQuery = "";    
    public void prepareTable(DefaultTableModel tbm, JTable tbl, String... columnNames){
        try{
            //Remove all columns on table
            tbm.setColumnCount(0);
            //Set row count to 0
            tbm.setRowCount(0);
            for(String columnName : columnNames){
                //Insert columns
                tbm.addColumn(columnName);
            }       
        }catch(Exception exception){
        exception.printStackTrace();
        }
    }
    
   //Populate dips on table
   public void populateStockOnTable(DefaultTableModel tbm, JTable tbl){        
        String productName = "";        
        String productUnit = "";
        int reOrderLevel = 0;
        int currentStockLevel = 0;
        String dateTaken = "";
        int rowIndex = 0;
       try{
           //Remove all rows from table
           tbm.setRowCount(0);
           rowIndex = 0;
           sqlQuery = queryBuilder.getStock();
           resultset = executeQuery.executeSelect(sqlQuery);
           if(resultset.next()){
               while(!resultset.isAfterLast()){
                   //Get values
                   productName = resultset.getString("product_name");
                   reOrderLevel = resultset.getInt("re_order_level");
                   productUnit = resultset.getString("unit_name");       
                   currentStockLevel = resultset.getInt("current_stock_level");
                   dateTaken = resultset.getString("date_taken");       
                   //Insert new row
                   tbm.addRow(new Object[]{null});
                   //Product name
                   tbl.setValueAt(productName, rowIndex, 0);
                   //Product unit
                   tbl.setValueAt(productUnit, rowIndex, 1);   
                   //Product reorder level
                   tbl.setValueAt(reOrderLevel, rowIndex, 2); 
                   //Product current stock level
                   tbl.setValueAt(currentStockLevel, rowIndex, 3);    
                   //Stock date taken
                   tbl.setValueAt(dateTaken, rowIndex, 4);                    
             
                   resultset.next();
                   rowIndex++;
               }
           }
       }catch(Exception exception){
       exception.printStackTrace();
       }
   }
   
   //Load product details on dialog
   public void loadProductDetailsOnDialog(JTable tbl, JDialog dlg, JTextField txtProductName, JComboBox cboCategory, JComboBox cboUnit, int selectedRowIndex){
       String productName = "";
       String productCategory = "";
       String productUnit = "";
       try{
            productName = tbl.getValueAt(selectedRowIndex, 0) !=null ? tbl.getValueAt(selectedRowIndex, 0).toString() : "";
            productCategory = tbl.getValueAt(selectedRowIndex, 1) !=null ? tbl.getValueAt(selectedRowIndex, 1).toString() : "";
            productUnit = tbl.getValueAt(selectedRowIndex, 2) !=null ? tbl.getValueAt(selectedRowIndex, 2).toString() : "";    
            //Load values on components            
           txtProductName.setText(productName);
           cboCategory.setSelectedItem(productCategory);
           cboUnit.setSelectedItem(productUnit);
           //Set dialog visible
           dlg.setVisible(true);
           //Center dialog
           dlg.setLocationRelativeTo(null);
       }catch(Exception exception){
       exception.printStackTrace();
       }
               
   }
}
