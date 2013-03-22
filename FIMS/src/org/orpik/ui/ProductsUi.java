/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.ui;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.orpik.core.Product;
import org.orpik.core.Tanks;
import org.orpik.helper.HelperProducts;

/**
 *
 * @author chemweno
 */
public class ProductsUi extends javax.swing.JPanel {
    private DefaultTableModel tbmProducts = new DefaultTableModel();
    private HelperProducts helperProducts = new HelperProducts();
    private Product product = new Product();
    /**
     * Creates new form ProductsUi
     */
    public ProductsUi() {
        initComponents();    
        //Set default button in edit product dialog
        dlgProducts.getRootPane().setDefaultButton(btnProductsSave);
    }

    public void prepareTable() {
        try {
            helperProducts.prepareTable(tbmProducts, tblProducts, "Product Name", "Category","Unit", "Current Stock Level", "Current Price");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    } 
    
    public void populateTable(){
        try{
            helperProducts.populateProductsOnTable(tbmProducts, tblProducts);
        }catch(Exception exception){
        exception.printStackTrace();
        }
    }
   /*  tblProducts = new javax.swing.JTable(tbmProducts) {
     public Component prepareRenderer(TableCellRenderer renderer, int row, int
     col) { Component comp = super.prepareRenderer(renderer, row, col); 
     if(row%2==0) {
     //Set even numbered rows to yellow 238-232-170
     comp.setBackground(new java.awt.Color(224, 224, 255)); 
     } else {
     //Set odd numbered rows to purple 147-112-219
     comp.setBackground(new java.awt.Color(255, 255, 255)); 
     } return comp; 
     }
     
     public boolean isCellEditable(int row, int column) {
     //Disable editing on all rows of the table
     return false;	  
     } 
     };	*/    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgProducts = new javax.swing.JDialog();
        lblEditProductTitle = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnProductsSave = new javax.swing.JButton();
        btnProductsCancel = new javax.swing.JButton();
        cboProductsProductCategory = new javax.swing.JComboBox();
        cboProductsProductUnit = new javax.swing.JComboBox();
        txtProductsProductName = new javax.swing.JTextField();
        btnProductsAddProductCategory = new javax.swing.JButton();
        btnProductsAddProductUnit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducts = new javax.swing.JTable();
        btnEditProduct = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        dlgProducts.setMinimumSize(new java.awt.Dimension(440, 250));
        dlgProducts.setResizable(false);

        lblEditProductTitle.setFont(new java.awt.Font("Arial Unicode MS", 0, 24)); // NOI18N
        lblEditProductTitle.setText("Edit Product");

        jLabel3.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jLabel3.setText("Product Name");

        jLabel4.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jLabel4.setText("Product Category");

        jLabel5.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        jLabel5.setText("Product Unit");

        btnProductsSave.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        btnProductsSave.setMnemonic('S');
        btnProductsSave.setText("Save");

        btnProductsCancel.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        btnProductsCancel.setMnemonic('C');
        btnProductsCancel.setText("Cancel");
        btnProductsCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductsCancelActionPerformed(evt);
            }
        });

        cboProductsProductCategory.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N

        cboProductsProductUnit.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N

        txtProductsProductName.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N

        btnProductsAddProductCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add_16x16.png"))); // NOI18N
        btnProductsAddProductCategory.setToolTipText("Add new product category");

        btnProductsAddProductUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add_16x16.png"))); // NOI18N
        btnProductsAddProductUnit.setToolTipText("Add new product unit");

        javax.swing.GroupLayout dlgProductsLayout = new javax.swing.GroupLayout(dlgProducts.getContentPane());
        dlgProducts.getContentPane().setLayout(dlgProductsLayout);
        dlgProductsLayout.setHorizontalGroup(
            dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(dlgProductsLayout.createSequentialGroup()
                        .addComponent(btnProductsSave, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnProductsCancel))
                    .addGroup(dlgProductsLayout.createSequentialGroup()
                        .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductsProductName)
                            .addComponent(cboProductsProductCategory, 0, 279, Short.MAX_VALUE)
                            .addComponent(cboProductsProductUnit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnProductsAddProductCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProductsAddProductUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgProductsLayout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addComponent(lblEditProductTitle)
                .addGap(160, 160, 160))
        );

        dlgProductsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnProductsCancel, btnProductsSave});

        dlgProductsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnProductsAddProductCategory, btnProductsAddProductUnit});

        dlgProductsLayout.setVerticalGroup(
            dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgProductsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblEditProductTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtProductsProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(dlgProductsLayout.createSequentialGroup()
                        .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cboProductsProductCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProductsAddProductCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cboProductsProductUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnProductsAddProductUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dlgProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProductsSave)
                    .addComponent(btnProductsCancel))
                .addContainerGap(52, Short.MAX_VALUE))
        );

        dlgProductsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnProductsAddProductCategory, btnProductsAddProductUnit});

        tblProducts.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        tblProducts.setModel(tbmProducts);
        tblProducts.setGridColor(new java.awt.Color(0, 0, 255));
        tblProducts.setRowHeight(20);
        tblProducts.setSelectionForeground(new java.awt.Color(255, 0, 0));
        tblProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducts);

        btnEditProduct.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        btnEditProduct.setMnemonic('E');
        btnEditProduct.setText("Edit Product");
        btnEditProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProductActionPerformed(evt);
            }
        });

        btnAddProduct.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        btnAddProduct.setMnemonic('A');
        btnAddProduct.setText("Add Product");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnDeleteProduct.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        btnDeleteProduct.setMnemonic('D');
        btnDeleteProduct.setText("Delete Product");

        jLabel1.setFont(new java.awt.Font("Arial Unicode MS", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Products");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(421, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAddProduct)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteProduct)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(374, 374, 374))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEditProduct)
                        .addComponent(btnDeleteProduct))
                    .addComponent(btnAddProduct))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblProductsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductsMouseClicked
        // Load product details on double click
        if(evt.getClickCount() == 2){
            launchEditProductdialog();
        }
    }//GEN-LAST:event_tblProductsMouseClicked

    private void btnProductsCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductsCancelActionPerformed
        // Dispose dialog
        dlgProducts.dispose();
    }//GEN-LAST:event_btnProductsCancelActionPerformed

    private void btnEditProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProductActionPerformed
        //Change dialog label text
        lblEditProductTitle.setText("Edit Product");
        // Edit selected product
        launchEditProductdialog();
    }//GEN-LAST:event_btnEditProductActionPerformed

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        //Change dialog label text
        lblEditProductTitle.setText("Add Product");
        // Add new product
        launchAddProductdialog();
    }//GEN-LAST:event_btnAddProductActionPerformed
private void launchEditProductdialog(){
    try{
    //  if(evt.getClickCount() == 2){
        int selectecRowInIdex = 0;
        if(tblProducts.getSelectedRow() != -1){
            selectecRowInIdex = tblProducts.getSelectedRow();
             helperProducts.loadProductDetailsOnDialog(tblProducts, dlgProducts, txtProductsProductName, cboProductsProductCategory, cboProductsProductUnit, selectecRowInIdex);
        }else{
            JOptionPane.showMessageDialog(null, "Please select a row to edit a product.\nTo select, click on the row.", "No product selected", JOptionPane.WARNING_MESSAGE);
        } 
   // }        
    }catch(Exception exception){}
}

private void launchAddProductdialog(){
    try{    
           //Clear product name
           txtProductsProductName.setText("");
           //Display dialog
           dlgProducts.setVisible(true);
           //Center dialog
           dlgProducts.setLocationRelativeTo(null);             
             
    }catch(Exception exception){}
}
    public void poulateProducts(JComboBox cbo) {
        ArrayList<Product> productsList = null;
        try {            
            //Remove all existing tanks in combo box
            cbo.removeAllItems();
           productsList = product.getFuelProducts();
           for(Product product : productsList){
               cbo.addItem(product.getProductName());
           }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnEditProduct;
    private javax.swing.JButton btnProductsAddProductCategory;
    private javax.swing.JButton btnProductsAddProductUnit;
    private javax.swing.JButton btnProductsCancel;
    private javax.swing.JButton btnProductsSave;
    private javax.swing.JComboBox cboProductsProductCategory;
    private javax.swing.JComboBox cboProductsProductUnit;
    private javax.swing.JDialog dlgProducts;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEditProductTitle;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField txtProductsProductName;
    // End of variables declaration//GEN-END:variables
}
