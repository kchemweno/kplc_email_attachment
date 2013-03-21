/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.modular;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.orpik.indicators.Indicators;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class SlimsPart2TableModel implements TableModelListener {

    private Indicators indicator = new Indicators();
    private DataUpdate dataUpdate = new DataUpdate();
    private int price = 0;
    private String priceUncast = "";
    private int row = 0;
    private int column = 0;
    private javax.swing.JComboBox cboYear = new JComboBox();
    private javax.swing.JComboBox cboMonth = new JComboBox();
    private javax.swing.JComboBox cboMarket = new JComboBox();    
    private javax.swing.JTable table;
    private TableModel model;
    public static boolean startEdit = false;
    private NumberFormat thousandFormat = new DecimalFormat("#,###");
    private static LogManager logManager = new LogManager();

    public SlimsPart2TableModel(javax.swing.JTable table) {
        logManager.logInfo("Entering 'SlimsPart2TableModel(javax.swing.JTable table)' constructor");
        model = table.getModel();
        model.addTableModelListener(this);
        this.table = table;
        logManager.logInfo("Exiting 'SlimsPart2TableModel(javax.swing.JTable table)' constructor");
    }
    public SlimsPart2TableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth) {
        logManager.logInfo("Entering 'SlimsPart2TableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth)' constructor");
        model = table.getModel();
        model.addTableModelListener(this);
        this.table = table;
        this.cboMarket = cboMarket;
        this.cboYear = cboYear;
        this.cboMonth = cboMonth;
        logManager.logInfo("Exiting 'SlimsPart2TableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth)' constructor");
    }    

    @Override
    public void tableChanged(TableModelEvent e) {
        logManager.logInfo("Entering 'tableChanged(TableModelEvent e)' method");
        int indicatorId = 0;
        int indicatorValue = 0;
        int result = 0;
        String databaseTableColumnName = "";
        String newValue = "";
        String civilInsecurityLevel = "";
        //String indicatorValue = "";
        try {
            //if (startEdit) {//if the table is in edit mode,save the changes
            row = e.getFirstRow();
            column = e.getColumn();
            //String columnName = model.getColumnName(column);
            //consumption of invalid data should only happen in column 1

            Object data = model.getValueAt(row, column);
            if (data != null) {
                //Filter out objects of type boolean
                if (!data.getClass().getName().equalsIgnoreCase("Boolean")) {
                    //For slims part 2 data entry table
                    //if (table.equals(tblDataEntrySlimsPart2MiddleMid) && column == 1) {
                    if (column == 1) {                                               
                        priceUncast = data.toString();
                        //Check for civil insecurity row
                        if(table.getValueAt(row, 0).toString().equalsIgnoreCase("Level Of Civil Insecurity")){
                            civilInsecurityLevel = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : "";
                        if (!civilInsecurityLevel.equalsIgnoreCase("")) {
                            indicatorValue = indicator.getCivilInsecurityLevelId(civilInsecurityLevel);
                        } else {
                            indicatorValue = -1;
                        }                            
                        }else{
                        //price = (Integer) data;
                        indicatorValue = Integer.parseInt(priceUncast);
                        }
                        if (indicatorValue < 0) {//0 is allowed here since we are not dealing with prices but with values
                            switch (indicatorValue) {
                                case -1:
                                    ;
                                    break;
                                case -99:
                                    ;
                                    //Allowed: Market inaccessible due to insecurity
                                    break;
                                case -98:
                                    ;
                                    //Allowed: Commodity not available in the market at that time
                                    break;
                                case -88:
                                    ;
                                    //Allowed
                                    break;
                                default:

                                    JOptionPane.showMessageDialog(null, "The value entered is invalid,only positive values and a "
                                            + "few chosen negative values are allowed."
                                            + "\nPlease make changes to proceed", "Invalid data entered", JOptionPane.ERROR_MESSAGE);
                                    //Consume value entered by user
                                    model.setValueAt(null, row, column);
                            }
                        } else {
                            //Introduce thousand separator if price > 999
                            if (indicatorValue > 999) {
                                model.setValueAt(thousandFormat.format(indicatorValue), row, column);
                            }
                        }
                    }
                }
                //Update value
                if (startEdit && column != 6 && indicatorValue >=0) {
                    indicatorId = indicator.getIndicatorId(table.getValueAt(row, 0).toString());     
                    if (column == 1) {
                        result = dataUpdate.updateMarketData(cboYear, cboMonth, cboMarket, indicatorId, indicatorValue, column);                        
                    } else {
                        if (table.getColumnName(column).toString().equalsIgnoreCase("LOCATION NAME")) {
                            databaseTableColumnName = "location_name";                           
                        } else if (table.getColumnName(column).toString().equalsIgnoreCase("KEY INFORMANT")) {
                            databaseTableColumnName = "key_informant";
                        } else if (table.getColumnName(column).toString().equalsIgnoreCase("TRIANGULATION")) {
                            databaseTableColumnName = "triangulation";
                        } else if (table.getColumnName(column).toString().equalsIgnoreCase("DATA TRUST LEVEL")) {
                            databaseTableColumnName = "data_trust_level";
                        }
                        //Update details if indicator value exists
                        if(table.getValueAt(row, 1) !=null){
                        //Get new value
                         newValue = table.getValueAt(row, column) !=null ?  table.getValueAt(row, column).toString() : "";
                        //Execute update statement
                        dataUpdate.updateSlimPart2Details(cboYear, cboMonth, cboMarket, indicatorId, newValue, databaseTableColumnName);
                    }
                    }
                }
            }
            //}
        } catch (ClassCastException classCastException) {
            JOptionPane.showMessageDialog(null, "The value entered is invalid,Only numbers are allowed for this cell",
                    "Invlaid Value Entered", JOptionPane.ERROR_MESSAGE);
            //classCastException.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'tableChanged(TableModelEvent e)' method");
        } catch (NumberFormatException numberFormatException) {
            logManager.logInfo("Entering 'tableChanged(TableModelEvent e)' method");
            if (!isNumberCastable(priceUncast)) {
                model.setValueAt(null, row, column);
            }
        } catch (Exception exception) {
            //System.err.println("Error occurred in tableChanged() method: " + exception.getMessage());
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'tableChanged(TableModelEvent e)' method");
        }
        logManager.logInfo("Exiting 'tableChanged(TableModelEvent e)' method");
    }

    private boolean isNumberCastable(String number) {
        logManager.logInfo("Entering 'isNumberCastable(String number)' method");
        boolean numberCastable = false;
        int castNumber = 0;
        try {
            if (model.getValueAt(row, 0).toString().equalsIgnoreCase("Level Of Civil Insecurity") && column == 1) {
                numberCastable = true;
            } else if (!number.equals("")) {
                number = number.replace(",", "");
                castNumber = Integer.parseInt(number);
                numberCastable = true;
            }

        } catch (NumberFormatException numberFormatException) {
            logManager.logInfo("Entering 'isNumberCastable(String number)' method");
        } catch (Exception excxeption) {
        logManager.logError("Exception was thrown and caught in 'isNumberCastable(String number)' method");
        }
        logManager.logInfo("Exiting 'isNumberCastable(String number)' method");
        return numberCastable;
    }

    public int getRowNumber() {
        logManager.logInfo("Entering and exiting'getRowNumber()' method");
        return row;
    }

    public int getColumnNumber() {
        logManager.logInfo("Entering and exiting'getColumnNumber()' method");
        return column;
    }

    public int getCellValue() {
        logManager.logInfo("Entering and exiting'getCellValue()' method");
        return price;
    }
}
