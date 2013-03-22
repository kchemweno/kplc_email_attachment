/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.core;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JComboBox;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;

/**
 *
 * @author chemweno
 */
public class Tanks {
    
    private int tankId = 0;
    private String tankName = "";
    private ResultSet resultset = null;
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private QueryBuilder queryBuilder = new QueryBuilder();
    String sqlString = "";

    public String getTankIds() {
        String tankIds = "";
        try{
            for(Tanks tank : getTanks()){
             tankIds =  tankIds + "," + tank.getTankId();
           }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        tankIds = tankIds.replaceFirst(",", "");
       // System.out.println("tank ids : " + tankIds);
        return tankIds;
    }

    
    //Get a list of tanks with names and ids
    public ArrayList<Tanks> getTanks() {
        ArrayList<Tanks> tanksList = new ArrayList<>();
        String sqlString = "";
        try {
            sqlString = queryBuilder.getAllTanks();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    Tanks tank = new Tanks();
                    tank.setTankId(resultset.getInt("id"));
                    tank.setTankName(resultset.getString("tank"));
                    tanksList.add(tank);
                    resultset.next();
                }
            }
        } catch (Exception exception) {
        }
        return tanksList;
    }    
    
    public void poulateTanks(JComboBox cboTanks) {
        ArrayList<Tanks> tanksList = null;
        try {
            //Remove all existing tanks in combo box
            cboTanks.removeAllItems();
           tanksList = getTanks();
           for(Tanks tank : tanksList){
               cboTanks.addItem(tank.getTankName());
           }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int getTankId() {
        return tankId;
    }

    public void setTankId(int tankId) {
        this.tankId = tankId;
    }

    public String getTankName() {
        return tankName;
    }

    public void setTankName(String tankName) {
        this.tankName = tankName;
    }     
}
