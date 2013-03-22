/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.helper;

import com.sun.jmx.snmp.Enumerated;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author chemweno
 */
public class HelperSales {
        //Prepare table
    public void prepareTable(DefaultTableModel tbm, JTable tbl, String... columnNames){
        try{
            //Remove all columns on table
            tbm.setColumnCount(0);
            //Set row count to 0
            tbm.setRowCount(0);
            for(String columnName : columnNames){
                //Insert columns
                tbm.addColumn(columnName);
            }       
        }catch(Exception exception){
        exception.printStackTrace();
        }
    }
        //Populate sales in combo box
       // tanks.poulateTanks(cboDipsTanks);    
    
    public void prepareTree(JTree tree){
        JCheckBox[] fuel = {new JCheckBox("Petrol"), new JCheckBox("Diesel"), new JCheckBox("Kerosene")};
        JCheckBox[] lubricants = {new JCheckBox("Oil"), new JCheckBox("Battery Water"), new JCheckBox("Coolant")};
        JCheckBox[] lpg = {new JCheckBox("6 Kg"), new JCheckBox("13 Kg")};
      //  DefaultMutableTreeNode trnFuel = new Defa
        try{
            //Remove all items on tree            
            tree.removeAll();
            //Add items
            tree.add(new JCheckBox("Fuel"));
            tree.add(new JCheckBox("Lubricants"));
            tree.add(new JCheckBox("Lpg"));
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
   //Check if node is in another node
    public boolean isNodeInTree(DefaultMutableTreeNode treeNode, DefaultMutableTreeNode childNode){
        boolean isNodeInTree = false;
        try{
            for(int index = 0; index < treeNode.getChildCount(); index++){
                if(treeNode.getChildAt(index).toString().equals(childNode.toString())){
                    isNodeInTree = true;
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return isNodeInTree;
    }
    
   //Get the index at which a child is located in the tree top node
    public int getNodeIndex(DefaultMutableTreeNode treeNode, DefaultMutableTreeNode childNode){
        int nodeIndex = 0;
        try{
            for(int index = 0; index < treeNode.getChildCount(); index++){
                if(treeNode.getChildAt(index).toString().equals(childNode.toString())){
                    nodeIndex = index;
                    break;
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return nodeIndex;
    }  
    
    //Traverse jtree recursively
    public ArrayList<String> getJTreeNodes(DefaultTreeModel trm, Object topNode){
    // public void getJTreeNodes(DefaultTreeModel trm, Object topNode){
        ArrayList<String> treeNodesList = new ArrayList<>();
        try{
            /*
            for(int i=0; i < topNode.getChildCount() ; i++){
               
              topNode = (DefaultMutableTreeNode) topNode.getChildAt(i);                
            if(topNode.isLeaf()){
                treeNodesList.add(topNode.toString());
            }else{
                getJTreeNodes(trm, topNode);
            }                
            }    
            * */
            int children = trm.getChildCount(topNode);
            for(int i = 0; i < children ; i++){
                Object child = trm.getChild(topNode, i);
               if(trm.isLeaf(child)){
                   treeNodesList.add(child.toString());
               }else{
                   getJTreeNodes(trm, child);
               }
            }                        
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return treeNodesList;
    }
    
    //Traverse jtree recursively
    public ArrayList<TreeNode> getSelectedProducts(TreeNode topNode, ArrayList<TreeNode> treeNodesList){
    // public void getJTreeNodes(DefaultTreeModel trm, Object topNode){        
        try{
            if(topNode.isLeaf()){                
                treeNodesList.add(topNode);
            }
            Enumeration children = topNode.children();
            if(children != null){
                while(children.hasMoreElements()){
                    getSelectedProducts((TreeNode)children.nextElement(), treeNodesList);
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return treeNodesList;
    }    
}
