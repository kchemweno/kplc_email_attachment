/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.helper;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author chemweno
 */
public class HelperMainFrame {
        public void exitWindow() {
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            //yes
            System.exit(0);
        }
    }
        
//Add panel to tabbed pane
   public void addPanelToPaneTabbed(String title, String icon, JTabbedPane tabbedPane, JPanel panel){
       Component[] components;
       try{           
           //Check if panel already exists in tabbed pane
           if(!checkIfCobtainerContainsComponent(tabbedPane, panel)){               
                //Add panel to pane
               tabbedPane.addTab(title,new javax.swing.ImageIcon(getClass().getResource("/images/"+icon)) ,panel);
               //Revalidate
               panel.revalidate();
               //Repaint
               panel.repaint();
               //Select panel
               tabbedPane.setSelectedComponent(panel);
            }   
       }catch(Exception exception){
           exception.printStackTrace();
       }
   }
   
   private boolean checkIfCobtainerContainsComponent(JTabbedPane tabbedPane, JPanel panel){
       boolean isInPane = true;
       try{
           if(panel.isShowing()){
               isInPane = true;
           }else{
               isInPane = false;
           }
       }catch(Exception exception){
            exception.printStackTrace();
       }
       return isInPane;
   }  
}
