/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.util.Calendar;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.orpik.data.QueryBuilder;
import org.orpik.location.Market;
import org.orpik.logging.LogManager;
import org.orpik.modular.DataEntry;
import org.orpik.modular.EditTrack;
import org.orpik.modular.MarketsTableModel;
import org.orpik.modular.SlimsPart1TableModel;
import org.orpik.modular.SlimsPart2TableModel;
import org.orpik.period.Month;
import org.orpik.period.Year;
import org.orpik.settings.global.GuiManager;

/**
 *
 * @author Chemweno
 */
public class DataCleaningUi extends javax.swing.JInternalFrame {

    private DefaultTableModel tbmDataEntryMarkets = new DefaultTableModel();
    private DefaultTableModel tbmDataEntrySlimsPart1 = new DefaultTableModel();
    private DefaultTableModel tbmDataEntrySlimsPart2 = new DefaultTableModel();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private GuiManager guiManager = new GuiManager();
    private DataEntry dataEntry = new DataEntry();
    private Market market = new Market();
    private Year year = new Year();
    private Month month = new Month();
    private EachRowEditor rowEditor;
    private int width = 1082;
    private int height = 554;
    private boolean startEdit = false;
    private EditTrack editTrack = new EditTrack();
    private static LogManager logManager = new LogManager();

    /**
     * Creates new form DataEntryInterface
     */
    public DataCleaningUi(java.awt.Frame parent, boolean modal) {
        logManager.logInfo("Entering 'DataCleaningUi(java.awt.Frame parent, boolean modal)' constructor");
        /*
         * super(parent, modal);
         */
        initComponents();
        //Allow Data Entry dialog to resize
        this.setResizable(true);
        //Dispose Data Entry dialog on close
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Set size of Data Entry dialog
        /*
         * guiManager.setDialogSize(this, width, height);
         */
        //Center Data Entry dialog on screen
        /*
         * guiManager.centerDialog(this, width, height);
         */
        //Set year combo box selected value
        //cboDataEntryMarketsYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));        
        //Prepare markets data entry table
        dataEntry.prepareDataEntryTable("markets", tbmDataEntryMarkets, tblDataCleaningMarkets, 9, "1,3", getSupplyComboBoxItems(),
                "INDICATOR NAME", "WEEK 1", "WEEK 2", "WEEK 3", "WEEK 4", "WEEK 5", "PREVIOUS MONTH AVERAGE");

        //Prepare slims part 1 data entry table
        dataEntry.prepareDataEntryTable("slims part 1", tbmDataEntrySlimsPart1, tblDataCleaningSlimsPart1,
                7, "2,3", getDataTrustLevelComboBoxItems(),
                "INDICATOR NAME", "WEEK 1", "WEEK 2", "WEEK 3", "WEEK 4", "WEEK 5", "PREVIOUS MONTH AVERAGE");
        dataEntry.setSlimsOneIndicatorRowsList();
        //Prepare slims part 2 data entry table
        dataEntry.prepareDataEntryTable("slims part 2", tbmDataEntrySlimsPart2, tblDataCleaningSlimsPart2,
                5, "4", getDataTrustLevelComboBoxItems(), "INDICATOR NAME", "MONTHLY VALUE", "LOCATION NAME",
                "KEY INFORMANT", "TRIANGULATION", "DATA TRUST LEVEL", "PREVIOUS MONTH VALUE");

        //Load market names, pass combo box and system id, 1 for markets
        dataEntry.populateMarketNames(cboDataCleaningMarketsMarket, "1");
        //Load slims part 1 market names, pass combo box and system id, 2 for slims part 1
        dataEntry.populateMarketNames(cboDataCleaningSlimsPart1Node, "2");
        //Load slims part 2 market names, pass combo box and system id, 2 for slims part 2
        dataEntry.populateMarketNames(cboDataCleaningSlimsPart2Node, "2");
        //Set indicator column preffered width to reveal the entire indicator name
        tblDataCleaningMarkets.getColumnModel().getColumn(0).setPreferredWidth(220);
        tblDataCleaningMarkets.getColumnModel().getColumn(6).setPreferredWidth(150);
        tblDataCleaningSlimsPart1.getColumnModel().getColumn(6).setPreferredWidth(120);
        tblDataCleaningSlimsPart1.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblDataCleaningSlimsPart2.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblDataCleaningSlimsPart2.getColumnModel().getColumn(6).setPreferredWidth(120);
        //Add table model listeners to all the tables that exist in the data entry module
        //Markets table
        tblDataCleaningMarkets.getModel().addTableModelListener(new MarketsTableModel(tblDataCleaningMarkets,
                cboDataCleaningMarketsMarket, cboDataCleaningMarketsYear, cboDataCleaningMarketsMonth, 1, 6));

        //Slims part 1 table        
        tblDataCleaningSlimsPart1.getModel().addTableModelListener(new SlimsPart1TableModel(
                tblDataCleaningSlimsPart1, cboDataCleaningSlimsPart1Node, cboDataCleaningSlimsPart1Year, cboDataCleaningSlimsPart1Month));

        //Slims part 2 table SlimsPart2TableModel
        //tblDataCleaningSlimsPart2.getModel().addTableModelListener(new SlimsPart2TableModel(tblDataCleaningSlimsPart2));
        tblDataCleaningSlimsPart2.getModel().addTableModelListener(new SlimsPart2TableModel(tblDataCleaningSlimsPart2,
                cboDataCleaningSlimsPart2Node, cboDataCleaningSlimsPart2Year, cboDataCleaningSlimsPart2Month));

        insertDataTrustLevelCombobox();
        tblDataCleaningSlimsPart1.getColumn("WEEK 1").setCellEditor(rowEditor);

        insertCivilInsecurityCombobox();
        tblDataCleaningSlimsPart2.getColumn("MONTHLY VALUE").setCellEditor(rowEditor);

        //Load Data Cleaning tabs on main tabbed pane
        //Load markets, slims 1, and slims 2 data entry windows to tabbed pane                          
        tbpDataCleaning.add("Markets", pnlDataCleaningMarkets);
        tbpDataCleaning.add("SLIMS Part 1", pnlDataCleaningSlimsPart1);
        tbpDataCleaning.add("SLIMS Part 2", pnlDataCleaningSlimsPart2);
        logManager.logInfo("Exiting 'DataCleaningUi(java.awt.Frame parent, boolean modal)' constructor");
    }
    /*
     * tblDataCleaningMarkets = new javax.swing.JTable(tbmDataEntryMarkets) {
     *
     * public Component prepareRenderer(TableCellRenderer renderer, int row, int
     * col) { Component comp = super.prepareRenderer(renderer, row, col); if
     * (dataEntry.marketIndicatorCategoryRows.contains(row)) {
     * comp.setBackground(new java.awt.Color(153, 204, 255)); } else {
     * comp.setBackground(Color.white); } return comp; }
     *
     * public boolean isCellEditable(int row, int column) { if
     * (dataEntry.marketIndicatorCategoryRows.contains(row) || column == 0 ||
     * column == 6) { return false; } else { return true; } } };
     * tblDataCleaningSlimsPart1 = new
     * javax.swing.JTable(tbmDataEntrySlimsPart1) {
     *
     * public Component prepareRenderer(TableCellRenderer renderer, int row, int
     * col) { Component comp = super.prepareRenderer(renderer, row, col); if
     * (dataEntry.slimsPart1IndicatorCategoryRows.contains(row)) {
     * comp.setBackground(new java.awt.Color(153, 204, 255)); } else if
     * ((dataEntry.slimsOneLookUpRows.contains(row) ||
     * dataEntry.slimsOneDetailsRows.contains(row)) && col > 1) {
     * comp.setBackground(Color.gray); } else { comp.setBackground(Color.white);
     * } return comp; }
     *
     * public boolean isCellEditable(int row, int column) { if
     * (dataEntry.slimsPart1IndicatorCategoryRows.contains(row) || column == 0
     * || column == 6) { return false; } else if
     * ((dataEntry.slimsOneLookUpRows.contains(row) ||
     * dataEntry.slimsOneDetailsRows.contains(row)) && column > 1) { return
     * false; } else { return true; } } }; tblDataCleaningSlimsPart2 = new
     * javax.swing.JTable(tbmDataEntrySlimsPart2) {
     *
     * public Component prepareRenderer(TableCellRenderer renderer, int row, int
     * col) { Component comp = super.prepareRenderer(renderer, row, col); if
     * (dataEntry.slimsPart2IndicatorCategoryRows.contains(row)) {
     * comp.setBackground(new java.awt.Color(153, 204, 255)); } else {
     * comp.setBackground(Color.white); } return comp; }
     *
     * public boolean isCellEditable(int row, int column) { if
     * (dataEntry.slimsPart2IndicatorCategoryRows.contains(row) || column == 0
     * || column == 6) { return false; } else { return true; } } };
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    tblDataCleaningMarkets  = new javax.swing.JTable(tbmDataEntryMarkets) {

        public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
            Component comp = super.prepareRenderer(renderer, row, col);
            if (dataEntry.marketIndicatorCategoryRows.contains(row)) {
                comp.setBackground(new java.awt.Color(153, 204, 255));
            } else {
                comp.setBackground(Color.white);
            }
            return comp;
        }

        public boolean isCellEditable(int row, int column) {
            if (dataEntry.marketIndicatorCategoryRows.contains(row) || column == 0
                    || column == 6) {
                return false;
            } else {
                return true;
            }
        }
    };
    tblDataCleaningSlimsPart1  = new javax.swing.JTable(tbmDataEntrySlimsPart1) {

        public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
            Component comp = super.prepareRenderer(renderer, row, col);
            if (dataEntry.slimsPart1IndicatorCategoryRows.contains(row)) {
                comp.setBackground(new java.awt.Color(153, 204, 255));
            } else if ((dataEntry.slimsOneLookUpRows.contains(row)
                    || dataEntry.slimsOneDetailsRows.contains(row)) && col > 1) {
                comp.setBackground(Color.gray);
            } else {
                comp.setBackground(Color.white);
            }
            return comp;
        }

        public boolean isCellEditable(int row, int column) {
            if (dataEntry.slimsPart1IndicatorCategoryRows.contains(row) || column == 0
                    || column == 6) {
                return false;
            } else if ((dataEntry.slimsOneLookUpRows.contains(row)
                    || dataEntry.slimsOneDetailsRows.contains(row)) && column > 1) {
                return false;
            } else {
                return true;
            }
        }
    };
    tblDataCleaningSlimsPart2  = new javax.swing.JTable(tbmDataEntrySlimsPart2) {

        public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
            Component comp = super.prepareRenderer(renderer, row, col);
            if (dataEntry.slimsPart2IndicatorCategoryRows.contains(row)) {
                comp.setBackground(new java.awt.Color(153, 204, 255));
            } else {
                comp.setBackground(Color.white);
            }
            return comp;
        }

        public boolean isCellEditable(int row, int column) {
            if (dataEntry.slimsPart2IndicatorCategoryRows.contains(row) || column
                    == 0 || column == 6) {
                return false;
            } else {
                return true;
            }
        }
    };	
	
        pnlDataCleaningSlimsPart2 = new javax.swing.JPanel();
        scpDataCleaningSlimsPart2 = new javax.swing.JScrollPane();
        //tblDataCleaningSlimsPart2 = new javax.swing.JTable();
        pnlDataCleaningSlimsPart2Top = new javax.swing.JPanel();
        lblDataCleaningSlimsPart2Node = new javax.swing.JLabel();
        lblDataCleaningSlimsPart2Month = new javax.swing.JLabel();
        lblDataCleaningSlimsPart2Year = new javax.swing.JLabel();
        cboDataCleaningSlimsPart2Node = new javax.swing.JComboBox();
        cboDataCleaningSlimsPart2Month = new javax.swing.JComboBox();
        cboDataCleaningSlimsPart2Year = new javax.swing.JComboBox();
        btnDataCleaningSlimsPart2Fetch = new javax.swing.JButton();
        btnDataCleaningSlimsPart2Clear = new javax.swing.JButton();
        btnDataCleaningSlimsPart2Exit = new javax.swing.JButton();
        pnlDataCleaningMarkets = new javax.swing.JPanel();
        scpDataCleaningMarkets = new javax.swing.JScrollPane();
        //tblDataCleaningMarkets = new javax.swing.JTable();
        pnlDataCleaningMarketsTop = new javax.swing.JPanel();
        lblDataCleaningMarketsMarket = new javax.swing.JLabel();
        lblDataCleaningMarketsMonth = new javax.swing.JLabel();
        lblDataCleaningMarketsYear = new javax.swing.JLabel();
        cboDataCleaningMarketsMarket = new javax.swing.JComboBox();
        cboDataCleaningMarketsMonth = new javax.swing.JComboBox();
        cboDataCleaningMarketsYear = new javax.swing.JComboBox();
        btnDataCleaningMarketsFetch = new javax.swing.JButton();
        btnDataCleaningMarketsClear = new javax.swing.JButton();
        btnDataCleaningMarketsExit = new javax.swing.JButton();
        pnlDataCleaningSlimsPart1 = new javax.swing.JPanel();
        scpDataCleaningSlimsPart1 = new javax.swing.JScrollPane();
        //tblDataCleaningSlimsPart1 = new javax.swing.JTable();
        pnlDataCleaningSlimsPart1Top = new javax.swing.JPanel();
        lblDataCleaningSlimsPart1Node = new javax.swing.JLabel();
        lblDataCleaningSlimsPart1Month = new javax.swing.JLabel();
        lblDataCleaningSlimsPart1Year = new javax.swing.JLabel();
        cboDataCleaningSlimsPart1Node = new javax.swing.JComboBox();
        cboDataCleaningSlimsPart1Month = new javax.swing.JComboBox();
        cboDataCleaningSlimsPart1Year = new javax.swing.JComboBox();
        btnDataCleaningSlimsPart1Fetch = new javax.swing.JButton();
        btnDataCleaningSlimsPart1Exit = new javax.swing.JButton();
        btnDataCleaningSlimsPart1Clear = new javax.swing.JButton();
        tbpDataCleaning = new javax.swing.JTabbedPane();

        tblDataCleaningSlimsPart2.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblDataCleaningSlimsPart2.setModel(tbmDataEntrySlimsPart2);
        tblDataCleaningSlimsPart2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDataCleaningSlimsPart2.setCellSelectionEnabled(true);
        tblDataCleaningSlimsPart2.setGridColor(Color.gray);
        tblDataCleaningSlimsPart2.setInheritsPopupMenu(true);
        tblDataCleaningSlimsPart2.setRowHeight(20);
        tblDataCleaningSlimsPart2.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblDataCleaningSlimsPart2.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpDataCleaningSlimsPart2.setViewportView(tblDataCleaningSlimsPart2);
        tblDataCleaningSlimsPart2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        lblDataCleaningSlimsPart2Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningSlimsPart2Node.setLabelFor(cboDataCleaningMarketsMarket);
        lblDataCleaningSlimsPart2Node.setText("Node");

        lblDataCleaningSlimsPart2Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningSlimsPart2Month.setLabelFor(cboDataCleaningMarketsMonth);
        lblDataCleaningSlimsPart2Month.setText("Month");

        lblDataCleaningSlimsPart2Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningSlimsPart2Year.setLabelFor(cboDataCleaningMarketsYear);
        lblDataCleaningSlimsPart2Year.setText("Year");

        cboDataCleaningSlimsPart2Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N

        cboDataCleaningSlimsPart2Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataCleaningSlimsPart2Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        cboDataCleaningSlimsPart2Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataCleaningSlimsPart2Year.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        cboDataCleaningSlimsPart2Year.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));

        btnDataCleaningSlimsPart2Fetch.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningSlimsPart2Fetch.setMnemonic('F');
        btnDataCleaningSlimsPart2Fetch.setText("Fetch Data");
        btnDataCleaningSlimsPart2Fetch.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnDataCleaningSlimsPart2Fetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningSlimsPart2FetchActionPerformed(evt);
            }
        });

        btnDataCleaningSlimsPart2Clear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningSlimsPart2Clear.setMnemonic('C');
        btnDataCleaningSlimsPart2Clear.setText("Clear");
        btnDataCleaningSlimsPart2Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningSlimsPart2ClearActionPerformed(evt);
            }
        });

        btnDataCleaningSlimsPart2Exit.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningSlimsPart2Exit.setMnemonic('x');
        btnDataCleaningSlimsPart2Exit.setText("Exit");
        btnDataCleaningSlimsPart2Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningSlimsPart2ExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataCleaningSlimsPart2TopLayout = new javax.swing.GroupLayout(pnlDataCleaningSlimsPart2Top);
        pnlDataCleaningSlimsPart2Top.setLayout(pnlDataCleaningSlimsPart2TopLayout);
        pnlDataCleaningSlimsPart2TopLayout.setHorizontalGroup(
            pnlDataCleaningSlimsPart2TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningSlimsPart2TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataCleaningSlimsPart2Node)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningSlimsPart2Node, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataCleaningSlimsPart2Month)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningSlimsPart2Month, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataCleaningSlimsPart2Year, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningSlimsPart2Year, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningSlimsPart2Fetch)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningSlimsPart2Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningSlimsPart2Exit)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        pnlDataCleaningSlimsPart2TopLayout.setVerticalGroup(
            pnlDataCleaningSlimsPart2TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDataCleaningSlimsPart2TopLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDataCleaningSlimsPart2TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataCleaningSlimsPart2Node)
                    .addComponent(lblDataCleaningSlimsPart2Month)
                    .addComponent(lblDataCleaningSlimsPart2Year)
                    .addComponent(cboDataCleaningSlimsPart2Node, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataCleaningSlimsPart2Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataCleaningSlimsPart2Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDataCleaningSlimsPart2Fetch)
                    .addComponent(btnDataCleaningSlimsPart2Clear)
                    .addComponent(btnDataCleaningSlimsPart2Exit))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlDataCleaningSlimsPart2Layout = new javax.swing.GroupLayout(pnlDataCleaningSlimsPart2);
        pnlDataCleaningSlimsPart2.setLayout(pnlDataCleaningSlimsPart2Layout);
        pnlDataCleaningSlimsPart2Layout.setHorizontalGroup(
            pnlDataCleaningSlimsPart2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDataCleaningSlimsPart2Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scpDataCleaningSlimsPart2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        pnlDataCleaningSlimsPart2Layout.setVerticalGroup(
            pnlDataCleaningSlimsPart2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningSlimsPart2Layout.createSequentialGroup()
                .addComponent(pnlDataCleaningSlimsPart2Top, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpDataCleaningSlimsPart2, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblDataCleaningMarkets.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblDataCleaningMarkets.setModel(tbmDataEntryMarkets);
        tblDataCleaningMarkets.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDataCleaningMarkets.setCellSelectionEnabled(true);
        tblDataCleaningMarkets.setGridColor(Color.gray);
        tblDataCleaningMarkets.setRowHeight(20);
        tblDataCleaningMarkets.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblDataCleaningMarkets.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpDataCleaningMarkets.setViewportView(tblDataCleaningMarkets);

        lblDataCleaningMarketsMarket.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningMarketsMarket.setLabelFor(cboDataCleaningMarketsMarket);
        lblDataCleaningMarketsMarket.setText("Market");

        lblDataCleaningMarketsMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningMarketsMonth.setLabelFor(cboDataCleaningMarketsMonth);
        lblDataCleaningMarketsMonth.setText("Month");

        lblDataCleaningMarketsYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningMarketsYear.setLabelFor(cboDataCleaningMarketsYear);
        lblDataCleaningMarketsYear.setText("Year");

        cboDataCleaningMarketsMarket.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N

        cboDataCleaningMarketsMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataCleaningMarketsMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        cboDataCleaningMarketsYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataCleaningMarketsYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        cboDataCleaningMarketsYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));

        btnDataCleaningMarketsFetch.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningMarketsFetch.setMnemonic('F');
        btnDataCleaningMarketsFetch.setText("Fetch Data");
        btnDataCleaningMarketsFetch.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnDataCleaningMarketsFetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningMarketsFetchActionPerformed(evt);
            }
        });

        btnDataCleaningMarketsClear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningMarketsClear.setMnemonic('C');
        btnDataCleaningMarketsClear.setText("Clear");
        btnDataCleaningMarketsClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningMarketsClearActionPerformed(evt);
            }
        });

        btnDataCleaningMarketsExit.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningMarketsExit.setMnemonic('x');
        btnDataCleaningMarketsExit.setText("Exit");
        btnDataCleaningMarketsExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningMarketsExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataCleaningMarketsTopLayout = new javax.swing.GroupLayout(pnlDataCleaningMarketsTop);
        pnlDataCleaningMarketsTop.setLayout(pnlDataCleaningMarketsTopLayout);
        pnlDataCleaningMarketsTopLayout.setHorizontalGroup(
            pnlDataCleaningMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningMarketsTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataCleaningMarketsMarket)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningMarketsMarket, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataCleaningMarketsMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningMarketsMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataCleaningMarketsYear, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningMarketsYear, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningMarketsFetch)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningMarketsClear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(btnDataCleaningMarketsExit)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        pnlDataCleaningMarketsTopLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDataCleaningMarketsExit, btnDataCleaningMarketsFetch});

        pnlDataCleaningMarketsTopLayout.setVerticalGroup(
            pnlDataCleaningMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningMarketsTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataCleaningMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDataCleaningMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDataCleaningMarketsExit)
                        .addComponent(btnDataCleaningMarketsClear))
                    .addGroup(pnlDataCleaningMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDataCleaningMarketsMarket)
                        .addComponent(lblDataCleaningMarketsMonth)
                        .addComponent(lblDataCleaningMarketsYear)
                        .addComponent(cboDataCleaningMarketsMarket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboDataCleaningMarketsMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboDataCleaningMarketsYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDataCleaningMarketsFetch)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDataCleaningMarketsTopLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDataCleaningMarketsClear, btnDataCleaningMarketsExit, btnDataCleaningMarketsFetch});

        javax.swing.GroupLayout pnlDataCleaningMarketsLayout = new javax.swing.GroupLayout(pnlDataCleaningMarkets);
        pnlDataCleaningMarkets.setLayout(pnlDataCleaningMarketsLayout);
        pnlDataCleaningMarketsLayout.setHorizontalGroup(
            pnlDataCleaningMarketsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpDataCleaningMarkets)
            .addComponent(pnlDataCleaningMarketsTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDataCleaningMarketsLayout.setVerticalGroup(
            pnlDataCleaningMarketsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningMarketsLayout.createSequentialGroup()
                .addComponent(pnlDataCleaningMarketsTop, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpDataCleaningMarkets, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
        );

        tblDataCleaningSlimsPart1.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblDataCleaningSlimsPart1.setModel(tbmDataEntrySlimsPart1);
        tblDataCleaningSlimsPart1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDataCleaningSlimsPart1.setCellSelectionEnabled(true);
        tblDataCleaningSlimsPart1.setGridColor(Color.gray);
        tblDataCleaningSlimsPart1.setRowHeight(20);
        tblDataCleaningSlimsPart1.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblDataCleaningSlimsPart1.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpDataCleaningSlimsPart1.setViewportView(tblDataCleaningSlimsPart1);
        tblDataCleaningSlimsPart1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        lblDataCleaningSlimsPart1Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningSlimsPart1Node.setLabelFor(cboDataCleaningMarketsMarket);
        lblDataCleaningSlimsPart1Node.setText("Node");

        lblDataCleaningSlimsPart1Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningSlimsPart1Month.setLabelFor(cboDataCleaningMarketsMonth);
        lblDataCleaningSlimsPart1Month.setText("Month");

        lblDataCleaningSlimsPart1Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataCleaningSlimsPart1Year.setLabelFor(cboDataCleaningMarketsYear);
        lblDataCleaningSlimsPart1Year.setText("Year");

        cboDataCleaningSlimsPart1Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N

        cboDataCleaningSlimsPart1Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataCleaningSlimsPart1Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        cboDataCleaningSlimsPart1Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataCleaningSlimsPart1Year.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        cboDataCleaningSlimsPart1Year.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));

        btnDataCleaningSlimsPart1Fetch.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningSlimsPart1Fetch.setMnemonic('F');
        btnDataCleaningSlimsPart1Fetch.setText("Fetch Data");
        btnDataCleaningSlimsPart1Fetch.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnDataCleaningSlimsPart1Fetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningSlimsPart1FetchActionPerformed(evt);
            }
        });

        btnDataCleaningSlimsPart1Exit.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningSlimsPart1Exit.setMnemonic('x');
        btnDataCleaningSlimsPart1Exit.setText("Exit");
        btnDataCleaningSlimsPart1Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningSlimsPart1ExitActionPerformed(evt);
            }
        });

        btnDataCleaningSlimsPart1Clear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataCleaningSlimsPart1Clear.setMnemonic('C');
        btnDataCleaningSlimsPart1Clear.setText("Clear");
        btnDataCleaningSlimsPart1Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataCleaningSlimsPart1ClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataCleaningSlimsPart1TopLayout = new javax.swing.GroupLayout(pnlDataCleaningSlimsPart1Top);
        pnlDataCleaningSlimsPart1Top.setLayout(pnlDataCleaningSlimsPart1TopLayout);
        pnlDataCleaningSlimsPart1TopLayout.setHorizontalGroup(
            pnlDataCleaningSlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningSlimsPart1TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataCleaningSlimsPart1Node)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningSlimsPart1Node, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataCleaningSlimsPart1Month)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningSlimsPart1Month, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataCleaningSlimsPart1Year, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataCleaningSlimsPart1Year, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningSlimsPart1Fetch)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningSlimsPart1Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataCleaningSlimsPart1Exit)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        pnlDataCleaningSlimsPart1TopLayout.setVerticalGroup(
            pnlDataCleaningSlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningSlimsPart1TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataCleaningSlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDataCleaningSlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDataCleaningSlimsPart1Exit)
                        .addComponent(btnDataCleaningSlimsPart1Clear))
                    .addGroup(pnlDataCleaningSlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDataCleaningSlimsPart1Node)
                        .addComponent(lblDataCleaningSlimsPart1Month)
                        .addComponent(lblDataCleaningSlimsPart1Year)
                        .addComponent(cboDataCleaningSlimsPart1Node, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboDataCleaningSlimsPart1Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboDataCleaningSlimsPart1Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDataCleaningSlimsPart1Fetch)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlDataCleaningSlimsPart1Layout = new javax.swing.GroupLayout(pnlDataCleaningSlimsPart1);
        pnlDataCleaningSlimsPart1.setLayout(pnlDataCleaningSlimsPart1Layout);
        pnlDataCleaningSlimsPart1Layout.setHorizontalGroup(
            pnlDataCleaningSlimsPart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpDataCleaningSlimsPart1)
            .addComponent(pnlDataCleaningSlimsPart1Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDataCleaningSlimsPart1Layout.setVerticalGroup(
            pnlDataCleaningSlimsPart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataCleaningSlimsPart1Layout.createSequentialGroup()
                .addComponent(pnlDataCleaningSlimsPart1Top, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpDataCleaningSlimsPart1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
        );

        setTitle("DATA CLEANING");

        tbpDataCleaning.setFont(new java.awt.Font("Arial Unicode MS", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpDataCleaning, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpDataCleaning, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDataCleaningMarketsExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningMarketsExitActionPerformed
        logManager.logInfo("Entering and exiting 'btnDataCleaningMarketsExitActionPerformed(java.awt.event.ActionEvent evt)' method");
        showConfirmDialog();
    }//GEN-LAST:event_btnDataCleaningMarketsExitActionPerformed
    private void showConfirmDialog() {
        logManager.logInfo("Entering 'showConfirmDialog()' method");
        // Allow user to conmfirm whether they want to exit
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
        if (result <= 0) {
            this.dispose();
        }
        logManager.logInfo("Exiting 'showConfirmDialog()' method");
    }

    private String[] getSupplyComboBoxItems() {
        logManager.logInfo("Entering 'getSupplyComboBoxItems()' method");
        String[] supplyComboBoxItems = {"(5) Surplus", "(4) Above Normal", "(3) Normal", "(2) Below Normal",
            "(1) Scarce", "(0) Not Available"};
        logManager.logInfo("Exiting 'getSupplyComboBoxItems()' method");
        return supplyComboBoxItems;
    }

    private String[] getDataTrustLevelComboBoxItems() {
        logManager.logInfo("Entering 'getDataTrustLevelComboBoxItems()' method");
        String[] dataTrustLevelComboBoxItems = {"Poor", "Fair", "Good"};
        logManager.logInfo("Exiting 'getDataTrustLevelComboBoxItems()' method");
        return dataTrustLevelComboBoxItems;
    }
    private void btnDataCleaningMarketsClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningMarketsClearActionPerformed
        // Clear data entry markets table
        logManager.logInfo("Entering 'btnDataCleaningMarketsClearActionPerformed(java.awt.event.ActionEvent evt)' method");
        dataEntry.clearTable(tblDataCleaningMarkets);
        logManager.logInfo("Exiting 'btnDataCleaningMarketsClearActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningMarketsClearActionPerformed

    private void btnDataCleaningSlimsPart2ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningSlimsPart2ExitActionPerformed
        logManager.logInfo("Entering 'btnDataCleaningSlimsPart2ExitActionPerformed(java.awt.event.ActionEvent evt)' method");
        showConfirmDialog();
        logManager.logInfo("Exiting 'btnDataCleaningSlimsPart2ExitActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningSlimsPart2ExitActionPerformed

    private void btnDataCleaningSlimsPart2ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningSlimsPart2ClearActionPerformed
        // Clear data entry markets table
        logManager.logInfo("Entering 'btnDataCleaningSlimsPart2ClearActionPerformed(java.awt.event.ActionEvent evt)' method");
        dataEntry.clearTable(tblDataCleaningSlimsPart2);
        logManager.logInfo("Exiting 'btnDataCleaningSlimsPart2ClearActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningSlimsPart2ClearActionPerformed

    private void btnDataCleaningSlimsPart1ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningSlimsPart1ExitActionPerformed
        logManager.logInfo("Entering 'btnDataCleaningSlimsPart1ExitActionPerformed(java.awt.event.ActionEvent evt)' method");
        showConfirmDialog();
        logManager.logInfo("Exiting 'btnDataCleaningSlimsPart1ExitActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningSlimsPart1ExitActionPerformed

    private void btnDataCleaningSlimsPart1ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningSlimsPart1ClearActionPerformed
        // Clear data entry markets table
        logManager.logInfo("Entering 'btnDataCleaningSlimsPart1ClearActionPerformed(java.awt.event.ActionEvent evt)' method");
        dataEntry.clearTable(tblDataCleaningSlimsPart1);
        logManager.logInfo("Exiting 'btnDataCleaningSlimsPart1ClearActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningSlimsPart1ClearActionPerformed

    private void btnDataCleaningMarketsFetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningMarketsFetchActionPerformed
        // Fecth markets data
        logManager.logInfo("Entering 'btnDataCleaningMarketsFetchActionPerformed(java.awt.event.ActionEvent evt)' method");
        int year = 0;
        int monthId = 0;
        String marketName = "";
        try {
            MarketsTableModel.setStartEdit(false);
            pnlDataCleaningMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            year = Integer.parseInt(cboDataCleaningMarketsYear.getSelectedItem().toString());
            monthId = cboDataCleaningMarketsMonth.getSelectedIndex() + 1;
            marketName = cboDataCleaningMarketsMarket.getSelectedItem().toString();
            dataEntry.fetchMarketPricesWithAverage(year, monthId, marketName, tblDataCleaningMarkets);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in "
                    + " 'btnDataCleaningMarketsFetchActionPerformed(java.awt.event.ActionEvent evt)' method");
        } finally {
            //this.startEdit = true;
            //editTrack.setStartEdit(true);
            //marketsTableModel.setStartEdit(true);
            MarketsTableModel.setStartEdit(true);
            MarketsTableModel.isStartEdit();
            pnlDataCleaningMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        logManager.logInfo("Exiting 'btnDataCleaningMarketsFetchActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningMarketsFetchActionPerformed

    private void btnDataCleaningSlimsPart2FetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningSlimsPart2FetchActionPerformed
        // Fetch saved slims part 2 data
        logManager.logInfo("Entering 'btnDataCleaningSlimsPart2FetchActionPerformed(java.awt.event.ActionEvent evt)' method");
        int year = 0;
        int monthId = 0;
        int marketId = 0;
        try {
            pnlDataCleaningSlimsPart2.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SlimsPart2TableModel.startEdit = false;
            year = Integer.parseInt(cboDataCleaningSlimsPart2Year.getSelectedItem().toString());
            monthId = cboDataCleaningSlimsPart2Month.getSelectedIndex() + 1;
            marketId = market.getMarketId(cboDataCleaningSlimsPart2Node.getSelectedItem().toString());
            dataEntry.fetchSlimsPart2Prices(year, monthId, marketId, tblDataCleaningSlimsPart2, null, "cleaning");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in"
                    + " 'btnDataCleaningSlimsPart2FetchActionPerformed(java.awt.event.ActionEvent evt)' method");
        } finally {
            //Set table in edit mode
            SlimsPart2TableModel.startEdit = true;
            pnlDataCleaningSlimsPart2.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        logManager.logInfo("Exiting 'btnDataCleaningSlimsPart2FetchActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningSlimsPart2FetchActionPerformed

    private void btnDataCleaningSlimsPart1FetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataCleaningSlimsPart1FetchActionPerformed
        logManager.logInfo("Entering 'btnDataCleaningSlimsPart1FetchActionPerformed(java.awt.event.ActionEvent evt)' method");
        // Fetch saved data
        int currentMonthYear = 0;
        int monthId = 0;
        String marketName = "";
        String function = "cleaning";

        currentMonthYear = Integer.parseInt(cboDataCleaningSlimsPart1Year.getSelectedItem().toString());
        monthId = cboDataCleaningSlimsPart1Month.getSelectedIndex() + 1;
        marketName = cboDataCleaningSlimsPart1Node.getSelectedItem().toString();

        try {
            this.startEdit = false;
            pnlDataCleaningSlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Fetch and load prices
            dataEntry.fetchSlimsPart1Data(currentMonthYear, monthId, marketName, null, function, tblDataCleaningSlimsPart1);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in "
                    + "'btnDataCleaningSlimsPart1FetchActionPerformed(java.awt.event.ActionEvent evt)' method");
            //exception.printStackTrace();
        } finally {
            //Set table in edit mode
            SlimsPart1TableModel.startEdit = true;
            pnlDataCleaningSlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        logManager.logInfo("Exiting 'btnDataCleaningSlimsPart1FetchActionPerformed(java.awt.event.ActionEvent evt)' method");
    }//GEN-LAST:event_btnDataCleaningSlimsPart1FetchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        logManager.logInfo("Entering 'main(String args[])' method");
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DataCleaningUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataCleaningUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataCleaningUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataCleaningUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                DataCleaningUi dialog = new DataCleaningUi(new javax.swing.JFrame(), true);
                /*
                 * dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                 *
                 * @Override public void
                 * windowClosing(java.awt.event.WindowEvent e) { System.exit(0);
                 * }
                });
                 */
                dialog.setVisible(true);
            }
        });
        logManager.logInfo("Exiting 'main(String args[])' method");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDataCleaningMarketsClear;
    private javax.swing.JButton btnDataCleaningMarketsExit;
    private javax.swing.JButton btnDataCleaningMarketsFetch;
    private javax.swing.JButton btnDataCleaningSlimsPart1Clear;
    private javax.swing.JButton btnDataCleaningSlimsPart1Exit;
    private javax.swing.JButton btnDataCleaningSlimsPart1Fetch;
    private javax.swing.JButton btnDataCleaningSlimsPart2Clear;
    private javax.swing.JButton btnDataCleaningSlimsPart2Exit;
    private javax.swing.JButton btnDataCleaningSlimsPart2Fetch;
    private javax.swing.JComboBox cboDataCleaningMarketsMarket;
    private javax.swing.JComboBox cboDataCleaningMarketsMonth;
    private javax.swing.JComboBox cboDataCleaningMarketsYear;
    private javax.swing.JComboBox cboDataCleaningSlimsPart1Month;
    private javax.swing.JComboBox cboDataCleaningSlimsPart1Node;
    private javax.swing.JComboBox cboDataCleaningSlimsPart1Year;
    private javax.swing.JComboBox cboDataCleaningSlimsPart2Month;
    private javax.swing.JComboBox cboDataCleaningSlimsPart2Node;
    private javax.swing.JComboBox cboDataCleaningSlimsPart2Year;
    private javax.swing.JLabel lblDataCleaningMarketsMarket;
    private javax.swing.JLabel lblDataCleaningMarketsMonth;
    private javax.swing.JLabel lblDataCleaningMarketsYear;
    private javax.swing.JLabel lblDataCleaningSlimsPart1Month;
    private javax.swing.JLabel lblDataCleaningSlimsPart1Node;
    private javax.swing.JLabel lblDataCleaningSlimsPart1Year;
    private javax.swing.JLabel lblDataCleaningSlimsPart2Month;
    private javax.swing.JLabel lblDataCleaningSlimsPart2Node;
    private javax.swing.JLabel lblDataCleaningSlimsPart2Year;
    public javax.swing.JPanel pnlDataCleaningMarkets;
    private javax.swing.JPanel pnlDataCleaningMarketsTop;
    public javax.swing.JPanel pnlDataCleaningSlimsPart1;
    private javax.swing.JPanel pnlDataCleaningSlimsPart1Top;
    public javax.swing.JPanel pnlDataCleaningSlimsPart2;
    private javax.swing.JPanel pnlDataCleaningSlimsPart2Top;
    private javax.swing.JScrollPane scpDataCleaningMarkets;
    private javax.swing.JScrollPane scpDataCleaningSlimsPart1;
    private javax.swing.JScrollPane scpDataCleaningSlimsPart2;
    private javax.swing.JTable tblDataCleaningMarkets;
    private javax.swing.JTable tblDataCleaningSlimsPart1;
    private javax.swing.JTable tblDataCleaningSlimsPart2;
    public javax.swing.JTabbedPane tbpDataCleaning;
    // End of variables declaration//GEN-END:variables

    private void prepareDataEntryTable(DefaultTableModel tableModel, String applicationIds) {
        logManager.logInfo("Entering 'prepareDataEntryTable(DefaultTableModel tableModel, String applicationIds)' method");
        try {
            queryBuilder.querySelectIndicators(applicationIds);
        } catch (Exception exception) {
            logManager.logInfo("Exception was thrown and caught in "
                    + "'prepareDataEntryTable(DefaultTableModel tableModel, String applicationIds)' method");
        }
        logManager.logInfo("Exiting 'prepareDataEntryTable(DefaultTableModel tableModel, String applicationIds)' method");
    }

    private void insertDataTrustLevelCombobox() {
        logManager.logInfo("Entering 'insertDataTrustLevelCombobox()' method");
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Poor");
        comboBox.addItem("Fair");
        comboBox.addItem("Good");
        rowEditor = new EachRowEditor(tblDataCleaningSlimsPart1);
        for (int row = 0; row < tblDataCleaningSlimsPart1.getRowCount(); row++) {
            if (tblDataCleaningSlimsPart1.getValueAt(row, 0).equals("DATA TRUST LEVEL")) {
                rowEditor.setEditorAt(row, new DefaultCellEditor(comboBox));
            }
        }
        logManager.logInfo("Exiting 'insertDataTrustLevelCombobox()' method");
    }

    private void insertCivilInsecurityCombobox() {
        logManager.logInfo("Entering 'insertCivilInsecurityCombobox()' method");
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Tranquil");
        comboBox.addItem("Tense but safe");
        comboBox.addItem("Restricted movement");
        comboBox.addItem("Clan clashes");
        rowEditor = new EachRowEditor(tblDataCleaningSlimsPart2);
        for (int row = 0; row < tblDataCleaningSlimsPart2.getRowCount(); row++) {
            if (tblDataCleaningSlimsPart2.getValueAt(row, 0).equals("Level Of Civil Insecurity")) {
                rowEditor.setEditorAt(row, new DefaultCellEditor(comboBox));
            }
        }
        logManager.logInfo("Exiting 'insertCivilInsecurityCombobox()' method");
    }
}
