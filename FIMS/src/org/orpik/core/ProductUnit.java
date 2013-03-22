/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.core;

/**
 *
 * @author Chemweno
 */
public class ProductUnit {
    
    private int productId = 0;
    private String productUnit = "";
    private String product = "";

    public ProductUnit() {
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
}
