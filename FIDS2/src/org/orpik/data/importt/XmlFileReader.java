/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.data.importt;

import java.io.File;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.orpik.data.QueryBuilder;
import org.orpik.data.Validation;
import org.orpik.indicators.Indicators;
import org.orpik.location.Market;
import org.orpik.logging.LogManager;
import org.orpik.period.Month;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Chemweno
 */
public class XmlFileReader {

    private Market market = new Market();
    private Month month = new Month();
    private Validation validation = new Validation();
    private InsertXmlData insertXmlData = new InsertXmlData();
    private QueryBuilder queryBuilder = new QueryBuilder();
    private Indicators indicators = new Indicators();
    private boolean xmlFileReadSucceeded = false;
    private int year = 0;
    private int monthId = 0;
    private int marketId = 0;
    private int applicationId = 0;
    private int fileReadResult = 0;
    private String sqlInsertSuffix = "";
    private static String sqlInsertSlimsOneDetailsSuffix = "";
    private static String sqlInsertSlimsOneLookupSuffix = "";
    private static String sqlInsertSlimsTwoDetailsSuffix = "";
    private static String sqlInsertSlimsTwoCommentsSuffix = "";
    private String sqlInsertPrefix = "";
    private String marketName = "";
    private String yearString = "";
    private String monthName = "";
    private String application = "";
    private File file = null;
    private DocumentBuilderFactory dbf = null;
    private DocumentBuilder db = null;
    private Document doc = null;
    private static LogManager logManager = new LogManager();

    public int readXmlFile(String xmlFilename) {
        logManager.logInfo("Entering 'readXmlFile(String xmlFilename)' method");
        file = null;
        application = "";
        applicationId = 0;
        fileReadResult = 0;
        dbf = null;
        db = null;
        doc = null;
        try {
            //Change fielname by appending extra backslash so that it can be read correctly
            xmlFilename = xmlFilename.replace("\\", "\\\\");
            //Get filename
            file = new File(xmlFilename);
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            application = doc.getDocumentElement().getAttribute("Application").toString();
            //Get application id to determine whether data exists already
            applicationId = !application.equals("") ? Integer.parseInt(application) : 0;
            if (applicationId == 1) {
                readXmlFileMarket(xmlFilename);
            } else if (applicationId == 2) {
                readXmlFileSlimsOne(xmlFilename);
            } else if (applicationId == 4) {
                readXmlFileSlimsTwo(xmlFilename);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'readXmlFile(String xmlFilename)' method");
        }
        logManager.logInfo("Exiting 'readXmlFile(String xmlFilename)' method");
        return fileReadResult;
    }

    private int readXmlFileMarket(String xmlFilename) {
        logManager.logInfo("Entering 'readXmlFileMarket(String xmlFilename)' method");
        //Reset variables
        year = 0;
        monthId = 0;
        marketId = 0;
        applicationId = 0;
        fileReadResult = 0;
        file = null;
        dbf = null;
        db = null;
        doc = null;
        try {
            //Change fielname by appending extra backslash so that it can be read correctly
            xmlFilename = xmlFilename.replace("\\", "\\\\");
            //Get filename
            file = new File(xmlFilename);

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //Extract XML file variables
            marketName = doc.getDocumentElement().getAttribute("Name").toString();
            //Get market id based on market name
            marketId = market.getMarketId(marketName);
            //Get year name
            yearString = doc.getDocumentElement().getAttribute("Year").toString();
            //Convert year to int from string
            year = !yearString.equalsIgnoreCase("") ? Integer.parseInt(yearString) : 0;
            //Get month name
            monthName = doc.getDocumentElement().getAttribute("Month").toString();
            //Get month integer
            monthId = month.getMonthId(monthName);
            //Get application
            application = doc.getDocumentElement().getAttribute("Application").toString();
            //Get application id to determine whether data exists already
            applicationId = !application.equals("") ? Integer.parseInt(application) : 0;

            //Confirm that file was properly made, it should have application and year should not be 0
            if (!(applicationId == 0 || year == 0)) {
                //Check whether data already exists
                if (!validation.dataExists(year, monthId, marketId, applicationId)) {
                    //Get insert market data prefix string
                    sqlInsertPrefix = queryBuilder.queryInsertMarketdata();
                    //Proceed to import data to database
                    fileReadResult = insertXmlData.saveData(sqlInsertPrefix, processDocument(doc, marketId, monthId, year));
                    //Commit database transaction
                    insertXmlData.commitTransaction();
                } else {
                    //Market prices already exist
                    fileReadResult = -1;
                    insertXmlData.rollBackTransaction();
                }
            } else {
                //application id or year is 0
                fileReadResult = 0;
                insertXmlData.rollBackTransaction();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'readXmlFileMarket(String xmlFilename)' method");
        }
        logManager.logInfo("Exiting 'readXmlFileMarket(String xmlFilename)' method");
        return fileReadResult;
    }

    private int readXmlFileSlimsOne(String xmlFilename) {
        logManager.logInfo("Entering 'readXmlFileSlimsOne(String xmlFilename)' method");
        //Reset variables
        year = 0;
        monthId = 0;
        marketId = 0;
        applicationId = 0;
        sqlInsertSlimsOneLookupSuffix = "";
        sqlInsertSlimsOneDetailsSuffix = "";
        fileReadResult = 0;
        file = null;
        dbf = null;
        db = null;
        doc = null;
        //Look-up variables
        String agriculturalActivity = "";
        String nonAgriculturalActivity = "";
        String transportMeans = "";
        String transportCommodity = "";
        String transportOrigin = "";
        String transportDestination = "";
        String comments = "";
        try {
            //Change fielname by appending extra backslash so that it can be read correctly
            xmlFilename = xmlFilename.replace("\\", "\\\\");
            //Get filename
            file = new File(xmlFilename);

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //Extract XML file variables
            marketName = doc.getDocumentElement().getAttribute("Name").toString();
            //Get market id based on market name
            marketId = market.getMarketId(marketName);
            //Get year name
            yearString = doc.getDocumentElement().getAttribute("Year").toString();
            //Convert year to int from string
            year = !yearString.equalsIgnoreCase("") ? Integer.parseInt(yearString) : 0;
            //Get month name
            monthName = doc.getDocumentElement().getAttribute("Month").toString();
            //Get month integer
            monthId = month.getMonthId(monthName);
            //Get application
            application = doc.getDocumentElement().getAttribute("Application").toString();
            //Get application id to determine whether data exists already
            applicationId = !application.equals("") ? Integer.parseInt(application) : 0;
            //Get slims part one look-up variables from xml file
            agriculturalActivity = doc.getDocumentElement().getAttribute("Agricultural-Activity").toString();
            nonAgriculturalActivity = doc.getDocumentElement().getAttribute("NonAgricultural-Activity").toString();
            transportMeans = doc.getDocumentElement().getAttribute("Transport-Means").toString();
            transportCommodity = doc.getDocumentElement().getAttribute("Transport-Commodity").toString();
            transportOrigin = doc.getDocumentElement().getAttribute("Transport-Origin").toString();
            transportDestination = doc.getDocumentElement().getAttribute("Transport-Destination").toString();
            comments = doc.getDocumentElement().getAttribute("comments").toString();

            sqlInsertSlimsOneLookupSuffix = "(" + year + "," + monthId + "," + marketId + ",'" + agriculturalActivity
                    + "','" + nonAgriculturalActivity + "','" + transportMeans + "','" + transportCommodity + "','"
                    + transportOrigin + "', '" + transportDestination + "','" + comments + "')";

            //Confirm that file was properly made, it should have application and year should not be 0
            if (!(applicationId == 0 || year == 0)) {
                //Check whether data already exists
                if (!validation.dataExists(year, monthId, marketId, applicationId)) {
                    //Get insert market data prefix string
                    sqlInsertPrefix = queryBuilder.queryInsertMarketdata();
                    //Proceed to import data to database
                    fileReadResult = insertXmlData.saveData(sqlInsertPrefix, processDocumentSlimsPartOne(doc, marketId, monthId, year));
                    if (fileReadResult > 0) {
                        //Insert Slims Part One details if prices are successfuly saved
                        sqlInsertPrefix = queryBuilder.queryInsertSlimOneDetails();
                        fileReadResult = insertXmlData.saveData(sqlInsertPrefix, sqlInsertSlimsOneDetailsSuffix);
                        if (fileReadResult > 0) {
                            //Insert Slims Part One look-up if details are successfuly saved
                            sqlInsertPrefix = queryBuilder.queryInsertSlimOneLookUp();
                            fileReadResult = insertXmlData.saveData(sqlInsertPrefix, sqlInsertSlimsOneLookupSuffix);
                            if (fileReadResult > 0) {
                                //Commit database transaction    
                                insertXmlData.commitTransaction();
                            } else {
                                //Error saving look-up data
                                fileReadResult = -4;
                                insertXmlData.rollBackTransaction();
                                System.err.println("Data for "+marketName+", "+yearString+","+monthName+" already exists");
                                JOptionPane.showMessageDialog(null, "Data for "+marketName+", "+yearString+","+monthName+" already exists", "Data Exists", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            //Error saving slims one details
                            fileReadResult = -3;
                            insertXmlData.rollBackTransaction();
                        }
                    } else {
                        //Error saving slims one prices
                        fileReadResult = -2;
                        insertXmlData.rollBackTransaction();
                    }
                } else {
                    //Data already exists
                    fileReadResult = -1;
                    insertXmlData.rollBackTransaction();
                }
            } else {
                //Application id or year value is invalid
                fileReadResult = 0;
                insertXmlData.rollBackTransaction();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'readXmlFileSlimsOne(String xmlFilename)' method");
        }
        logManager.logInfo("Exiting 'readXmlFileSlimsOne(String xmlFilename)' method");
        return fileReadResult;
    }

    private int readXmlFileSlimsTwo(String xmlFilename) {
        logManager.logInfo("Entering 'readXmlFileSlimsTwo(String xmlFilename)' method");
        //Reset variables
        year = 0;
        monthId = 0;
        marketId = 0;
        applicationId = 0;
        sqlInsertSlimsOneLookupSuffix = "";
        sqlInsertSlimsOneDetailsSuffix = "";
        fileReadResult = 0;
        file = null;
        dbf = null;
        db = null;
        doc = null;
        //Look-up variables
        String comments = "";
        try {
            //Change fielname by appending extra backslash so that it can be read correctly
            xmlFilename = xmlFilename.replace("\\", "\\\\");
            //Get filename
            file = new File(xmlFilename);

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //Extract XML file variables
            marketName = doc.getDocumentElement().getAttribute("Name").toString();
            //Get market id based on market name
            marketId = market.getMarketId(marketName);
            //Get year name
            yearString = doc.getDocumentElement().getAttribute("Year").toString();
            //Convert year to int from string
            year = !yearString.equalsIgnoreCase("") ? Integer.parseInt(yearString) : 0;
            //Get month name
            monthName = doc.getDocumentElement().getAttribute("Month").toString();
            //Get month integer
            monthId = month.getMonthId(monthName);
            //Get application
            application = doc.getDocumentElement().getAttribute("Application").toString();
            //Get application id to determine whether data exists already
            applicationId = !application.equals("") ? Integer.parseInt(application) : 0;
            //Get slims part one look-up variables from xml file
            comments = doc.getDocumentElement().getAttribute("comments").toString();

            sqlInsertSlimsTwoCommentsSuffix = " (" + year + "," + monthId + "," + marketId + ",'" + comments + "')";

            //Confirm that file was properly made, it should have application and year should not be 0
            if (!(applicationId == 0 || year == 0)) {
                //Check whether data already exists
                if (!validation.dataExists(year, monthId, marketId, applicationId)) {
                    //Get insert slims 2 data prefix string
                    sqlInsertPrefix = queryBuilder.queryInsertMarketdata();
                    //Proceed to import data to database
                    fileReadResult = insertXmlData.saveData(sqlInsertPrefix, processSlimsPartTwoDocument(doc, marketId, monthId, year));
                    if (fileReadResult > 0) {
                        //Insert Slims Part 2 details if month datais successfuly saved
                        sqlInsertPrefix = queryBuilder.queryInsertSlims2DetailsPrefix();
                        fileReadResult = insertXmlData.saveData(sqlInsertPrefix, sqlInsertSlimsTwoDetailsSuffix);
                        if (fileReadResult > 0) {
                            //Insert Slims Part two comments if details are successfuly saved
                            sqlInsertPrefix = queryBuilder.queryInsertSlims2CommentsPrefix();
                            fileReadResult = insertXmlData.saveData(sqlInsertPrefix, sqlInsertSlimsTwoCommentsSuffix);
                            if (fileReadResult > 0) {
                                //Commit database transaction          
                                insertXmlData.commitTransaction();
                            } else {
                                //Error saving slims two comments
                                fileReadResult = -4;
                                insertXmlData.rollBackTransaction();
                            }
                        } else {
                            //Error saving slims two details
                            fileReadResult = -3;
                            insertXmlData.rollBackTransaction();
                        }
                    } else {
                        //Error saving slims two month values
                        fileReadResult = -2;
                        insertXmlData.rollBackTransaction();
                    }
                } else {
                    //Data already exists
                    fileReadResult = -1;
                }
            } else {
                //Application id or year value is invalid
                fileReadResult = 0;
                insertXmlData.rollBackTransaction();
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'readXmlFileSlimsTwo(String xmlFilename)' method");
        }
        logManager.logInfo("Exiting 'readXmlFileSlimsTwo(String xmlFilename)' method");
        return fileReadResult;
    }

    private String processDocument(Document doc, int marketId, int monthId, int year) {
        logManager.logInfo("Entering 'processDocument(Document doc, int marketId, int monthId, int year)' method");
        NodeList indicatorList = null;
        NodeList indicatorDetailList = null;
        NodeList weekDetailList = null;
        NodeList supplyDetailList = null;
        Node indicatorNode = null;

        //Element indicatorElement = null;
        Element weekElement = null;
        Element supplyElement = null;

        String indicator = "";
        int weekValue = -1;
        int week = 0;
        int indicatorId = 0;
        int supplyId = 0;

        try {
            sqlInsertSuffix = "";
            indicatorList = doc.getElementsByTagName("Commodity");
            //Traverse commodity names in xml file
            for (int indicatorIndex = 0; indicatorIndex < indicatorList.getLength(); indicatorIndex++) {
                // indicator = indicatorList.item(indicatorIndex).getAttributes().item(0).getNodeValue();
                indicatorNode = indicatorList.item(indicatorIndex);
                indicator = indicatorNode.getAttributes().item(0).getNodeValue();
                // indicatorList.item(indicatorIndex).
                indicatorId = indicators.getIndicatorId(indicator);
                //Get all the elements in this node
                // indicatorNode = indicatorList.item(indicatorIndex);

                //Check whether the node is a leaf or a branch
                if (indicatorNode.getNodeType() == Node.ELEMENT_NODE) {
                    //indicatorElement = (Element) indicatorNode;
                    Element indicatorElement = (Element) indicatorNode;
                    //Get Week 1 values
                    for (int weekIndex = 1; weekIndex <= 5; weekIndex++) {
                        indicatorDetailList = indicatorElement.getElementsByTagName("Week" + weekIndex);
                        weekElement = (Element) indicatorDetailList.item(0);
                        weekDetailList = weekElement.getChildNodes();
                        //Get week value, value of indicator for the week
                        weekValue = Integer.parseInt((weekDetailList.item(0)).getNodeValue().replaceAll(",", ""));
                        //Confirm whether price is available
                        if (weekValue > 0) {
                            //Get supply id value
                            indicatorDetailList = indicatorElement.getElementsByTagName("Supply");
                            supplyElement = (Element) indicatorDetailList.item(0);
                            supplyDetailList = supplyElement.getChildNodes();
                            supplyId = Integer.parseInt(supplyDetailList.item(0).getNodeValue().toString().substring(1, 2)) + 1;
                            //Update sql insert statement                            
                            sqlInsertSuffix = sqlInsertSuffix + ",(" + year + "," + monthId + "," + weekIndex + ","
                                    + "" + marketId + "," + indicatorId + "," + supplyId + "," + weekValue + ")";
                        }
                    }
                }
            }
            sqlInsertSuffix = sqlInsertSuffix.replaceFirst(",", "");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'processDocument(Document doc, int marketId, int monthId, int year)' method");
            //exception.printStackTrace();
        }
        logManager.logInfo("Exiting 'processDocument(Document doc, int marketId, int monthId, int year)' method");
        return sqlInsertSuffix;
    }

    private String processDocumentSlimsPartOne(Document doc, int marketId, int monthId, int year) {
        logManager.logInfo("Entering 'processDocumentSlimsPartOne(Document doc, int marketId, int monthId, int year)' method");
        NodeList categoryList = null;
        NodeList categoryDetailList = null;
        NodeList indicatorList = null;
        NodeList indicatorDetailList = null;
        NodeList weekDetailList = null;
        Element indicatorElement = null;
        Element categoryElement = null;
        Element weekElement = null;
        String indicator = "";
        Node categoryNode = null;
        String categoryName = "";
        String categoryId = "";
        String locationName = "";
        String dataTrustLevel = "";
        String keyInformant = "";
        String triangulation = "";
        int weekValue = -1;
        int week = 0;
        int indicatorId = 0;
        int supplyId = 1;//supplyId is set to 1 to allow insertion with 1 where supply id is not available e.g slims one data
        boolean hasChildren = false;

        try {
            sqlInsertSuffix = "";
            //Get category list
            categoryList = doc.getElementsByTagName("Category");

            for (int categoryIndex = 0; categoryIndex < categoryList.getLength(); categoryIndex++) {       
                categoryName = doc.getElementsByTagName("Category").item(categoryIndex).getAttributes().getNamedItem("Name").getNodeValue();
                categoryId = doc.getElementsByTagName("Category").item(categoryIndex).getAttributes().getNamedItem("CategoryId").getNodeValue();
                locationName = doc.getElementsByTagName("Category").item(categoryIndex).getAttributes().getNamedItem("Location-Name").getNodeValue();            
                dataTrustLevel = doc.getElementsByTagName("Category").item(categoryIndex).getAttributes().getNamedItem("Data-Trust-Level").getNodeValue();
                keyInformant = doc.getElementsByTagName("Category").item(categoryIndex).getAttributes().getNamedItem("Key-Informant").getNodeValue();
                triangulation = doc.getElementsByTagName("Category").item(categoryIndex).getAttributes().getNamedItem("Triangulation").getNodeValue();
                //Get the commodity name               
                /*categoryName = categoryList.item(categoryIndex).getAttributes().getNamedItem("Name").getNodeValue();                                  
                categoryId = categoryList.item(categoryIndex).getAttributes().getNamedItem("CategoryId").getNodeValue();
                locationName = categoryList.item(categoryIndex).getAttributes().getNamedItem("Location-Name").getNodeValue().toString();
                dataTrustLevel = categoryList.item(categoryIndex).getAttributes().getNamedItem("Data-Trust-Level").getNodeValue();
                keyInformant = categoryList.item(categoryIndex).getAttributes().getNamedItem("Key-Informant").getNodeValue();
                triangulation = categoryList.item(categoryIndex).getAttributes().getNamedItem("Triangulation").getNodeValue();*/

                sqlInsertSlimsOneDetailsSuffix = sqlInsertSlimsOneDetailsSuffix + ","
                        + "(" + year + "," + monthId + "," + marketId + "," + categoryId + ",'"
                        + locationName + "','" + keyInformant + "','" + triangulation + "','" + dataTrustLevel + "')";

                categoryNode = categoryList.item(categoryIndex);

                hasChildren = categoryNode.hasChildNodes();
                if (hasChildren) {
                    //Traverse category children if the categpry is not an orphan               
                    indicatorList = categoryNode.getChildNodes();
                    //Traverse commodity names in xml file
                    for (int indicatorIndex = 0; indicatorIndex < indicatorList.getLength(); indicatorIndex++) {
                        //Get all the elements in this node
                        Node indicatorNode = indicatorList.item(indicatorIndex);
                        //Test
                        if(indicatorNode == null){
                           // System.err.println("Node is null");
                        }else{
                           // System.out.println("Node is not null");
                            indicatorNode.getNodeType();
                        }
                      //  indicatorNode.getAttributes();
                       // indicatorNode.getAttributes().getLength();
                        //End of test
                        //indicator = indicatorList.item(indicatorIndex).getAttributes().item(0).getNodeValue();
                  //      indicator = indicatorNode.getAttributes().item(0).getNodeValue();

                        //Node indicatorNode = indicatorList.item(indicatorIndex);
                        //Check whether the node is a leaf or a branch
                        if (indicatorNode.getNodeType() == Node.ELEMENT_NODE) {
                            indicatorElement = (Element) indicatorNode;
                            indicatorId = indicatorNode.getAttributes().getNamedItem("CommodityId").getNodeValue() != null ? 
                                    Integer.parseInt(indicatorNode.getAttributes().getNamedItem("CommodityId").getNodeValue().toString()) : 0;
                            //Get Week values
                            for (int weekIndex = 1; weekIndex < 5; weekIndex++) {
                                week = 0;
                                indicatorDetailList = indicatorElement.getElementsByTagName("Week" + weekIndex);
                                weekElement = (Element) indicatorDetailList.item(0);
                                weekDetailList = weekElement.getChildNodes();
                                //Get week
                                 week = weekIndex;
                               //Get week value, value of indicator for the week                                 
                               //weekValue = Integer.parseInt((weekDetailList.item(0)).getNodeValue().replace(",", ""));
                                 weekValue = weekDetailList.item(0) !=null ? Integer.parseInt((weekDetailList.item(0)).getNodeValue().replace(",", "")) : 0;
                                //Confirm if price is available
                                if (weekValue > 0) {
                                    //Update sql insert statement                            
                                   sqlInsertSuffix = sqlInsertSuffix + ",(" + year + "," + monthId + "," + week + ","
                                            + "" + marketId + "," + indicatorId + "," + supplyId + "," + weekValue + ")";
                                    /*sqlInsertSuffix = sqlInsertSuffix + ",(" + year + "," + monthId + "," + week + ","
                                            + "" + marketId + "," + indicatorId + "," + weekValue + ")";*/
                                }
                            }
                        }
                    }
                }
            }
            sqlInsertSlimsOneDetailsSuffix = sqlInsertSlimsOneDetailsSuffix.replaceFirst(",", "");
            sqlInsertSuffix = sqlInsertSuffix.replaceFirst(",", "");
        } catch (Exception exception) {
            //System.err.println(exception.getMessage());
           // System.err.println("Week Value : "+(weekDetailList.item(0)).getNodeValue());
            exception.printStackTrace();
            logManager.logError("Exception was thrown and caught in 'processDocumentSlimsPartOne(Document doc, int marketId, int monthId, int year)' method");
        }
        logManager.logInfo("Exiting 'processDocumentSlimsPartOne(Document doc, int marketId, int monthId, int year)' method");
        return sqlInsertSuffix;
    }

    private String processSlimsPartTwoDocument(Document doc, int marketId, int monthId, int year) {
        logManager.logInfo("Entering 'processSlimsPartTwoDocument(Document doc, int marketId, int monthId, int year)' method");
        NodeList indicatorList = null;
        NodeList indicatorDetailList = null;
        NodeList valueDetailList = null;
        NodeList locationNameDetailList = null;
        NodeList keyInformantDetailList = null;
        NodeList tiangulationDetailList = null;
        NodeList dataTrustLevelDetailList = null;
        Element indicatorElement = null;
        Element valueElement = null;
        Element locationNameElement = null;
        Element keyInformantElement = null;
        Element tiangulationElement = null;
        Element dataTrustLevelElement = null;
        String indicator = "";
        String comments = "";
        String locationName = "";
        String keyInformant = "";
        String triangulation = "";
        String dataTrustLevel = "";
        int monthValue = -1;
        int week = 1;//Since slims part 2 data is monthly, all rows will have 1 as week value
        int indicatorId = 0;
        int supplyId = 1;

        try {
            sqlInsertSuffix = "";
            indicatorList = doc.getElementsByTagName("Commodity");
            //Traverse commodity names in xml file
            for (int indicatorIndex = 0; indicatorIndex < indicatorList.getLength(); indicatorIndex++) {
               // indicator = indicatorList.item(indicatorIndex).getAttributes().item(0).getNodeValue();
                indicator = doc.getElementsByTagName("Commodity").item(indicatorIndex).getAttributes().getNamedItem("Name").getNodeValue();
                 //indicator = indicatorList.item(indicatorIndex).getAttributes().item(0).getNodeValue();
                //Get all the elements in this node
                Node indicatorNode = indicatorList.item(indicatorIndex);
                //Check whether the node is a leaf or a branch
                if (indicatorNode.getNodeType() == Node.ELEMENT_NODE) {
                    indicatorElement = (Element) indicatorNode;
                    //Rset indicator node
                    indicatorId = 0;
                    //Get indicator id
                    indicatorId = indicatorNode.getAttributes().getNamedItem("CommodityId").getNodeValue() != null ? 
                                    Integer.parseInt(indicatorNode.getAttributes().getNamedItem("CommodityId").getNodeValue().toString()) : 0;                    
                    //Get Week 1 values
                    //for (int weekIndex = 1; weekIndex < 5; weekIndex++) {
                    indicatorDetailList = indicatorElement.getElementsByTagName("Value");
                    valueElement = (Element) indicatorDetailList.item(0);
                    valueDetailList = valueElement.getChildNodes();
                    monthValue = Integer.parseInt((valueDetailList.item(0)).getNodeValue());

                    //Get location name
                    locationNameDetailList = indicatorElement.getElementsByTagName("Location_Name");
                    locationNameElement = (Element) locationNameDetailList.item(0);
                    locationNameDetailList = locationNameElement.getChildNodes();
                    locationName = locationNameDetailList.item(0).getNodeValue();

                    //Get key informant
                    keyInformantDetailList = indicatorElement.getElementsByTagName("Key_Informant");
                    keyInformantElement = (Element) keyInformantDetailList.item(0);
                    keyInformantDetailList = keyInformantElement.getChildNodes();
                    keyInformant = keyInformantDetailList.item(0).getNodeValue();

                    //Get triangulation
                    tiangulationDetailList = indicatorElement.getElementsByTagName("Triangulation");
                    tiangulationElement = (Element) tiangulationDetailList.item(0);
                    tiangulationDetailList = tiangulationElement.getChildNodes();
                    triangulation = tiangulationDetailList.item(0).getNodeValue();

                    //Get data trust level
                    dataTrustLevelDetailList = indicatorElement.getElementsByTagName("Data_Trust_Level");
                    dataTrustLevelElement = (Element) dataTrustLevelDetailList.item(0);
                    dataTrustLevelDetailList = dataTrustLevelElement.getChildNodes();
                    dataTrustLevel = dataTrustLevelDetailList.item(0).getNodeValue();

                    //Confirm if indicator value is available                       
                    if (monthValue > -1) {//-1 is used instead of 0 since some responses may include 0
                        //Update slims 2 month values sql insert statement                            
                        sqlInsertSuffix = sqlInsertSuffix + ",(" + year + "," + monthId + "," + week + "," + marketId + ","
                                + indicatorId + ","+supplyId+"," + monthValue + ")";
                        //Update slims 2 details sql insert statement
                        sqlInsertSlimsTwoDetailsSuffix = sqlInsertSlimsTwoDetailsSuffix + ",(" + year + "," + monthId + "," + marketId + "," + indicatorId
                                + ",'" + locationName + "','" + keyInformant + "','" + triangulation
                                + "','" + dataTrustLevel + "')";
                    }
                }
            }
            sqlInsertSlimsTwoDetailsSuffix = sqlInsertSlimsTwoDetailsSuffix.replaceFirst(",", "");
            sqlInsertSuffix = sqlInsertSuffix.replaceFirst(",", "");
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'processSlimsPartTwoDocument(Document doc, int marketId, int monthId, int year)' method");
        }
        logManager.logInfo("Exiting 'processSlimsPartTwoDocument(Document doc, int marketId, int monthId, int year)' method");
        return sqlInsertSuffix;
    }
}
