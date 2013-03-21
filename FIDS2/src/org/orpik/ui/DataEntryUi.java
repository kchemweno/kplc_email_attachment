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
import org.orpik.modular.DataEntry;
import org.orpik.modular.MarketsTableModel;
import org.orpik.modular.SlimsPart1TableModel;
import org.orpik.modular.SlimsPart2TableModel;
import org.orpik.settings.global.GuiManager;

/**
 *
 * @author Chemweno
 */
public class DataEntryUi extends javax.swing.JInternalFrame {

    private DefaultTableModel tbmDataEntryMarkets = new DefaultTableModel();
    private DefaultTableModel tbmDataEntrySlimsPart1 = new DefaultTableModel();
    private DefaultTableModel tbmDataEntrySlimsPart2 = new DefaultTableModel();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private GuiManager guiManager = new GuiManager();
    private DataEntry dataEntry = new DataEntry();
    private Market market = new Market();
    private EachRowEditor rowEditor;
    private int width = 1082;
    private int height = 554;

    /**
     * Creates new form DataEntryUi
     */
    public DataEntryUi(java.awt.Frame parent, boolean modal) {
        /*
         * super(parent, modal);
         */
        initComponents();
        //Allow Data Entry dialog to resize
        /*
         * this.setResizable(true);
         */
        //Dispose Data Entry dialog on close
        /*
         * this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         */
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
        dataEntry.prepareDataEntryTable("markets", tbmDataEntryMarkets, tblDataEntryMarkets, 6, "1,3", getSupplyComboBoxItems(),
                "INDICATOR NAME", "WEEK 1", "WEEK 2", "WEEK 3", "WEEK 4", "WEEK 5", "SUPPLY");

        //Prepare slims part 1 data entry table
        dataEntry.prepareDataEntryTable("slims part 1", tbmDataEntrySlimsPart1, tblDataEntrySlimsPart1,
                7, "2,3", getDataTrustLevelComboBoxItems(),
                "INDICATOR NAME", "WEEK 1", "WEEK 2", "WEEK 3", "WEEK 4", "WEEK 5");
        dataEntry.setSlimsOneIndicatorRowsList();
        //Prepare slims part 2 data entry table
        dataEntry.prepareDataEntryTable("slims part 2", tbmDataEntrySlimsPart2, tblDataEntrySlimsPart2,
                5, "4", getDataTrustLevelComboBoxItems(),
                "INDICATOR NAME", "MONTHLY VALUE", "LOCATION NAME", "KEY INFORMANT", "TRIANGULATION", "DATA TRUST LEVEL");

        //Load market names, pass combo box and system id, 1 for markets
        dataEntry.populateMarketNames(cboDataEntryMarketsMarket, "1");
        //Load slims part 1 market names, pass combo box and system id, 2 for slims part 1
        dataEntry.populateMarketNames(cboDataEntrySlimsPart1Node, "2");
        //Load slims part 2 market names, pass combo box and system id, 2 for slims part 2
        dataEntry.populateMarketNames(cboDataEntrySlimsPart2Node, "2");
        //Set indicator column preffered width to reveal the entire indicator name
        tblDataEntryMarkets.getColumnModel().getColumn(0).setPreferredWidth(220);
        tblDataEntrySlimsPart1.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblDataEntrySlimsPart2.getColumnModel().getColumn(0).setPreferredWidth(180);
        //Add table model listeners to all the tables that exist in the data entry module
        //Markets table
        tblDataEntryMarkets.getModel().addTableModelListener(new MarketsTableModel(tblDataEntryMarkets, 1, 5));

        //Slims part 1 table        
        tblDataEntrySlimsPart1.getModel().addTableModelListener(new SlimsPart1TableModel(tblDataEntrySlimsPart1));

        //Slims part 2 table
        tblDataEntrySlimsPart2.getModel().addTableModelListener(new SlimsPart2TableModel(tblDataEntrySlimsPart2));

        insertDataTrustLevelCombobox();
        tblDataEntrySlimsPart1.getColumn("WEEK 1").setCellEditor(rowEditor);

        insertCivilInsecurityCombobox();
        tblDataEntrySlimsPart2.getColumn("MONTHLY VALUE").setCellEditor(rowEditor);

        //Load markets, slims 1, and slims 2 data entry windows to tabbed pane                          
        tbpDataEntry.add("Markets", pnlDataEntryMarkets);
        tbpDataEntry.add("SLIMS Part 1", pnlDataEntrySlimsPart1);
        tbpDataEntry.add("SLIMS Part 2", pnlDataEntrySlimsPart2);
    }
    /*
     * tblDataEntryMarkets = new javax.swing.JTable(tbmDataEntryMarkets) {
     * public Component prepareRenderer(TableCellRenderer renderer, int row, int
     * col) { Component comp = super.prepareRenderer(renderer, row, col); if
     * (dataEntry.marketIndicatorCategoryRows.contains(row)) {
     * comp.setBackground(new java.awt.Color(153, 204, 255)); } else {
     * comp.setBackground(Color.white); } return comp; }
     *
     * public boolean isCellEditable(int row, int column) { if
     * (dataEntry.marketIndicatorCategoryRows.contains(row) || column == 0) {
     * return false; } else { return true; } } };
     *
     * tblDataEntrySlimsPart1 = new javax.swing.JTable(tbmDataEntrySlimsPart1) {
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
     * (dataEntry.slimsPart1IndicatorCategoryRows.contains(row) || column == 0)
     * { return false; } else if ((dataEntry.slimsOneLookUpRows.contains(row) ||
     * dataEntry.slimsOneDetailsRows.contains(row)) && column > 1) { return
     * false; } else { return true; } } };
     *
     * tblDataEntrySlimsPart2 = new javax.swing.JTable(tbmDataEntrySlimsPart2) {
     * public Component prepareRenderer(TableCellRenderer renderer, int row, int
     * col) { Component comp = super.prepareRenderer(renderer, row, col); if
     * (dataEntry.slimsPart2IndicatorCategoryRows.contains(row)) {
     * comp.setBackground(new java.awt.Color(153, 204, 255)); } else {
     * comp.setBackground(Color.white); } return comp; }
     *
     * public boolean isCellEditable(int row, int column) { if
     * (dataEntry.slimsPart2IndicatorCategoryRows.contains(row) || column == 0)
     * { return false; } else { return true; } } };
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDataEntrySlimsPart2 = new javax.swing.JPanel();
        scpDataEntrySlimsPart2 = new javax.swing.JScrollPane();
        //tblDataEntrySlimsPart2 = new javax.swing.JTable();
        pnlDataEntrySlimsPart2Top = new javax.swing.JPanel();
        lblDataEntrySlimsPart2Node = new javax.swing.JLabel();
        lblDataEntrySlimsPart2Month = new javax.swing.JLabel();
        lblDataEntrySlimsPart2Year = new javax.swing.JLabel();
        cboDataEntrySlimsPart2Node = new javax.swing.JComboBox();
        cboDataEntrySlimsPart2Month = new javax.swing.JComboBox();
        cboDataEntrySlimsPart2Year = new javax.swing.JComboBox();
        pnlDataEntrySlimsPart2Bottom = new javax.swing.JPanel();
        btnDataEntrySlimsPart2SaveSend = new javax.swing.JButton();
        btnDataEntrySlimsPart2Save = new javax.swing.JButton();
        btnDataEntrySlimsPart2Fetch = new javax.swing.JButton();
        btnDataEntrySlimsPart2Exit = new javax.swing.JButton();
        btnDataEntrySlimsPart2Clear = new javax.swing.JButton();
        pnlDataEntrySlimsPart2Comments = new javax.swing.JPanel();
        lblDataEntrySlimsPart2Comments = new javax.swing.JLabel();
        scpDataEntrySlimsPart2comments = new javax.swing.JScrollPane();
        txaDataEntrySlimsPart2comments = new javax.swing.JTextArea();
        pnlDataEntryMarkets = new javax.swing.JPanel();
        scpDataEntryMarkets = new javax.swing.JScrollPane();
        //tblDataEntryMarkets = new javax.swing.JTable();
        pnlDataEntryMarketsTop = new javax.swing.JPanel();
        lblDataEntryMarketsMarket = new javax.swing.JLabel();
        lblDataEntryMarketsMonth = new javax.swing.JLabel();
        lblDataEntryMarketsYear = new javax.swing.JLabel();
        cboDataEntryMarketsMarket = new javax.swing.JComboBox();
        cboDataEntryMarketsMonth = new javax.swing.JComboBox();
        cboDataEntryMarketsYear = new javax.swing.JComboBox();
        pnlDataEntryMarketsBottom = new javax.swing.JPanel();
        btnDataEntryMarketsSaveSend = new javax.swing.JButton();
        btnDataEntryMarketsSave = new javax.swing.JButton();
        btnDataEntryMarketsFetch = new javax.swing.JButton();
        btnDataEntryMarketsExit = new javax.swing.JButton();
        btnDataEntryMarketsClear = new javax.swing.JButton();
        pnlDataEntrySlimsPart1 = new javax.swing.JPanel();
        scpDataEntrySlimsPart1 = new javax.swing.JScrollPane();
        //tblDataEntrySlimsPart1 = new javax.swing.JTable();
		
      tblDataEntryMarkets = new javax.swing.JTable(tbmDataEntryMarkets) {
      public Component prepareRenderer(TableCellRenderer renderer, int row, int
      col) { Component comp = super.prepareRenderer(renderer, row, col); if
      (dataEntry.marketIndicatorCategoryRows.contains(row)) {
      comp.setBackground(new java.awt.Color(153, 204, 255)); } else {
      comp.setBackground(Color.white); } return comp; }
     
      public boolean isCellEditable(int row, int column) { if
      (dataEntry.marketIndicatorCategoryRows.contains(row) || column == 0) {
      return false; } else { return true; 
	  } 
	  } 
	  };
     
      tblDataEntrySlimsPart1 = new javax.swing.JTable(tbmDataEntrySlimsPart1) {
      public Component prepareRenderer(TableCellRenderer renderer, int row, int
      col) { Component comp = super.prepareRenderer(renderer, row, col); if
      (dataEntry.slimsPart1IndicatorCategoryRows.contains(row)) {
      comp.setBackground(new java.awt.Color(153, 204, 255)); } else if
      ((dataEntry.slimsOneLookUpRows.contains(row) ||
      dataEntry.slimsOneDetailsRows.contains(row)) && col > 1) {
      comp.setBackground(Color.gray); } else { comp.setBackground(Color.white);
      } return comp; }
     
      public boolean isCellEditable(int row, int column) { if
      (dataEntry.slimsPart1IndicatorCategoryRows.contains(row) || column == 0)
      { return false; } else if ((dataEntry.slimsOneLookUpRows.contains(row) ||
      dataEntry.slimsOneDetailsRows.contains(row)) && column > 1) { return
      false; } else { return true; } } };
     
      tblDataEntrySlimsPart2 = new javax.swing.JTable(tbmDataEntrySlimsPart2) {
      public Component prepareRenderer(TableCellRenderer renderer, int row, int
      col) { Component comp = super.prepareRenderer(renderer, row, col); if
      (dataEntry.slimsPart2IndicatorCategoryRows.contains(row)) {
      comp.setBackground(new java.awt.Color(153, 204, 255)); } else {
      comp.setBackground(Color.white); } return comp; }
     
      public boolean isCellEditable(int row, int column) { if
      (dataEntry.slimsPart2IndicatorCategoryRows.contains(row) || column == 0)
      { return false; 
	  } else { 
	  return true; 
	  } 
	  } 
	  };		
		
        pnlDataEntrySlimsPart1Top = new javax.swing.JPanel();
        lblDataEntrySlimsPart1Node = new javax.swing.JLabel();
        lblDataEntrySlimsPart1Month = new javax.swing.JLabel();
        lblDataEntrySlimsPart1Year = new javax.swing.JLabel();
        cboDataEntrySlimsPart1Node = new javax.swing.JComboBox();
        cboDataEntrySlimsPart1Month = new javax.swing.JComboBox();
        cboDataEntrySlimsPart1Year = new javax.swing.JComboBox();
        pnlDataEntrySlimsPart1Bottom = new javax.swing.JPanel();
        btnDataEntrySlimsPart1SaveSend = new javax.swing.JButton();
        btnDataEntrySlimsPart1Save = new javax.swing.JButton();
        btnDataEntrySlimsPart1Fetch = new javax.swing.JButton();
        btnDataEntrySlimsPart1Exit = new javax.swing.JButton();
        btnDataEntrySlimsPart1Clear = new javax.swing.JButton();
        pnlDataEntrySlimsPart1Comments = new javax.swing.JPanel();
        lblDataEntrySlimsPart1Comments = new javax.swing.JLabel();
        scpDataEntrySlimsPart1comments = new javax.swing.JScrollPane();
        txaDataEntrySlimsPart1comments = new javax.swing.JTextArea();
        tbpDataEntry = new javax.swing.JTabbedPane();

        tblDataEntrySlimsPart2.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblDataEntrySlimsPart2.setModel(tbmDataEntrySlimsPart2);
        tblDataEntrySlimsPart2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDataEntrySlimsPart2.setCellSelectionEnabled(true);
        tblDataEntrySlimsPart2.setGridColor(Color.gray);
        tblDataEntrySlimsPart2.setInheritsPopupMenu(true);
        tblDataEntrySlimsPart2.setRowHeight(20);
        tblDataEntrySlimsPart2.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblDataEntrySlimsPart2.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpDataEntrySlimsPart2.setViewportView(tblDataEntrySlimsPart2);
        tblDataEntrySlimsPart2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        lblDataEntrySlimsPart2Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart2Node.setLabelFor(cboDataEntryMarketsMarket);
        lblDataEntrySlimsPart2Node.setText("Node");

        lblDataEntrySlimsPart2Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart2Month.setLabelFor(cboDataEntryMarketsMonth);
        lblDataEntrySlimsPart2Month.setText("Month");

        lblDataEntrySlimsPart2Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart2Year.setLabelFor(cboDataEntryMarketsYear);
        lblDataEntrySlimsPart2Year.setText("Year");

        cboDataEntrySlimsPart2Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N

        cboDataEntrySlimsPart2Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataEntrySlimsPart2Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        cboDataEntrySlimsPart2Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataEntrySlimsPart2Year.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        cboDataEntrySlimsPart2Year.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));

        javax.swing.GroupLayout pnlDataEntrySlimsPart2TopLayout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart2Top);
        pnlDataEntrySlimsPart2Top.setLayout(pnlDataEntrySlimsPart2TopLayout);
        pnlDataEntrySlimsPart2TopLayout.setHorizontalGroup(
            pnlDataEntrySlimsPart2TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataEntrySlimsPart2Node)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntrySlimsPart2Node, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataEntrySlimsPart2Month)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntrySlimsPart2Month, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataEntrySlimsPart2Year, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntrySlimsPart2Year, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataEntrySlimsPart2TopLayout.setVerticalGroup(
            pnlDataEntrySlimsPart2TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataEntrySlimsPart2TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataEntrySlimsPart2Node)
                    .addComponent(lblDataEntrySlimsPart2Month)
                    .addComponent(lblDataEntrySlimsPart2Year)
                    .addComponent(cboDataEntrySlimsPart2Node, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataEntrySlimsPart2Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataEntrySlimsPart2Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnDataEntrySlimsPart2SaveSend.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart2SaveSend.setMnemonic('S');
        btnDataEntrySlimsPart2SaveSend.setText("Save & Send");
        btnDataEntrySlimsPart2SaveSend.setToolTipText("Save and send data. Prefer when enetring data with internect connection available");
        btnDataEntrySlimsPart2SaveSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart2SaveSendActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart2Save.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart2Save.setMnemonic('e');
        btnDataEntrySlimsPart2Save.setText("Save");
        btnDataEntrySlimsPart2Save.setToolTipText("Save data without sending. Prefer this when enetering data with no internet connection available");
        btnDataEntrySlimsPart2Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart2SaveActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart2Fetch.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart2Fetch.setMnemonic('F');
        btnDataEntrySlimsPart2Fetch.setText("Fetch Saved Data");
        btnDataEntrySlimsPart2Fetch.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnDataEntrySlimsPart2Fetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart2FetchActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart2Exit.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart2Exit.setMnemonic('x');
        btnDataEntrySlimsPart2Exit.setText("Exit");
        btnDataEntrySlimsPart2Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart2ExitActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart2Clear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart2Clear.setMnemonic('C');
        btnDataEntrySlimsPart2Clear.setText("Clear");
        btnDataEntrySlimsPart2Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart2ClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataEntrySlimsPart2BottomLayout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart2Bottom);
        pnlDataEntrySlimsPart2Bottom.setLayout(pnlDataEntrySlimsPart2BottomLayout);
        pnlDataEntrySlimsPart2BottomLayout.setHorizontalGroup(
            pnlDataEntrySlimsPart2BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2BottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDataEntrySlimsPart2SaveSend)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntrySlimsPart2Save)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntrySlimsPart2Fetch)
                .addGap(161, 161, 161)
                .addComponent(btnDataEntrySlimsPart2Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntrySlimsPart2Exit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataEntrySlimsPart2BottomLayout.setVerticalGroup(
            pnlDataEntrySlimsPart2BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2BottomLayout.createSequentialGroup()
                .addGroup(pnlDataEntrySlimsPart2BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDataEntrySlimsPart2SaveSend)
                    .addComponent(btnDataEntrySlimsPart2Save)
                    .addComponent(btnDataEntrySlimsPart2Fetch)
                    .addComponent(btnDataEntrySlimsPart2Exit)
                    .addComponent(btnDataEntrySlimsPart2Clear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblDataEntrySlimsPart2Comments.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart2Comments.setText("COMMENTS");

        txaDataEntrySlimsPart2comments.setColumns(20);
        txaDataEntrySlimsPart2comments.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        txaDataEntrySlimsPart2comments.setRows(5);
        scpDataEntrySlimsPart2comments.setViewportView(txaDataEntrySlimsPart2comments);

        javax.swing.GroupLayout pnlDataEntrySlimsPart2CommentsLayout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart2Comments);
        pnlDataEntrySlimsPart2Comments.setLayout(pnlDataEntrySlimsPart2CommentsLayout);
        pnlDataEntrySlimsPart2CommentsLayout.setHorizontalGroup(
            pnlDataEntrySlimsPart2CommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2CommentsLayout.createSequentialGroup()
                .addComponent(lblDataEntrySlimsPart2Comments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(807, 807, 807))
            .addComponent(scpDataEntrySlimsPart2comments)
        );
        pnlDataEntrySlimsPart2CommentsLayout.setVerticalGroup(
            pnlDataEntrySlimsPart2CommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2CommentsLayout.createSequentialGroup()
                .addComponent(lblDataEntrySlimsPart2Comments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scpDataEntrySlimsPart2comments, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlDataEntrySlimsPart2Layout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart2);
        pnlDataEntrySlimsPart2.setLayout(pnlDataEntrySlimsPart2Layout);
        pnlDataEntrySlimsPart2Layout.setHorizontalGroup(
            pnlDataEntrySlimsPart2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDataEntrySlimsPart2Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDataEntrySlimsPart2Bottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scpDataEntrySlimsPart2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnlDataEntrySlimsPart2Comments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDataEntrySlimsPart2Layout.setVerticalGroup(
            pnlDataEntrySlimsPart2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart2Layout.createSequentialGroup()
                .addComponent(pnlDataEntrySlimsPart2Top, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpDataEntrySlimsPart2, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDataEntrySlimsPart2Comments, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDataEntrySlimsPart2Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblDataEntryMarkets.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblDataEntryMarkets.setModel(tbmDataEntryMarkets);
        tblDataEntryMarkets.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDataEntryMarkets.setCellSelectionEnabled(true);
        tblDataEntryMarkets.setGridColor(Color.gray);
        tblDataEntryMarkets.setRowHeight(20);
        tblDataEntryMarkets.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblDataEntryMarkets.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpDataEntryMarkets.setViewportView(tblDataEntryMarkets);

        lblDataEntryMarketsMarket.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntryMarketsMarket.setLabelFor(cboDataEntryMarketsMarket);
        lblDataEntryMarketsMarket.setText("Market");

        lblDataEntryMarketsMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntryMarketsMonth.setLabelFor(cboDataEntryMarketsMonth);
        lblDataEntryMarketsMonth.setText("Month");

        lblDataEntryMarketsYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntryMarketsYear.setLabelFor(cboDataEntryMarketsYear);
        lblDataEntryMarketsYear.setText("Year");

        cboDataEntryMarketsMarket.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N

        cboDataEntryMarketsMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataEntryMarketsMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        cboDataEntryMarketsYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataEntryMarketsYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        cboDataEntryMarketsYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));

        javax.swing.GroupLayout pnlDataEntryMarketsTopLayout = new javax.swing.GroupLayout(pnlDataEntryMarketsTop);
        pnlDataEntryMarketsTop.setLayout(pnlDataEntryMarketsTopLayout);
        pnlDataEntryMarketsTopLayout.setHorizontalGroup(
            pnlDataEntryMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntryMarketsTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataEntryMarketsMarket)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntryMarketsMarket, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataEntryMarketsMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntryMarketsMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataEntryMarketsYear, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntryMarketsYear, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataEntryMarketsTopLayout.setVerticalGroup(
            pnlDataEntryMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntryMarketsTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataEntryMarketsTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataEntryMarketsMarket)
                    .addComponent(lblDataEntryMarketsMonth)
                    .addComponent(lblDataEntryMarketsYear)
                    .addComponent(cboDataEntryMarketsMarket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataEntryMarketsMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataEntryMarketsYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnDataEntryMarketsSaveSend.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntryMarketsSaveSend.setMnemonic('S');
        btnDataEntryMarketsSaveSend.setText("Save & Send");
        btnDataEntryMarketsSaveSend.setToolTipText("Save and send data. Prefer when enetring data with internect connection available");
        btnDataEntryMarketsSaveSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntryMarketsSaveSendActionPerformed(evt);
            }
        });

        btnDataEntryMarketsSave.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntryMarketsSave.setMnemonic('e');
        btnDataEntryMarketsSave.setText("Save");
        btnDataEntryMarketsSave.setToolTipText("Save data without sending. Prefer this when enetering data with no internet connection available");
        btnDataEntryMarketsSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntryMarketsSaveActionPerformed(evt);
            }
        });

        btnDataEntryMarketsFetch.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntryMarketsFetch.setMnemonic('F');
        btnDataEntryMarketsFetch.setText("Fetch Saved Data");
        btnDataEntryMarketsFetch.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnDataEntryMarketsFetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntryMarketsFetchActionPerformed(evt);
            }
        });

        btnDataEntryMarketsExit.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntryMarketsExit.setMnemonic('x');
        btnDataEntryMarketsExit.setText("Exit");
        btnDataEntryMarketsExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntryMarketsExitActionPerformed(evt);
            }
        });

        btnDataEntryMarketsClear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntryMarketsClear.setMnemonic('C');
        btnDataEntryMarketsClear.setText("Clear");
        btnDataEntryMarketsClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntryMarketsClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataEntryMarketsBottomLayout = new javax.swing.GroupLayout(pnlDataEntryMarketsBottom);
        pnlDataEntryMarketsBottom.setLayout(pnlDataEntryMarketsBottomLayout);
        pnlDataEntryMarketsBottomLayout.setHorizontalGroup(
            pnlDataEntryMarketsBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntryMarketsBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDataEntryMarketsSaveSend)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntryMarketsSave)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntryMarketsFetch)
                .addGap(161, 161, 161)
                .addComponent(btnDataEntryMarketsClear, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntryMarketsExit)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        pnlDataEntryMarketsBottomLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDataEntryMarketsClear, btnDataEntryMarketsExit, btnDataEntryMarketsFetch, btnDataEntryMarketsSave, btnDataEntryMarketsSaveSend});

        pnlDataEntryMarketsBottomLayout.setVerticalGroup(
            pnlDataEntryMarketsBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntryMarketsBottomLayout.createSequentialGroup()
                .addGroup(pnlDataEntryMarketsBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDataEntryMarketsSaveSend)
                    .addComponent(btnDataEntryMarketsSave)
                    .addComponent(btnDataEntryMarketsFetch)
                    .addComponent(btnDataEntryMarketsExit)
                    .addComponent(btnDataEntryMarketsClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDataEntryMarketsBottomLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDataEntryMarketsClear, btnDataEntryMarketsExit, btnDataEntryMarketsFetch, btnDataEntryMarketsSave, btnDataEntryMarketsSaveSend});

        javax.swing.GroupLayout pnlDataEntryMarketsLayout = new javax.swing.GroupLayout(pnlDataEntryMarkets);
        pnlDataEntryMarkets.setLayout(pnlDataEntryMarketsLayout);
        pnlDataEntryMarketsLayout.setHorizontalGroup(
            pnlDataEntryMarketsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpDataEntryMarkets)
            .addComponent(pnlDataEntryMarketsTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDataEntryMarketsBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDataEntryMarketsLayout.setVerticalGroup(
            pnlDataEntryMarketsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntryMarketsLayout.createSequentialGroup()
                .addComponent(pnlDataEntryMarketsTop, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpDataEntryMarkets, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDataEntryMarketsBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblDataEntrySlimsPart1.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblDataEntrySlimsPart1.setModel(tbmDataEntrySlimsPart1);
        tblDataEntrySlimsPart1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDataEntrySlimsPart1.setCellSelectionEnabled(true);
        tblDataEntrySlimsPart1.setGridColor(Color.gray);
        tblDataEntrySlimsPart1.setRowHeight(20);
        tblDataEntrySlimsPart1.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblDataEntrySlimsPart1.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpDataEntrySlimsPart1.setViewportView(tblDataEntrySlimsPart1);
        tblDataEntrySlimsPart1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        lblDataEntrySlimsPart1Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart1Node.setLabelFor(cboDataEntryMarketsMarket);
        lblDataEntrySlimsPart1Node.setText("Node");

        lblDataEntrySlimsPart1Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart1Month.setLabelFor(cboDataEntryMarketsMonth);
        lblDataEntrySlimsPart1Month.setText("Month");

        lblDataEntrySlimsPart1Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart1Year.setLabelFor(cboDataEntryMarketsYear);
        lblDataEntrySlimsPart1Year.setText("Year");

        cboDataEntrySlimsPart1Node.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N

        cboDataEntrySlimsPart1Month.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataEntrySlimsPart1Month.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        cboDataEntrySlimsPart1Year.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        cboDataEntrySlimsPart1Year.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        cboDataEntrySlimsPart1Year.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));

        javax.swing.GroupLayout pnlDataEntrySlimsPart1TopLayout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart1Top);
        pnlDataEntrySlimsPart1Top.setLayout(pnlDataEntrySlimsPart1TopLayout);
        pnlDataEntrySlimsPart1TopLayout.setHorizontalGroup(
            pnlDataEntrySlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataEntrySlimsPart1Node)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntrySlimsPart1Node, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataEntrySlimsPart1Month)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntrySlimsPart1Month, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDataEntrySlimsPart1Year, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDataEntrySlimsPart1Year, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataEntrySlimsPart1TopLayout.setVerticalGroup(
            pnlDataEntrySlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataEntrySlimsPart1TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataEntrySlimsPart1Node)
                    .addComponent(lblDataEntrySlimsPart1Month)
                    .addComponent(lblDataEntrySlimsPart1Year)
                    .addComponent(cboDataEntrySlimsPart1Node, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataEntrySlimsPart1Month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDataEntrySlimsPart1Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnDataEntrySlimsPart1SaveSend.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart1SaveSend.setMnemonic('S');
        btnDataEntrySlimsPart1SaveSend.setText("Save & Send");
        btnDataEntrySlimsPart1SaveSend.setToolTipText("Save and send data. Prefer when enetring data with internect connection available");
        btnDataEntrySlimsPart1SaveSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart1SaveSendActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart1Save.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart1Save.setMnemonic('e');
        btnDataEntrySlimsPart1Save.setText("Save");
        btnDataEntrySlimsPart1Save.setToolTipText("Save data without sending. Prefer this when enetering data with no internet connection available");
        btnDataEntrySlimsPart1Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart1SaveActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart1Fetch.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart1Fetch.setMnemonic('F');
        btnDataEntrySlimsPart1Fetch.setText("Fetch Saved Data");
        btnDataEntrySlimsPart1Fetch.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnDataEntrySlimsPart1Fetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart1FetchActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart1Exit.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart1Exit.setMnemonic('x');
        btnDataEntrySlimsPart1Exit.setText("Exit");
        btnDataEntrySlimsPart1Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart1ExitActionPerformed(evt);
            }
        });

        btnDataEntrySlimsPart1Clear.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnDataEntrySlimsPart1Clear.setMnemonic('C');
        btnDataEntrySlimsPart1Clear.setText("Clear");
        btnDataEntrySlimsPart1Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDataEntrySlimsPart1ClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDataEntrySlimsPart1BottomLayout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart1Bottom);
        pnlDataEntrySlimsPart1Bottom.setLayout(pnlDataEntrySlimsPart1BottomLayout);
        pnlDataEntrySlimsPart1BottomLayout.setHorizontalGroup(
            pnlDataEntrySlimsPart1BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1BottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDataEntrySlimsPart1SaveSend)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntrySlimsPart1Save, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntrySlimsPart1Fetch)
                .addGap(161, 161, 161)
                .addComponent(btnDataEntrySlimsPart1Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDataEntrySlimsPart1Exit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataEntrySlimsPart1BottomLayout.setVerticalGroup(
            pnlDataEntrySlimsPart1BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1BottomLayout.createSequentialGroup()
                .addGroup(pnlDataEntrySlimsPart1BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDataEntrySlimsPart1SaveSend)
                    .addComponent(btnDataEntrySlimsPart1Save)
                    .addComponent(btnDataEntrySlimsPart1Fetch)
                    .addComponent(btnDataEntrySlimsPart1Exit)
                    .addComponent(btnDataEntrySlimsPart1Clear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblDataEntrySlimsPart1Comments.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        lblDataEntrySlimsPart1Comments.setText("COMMENTS");

        txaDataEntrySlimsPart1comments.setColumns(20);
        txaDataEntrySlimsPart1comments.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        txaDataEntrySlimsPart1comments.setRows(5);
        scpDataEntrySlimsPart1comments.setViewportView(txaDataEntrySlimsPart1comments);

        javax.swing.GroupLayout pnlDataEntrySlimsPart1CommentsLayout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart1Comments);
        pnlDataEntrySlimsPart1Comments.setLayout(pnlDataEntrySlimsPart1CommentsLayout);
        pnlDataEntrySlimsPart1CommentsLayout.setHorizontalGroup(
            pnlDataEntrySlimsPart1CommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1CommentsLayout.createSequentialGroup()
                .addComponent(lblDataEntrySlimsPart1Comments, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addGap(807, 807, 807))
            .addComponent(scpDataEntrySlimsPart1comments)
        );
        pnlDataEntrySlimsPart1CommentsLayout.setVerticalGroup(
            pnlDataEntrySlimsPart1CommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1CommentsLayout.createSequentialGroup()
                .addComponent(lblDataEntrySlimsPart1Comments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scpDataEntrySlimsPart1comments, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlDataEntrySlimsPart1Layout = new javax.swing.GroupLayout(pnlDataEntrySlimsPart1);
        pnlDataEntrySlimsPart1.setLayout(pnlDataEntrySlimsPart1Layout);
        pnlDataEntrySlimsPart1Layout.setHorizontalGroup(
            pnlDataEntrySlimsPart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpDataEntrySlimsPart1)
            .addComponent(pnlDataEntrySlimsPart1Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDataEntrySlimsPart1Bottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDataEntrySlimsPart1Comments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDataEntrySlimsPart1Layout.setVerticalGroup(
            pnlDataEntrySlimsPart1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataEntrySlimsPart1Layout.createSequentialGroup()
                .addComponent(pnlDataEntrySlimsPart1Top, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpDataEntrySlimsPart1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDataEntrySlimsPart1Comments, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDataEntrySlimsPart1Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setTitle("DATA ENTRY");

        tbpDataEntry.setFont(new java.awt.Font("Arial Unicode MS", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpDataEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpDataEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDataEntryMarketsExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryMarketsExitActionPerformed
        showConfirmDialog();
    }//GEN-LAST:event_btnDataEntryMarketsExitActionPerformed
    private void showConfirmDialog() {
        // Allow user to conmfirm whether they want to exit
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
        if (result <= 0) {
            this.dispose();
        }
    }

    private String[] getSupplyComboBoxItems() {
        String[] supplyComboBoxItems = {"(5) Surplus", "(4) Above Normal", "(3) Normal", "(2) Below Normal",
            "(1) Scarce", "(0) Not Available"};
        return supplyComboBoxItems;
    }

    private String[] getDataTrustLevelComboBoxItems() {
        String[] dataTrustLevelComboBoxItems = {"Poor", "Fair", "Good"};
        return dataTrustLevelComboBoxItems;
    }
    private void btnDataEntryMarketsClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryMarketsClearActionPerformed
        // Clear data entry markets table
        dataEntry.clearTable(tblDataEntryMarkets);
    }//GEN-LAST:event_btnDataEntryMarketsClearActionPerformed

    private void btnDataEntryMarketsSaveSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryMarketsSaveSendActionPerformed
        // Insert market data 
        int result = 0;
        //Application Id is the id value as given in application table of db. 2 is for Slims Part 1
        int applicationId = 2;
        try {
            pnlDataEntryMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Validate supply column
            if (dataEntry.validateSupplyValues(tblDataEntryMarkets)) {
                result = dataEntry.saveMarketData(tblDataEntryMarkets, cboDataEntryMarketsYear, cboDataEntryMarketsMonth,
                        cboDataEntryMarketsMarket, applicationId);
                if (result > 0) {
                    if (dataEntry.sendEmail()) {
                        JOptionPane.showMessageDialog(null, "Data saved and sent successfully", "Success Saving & Sending Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        //Clear table
                        dataEntry.clearTable(tblDataEntryMarkets);
                    }
                    //JOptionPane.showMessageDialog(null, "Data Saved Successfully",
                    //"Success Saving data", JOptionPane.INFORMATION_MESSAGE);
                } else if (result == 0) {
                    JOptionPane.showMessageDialog(null, "Data not saved. Please try again", "Unsuccessful saving data",
                            JOptionPane.WARNING_MESSAGE);
                } else if (result == -1) {
                    JOptionPane.showMessageDialog(null, "This data already exists, it had been saved before",
                            "Data Exists", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "One or more supply values have not been selected"
                        + "\n\nPlease update the supply values for the affected rows before proceeding", "Supply column",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception exception) {
        } finally {
            pnlDataEntryMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnDataEntryMarketsSaveSendActionPerformed

    private void btnDataEntryMarketsSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryMarketsSaveActionPerformed
        //Save market data
        try {
            pnlDataEntryMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int result = 0;
            //Application Id is the id value as given in application table of db. 1 is for markets
            int applicationId = 1;
            if (dataEntry.validateSupplyValues(tblDataEntryMarkets)) {
                result = dataEntry.saveMarketData(tblDataEntryMarkets, cboDataEntryMarketsYear, cboDataEntryMarketsMonth,
                        cboDataEntryMarketsMarket, applicationId);
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Data saved successfully", "Success Saving Data",
                            JOptionPane.INFORMATION_MESSAGE);
                    //Clear table
                    dataEntry.clearTable(tblDataEntryMarkets);
                } else if (result == 0) {
                    JOptionPane.showMessageDialog(null, "Data not saved. Please try again", "Unsuccessful saving data",
                            JOptionPane.WARNING_MESSAGE);
                } else if (result == -1) {
                    JOptionPane.showMessageDialog(null, "This data already exists, it had been saved before",
                            "Data Exists", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "One or more supply values have not been selected"
                        + "\n\nPlease update the supply values for the affected rows before proceeding", "Supply column",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception exception) {
        } finally {
            pnlDataEntryMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnDataEntryMarketsSaveActionPerformed

    private void btnDataEntrySlimsPart2SaveSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart2SaveSendActionPerformed
        //Save Slims part 2 and send xml file
        int year = 0;
        int monthId = 0;
        int marketId = 0;
        int result = 0;
        String marketName = "";
        String monthName = "";

        try {
            pnlDataEntrySlimsPart2.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            marketName = cboDataEntrySlimsPart2Node.getSelectedItem().toString();
            monthName = cboDataEntrySlimsPart2Month.getSelectedItem().toString();
            year = Integer.parseInt(cboDataEntrySlimsPart2Year.getSelectedItem().toString());
            monthId = cboDataEntrySlimsPart2Month.getSelectedIndex() + 1;
            marketId = market.getMarketId(marketName);
            result = dataEntry.saveSlimsPart2Data(tblDataEntrySlimsPart2, txaDataEntrySlimsPart2comments, 
                    year, monthId, marketId, marketName, monthName);

            if (result > 0) {
                if (dataEntry.sendEmail()) {
                    JOptionPane.showMessageDialog(null, "Data saved and sent successfully", "Saving & Sending Data",
                            JOptionPane.INFORMATION_MESSAGE);
                    //Clear table
                    dataEntry.clearTable(tblDataEntrySlimsPart2);
                    //Clear comments
                    txaDataEntrySlimsPart2comments.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Data saving & sending was not successful", "Saving & Sending Data",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else if (result == 0) {
                JOptionPane.showMessageDialog(null, "Data not saved. Please try again", "Saving data",
                        JOptionPane.WARNING_MESSAGE);
            } else if (result == -1) {
                JOptionPane.showMessageDialog(null, "This data already exists, it had been saved before",
                        "Data Exists", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception exception) {
        } finally {
            pnlDataEntrySlimsPart2.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnDataEntrySlimsPart2SaveSendActionPerformed

    private void btnDataEntrySlimsPart2SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart2SaveActionPerformed
        //Save Slims Part 2
        saveSlimsPart2(tblDataEntrySlimsPart2);
    }//GEN-LAST:event_btnDataEntrySlimsPart2SaveActionPerformed

    private void btnDataEntrySlimsPart2ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart2ExitActionPerformed
        showConfirmDialog();
    }//GEN-LAST:event_btnDataEntrySlimsPart2ExitActionPerformed

    private void btnDataEntrySlimsPart2ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart2ClearActionPerformed
        // Clear data entry markets table
        dataEntry.clearTable(tblDataEntrySlimsPart2);
        //Clear comments
        txaDataEntrySlimsPart2comments.setText("");
    }//GEN-LAST:event_btnDataEntrySlimsPart2ClearActionPerformed

    private void btnDataEntrySlimsPart1SaveSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart1SaveSendActionPerformed
        //Save Slims part 1 data
        try {
            pnlDataEntrySlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int result = 0;
            int applicationId = 2;

            result = dataEntry.saveSlimsPart1Data(tblDataEntrySlimsPart1, cboDataEntrySlimsPart1Year,
                    cboDataEntrySlimsPart1Month, cboDataEntrySlimsPart1Node, txaDataEntrySlimsPart1comments, applicationId);

            if (result > 0) {
                //Send email
                if (dataEntry.sendEmail()) {
                    JOptionPane.showMessageDialog(null, "Data saved and sent successfully", "Saving & Sending Data",
                            JOptionPane.INFORMATION_MESSAGE);
                    //Clear table
                    dataEntry.clearTable(tblDataEntrySlimsPart1);
                    //Clear comments
                    txaDataEntrySlimsPart1comments.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Data saving & sending was not successful", "Saving & Sending Data",
                            JOptionPane.WARNING_MESSAGE);
                }
                //Clear table
                //dataEntry.clearTable(tblDataEntryMarkets);
            } else if (result == 0) {
                JOptionPane.showMessageDialog(null, "Data not saved. Please try again", "Saving data",
                        JOptionPane.WARNING_MESSAGE);
            } else if (result == -1) {
                JOptionPane.showMessageDialog(null, "This data already exists, it had been saved before",
                        "Data Exists", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception exception) {
        } finally {
            pnlDataEntrySlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));       
        }
    }//GEN-LAST:event_btnDataEntrySlimsPart1SaveSendActionPerformed
    
    private void saveSlimsPart2(javax.swing.JTable table) {
        int year = 0;
        int monthId = 0;
        int marketId = 0;
        int result = 0;
        String monthName = "";
        String marketName = "";

        monthName = cboDataEntrySlimsPart2Month.getSelectedItem().toString();
        marketName = cboDataEntrySlimsPart2Node.getSelectedItem().toString();
        year = Integer.parseInt(cboDataEntrySlimsPart2Year.getSelectedItem().toString());
        monthId = cboDataEntrySlimsPart2Month.getSelectedIndex() + 1;
        marketId = market.getMarketId(marketName);
        result = dataEntry.saveSlimsPart2Data(tblDataEntrySlimsPart2, txaDataEntrySlimsPart2comments,
                year, monthId, marketId, marketName, monthName);

        if (result > 0) {
            JOptionPane.showMessageDialog(null, "Data saved successfully", "Saving Data",
                    JOptionPane.INFORMATION_MESSAGE);
            //Clear table
            dataEntry.clearTable(tblDataEntrySlimsPart2);
            //Clear comments
            txaDataEntrySlimsPart2comments.setText("");
        } else if (result == 0) {
            JOptionPane.showMessageDialog(null, "Data not saved. Please try again", "Saving data",
                    JOptionPane.WARNING_MESSAGE);
        } else if (result == -1) {
            JOptionPane.showMessageDialog(null, "This data already exists, it had been saved before",
                    "Data Exists", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnDataEntrySlimsPart1SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart1SaveActionPerformed
        //Save Slims part 1 data
        try {
            pnlDataEntrySlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int result = 0;
            int applicationId = 2;

            result = dataEntry.saveSlimsPart1Data(tblDataEntrySlimsPart1, cboDataEntrySlimsPart1Year,
                    cboDataEntrySlimsPart1Month, cboDataEntrySlimsPart1Node, txaDataEntrySlimsPart1comments, applicationId);

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data saved successfully", "Saving Data",
                        JOptionPane.INFORMATION_MESSAGE);
                //Clear table
                dataEntry.clearTable(tblDataEntryMarkets);
            } else if (result == 0) {
                JOptionPane.showMessageDialog(null, "Data not saved. Please try again", "Saving data",
                        JOptionPane.WARNING_MESSAGE);
            } else if (result == -1) {
                JOptionPane.showMessageDialog(null, "This data already exists, it had been saved before",
                        "Data Exists", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception exception) {
        } finally {
            pnlDataEntrySlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnDataEntrySlimsPart1SaveActionPerformed

    private void btnDataEntrySlimsPart1ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart1ExitActionPerformed
        showConfirmDialog();
    }//GEN-LAST:event_btnDataEntrySlimsPart1ExitActionPerformed

    private void btnDataEntrySlimsPart1ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart1ClearActionPerformed
        // Clear data entry markets table
        dataEntry.clearTable(tblDataEntrySlimsPart1);
        //Clear commments
        txaDataEntrySlimsPart1comments.setText("");
    }//GEN-LAST:event_btnDataEntrySlimsPart1ClearActionPerformed

    private void btnDataEntryMarketsFetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntryMarketsFetchActionPerformed
        // Fetch market data and load on table
        try {
            pnlDataEntryMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int year = Integer.parseInt(cboDataEntryMarketsYear.getSelectedItem().toString());
            int monthId = cboDataEntryMarketsMonth.getSelectedIndex() + 1;
            String marketName = cboDataEntryMarketsMarket.getSelectedItem().toString();
            dataEntry.fetchMarketPrices(year, monthId, marketName, tblDataEntryMarkets);
        } catch (Exception exception) {
        } finally {
            pnlDataEntryMarkets.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnDataEntryMarketsFetchActionPerformed

    private void btnDataEntrySlimsPart2FetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart2FetchActionPerformed
        // Fetch saved slims part 2 data
        int year = 0;
        int monthId = 0;
        int marketId = 0;

        year = Integer.parseInt(cboDataEntrySlimsPart2Year.getSelectedItem().toString());
        monthId = cboDataEntrySlimsPart2Month.getSelectedIndex() + 1;
        marketId = market.getMarketId(cboDataEntrySlimsPart2Node.getSelectedItem().toString());
        dataEntry.fetchSlimsPart2Prices(year, monthId, marketId, tblDataEntrySlimsPart2, txaDataEntrySlimsPart2comments, "entry");
    }//GEN-LAST:event_btnDataEntrySlimsPart2FetchActionPerformed

    private void btnDataEntrySlimsPart1FetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDataEntrySlimsPart1FetchActionPerformed
        // Fetch saved data
        int year = 0;
        int monthId = 0;
        String marketName = "";
        year = Integer.parseInt(cboDataEntrySlimsPart1Year.getSelectedItem().toString());
        monthId = cboDataEntrySlimsPart1Month.getSelectedIndex() + 1;
        marketName = cboDataEntrySlimsPart1Node.getSelectedItem().toString();
        String function = "entry";

        try {
            pnlDataEntrySlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Clear previous comments
            txaDataEntrySlimsPart1comments.setText("");
            //Fetch and load prices
            dataEntry.fetchSlimsPart1Data(year, monthId, marketName, txaDataEntrySlimsPart1comments, function, tblDataEntrySlimsPart1);
        } catch (Exception exception) {
            //exception.printStackTrace();
        } finally {
            pnlDataEntrySlimsPart1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnDataEntrySlimsPart1FetchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(DataEntryUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataEntryUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataEntryUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataEntryUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                DataEntryUi dialog = new DataEntryUi(new javax.swing.JFrame(), true);
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
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDataEntryMarketsClear;
    private javax.swing.JButton btnDataEntryMarketsExit;
    private javax.swing.JButton btnDataEntryMarketsFetch;
    private javax.swing.JButton btnDataEntryMarketsSave;
    private javax.swing.JButton btnDataEntryMarketsSaveSend;
    private javax.swing.JButton btnDataEntrySlimsPart1Clear;
    private javax.swing.JButton btnDataEntrySlimsPart1Exit;
    private javax.swing.JButton btnDataEntrySlimsPart1Fetch;
    private javax.swing.JButton btnDataEntrySlimsPart1Save;
    private javax.swing.JButton btnDataEntrySlimsPart1SaveSend;
    private javax.swing.JButton btnDataEntrySlimsPart2Clear;
    private javax.swing.JButton btnDataEntrySlimsPart2Exit;
    private javax.swing.JButton btnDataEntrySlimsPart2Fetch;
    private javax.swing.JButton btnDataEntrySlimsPart2Save;
    private javax.swing.JButton btnDataEntrySlimsPart2SaveSend;
    private javax.swing.JComboBox cboDataEntryMarketsMarket;
    private javax.swing.JComboBox cboDataEntryMarketsMonth;
    private javax.swing.JComboBox cboDataEntryMarketsYear;
    private javax.swing.JComboBox cboDataEntrySlimsPart1Month;
    private javax.swing.JComboBox cboDataEntrySlimsPart1Node;
    private javax.swing.JComboBox cboDataEntrySlimsPart1Year;
    private javax.swing.JComboBox cboDataEntrySlimsPart2Month;
    private javax.swing.JComboBox cboDataEntrySlimsPart2Node;
    private javax.swing.JComboBox cboDataEntrySlimsPart2Year;
    private javax.swing.JLabel lblDataEntryMarketsMarket;
    private javax.swing.JLabel lblDataEntryMarketsMonth;
    private javax.swing.JLabel lblDataEntryMarketsYear;
    private javax.swing.JLabel lblDataEntrySlimsPart1Comments;
    private javax.swing.JLabel lblDataEntrySlimsPart1Month;
    private javax.swing.JLabel lblDataEntrySlimsPart1Node;
    private javax.swing.JLabel lblDataEntrySlimsPart1Year;
    private javax.swing.JLabel lblDataEntrySlimsPart2Comments;
    private javax.swing.JLabel lblDataEntrySlimsPart2Month;
    private javax.swing.JLabel lblDataEntrySlimsPart2Node;
    private javax.swing.JLabel lblDataEntrySlimsPart2Year;
    public javax.swing.JPanel pnlDataEntryMarkets;
    private javax.swing.JPanel pnlDataEntryMarketsBottom;
    private javax.swing.JPanel pnlDataEntryMarketsTop;
    public javax.swing.JPanel pnlDataEntrySlimsPart1;
    private javax.swing.JPanel pnlDataEntrySlimsPart1Bottom;
    private javax.swing.JPanel pnlDataEntrySlimsPart1Comments;
    private javax.swing.JPanel pnlDataEntrySlimsPart1Top;
    public javax.swing.JPanel pnlDataEntrySlimsPart2;
    private javax.swing.JPanel pnlDataEntrySlimsPart2Bottom;
    private javax.swing.JPanel pnlDataEntrySlimsPart2Comments;
    private javax.swing.JPanel pnlDataEntrySlimsPart2Top;
    private javax.swing.JScrollPane scpDataEntryMarkets;
    private javax.swing.JScrollPane scpDataEntrySlimsPart1;
    private javax.swing.JScrollPane scpDataEntrySlimsPart1comments;
    private javax.swing.JScrollPane scpDataEntrySlimsPart2;
    private javax.swing.JScrollPane scpDataEntrySlimsPart2comments;
    private javax.swing.JTable tblDataEntryMarkets;
    private javax.swing.JTable tblDataEntrySlimsPart1;
    private javax.swing.JTable tblDataEntrySlimsPart2;
    public javax.swing.JTabbedPane tbpDataEntry;
    private javax.swing.JTextArea txaDataEntrySlimsPart1comments;
    private javax.swing.JTextArea txaDataEntrySlimsPart2comments;
    // End of variables declaration//GEN-END:variables

    private void prepareDataEntryTable(DefaultTableModel tableModel, String applicationIds) {
        try {
            queryBuilder.querySelectIndicators(applicationIds);
        } catch (Exception exception) {
        }
    }

    private void insertDataTrustLevelCombobox() {
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Poor");
        comboBox.addItem("Fair");
        comboBox.addItem("Good");
        rowEditor = new EachRowEditor(tblDataEntrySlimsPart1);
        for (int row = 0; row < tblDataEntrySlimsPart1.getRowCount(); row++) {
            if (tblDataEntrySlimsPart1.getValueAt(row, 0).equals("DATA TRUST LEVEL")) {
                rowEditor.setEditorAt(row, new DefaultCellEditor(comboBox));
            }
        }
    }

    private void insertCivilInsecurityCombobox() {
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Tranquil");
        comboBox.addItem("Tense but safe");
        comboBox.addItem("Restricted movement");
        comboBox.addItem("Clan clashes");
        rowEditor = new EachRowEditor(tblDataEntrySlimsPart2);
        for (int row = 0; row < tblDataEntrySlimsPart2.getRowCount(); row++) {
            if (tblDataEntrySlimsPart2.getValueAt(row, 0).equals("Level Of Civil Insecurity")) {
                rowEditor.setEditorAt(row, new DefaultCellEditor(comboBox));
            }
        }
    }
}
/*
 * class EachRowEditor implements TableCellEditor {
 *
 * protected HashMap<Integer, TableCellEditor> editors; protected
 * TableCellEditor editor, defaultEditor; JTable table;
 *
 * /**
 * Constructs a EachRowEditor. create default editor
 *
 * @see TableCellEditor @see DefaultCellEditor
 */
/*
 * public EachRowEditor(JTable table) { this.table = table; //editors = new
 * Hashtable(); editors = new HashMap<Integer, TableCellEditor>(); defaultEditor
 * = new DefaultCellEditor(new JTextField()); }
 *
 * /**
 * @param row table row @param editor table cell editor
 */
  /*  public void setEditorAt(int row, TableCellEditor editor) {
        editors.put(new Integer(row), editor);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        editor = (TableCellEditor) editors.get(new Integer(row));
        if (editor == null) {
            editor = defaultEditor;
        }

        return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    @Override
    public Object getCellEditorValue() {
        return editor.getCellEditorValue();
    }

    @Override
    public boolean stopCellEditing() {
        return editor.stopCellEditing();
    }

    @Override
    public void cancelCellEditing() {
        editor.cancelCellEditing();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        try {
            selectEditor((MouseEvent) anEvent);
        } catch (java.lang.ClassCastException classCastException) {
        }
        return editor.isCellEditable(anEvent);
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        editor.addCellEditorListener(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        editor.removeCellEditorListener(l);
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        selectEditor((MouseEvent) anEvent);
        return editor.shouldSelectCell(anEvent);
    }

    protected void selectEditor(MouseEvent e) {
        int row;
        if (e == null) {
            row = table.getSelectionModel().getAnchorSelectionIndex();
        } else {
            row = table.rowAtPoint(e.getPoint());
        }
        editor = (TableCellEditor) editors.get(new Integer(row));
        if (editor == null) {
            editor = defaultEditor;
        }
    }
}
*/