/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.settings.global;

import java.awt.Dimension;
import java.awt.Toolkit;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Interface {

    private Dimension screenSize = null;
    private String lookAndFeel = "Windows";//"Nimbus";//Windows
    private int defaultCloseOperation = 0;
    private static LogManager logManager = new LogManager();
       

    public Interface() {
        //Calculate and update screen size
        logManager.logInfo("Entering and exiting 'Interface()' constructor");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public void setSystemLookAndFeel() {
        logManager.logInfo("Entering 'setSystemLookAndFeel()' method");
        try{
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (lookAndFeel.equals(info.getName())) {
                    //javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    lookAndFeel = info.getClassName();
                    break;
                }
            }
         } catch (Exception ex) {
            //java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
             logManager.logError("Exception was thrown and caught in 'setSystemLookAndFeel()' method");
        } 
    }

    public void setScreenSize() {
        try {
        } catch (Exception exception) {
        }
    }

    //Get the size of default screen
    public Dimension getScreenSize() {
        logManager.logInfo("Entering and exiting 'getScreenSize()' method");
        return screenSize;
    }

    public String getLookAndFeel() {
        logManager.logInfo("Entering and exiting 'getLookAndFeel()' method");
        return lookAndFeel;
    }
}
