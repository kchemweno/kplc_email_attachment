/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.validation;

/**
 *
 * @author chemweno
 */
public class RangeValidation {
    public boolean isMarketUpdateFiveYearAverageValid(int startYear, int endYear){
        boolean isValid = false;
        try{
             isValid = endYear - startYear == 4 ? true : false;
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return isValid;
    }
}
