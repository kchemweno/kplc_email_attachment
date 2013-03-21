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
 * @author Kiprotich Chemweno
 */
public class GuiManager {

    private ScreenGeometry screenGeometry = new ScreenGeometry();
    private Interface onInterface = new Interface();
    private static LogManager logManager = new LogManager();
    /**
    * 
    * Method centers JFrame window on screen, takes a JFrame object as a parameter
    */
    public void centerFrame(javax.swing.JFrame frame, int width, int height) {
        logManager.logInfo("Entering 'centerFrame(javax.swing.JFrame frame, int width, int height)' method");
        try {
            frame.setLocation(new Point(getCenterXCoordinate(width),getCenterYCoordinate(height)));
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'centerFrame(javax.swing.JFrame frame, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'centerFrame(javax.swing.JFrame frame, int width, int height)' method");
    }

    /**
    * 
    * Method sets size of a JFrame window on screen, takes a JFrame as a parameter
    */
    public void setFrameSize(javax.swing.JFrame frame, int width, int height) {
        logManager.logInfo("Entering 'setFrameSize(javax.swing.JFrame frame, int width, int height)' method");
        int screenWidth = 0;
        int screenHeight = 0;        
        try {
        //If width and height are greater than the screen size, set these values to the screen width and screen height
        screenWidth = onInterface.getScreenSize().width;
        screenHeight = onInterface.getScreenSize().height;
        if(width > screenWidth){
            width = screenWidth - 5;
        }
        if(height > screenHeight){
            height = screenHeight - 75;
        }
        frame.setSize(width, height);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'setFrameSize(javax.swing.JFrame frame, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'setFrameSize(javax.swing.JFrame frame, int width, int height)' method");
    }

    /**
    * 
    * Method centers JDialog window on screen, takes a JDialog object as a parameter
    */
    public void centerDialog(javax.swing.JDialog dialog, int width, int height) {
        logManager.logInfo("Entering 'centerDialog(javax.swing.JDialog dialog, int width, int height)' method");
        try {
            dialog.setLocation(new Point(getCenterXCoordinate(width), getCenterYCoordinate(height)));
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'centerDialog(javax.swing.JDialog dialog, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'centerDialog(javax.swing.JDialog dialog, int width, int height)' method");
    }

    /**
    * 
    * Method sets size of a JDialog window on screen, takes a JDialog as a parameter
    */
    public void setDialogSize(javax.swing.JDialog dialog, int width, int height) {
        logManager.logInfo("Entering 'setDialogSize(javax.swing.JDialog dialog, int width, int height)' method");
        try {
            dialog.setSize(width, height);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown an caught in 'setDialogSize(javax.swing.JDialog dialog, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'setDialogSize(javax.swing.JDialog dialog, int width, int height)' method");
    }

    private int getCenterXCoordinate(int width) {
        logManager.logInfo("Entering 'getCenterXCoordinate(int width)' method");
        int centerXCoordinate = 0;
        try {
          centerXCoordinate = (getScreenCoordinate().x - width) /2;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getCenterXCoordinate(int width)' method");
        }
        logManager.logInfo("Exiting 'getCenterXCoordinate(int width)' method");
        return centerXCoordinate;
    }

    private int getCenterYCoordinate(int height) {
        logManager.logInfo("Entering 'getCenterYCoordinate(int height)' method");
        int centerYCoordinate = 0;
        try {
            centerYCoordinate = (getScreenCoordinate().y - height) /2;
        } catch (Exception exception) {
            logManager.logInfo("Exception was thrown and caught in 'getCenterYCoordinate(int height)' method");
        }
        logManager.logInfo("Exiting 'getCenterYCoordinate(int height)' method");
        return centerYCoordinate;
    }  
    
    //Get the screen center point 
    public Point getScreenCoordinate() {
        logManager.logInfo("Entering 'getScreenCoordinate()' method");
        Dimension screenSize = null;
        double xCoordinate = 0;
        double yCoordinate = 0;
        Point screenCoordinate = null;
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
    
    public void setFrameMaximumSize(javax.swing.JFrame frame){
        logManager.logInfo("Entering 'setFrameMaximumSize(javax.swing.JFrame frame)' method");
        try{
            frame.setMaximumSize(onInterface.getScreenSize());
        }catch(Exception exception){
        logManager.logError("Exception thrown and caught in 'setFrameMaximumSize(javax.swing.JFrame frame)' method");
        }
        logManager.logInfo("Exiting 'setFrameMaximumSize(javax.swing.JFrame frame)' method");
    }    
  }
