/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.data;

import org.orpik.logging.LogManager;
import org.orpik.settings.global.Properties;

/**
 *
 * @author Chemweno
 */
public class QueryBuilder {

    private Properties properties;
    private String databasePrefix = "";
    private String databaseServer = "";
    private String sqlQuery = "";
    private String databaseName = "nishati";
    private String systemNumber = "";
    private static LogManager logManager = new LogManager();

    public QueryBuilder() {
        logManager.logInfo("Entering 'QueryBuilder()' method");
        logManager.logInfo("Initializing properties in 'QueryBuilder()' method");
        //Initialize properties object
        properties = new Properties();
        logManager.logInfo("Getting database server in 'QueryBuilder()' method");
        //Get database prefix
        databasePrefix = getDatabaseServer();
        if (databaseServer.startsWith("mysq")) {//mysq for mysql
            //MySQL prefix will be empty string since it does not have a schema like postgresql
            databasePrefix = "";
        } else if (databaseServer.startsWith("postg")) {//postg for postgres
            //Postgresql prefix will be a non empty string since it utilizes a schema
            databasePrefix = "schema_market_data.";
        }
        logManager.logInfo("Exiting 'QueryBuilder()' method");
    }
    
    private String getDatabaseServer() {
        logManager.logInfo("Entering 'getDatabaseServer()' method");
        String dbSever = "";
        try {
            dbSever = properties.getDbServer();
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getDatabaseServer()' method");
        }
        logManager.logInfo("Exiting 'getDatabaseServer()' method");
        return dbSever;
    }   
    
    //Get settings
    public String getSettings(){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT message_body,time_before_appointment FROM "+databaseName+".settings";
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }
    
    //Update settings
    public String updateSettings(int time, String message){
        sqlQuery = "";
        try{
            sqlQuery = "UPDATE "+databaseName+".settings SET time_before_appointment="+time+" , message_body='"+message+"'";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }    
    
    //Insert new customer prepared statement
    public String insertCustomerPreparedStatement(){
        sqlQuery = "";
        try{
            sqlQuery = "INSERT INTO "+databaseName+".customers(first_name,last_name,date_of_birth,biometric) VALUES "
                    + "(?,?,?,?)";
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    } 
    
    
    //Get all messages
    public String getAllMessages(String fromDate, String toDate){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT id,message,sender,recipient,time_stamp FROM "+databaseName+".messages WHERE time_stamp BETWEEN '"+fromDate+"' AND '"+toDate+"'";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }    
    
  
    //Get all messages
    public String getAllUsers(){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT users.id,first_name,last_name,middle_name,username,role_name FROM "+databaseName+".users INNER JOIN "+databaseName+".roles ON users.role_id=roles.id  WHERE active_status='1'";
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }   
    
    //Get user with username
    public String getUsersWithUsername(String username){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT * FROM "+databaseName+".users WHERE username='"+username+"'";
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
    
    //Get user with username and password
    public String getUserLogin(String username, String password){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT count(*) as number FROM "+databaseName+".users WHERE username='"+username+"' AND passwrd=SHA('"+password+"')";
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }      
    
    //Add new user or 
    public String insertUser(int roleId,String username, String firstName, String lastName, String middleName, char[] password){
        sqlQuery = "";
        try{
            sqlQuery = "INSERT INTO "+databaseName+".users(username,passwrd,first_name,last_name,middle_name,role_id) VALUES"
                    + " ('"+username+"',SHA1('"+password+"'),'"+firstName+"','"+lastName+"','"+middleName+"',"+roleId+")"
                    + " ON DUPLICATE KEY UPDATE username='"+username+"',passwrd='"+password+"',first_name='"+firstName+"',last_name='"+lastName+"',middle_name='"+middleName+"',role_id="+roleId;
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    } 
    
    //Edit existing user
    public String updateUser(int roleId,String username, String firstName, String lastName, String middleName){
        sqlQuery = "";
        try{
            sqlQuery = "INSERT INTO "+databaseName+".users(username,first_name,last_name,middle_name,role_id) VALUES"
                    + " ('"+username+"','"+firstName+"','"+lastName+"','"+middleName+"',"+roleId+")"
                    + " ON DUPLICATE KEY UPDATE username='"+username+"',first_name='"+firstName+"',last_name='"+lastName+"',middle_name='"+middleName+"',role_id="+roleId;
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
    
//Deactivate user
    public String deactivateUser(int userId){
        sqlQuery = "";
        try{
            sqlQuery = "UPDATE "+databaseName+".users SET active_status='0' WHERE users.id="+userId;
            //System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }    
     
    //Change password
    public String changePassword(int userId, String password){
        int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "UPDATE "+databaseName+".users SET passwrd=SHA1('"+password+"') WHERE id="+userId;
        }catch(Exception exception){}
        return sqlQuery;
    }
    
    //User exists
    public String userExists(int userId, String password){
       // int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "SELECT COUNT(*) AS users FROM "+databaseName+".users WHERE id="+userId+" AND passwrd=sha1('"+password+"')";
        }catch(Exception exception){}
        return sqlQuery;
    }    
    
    //Get user id
    public String getUserId(String username){
        int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "SELECT id FROM "+databaseName+".users WHERE username='"+username+"'";            
        }catch(Exception exception){}
        return sqlQuery;
    }     
        
    //Orpik Energy
    //Select all dips
    public String getAllDips(String productIds, String tankIds, String from, String to){
       // int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "SELECT dips.product_id AS prod,DATE AS today,opening_dip,closing_dip, product_in,"
                    + " ((opening_dip - closing_dip)+(SELECT product_in FROM "+databaseName+".stock WHERE date_taken=today AND product_id=prod)) AS dip_sale,tanks.tank,"
                    + " products.name"
                    + " FROM "+databaseName+".dips "
                    + " INNER JOIN "+databaseName+".tanks ON dips.tank_id=tanks.id "
                    + " INNER JOIN "+databaseName+".products ON dips.product_id=products.id"
                    + " INNER JOIN "+databaseName+".stock ON (dips.tank_id=tanks.id AND dips.date=stock.date_taken AND stock.product_id=dips.product_id)"
                    + " WHERE dips.product_id IN("+productIds+") AND dips.tank_id IN ("+tankIds+")"
                    + "  AND DATE BETWEEN '"+from+"' AND '"+to+"'";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
    
    //Select all tanks
    public String getAllTanks(){
       // int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "SELECT id,tank FROM "+databaseName+".tanks ";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }          
    
    //Select all products
    public String getAllProducts(){
       // int result = 0;
        sqlQuery = "";
        try{
          //  sqlQuery = "SELECT id,name,product_unit_id FROM "+databaseName+".products";
            sqlQuery = "SELECT products.id, products.product_unit_id,products.name,product_categories.category_name,product_unit.name AS unit_name FROM "+databaseName+".products "
                    + " INNER JOIN "+databaseName+".product_categories ON products.product_category_id=product_categories.id "
                    + " INNER JOIN "+databaseName+".product_unit ON products.product_unit_id=product_unit.id ORDER BY category_name, products.name";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
    
     //Select all products and categories
    public String getAllProductCategories(){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT name,category_name FROM "+databaseName+".products INNER JOIN "+databaseName+".product_categories "
                    + " ON products.product_category_id = product_categories.id "
                    + " ORDER BY category_name,NAME desc";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }    
    
     //Select all sales
    public String getSales(String dateFrom, String dateTo, String products){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT staff.firstname,staff.lastname,products.name,quantity,ROUND((amount_cash+amount_cheque+amount_credit+amount_debit_card),2) AS total, "
                    + " date_and_time FROM "+databaseName+".sales INNER JOIN "+databaseName+".products ON sales.product_id=products.id INNER JOIN "+databaseName+".staff ON sales.staff_id=staff.id "
                    + " WHERE date_and_time BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND products.name IN("+products+") "
                    + " ORDER BY date_and_time,name";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }   
    
    //Select all products
    public String getProducts(){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT products.name AS product_name,product_categories.category_name,product_unit.name AS unit_name FROM "+databaseName+".products "
                    + " INNER JOIN "+databaseName+".product_categories ON products.product_category_id=product_categories.id "
                    + " INNER JOIN "+databaseName+".product_unit ON products.product_unit_id=product_unit.id ORDER BY category_name, products.name";                   
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }
 //Select fuel products
    //Select all products
    public String getFuelProducts(){
        sqlQuery = "";
        try{
            sqlQuery = "SELECT products.name FROM "+databaseName+".products WHERE product_category_id=1 ORDER BY products.name ASC";                   
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
        
    //Select stock
    public String getStock(){
       // int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "SELECT * FROM (SELECT products.id AS product_id,products.name AS product_name,product_unit.name AS unit_name,"
                    + " re_order_level.re_order_level,(stock.product_in - stock.product_out) AS current_stock_level,stock.date_taken "
                    + " FROM "+databaseName+".products INNER JOIN "+databaseName+".re_order_level ON products.id=re_order_level.product_id "
                    + " INNER JOIN "+databaseName+".stock ON products.id=stock.product_id "
                    + " INNER JOIN "+databaseName+".product_unit ON products.product_unit_id=product_unit.id) AS stocks "
                    + " WHERE "+databaseName+".stocks.date_taken=(SELECT MAX(date_taken) FROM "+databaseName+".stock WHERE product_id=stocks.product_id)";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
    //Get product categories
    public String getProductCategories(){
       // int result = 0;
        sqlQuery = "";
        try{
            sqlQuery = "select category_name from "+databaseName+".product_categories";
           // System.out.println(sqlQuery);
        }catch(Exception exception){}
        return sqlQuery;
    }     
    
}
