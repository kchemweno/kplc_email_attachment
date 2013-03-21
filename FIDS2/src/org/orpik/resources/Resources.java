/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.resources;

import java.util.Locale;
import java.util.ResourceBundle;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Resources {
    private ResourceBundle enWarningResourceBundle;
    private ResourceBundle frWarningResourceBundle;
    private ResourceBundle soWarningResourceBundle;
    private ResourceBundle keWarningResourceBundle;
    private Locale[] locale= {Locale.ENGLISH, Locale.FRANCE};
    private static LogManager logManager = new LogManager();

    
    public Resources(String locale) {
        logManager.logInfo("Entering 'Resources(String locale)' constructor");
        if(locale.startsWith("en")){
            //Load English locale resource budle
            
        }else if(locale.startsWith("fr")){
            enWarningResourceBundle = ResourceBundle.getBundle("WarningResources", Locale.FRENCH);
        }else if(locale.startsWith("so")){
            
        }else if(locale.startsWith("ke")){
            
        }
        logManager.logInfo("Exiting 'Resources(String locale)' constructor");
    }      
}
