/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.settings.global;

import java.awt.Dimension;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class ComponentSize {
    private static LogManager logManager = new LogManager();
    
    //private Dimension componentDimension = null;
    
    //Set frame size
    private void setComponentSize(javax.swing.JFrame frame, Dimension componentDimension){        
        logManager.logInfo("Entering 'setComponentSize(javax.swing.JFrame frame, Dimension componentDimension)' method");
        try{
            frame.setSize(componentDimension);
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'setComponentSize(javax.swing.JFrame frame, Dimension componentDimension)' method");
        }
        logManager.logInfo("Exiting 'setComponentSize(javax.swing.JFrame frame, Dimension componentDimension)' method");
    }
    
    //Set panel size
    private void setComponentSize(javax.swing.JPanel panel, Dimension componentDimension){
        logManager.logInfo("Entering 'setComponentSize(javax.swing.JPanel panel, Dimension componentDimension)' method");
        try{
            panel.setSize(componentDimension);
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'setComponentSize(javax.swing.JPanel panel, Dimension componentDimension)' method");
        }
        logManager.logInfo("Exiting 'setComponentSize(javax.swing.JPanel panel, Dimension componentDimension)' method");
    }  
    
    //Set dialog size
    private void setComponentSize(javax.swing.JDialog dialog, Dimension componentDimension){
        logManager.logInfo("Entering 'setComponentSize(javax.swing.JDialog dialog, Dimension componentDimension)' method");
        try{
            dialog.setSize(componentDimension);
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'setComponentSize(javax.swing.JDialog dialog, Dimension componentDimension)' method");
        }
        logManager.logInfo("Exiting 'setComponentSize(javax.swing.JDialog dialog, Dimension componentDimension)' method");
    }      
}
