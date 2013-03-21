/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.indicators;

import java.util.HashMap;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class CategoryRows {
    
    private static HashMap<Integer,Integer> marketsTableRowCategoryId = new HashMap<>();
    private static HashMap<Integer,Integer> slimsPart1TableRowCategoryId = new HashMap<>();    
    private static HashMap<Integer,Integer> slimsPart2TableRowCategoryId = new HashMap<>();
    private static LogManager logManager = new LogManager();
    
    public static HashMap<Integer, Integer> getSlimsPart1TableRowCategoryId() {
        logManager.logError("Entering and exiting 'getSlimsPart1TableRowCategoryId()' method");
        return slimsPart1TableRowCategoryId;
    }

    public static void setSlimsPart1TableRowCategoryId(int row, int categoryId) {
        logManager.logError("Entering and exiting 'setSlimsPart1TableRowCategoryId(int row, int categoryId)' method");
        CategoryRows.slimsPart1TableRowCategoryId.put(row, categoryId);
    }

    public static HashMap<Integer, Integer> getMarketsTableRowCategoryId() {
        logManager.logError("Entering and exiting 'getMarketsTableRowCategoryId()' method");
        return marketsTableRowCategoryId;
    }

    public static void setMarketsTableRowCategoryId(int row, int categoryId) {
        logManager.logError("Entering and exiting 'setMarketsTableRowCategoryId(int row, int categoryId)' method");
        CategoryRows.marketsTableRowCategoryId.put(row, categoryId);
    }

    public static HashMap<Integer, Integer> getSlimsPart2TableRowCategoryId() {
        logManager.logError("Entering and exiting 'getSlimsPart2TableRowCategoryId()' method");
        return slimsPart2TableRowCategoryId;
    }

    public static void setSlimsPart2TableRowCategoryId(int row, int categoryId) {
        logManager.logError("Entering and exiting 'setSlimsPart2TableRowCategoryId(int row, int categoryId)' method");
        CategoryRows.slimsPart2TableRowCategoryId.put(row, categoryId);
    }    
}
