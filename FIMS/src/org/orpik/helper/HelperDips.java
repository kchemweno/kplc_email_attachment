/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.helper;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;


/**
 *
 * @author chemweno
 */
public class HelperDips {
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ResultSet resultset = null;
    private String sqlQuery = "";
    public void prepareTable(DefaultTableModel tbm, JTable tbl, String... columnNames){
        try{
            //Remove any existing columns on table
            tbm.setColumnCount(0);
            tbm.setRowCount(0);
            //Add columns to table model
            for(String columnName : columnNames){
                tbm.addColumn(columnName);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
   //Populate dips on table
   public void populateDipsOnTable(DefaultTableModel tbm, JTable tbl, String productIds, String tankIds, Date from, Date to){
        Date date = null;
        String productName = "";
        String tank = "";
        String fromFormatedDate = "";
        String toFormatedDate = "";
        int opening = 0;
        int closing = 0;
        int rowIndex = 0;
        int dipSale = 0;
        
       try{
           //Format dates
           fromFormatedDate = formatDate(from);
           toFormatedDate = formatDate(to);
           //Remove all rows from table
           tbm.setRowCount(0);
           rowIndex = 0;
           sqlQuery = queryBuilder.getAllDips(productIds, tankIds, fromFormatedDate, toFormatedDate);
           resultset = executeQuery.executeSelect(sqlQuery);
           if(resultset.next()){
               while(!resultset.isAfterLast()){
                   //Get values
                   date = resultset.getDate("today");
                   productName = resultset.getString("name");
                   tank = resultset.getString("tank");
                   opening = resultset.getInt("opening_dip");
                   closing = resultset.getInt("closing_dip");
                   dipSale = resultset.getInt("dip_sale");
                   
                   //Insert new row
                   tbm.addRow(new Object[]{null});
                   //Date
                   tbl.setValueAt(date, rowIndex, 0);
                   //Product
                   tbl.setValueAt(productName, rowIndex, 1);    
                   //Tank
                   tbl.setValueAt(tank, rowIndex, 2);   
                   //Opening Dip
                   tbl.setValueAt(opening, rowIndex, 3);        
                   //Closing Dip
                   tbl.setValueAt(closing, rowIndex, 4);  
                   //Dip Sale
                   tbl.setValueAt(dipSale, rowIndex, 5);                   
                   resultset.next();
                   rowIndex++;
               }
           }
       }catch(Exception exception){
       exception.printStackTrace();
       }
   }
   
   //Format date into simple date format
   private String formatDate(Date date){
       String formatDate = null;
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       try{
           formatDate = simpleDateFormat.format(date);
       }catch(Exception exception){
           exception.printStackTrace();
       }
       return formatDate;               
   }
}
