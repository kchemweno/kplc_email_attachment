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
import org.orpik.indicators.CategoryRows;
import org.orpik.indicators.IndicatorRows;
import org.orpik.indicators.Indicators;
import org.orpik.location.Market;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class SlimsPart1TableModel implements TableModelListener {

    private Market market = new Market();
    private Indicators indicator = new Indicators();
    private int price = 0;
    private String priceUncast = "";
    private int row = 0;
    private int column = 0;
    private javax.swing.JTable table;
    private javax.swing.JComboBox cboYear = new JComboBox();
    private javax.swing.JComboBox cboMonth = new JComboBox();
    private javax.swing.JComboBox cboMarket = new JComboBox();
    private TableModel model;
    public static boolean startEdit = false;
    private NumberFormat thousandFormat = new DecimalFormat("#,###");
    private SlimsOneIndicatorRows slimsOneRowsArrayList = new SlimsOneIndicatorRows();
    private DataUpdate dataUpdate = new DataUpdate();
    private int year = 0;
    private int month = 0;
    private int marketId = 0;
    private static LogManager logManager = new LogManager();
    //  private DataEntry dataEntry = new DataEntry();

    public SlimsPart1TableModel(javax.swing.JTable table) {
        logManager.logInfo("Entering 'SlimsPart1TableModel(javax.swing.JTable table)' constructor");
        model = table.getModel();
        model.addTableModelListener(this);
        this.table = table;
        logManager.logInfo("Exiting 'SlimsPart1TableModel(javax.swing.JTable table)' constructor");
    }

    public SlimsPart1TableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth) {
        logManager.logInfo("Entering 'SlimsPart1TableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth)' constructor");
        model = table.getModel();
        model.addTableModelListener(this);
        this.table = table;
        //this.year = Integer.parseInt(cboYear.getSelectedItem().toString());
        //this.month = cboMonth.getSelectedIndex() + 1;
        //this.marketId = market.getMarketId(cboMarket.getSelectedItem().toString());
        this.cboMarket = cboMarket;
        this.cboYear = cboYear;
        this.cboMonth = cboMonth;
        logManager.logInfo("Exiting 'SlimsPart1TableModel(javax.swing.JTable table, JComboBox cboMarket, JComboBox cboYear, JComboBox cboMonth)' constructor");
    }
    
    @Override
    public void tableChanged(TableModelEvent e) {
        logManager.logInfo("Entering 'tableChanged(TableModelEvent e)' method");
        String detail = "";
        String newValue = "";
        int categoryId = 0;
        int indicatorId = 0;
        try {
            //if (startEdit) {//if the table is in edit mode,save the changes
            row = e.getFirstRow();
            column = e.getColumn();
            //Consumption of invalid data should only happen in column 1
            //if (startEdit) {
                Object data = model.getValueAt(row, column);
                if (data != null) {
                    //Filter out objects of type boolean
                    if (!data.getClass().getName().equalsIgnoreCase("Boolean")) {
                        //For slims part 2 data entry table
                        /*
                         * Formating of the table row depends on the expected
                         * value of the row, rows whose 5th column is editable
                         * are expected to store integer values and therefore
                         * are formatted with thousand separator
                         */
                        if (column != 0 && table.isCellEditable(row, table.getColumnCount() - 1)) {
                            priceUncast = data.toString();
                            //price = (Integer) data;
                            price = Integer.parseInt(priceUncast);
                            if (price < 1) {
                                switch (price) {
                                    case -99:;                                        ;
                                        //Allowed: Market inaccessible due to insecurity
                                        break;
                                    case -98:;                                        
                                        //Allowed: Commodity not available in the market at that time
                                        break;
                                    case -88:;                                        
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
                                if (price > 999) {
                                    model.setValueAt(thousandFormat.format(price), row, column);
                                }
                            }
                        }
                    }
                }
                if(startEdit){
                    if(IndicatorRows.getSlimsPart1IndicatorRowsList().contains(row)){
                        //Slims part 1 price update
                        indicatorId = indicator.getIndicatorId(table.getValueAt(row, 0).toString());
                        price = data.toString().contains(",") ?  Integer.parseInt(data.toString().replaceAll(",", "").trim()) :
                                Integer.parseInt(data.toString().trim());
                        dataUpdate.updateMarketData(cboYear, cboMonth, cboMarket, indicatorId, price, column);
                    }else{
                    //All the non-price updates will be done from column 1
                    if(table.getValueAt(row, 0).toString().startsWith("LOCATI")){
                        //Location Name
                        detail = "location_name";
                        newValue = data.toString();
                        categoryId = CategoryRows.getSlimsPart1TableRowCategoryId().get(row);
                        dataUpdate.updateSlimPart1Details(cboYear, cboMonth, cboMarket, categoryId, newValue, detail);
                    }else if(table.getValueAt(row, 0).toString().startsWith("KEY")){
                        //Key Informant
                        detail = "key_informant";
                        newValue = data.toString();
                        categoryId = CategoryRows.getSlimsPart1TableRowCategoryId().get(row);
                        dataUpdate.updateSlimPart1Details(cboYear, cboMonth, cboMarket, categoryId, newValue, detail);
                    }else if(table.getValueAt(row, 0).toString().startsWith("TRIANGU")){
                        //Triangulation
                        detail = "triangulation";
                        newValue = data.toString();
                        categoryId = CategoryRows.getSlimsPart1TableRowCategoryId().get(row);
                        dataUpdate.updateSlimPart1Details(cboYear, cboMonth, cboMarket, categoryId, newValue, detail);                        
                    }else if(table.getValueAt(row, 0).toString().startsWith("DATA")){
                        //Data Trust Level
                        detail = "data_trust_level";
                        newValue = data.toString();
                        categoryId = CategoryRows.getSlimsPart1TableRowCategoryId().get(row);
                        dataUpdate.updateSlimPart1Details(cboYear, cboMonth, cboMarket, categoryId, newValue, detail);                        
                    }else if(table.getValueAt(row, 0).toString().startsWith("Acti")){
                        //Activity, Agricultural and Non-Agricultural labour rate
                        if(table.getValueAt(row - 1, 0).equals("Labor Rate Agricultural")){
                            //Labor Rate Agricultural
                            detail = "agricultural_activity";
                            newValue = data.toString();
                            dataUpdate.updateSlimPart1Lookup(cboYear, cboMonth, cboMarket, newValue, detail);
                        }else if(table.getValueAt(row - 1, 0).equals("Labor Rate Non-Agricultural")){
                            //Labor Rate Non-Agricultural   
                            detail = "non_agricultural_activity";
                            newValue = data.toString();
                            dataUpdate.updateSlimPart1Lookup(cboYear, cboMonth, cboMarket, newValue, detail);
                        }
                    }else if(table.getValueAt(row, 0).toString().startsWith("Mean")){
                        //Means
                        detail = "transport_means";
                        newValue = data.toString();
                        dataUpdate.updateSlimPart1Lookup(cboYear, cboMonth, cboMarket, newValue, detail);
                    }else if(table.getValueAt(row, 0).toString().startsWith("Commodit")){
                     //Commodity Transported   
                        detail = "transport_commodity";
                        newValue = data.toString();
                        dataUpdate.updateSlimPart1Lookup(cboYear, cboMonth, cboMarket, newValue, detail);
                    }
                    //dataUpdate.updateMarketData
                }
                }                
            //}
        } catch (ClassCastException classCastException) {
            logManager.logError("ClassCastException was thrown and caught in 'tableChanged(TableModelEvent e)' method");
            JOptionPane.showMessageDialog(null, "The value entered is invalid,Only numbers are allowed for this cell",
                    "Invlaid Value Entered", JOptionPane.ERROR_MESSAGE);
            //classCastException.printStackTrace();
        } catch (NumberFormatException numberFormatException) {
            logManager.logError("NumberFormatException was thrown and caught in 'tableChanged(TableModelEvent e)' method");
            if (!isNumberCastable(priceUncast)) {
                model.setValueAt(null, row, column);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'tableChanged(TableModelEvent e)' method");
            //System.err.println("Error occurred in tableChanged() method: " + exception.getMessage());
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'tableChanged(TableModelEvent e)' method");
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
            logManager.logInfo("Entering 'isNumberCastable(String number)' method");
        } catch (Exception exeption) {
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
