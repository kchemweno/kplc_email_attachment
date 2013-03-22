/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.core;

/**
 *
 * @author Chemweno
 */
public class Stock {
    
    private int openingStock = 0;
    private int closingStock = 0;
    private int stockIn = 0;
    private int stockOut = 0;
    private int availableStock = 0;

    public Stock() {
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(int closingStock) {
        this.closingStock = closingStock;
    }

    public int getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(int openingStock) {
        this.openingStock = openingStock;
    }

    public int getStockIn() {
        return stockIn;
    }

    public void setStockIn(int stockIn) {
        this.stockIn = stockIn;
    }

    public int getStockOut() {
        return stockOut;
    }

    public void setStockOut(int stockOut) {
        this.stockOut = stockOut;
    }
}
