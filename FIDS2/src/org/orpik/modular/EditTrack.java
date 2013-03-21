/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class EditTrack {
    private boolean startEdit = false;
    private static LogManager logManager = new LogManager();
    

    public boolean isStartEdit() {
        logManager.logInfo("Entering and exiting 'isStartEdit()' method");
        return startEdit;
    }
 
    public void setStartEdit(boolean startEdit) {
        logManager.logInfo("Entering and exiting 'setStartEdit(boolean startEdit)' method");
        this.startEdit = startEdit;
  }       
}
