/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.ui;

import org.orpik.modular.MarketAnalysis;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.io.BufferedWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.jfree.ui.ExtensionFileFilter;
import org.orpik.access.control.Login;
import org.orpik.data.QueryBuilder;
import org.orpik.data.export.ExcelExporter;
import org.orpik.location.MarketZone;
import org.orpik.location.Region;
import org.orpik.modular.DataEntry;
import org.orpik.people.FieldAnalyst;
import org.orpik.settings.analysis.ChartSettings;
import org.orpik.settings.global.GuiManager;
import org.orpik.ui.customizer.table.ColumnsAutoSizer;
import org.orpik.validation.RangeValidation;

/**
 *
 * @author Chemweno
 */
public class MarketAnalysisUi extends javax.swing.JInternalFrame {

    private DefaultTableModel tbmMarketAnalysisMonthly = new DefaultTableModel();
    private DefaultTableModel tbmMarketAnalysisTot = new DefaultTableModel();
    private DefaultTableModel tbmMarketAnalysisMarketUpdate = new DefaultTableModel();
    private MarketAnalysis marketAnalysis = new MarketAnalysis();
    private ChartSettings chartSettings = new ChartSettings();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private GuiManager guiManager = new GuiManager();
    private DataEntry dataEntry = new DataEntry();
    private MarketZone marketZone = new MarketZone();
    private FieldAnalyst fieldAnalyst = new FieldAnalyst();
    private Region region = new Region();
    private RangeValidation validation = new RangeValidation();
    private ExcelExporter excelExporter;// = new ExcelExporter();
    private EachRowEditor rowEditor;
    private JFileChooser fchSaveChart = null;
    private int width = 1250;
    private int height = 700;
    private int chartDialogWidth = 500;
    private int chartDialogHeight = 400;
    private int chartImageWidth = 570;
    private int chartImageHeight = 370;

    /**
     * Creates new form DataEntryInterface
     */
    public MarketAnalysisUi(java.awt.Frame parent, boolean modal) {
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
        dlgMarketAnalysisChart.setSize(chartDialogWidth, chartDialogHeight);
        //Prepare market analysis TOT table model
        dataEntry.prepareMarketAnalysisTable("Monthly Analysis", tbmMarketAnalysisTot, tblMarketAnalysisTot,
                "YEAR", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

        //Prepare market analysis Monthly table model
        dataEntry.prepareMarketAnalysisTable("Monthly Analysis", tbmMarketAnalysisMonthly, tblMarketAnalysisMonthly,
                "YEAR", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

        //Prepare market analysis Market Update table model
        /*
         * dataEntry.prepareMarketAnalysisTable("TOT",
         * tbmMarketAnalysisMarketUpdate, tblMarketAnalysisMarketUpdate,
         * "INDICATOR", "5-YEAR AVG", "","END OF", "", "CURRENT NOMINAL PRICE",
         * "CURRENT REAL(DEFLATED PRICE)", "% CHANGE SAME MONTH PREVIOUS YEAR",
         * "% CHANGE PREVIOUS MONTH", "% CHANGE", "% CHANGE 5-YEAR
         * AVG(0000-0000)", "2003", "2004", "2005", "2006", "2007");
         */
        //Hide market update table
        //tblMarketAnalysisMarketUpdate.setVisible(false);

        //Load market zones on combo box        
        dataEntry.populateMarketZones(cboMarketAnalysisMonthlyMarketZone, cboMarketAnalysisTotMarketZone,
                cboMarketAnalysisMarketUpdateMarketZone);

        //Load field analysts on combo box
        dataEntry.populateFieldAnalysts(cboMarketAnalysisMonthlyFieldAnalyst, cboMarketAnalysisTotFieldAnalyst);

        //Load regions on combo box
        dataEntry.populateRegions(cboMarketAnalysisMonthlyRegion, cboMarketAnalysisTotRegion);

        //Load commodities on combo box
        dataEntry.populateCommodities(getSystemId(rbtMarketAnalysisMonthlyMarkets), cboMarketAnalysisMonthlyCommodity,
                cboMarketAnalysisTotFirstCommodity, cboMarketAnalysisTotSecondCommodity);

        //Load years with data on combo box
        dataEntry.populateYears(cboMarketAnalysisMonthlyStartYear, cboMarketAnalysisMonthlyEndYear, cboMarketAnalysisTotStartYear,
                cboMarketAnalysisTotEndYear, cboMarketAnalysisMarketUpdateStartYear, cboMarketAnalysisMarketUpdateAvgStartYear,
                cboMarketAnalysisMarketUpdateAvgFinishYear);

        //Set end year selected item to latest year
        cboMarketAnalysisMonthlyEndYear.setSelectedIndex(cboMarketAnalysisMonthlyEndYear.getItemCount() - 1);
        cboMarketAnalysisTotEndYear.setSelectedIndex(cboMarketAnalysisTotEndYear.getItemCount() - 1);
        /*
         * //Load market zone markets in monthly analysis panel
         * dataEntry.populateMarkets("marketZone",
         * marketZone.getMarketZoneId(cboMarketAnalysisMonthlyMarketZone.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisMonthlyMarkets),pnlMarketAnalysisMonthlySelectMarketsInner);
         *
         * //Load market zone markets in tot panel
         * dataEntry.populateMarkets("marketZone",
         * marketZone.getMarketZoneId(cboMarketAnalysisTotMarketZone.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisTotMarkets),pnlMarketAnalysisTotSelectMarketsInner);
         * * //Load region markets in monthly analysis panel
         * dataEntry.populateMarkets("region",
         * marketZone.getMarketZoneId(cboMarketAnalysisMonthlyRegion.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisMonthlyMarkets),pnlMarketAnalysisMonthlySelectMarketsInner);
         * * //Load region markets in tot analysis panel
         * dataEntry.populateMarkets("region",
         * marketZone.getMarketZoneId(cboMarketAnalysisTotRegion.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisTotMarkets),pnlMarketAnalysisTotSelectMarketsInner);
         * * //Load field analyst markets in monthly analysis panel
         * dataEntry.populateMarkets("fieldAnalyst",
         * marketZone.getMarketZoneId(cboMarketAnalysisMonthlyFieldAnalyst.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisMonthlyMarkets),pnlMarketAnalysisMonthlySelectMarketsInner);
         * * //Load field analyst markets in tot analysis panel
         * dataEntry.populateMarkets("fieldAnalyst",
         * marketZone.getMarketZoneId(cboMarketAnalysisTotFieldAnalyst.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisTotMarkets),pnlMarketAnalysisTotSelectMarketsInner);
         */

        dataEntry.populateYears(pnlMarketAnalysisMonthlyCalculateAverageInner, pnlMarketAnalysisMonthlySelectSeriesInner,
                pnlMarketAnalysisTotCalculateAverageInner, pnlMarketAnalysisTotSelectSeriesInner);

        tblMarketAnalysisMonthly.getColumnModel().getColumn(0).setPreferredWidth(80);

        //Load market analysis tabs        
        tbpMarketAnalysis.add("Monthly Analysis", pnlMarketAnalaysisMonthly);
        tbpMarketAnalysis.add("Terms of Trade", pnlMarketAnalaysisTot);
        tbpMarketAnalysis.add("Market Update", pnlMarketAnalysisMarketUpdate);

        marketAnalysis.scrollPanelToBottom(pnlMarketAnalysisMonthlyCalculateAverageInner, scpMarketAnalysisMonthlyCalculateAverage);
        marketAnalysis.scrollPanelToBottom(pnlMarketAnalysisMonthlySelectSeriesInner, scpMarketAnalysisMonthlySelectSeries);
        marketAnalysis.scrollPanelToBottom(pnlMarketAnalysisTotCalculateAverageInner, scpMarketAnalysisTotMiddle);
        marketAnalysis.scrollPanelToBottom(pnlMarketAnalysisTotSelectSeriesInner, scpMarketAnalysisTotSelectSeries);

        //Select default series years, use mode value=1 for series
        marketAnalysis.selectDefaultYears(1, pnlMarketAnalysisMonthlySelectSeriesInner, pnlMarketAnalysisTotSelectSeriesInner);
        //Select default average years, use mode value=2 for average        
        marketAnalysis.selectDefaultYears(2, pnlMarketAnalysisMonthlyCalculateAverageInner, pnlMarketAnalysisTotCalculateAverageInner);
    }
    /*
     * tblMarketAnalysisMarketUpdate = new
     * javax.swing.JTable(tbmMarketAnalysisMarketUpdate){ public Component
     * prepareRenderer(TableCellRenderer renderer,int row, int col) { Component
     * comp = super.prepareRenderer(renderer, row, col);
     * if(MarketAnalysis.marketUpdateCategoryRowsList.contains(row)){
     * comp.setBackground(new java.awt.Color(153,204,255)); }else{
     * comp.setBackground(Color.white); } return comp; } public boolean
     * isCellEditable(int row,int column){
     * if(MarketAnalysis.marketUpdateCategoryRowsList.contains(row) || column ==
     * 0){ return false; }else{ return true; } } };
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMarketAnalaysisMonthly = new javax.swing.JPanel();
        pnlMarketAnalysisMonthly = new javax.swing.JPanel();
        pnlMarketAnalysisMonthlyTop = new javax.swing.JPanel();
        pnlMarketAnalysisMonthlySpace = new javax.swing.JPanel();
        rbtMarketAnalysisMonthlyMarketZone = new javax.swing.JRadioButton();
        rbtMarketAnalysisMonthlyFieldAnalyst = new javax.swing.JRadioButton();
        rbtMarketAnalysisMonthlyRegion = new javax.swing.JRadioButton();
        cboMarketAnalysisMonthlyMarketZone = new javax.swing.JComboBox();
        cboMarketAnalysisMonthlyFieldAnalyst = new javax.swing.JComboBox();
        cboMarketAnalysisMonthlyRegion = new javax.swing.JComboBox();
        pnlMarketAnalysisMonthlyTemporal = new javax.swing.JPanel();
        lblMarketAnalysisMonthlyStartYear = new javax.swing.JLabel();
        lblMarketAnalysisMonthlyEndYear = new javax.swing.JLabel();
        cboMarketAnalysisMonthlyStartYear = new javax.swing.JComboBox();
        cboMarketAnalysisMonthlyEndYear = new javax.swing.JComboBox();
        pnlMarketAnalysisMonthlyDataset = new javax.swing.JPanel();
        lblMarketAnalysisMonthlyDataset = new javax.swing.JLabel();
        rbtMarketAnalysisMonthlyMarkets = new javax.swing.JRadioButton();
        rbtMarketAnalysisMonthlySlims = new javax.swing.JRadioButton();
        lblMarketAnalysisMonthlyCommodity = new javax.swing.JLabel();
        cboMarketAnalysisMonthlyCommodity = new javax.swing.JComboBox();
        pnlMarketAnalysisMonthlyOther = new javax.swing.JPanel();
        chkMarketAnalysisMonthlyShowAverage = new javax.swing.JCheckBox();
        lblMarketAnalysisMonthlyXaxisStartMonth = new javax.swing.JLabel();
        cboMarketAnalysisMonthlyXaxisStartMonth = new javax.swing.JComboBox();
        btnMarketAnalysisMonthlyGetData = new javax.swing.JButton();
        pnlMarketAnalysisMonthlySelectSeriesOuter = new javax.swing.JPanel();
        scpMarketAnalysisMonthlySelectSeries = new javax.swing.JScrollPane();
        pnlMarketAnalysisMonthlySelectSeriesInner = new javax.swing.JPanel();
        pnlMarketAnalysisMonthlyCalculateAverageOuter = new javax.swing.JPanel();
        scpMarketAnalysisMonthlyCalculateAverage = new javax.swing.JScrollPane();
        pnlMarketAnalysisMonthlyCalculateAverageInner = new javax.swing.JPanel();
        pnlMarketAnalysisMonthlySelectMarketsOuter = new javax.swing.JPanel();
        scpMarketAnalysisMonthlySelectMarkets = new javax.swing.JScrollPane();
        pnlMarketAnalysisMonthlySelectMarketsInner = new javax.swing.JPanel();
        pnlMarketAnalysisMonthlyMiddle = new javax.swing.JPanel();
        scpMarketAnalysisMonthlyMiddle = new javax.swing.JScrollPane();
        tblMarketAnalysisMonthly = new javax.swing.JTable();
        pnlMarketAnalysisMonthlyBottom = new javax.swing.JPanel();
        btnMarketyAnalysisMonthlyExport = new javax.swing.JButton();
        btgMarketAnalysisMonthlySpace = new javax.swing.ButtonGroup();
        btgMarketAnalysisMonthlyDataset = new javax.swing.ButtonGroup();
        pnlMarketAnalaysisTot = new javax.swing.JPanel();
        pnlMarketAnalysisTotBottom = new javax.swing.JPanel();
        btnMarketyAnalysisTotExport = new javax.swing.JButton();
        pnlMarketAnalysisTot = new javax.swing.JPanel();
        pnlMarketAnalysisTotTop = new javax.swing.JPanel();
        pnlMarketAnalysisTotSpace = new javax.swing.JPanel();
        rbtMarketAnalysisTotMarketZone = new javax.swing.JRadioButton();
        rbtMarketAnalysisTotFieldAnalyst = new javax.swing.JRadioButton();
        rbtMarketAnalysisTotRegion = new javax.swing.JRadioButton();
        cboMarketAnalysisTotMarketZone = new javax.swing.JComboBox();
        cboMarketAnalysisTotFieldAnalyst = new javax.swing.JComboBox();
        cboMarketAnalysisTotRegion = new javax.swing.JComboBox();
        pnlMarketAnalysisTotTemporal = new javax.swing.JPanel();
        lblMarketAnalysisTotStartYear = new javax.swing.JLabel();
        lblMarketAnalysisTotEndYear = new javax.swing.JLabel();
        cboMarketAnalysisTotStartYear = new javax.swing.JComboBox();
        cboMarketAnalysisTotEndYear = new javax.swing.JComboBox();
        pnlMarketAnalysisTotOther = new javax.swing.JPanel();
        chkMarketAnalysisTotShowAverage = new javax.swing.JCheckBox();
        lblMarketAnalysisTotXaxisStartMonth = new javax.swing.JLabel();
        cboMarketAnalysisTotXaxisStartMonth = new javax.swing.JComboBox();
        btnMarketAnalysisTotGetData = new javax.swing.JButton();
        pnlMarketAnalysisTotDataset = new javax.swing.JPanel();
        lblMarketAnalysisMonthlyDataset1 = new javax.swing.JLabel();
        rbtMarketAnalysisTotMarkets = new javax.swing.JRadioButton();
        rbtMarketAnalysisTotSlims = new javax.swing.JRadioButton();
        lblMarketAnalysisTotFirstCommodity = new javax.swing.JLabel();
        cboMarketAnalysisTotFirstCommodity = new javax.swing.JComboBox();
        lblMarketAnalysisTotSecondCommodity = new javax.swing.JLabel();
        cboMarketAnalysisTotSecondCommodity = new javax.swing.JComboBox();
        pnlMarketAnalysisTotSelectSeriesOuter = new javax.swing.JPanel();
        scpMarketAnalysisTotSelectSeries = new javax.swing.JScrollPane();
        pnlMarketAnalysisTotSelectSeriesInner = new javax.swing.JPanel();
        pnlMarketAnalysisTotCalculateAverageOuter = new javax.swing.JPanel();
        scpMarketAnalysisTotCalculateAverage = new javax.swing.JScrollPane();
        pnlMarketAnalysisTotCalculateAverageInner = new javax.swing.JPanel();
        pnlMarketAnalysisTotSelectMarketsOuter = new javax.swing.JPanel();
        scpMarketAnalysisTotSelectMarkets = new javax.swing.JScrollPane();
        pnlMarketAnalysisTotSelectMarketsInner = new javax.swing.JPanel();
        pnlMarketAnalysisTotMiddle = new javax.swing.JPanel();
        scpMarketAnalysisTotMiddle = new javax.swing.JScrollPane();
        tblMarketAnalysisTot = new javax.swing.JTable();
        pnlMarketAnalysisTotChart = new javax.swing.JPanel();
        btgMarketAnalysisTotSpace = new javax.swing.ButtonGroup();
        btgMarketAnalysisTotDataset = new javax.swing.ButtonGroup();
        pnlMarketAnalysisMarketUpdate = new javax.swing.JPanel();
        pnlMarketAnalysisMarketUpdateMiddle = new javax.swing.JPanel();
        scpMarketAnalysisMarketUpdateMiddle = new javax.swing.JScrollPane();
        //tblMarketAnalysisMarketUpdate = new javax.swing.JTable();
		tblMarketAnalysisMarketUpdate = new
      javax.swing.JTable(tbmMarketAnalysisMarketUpdate){ 
	  public Component prepareRenderer(TableCellRenderer renderer,int row, int col) { 
	  Component comp = super.prepareRenderer(renderer, row, col);
      if(MarketAnalysis.marketUpdateCategoryRowsList.contains(row)){
      comp.setBackground(new java.awt.Color(153,204,255)); 
	  }else{
      comp.setBackground(Color.white); 
	  } 
	  return comp; 
	  } 
	  public boolean
      isCellEditable(int row,int column){
      if(MarketAnalysis.marketUpdateCategoryRowsList.contains(row) || column ==
      0){ return false; }else{ return true; 
	  } 
	  } 
	  };
        pnlMarketAnalysisMarketUpdateTop = new javax.swing.JPanel();
        btnMarketAnalysisMarketUpdateGetData = new javax.swing.JButton();
        pnlMarketAnalysisMarketUpdateMarketsOuter = new javax.swing.JPanel();
        scplMarketAnalysisMarketUpdateMarkets = new javax.swing.JScrollPane();
        pnlMarketAnalysisMarketUpdateMarketsInner = new javax.swing.JPanel();
        pnlMarketAnalysisMarketUpdate5YearAverageYears = new javax.swing.JPanel();
        lblMarketAnalysisMarketUpdateAvgStartYear = new javax.swing.JLabel();
        cboMarketAnalysisMarketUpdateAvgFinishYear = new javax.swing.JComboBox();
        cboMarketAnalysisMarketUpdateAvgStartYear = new javax.swing.JComboBox();
        cboMarketAnalysisMarketUpdateAvgEndYear = new javax.swing.JLabel();
        pnlMarketAnalysisMarketUpdatePeriod = new javax.swing.JPanel();
        lblMarketAnalysisMonthlyStartYear1 = new javax.swing.JLabel();
        cboMarketAnalysisMarketUpdateStartYear = new javax.swing.JComboBox();
        lblMarketAnalysisMonthlyEndYear1 = new javax.swing.JLabel();
        cboMarketAnalysisMarketUpdateMonth = new javax.swing.JComboBox();
        lblMarketAnalysisMonthlyCommodity1 = new javax.swing.JLabel();
        cboMarketAnalysisMarketUpdateSeason = new javax.swing.JComboBox();
        pnlMarketAnalysisMarketUpdateMarketZone = new javax.swing.JPanel();
        lblMarketAnalysisMarketUpdateMarketZone = new javax.swing.JLabel();
        cboMarketAnalysisMarketUpdateMarketZone = new javax.swing.JComboBox();
        pnlMarketAnalysisMarketUpdateBottom = new javax.swing.JPanel();
        btnMarketyAnalysisMarketUpdateExport = new javax.swing.JButton();
        dlgMarketAnalysisChart = new javax.swing.JDialog();
        pnlMarketAnalysisChart = new javax.swing.JPanel();
        pnlMarketAnalysisChartShowChartSettingsCheckBox = new javax.swing.JPanel();
        btnMonthlyAnalysisSaveChart = new javax.swing.JButton();
        btnChartSettingsShowChartSettings = new javax.swing.JButton();
        pnlMarketAnalysisChartBottom = new javax.swing.JPanel();
        dlgChartSettingsFont = new javax.swing.JDialog();
        pnlChartSettingsFontMain = new javax.swing.JPanel();
        pnlChartSettingsFontFont = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstChartSettingsFontNames = new javax.swing.JList();
        pnlChartSettingsFontSize = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstChartSettingsFontSizes = new javax.swing.JList();
        pnlChartSettingsFontBottom = new javax.swing.JPanel();
        btnChartSettingsFontOk = new javax.swing.JButton();
        btnChartSettingsFontCancel = new javax.swing.JButton();
        pnlChartSettingsFontAttributes = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        tbpMarketAnalysis = new javax.swing.JTabbedPane();

        btgMarketAnalysisMonthlySpace.add(rbtMarketAnalysisMonthlyMarketZone);
        rbtMarketAnalysisMonthlyMarketZone.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisMonthlyMarketZone.setSelected(true);
        rbtMarketAnalysisMonthlyMarketZone.setText("Market Zone");
        rbtMarketAnalysisMonthlyMarketZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisMonthlyMarketZoneActionPerformed(evt);
            }
        });

        btgMarketAnalysisMonthlySpace.add(rbtMarketAnalysisMonthlyFieldAnalyst);
        rbtMarketAnalysisMonthlyFieldAnalyst.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisMonthlyFieldAnalyst.setText("Field Analyst");
        rbtMarketAnalysisMonthlyFieldAnalyst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisMonthlyFieldAnalystActionPerformed(evt);
            }
        });

        btgMarketAnalysisMonthlySpace.add(rbtMarketAnalysisMonthlyRegion);
        rbtMarketAnalysisMonthlyRegion.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisMonthlyRegion.setText("Region");
        rbtMarketAnalysisMonthlyRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisMonthlyRegionActionPerformed(evt);
            }
        });

        cboMarketAnalysisMonthlyMarketZone.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMonthlyMarketZone.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMarketAnalysisMonthlyMarketZoneItemStateChanged(evt);
            }
        });
        cboMarketAnalysisMonthlyMarketZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisMonthlyMarketZoneActionPerformed(evt);
            }
        });

        cboMarketAnalysisMonthlyFieldAnalyst.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMonthlyFieldAnalyst.setEnabled(false);
        cboMarketAnalysisMonthlyFieldAnalyst.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMarketAnalysisMonthlyFieldAnalystItemStateChanged(evt);
            }
        });
        cboMarketAnalysisMonthlyFieldAnalyst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisMonthlyFieldAnalystActionPerformed(evt);
            }
        });

        cboMarketAnalysisMonthlyRegion.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMonthlyRegion.setEnabled(false);
        cboMarketAnalysisMonthlyRegion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboMarketAnalysisMonthlyRegionItemStateChanged(evt);
            }
        });
        cboMarketAnalysisMonthlyRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisMonthlyRegionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisMonthlySpaceLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlySpace);
        pnlMarketAnalysisMonthlySpace.setLayout(pnlMarketAnalysisMonthlySpaceLayout);
        pnlMarketAnalysisMonthlySpaceLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createSequentialGroup()
                        .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtMarketAnalysisMonthlyFieldAnalyst)
                            .addComponent(rbtMarketAnalysisMonthlyRegion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboMarketAnalysisMonthlyFieldAnalyst, 0, 220, Short.MAX_VALUE)
                            .addComponent(cboMarketAnalysisMonthlyRegion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createSequentialGroup()
                        .addComponent(rbtMarketAnalysisMonthlyMarketZone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboMarketAnalysisMonthlyMarketZone, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlMarketAnalysisMonthlySpaceLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMarketAnalysisMonthlyMarketZone)
                    .addComponent(cboMarketAnalysisMonthlyMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMarketAnalysisMonthlyFieldAnalyst)
                    .addComponent(cboMarketAnalysisMonthlyFieldAnalyst, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisMonthlySpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMarketAnalysisMonthlyRegion)
                    .addComponent(cboMarketAnalysisMonthlyRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlMarketAnalysisMonthlyTemporal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Period", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        lblMarketAnalysisMonthlyStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyStartYear.setLabelFor(cboMarketAnalysisMonthlyStartYear);
        lblMarketAnalysisMonthlyStartYear.setText("Start");

        lblMarketAnalysisMonthlyEndYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyEndYear.setLabelFor(cboMarketAnalysisMonthlyEndYear);
        lblMarketAnalysisMonthlyEndYear.setText("End");

        cboMarketAnalysisMonthlyStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMonthlyStartYear.setMinimumSize(new java.awt.Dimension(51, 20));
        cboMarketAnalysisMonthlyStartYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisMonthlyStartYearActionPerformed(evt);
            }
        });

        cboMarketAnalysisMonthlyEndYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMonthlyEndYear.setSelectedIndex(cboMarketAnalysisMonthlyEndYear.getItemCount() - 1);

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyTemporalLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyTemporal);
        pnlMarketAnalysisMonthlyTemporal.setLayout(pnlMarketAnalysisMonthlyTemporalLayout);
        pnlMarketAnalysisMonthlyTemporalLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyTemporalLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisMonthlyTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblMarketAnalysisMonthlyStartYear)
                    .addComponent(lblMarketAnalysisMonthlyEndYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisMonthlyTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboMarketAnalysisMonthlyStartYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboMarketAnalysisMonthlyEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        pnlMarketAnalysisMonthlyTemporalLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMarketAnalysisMonthlyTemporalLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlMarketAnalysisMonthlyTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarketAnalysisMonthlyStartYear)
                    .addComponent(cboMarketAnalysisMonthlyStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisMonthlyTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMarketAnalysisMonthlyEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMarketAnalysisMonthlyEndYear)))
        );

        pnlMarketAnalysisMonthlyTemporalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cboMarketAnalysisMonthlyEndYear, cboMarketAnalysisMonthlyStartYear});

        lblMarketAnalysisMonthlyDataset.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyDataset.setText("Dataset");

        btgMarketAnalysisMonthlyDataset.add(rbtMarketAnalysisMonthlyMarkets);
        rbtMarketAnalysisMonthlyMarkets.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisMonthlyMarkets.setSelected(true);
        rbtMarketAnalysisMonthlyMarkets.setText("Markets");
        rbtMarketAnalysisMonthlyMarkets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisMonthlyMarketsActionPerformed(evt);
            }
        });

        btgMarketAnalysisMonthlyDataset.add(rbtMarketAnalysisMonthlySlims);
        rbtMarketAnalysisMonthlySlims.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisMonthlySlims.setText("SLIMS");
        rbtMarketAnalysisMonthlySlims.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisMonthlySlimsActionPerformed(evt);
            }
        });

        lblMarketAnalysisMonthlyCommodity.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyCommodity.setText("Commodity");

        cboMarketAnalysisMonthlyCommodity.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyDatasetLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyDataset);
        pnlMarketAnalysisMonthlyDataset.setLayout(pnlMarketAnalysisMonthlyDatasetLayout);
        pnlMarketAnalysisMonthlyDatasetLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyDatasetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMarketAnalysisMonthlyDataset)
                .addGap(18, 18, 18)
                .addComponent(rbtMarketAnalysisMonthlyMarkets)
                .addGap(18, 18, 18)
                .addComponent(rbtMarketAnalysisMonthlySlims)
                .addGap(18, 18, 18)
                .addComponent(lblMarketAnalysisMonthlyCommodity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMarketAnalysisMonthlyCommodity, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMarketAnalysisMonthlyDatasetLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyDatasetLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisMonthlyDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarketAnalysisMonthlyDataset)
                    .addComponent(rbtMarketAnalysisMonthlyMarkets)
                    .addComponent(rbtMarketAnalysisMonthlySlims)
                    .addComponent(lblMarketAnalysisMonthlyCommodity)
                    .addComponent(cboMarketAnalysisMonthlyCommodity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlMarketAnalysisMonthlyOther.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        chkMarketAnalysisMonthlyShowAverage.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        chkMarketAnalysisMonthlyShowAverage.setText("Show Average On Graph");

        lblMarketAnalysisMonthlyXaxisStartMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyXaxisStartMonth.setText("X-Axis Start Month");

        cboMarketAnalysisMonthlyXaxisStartMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMonthlyXaxisStartMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        btnMarketAnalysisMonthlyGetData.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        btnMarketAnalysisMonthlyGetData.setMnemonic('F');
        btnMarketAnalysisMonthlyGetData.setText("Get Data");
        btnMarketAnalysisMonthlyGetData.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnMarketAnalysisMonthlyGetData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarketAnalysisMonthlyGetDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyOtherLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyOther);
        pnlMarketAnalysisMonthlyOther.setLayout(pnlMarketAnalysisMonthlyOtherLayout);
        pnlMarketAnalysisMonthlyOtherLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyOtherLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisMonthlyOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkMarketAnalysisMonthlyShowAverage)
                    .addGroup(pnlMarketAnalysisMonthlyOtherLayout.createSequentialGroup()
                        .addGroup(pnlMarketAnalysisMonthlyOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMarketAnalysisMonthlyXaxisStartMonth)
                            .addComponent(btnMarketAnalysisMonthlyGetData))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboMarketAnalysisMonthlyXaxisStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        pnlMarketAnalysisMonthlyOtherLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyOtherLayout.createSequentialGroup()
                .addComponent(chkMarketAnalysisMonthlyShowAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMarketAnalysisMonthlyOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarketAnalysisMonthlyXaxisStartMonth)
                    .addComponent(cboMarketAnalysisMonthlyXaxisStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMarketAnalysisMonthlyGetData))
        );

        pnlMarketAnalysisMonthlySelectSeriesOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Series", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        scpMarketAnalysisMonthlySelectSeries.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout pnlMarketAnalysisMonthlySelectSeriesInnerLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlySelectSeriesInner);
        pnlMarketAnalysisMonthlySelectSeriesInner.setLayout(pnlMarketAnalysisMonthlySelectSeriesInnerLayout);
        pnlMarketAnalysisMonthlySelectSeriesInnerLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlySelectSeriesInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 167, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMonthlySelectSeriesInnerLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlySelectSeriesInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );

        scpMarketAnalysisMonthlySelectSeries.setViewportView(pnlMarketAnalysisMonthlySelectSeriesInner);

        javax.swing.GroupLayout pnlMarketAnalysisMonthlySelectSeriesOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlySelectSeriesOuter);
        pnlMarketAnalysisMonthlySelectSeriesOuter.setLayout(pnlMarketAnalysisMonthlySelectSeriesOuterLayout);
        pnlMarketAnalysisMonthlySelectSeriesOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlySelectSeriesOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlySelectSeries, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMonthlySelectSeriesOuterLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlySelectSeriesOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlySelectSeries, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlMarketAnalysisMonthlyCalculateAverageOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calculate Average", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N
        pnlMarketAnalysisMonthlyCalculateAverageOuter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMarketAnalysisMonthlyCalculateAverageOuterMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlMarketAnalysisMonthlyCalculateAverageOuterMouseReleased(evt);
            }
        });
        pnlMarketAnalysisMonthlyCalculateAverageOuter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pnlMarketAnalysisMonthlyCalculateAverageOuterFocusLost(evt);
            }
        });

        scpMarketAnalysisMonthlyCalculateAverage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpMarketAnalysisMonthlyCalculateAverage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scpMarketAnalysisMonthlyCalculateAverageMouseClicked(evt);
            }
        });

        pnlMarketAnalysisMonthlyCalculateAverageInner.setMaximumSize(new java.awt.Dimension(116, 400));
        pnlMarketAnalysisMonthlyCalculateAverageInner.setMinimumSize(new java.awt.Dimension(116, 61));
        pnlMarketAnalysisMonthlyCalculateAverageInner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlMarketAnalysisMonthlyCalculateAverageInnerMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlMarketAnalysisMonthlyCalculateAverageInnerMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyCalculateAverageInnerLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyCalculateAverageInner);
        pnlMarketAnalysisMonthlyCalculateAverageInner.setLayout(pnlMarketAnalysisMonthlyCalculateAverageInnerLayout);
        pnlMarketAnalysisMonthlyCalculateAverageInnerLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyCalculateAverageInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMonthlyCalculateAverageInnerLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyCalculateAverageInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 63, Short.MAX_VALUE)
        );

        scpMarketAnalysisMonthlyCalculateAverage.setViewportView(pnlMarketAnalysisMonthlyCalculateAverageInner);

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyCalculateAverageOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyCalculateAverageOuter);
        pnlMarketAnalysisMonthlyCalculateAverageOuter.setLayout(pnlMarketAnalysisMonthlyCalculateAverageOuterLayout);
        pnlMarketAnalysisMonthlyCalculateAverageOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyCalculateAverageOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlyCalculateAverage, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMonthlyCalculateAverageOuterLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyCalculateAverageOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlyCalculateAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pnlMarketAnalysisMonthlySelectMarketsOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Markets", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        scpMarketAnalysisMonthlySelectMarkets.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlMarketAnalysisMonthlySelectMarketsInner.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        pnlMarketAnalysisMonthlySelectMarketsInner.setLayout(new java.awt.GridLayout(0, 1, 0, 2));
        scpMarketAnalysisMonthlySelectMarkets.setViewportView(pnlMarketAnalysisMonthlySelectMarketsInner);

        javax.swing.GroupLayout pnlMarketAnalysisMonthlySelectMarketsOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlySelectMarketsOuter);
        pnlMarketAnalysisMonthlySelectMarketsOuter.setLayout(pnlMarketAnalysisMonthlySelectMarketsOuterLayout);
        pnlMarketAnalysisMonthlySelectMarketsOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlySelectMarketsOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlySelectMarkets, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMonthlySelectMarketsOuterLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlySelectMarketsOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlySelectMarkets, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyTopLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyTop);
        pnlMarketAnalysisMonthlyTop.setLayout(pnlMarketAnalysisMonthlyTopLayout);
        pnlMarketAnalysisMonthlyTopLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisMonthlyDataset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMarketAnalysisMonthlyTopLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisMonthlySpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlyTemporal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlyCalculateAverageOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlySelectSeriesOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlySelectMarketsOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlyOther, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(61, 61, 61))
        );
        pnlMarketAnalysisMonthlyTopLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyTopLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisMonthlyDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMarketAnalysisMonthlyTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMarketAnalysisMonthlySpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMarketAnalysisMonthlySelectSeriesOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMarketAnalysisMonthlySelectMarketsOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMarketAnalysisMonthlyTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlMarketAnalysisMonthlyCalculateAverageOuter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlMarketAnalysisMonthlyTemporal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlMarketAnalysisMonthlyOther, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        tblMarketAnalysisMonthly.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tblMarketAnalysisMonthly.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        tblMarketAnalysisMonthly.setModel(tbmMarketAnalysisMonthly);
        tblMarketAnalysisMonthly.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblMarketAnalysisMonthly.setCellSelectionEnabled(true);
        tblMarketAnalysisMonthly.setGridColor(Color.gray);
        tblMarketAnalysisMonthly.setInheritsPopupMenu(true);
        tblMarketAnalysisMonthly.setRowHeight(20);
        tblMarketAnalysisMonthly.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblMarketAnalysisMonthly.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpMarketAnalysisMonthlyMiddle.setViewportView(tblMarketAnalysisMonthly);
        tblMarketAnalysisMonthly.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyMiddleLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyMiddle);
        pnlMarketAnalysisMonthlyMiddle.setLayout(pnlMarketAnalysisMonthlyMiddleLayout);
        pnlMarketAnalysisMonthlyMiddleLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlyMiddle)
        );
        pnlMarketAnalysisMonthlyMiddleLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMonthlyMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );

        btnMarketyAnalysisMonthlyExport.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        btnMarketyAnalysisMonthlyExport.setMnemonic('E');
        btnMarketyAnalysisMonthlyExport.setText("Export Data File");
        btnMarketyAnalysisMonthlyExport.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnMarketyAnalysisMonthlyExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarketyAnalysisMonthlyExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyBottomLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthlyBottom);
        pnlMarketAnalysisMonthlyBottom.setLayout(pnlMarketAnalysisMonthlyBottomLayout);
        pnlMarketAnalysisMonthlyBottomLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyBottomLayout.createSequentialGroup()
                .addComponent(btnMarketyAnalysisMonthlyExport)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlMarketAnalysisMonthlyBottomLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyBottomLayout.createSequentialGroup()
                .addComponent(btnMarketyAnalysisMonthlyExport)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMarketAnalysisMonthlyLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMonthly);
        pnlMarketAnalysisMonthly.setLayout(pnlMarketAnalysisMonthlyLayout);
        pnlMarketAnalysisMonthlyLayout.setHorizontalGroup(
            pnlMarketAnalysisMonthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisMonthlyBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMarketAnalysisMonthlyTop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMarketAnalysisMonthlyMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMonthlyLayout.setVerticalGroup(
            pnlMarketAnalysisMonthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMonthlyLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisMonthlyTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlyMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMonthlyBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlMarketAnalaysisMonthlyLayout = new javax.swing.GroupLayout(pnlMarketAnalaysisMonthly);
        pnlMarketAnalaysisMonthly.setLayout(pnlMarketAnalaysisMonthlyLayout);
        pnlMarketAnalaysisMonthlyLayout.setHorizontalGroup(
            pnlMarketAnalaysisMonthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisMonthly, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMarketAnalaysisMonthlyLayout.setVerticalGroup(
            pnlMarketAnalaysisMonthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisMonthly, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnMarketyAnalysisTotExport.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnMarketyAnalysisTotExport.setMnemonic('E');
        btnMarketyAnalysisTotExport.setText("Export Data File");
        btnMarketyAnalysisTotExport.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnMarketyAnalysisTotExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarketyAnalysisTotExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisTotBottomLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotBottom);
        pnlMarketAnalysisTotBottom.setLayout(pnlMarketAnalysisTotBottomLayout);
        pnlMarketAnalysisTotBottomLayout.setHorizontalGroup(
            pnlMarketAnalysisTotBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotBottomLayout.createSequentialGroup()
                .addComponent(btnMarketyAnalysisTotExport)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlMarketAnalysisTotBottomLayout.setVerticalGroup(
            pnlMarketAnalysisTotBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMarketyAnalysisTotExport)
        );

        btgMarketAnalysisTotSpace.add(rbtMarketAnalysisTotMarketZone);
        rbtMarketAnalysisTotMarketZone.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisTotMarketZone.setSelected(true);
        rbtMarketAnalysisTotMarketZone.setText("Market Zone");
        rbtMarketAnalysisTotMarketZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisTotMarketZoneActionPerformed(evt);
            }
        });

        btgMarketAnalysisTotSpace.add(rbtMarketAnalysisTotFieldAnalyst);
        rbtMarketAnalysisTotFieldAnalyst.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisTotFieldAnalyst.setText("Field Analyst");
        rbtMarketAnalysisTotFieldAnalyst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisTotFieldAnalystActionPerformed(evt);
            }
        });

        btgMarketAnalysisTotSpace.add(rbtMarketAnalysisTotRegion);
        rbtMarketAnalysisTotRegion.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisTotRegion.setText("Region");
        rbtMarketAnalysisTotRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisTotRegionActionPerformed(evt);
            }
        });

        cboMarketAnalysisTotMarketZone.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisTotMarketZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisTotMarketZoneActionPerformed(evt);
            }
        });

        cboMarketAnalysisTotFieldAnalyst.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisTotFieldAnalyst.setEnabled(false);
        cboMarketAnalysisTotFieldAnalyst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisTotFieldAnalystActionPerformed(evt);
            }
        });

        cboMarketAnalysisTotRegion.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisTotRegion.setEnabled(false);
        cboMarketAnalysisTotRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisTotRegionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisTotSpaceLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotSpace);
        pnlMarketAnalysisTotSpace.setLayout(pnlMarketAnalysisTotSpaceLayout);
        pnlMarketAnalysisTotSpaceLayout.setHorizontalGroup(
            pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotSpaceLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMarketAnalysisTotSpaceLayout.createSequentialGroup()
                        .addGroup(pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtMarketAnalysisTotFieldAnalyst)
                            .addComponent(rbtMarketAnalysisTotRegion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboMarketAnalysisTotRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboMarketAnalysisTotFieldAnalyst, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlMarketAnalysisTotSpaceLayout.createSequentialGroup()
                        .addComponent(rbtMarketAnalysisTotMarketZone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboMarketAnalysisTotMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMarketAnalysisTotSpaceLayout.setVerticalGroup(
            pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMarketAnalysisTotSpaceLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMarketAnalysisTotMarketZone)
                    .addComponent(cboMarketAnalysisTotMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMarketAnalysisTotFieldAnalyst)
                    .addComponent(cboMarketAnalysisTotFieldAnalyst, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisTotSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMarketAnalysisTotRegion)
                    .addComponent(cboMarketAnalysisTotRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlMarketAnalysisTotTemporal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Period", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        lblMarketAnalysisTotStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisTotStartYear.setLabelFor(cboMarketAnalysisMonthlyStartYear);
        lblMarketAnalysisTotStartYear.setText("Start");

        lblMarketAnalysisTotEndYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisTotEndYear.setLabelFor(cboMarketAnalysisMonthlyEndYear);
        lblMarketAnalysisTotEndYear.setText("End");

        cboMarketAnalysisTotStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisTotStartYear.setMinimumSize(new java.awt.Dimension(51, 20));

        cboMarketAnalysisTotEndYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisTotEndYear.setSelectedIndex(cboMarketAnalysisTotEndYear.getItemCount() - 1);
        cboMarketAnalysisTotEndYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisTotEndYearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisTotTemporalLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotTemporal);
        pnlMarketAnalysisTotTemporal.setLayout(pnlMarketAnalysisTotTemporalLayout);
        pnlMarketAnalysisTotTemporalLayout.setHorizontalGroup(
            pnlMarketAnalysisTotTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotTemporalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlMarketAnalysisTotTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblMarketAnalysisTotStartYear)
                    .addComponent(lblMarketAnalysisTotEndYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisTotTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboMarketAnalysisTotStartYear, 0, 68, Short.MAX_VALUE)
                    .addComponent(cboMarketAnalysisTotEndYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlMarketAnalysisTotTemporalLayout.setVerticalGroup(
            pnlMarketAnalysisTotTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotTemporalLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisTotTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarketAnalysisTotStartYear)
                    .addComponent(cboMarketAnalysisTotStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlMarketAnalysisTotTemporalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMarketAnalysisTotEndYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMarketAnalysisTotEndYear)))
        );

        pnlMarketAnalysisTotTemporalLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cboMarketAnalysisTotEndYear, cboMarketAnalysisTotStartYear});

        pnlMarketAnalysisTotOther.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        chkMarketAnalysisTotShowAverage.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        chkMarketAnalysisTotShowAverage.setText("Show Average On Graph");

        lblMarketAnalysisTotXaxisStartMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisTotXaxisStartMonth.setText("X-Axis Start Month");

        cboMarketAnalysisTotXaxisStartMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisTotXaxisStartMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        btnMarketAnalysisTotGetData.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnMarketAnalysisTotGetData.setMnemonic('F');
        btnMarketAnalysisTotGetData.setText("Get Data");
        btnMarketAnalysisTotGetData.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnMarketAnalysisTotGetData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarketAnalysisTotGetDataActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisTotOtherLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotOther);
        pnlMarketAnalysisTotOther.setLayout(pnlMarketAnalysisTotOtherLayout);
        pnlMarketAnalysisTotOtherLayout.setHorizontalGroup(
            pnlMarketAnalysisTotOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chkMarketAnalysisTotShowAverage)
            .addGroup(pnlMarketAnalysisTotOtherLayout.createSequentialGroup()
                .addComponent(lblMarketAnalysisTotXaxisStartMonth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMarketAnalysisTotXaxisStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(btnMarketAnalysisTotGetData)
        );
        pnlMarketAnalysisTotOtherLayout.setVerticalGroup(
            pnlMarketAnalysisTotOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotOtherLayout.createSequentialGroup()
                .addComponent(chkMarketAnalysisTotShowAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMarketAnalysisTotOtherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarketAnalysisTotXaxisStartMonth)
                    .addComponent(cboMarketAnalysisTotXaxisStartMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMarketAnalysisTotGetData)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblMarketAnalysisMonthlyDataset1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyDataset1.setText("Dataset");

        btgMarketAnalysisTotDataset.add(rbtMarketAnalysisTotMarkets);
        rbtMarketAnalysisTotMarkets.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisTotMarkets.setSelected(true);
        rbtMarketAnalysisTotMarkets.setText("Markets");
        rbtMarketAnalysisTotMarkets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisTotMarketsActionPerformed(evt);
            }
        });

        btgMarketAnalysisTotDataset.add(rbtMarketAnalysisTotSlims);
        rbtMarketAnalysisTotSlims.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        rbtMarketAnalysisTotSlims.setText("SLIMS");
        rbtMarketAnalysisTotSlims.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtMarketAnalysisTotSlimsActionPerformed(evt);
            }
        });

        lblMarketAnalysisTotFirstCommodity.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisTotFirstCommodity.setText("First Commodity");

        cboMarketAnalysisTotFirstCommodity.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N

        lblMarketAnalysisTotSecondCommodity.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisTotSecondCommodity.setText("Second Commodity");

        cboMarketAnalysisTotSecondCommodity.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlMarketAnalysisTotDatasetLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotDataset);
        pnlMarketAnalysisTotDataset.setLayout(pnlMarketAnalysisTotDatasetLayout);
        pnlMarketAnalysisTotDatasetLayout.setHorizontalGroup(
            pnlMarketAnalysisTotDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotDatasetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMarketAnalysisMonthlyDataset1)
                .addGap(18, 18, 18)
                .addComponent(rbtMarketAnalysisTotMarkets)
                .addGap(18, 18, 18)
                .addComponent(rbtMarketAnalysisTotSlims)
                .addGap(18, 18, 18)
                .addComponent(lblMarketAnalysisTotFirstCommodity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMarketAnalysisTotFirstCommodity, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblMarketAnalysisTotSecondCommodity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMarketAnalysisTotSecondCommodity, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlMarketAnalysisTotDatasetLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboMarketAnalysisTotFirstCommodity, cboMarketAnalysisTotSecondCommodity});

        pnlMarketAnalysisTotDatasetLayout.setVerticalGroup(
            pnlMarketAnalysisTotDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotDatasetLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisTotDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMarketAnalysisTotDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMarketAnalysisTotSecondCommodity)
                        .addComponent(cboMarketAnalysisTotSecondCommodity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlMarketAnalysisTotDatasetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMarketAnalysisMonthlyDataset1)
                        .addComponent(rbtMarketAnalysisTotMarkets)
                        .addComponent(rbtMarketAnalysisTotSlims)
                        .addComponent(lblMarketAnalysisTotFirstCommodity)
                        .addComponent(cboMarketAnalysisTotFirstCommodity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlMarketAnalysisTotSelectSeriesOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Series", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        scpMarketAnalysisTotSelectSeries.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlMarketAnalysisTotSelectSeriesInner.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlMarketAnalysisTotSelectSeriesInnerLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotSelectSeriesInner);
        pnlMarketAnalysisTotSelectSeriesInner.setLayout(pnlMarketAnalysisTotSelectSeriesInnerLayout);
        pnlMarketAnalysisTotSelectSeriesInnerLayout.setHorizontalGroup(
            pnlMarketAnalysisTotSelectSeriesInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );
        pnlMarketAnalysisTotSelectSeriesInnerLayout.setVerticalGroup(
            pnlMarketAnalysisTotSelectSeriesInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 119, Short.MAX_VALUE)
        );

        scpMarketAnalysisTotSelectSeries.setViewportView(pnlMarketAnalysisTotSelectSeriesInner);

        javax.swing.GroupLayout pnlMarketAnalysisTotSelectSeriesOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotSelectSeriesOuter);
        pnlMarketAnalysisTotSelectSeriesOuter.setLayout(pnlMarketAnalysisTotSelectSeriesOuterLayout);
        pnlMarketAnalysisTotSelectSeriesOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisTotSelectSeriesOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotSelectSeriesOuterLayout.createSequentialGroup()
                .addComponent(scpMarketAnalysisTotSelectSeries, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );
        pnlMarketAnalysisTotSelectSeriesOuterLayout.setVerticalGroup(
            pnlMarketAnalysisTotSelectSeriesOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisTotSelectSeries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pnlMarketAnalysisTotCalculateAverageOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calculate Average", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        scpMarketAnalysisTotCalculateAverage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlMarketAnalysisTotCalculateAverageInner.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlMarketAnalysisTotCalculateAverageInnerLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotCalculateAverageInner);
        pnlMarketAnalysisTotCalculateAverageInner.setLayout(pnlMarketAnalysisTotCalculateAverageInnerLayout);
        pnlMarketAnalysisTotCalculateAverageInnerLayout.setHorizontalGroup(
            pnlMarketAnalysisTotCalculateAverageInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 133, Short.MAX_VALUE)
        );
        pnlMarketAnalysisTotCalculateAverageInnerLayout.setVerticalGroup(
            pnlMarketAnalysisTotCalculateAverageInnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 72, Short.MAX_VALUE)
        );

        scpMarketAnalysisTotCalculateAverage.setViewportView(pnlMarketAnalysisTotCalculateAverageInner);

        javax.swing.GroupLayout pnlMarketAnalysisTotCalculateAverageOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotCalculateAverageOuter);
        pnlMarketAnalysisTotCalculateAverageOuter.setLayout(pnlMarketAnalysisTotCalculateAverageOuterLayout);
        pnlMarketAnalysisTotCalculateAverageOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisTotCalculateAverageOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisTotCalculateAverage, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
        );
        pnlMarketAnalysisTotCalculateAverageOuterLayout.setVerticalGroup(
            pnlMarketAnalysisTotCalculateAverageOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisTotCalculateAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pnlMarketAnalysisTotSelectMarketsOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Markets", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        scpMarketAnalysisTotSelectMarkets.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scpMarketAnalysisTotSelectMarkets.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        pnlMarketAnalysisTotSelectMarketsInner.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        pnlMarketAnalysisTotSelectMarketsInner.setLayout(new java.awt.GridLayout(0, 1));
        scpMarketAnalysisTotSelectMarkets.setViewportView(pnlMarketAnalysisTotSelectMarketsInner);

        javax.swing.GroupLayout pnlMarketAnalysisTotSelectMarketsOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotSelectMarketsOuter);
        pnlMarketAnalysisTotSelectMarketsOuter.setLayout(pnlMarketAnalysisTotSelectMarketsOuterLayout);
        pnlMarketAnalysisTotSelectMarketsOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisTotSelectMarketsOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisTotSelectMarkets, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
        );
        pnlMarketAnalysisTotSelectMarketsOuterLayout.setVerticalGroup(
            pnlMarketAnalysisTotSelectMarketsOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisTotSelectMarkets, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlMarketAnalysisTotTopLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotTop);
        pnlMarketAnalysisTotTop.setLayout(pnlMarketAnalysisTotTopLayout);
        pnlMarketAnalysisTotTopLayout.setHorizontalGroup(
            pnlMarketAnalysisTotTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisTotDataset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMarketAnalysisTotTopLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisTotSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotTemporal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotCalculateAverageOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotSelectSeriesOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotSelectMarketsOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotOther, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        pnlMarketAnalysisTotTopLayout.setVerticalGroup(
            pnlMarketAnalysisTotTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotTopLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisTotDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMarketAnalysisTotTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMarketAnalysisTotTopLayout.createSequentialGroup()
                        .addGap(0, 7, Short.MAX_VALUE)
                        .addGroup(pnlMarketAnalysisTotTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlMarketAnalysisTotOther, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(pnlMarketAnalysisTotSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlMarketAnalysisTotTemporal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlMarketAnalysisTotTopLayout.createSequentialGroup()
                        .addGroup(pnlMarketAnalysisTotTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlMarketAnalysisTotSelectMarketsOuter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlMarketAnalysisTotSelectSeriesOuter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlMarketAnalysisTotCalculateAverageOuter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        tblMarketAnalysisTot.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tblMarketAnalysisTot.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblMarketAnalysisTot.setModel(tbmMarketAnalysisTot);
        tblMarketAnalysisTot.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblMarketAnalysisTot.setCellSelectionEnabled(true);
        tblMarketAnalysisTot.setGridColor(Color.gray);
        tblMarketAnalysisTot.setInheritsPopupMenu(true);
        tblMarketAnalysisTot.setRowHeight(20);
        tblMarketAnalysisTot.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblMarketAnalysisTot.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpMarketAnalysisTotMiddle.setViewportView(tblMarketAnalysisTot);
        tblMarketAnalysisTot.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        pnlMarketAnalysisTotChart.setBackground(new java.awt.Color(255, 255, 255));
        pnlMarketAnalysisTotChart.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        pnlMarketAnalysisTotChart.setPreferredSize(new java.awt.Dimension(300, 372));

        javax.swing.GroupLayout pnlMarketAnalysisTotChartLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotChart);
        pnlMarketAnalysisTotChart.setLayout(pnlMarketAnalysisTotChartLayout);
        pnlMarketAnalysisTotChartLayout.setHorizontalGroup(
            pnlMarketAnalysisTotChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlMarketAnalysisTotChartLayout.setVerticalGroup(
            pnlMarketAnalysisTotChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlMarketAnalysisTotMiddleLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTotMiddle);
        pnlMarketAnalysisTotMiddle.setLayout(pnlMarketAnalysisTotMiddleLayout);
        pnlMarketAnalysisTotMiddleLayout.setHorizontalGroup(
            pnlMarketAnalysisTotMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotMiddleLayout.createSequentialGroup()
                .addComponent(scpMarketAnalysisTotMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotChart, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE))
        );
        pnlMarketAnalysisTotMiddleLayout.setVerticalGroup(
            pnlMarketAnalysisTotMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisTotMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
            .addComponent(pnlMarketAnalysisTotChart, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlMarketAnalysisTotLayout = new javax.swing.GroupLayout(pnlMarketAnalysisTot);
        pnlMarketAnalysisTot.setLayout(pnlMarketAnalysisTotLayout);
        pnlMarketAnalysisTotLayout.setHorizontalGroup(
            pnlMarketAnalysisTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisTotTop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMarketAnalysisTotMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMarketAnalysisTotLayout.setVerticalGroup(
            pnlMarketAnalysisTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisTotLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisTotTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlMarketAnalaysisTotLayout = new javax.swing.GroupLayout(pnlMarketAnalaysisTot);
        pnlMarketAnalaysisTot.setLayout(pnlMarketAnalaysisTotLayout);
        pnlMarketAnalaysisTotLayout.setHorizontalGroup(
            pnlMarketAnalaysisTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisTotBottom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlMarketAnalysisTot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMarketAnalaysisTotLayout.setVerticalGroup(
            pnlMarketAnalaysisTotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalaysisTotLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisTot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisTotBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tblMarketAnalysisMarketUpdate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tblMarketAnalysisMarketUpdate.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        tblMarketAnalysisMarketUpdate.setModel(tbmMarketAnalysisMarketUpdate);
        tblMarketAnalysisMarketUpdate.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMarketAnalysisMarketUpdate.setCellSelectionEnabled(true);
        tblMarketAnalysisMarketUpdate.setGridColor(Color.gray);
        tblMarketAnalysisMarketUpdate.setInheritsPopupMenu(true);
        tblMarketAnalysisMarketUpdate.setRowHeight(20);
        tblMarketAnalysisMarketUpdate.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tblMarketAnalysisMarketUpdate.setSelectionForeground(new java.awt.Color(204, 0, 0));
        scpMarketAnalysisMarketUpdateMiddle.setViewportView(tblMarketAnalysisMarketUpdate);
        tblMarketAnalysisMarketUpdate.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        pnlMarketAnalysisMarketUpdateTop.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnMarketAnalysisMarketUpdateGetData.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        btnMarketAnalysisMarketUpdateGetData.setMnemonic('G');
        btnMarketAnalysisMarketUpdateGetData.setText("Get Data");
        btnMarketAnalysisMarketUpdateGetData.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnMarketAnalysisMarketUpdateGetData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarketAnalysisMarketUpdateGetDataActionPerformed(evt);
            }
        });

        pnlMarketAnalysisMarketUpdateMarketsOuter.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Markets", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        scplMarketAnalysisMarketUpdateMarkets.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlMarketAnalysisMarketUpdateMarketsInner.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        pnlMarketAnalysisMarketUpdateMarketsInner.setLayout(new java.awt.GridLayout(0, 1, 0, 2));
        scplMarketAnalysisMarketUpdateMarkets.setViewportView(pnlMarketAnalysisMarketUpdateMarketsInner);

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdateMarketsOuterLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdateMarketsOuter);
        pnlMarketAnalysisMarketUpdateMarketsOuter.setLayout(pnlMarketAnalysisMarketUpdateMarketsOuterLayout);
        pnlMarketAnalysisMarketUpdateMarketsOuterLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdateMarketsOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scplMarketAnalysisMarketUpdateMarkets, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMarketUpdateMarketsOuterLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdateMarketsOuterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scplMarketAnalysisMarketUpdateMarkets, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );

        pnlMarketAnalysisMarketUpdate5YearAverageYears.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "5-Years Average ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        lblMarketAnalysisMarketUpdateAvgStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMarketUpdateAvgStartYear.setLabelFor(cboMarketAnalysisMonthlyStartYear);
        lblMarketAnalysisMarketUpdateAvgStartYear.setText("Start Year");

        cboMarketAnalysisMarketUpdateAvgFinishYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateAvgFinishYear.setMinimumSize(new java.awt.Dimension(51, 20));

        cboMarketAnalysisMarketUpdateAvgStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateAvgStartYear.setMinimumSize(new java.awt.Dimension(51, 20));

        cboMarketAnalysisMarketUpdateAvgEndYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateAvgEndYear.setLabelFor(cboMarketAnalysisMonthlyStartYear);
        cboMarketAnalysisMarketUpdateAvgEndYear.setText("End Year");

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdate5YearAverageYears);
        pnlMarketAnalysisMarketUpdate5YearAverageYears.setLayout(pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout);
        pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.createSequentialGroup()
                .addComponent(lblMarketAnalysisMarketUpdateAvgStartYear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMarketAnalysisMarketUpdateAvgStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cboMarketAnalysisMarketUpdateAvgEndYear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMarketAnalysisMarketUpdateAvgFinishYear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cboMarketAnalysisMarketUpdateAvgFinishYear, cboMarketAnalysisMarketUpdateAvgStartYear});

        pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cboMarketAnalysisMarketUpdateAvgFinishYear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cboMarketAnalysisMarketUpdateAvgEndYear))
            .addGroup(pnlMarketAnalysisMarketUpdate5YearAverageYearsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cboMarketAnalysisMarketUpdateAvgStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblMarketAnalysisMarketUpdateAvgStartYear))
        );

        pnlMarketAnalysisMarketUpdatePeriod.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Period", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        lblMarketAnalysisMonthlyStartYear1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyStartYear1.setLabelFor(cboMarketAnalysisMonthlyStartYear);
        lblMarketAnalysisMonthlyStartYear1.setText("Year");

        cboMarketAnalysisMarketUpdateStartYear.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateStartYear.setMinimumSize(new java.awt.Dimension(51, 20));

        lblMarketAnalysisMonthlyEndYear1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyEndYear1.setLabelFor(cboMarketAnalysisMonthlyEndYear);
        lblMarketAnalysisMonthlyEndYear1.setText("Month");

        cboMarketAnalysisMarketUpdateMonth.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        lblMarketAnalysisMonthlyCommodity1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMonthlyCommodity1.setText("Last Season");

        cboMarketAnalysisMarketUpdateSeason.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateSeason.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Deyr", "Gu" }));

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdatePeriodLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdatePeriod);
        pnlMarketAnalysisMarketUpdatePeriod.setLayout(pnlMarketAnalysisMarketUpdatePeriodLayout);
        pnlMarketAnalysisMarketUpdatePeriodLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdatePeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdatePeriodLayout.createSequentialGroup()
                .addComponent(lblMarketAnalysisMonthlyStartYear1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMarketAnalysisMarketUpdateStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblMarketAnalysisMonthlyEndYear1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMarketAnalysisMarketUpdateMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblMarketAnalysisMonthlyCommodity1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMarketAnalysisMarketUpdateSeason, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlMarketAnalysisMarketUpdatePeriodLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdatePeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdatePeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblMarketAnalysisMonthlyCommodity1)
                .addComponent(cboMarketAnalysisMarketUpdateSeason, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlMarketAnalysisMarketUpdatePeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cboMarketAnalysisMarketUpdateMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblMarketAnalysisMonthlyEndYear1))
            .addGroup(pnlMarketAnalysisMarketUpdatePeriodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblMarketAnalysisMonthlyStartYear1)
                .addComponent(cboMarketAnalysisMarketUpdateStartYear, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlMarketAnalysisMarketUpdateMarketZone.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Unicode MS", 0, 14))); // NOI18N

        lblMarketAnalysisMarketUpdateMarketZone.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        lblMarketAnalysisMarketUpdateMarketZone.setText("Market Zone");

        cboMarketAnalysisMarketUpdateMarketZone.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        cboMarketAnalysisMarketUpdateMarketZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMarketAnalysisMarketUpdateMarketZoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdateMarketZoneLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdateMarketZone);
        pnlMarketAnalysisMarketUpdateMarketZone.setLayout(pnlMarketAnalysisMarketUpdateMarketZoneLayout);
        pnlMarketAnalysisMarketUpdateMarketZoneLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdateMarketZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdateMarketZoneLayout.createSequentialGroup()
                .addComponent(lblMarketAnalysisMarketUpdateMarketZone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboMarketAnalysisMarketUpdateMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlMarketAnalysisMarketUpdateMarketZoneLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdateMarketZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdateMarketZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblMarketAnalysisMarketUpdateMarketZone)
                .addComponent(cboMarketAnalysisMarketUpdateMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdateTopLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdateTop);
        pnlMarketAnalysisMarketUpdateTop.setLayout(pnlMarketAnalysisMarketUpdateTopLayout);
        pnlMarketAnalysisMarketUpdateTopLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdateTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdateTopLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisMarketUpdateMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisMarketUpdateMarketsOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMarketAnalysisMarketUpdateTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlMarketAnalysisMarketUpdateTopLayout.createSequentialGroup()
                        .addComponent(pnlMarketAnalysisMarketUpdatePeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlMarketAnalysisMarketUpdate5YearAverageYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnMarketAnalysisMarketUpdateGetData)))
        );
        pnlMarketAnalysisMarketUpdateTopLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdateTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMarketAnalysisMarketUpdateTopLayout.createSequentialGroup()
                .addGroup(pnlMarketAnalysisMarketUpdateTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMarketAnalysisMarketUpdatePeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMarketAnalysisMarketUpdate5YearAverageYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMarketAnalysisMarketUpdateGetData)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMarketAnalysisMarketUpdateTopLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlMarketAnalysisMarketUpdateTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMarketAnalysisMarketUpdateMarketZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlMarketAnalysisMarketUpdateMarketsOuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlMarketAnalysisMarketUpdateBottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnMarketyAnalysisMarketUpdateExport.setFont(new java.awt.Font("Arial Unicode MS", 0, 13)); // NOI18N
        btnMarketyAnalysisMarketUpdateExport.setMnemonic('E');
        btnMarketyAnalysisMarketUpdateExport.setText("Export To File");
        btnMarketyAnalysisMarketUpdateExport.setToolTipText("Populate table with already saved data. Prefer when viewing entered data or when sending already saved data.");
        btnMarketyAnalysisMarketUpdateExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarketyAnalysisMarketUpdateExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdateBottomLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdateBottom);
        pnlMarketAnalysisMarketUpdateBottom.setLayout(pnlMarketAnalysisMarketUpdateBottomLayout);
        pnlMarketAnalysisMarketUpdateBottomLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdateBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdateBottomLayout.createSequentialGroup()
                .addComponent(btnMarketyAnalysisMarketUpdateExport)
                .addGap(0, 921, Short.MAX_VALUE))
        );
        pnlMarketAnalysisMarketUpdateBottomLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdateBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMarketyAnalysisMarketUpdateExport)
        );

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdateMiddleLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdateMiddle);
        pnlMarketAnalysisMarketUpdateMiddle.setLayout(pnlMarketAnalysisMarketUpdateMiddleLayout);
        pnlMarketAnalysisMarketUpdateMiddleLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdateMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scpMarketAnalysisMarketUpdateMiddle)
            .addComponent(pnlMarketAnalysisMarketUpdateTop, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMarketAnalysisMarketUpdateMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlMarketAnalysisMarketUpdateBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMarketAnalysisMarketUpdateMiddleLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdateMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisMarketUpdateMiddleLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisMarketUpdateTop, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpMarketAnalysisMarketUpdateMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addGap(32, 32, 32))
            .addGroup(pnlMarketAnalysisMarketUpdateMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMarketAnalysisMarketUpdateMiddleLayout.createSequentialGroup()
                    .addGap(0, 547, Short.MAX_VALUE)
                    .addComponent(pnlMarketAnalysisMarketUpdateBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout pnlMarketAnalysisMarketUpdateLayout = new javax.swing.GroupLayout(pnlMarketAnalysisMarketUpdate);
        pnlMarketAnalysisMarketUpdate.setLayout(pnlMarketAnalysisMarketUpdateLayout);
        pnlMarketAnalysisMarketUpdateLayout.setHorizontalGroup(
            pnlMarketAnalysisMarketUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisMarketUpdateMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMarketAnalysisMarketUpdateLayout.setVerticalGroup(
            pnlMarketAnalysisMarketUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisMarketUpdateMiddle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        dlgMarketAnalysisChart.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgMarketAnalysisChart.setResizable(false);
        dlgMarketAnalysisChart.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlgMarketAnalysisChartWindowClosing(evt);
            }
        });

        pnlMarketAnalysisChartShowChartSettingsCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlMarketAnalysisChartShowChartSettingsCheckBox.setPreferredSize(new java.awt.Dimension(876, 10));
        pnlMarketAnalysisChartShowChartSettingsCheckBox.setRequestFocusEnabled(false);

        btnMonthlyAnalysisSaveChart.setMnemonic('C');
        btnMonthlyAnalysisSaveChart.setText("Save Chart");
        btnMonthlyAnalysisSaveChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonthlyAnalysisSaveChartActionPerformed(evt);
            }
        });

        btnChartSettingsShowChartSettings.setText("Show chart properies");
        btnChartSettingsShowChartSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChartSettingsShowChartSettingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout = new javax.swing.GroupLayout(pnlMarketAnalysisChartShowChartSettingsCheckBox);
        pnlMarketAnalysisChartShowChartSettingsCheckBox.setLayout(pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout);
        pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout.setHorizontalGroup(
            pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addComponent(btnChartSettingsShowChartSettings)
                .addGap(92, 92, 92)
                .addComponent(btnMonthlyAnalysisSaveChart)
                .addGap(0, 314, Short.MAX_VALUE))
        );
        pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout.setVerticalGroup(
            pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisChartShowChartSettingsCheckBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnMonthlyAnalysisSaveChart)
                .addComponent(btnChartSettingsShowChartSettings))
        );

        pnlMarketAnalysisChartBottom.setBackground(new java.awt.Color(255, 255, 255));
        pnlMarketAnalysisChartBottom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnlMarketAnalysisChartBottomLayout = new javax.swing.GroupLayout(pnlMarketAnalysisChartBottom);
        pnlMarketAnalysisChartBottom.setLayout(pnlMarketAnalysisChartBottomLayout);
        pnlMarketAnalysisChartBottomLayout.setHorizontalGroup(
            pnlMarketAnalysisChartBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlMarketAnalysisChartBottomLayout.setVerticalGroup(
            pnlMarketAnalysisChartBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlMarketAnalysisChartLayout = new javax.swing.GroupLayout(pnlMarketAnalysisChart);
        pnlMarketAnalysisChart.setLayout(pnlMarketAnalysisChartLayout);
        pnlMarketAnalysisChartLayout.setHorizontalGroup(
            pnlMarketAnalysisChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisChartShowChartSettingsCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
            .addComponent(pnlMarketAnalysisChartBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMarketAnalysisChartLayout.setVerticalGroup(
            pnlMarketAnalysisChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMarketAnalysisChartLayout.createSequentialGroup()
                .addComponent(pnlMarketAnalysisChartShowChartSettingsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMarketAnalysisChartBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout dlgMarketAnalysisChartLayout = new javax.swing.GroupLayout(dlgMarketAnalysisChart.getContentPane());
        dlgMarketAnalysisChart.getContentPane().setLayout(dlgMarketAnalysisChartLayout);
        dlgMarketAnalysisChartLayout.setHorizontalGroup(
            dlgMarketAnalysisChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgMarketAnalysisChartLayout.setVerticalGroup(
            dlgMarketAnalysisChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMarketAnalysisChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        dlgChartSettingsFont.setResizable(false);

        pnlChartSettingsFontFont.setBorder(javax.swing.BorderFactory.createTitledBorder("Font"));

        lstChartSettingsFontNames.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "28", "36", "48", "72" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(lstChartSettingsFontNames);

        javax.swing.GroupLayout pnlChartSettingsFontFontLayout = new javax.swing.GroupLayout(pnlChartSettingsFontFont);
        pnlChartSettingsFontFont.setLayout(pnlChartSettingsFontFontLayout);
        pnlChartSettingsFontFontLayout.setHorizontalGroup(
            pnlChartSettingsFontFontLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlChartSettingsFontFontLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        pnlChartSettingsFontFontLayout.setVerticalGroup(
            pnlChartSettingsFontFontLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );

        pnlChartSettingsFontSize.setBorder(javax.swing.BorderFactory.createTitledBorder("Size"));
        pnlChartSettingsFontSize.setToolTipText("");

        lstChartSettingsFontSizes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "28", "36", "48", "72" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstChartSettingsFontSizes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(lstChartSettingsFontSizes);

        javax.swing.GroupLayout pnlChartSettingsFontSizeLayout = new javax.swing.GroupLayout(pnlChartSettingsFontSize);
        pnlChartSettingsFontSize.setLayout(pnlChartSettingsFontSizeLayout);
        pnlChartSettingsFontSizeLayout.setHorizontalGroup(
            pnlChartSettingsFontSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
        );
        pnlChartSettingsFontSizeLayout.setVerticalGroup(
            pnlChartSettingsFontSizeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
        );

        btnChartSettingsFontOk.setText("OK");

        btnChartSettingsFontCancel.setText("Cancel");

        javax.swing.GroupLayout pnlChartSettingsFontBottomLayout = new javax.swing.GroupLayout(pnlChartSettingsFontBottom);
        pnlChartSettingsFontBottom.setLayout(pnlChartSettingsFontBottomLayout);
        pnlChartSettingsFontBottomLayout.setHorizontalGroup(
            pnlChartSettingsFontBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlChartSettingsFontBottomLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnChartSettingsFontOk)
                .addGap(20, 20, 20)
                .addComponent(btnChartSettingsFontCancel)
                .addContainerGap())
        );

        pnlChartSettingsFontBottomLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnChartSettingsFontCancel, btnChartSettingsFontOk});

        pnlChartSettingsFontBottomLayout.setVerticalGroup(
            pnlChartSettingsFontBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlChartSettingsFontBottomLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(pnlChartSettingsFontBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChartSettingsFontOk)
                    .addComponent(btnChartSettingsFontCancel)))
        );

        pnlChartSettingsFontAttributes.setBorder(javax.swing.BorderFactory.createTitledBorder("Attributes"));

        jCheckBox1.setMnemonic('I');
        jCheckBox1.setText("Italic");

        jCheckBox7.setMnemonic('B');
        jCheckBox7.setText("Bold");

        javax.swing.GroupLayout pnlChartSettingsFontAttributesLayout = new javax.swing.GroupLayout(pnlChartSettingsFontAttributes);
        pnlChartSettingsFontAttributes.setLayout(pnlChartSettingsFontAttributesLayout);
        pnlChartSettingsFontAttributesLayout.setHorizontalGroup(
            pnlChartSettingsFontAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChartSettingsFontAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addContainerGap())
        );
        pnlChartSettingsFontAttributesLayout.setVerticalGroup(
            pnlChartSettingsFontAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlChartSettingsFontAttributesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlChartSettingsFontAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox1)))
        );

        javax.swing.GroupLayout pnlChartSettingsFontMainLayout = new javax.swing.GroupLayout(pnlChartSettingsFontMain);
        pnlChartSettingsFontMain.setLayout(pnlChartSettingsFontMainLayout);
        pnlChartSettingsFontMainLayout.setHorizontalGroup(
            pnlChartSettingsFontMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChartSettingsFontBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlChartSettingsFontMainLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlChartSettingsFontMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlChartSettingsFontAttributes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlChartSettingsFontSize, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(pnlChartSettingsFontMainLayout.createSequentialGroup()
                .addComponent(pnlChartSettingsFontFont, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 108, Short.MAX_VALUE))
        );
        pnlChartSettingsFontMainLayout.setVerticalGroup(
            pnlChartSettingsFontMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChartSettingsFontMainLayout.createSequentialGroup()
                .addGroup(pnlChartSettingsFontMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChartSettingsFontMainLayout.createSequentialGroup()
                        .addComponent(pnlChartSettingsFontSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlChartSettingsFontAttributes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlChartSettingsFontFont, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlChartSettingsFontBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout dlgChartSettingsFontLayout = new javax.swing.GroupLayout(dlgChartSettingsFont.getContentPane());
        dlgChartSettingsFont.getContentPane().setLayout(dlgChartSettingsFontLayout);
        dlgChartSettingsFontLayout.setHorizontalGroup(
            dlgChartSettingsFontLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChartSettingsFontMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgChartSettingsFontLayout.setVerticalGroup(
            dlgChartSettingsFontLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChartSettingsFontMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setTitle("MARKET ANALYSIS");

        tbpMarketAnalysis.setFont(new java.awt.Font("Arial Unicode MS", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpMarketAnalysis, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbpMarketAnalysis, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbtMarketAnalysisMonthlyMarketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisMonthlyMarketsActionPerformed
        dataEntry.populateCommodities(getSystemId(rbtMarketAnalysisMonthlyMarkets), cboMarketAnalysisMonthlyCommodity);
    }
        // TODO add your handling code here:}//GEN-LAST:event_rbtMarketAnalysisMonthlyMarketsActionPerformed

        private void rbtMarketAnalysisMonthlyRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisMonthlyRegionActionPerformed
        if (rbtMarketAnalysisMonthlyRegion.isSelected()) {
            cboMarketAnalysisMonthlyMarketZone.setEnabled(false);
            cboMarketAnalysisMonthlyFieldAnalyst.setEnabled(false);
            cboMarketAnalysisMonthlyRegion.setEnabled(true);
        //Load region markets in monthly analysis panel
        dataEntry.populateMarkets("region",
                region.getRegionId(cboMarketAnalysisMonthlyRegion.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisMonthlyMarkets), pnlMarketAnalysisMonthlySelectMarketsInner);            
        }
    }
        // TODO add your handling code here:}//GEN-LAST:event_rbtMarketAnalysisMonthlyRegionActionPerformed

        private void rbtMarketAnalysisMonthlyFieldAnalystActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisMonthlyFieldAnalystActionPerformed
        if (rbtMarketAnalysisMonthlyFieldAnalyst.isSelected()) {
            cboMarketAnalysisMonthlyMarketZone.setEnabled(false);
            cboMarketAnalysisMonthlyFieldAnalyst.setEnabled(true);
            cboMarketAnalysisMonthlyRegion.setEnabled(false);
        //Load field analyst markets in monthly analysis panel
        dataEntry.populateMarkets("fieldAnalyst",
                fieldAnalyst.getFieldAnalystId(cboMarketAnalysisMonthlyFieldAnalyst.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisMonthlyMarkets), pnlMarketAnalysisMonthlySelectMarketsInner);            
        }
    }
        // TODO add your handling code here:}//GEN-LAST:event_rbtMarketAnalysisMonthlyFieldAnalystActionPerformed

        private void rbtMarketAnalysisMonthlyMarketZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisMonthlyMarketZoneActionPerformed
        if (rbtMarketAnalysisMonthlyMarketZone.isSelected()) {
            cboMarketAnalysisMonthlyMarketZone.setEnabled(true);
            cboMarketAnalysisMonthlyFieldAnalyst.setEnabled(false);
            cboMarketAnalysisMonthlyRegion.setEnabled(false);
           //Load market zone markets in monthly analysis panel
        dataEntry.populateMarkets("marketZone",
                marketZone.getMarketZoneId(cboMarketAnalysisMonthlyMarketZone.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisMonthlyMarkets), pnlMarketAnalysisMonthlySelectMarketsInner);
        }
    }
        // TODO add your handling code here:}//GEN-LAST:event_rbtMarketAnalysisMonthlyMarketZoneActionPerformed

        private void btnMarketyAnalysisMonthlyExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarketyAnalysisMonthlyExportActionPerformed
            // Export table to excel
            excelExporter = null;
            excelExporter = new ExcelExporter(tblMarketAnalysisMonthly, "", false);
    }//GEN-LAST:event_btnMarketyAnalysisMonthlyExportActionPerformed

    private void btnMarketyAnalysisTotExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarketyAnalysisTotExportActionPerformed
        // Export table to excel
        excelExporter = null;
        excelExporter = new ExcelExporter(tblMarketAnalysisTot, "", false);
    }//GEN-LAST:event_btnMarketyAnalysisTotExportActionPerformed

    private void rbtMarketAnalysisTotMarketZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisTotMarketZoneActionPerformed
        if (rbtMarketAnalysisTotMarketZone.isSelected()) {
            cboMarketAnalysisTotMarketZone.setEnabled(true);
            cboMarketAnalysisTotFieldAnalyst.setEnabled(false);
            cboMarketAnalysisTotRegion.setEnabled(false);
        //Load market zone markets in monthly analysis panel
        dataEntry.populateMarkets("marketZone",
                marketZone.getMarketZoneId(cboMarketAnalysisTotMarketZone.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisTotMarkets), pnlMarketAnalysisTotSelectMarketsInner);            
        }
    }//GEN-LAST:event_rbtMarketAnalysisTotMarketZoneActionPerformed

    private void rbtMarketAnalysisTotFieldAnalystActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisTotFieldAnalystActionPerformed
        if (rbtMarketAnalysisTotFieldAnalyst.isSelected()) {
            cboMarketAnalysisTotMarketZone.setEnabled(false);
            cboMarketAnalysisTotFieldAnalyst.setEnabled(true);
            cboMarketAnalysisTotRegion.setEnabled(false);
        //Load field analyst markets in monthly analysis panel
        dataEntry.populateMarkets("fieldAnalyst",
                fieldAnalyst.getFieldAnalystId(cboMarketAnalysisTotFieldAnalyst.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisTotMarkets), pnlMarketAnalysisTotSelectMarketsInner);            
        }
    }//GEN-LAST:event_rbtMarketAnalysisTotFieldAnalystActionPerformed

    private void rbtMarketAnalysisTotRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisTotRegionActionPerformed
        if (rbtMarketAnalysisTotRegion.isSelected()) {
            cboMarketAnalysisTotMarketZone.setEnabled(false);
            cboMarketAnalysisTotFieldAnalyst.setEnabled(false);
            cboMarketAnalysisTotRegion.setEnabled(true);
        //Load region markets in monthly analysis panel
        dataEntry.populateMarkets("region",
                region.getRegionId(cboMarketAnalysisTotRegion.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisTotMarkets), pnlMarketAnalysisTotSelectMarketsInner);            
        }
    }//GEN-LAST:event_rbtMarketAnalysisTotRegionActionPerformed

    private void rbtMarketAnalysisTotMarketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisTotMarketsActionPerformed
        dataEntry.populateCommodities(getSystemId(rbtMarketAnalysisTotMarkets), cboMarketAnalysisTotFirstCommodity,
                cboMarketAnalysisTotSecondCommodity);
    }//GEN-LAST:event_rbtMarketAnalysisTotMarketsActionPerformed

    private void btnMarketyAnalysisMarketUpdateExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarketyAnalysisMarketUpdateExportActionPerformed
        // Export table to excel
        excelExporter = null;
        excelExporter = new ExcelExporter(tblMarketAnalysisMarketUpdate, "", false);
    }//GEN-LAST:event_btnMarketyAnalysisMarketUpdateExportActionPerformed

    private void rbtMarketAnalysisMonthlySlimsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisMonthlySlimsActionPerformed
        dataEntry.populateCommodities(getSystemId(rbtMarketAnalysisMonthlyMarkets), cboMarketAnalysisMonthlyCommodity);
    }//GEN-LAST:event_rbtMarketAnalysisMonthlySlimsActionPerformed

    private void rbtMarketAnalysisTotSlimsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtMarketAnalysisTotSlimsActionPerformed
        dataEntry.populateCommodities(getSystemId(rbtMarketAnalysisTotMarkets), cboMarketAnalysisTotFirstCommodity,
                cboMarketAnalysisTotSecondCommodity);
    }//GEN-LAST:event_rbtMarketAnalysisTotSlimsActionPerformed

    private void cboMarketAnalysisMonthlyMarketZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyMarketZoneActionPerformed
        //Load market zone markets in monthly analysis panel
        dataEntry.populateMarkets("marketZone",
                marketZone.getMarketZoneId(cboMarketAnalysisMonthlyMarketZone.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisMonthlyMarkets), pnlMarketAnalysisMonthlySelectMarketsInner);

    }//GEN-LAST:event_cboMarketAnalysisMonthlyMarketZoneActionPerformed

    private void cboMarketAnalysisMonthlyMarketZoneItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyMarketZoneItemStateChanged
        //Load market zone markets in monthly analysis panel
         /*
         * dataEntry.populateMarkets("marketZone",
         * marketZone.getMarketZoneId(cboMarketAnalysisMonthlyMarketZone.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisMonthlyMarkets),pnlMarketAnalysisMonthlySelectMarketsInner);
         */
    }//GEN-LAST:event_cboMarketAnalysisMonthlyMarketZoneItemStateChanged

    private void cboMarketAnalysisMonthlyFieldAnalystItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyFieldAnalystItemStateChanged
    }//GEN-LAST:event_cboMarketAnalysisMonthlyFieldAnalystItemStateChanged

    private void cboMarketAnalysisMonthlyRegionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyRegionItemStateChanged
        //Load region markets in monthly analysis panel
        /*
         * dataEntry.populateMarkets("region",
         * marketZone.getMarketZoneId(cboMarketAnalysisMonthlyRegion.getSelectedItem().toString()),
         * getApplicationId(rbtMarketAnalysisMonthlyMarkets),pnlMarketAnalysisMonthlySelectMarketsInner);
         */
    }//GEN-LAST:event_cboMarketAnalysisMonthlyRegionItemStateChanged

    private void cboMarketAnalysisMonthlyRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyRegionActionPerformed
        //Load region markets in monthly analysis panel
        dataEntry.populateMarkets("region",
                region.getRegionId(cboMarketAnalysisMonthlyRegion.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisMonthlyMarkets), pnlMarketAnalysisMonthlySelectMarketsInner);
    }//GEN-LAST:event_cboMarketAnalysisMonthlyRegionActionPerformed

    private void cboMarketAnalysisMonthlyFieldAnalystActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyFieldAnalystActionPerformed
        //Load field analyst markets in monthly analysis panel
        dataEntry.populateMarkets("fieldAnalyst",
                fieldAnalyst.getFieldAnalystId(cboMarketAnalysisMonthlyFieldAnalyst.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisMonthlyMarkets), pnlMarketAnalysisMonthlySelectMarketsInner);
    }//GEN-LAST:event_cboMarketAnalysisMonthlyFieldAnalystActionPerformed

    private void cboMarketAnalysisTotFieldAnalystActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisTotFieldAnalystActionPerformed
        //Load field analyst markets in monthly analysis panel
        dataEntry.populateMarkets("fieldAnalyst",
                fieldAnalyst.getFieldAnalystId(cboMarketAnalysisTotFieldAnalyst.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisTotMarkets), pnlMarketAnalysisTotSelectMarketsInner);
    }//GEN-LAST:event_cboMarketAnalysisTotFieldAnalystActionPerformed

    private void cboMarketAnalysisTotRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisTotRegionActionPerformed
        //Load region markets in monthly analysis panel
        dataEntry.populateMarkets("region",
                region.getRegionId(cboMarketAnalysisTotRegion.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisTotMarkets), pnlMarketAnalysisTotSelectMarketsInner);
    }//GEN-LAST:event_cboMarketAnalysisTotRegionActionPerformed

    private void cboMarketAnalysisTotMarketZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisTotMarketZoneActionPerformed
        //Load market zone markets in monthly analysis panel
        dataEntry.populateMarkets("marketZone",
                marketZone.getMarketZoneId(cboMarketAnalysisTotMarketZone.getSelectedItem().toString()),
                getApplicationId(rbtMarketAnalysisTotMarkets), pnlMarketAnalysisTotSelectMarketsInner);
    }//GEN-LAST:event_cboMarketAnalysisTotMarketZoneActionPerformed

    private void cboMarketAnalysisMarketUpdateMarketZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMarketUpdateMarketZoneActionPerformed
        //Load market zone markets in monthly analysis panel
        dataEntry.populateMarkets("marketZone",
                marketZone.getMarketZoneId(cboMarketAnalysisMarketUpdateMarketZone.getSelectedItem().toString()), 1, pnlMarketAnalysisMarketUpdateMarketsInner);
    }//GEN-LAST:event_cboMarketAnalysisMarketUpdateMarketZoneActionPerformed

    private void btnMarketAnalysisMonthlyGetDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarketAnalysisMonthlyGetDataActionPerformed
        // Load monthly analysis data on table
        int startYear = 0;
        int endYear = 0;
        String chartTitle = "";
        String chartYAxisLabel = "";
        try {
            //Populate table with data
            pnlMarketAnalysisMonthly.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            startYear = Integer.parseInt(cboMarketAnalysisMonthlyStartYear.getSelectedItem().toString());
            endYear = Integer.parseInt(cboMarketAnalysisMonthlyEndYear.getSelectedItem().toString());
            //Remove any existing rows
            tbmMarketAnalysisMonthly.setRowCount(0);

            marketAnalysis.monthlyAnalysis(true, startYear, endYear, tblMarketAnalysisMonthly,
                    tbmMarketAnalysisMonthly, pnlMarketAnalysisMonthlySelectSeriesInner, pnlMarketAnalysisMonthlyCalculateAverageInner,
                    pnlMarketAnalysisMonthlySelectMarketsInner, cboMarketAnalysisMonthlyCommodity, null);

            //Set chart settings
            chartSettings.setChartSettings(Login.userId);
            //Draw chart
            chartTitle = cboMarketAnalysisMonthlyCommodity.getSelectedItem().toString();
            //chartYAxisLabel = "SoShl";
            chartYAxisLabel = ChartSettings.rangeAxisLabelName;

            marketAnalysis.showChart(pnlMarketAnalysisChartBottom, pnlMarketAnalysisMonthlySelectSeriesInner,
                    pnlMarketAnalysisMonthlyCalculateAverageInner, chkMarketAnalysisMonthlyShowAverage.isSelected(),
                    tblMarketAnalysisMonthly, cboMarketAnalysisMonthlyXaxisStartMonth.getSelectedIndex() + 1, chartTitle, chartYAxisLabel);

            //Copy chart to copychart
            marketAnalysis.equalizeCharts();

            //chartSettings.setChartTitleOnTextField(txtChartSettingsTitle);
            dlgMarketAnalysisChart.setSize(920, 650);
            dlgMarketAnalysisChart.setLocationRelativeTo(null);
            dlgMarketAnalysisChart.setVisible(true);
        } catch (Exception exception) {
        } finally {
            pnlMarketAnalysisMonthly.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnMarketAnalysisMonthlyGetDataActionPerformed

    private void cboMarketAnalysisTotEndYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisTotEndYearActionPerformed
    }//GEN-LAST:event_cboMarketAnalysisTotEndYearActionPerformed

    private void btnMarketAnalysisTotGetDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarketAnalysisTotGetDataActionPerformed
        // Load monthly analysis data on table
        int startYear = 0;
        int endYear = 0;
        String chartTitle = "";
        String chartYAxisLabel = "";
        try {
            //Populate table with data
            pnlMarketAnalysisTot.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            startYear = Integer.parseInt(cboMarketAnalysisTotStartYear.getSelectedItem().toString());
            endYear = Integer.parseInt(cboMarketAnalysisTotEndYear.getSelectedItem().toString());
            //Remove any existing rows
            tbmMarketAnalysisTot.setRowCount(0);

            marketAnalysis.monthlyAnalysis(true, startYear, endYear, tblMarketAnalysisTot,
                    tbmMarketAnalysisTot, pnlMarketAnalysisTotSelectSeriesInner, pnlMarketAnalysisTotCalculateAverageInner,
                    pnlMarketAnalysisTotSelectMarketsInner, cboMarketAnalysisTotFirstCommodity, cboMarketAnalysisTotSecondCommodity);

            //Draw chart
            chartTitle = "TOT " + cboMarketAnalysisTotFirstCommodity.getSelectedItem().toString() + " TO "
                    + cboMarketAnalysisTotSecondCommodity.getSelectedItem().toString();
            chartYAxisLabel = "";
            marketAnalysis.showChart(pnlMarketAnalysisTotChart, pnlMarketAnalysisTotSelectSeriesInner,
                    pnlMarketAnalysisTotCalculateAverageInner, chkMarketAnalysisTotShowAverage.isSelected(), tblMarketAnalysisTot,
                    cboMarketAnalysisTotXaxisStartMonth.getSelectedIndex() + 1, chartTitle, chartYAxisLabel);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            pnlMarketAnalysisTot.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnMarketAnalysisTotGetDataActionPerformed

    private void btnMarketAnalysisMarketUpdateGetDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarketAnalysisMarketUpdateGetDataActionPerformed

        int year = 0;
        int month = 0;
        int fiveYearStart = 0;
        int fiveYearEnd = 0;
        String lastSeason = "";
        String marketZoneName = "";
        String previousMonth = "";
        String currentMonth = "";

        try {
            //Set cursor to busy
            //setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            pnlMarketAnalysisMarketUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            //Reset table row count
            tbmMarketAnalysisMarketUpdate.setRowCount(0);
            //Reset table column count
            tbmMarketAnalysisMarketUpdate.setColumnCount(0);
            year = Integer.parseInt(cboMarketAnalysisMarketUpdateStartYear.getSelectedItem().toString());
            month = cboMarketAnalysisMarketUpdateMonth.getSelectedIndex() + 1;
            fiveYearStart = Integer.parseInt(cboMarketAnalysisMarketUpdateAvgStartYear.getSelectedItem().toString());
            fiveYearEnd = Integer.parseInt(cboMarketAnalysisMarketUpdateAvgFinishYear.getSelectedItem().toString());
            lastSeason = cboMarketAnalysisMarketUpdateSeason.getSelectedItem().toString();
            marketZoneName = cboMarketAnalysisMarketUpdateMarketZone.getSelectedItem().toString();
            currentMonth = cboMarketAnalysisMarketUpdateMonth.getSelectedItem().toString().substring(0, 3);
            previousMonth = !cboMarketAnalysisMarketUpdateMonth.getSelectedItem().toString().equalsIgnoreCase("January")
                    ? cboMarketAnalysisMarketUpdateMonth.getItemAt(cboMarketAnalysisMarketUpdateMonth.getSelectedIndex() - 1).
                    toString().substring(0, 3) : "Dec";

            //Check five year average validity
            if (validation.isMarketUpdateFiveYearAverageValid(fiveYearStart, fiveYearEnd)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                //Create table columns
                prepareMarketUpdateTable(year, lastSeason.toUpperCase(), currentMonth, previousMonth, fiveYearStart, fiveYearEnd);

                //Add indicator names, categories, and values to table
                addMarketUpdateIndicatorsAndValuesToTable(year, month, fiveYearStart, fiveYearEnd, lastSeason, marketZoneName,
                        pnlMarketAnalysisMarketUpdateMarketsInner, tbmMarketAnalysisMarketUpdate, tblMarketAnalysisMarketUpdate);

                //Auto resize columns to fit contents
                ColumnsAutoSizer.sizeColumnsToFit(tblMarketAnalysisMarketUpdate);
            } else {
                JOptionPane.showMessageDialog(this, "Five year average start and end are not properly chosen."
                        + "\nPlease choose an end year greater than the start year chosen."
                        + "\nEnsure that the years chosen is a five year rage", "",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exception) {
            // exception.printStackTrace();
        } finally {
            //setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            pnlMarketAnalysisMarketUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnMarketAnalysisMarketUpdateGetDataActionPerformed

    private void pnlMarketAnalysisMonthlyCalculateAverageInnerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMarketAnalysisMonthlyCalculateAverageInnerMouseReleased
        // Check if there's a year selected
        if (marketAnalysis.isCheckboxSelected(pnlMarketAnalysisMonthlyCalculateAverageInner)) {
            chkMarketAnalysisMonthlyShowAverage.setEnabled(true);
        } else {
            chkMarketAnalysisMonthlyShowAverage.setEnabled(false);
        }
    }//GEN-LAST:event_pnlMarketAnalysisMonthlyCalculateAverageInnerMouseReleased

    private void pnlMarketAnalysisMonthlyCalculateAverageOuterMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMarketAnalysisMonthlyCalculateAverageOuterMouseReleased
    }//GEN-LAST:event_pnlMarketAnalysisMonthlyCalculateAverageOuterMouseReleased

    private void pnlMarketAnalysisMonthlyCalculateAverageInnerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMarketAnalysisMonthlyCalculateAverageInnerMouseExited
    }//GEN-LAST:event_pnlMarketAnalysisMonthlyCalculateAverageInnerMouseExited

    private void pnlMarketAnalysisMonthlyCalculateAverageOuterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMarketAnalysisMonthlyCalculateAverageOuterMouseClicked
    }//GEN-LAST:event_pnlMarketAnalysisMonthlyCalculateAverageOuterMouseClicked

    private void scpMarketAnalysisMonthlyCalculateAverageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scpMarketAnalysisMonthlyCalculateAverageMouseClicked
    }//GEN-LAST:event_scpMarketAnalysisMonthlyCalculateAverageMouseClicked

    private void pnlMarketAnalysisMonthlyCalculateAverageOuterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pnlMarketAnalysisMonthlyCalculateAverageOuterFocusLost
        // TODO add your handling code here:

        if (marketAnalysis.isCheckboxSelected(pnlMarketAnalysisMonthlyCalculateAverageInner)) {
            chkMarketAnalysisMonthlyShowAverage.setEnabled(true);
        } else {
            chkMarketAnalysisMonthlyShowAverage.setEnabled(false);
        }
    }//GEN-LAST:event_pnlMarketAnalysisMonthlyCalculateAverageOuterFocusLost

    private void cboMarketAnalysisMonthlyStartYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMarketAnalysisMonthlyStartYearActionPerformed
    }//GEN-LAST:event_cboMarketAnalysisMonthlyStartYearActionPerformed

    private void btnMonthlyAnalysisSaveChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonthlyAnalysisSaveChartActionPerformed
        // Save chart
        java.io.File file = null;
        BufferedWriter outFile = null;
        try {
            fchSaveChart = new JFileChooser();
            javax.swing.filechooser.FileFilter fileFilterPng = new ExtensionFileFilter("PNG Files", "PNG");
            fchSaveChart.setFileFilter(fileFilterPng);
            fchSaveChart.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //fchSaveChart.setFileSystemView(JFileChooser.FILES_AND_DIRECTORIES);
            //Show save dialog
            if (fchSaveChart.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                //Get filename
                file = new java.io.File(fchSaveChart.getSelectedFile() + ".png");
                marketAnalysis.saveChartAsPng(file, chartImageWidth, chartImageHeight);
            }
        } catch (Exception exception) {
        }
    }//GEN-LAST:event_btnMonthlyAnalysisSaveChartActionPerformed

    private void btnChartSettingsShowChartSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChartSettingsShowChartSettingsActionPerformed
        // Show dialog for editing chart settings
        marketAnalysis.editChartProperties();
        // Save any new changes to chart settings
        // chartSettings.saveChartSettingsChanges(userId);
    }//GEN-LAST:event_btnChartSettingsShowChartSettingsActionPerformed

    private void dlgMarketAnalysisChartWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgMarketAnalysisChartWindowClosing
        // Check whether user needs to commit some changes before exiting this window
        marketAnalysis.savechartChangesIfAny(dlgMarketAnalysisChart);
    }//GEN-LAST:event_dlgMarketAnalysisChartWindowClosing

    private void prepareMarketUpdateTable(int year, String lastSeason, String currentMonth, String previousMonth,
            int fiveYearStart, int fiveYearEnd) {
        int previousMonthYear = 0;
        //Determine previous month year
        if (previousMonth.startsWith("De")) {
            previousMonthYear = year - 1;
        } else {
            previousMonthYear = year;
        }
        //ArrayList<Integer> fiveYearAverageList ;
        try {
            //fiveYearAverageList = this.marketupd
            dataEntry.prepareMarketAnalysisTable("Market Update", tbmMarketAnalysisMarketUpdate, tblMarketAnalysisMarketUpdate,
                    "INDICATOR", "5-YEAR AVG", currentMonth + "-" + (year - 1), "END OF " + lastSeason, previousMonth + "-" + previousMonthYear,
                    "CURRENT NOMINAL PRICE", "CURRENT REAL(DEFLATED PRICE)",
                    "% CHANGE SAME MONTH PREVIOUS YEAR", "% CHANGE PREVIOUS MONTH", "% CHANGE", "% CHANGE 5-YEAR AVG("
                    + fiveYearStart + "-" + fiveYearEnd + ")", fiveYearStart + "", (fiveYearStart + 1) + "", (fiveYearStart + 2) + "",
                    (fiveYearStart + 3) + "", (fiveYearStart + 4) + "");
            //Set width of first column
        } catch (Exception exception) {
        }
    }

    private void addMarketUpdateIndicatorsAndValuesToTable(int year, int month, int fiveYearStart, int fiveYearEnd, String lastSeason,
            String marketZoneName, javax.swing.JPanel pnlMarkets, javax.swing.table.DefaultTableModel tbmMarketUpdate,
            javax.swing.JTable tblMarketUpdate) {
        try {

            /*
             * marketAnalysis.populateMarketUpdateIndicators(year, month,
             * fiveYearStart, fiveYearEnd, lastSeason, marketZoneName,
             * pnlMarkets, tbmMarketUpdate, tblMarketUpdate);
             */

            marketAnalysis.getMarketUpdateValues(year, month, fiveYearStart, fiveYearEnd, lastSeason, marketZoneName, pnlMarkets,
                    tbmMarketUpdate, tblMarketUpdate);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void showConfirmDialog() {
        // Allow user to conmfirm whether they want to exit
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
        if (result <= 0) {
            this.dispose();
        }
    }

    private String[] getDataTrustLevelComboBoxItems() {
        String[] dataTrustLevelComboBoxItems = {"Poor", "Fair", "Good"};
        return dataTrustLevelComboBoxItems;
    }

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
            java.util.logging.Logger.getLogger(MarketAnalysisUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MarketAnalysisUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MarketAnalysisUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MarketAnalysisUi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                MarketAnalysisUi dialog = new MarketAnalysisUi(new javax.swing.JFrame(), true);
                /*
                 * dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                 *
                 * @Override public void
                 * windowClosing(java.awt.event.WindowEvent e) { System.exit(0);
                 * } });
                 */
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgMarketAnalysisMonthlyDataset;
    private javax.swing.ButtonGroup btgMarketAnalysisMonthlySpace;
    private javax.swing.ButtonGroup btgMarketAnalysisTotDataset;
    private javax.swing.ButtonGroup btgMarketAnalysisTotSpace;
    private javax.swing.JButton btnChartSettingsFontCancel;
    private javax.swing.JButton btnChartSettingsFontOk;
    private javax.swing.JButton btnChartSettingsShowChartSettings;
    private javax.swing.JButton btnMarketAnalysisMarketUpdateGetData;
    private javax.swing.JButton btnMarketAnalysisMonthlyGetData;
    private javax.swing.JButton btnMarketAnalysisTotGetData;
    private javax.swing.JButton btnMarketyAnalysisMarketUpdateExport;
    private javax.swing.JButton btnMarketyAnalysisMonthlyExport;
    private javax.swing.JButton btnMarketyAnalysisTotExport;
    private javax.swing.JButton btnMonthlyAnalysisSaveChart;
    private javax.swing.JLabel cboMarketAnalysisMarketUpdateAvgEndYear;
    private javax.swing.JComboBox cboMarketAnalysisMarketUpdateAvgFinishYear;
    private javax.swing.JComboBox cboMarketAnalysisMarketUpdateAvgStartYear;
    private javax.swing.JComboBox cboMarketAnalysisMarketUpdateMarketZone;
    private javax.swing.JComboBox cboMarketAnalysisMarketUpdateMonth;
    private javax.swing.JComboBox cboMarketAnalysisMarketUpdateSeason;
    private javax.swing.JComboBox cboMarketAnalysisMarketUpdateStartYear;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyCommodity;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyEndYear;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyFieldAnalyst;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyMarketZone;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyRegion;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyStartYear;
    private javax.swing.JComboBox cboMarketAnalysisMonthlyXaxisStartMonth;
    private javax.swing.JComboBox cboMarketAnalysisTotEndYear;
    private javax.swing.JComboBox cboMarketAnalysisTotFieldAnalyst;
    private javax.swing.JComboBox cboMarketAnalysisTotFirstCommodity;
    private javax.swing.JComboBox cboMarketAnalysisTotMarketZone;
    private javax.swing.JComboBox cboMarketAnalysisTotRegion;
    private javax.swing.JComboBox cboMarketAnalysisTotSecondCommodity;
    private javax.swing.JComboBox cboMarketAnalysisTotStartYear;
    private javax.swing.JComboBox cboMarketAnalysisTotXaxisStartMonth;
    private javax.swing.JCheckBox chkMarketAnalysisMonthlyShowAverage;
    private javax.swing.JCheckBox chkMarketAnalysisTotShowAverage;
    private javax.swing.JDialog dlgChartSettingsFont;
    private javax.swing.JDialog dlgMarketAnalysisChart;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblMarketAnalysisMarketUpdateAvgStartYear;
    private javax.swing.JLabel lblMarketAnalysisMarketUpdateMarketZone;
    private javax.swing.JLabel lblMarketAnalysisMonthlyCommodity;
    private javax.swing.JLabel lblMarketAnalysisMonthlyCommodity1;
    private javax.swing.JLabel lblMarketAnalysisMonthlyDataset;
    private javax.swing.JLabel lblMarketAnalysisMonthlyDataset1;
    private javax.swing.JLabel lblMarketAnalysisMonthlyEndYear;
    private javax.swing.JLabel lblMarketAnalysisMonthlyEndYear1;
    private javax.swing.JLabel lblMarketAnalysisMonthlyStartYear;
    private javax.swing.JLabel lblMarketAnalysisMonthlyStartYear1;
    private javax.swing.JLabel lblMarketAnalysisMonthlyXaxisStartMonth;
    private javax.swing.JLabel lblMarketAnalysisTotEndYear;
    private javax.swing.JLabel lblMarketAnalysisTotFirstCommodity;
    private javax.swing.JLabel lblMarketAnalysisTotSecondCommodity;
    private javax.swing.JLabel lblMarketAnalysisTotStartYear;
    private javax.swing.JLabel lblMarketAnalysisTotXaxisStartMonth;
    private javax.swing.JList lstChartSettingsFontNames;
    private javax.swing.JList lstChartSettingsFontSizes;
    private javax.swing.JPanel pnlChartSettingsFontAttributes;
    private javax.swing.JPanel pnlChartSettingsFontBottom;
    private javax.swing.JPanel pnlChartSettingsFontFont;
    private javax.swing.JPanel pnlChartSettingsFontMain;
    private javax.swing.JPanel pnlChartSettingsFontSize;
    public javax.swing.JPanel pnlMarketAnalaysisMonthly;
    public javax.swing.JPanel pnlMarketAnalaysisTot;
    private javax.swing.JPanel pnlMarketAnalysisChart;
    private javax.swing.JPanel pnlMarketAnalysisChartBottom;
    private javax.swing.JPanel pnlMarketAnalysisChartShowChartSettingsCheckBox;
    public javax.swing.JPanel pnlMarketAnalysisMarketUpdate;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdate5YearAverageYears;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdateBottom;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdateMarketZone;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdateMarketsInner;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdateMarketsOuter;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdateMiddle;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdatePeriod;
    private javax.swing.JPanel pnlMarketAnalysisMarketUpdateTop;
    private javax.swing.JPanel pnlMarketAnalysisMonthly;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyBottom;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyCalculateAverageInner;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyCalculateAverageOuter;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyDataset;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyMiddle;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyOther;
    private javax.swing.JPanel pnlMarketAnalysisMonthlySelectMarketsInner;
    private javax.swing.JPanel pnlMarketAnalysisMonthlySelectMarketsOuter;
    private javax.swing.JPanel pnlMarketAnalysisMonthlySelectSeriesInner;
    private javax.swing.JPanel pnlMarketAnalysisMonthlySelectSeriesOuter;
    private javax.swing.JPanel pnlMarketAnalysisMonthlySpace;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyTemporal;
    private javax.swing.JPanel pnlMarketAnalysisMonthlyTop;
    private javax.swing.JPanel pnlMarketAnalysisTot;
    private javax.swing.JPanel pnlMarketAnalysisTotBottom;
    private javax.swing.JPanel pnlMarketAnalysisTotCalculateAverageInner;
    private javax.swing.JPanel pnlMarketAnalysisTotCalculateAverageOuter;
    private javax.swing.JPanel pnlMarketAnalysisTotChart;
    private javax.swing.JPanel pnlMarketAnalysisTotDataset;
    private javax.swing.JPanel pnlMarketAnalysisTotMiddle;
    private javax.swing.JPanel pnlMarketAnalysisTotOther;
    private javax.swing.JPanel pnlMarketAnalysisTotSelectMarketsInner;
    private javax.swing.JPanel pnlMarketAnalysisTotSelectMarketsOuter;
    private javax.swing.JPanel pnlMarketAnalysisTotSelectSeriesInner;
    private javax.swing.JPanel pnlMarketAnalysisTotSelectSeriesOuter;
    private javax.swing.JPanel pnlMarketAnalysisTotSpace;
    private javax.swing.JPanel pnlMarketAnalysisTotTemporal;
    private javax.swing.JPanel pnlMarketAnalysisTotTop;
    private javax.swing.JRadioButton rbtMarketAnalysisMonthlyFieldAnalyst;
    private javax.swing.JRadioButton rbtMarketAnalysisMonthlyMarketZone;
    private javax.swing.JRadioButton rbtMarketAnalysisMonthlyMarkets;
    private javax.swing.JRadioButton rbtMarketAnalysisMonthlyRegion;
    private javax.swing.JRadioButton rbtMarketAnalysisMonthlySlims;
    private javax.swing.JRadioButton rbtMarketAnalysisTotFieldAnalyst;
    private javax.swing.JRadioButton rbtMarketAnalysisTotMarketZone;
    private javax.swing.JRadioButton rbtMarketAnalysisTotMarkets;
    private javax.swing.JRadioButton rbtMarketAnalysisTotRegion;
    private javax.swing.JRadioButton rbtMarketAnalysisTotSlims;
    private javax.swing.JScrollPane scpMarketAnalysisMarketUpdateMiddle;
    private javax.swing.JScrollPane scpMarketAnalysisMonthlyCalculateAverage;
    private javax.swing.JScrollPane scpMarketAnalysisMonthlyMiddle;
    private javax.swing.JScrollPane scpMarketAnalysisMonthlySelectMarkets;
    private javax.swing.JScrollPane scpMarketAnalysisMonthlySelectSeries;
    private javax.swing.JScrollPane scpMarketAnalysisTotCalculateAverage;
    private javax.swing.JScrollPane scpMarketAnalysisTotMiddle;
    private javax.swing.JScrollPane scpMarketAnalysisTotSelectMarkets;
    private javax.swing.JScrollPane scpMarketAnalysisTotSelectSeries;
    private javax.swing.JScrollPane scplMarketAnalysisMarketUpdateMarkets;
    private javax.swing.JTable tblMarketAnalysisMarketUpdate;
    private javax.swing.JTable tblMarketAnalysisMonthly;
    private javax.swing.JTable tblMarketAnalysisTot;
    public javax.swing.JTabbedPane tbpMarketAnalysis;
    // End of variables declaration//GEN-END:variables

    private void prepareDataEntryTable(DefaultTableModel tableModel, String applicationIds) {
        try {
            queryBuilder.querySelectIndicators(applicationIds);
        } catch (Exception exception) {
        }
    }

    private String getSystemId(JRadioButton rbtSystemId) {
        String systemId = "";
        if (rbtSystemId.isSelected()) {
            systemId = "1,3";
        } else {
            systemId = "2,3,4";
        }
        return systemId;
    }

    private int getApplicationId(JRadioButton rbtSystemId) {
        int systemId = 0;
        if (rbtSystemId.isSelected()) {
            systemId = 1;
        } else {
            systemId = 2;
        }
        return systemId;
    }
}
