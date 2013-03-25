/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.core;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;

/**
 *
 * @author Chemweno
 */
public class Product {

    private int productId = 0;
    private float reOrderLevel = 0;
    private String productName = "";
    private String productUnit = "";
    private String productCategory = "";
    private String productQuantity = "";
    private ResultSet resultset = null;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private String sqlString = "";

    public Product() {
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductIds(JCheckBox... dipsCheckBoxes) {
        ArrayList<Integer> productIds = new ArrayList<>();
        String ids = "";
        try {
            for (JCheckBox chk : dipsCheckBoxes) {
                if (chk.isSelected()) {
                    //Get only selected product ids
                    if (chk.getText().equals("Petrol Regular")) {
                        //ids = ids + "," + 24;
                        productIds.add(24);
                    }
                    if (chk.getText().equals("Petrol Super")) {
                        //ids = ids + "," + 2;
                        productIds.add(2);
                    }
                    if (chk.getText().equals("Diesel")) {
                        //ids = ids + "," + 1;
                        productIds.add(1);
                    }
                    if (chk.getText().equals("Paraffin")) {
                        // ids = ids + "," + 3;
                        productIds.add(3);
                    }
                }
            }
            ids = productIds.isEmpty() ? "0" : productIds.toString().replace("[", "").replace("]", "");
            //System.out.println("product ids : " + ids);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ids;
    }

    //Get a list of all products
    public ArrayList<Product> getProducts() {
        ArrayList<Product> productsList = new ArrayList<>();
        // String sqlString = "";
        try {
            sqlString = queryBuilder.getAllProducts();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    Product product = new Product();
                    product.setProductId(resultset.getInt("id"));
                    product.setProductName(resultset.getString("name"));
                    product.setProductCategory(resultset.getString("category_name"));
                    product.setProductUnit(resultset.getString("unit_name"));
                    productsList.add(product);
                    resultset.next();
                }
            }
        } catch (Exception exception) {
        }
        return productsList;
    }

    //Get a list of all fuel products
    public ArrayList<Product> getFuelProducts() {
        ArrayList<Product> productsList = new ArrayList<>();
        // String sqlString = "";
        try {
            sqlString = queryBuilder.getFuelProducts();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    Product product = new Product();
                    product.setProductName(resultset.getString("name"));
                    productsList.add(product);
                    resultset.next();
                }
            }
        } catch (Exception exception) {
        }
        return productsList;
    }

    //Populate products
    public void poulateProducts(JComboBox cboProducts) {
        ArrayList<Product> productList = null;
        try {
            //Remove all existing products in combo box
            cboProducts.removeAllItems();
            productList = getProducts();
            for (Product product : productList) {
                cboProducts.addItem(product.getProductName());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //Get all product categories
    public HashMap<String, String> getProductCategoriesList() {
        HashMap<String, String> productCategoriesHashMap = new HashMap<>();
        try {
            sqlString = queryBuilder.getAllProductCategories();
            resultset = executeQuery.executeSelect(sqlString);
            //Remove all items in hash map
            productCategoriesHashMap.clear();
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    //Put mappings in product -> category manner
                    productCategoriesHashMap.put(resultset.getString("name"), resultset.getString("category_name"));
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return productCategoriesHashMap;
    }

    //populate product categories
    public void populateProductCategoriesProductUnits(javax.swing.JComboBox cboProductCategory, javax.swing.JComboBox cboProductUnit) {
        try {
            if (!getProducts().isEmpty()) {
                //Clear previous items on product category combo box if any
                cboProductCategory.removeAllItems();
                //Load product categories into combo box
                for (Product product : getProducts()) {
                    //Check if item is in combo box before adding
                    if (!isItemInComboBox(cboProductCategory, product.getProductCategory())) {
                        cboProductCategory.addItem(product.getProductCategory());
                    }
                    //Load product units
                    if (!isItemInComboBox(cboProductUnit, product.getProductUnit())) {
                        cboProductUnit.addItem(product.getProductUnit());
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    //Check whether combobox contains item

    private boolean isItemInComboBox(javax.swing.JComboBox cbo, String item) {
        boolean isItemContained = false;
        try {
            for (int i = 0; i < cbo.getItemCount(); i++) {
                if (item.equalsIgnoreCase(cbo.getItemAt(i).toString())) {
                    isItemContained = true;
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return isItemContained;
    }
}
