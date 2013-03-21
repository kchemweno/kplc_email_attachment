package org.orpik.data.export;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.orpik.logging.LogManager;

/**
 * This class is now JDK 1.6 Dependent, with the addition of the use of the Desktop
 * class instead of the default application launch procedure, and the use of the new
 * FileNameExtensionFilter class introduced under javax.swing.filechooser.
 *
 * This class can be used in one line of code by passing the appropriate JTable to the
 * desired constructor.
 */
public class ExcelExporter extends Object {
    public JTable source;
    public JFileChooser chooser;
    public File csvFile;
    private boolean cancelOp = false, isDefault = true;
    private String topText = "";
    private static LogManager logManager = new LogManager();

    public ExcelExporter(JTable source) {
        this(source, "");
        logManager.logInfo("Just ran 'ExcelExporter(JTable source)' constructor");
    }

    public ExcelExporter(JTable source, String topText) {
        this(source, topText, true);
        logManager.logInfo("Just ran 'ExcelExporter(JTable source, String topText)' constructor");
    }

    public ExcelExporter(JTable source, String topText, boolean isDefault, File target, JProgressBar bar) {
        super();
        this.source = source;
        this.topText = topText;
        this.isDefault = isDefault;

        new ProgressDialog(source, target, bar);
        logManager.logInfo("Just ran 'ExcelExporter(JTable source, String topText, boolean isDefault, File target, JProgressBar bar)' constructor");
    }

    public ExcelExporter(JTable source, String topText, boolean isDefault) {
        super();
        this.source = source;
        this.topText = topText;
        this.isDefault = isDefault;
        obtainFileName();
        logManager.logInfo("Just ran 'ExcelExporter(JTable source, String topText, boolean isDefault)' constructor");
    }

    public void obtainFileName() {
        logManager.logInfo("Entering 'obtainFileName()' method");
        cancelOp = false;

        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV file format", "csv");
        
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setDialogTitle("Saving File");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setSelectedFile(new File("database.csv"));
            chooser.setAcceptAllFileFilterUsed(false);
        }

        int val = chooser.showSaveDialog((Component) null);

        if (val == JFileChooser.APPROVE_OPTION) {
            csvFile = chooser.getSelectedFile();
            boolean fixed = fixExtension(csvFile, "csv");

            if (!fixed && !cancelOp) {
                JOptionPane.showMessageDialog(null, "File Name Specified Not Supported",
                        "File Name Error", JOptionPane.ERROR_MESSAGE);
                obtainFileName();
                return;
            }

            if (!cancelOp) {
                //storeTableAsCSV(csvFile, source);
                new ProgressDialog(source, csvFile).setVisible(true);
            }
        }
        logManager.logInfo("Exiting 'obtainFileName()' method");
    }

    public boolean fixExtension(File file, String prefExt) {
        logManager.logInfo("Entering 'fixExtension(File file, String prefExt)' method");
        String fileName = file.getName();
        String dir = file.getParentFile().getAbsolutePath();

        String ext = null;

        try {
            ext = fileName.substring(fileName.lastIndexOf("."), fileName.length());
         //   System.out.println("Original File Extension: " + ext);
        } catch (StringIndexOutOfBoundsException e) {
            ext = null;
        }

        if (ext != null && !ext.equalsIgnoreCase("." + prefExt)) {
            return false;
        }

        String csvName = null;

        if (ext == null || ext.length() == 0) {
            csvName = fileName + "." + prefExt;
        } else {
            csvName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + prefExt;
        }

       // System.out.println("Corrected File Name: " + csvName);

        File csvCert = new File(dir, csvName);

        if (csvCert.exists()) {
            int val = JOptionPane.showConfirmDialog(null, "Replace Existing File?", "File Exists",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (val == JOptionPane.NO_OPTION) {
                obtainFileName();
                cancelOp = true;
                return false;
            } else if (val == JOptionPane.CANCEL_OPTION) {
                cancelOp = true;
                return false;
            }
        }

        if (!file.renameTo(csvCert)) {
            file = new File(dir, csvName);
            try {
                file.createNewFile();
            } catch (IOException ioe) {
            }
        }

        //System.out.println("Exporting as: " + file.getAbsolutePath());
        logManager.logInfo("Exiting 'fixExtension(File file, String prefExt)' method");
        return true;
    }

    public void storeTableAsCSV(File target, JTable src) {
        logManager.logInfo("Entering 'storeTableAsCSV(File target, JTable src)' method");
        System.out.println("Entered...");
        String csvData = topText + "\n\n";
        for (int i = 0; i < src.getModel().getRowCount(); i++) {
            for (int x = 0; x < src.getModel().getColumnCount(); x++) {
                int col = src.convertColumnIndexToView(x);
                String curVal = (String) src.getModel().getValueAt(i, col);

                if (curVal == null) {
                    curVal = "";
                }

                csvData = csvData + removeAnyCommas(curVal) + ",";

                if (isDefault) {
                   if (x == src.getModel().getColumnCount() - 3) {                     
                        csvData = csvData + "\n";
                        continue;
                    }
                } else {
                    if (x == src.getModel().getColumnCount() - 1) {
                        csvData = csvData + "\n";
                    }
                }
            }
        }

        try {
            FileWriter writer = new FileWriter(target);
            writer.write(csvData);
            writer.flush();
            writer.close();

            writer = null;
            csvData = null;
        } catch (IOException ioe) {
            logManager.logInfo("Exception was thrown and caught in 'storeTableAsCSV(File target, JTable src)' method");
            JOptionPane.showMessageDialog(source, "Error Writing File.\nFile may be in use by another application."
                    + "\nCheck and try re-exporting", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
        logManager.logInfo("Exitng 'storeTableAsCSV(File target, JTable src)' method");
    }

    public String removeAnyCommas(String src) {
        logManager.logInfo("Entering 'removeAnyCommas(String src)' method");
        if (src == null) {
            return "";
        }

        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == ',') {
                src = src.substring(0, i) + src.substring(i + 1, src.length());
            }
        }
        logManager.logInfo("Exiting 'removeAnyCommas(String src)' method");
        return src;
    }

    class ProgressDialog extends JDialog {
        
        JProgressBar progress;
        JLabel progressLabel;
        javax.swing.Timer timer;
        int rowPoint = -1;
        StringBuffer data;
        JTable srcTable;
        DefaultTableModel srcModel;
        File targetFile;

        public ProgressDialog(JTable src, File target) {
            super(new JDialog(), "Exporting", true);
            createUI();
            setLocationRelativeTo(null);
            srcTable = src;
            srcModel = (DefaultTableModel) src.getModel();
            targetFile = target;
            exportDataProgressively();
        logManager.logInfo("Exiting 'ProgressDialog(JTable src, File target)' method");            
        }

        public ProgressDialog(JTable src, File target, JProgressBar progress) {
            super();
            srcTable = src;
            srcModel = (DefaultTableModel) src.getModel();
            targetFile = target;
            setProgressBar(progress);
            exportDataProgressively();
            logManager.logInfo("Exiting 'ProgressDialog(JTable src, File target, JProgressBar progress)' method");            
        }

        public void createUI() {
            logManager.logInfo("Entering 'createUI()' method");            
            progress = new JProgressBar(0, 100);
            progress.setIndeterminate(true);
            progress.setValue(0);
            progress.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    UIManager.getBorder("ProgressBar.border")));
            progress.setPreferredSize(new Dimension(300, 38));

            progressLabel = new JLabel("Writing Excel Format. Please Wait...");
            progressLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
            progressLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel progressPanel = new JPanel(new BorderLayout());
            progressPanel.add(progressLabel, BorderLayout.NORTH);
            progressPanel.add(progress, BorderLayout.CENTER);
            progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            setContentPane(progressPanel);
            pack();
            logManager.logInfo("Exiting 'createUI()' method");            
        }

        public void exportDataProgressively() {
            logManager.logInfo("Entering 'exportDataProgressively()' method");            
            //progress.setString("");
            progress.setMaximum(srcModel.getRowCount());
            progress.setIndeterminate(false);

            data = new StringBuffer("" //Autogenerated Excel Format
                    + topText + "\n\n");

            timer = new javax.swing.Timer(15, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (rowPoint > -1 && srcModel.getValueAt(rowPoint, 0) == null) {
                        timer.stop();
                    }

                    progress.setValue(rowPoint);
                    appendData();
                }
            });

            timer.setRepeats(false);
            timer.start();
            logManager.logInfo("Exiting 'exportDataProgressively()' method");            
        }

        public void appendData() {
            logManager.logInfo("Entering 'appendData()' method");            
            timer.stop();

            for (int j = 0; j < srcModel.getColumnCount(); j++) {
                int col = srcTable.convertColumnIndexToView(j);

                if (rowPoint == -1) {
                    data.append(removeAnyCommas((String) srcModel.getColumnName(col)).toUpperCase());
                } else if (col != -1) {

                    //String text = (String)srcModel.getValueAt(rowPoint,col);
                    String text = "";
                    if (srcModel.getValueAt(rowPoint, col) != null) {
                        text = srcModel.getValueAt(rowPoint, col).toString();
                    }
                    if (srcModel.getColumnName(col).equalsIgnoreCase("Phone Number(s)") && !isPrintableText(text)) {
                        text = "\'" + getPrintableText(text);
                    } else if (srcModel.getColumnName(col).equalsIgnoreCase("Phone Number(s)") && isPrintableText(text)) {
                        text = "\'" + removeAnyCommas(text);
                    } else {
                        text = removeAnyCommas(text);
                    }

                    data.append(text);
                }

                if (isDefault) {
                  //  if (j != srcModel.getColumnCount() - 3) {
                     if (j != srcModel.getColumnCount() - 1) {
                        data.append(",");
                    } else {
                        data.append("\n");
                        break;
                    }
                } else {
                    if (j != srcModel.getColumnCount() - 1) {
                        data.append(",");
                    } else {
                        data.append("\n");
                    }
                }

            }


            rowPoint++;

            if (rowPoint < srcModel.getRowCount()) {
                timer.start();
            } else {
                try {
                    if (!targetFile.exists()) {
                        targetFile.createNewFile();
                    }

                    FileWriter writer = new FileWriter(targetFile);
                    writer.write(data.toString());
                    writer.close();

                    progress.setValue(progress.getMaximum());
                    progress.setStringPainted(true);
                    progress.setString("Done");

                    openFile(targetFile);

                    setVisible(false);

                    data = null;
                } catch (IOException ioe) {
                    logManager.logError("IOException was thrown and caught in 'appendData()' method");            
                    JOptionPane.showMessageDialog(source, "Error Writing File. Try Resaving",
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exc) {
                    logManager.logError("Exception was thrown and caught in 'appendData()' method");            
                    //System.out.println(exc);
                }
            }
            logManager.logInfo("Exiting 'appendData()' method");            
        }

        public void setProgressBar(JProgressBar bar) {
            logManager.logInfo("Entering 'setProgressBar(JProgressBar bar)' method");            
            progress = bar;
            logManager.logInfo("Exiting 'setProgressBar(JProgressBar bar)' method");            
        }

        public boolean isPrintableText(String text) {
            logManager.logInfo("Entering and exiting 'isPrintableText(String text)' method");            
            return !text.contains("::");
        }

        public String getPrintableText(String text) {
            logManager.logInfo("Entering 'getPrintableText(String text)' method");            
            try {
                text = text.substring(2);
            } catch (StringIndexOutOfBoundsException sie) {
                logManager.logError("StringIndexOutOfBoundsException was thrown and caught in 'getPrintableText(String text)' method");            
               // System.out.println(sie);
            }

            String generated = "";
            StringTokenizer st = new StringTokenizer(text, ",");

            try {
                for (int i = 0; st.hasMoreTokens(); i++) {
                    if (i > 0) {
                        generated = generated + " / ";
                    }

                    String token = st.nextToken();
                    generated = generated + token.substring(token.indexOf("-") + 2);
                }
            } catch (NoSuchElementException nse) {
                logManager.logError("NoSuchElementException was thrown and caught in 'getPrintableText(String text)' method");            
            }
            logManager.logInfo("Exiting 'getPrintableText(String text)' method");            
            return generated;
        }

        public void openFile(File file) {
            logManager.logInfo("Entering 'openFile(File file)' method");
            int val = JOptionPane.showConfirmDialog(null, "Would You Like To View The File Right Now?",
                    "View File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            try {
                if (val == JOptionPane.YES_OPTION) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        //  new ExcelExecutor(file.getAbsolutePath());
                    }
                }
            } catch (IOException ioe) {
                logManager.logError("IOException was thrown and caught in 'openFile(File file)' method");
                JOptionPane.showMessageDialog(null, "Failed to Open File", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            logManager.logInfo("Exiting 'openFile(File file)' method");
        }
    }
}