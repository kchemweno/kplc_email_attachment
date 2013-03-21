/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.xml;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.orpik.indicators.IndicatorCategory;
import org.orpik.indicators.Indicators;
import org.orpik.location.Market;
import org.orpik.period.Month;
import org.orpik.settings.global.OperatingSystem;

/**
 *
 * @author chemweno
 */
public class XmlWriter {

    private OperatingSystem operatingSystem = new OperatingSystem();
    private XMLOutputFactory xmlOutputFactory = null;
    private XMLStreamWriter xmlStreamWriter = null;
    private Indicators indicator = new Indicators();
    private IndicatorCategory indicatorCategory = new IndicatorCategory();
    private Month month = new Month();
    private Market market = new Market();
    private String xmlFilename = "";
    private File file = null;
    private String directory = "";

    /**
     * Write markets xml file using data on table
     */
    public void createMarketXmlFile(javax.swing.JTable table, String market, String year,
            String month, ArrayList<Integer> categoryRowsList) {
        String indicator = "";
        String indicatorId = "";
        String marketId = "";
        String monthId = "";
        String week = "";
        String price = "";
        //String systemType = "market";
        String application = "1";
        File file = null;
        try {
            /**
             * Write xml file header
             */
            xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlStreamWriter = null;
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileWriter(getXmlFilename("market",
                    year, market, month).getAbsolutePath()));
            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.setPrefix("html", "http://www.w3.org/TR/REC-html40");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement("Market");
            xmlStreamWriter.writeAttribute("Name", market);
            //Get market id
            marketId = this.market.getMarketId(market) != 0 ? this.market.getMarketId(market) + "" : "";
            xmlStreamWriter.writeAttribute("MarketId", marketId);
            xmlStreamWriter.writeAttribute("Month", month);
            //Get month id 
            monthId = this.month.getMonthId(month) + "";
            xmlStreamWriter.writeAttribute("MonthId", monthId);
            xmlStreamWriter.writeAttribute("Year", year);
            //Add a new attribute for fids 2, system type
            xmlStreamWriter.writeAttribute("Application", application);
            /**
             * Traverse table row then column
             */
            for (int row = 0; row < table.getRowCount(); row++) {
                /**
                 * Skip category rows, e.g cereals, Non-Food items etc
                 */
                if (!categoryRowsList.contains(row)) {
                    /**
                     * Check for null values on indicator column
                     */
                    if (table.getValueAt(row, 0) != null) {
                        /**
                         * Get indicator name
                         */
                        indicator = table.getValueAt(row, 0).toString();
                        xmlStreamWriter.writeCharacters("\n");
                        //xtw.writeStartElement(commodityName);//commodity name
                        xmlStreamWriter.writeStartElement("Commodity");
                        xmlStreamWriter.writeAttribute("Name", indicator);
                        //Get indicator id
                        indicatorId = this.indicator.getIndicatorId(indicator) != 0 ? this.indicator.getIndicatorId(indicator) + "" : "";
                        xmlStreamWriter.writeAttribute("CommodityId", indicatorId);
                        for (int column = 1; column < table.getColumnCount(); column++) {
                            if (column == 6) {
                                week = "Supply";
                            } else {
                                week = "Week" + column;
                            }
                            if (table.getValueAt(row, column) != null) {
                                price = table.getValueAt(row, column).toString();
                            } else {
                                /**
                                 * For missing price value, -100 is used
                                 */
                                price = "-100";//No value found
                            }
                            xmlStreamWriter.writeCharacters("\n");
                            xmlStreamWriter.writeStartElement(week);//dimension
                            xmlStreamWriter.writeCharacters(price);//commodity value
                            xmlStreamWriter.writeEndElement();//dimension
                            xmlStreamWriter.writeCharacters("\n");
                        }
                        xmlStreamWriter.writeEndElement();
                    }
                }
            }
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (XMLStreamException xmlStreamException) {
            System.err.println("XML Stream Error occurred while creating XML file \n" + xmlStreamException.getMessage());
            //xmlStreamException.printStackTrace();
        } catch (Exception exception) {
        }
    }

    public void createSlimsPartOneXmlFile(javax.swing.JTable table, String market, String year, String month,
            ArrayList<Integer> categoryRowsList, ArrayList<Integer> detailsRowsList, ArrayList<Integer> lookUpRowsList, String agriculturalActivity,
            String nonAgriculturalActivity, String transportMeans, String transportCommodity, String transportOrigin,
            String transportDestination, String comments) {

        String indicator = "";
        String week = "";
        String price = "";
        String indicatorId = "";
        String monthId = "";
        String marketId = "";
        //String systemType = "slim1";
        String application = "2";
        String category = "";
        String categoryId = "";
        String locationName = "";
        String keyInformant = "";
        String triangulation = "";
        String dataTrustLevel = "";
        String previousCategory = "";
        File file = null;

        try {
            //Get Id values         
            monthId = this.month.getMonthId(month) + "";
            marketId = this.market.getMarketId(market) != 0 ? this.market.getMarketId(market) + "" : "";
            /**
             * Write xml file header
             */
            xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlStreamWriter = null;
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileWriter(getXmlFilename("slims_part1",
                    year, market, month).getAbsolutePath()));
            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.setPrefix("html", "http://www.w3.org/TR/REC-html40");
            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement("Node");
            xmlStreamWriter.writeAttribute("Name", market);
            xmlStreamWriter.writeAttribute("MarketId", marketId);
            xmlStreamWriter.writeAttribute("Month", month);
            xmlStreamWriter.writeAttribute("MonthId", monthId);
            xmlStreamWriter.writeAttribute("Year", year);
            //Add a new attribute for fids 1, application
            xmlStreamWriter.writeAttribute("Application", application);
            /*
             * Add slims part one attributes
             */
            xmlStreamWriter.writeAttribute("Agricultural-Activity", agriculturalActivity);
            xmlStreamWriter.writeAttribute("NonAgricultural-Activity", nonAgriculturalActivity);
            xmlStreamWriter.writeAttribute("Transport-Means", transportMeans);

            xmlStreamWriter.writeAttribute("Transport-Commodity", transportCommodity);
            xmlStreamWriter.writeAttribute("Transport-Origin", transportOrigin);
            xmlStreamWriter.writeAttribute("Transport-Destination", transportDestination);
            xmlStreamWriter.writeAttribute("comments", comments);
            xmlStreamWriter.writeCharacters("\n");
            /**
             * Traverse table row then column
             */
            for (int row = 0; row < table.getRowCount(); row++) {
                /**
                 * Skip category rows, e.g cereals, Non-Food items etc
                 */
                if (!categoryRowsList.contains(row)) {

                    /**
                     * Skip details rows, e.g DATA TRUST LEVEL, KEY INFORMANT etc
                     * Also skip look up rows e.g Agricultural Activity, Transport means etc
                     */
                    if (!detailsRowsList.contains(row) && !lookUpRowsList.contains(row)) {
                        /**
                         * Check for null values on indicator column
                         */
                        if (table.getValueAt(row, 0) != null) {
                            /**
                             * Get indicator name
                             */
                            indicator = table.getValueAt(row, 0).toString();
                            xmlStreamWriter.writeCharacters("\n");
                            xmlStreamWriter.writeStartElement("Commodity");
                            xmlStreamWriter.writeAttribute("Name", indicator);
                            //Get indicator id
                            indicatorId = this.indicator.getIndicatorId(indicator) != 0 ? this.indicator.getIndicatorId(indicator) + "" : "";
                            //Add indicator id as attribute
                            xmlStreamWriter.writeAttribute("CommodityId", indicatorId);
                            for (int column = 1; column < table.getColumnCount(); column++) {
                                // if (column == 6) {
                                //    week = "Supply";
                                // } else {
                                week = "Week" + column;
                                //}
                                if (table.getValueAt(row, column) != null) {
                                    price = table.getValueAt(row, column).toString();
                                } else {
                                    /**
                                     * For missing price value, -100 is used
                                     */
                                    price = "-100";//No value found
                                }
                                xmlStreamWriter.writeCharacters("\n");
                                xmlStreamWriter.writeStartElement(week);//dimension
                                xmlStreamWriter.writeCharacters(price);//commodity value
                                xmlStreamWriter.writeEndElement();//dimension
                                xmlStreamWriter.writeCharacters("\n");
                            }
                            xmlStreamWriter.writeEndElement();
                        }
                    }
                } else {
                    //Check if previous category was closed, if it is the first row then no need to close previous coz previous exister pas
                    if (row != 0) {
                        xmlStreamWriter.writeCharacters("\n");
                        //Write end of element category
                        xmlStreamWriter.writeEndElement();
                    }
                    //Write category details
                    xmlStreamWriter.writeCharacters("\n");
                    //Add category
                    xmlStreamWriter.writeStartElement("Category");
                    //Category name
                    category = table.getValueAt(row, 0) != null ? table.getValueAt(row, 0).toString() : "";
                    xmlStreamWriter.writeAttribute("Name", category);
                    //Add category id
                    categoryId = indicatorCategory.getIndicatorCategoryId(category) != 0
                            ? indicatorCategory.getIndicatorCategoryId(category) + "" : "";
                    xmlStreamWriter.writeAttribute("CategoryId", categoryId);

                    //Add attributes
                    for (int rowIndex = row + 1; rowIndex < table.getRowCount(); rowIndex++) {
                        //Traverse table till you meet a category row
                        if (categoryRowsList.contains(rowIndex)) {
                            rowIndex = table.getRowCount();
                        } else {
                            if (detailsRowsList.contains(rowIndex)) {
                                //Get location name
                                if (table.getValueAt(rowIndex, 0).toString().equalsIgnoreCase("LOCATION NAME")) {
                                    locationName = table.getValueAt(rowIndex, 1) != null ? table.getValueAt(rowIndex, 1).toString() : "";
                                    xmlStreamWriter.writeAttribute("Location-Name", locationName);
                                    //categoryDataExists = locationName.equals("") ? false : true;
                                }
                                //Get key informant
                                if (table.getValueAt(rowIndex, 0).toString().equalsIgnoreCase("KEY INFORMANT")) {
                                    keyInformant = table.getValueAt(rowIndex, 1) != null ? table.getValueAt(rowIndex, 1).toString() : "";
                                    xmlStreamWriter.writeAttribute("Key-Informant", keyInformant);
                                    //categoryDataExists = keyInformant.equals("") ? false : true;
                                }
                                //Get triangulation
                                if (table.getValueAt(rowIndex, 0).toString().equalsIgnoreCase("TRIANGULATION")) {
                                    triangulation = table.getValueAt(rowIndex, 1) != null ? table.getValueAt(rowIndex, 1).toString() : "";
                                    xmlStreamWriter.writeAttribute("Triangulation", triangulation);
                                    //categoryDataExists = triangulation.equals("") ? false : true;
                                }
                                //Get data trust level
                                if (table.getValueAt(rowIndex, 0).toString().equalsIgnoreCase("DATA TRUST LEVEL")) {
                                    dataTrustLevel = table.getValueAt(rowIndex, 1) != null ? table.getValueAt(rowIndex, 1).toString() : "";
                                    xmlStreamWriter.writeAttribute("Data-Trust-Level", dataTrustLevel);
                                    //categoryDataExists = dataTrustLevel.equals("") ? false : true;
                                }
                            }
                        }
                    }
                }
            }
            //Write final ending elemnt for last category on table
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (XMLStreamException xmlStreamException) {
            System.err.println("XML Stream Error occurred while creating XML file \n" + xmlStreamException.getMessage());
            xmlStreamException.printStackTrace();
        } catch (Exception exception) {
        }
    }

    public boolean createSlimsPartTwoXmlFile(String market, String yearName, String monthName, String comments,
            javax.swing.JTable table, ArrayList<Integer> categoryRows) {

        File file = null;//new File(filename);
        String commodityId = "";
        String monthId = "";
        String marketId = "";
        String filename = "";
        String columnContent = "";
        String commodityName = "";
        String value = "";
        //String systemType = "slim2";
        String application = "4";
        try {
            filename = getXmlFilename("slims_part2", yearName, market, monthName).getAbsolutePath();
            file = new File(filename);
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw = null;
            xtw = xof.createXMLStreamWriter(new FileWriter(filename));
            xtw.writeStartDocument();
            xtw.setPrefix("html", "http://www.w3.org/TR/REC-html40");
            xtw.writeCharacters("\n");
            xtw.writeStartElement("Market");//No need to say Node, this will complicate issues
            xtw.writeAttribute("Name", market);
            //Get market id
            marketId = this.market.getMarketId(market) + "";
            xtw.writeAttribute("MarketId", marketId);
            xtw.writeAttribute("Month", monthName);
            //Get month id from month name
            monthId = month.getMonthId(monthName) + "";
            xtw.writeAttribute("MonthId", monthId);
            xtw.writeAttribute("Year", yearName);
            xtw.writeAttribute("Comments", comments);
            //Add attribute for fids 2 
            xtw.writeAttribute("Application", application);
            //Traverse table one row at a time
            for (int tableRow = 0; tableRow < table.getRowCount(); tableRow++) {
                //Skip category rows,category rows are contained in array list
                if (!categoryRows.contains(tableRow)) {
                    //Skip category rows,category rows are distinct in that they have the second column cells as unedittable
                    if (table.isCellEditable(tableRow, 1)) {//Skip row if second column cell in the row is unedittable
                        if (table.getValueAt(tableRow, 0) != null) {
                            commodityName = table.getValueAt(tableRow, 0).toString();
                            //Get commodityId
                            commodityId = !commodityName.equals("") ? indicator.getIndicatorId(commodityName) + "" : "";
                            //insert new line to document
                            xtw.writeCharacters("\n");
                            //xtw.writeStartElement(commodityName);//commodity name
                            xtw.writeStartElement("Commodity");
                            xtw.writeAttribute("Name", commodityName);
                            xtw.writeAttribute("CommodityId", commodityId);
                            //Traverse table a column at a time
                            for (int tableColumn = 1; tableColumn < table.getColumnCount(); tableColumn++) {
                                //
                                switch (tableColumn) {
                                    case 1:
                                        columnContent = "Value";
                                        break;

                                    case 2:
                                        columnContent = "Location_Name";
                                        break;

                                    case 3:
                                        columnContent = "Key_Informant";
                                        break;

                                    case 4:
                                        columnContent = "Triangulation";
                                        break;

                                    case 5:
                                        columnContent = "Data_Trust_Level";
                                        break;
                                }

                                if (table.getValueAt(tableRow, tableColumn) != null) {
                                    value = table.getValueAt(tableRow, tableColumn).toString();
                                } else {
                                    value = "0";//No value found
                                }
                                xtw.writeCharacters("\n");
                                xtw.writeStartElement(columnContent);//dimension
                                xtw.writeCharacters(value);//commodity value
                                xtw.writeEndElement();//dimension
                                xtw.writeCharacters("\n");
                                //xtw.writeEndElement();
                            }
                            xtw.writeEndElement();// commodity name
                        }
                    }
                }
            }
            xtw.writeEndElement();
            xtw.flush();
            xtw.close();
        } catch (XMLStreamException xmlStreamException) {
            System.err.println("XML Stream Error occurred while creating XML file \n" + xmlStreamException.getMessage());
            xmlStreamException.printStackTrace();
            return false; //false shows taht XML file creation failed
        } catch (Exception exception) {
            System.err.println("Error occurred while creating XML file \n" + exception.getMessage());
            exception.printStackTrace();
            //false shows taht XML file creation failed
            return false;
        }
        //true shows that XML file creation succeeded
        return true;
    }

    private File getXmlFilename(String dataset, String year, String market, String month) {
        String fileStructure = "";
        try {
            fileStructure = createXmlStorageDirectory(dataset, year);
            //file = new File(directory + "\\" + getXmlFilenameSuffix(market, year, market));
            file = new File(fileStructure + operatingSystem.getDirectorySeparator() + getXmlFilenameSuffix(market, year, month));
            //file = new File(filed+"//" + filename);
            if (!file.exists()) {
                if (!new File(directory).exists()) {
                    if (new File(directory).mkdirs()) {
                    } else {
                        // JOptionPane.showMessageDialog(null, "Could not create new directory " + directory + " Please try again\n\n",
                        //         "Cirectory Not Created", JOptionPane.WARNING_MESSAGE);
                        System.err.println("Directory " + directory + " was not created");
                    }
                }
            }
        } catch (SecurityException exception) {
            JOptionPane.showMessageDialog(null, "Could not create file in directory " + directory + " beacuse it is read only\n\n",
                    "Access denied on directory", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (Exception exception) {
        }
        return file;
    }

    private String createXmlStorageDirectory(String dataset, String year) {
        try {
            if (operatingSystem.isWindows()) {
                if (dataset.equalsIgnoreCase("market")) {
                    directory = "C:\\FidsData\\Markets\\" + year;
                } else if (dataset.equalsIgnoreCase("slims_part1")) {
                    directory = "C:\\FidsData\\SlimsPart1\\" + year;
                } else if (dataset.equalsIgnoreCase("slims_part2")) {
                    directory = "C:\\FidsData\\SlimsPart2\\" + year;
                }
            } else if (operatingSystem.isLinux() || operatingSystem.isUnix()) {
                if (dataset.equalsIgnoreCase("market")) {
                    directory = "/FidsData/Markets/" + year;
                } else if (dataset.equalsIgnoreCase("slims_part1")) {
                    directory = "/FidsData/SlimsPart2/" + year;
                } else if (dataset.equalsIgnoreCase("slims_part2")) {
                    directory = "/FidsData/SlimsPart2/" + year;
                }
            }
        } catch (Exception exception) {
        }
        return directory;
    }

    private String getXmlFilenameSuffix(String market, String year, String month) {
        String filename = "";
        try {
            if (market.contains("/")) {
                filename = market.replace("/", "_") + year + month + ".xml";
            } else {
                filename = market + year + month + ".xml";
            }
        } catch (Exception exception) {
        }
        return filename;
    }

    public File getXmlFilename() {
        return this.file;
    }
}
