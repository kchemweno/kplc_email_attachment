/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.core;

/**
 *
 * @author Chemweno
 */
public class Staff {
    
    private int staffId = 0;
    private String staffName = "";
    private String staffRole = "";

    public Staff() {
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }     
}
