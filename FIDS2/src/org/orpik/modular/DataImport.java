/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.modular;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;
import org.orpik.data.importt.XmlFileReader;
import org.orpik.logging.LogManager;

/**
 *
 * @author Chemweno
 */
public class DataImport {

    private static ArrayList<File> fileList = new ArrayList<>();
    private static ArrayList<String> filenamesList = new ArrayList<>();
    public static ArrayList<File> filenamesNotImportedList = new ArrayList<>();
    private XmlFileReader xmlFileReader = new XmlFileReader();
    private static LogManager logManager = new LogManager();

    public void loadXmlFiles(JPanel panel, File directory) {
        logManager.logInfo("Entering 'loadXmlFiles(JPanel panel, File directory)' method");
        ArrayList<File> filesArrayList = null;
        int arrayIndex = 0;
        try {
            filesArrayList = getXmlFilesList(directory);
            while (arrayIndex < filesArrayList.size()) {
                //Populate panel with list of files in selected folder
                panel.add(new JCheckBox(filesArrayList.get(arrayIndex).getCanonicalPath().toString(), true));
                arrayIndex++;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadXmlFiles(JPanel panel, File directory)' method");
        }
        logManager.logInfo("Exiting 'loadXmlFiles(JPanel panel, File directory)' method");
    }

    public void loadXmlFiles(JPanel panel, ArrayList<File> selectedFiles) {
        logManager.logInfo("Entering 'loadXmlFiles(JPanel panel, ArrayList<File> selectedFiles)' method");
        ArrayList<File> filesArrayList = null;
        int arrayIndex = 0;
        try {
            filesArrayList = selectedFiles;
            while (arrayIndex < filesArrayList.size()) {
                //Populate panel with list of files in selected folder
                panel.add(new JCheckBox(filesArrayList.get(arrayIndex).getCanonicalPath().toString(), true));
                arrayIndex++;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadXmlFiles(JPanel panel, ArrayList<File> selectedFiles)' method");
        }
        logManager.logInfo("Exiting 'loadXmlFiles(JPanel panel, ArrayList<File> selectedFiles)' method");
    }

    public void loadXmlFiles(JPanel panel, File[] selectedFiles) {
        logManager.logInfo("Entering 'loadXmlFiles(JPanel panel, File[] selectedFiles)' method");
        //ArrayList<File> filesArrayList = null;
        int arrayIndex = 0;
        File file = null;
        try {
            while (arrayIndex < selectedFiles.length) {
                //Populate panel with list of files in selected folder
                file = selectedFiles[arrayIndex];
                if (file.isDirectory()) {
                    //Recursively fetch files from folders
                    loadXmlFiles(panel, file.listFiles());
                } else {
                    panel.add(new JCheckBox(file.getCanonicalPath().toString(), true));
                    //Create a list of filenames 
                    filenamesList.add(file.getCanonicalPath().toString());
                }
                arrayIndex++;
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadXmlFiles(JPanel panel, File[] selectedFiles)' method");
        }
        logManager.logInfo("Exiting 'loadXmlFiles(JPanel panel, File[] selectedFiles)' method");
    }

    //Get all files in the directory tree , return an arrayList
    private ArrayList<File> getXmlFilesList(File files) {
        logManager.logInfo("Entering 'getXmlFilesList(File files)' method");
        //fileList = new ArrayList<File>();
        try {
            //Remove any existing files in list
            fileList.clear();
            for (File file : files.listFiles()) {
                if (file.isDirectory()) {
                    //Recursively call this method to iterate through directory files
                    getXmlFilesList(file);
                } else {
                    fileList.add(file);
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getXmlFilesList(File files)' method");
        }
        logManager.logInfo("Exiting 'getXmlFilesList(File files)' method");
        return fileList;
    }

    public int processXmlFiles() {
        logManager.logInfo("Entering 'processXmlFiles()' method");
        int result = 0;
        int errorCount = 0;
        //Clear any existing elements in the arraylist
        filenamesNotImportedList.clear();
        try {
            for (int fileIndex = 0; fileIndex < filenamesList.size(); fileIndex++) {
                result = xmlFileReader.readXmlFile(filenamesList.get(fileIndex));

                if (result > 0) {
                    /*
                     * JOptionPane.showMessageDialog(null, "File
                     * "+filenamesList.get(fileIndex) +" imported successflly",
                     * "Import XML data", JOptionPane.INFORMATION_MESSAGE);
                     */
                } else if (result == 0) {
                    errorCount++;
                    /*
                     * JOptionPane.showMessageDialog(null, "File
                     * "+filenamesList.get(fileIndex) +" failed to import. Year
                     * or Application Id Error", "Import XML data", JOptionPane.WARNING_MESSAGE);
                     */
                    
                    filenamesNotImportedList.add(new File(filenamesList.get(fileIndex)));
                } else if (result < 0) {
                    errorCount++;
                    /*
                     * JOptionPane.showMessageDialog(null, "File
                     * "+filenamesList.get(fileIndex) +" failed to import due to
                     * an error", "Import XML data", JOptionPane.WARNING_MESSAGE);
                     */
                    filenamesNotImportedList.add(new File(filenamesList.get(fileIndex)));
                }
            }
            if (errorCount > 0) {
                JOptionPane.showMessageDialog(null, "Some files were not successfully imported.\nPlease try again",
                        "Import XML data", JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "All files were successfully imported.",
                        "Import XML data", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'processXmlFiles()' method");
        }
        logManager.logInfo("Exiting 'processXmlFiles()' method");
        return result;
    }

    //Retain files that were not successfully imported on panel
    public void retainUnImportedFiles(javax.swing.JPanel pnl) {
        logManager.logInfo("Entering 'retainUnImportedFiles(javax.swing.JPanel pnl)' method");
        try {            
            loadXmlFiles(pnl, filenamesNotImportedList);
            pnl.repaint();
            pnl.revalidate();            
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'retainUnImportedFiles(javax.swing.JPanel pnl)' method");
        }
        logManager.logInfo("Exiting 'retainUnImportedFiles(javax.swing.JPanel pnl)' method");
    }
    
    //Remove all files in panel
//    public void clearFilesNotImportedList(){
//        try{
//            filenamesNotImportedList.clear();
//        }catch(Exception exception){}
//    }
    
    //Load XML files on table
    public void loadXmlFiles(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm, File[] selectedFiles){
        logManager.logInfo("Entering 'loadXmlFiles(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm, File[] selectedFiles)' method");
        int arrayIndex = 0;
        int rowIndex = 0;
        File file = null;
        
        try {  
            prepareDataImportTable(tbl, tbm);
            while (arrayIndex < selectedFiles.length) {
                //Insert new row
                tbm.insertRow(rowIndex, new Object[]{null});
                //Populate panel with list of files in selected folder
                file = selectedFiles[arrayIndex];
                if (file.isDirectory()) {
                    //Recursively fetch files from folders
                    loadXmlFiles(tbl, tbm, file.listFiles());
                } else {
                    //Insert filename
                    tbl.setValueAt(new JCheckBox(file.getCanonicalPath().toString(), true), rowIndex, 0);
                    //Insert file status
                    tbl.setValueAt("Waiting", rowIndex, 1);
                    //Insert file status description
                    tbl.setValueAt("Waiting importation", rowIndex, 2);
                    //Create a list of filenames 
                    filenamesList.add(file.getCanonicalPath().toString());
                }
                rowIndex++;
                arrayIndex++;
            }   
        }catch(Exception exception){
        //exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'loadXmlFiles(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm, File[] selectedFiles)' method");
        }
        logManager.logInfo("Exiting 'loadXmlFiles(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm, File[] selectedFiles)' method");
    }
    
    //Prepare data import table
    public void prepareDataImportTable(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm){
        logManager.logInfo("Entering 'prepareDataImportTable(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm)' method");
        TableColumn tbc = new TableColumn();
        try{
            tbm.setRowCount(0);
            tbm.setColumnCount(0);
            //Add columns
            tbm.addColumn("FILENAME");            
            tbm.addColumn("STATUS");
            tbm.addColumn("DESCRIPTION");
        }catch(Exception exception){
        logManager.logError("Exception was thrown and caught in 'prepareDataImportTable(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm)' method");
        }
        logManager.logInfo("Exiting 'prepareDataImportTable(javax.swing.JTable tbl, javax.swing.table.DefaultTableModel tbm)' method");
    }
}
