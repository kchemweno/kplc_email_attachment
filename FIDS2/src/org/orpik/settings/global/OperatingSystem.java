/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.settings.global;

import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public final class OperatingSystem {
   private static LogManager logManager = new LogManager();
   private String OS = null;
   public String getOsName(){
      logManager.logInfo("Entering 'getOsName()' method"); 
      if(OS == null) {
          OS = System.getProperty("os.name"); 
      }
      logManager.logInfo("Exiting 'getOsName()' method");
      return OS;
   }
   
   public boolean isWindows()     
   {
       logManager.logInfo("Entering and exiting 'isWindows()' method"); 
      return getOsName().startsWith("Windows");
   }

   public boolean isLinux() {
       logManager.logInfo("Entering and exiting 'isLinux()' method"); 
        return getOsName().startsWith("Linux");               
   }
   
   public boolean isUnix() {
       logManager.logInfo("Entering and exiting 'isUnix()' method"); 
        return getOsName().startsWith("Unix");               
   } 
   
  public String getDirectorySeparator(){
      logManager.logInfo("Entering 'getDirectorySeparator()' method"); 
      String separator = "";
      if(isWindows()){
          separator = "\\";
      }else if(isLinux() || isUnix()){
          separator = "/";
      }
      logManager.logInfo("Exiting 'getDirectorySeparator()' method"); 
      return separator;
  } 
}    
