/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.ui;

import java.util.ArrayList;
import javax.swing.JCheckBox;

/**
 *
 * @author chemweno
 */
public class Common {

    private ArrayList<String> checkBoxItemsList = null;
    private ArrayList<Integer> checkBoxIntegerItemsList = null;
    private ArrayList<Integer> rowIndices = new ArrayList<>();
    private String numbersBetween = "";
    public static int currentValueRow = 0;
    public static ArrayList<Integer> averageYears;

    //Return a list of selected checkbox items in a panel. Return the item labels
    public ArrayList<String> getPanelCheckBoxItems(javax.swing.JPanel panel, boolean selectedItems) {
        int checkBoxIndex = 0;
        String checkBoxLabel = "";
        JCheckBox checkboxObject = new JCheckBox();
        checkBoxItemsList = new ArrayList<>();
        try {
            //Traverse the market names checkboxes and create market names to be used in select query
            Object[] checkboxObjects = panel.getComponents();
            while (checkBoxIndex < checkboxObjects.length) {
                checkboxObject = (JCheckBox) checkboxObjects[checkBoxIndex];
                if (checkboxObject.isSelected()) {
                    //Get check box object label
                    checkBoxLabel = checkboxObject.getText();
                    checkBoxItemsList.add(checkBoxLabel);
                }
                checkBoxIndex++;
            }
            //Remove "'All'" in arraylist if exists
            if (checkBoxItemsList.contains("ALL")) {
                checkBoxItemsList.remove("ALL");
            }
        } catch (Exception exception) {
        }
        return checkBoxItemsList;
    }

    //Return a list of selected checkbox items in a panel. Return the item labels
    public ArrayList<Integer> getPanelCheckBoxIntegerItems(javax.swing.JPanel panel, boolean selectedItems) {
        int checkBoxIndex = 0;
        String checkBoxLabel = "";
        JCheckBox checkboxObject = new JCheckBox();
        checkBoxIntegerItemsList = new ArrayList<>();
        try {
            //Traverse the market names checkboxes and create market names to be used in select query
            Object[] checkboxObjects = panel.getComponents();
            while (checkBoxIndex < checkboxObjects.length) {
                checkboxObject = (JCheckBox) checkboxObjects[checkBoxIndex];
                if (checkboxObject.isSelected()) {
                    //Get check box object label
                    checkBoxLabel = checkboxObject.getText();
                    checkBoxIntegerItemsList.add(Integer.parseInt(checkBoxLabel));
                }
                checkBoxIndex++;
            }
            //Remove "'All'" in arraylist if exists
            //if (checkBoxIntegerItemsList.contains("ALL")) {
            //    checkBoxIntegerItemsList.remove("ALL");
            //}
        } catch (Exception exception) {
        }
        return checkBoxIntegerItemsList;
    }    
    
    //Return a list of numbers between two given boundaries, inclusive
    public String getNumbersBetween(int startNumber, int endNumber) {
        try {
            while (startNumber <= endNumber) {
                numbersBetween = numbersBetween + "," + startNumber;
                startNumber++;
            }
            //Replacce first occurring comma
            numbersBetween = numbersBetween.replaceFirst(",", "");
        } catch (Exception exception) {
        }
        return numbersBetween;
    }

    //Add a row to a table column
    public void addRowToTable(javax.swing.table.DefaultTableModel tbmModel) {
        try {
            tbmModel.addRow(new Object[]{null});
        } catch (Exception exception) {
        }
    }
    //Create an arraylist containing row indices of selected years on table

    public ArrayList<Integer> getRowIndices(javax.swing.JPanel panel, javax.swing.JTable table) {
        int rowIndex = 0;
        int index = 0;
        String item = "";
        ArrayList<String> arrayList = new ArrayList<>();
        rowIndices = new ArrayList<>();
        try {
            arrayList = getPanelCheckBoxItems(panel, true);
            while (index < arrayList.size()) {
                item = arrayList.get(index);
                for (int row = 0; row < table.getRowCount(); row++) {
                    if (table.getValueAt(row, 0) != null) {
                        if (item.equalsIgnoreCase(table.getValueAt(row, 0).toString())) {
                            //Add row where value was found to array list
                            rowIndices.add(row);
                        }
                    }
                }
                index++;
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
        }
        return rowIndices;
    }

    public int getPeviousMonthRow(int currentRow, int currentColumn) {
        int previousRow = currentRow;
        try {
            if (currentColumn == 1) {
                previousRow = currentRow - 1;
            }
        } catch (Exception exception) {
        }
        return previousRow;
    }

    public int getPeviousMonthColumn(int currentColumn) {
        int previousColumn = 0;
        try {
            if (currentColumn == 1) {
                previousColumn = 12;
            }else{
                previousColumn = currentColumn - 1;
            }
        } catch (Exception exception) {
        }
        return previousColumn;
    }
    
    public int getRowIndex(String matchingFirstColumnText, javax.swing.JTable table){
        int rowIndex = -1;
        try{
           for(int row=0; row < table.getRowCount(); row++){
               
               if(table.getValueAt(row, 0) != null){                   
                   if(table.getValueAt(row, 0).toString().equalsIgnoreCase(matchingFirstColumnText) ){
                       rowIndex = row;
                   }                   
               }               
           }
        }catch(Exception exception){}
        return rowIndex;
    }    
}
