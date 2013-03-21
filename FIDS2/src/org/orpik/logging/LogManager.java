/*
 * This class will be called instantiated and called from any class that
 * logs information or errors
 */

package org.orpik.logging;

import org.apache.log4j.Logger;
/**
 *
 * @author kiprotich
 */
public class LogManager {
    private static Logger infoLogger = Logger.getLogger("Information");
    private static Logger errorLogger = Logger.getLogger("Error");
    private static Logger warningLogger = Logger.getLogger("Warning");
    private static Logger queryLogger = Logger.getLogger("Query");

    //Log Information, used for recording successful process information
    public void logInfo(Object message) {
        infoLogger.info(message.toString());
    }
    //Log Errors, any errors and exceptions thrown
    public void logError(Object message) {
        errorLogger.error(message.toString());
    }
    //Log warnings
    public void logWarning(Object message) {
        warningLogger.warn(message.toString());
    }
    //Log warnings
    public void logQuery(Object message) {
        queryLogger.warn(message.toString());
    }
}
