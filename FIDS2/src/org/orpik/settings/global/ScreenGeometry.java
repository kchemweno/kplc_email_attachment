/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.settings.global;

import java.awt.Dimension;
import java.awt.Point;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class ScreenGeometry {

    private Interface onInterface;
    private Point screenCoordinate = null;   
    private static LogManager logManager = new LogManager();

    public ScreenGeometry() {
        //Update screen center point
        logManager.logInfo("Entering and exiting 'ScreenGeometry()' constructor");
        screenCoordinate = getScreenCoordinate();
    }

    //Center frame on screen
    public void centerFrameOnScreen(javax.swing.JFrame frame) {
        try {
            logManager.logInfo("Entering and exiting 'centerFrameOnScreen(javax.swing.JFrame frame)' method");
            frame.setLocation(screenCoordinate);   
            logManager.logInfo("Exiting 'centerFrameOnScreen(javax.swing.JFrame frame)' method");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in and exiting 'centerFrameOnScreen(javax.swing.JFrame frame)' method");
        }
    }

    //Center panel on screen
    public void centerPanelOnScreen(javax.swing.JPanel panel) {
        logManager.logInfo("Entering 'centerPanelOnScreen(javax.swing.JPanel panel)' method");
        try {
            panel.setLocation(screenCoordinate);
            logManager.logInfo("Exiting 'centerPanelOnScreen(javax.swing.JPanel panel)' method");
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'centerPanelOnScreen(javax.swing.JPanel panel)' method");
        }
    }

    //Center dialog on screen
    public void centerPanelOnScreen(javax.swing.JDialog dialog) {
        logManager.logInfo("Entering 'centerPanelOnScreen(javax.swing.JDialog dialog)' method");
        try {
            dialog.setLocation(screenCoordinate);
            logManager.logInfo("Exiting 'centerPanelOnScreen(javax.swing.JDialog dialog)' method");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'centerPanelOnScreen(javax.swing.JDialog dialog)' method");
        }
    }

    //Get the screen center point 
    public Point getScreenCoordinate() {
        logManager.logInfo("Entering 'getScreenCoordinate()' method");
        Dimension screenSize = null;
        double xCoordinate = 0;
        double yCoordinate = 0;
        try {
            //Get the screen size
            onInterface = new Interface();
            //Get screen size
            screenSize = onInterface.getScreenSize();
            //Get X coodinate
            xCoordinate = screenSize.getWidth();
            //Get Y coordinate
            yCoordinate = screenSize.getHeight();
            
            //Update screen center point
            screenCoordinate = new Point((int) xCoordinate, (int) yCoordinate);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getScreenCoordinate()' method");
        }
        logManager.logInfo("Exiting 'getScreenCoordinate()' method");
        return screenCoordinate;
    }
}
