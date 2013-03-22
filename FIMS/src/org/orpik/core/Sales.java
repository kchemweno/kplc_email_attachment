/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.core;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;

/**
 *
 * @author Chemweno
 */
public class Sales {

    private int salesId = 0;
    private int staffId = 0;
    private String dateTime = null;
    String productName = "";
    String staffName = "";
    private int productId = 0;
    private float productQuantity = 0;
    private float productPrice = 0;
    private float salesAmount = 0;
    private ResultSet resultset = null;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private String sqlString = "";

    public Sales() {
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getProductName() {
        return productName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }    
    
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public float getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(float productQuantity) {
        this.productQuantity = productQuantity;
    }

    public float getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(float salesAmoutn) {
        this.salesAmount = salesAmoutn;
    }

    public int getSalesId() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId = salesId;
    }

    //Get an arraylist of sales
    public ArrayList<Sales> getSales(String dateFrom, String dateTo, String products) {
        ArrayList<Sales> salesList = new ArrayList<>();
        try {
            sqlString = queryBuilder.getSales(dateFrom, dateTo, products);
            System.out.println(sqlString);
            resultset = executeQuery.executeSelect(sqlString);
            if(resultset.next()){
                while(!resultset.isAfterLast()){
                    Sales sales = new Sales();
                    sales.setDateTime(resultset.getString("date_and_time"));
                    sales.setProductName(resultset.getString("name"));
                    sales.setStaffName(resultset.getString("firstname") + " " + resultset.getString("lastname"));
                    sales.setProductQuantity(resultset.getFloat("quantity"));
                    sales.setSalesAmount(resultset.getFloat("total"));
                    salesList.add(sales);
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return salesList;
    }
}
