/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.modular;

import java.sql.ResultSet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

/**
 *
 * @author chemweno
 */
public class ManageUser {

    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private Organisation organisation = new Organisation();
    private Language language = new Language();
    private JobFunction jobFunction = new JobFunction();
    private Role role = new Role();
    private ResultSet resultset = null;
    private String sqlString = "";
    private static LogManager logManager = new LogManager();

    //Load user roles
    public void loadUserRoles(javax.swing.JComboBox... cboUserRoles) {
        logManager.logInfo("Entering 'loadUserRoles(javax.swing.JComboBox... cboUserRoles)' method");
        String userRole = "";
        try {
            sqlString = queryBuilder.sqlSelectUserRoles();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    userRole = resultset.getString("role");
                    //Insert role into combo box
                    for(JComboBox cboUserRole : cboUserRoles){
                    cboUserRole.addItem(userRole);
                }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadUserRoles(javax.swing.JComboBox... cboUserRoles)' method");
        }
        logManager.logInfo("Exiting 'loadUserRoles(javax.swing.JComboBox... cboUserRoles)' method");
    }

    //Load languages
    public void loadLanguages(javax.swing.JComboBox... cboLanguages) {
        logManager.logInfo("Entering 'loadUserRoles(javax.swing.JComboBox... cboUserRoles)' method");
        String language = "";
        try {
            sqlString = queryBuilder.sqlSelectLanguages();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    language = resultset.getString("lang");
                    //Insert language into combo box
                    for(JComboBox cboLanguage : cboLanguages){
                    cboLanguage.addItem(language);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadUserRoles(javax.swing.JComboBox... cboUserRoles)' method");
        }
        logManager.logInfo("Exiting 'loadUserRoles(javax.swing.JComboBox... cboUserRoles)' method");
    }

    //Load languages
    public void loadOrganisations(javax.swing.JComboBox... cboOrganisations) {
        logManager.logInfo("Entering 'loadOrganisations(javax.swing.JComboBox... cboOrganisations)' method");
        String organisation = "";
        try {
            sqlString = queryBuilder.sqlSelectOrganisations();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    organisation = resultset.getString("org");
                    //Insert organisations into combo box
                    for(JComboBox cboOrganisation : cboOrganisations){
                    cboOrganisation.addItem(organisation);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadOrganisations(javax.swing.JComboBox... cboOrganisations)' method");
        }
        logManager.logInfo("Exiting 'loadOrganisations(javax.swing.JComboBox... cboOrganisations)' method");
    }

    //Load languages
    public void loadJobFunction(javax.swing.JComboBox... cboJobFunctions) {
        logManager.logInfo("Entering 'loadJobFunction(javax.swing.JComboBox... cboJobFunctions)' method");
        String jobFunction = "";
        try {
            sqlString = queryBuilder.sqlSelectJobFunction();
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    jobFunction = resultset.getString("job");
                    //Insert organisations into combo box
                    for(JComboBox cboJobFunction : cboJobFunctions){
                    cboJobFunction.addItem(jobFunction);
                    }
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadJobFunction(javax.swing.JComboBox... cboJobFunctions)' method");
        }
        logManager.logInfo("Exiting 'loadJobFunction(javax.swing.JComboBox... cboJobFunctions)' method");
    }

    //Load all users
    public void loadUsers(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers) {
        logManager.logInfo("Entering 'loadUsers(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers)' method");
        String firstName = "";
        String lastName = "";
        String middleName = "";
        String userRole = "";
        String organisation = "";
        String language = "";
        int userNumber = 0;
        int userIndex = 0;
        int userId = 0;
        boolean isActive = false;

        try {
            sqlString = queryBuilder.sqlSelectUsers();
            resultset = executeQuery.executeSelect(sqlString);
            //Add columns to model
            insertColumnsToModel(tblUsers, tbmUsers, "Number", "First Name", "Last Name", "Middle Name", "User Role",
                    "Organisation", "Language", "Active");
            //Set row count to 0
            tbmUsers.setRowCount(0);

            if (resultset.next()) {
                while (!resultset.isAfterLast()) {
                    firstName = resultset.getString("fname");//index 1
                    lastName = resultset.getString("lname");//index 2
                    middleName = resultset.getString("mname");//index 3
                    userRole = resultset.getString("rname");//index 4
                    organisation = resultset.getString("org");//index 5
                    language = resultset.getString("langname");//index 6
                    isActive = resultset.getInt("active") != 0 ? true : false; //index 7
                    userId = resultset.getInt("user_id");//index 0
                    //Insert row
                    tbmUsers.insertRow(userIndex, new Object[]{null});
                    //Populate rows
                    tblUsers.setValueAt(userId, userIndex, 0);
                    tblUsers.setValueAt(firstName, userIndex, 1);
                    tblUsers.setValueAt(lastName, userIndex, 2);
                    tblUsers.setValueAt(middleName, userIndex, 3);
                    tblUsers.setValueAt(userRole, userIndex, 4);
                    tblUsers.setValueAt(organisation, userIndex, 5);
                    tblUsers.setValueAt(language, userIndex, 6);
                    tblUsers.setValueAt(new JCheckBox("", isActive), userIndex, 7);
                    resultset.next();
                }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadUsers(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers)' method");
        }
        logManager.logInfo("Exiting 'loadUsers(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers)' method");
    }

    //Load all users
    public void loadUsers(int userId, JTextField txtFirstname, JTextField txtLastname, JTextField txtMiddlename, JTextField txtEmail,
            JRadioButton rbtFemale, JRadioButton rbtMale, JRadioButton rbtActive, JRadioButton rbtInactive,
            JComboBox cboUserRole, JComboBox cboLanguage, JComboBox cboJobFunction, JComboBox cboOrganisation) {
        logManager.logInfo("Entering 'loadUsers(int userId, JTextField txtFirstname, JTextField txtLastname, JTextField txtMiddlename, JTextField txtEmail,"
                + "  JRadioButton rbtFemale, JRadioButton rbtMale, JRadioButton rbtActive, JRadioButton rbtInactive,"
                + "  JComboBox cboUserRole, JComboBox cboLanguage, JComboBox cboJobFunction, JComboBox cboOrganisation)' method");
        
        String firstName = "";
        String lastName = "";
        String middleName = "";
        String gender = "";
        String email = "";
        String userRole = "";
        String organisation = "";
        String language = "";
        String jobFunction = "";
        //int userId = 0;
        boolean isActive = false;        

        try {
            sqlString = queryBuilder.sqlSelectUsers(userId);
            resultset = executeQuery.executeSelect(sqlString);

            if (resultset.next()) {
                    firstName = resultset.getString("fname");
                    lastName = resultset.getString("lname");
                    middleName = resultset.getString("mname");
                    email =  resultset.getString("email");
                    gender = resultset.getString("gender");
                    userRole = resultset.getString("rname");
                    organisation = resultset.getString("org");
                    language = resultset.getString("langname");
                    isActive = resultset.getInt("active") != 0 ? true : false; 
                    jobFunction = resultset.getString("job_func");
                    
                  //Load user details
                    txtFirstname.setText(firstName);
                    txtLastname.setText(lastName);
                    txtMiddlename.setText(middleName);
                    txtEmail.setText(email);
                    cboLanguage.setSelectedItem(language);
                    cboOrganisation.setSelectedItem(organisation);
                    cboJobFunction.setSelectedItem(jobFunction);
                    cboUserRole.setSelectedItem(userRole);
                    if(gender.equalsIgnoreCase("Female")){
                        rbtFemale.setSelected(true);
                    }else if(gender.equalsIgnoreCase("Male")){
                        rbtMale.setSelected(true);
                    }
                    if(isActive){
                        rbtActive.setSelected(true);
                    }else {
                        rbtInactive.setSelected(false);
                    }
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'loadUsers(int userId, JTextField txtFirstname, JTextField txtLastname, JTextField txtMiddlename, JTextField txtEmail,"
                + "  JRadioButton rbtFemale, JRadioButton rbtMale, JRadioButton rbtActive, JRadioButton rbtInactive,"
                + "  JComboBox cboUserRole, JComboBox cboLanguage, JComboBox cboJobFunction, JComboBox cboOrganisation)' method");
        }
        logManager.logInfo("Exiting 'loadUsers(int userId, JTextField txtFirstname, JTextField txtLastname, JTextField txtMiddlename, JTextField txtEmail,"
                + "  JRadioButton rbtFemale, JRadioButton rbtMale, JRadioButton rbtActive, JRadioButton rbtInactive,"
                + "  JComboBox cboUserRole, JComboBox cboLanguage, JComboBox cboJobFunction, JComboBox cboOrganisation)' method");
    }

    //Set default button
    public void setDefaultButton(javax.swing.JDialog dlgDialog, javax.swing.JButton btnDefault) {
        logManager.logInfo("Entering 'setDefaultButton(javax.swing.JDialog dlgDialog, javax.swing.JButton btnDefault)' method");
        try {
            dlgDialog.getRootPane().setDefaultButton(btnDefault);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'setDefaultButton(javax.swing.JDialog dlgDialog, javax.swing.JButton btnDefault)' method");
        }
        logManager.logInfo("Exiting 'setDefaultButton(javax.swing.JDialog dlgDialog, javax.swing.JButton btnDefault)' method");
    }

    //Save new organisation in the database
    public int saveNewOrganisation(String organisationName, String description) {
        logManager.logInfo("Entering 'saveNewOrganisation(String organisationName, String description)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertOrganisation(organisationName, description);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveNewOrganisation(String organisationName, String description)' method");
        }
        logManager.logInfo("Exiting 'saveNewOrganisation(String organisationName, String description)' method");
        return result;
    }

    //Save new organisation in the database
    public int saveNewJobFunction(String jobFunction, String description) {
        logManager.logInfo("Entering 'saveNewJobFunction(String jobFunction, String description)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertJobFunction(jobFunction, description);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveNewJobFunction(String jobFunction, String description)' method");
        }
        logManager.logInfo("Exiting 'saveNewJobFunction(String jobFunction, String description)' method");
        return result;
    }

    //Save user
    public int saveNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,
            String email, String organisationName, String roleName, String jobFunctionName, String languageName) {
        logManager.logInfo("Entering 'saveNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,"
                + " String email, String organisationName, String roleName, String jobFunctionName, String languageName)' method");
        int result = 0;
        int organisationId = 0;
        int roleId = 0;
        int jobFunctionId = 0;
        int languageId = 0;
        try {
            organisationId = organisation.getOrganisationId(organisationName);
            roleId = role.getRoleId(roleName);
            jobFunctionId = jobFunction.getJobFunctionId(jobFunctionName);
            languageId = language.getLanguageId(languageName);

            result = insertNewUser(firstName, lastName, middleName, userName, password, gender, email,
                    organisationId, roleId, jobFunctionId, languageId);

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'saveNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,"
                + " String email, String organisationName, String roleName, String jobFunctionName, String languageName)' method");
        }
        logManager.logInfo("Exiting 'saveNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,"
                + " String email, String organisationName, String roleName, String jobFunctionName, String languageName)' method");
        return result;
    }

    //Save new user
    private int insertNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,
            String email, int organisationId, int roleId, int jobFunctionId, int languageId) {
        logManager.logInfo("Entering 'insertNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,"
                + " String email, int organisationId, int roleId, int jobFunctionId, int languageId)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlInsertNewUser(firstName, lastName, middleName, userName, password, gender, email, organisationId,
                    roleId, jobFunctionId, languageId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                //Comit
                executeQuery.commit();
            } else {
                //Roll back
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'insertNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,"
                + " String email, int organisationId, int roleId, int jobFunctionId, int languageId)' method");
        }
        logManager.logInfo("Exiting 'insertNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,"
                + " String email, int organisationId, int roleId, int jobFunctionId, int languageId)' method");
        return result;
    }

    //Launch dialog
    public void launchDialog(javax.swing.JDialog dialog, int width, int height) {
        logManager.logInfo("Entering 'launchDialog(javax.swing.JDialog dialog, int width, int height)' method");
        try {
            dialog.setSize(width, height);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'launchDialog(javax.swing.JDialog dialog, int width, int height)' method");
        }
        logManager.logInfo("Exiting 'launchDialog(javax.swing.JDialog dialog, int width, int height)' method");
    }

    //Match passwords
    public boolean passwordsMatch(javax.swing.JPasswordField pwdPassword, javax.swing.JPasswordField pwdPasswordRetyped) {
        logManager.logInfo("Entering 'passwordsMatch(javax.swing.JPasswordField pwdPassword, javax.swing.JPasswordField pwdPasswordRetyped)' method");
        boolean passwordsMatch = false;
        try {
            passwordsMatch = pwdPassword.getText().equals(pwdPasswordRetyped.getText());
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'passwordsMatch(javax.swing.JPasswordField pwdPassword, javax.swing.JPasswordField pwdPasswordRetyped)' method");
        }
        logManager.logInfo("Exiting 'passwordsMatch(javax.swing.JPasswordField pwdPassword, javax.swing.JPasswordField pwdPasswordRetyped)' method");
        return passwordsMatch;
    }

    //Add columns to tablemodel
    public void insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames) {
        logManager.logInfo("Entering 'insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames)' method");
        try {
            //Remove all previous columns
            tbmUsers.setColumnCount(0);
            //Add columns to table model
            for (String columnName : columnNames) {
                tbmUsers.addColumn(columnName);
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames)' method");
        }
        logManager.logInfo("Exiting 'insertColumnsToModel(javax.swing.JTable tblUsers, javax.swing.table.DefaultTableModel tbmUsers, String... columnNames)' method");
    }

    //Reset password
    public int resetPassword(int userId, String newPassword) {
        logManager.logInfo("Entering 'resetPassword(int userId, String newPassword)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlUpdatePassword(newPassword, userId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                executeQuery.commit();
            } else {
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'resetPassword(int userId, String newPassword)' method");
        }
        logManager.logInfo("Exiting 'resetPassword(int userId, String newPassword)' method");
        return result;
    }

    //Get username
    public String getUsername(int userId) {
        logManager.logInfo("Entering 'getUsername(int userId)' method");
        String username = "";
        try {
            sqlString = queryBuilder.sqlSelectUsername(userId);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                username = resultset.getString("usr");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getUsername(int userId)' method");
        }
        logManager.logInfo("Exiting 'getUsername(int userId)' method");
        return username;
    }

    //Delete user
    public int deleteUser(int userId) {
        logManager.logInfo("Entering 'deleteUser(int userId)' method");
        int result = 0;
        try {
            sqlString = queryBuilder.sqlUpdateUserSetInactive(userId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                executeQuery.commit();
            } else {
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'deleteUser(int userId)' method");
        }
        logManager.logInfo("Exiting 'deleteUser(int userId)' method");
        return result;
    }

    //Get userid
    public int getUserId(javax.swing.JTable tbl) {
        logManager.logInfo("Entering 'getUserId(javax.swing.JTable tbl)' method");
        int userId = 0;
        try {
            userId = Integer.parseInt(tbl.getValueAt(tbl.getSelectedRow(), 0).toString());
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getUserId(javax.swing.JTable tbl)' method");
        }
        logManager.logInfo("Exiting 'getUserId(javax.swing.JTable tbl)' method");
        return userId;
    }

    //Get userid
    public int getUserId(String username) {
        logManager.logInfo("Entering 'getUserId(String username)' method");
        int userId = 0;
        try {
            sqlString = queryBuilder.sqlSelectUserId(username);
            resultset = executeQuery.executeSelect(sqlString);
            if (resultset.next()) {
                userId = resultset.getInt("user_id");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getUserId(String username)' method");
        }
        logManager.logInfo("Exiting 'getUserId(String username)' method");
        return userId;
    }

    //Change password
    public int changePassword(String newPassword, int userId) {
        logManager.logInfo("Entering 'changePassword(String newPassword, int userId)' method");
        int result = 0;

        try {
            sqlString = queryBuilder.sqlUpdatePassword(newPassword, userId);
            result = executeQuery.executeInsert(sqlString);
            if (result > 0) {
                executeQuery.commit();
            } else {
                executeQuery.rollBack();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'changePassword(String newPassword, int userId)' method");
        }
        logManager.logInfo("Exiting 'changePassword(String newPassword, int userId)' method");
        return result;
    }
}
