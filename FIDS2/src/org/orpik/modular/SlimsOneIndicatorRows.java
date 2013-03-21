/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.modular;

import java.util.ArrayList;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class SlimsOneIndicatorRows {
    private ArrayList<Integer> slimsOneRowsList = new ArrayList<Integer>();
    private static LogManager logManager = new LogManager();

    public ArrayList<Integer> getSlimsOneRowsList() {
        logManager.logInfo("Entering and exiting 'getSlimsOneRowsList()' method");
        return slimsOneRowsList;
    }

    public void setSlimsOneRowsList(ArrayList<Integer> slimsOneRowsList) {
        logManager.logInfo("Entering and exiting 'setSlimsOneRowsList(ArrayList<Integer> slimsOneRowsList)' method");
        this.slimsOneRowsList = slimsOneRowsList;
    }    
}
