/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.settings.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class Properties {

    private String dbServer = "";
    private String postgresDbName = "";
    private String mysqlDbName = "";
    private String postgresDbUser = "";
    private String mysqlDbUser = "";
    private String postgresDbPassword = "";
    private String mysqlDbPassword = "";
    private String postgresDbHost = "";
    private String mysqlDbHost = "";
    private String postgresDbPort = "";
    private String mysqlDbPort = "";
    private String commPort = "";
    private String propertiesFileName = "nishati.properties";
    private java.util.Properties properties;
    private static LogManager logManager = new LogManager();

    public Properties() {
        //Load Properties file
        logManager.logInfo("Entering and exiting 'Properties()' constructor");
       properties = loadPropertiesFile();       
    }

    //Load file containing properties for this application
    private java.util.Properties loadPropertiesFile() {
       logManager.logInfo("Entering 'loadPropertiesFile()' method"); 
        String path = "";
        try {
            path = new File(".").getCanonicalPath();
            File propertiesFile = new File(path + "\\src\\" + propertiesFileName);
            //Check whether the file exists in the said location
            if (propertiesFile.exists()) {
                //Load properties from file
                properties = new java.util.Properties();
                FileInputStream in = new FileInputStream(propertiesFile);
                //Load properties file
                properties.load(in);
            } else {
                //System.err.println("File '" + propertiesFileName + "' not found");
                JOptionPane.showMessageDialog(null, "File '" + propertiesFileName + "' not found",
                        "File not found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            //Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
            logManager.logError("IOException was thrown and caught in 'loadPropertiesFile()' method"); 
        } catch (Exception exception) {
           //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'loadPropertiesFile()' method"); 
        }
        logManager.logInfo("Exiting 'loadPropertiesFile()' method"); 
        return properties;
    }

    public String getDbServer() {
        logManager.logInfo("Entering 'getDbServer()' method"); 
        dbServer = properties.getProperty("db_server");
        logManager.logInfo("Exiting 'getDbServer()' method"); 
        return dbServer;
    }

    public String getMysqlDbHost() {
        logManager.logInfo("Entering 'getMysqlDbHost()' method"); 
        mysqlDbHost = properties.getProperty("mysql_db_host");
        logManager.logInfo("Exiting 'getMysqlDbHost()' method"); 
        return mysqlDbHost;
    }

    public String getMysqlDbName() {
        logManager.logInfo("Entering 'getMysqlDbName()' method"); 
        mysqlDbName = properties.getProperty("mysql_db_name");     
        logManager.logInfo("Exiting 'getMysqlDbName()' method"); 
        return mysqlDbName;
    }

    public String getMysqlDbPassword() {
        logManager.logInfo("Entering 'getMysqlDbPassword()' method"); 
        mysqlDbPassword = properties.getProperty("mysql_db_password");
        logManager.logInfo("Exiting 'getMysqlDbPassword()' method"); 
        return mysqlDbPassword;
    }

    public String getMysqlDbPort() {
        logManager.logInfo("Entering 'getMysqlDbPort()' method"); 
        mysqlDbPort = properties.getProperty("mysql_db_port");
        logManager.logInfo("Exiting 'getMysqlDbPort()' method"); 
        return mysqlDbPort;
    }

    public String getMysqlDbUser() {
        logManager.logInfo("Entering 'getMysqlDbUser()' method"); 
        mysqlDbUser = properties.getProperty("mysql_db_user");
        logManager.logInfo("Exiting 'getMysqlDbUser()' method"); 
        return mysqlDbUser;
    }

    public String getPostgresDbHost() {
        logManager.logInfo("Entering 'getPostgresDbHost()' method"); 
        postgresDbHost = properties.getProperty("postgres_db_host");
        logManager.logInfo("Exiting 'getPostgresDbHost()' method"); 
        return postgresDbHost;
    }

    public String getPostgresDbName() {
        logManager.logInfo("Entering 'getPostgresDbName()' method"); 
        postgresDbName = properties.getProperty("postgres_db_name");
        logManager.logInfo("Exiting 'getPostgresDbName()' method"); 
        return postgresDbName;
    }

    public String getPostgresDbPassword() {
        logManager.logInfo("Entering 'getPostgresDbPassword()' method"); 
        postgresDbPassword = properties.getProperty("postgres_db_password");
        logManager.logInfo("Exiting 'getPostgresDbPassword()' method"); 
        return postgresDbPassword;
    }

    public String getPostgresDbPort() {
        logManager.logInfo("Entering 'getPostgresDbPort()' method"); 
        postgresDbPort = properties.getProperty("postgres_db_port");
        logManager.logInfo("Exiting 'getPostgresDbPort()' method"); 
        return postgresDbPort;
    }

    public String getPostgresDbUser() {
        logManager.logInfo("Entering 'getPostgresDbUser()' method"); 
        postgresDbUser = properties.getProperty("postgres_db_user");
        logManager.logInfo("Exiting 'getPostgresDbUser()' method"); 
        return postgresDbUser;
    }

    public java.util.Properties getProperties() {
        logManager.logInfo("Entering and exiting 'getProperties()' method"); 
        return properties;
    }

    public String getPropertiesFileName() {
        logManager.logInfo("Entering and exiting 'getPropertiesFileName()' method"); 
        return propertiesFileName;
    }  

    public String getCommPort() {
        commPort = properties.getProperty("comm_port");
        return commPort;
    }     
}
