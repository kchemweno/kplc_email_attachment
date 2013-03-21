/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.indicators;

import java.util.ArrayList;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class IndicatorRows {
    private static ArrayList<Integer> marketsIndicatorRowsList = new ArrayList<>();
    private static ArrayList<Integer> slimsPart1IndicatorRowsList = new ArrayList<>();
    private static ArrayList<Integer> slimsPart2IndicatorRowsList = new ArrayList<>();
    private static LogManager logManager = new LogManager();

    public static ArrayList<Integer> getMarketsIndicatorRowsList() {
        logManager.logInfo("Entering and exiting 'getMarketsIndicatorRowsList()' method");
        return marketsIndicatorRowsList;
    }

    public static void setMarketsIndicatorRowsList(ArrayList<Integer> marketsIndicatorRowsList) {
        logManager.logInfo("Entering and exiting 'setMarketsIndicatorRowsList(ArrayList<Integer> marketsIndicatorRowsList)' method");
        IndicatorRows.marketsIndicatorRowsList = marketsIndicatorRowsList;
    }

    public static ArrayList<Integer> getSlimsPart1IndicatorRowsList() {
        logManager.logInfo("Entering and exiting 'getSlimsPart1IndicatorRowsList()' method");
        return slimsPart1IndicatorRowsList;
    }

    public static void setSlimsPart1IndicatorRowsList(ArrayList<Integer> slimsPart1IndicatorRowsList) {
        logManager.logInfo("Entering and exiting 'setSlimsPart1IndicatorRowsList(ArrayList<Integer> slimsPart1IndicatorRowsList)' method");
        IndicatorRows.slimsPart1IndicatorRowsList = slimsPart1IndicatorRowsList;
    }

    public static ArrayList<Integer> getSlimsPart2IndicatorRowsList() {
        logManager.logInfo("Entering and exiting 'getSlimsPart2IndicatorRowsList()' method");
        return slimsPart2IndicatorRowsList;
    }

    public static void setSlimsPart2IndicatorRowsList(ArrayList<Integer> slimsPart2IndicatorRowsList) {
        logManager.logInfo("Entering and exiting 'setSlimsPart2IndicatorRowsList(ArrayList<Integer> slimsPart2IndicatorRowsList)' method");
        IndicatorRows.slimsPart2IndicatorRowsList = slimsPart2IndicatorRowsList;
    }    
}
