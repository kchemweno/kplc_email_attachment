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
import org.orpik.location.Market;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class MarketsTableModel implements TableModelListener {

    private DataUpdate dataUpdate = new DataUpdate();
    private Market market = new Market();
    private Indicators indicator = new Indicators();
    private EditTrack editTrack = new EditTrack();
    private int price = 0;
    private int row = 0;
    private int column = 0;
    private int firstColumn = 0;
    private int lastColumn = 0;
    private int year = 0;
    private int month = 0;
    private int marketId = 0;
    private int supplyId = 0;
    private int indicatorId = 0;
    private javax.swing.JTable table;
    private javax.swing.JComboBox cboYear = new JComboBox();
    private javax.swing.JComboBox cboMarket = new JComboBox();
    private javax.swing.JComboBox cboMonth = new JComboBox();
    private TableModel model;
    private String priceUncast = "";
    private static boolean startEdit = false;
    private NumberFormat thousandFormat = new DecimalFormat("#,###");
    private static LogManager logManager = new LogManager();

    public MarketsTableModel() {
    }

    public MarketsTableModel(javax.swing.JTable table, int firstPriceColumn, int lastPriceColumn) {
        model = table.getModel();
        model.addTableModelListener(this);
        this.table = table;
        this.lastColumn = lastPriceColumn;
        this.firstColumn = firstPriceColumn;
    }

    public MarketsTableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth,
            int firstPriceColumn, int lastPriceColumn) {
        model = table.getModel();
        model.addTableModelListener(this);
        this.cboMarket = cboMarket;
        this.cboYear = cboYear;
        this.cboMonth = cboMonth;
        this.table = table;
        this.lastColumn = lastPriceColumn;
        this.firstColumn = firstPriceColumn;
        this.year = Integer.parseInt(cboYear.getSelectedItem().toString());
        this.month = cboMonth.getSelectedIndex() + 1;
        this.marketId = market.getMarketId(cboMarket.getSelectedItem().toString());
        //startEdit = startEdit;
    }

    public static boolean isStartEdit() {
        return startEdit;
    }

    public static void setStartEdit(boolean startEditStatus) {
        startEdit = startEditStatus;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        logManager.logInfo("Entering 'tableChanged(TableModelEvent e)' method");
        try {
            //if (this.startEdit) {//if the table is in edit mode,save the changes
            //if (MarketsTableModel.startEdit) {
                row = e.getFirstRow();
                column = e.getColumn();
                //Consumption of invalid data should only happen in all columns except column 6

                Object data = model.getValueAt(row, column);
                if (data != null) {
                    //Filter out objects of type boolean
                    if (!data.getClass().getName().equalsIgnoreCase("Boolean")) {
                        //For markets data entry table                        
                        if (column >= firstColumn && column <= lastColumn) {
                            //Replace comma separator for thousand format
                            //priceUncast = data.toString().contains(",") ? data.toString().replaceAll(",", "") : data.toString();
                            priceUncast = data.toString();
                            price = Integer.parseInt(priceUncast);
                            //saveMarketChanges(getRowNumber(), getColumnNumber(), getCellValue(), this.table);
                            if (price < 1) {
                                switch (price) {
                                    case 0:
                                        JOptionPane.showMessageDialog(null, "The value entered is invalid,0 prices are not allowed."
                                                + "\nPlease enter a valid value", "Invalid Value Entered", JOptionPane.ERROR_MESSAGE);
                                        //Consume value entered by user
                                        model.setValueAt(null, row, column);
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
                                        JOptionPane.showMessageDialog(null, "The value entered is invalid,negative values are not allowed."
                                                + "\nPlease enter a valid value", "Invalid Value Entered", JOptionPane.ERROR_MESSAGE);
                                        //Consume value entered by user
                                        model.setValueAt(null, row, column);
                                }
                            } else {
                                // if (this.startEdit) {
                                 if (MarketsTableModel.startEdit) {
                                //Get indicator Id
                                indicatorId = indicator.getIndicatorId(this.table.getValueAt(row, 0).toString());
                                //Update saved value in database
                                dataUpdate.updateMarketData(cboYear, cboMonth, cboMarket, indicatorId, price, column);
                                //  }                               
                                }
                                //Introduce thousand separator if price > 999
                                if (price > 999) {
                                   model.setValueAt(thousandFormat.format(price), row, column);
                                }                                 
                            }
                        }
                    }
                }
            //}
            //if (price > 999) {
             //   model.setValueAt(thousandFormat.format(price), row, column);
            //}
        } catch (ClassCastException classCastException) {
            //classCastException.printStackTrace();
            logManager.logError("ClassCastException was thrown and caught in 'tableChanged(TableModelEvent e)' method");
        } catch (NumberFormatException numberFormatException) {
            logManager.logError("NumberFormatException was thrown and caught in 'tableChanged(TableModelEvent e)' method");
            if (!isNumberCastable(priceUncast)) {
                model.setValueAt(null, row, column);
            }
        } catch (Exception exception) {
            System.err.println("Error occurred in tableChanged() method: " + exception.getMessage());
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'tableChanged(TableModelEvent e)' method");
        }
        logManager.logInfo("Exiting 'tableChanged(TableModelEvent e)' method");
    }
    
private void separateThousanths(javax.swing.JTable table, int startColumn, int endColumn){
    logManager.logInfo("Entering 'separateThousanths(javax.swing.JTable table, int startColumn, int endColumn)' method");
    try{
        for(int col=column;col<=endColumn;col++){
            for(int row=0;row<table.getRowCount();row++){
                model.setValueAt(thousandFormat.format(price), row, col);
            }
        }
    }catch(Exception exception){
    logManager.logError("Exception was thrown and caught in 'separateThousanths(javax.swing.JTable table, int startColumn, int endColumn)' method");
    }
    logManager.logInfo("Exiting 'separateThousanths(javax.swing.JTable table, int startColumn, int endColumn)' method");
}

    private boolean isNumberCastable(String number) {
        logManager.logInfo("Entering 'isNumberCastable(String number)' method");
        boolean numberCastable = false;
        int castNumber = 0;
        try {
            if (!number.equals("")) {
                number = number.replace(",", "");
                castNumber = Integer.parseInt(number);
                numberCastable = true;
            }
        } catch (NumberFormatException numberFormatException) {
            logManager.logError("NumberFormatException was thrown and caught in 'isNumberCastable(String number)' method");
        } catch (Exception excxeption) {
            logManager.logError("Exception was thrown and caught in 'isNumberCastable(String number)' method");
        }
        logManager.logInfo("Exiting 'isNumberCastable(String number)' method");
        return numberCastable;
    }

    public int getRowNumber() {
        logManager.logInfo("Entering and exiting 'getRowNumber()' method");
        return row;
    }

    public int getColumnNumber() {
        logManager.logInfo("Entering and exiting 'getColumnNumber()' method");
        return column;
    }

    public int getCellValue() {
        logManager.logInfo("Entering and exiting 'getCellValue()' method");
        return price;
    }
}
