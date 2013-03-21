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
    private String databaseName = "mids";
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

    //Create sql select user query
    public String querySelectUser(String user, String password) {
        logManager.logInfo("Entering 'querySelectUser(String user, String password)' method");
        try {
            sqlQuery = "SELECT COUNT(*) as number_of_users FROM " + databaseName
                    + ".users WHERE username='" + user + "' AND passwrd=SHA1('" + password + "');";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectUser(String user, String password)' method");
        }
        logManager.logInfo("Exiting 'querySelectUser(String user, String password)' method");
        return sqlQuery;
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

    //Get user id query
    public String querySelectUserId(String username) {
        logManager.logInfo("Entering 'querySelectUserId(String username)' method");
        try {
            sqlQuery = "SELECT id as user_id FROM " + databaseName + ".users WHERE username='" + username + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectUserId(String username)' method");
        }
        logManager.logInfo("Exiting 'querySelectUserId(String username)' method");
        return sqlQuery;
    }

    //Create SQL select indicators and categories based on application Ids
    public String querySelectIndicators(String applicationIds) {
        logManager.logInfo("Entering 'querySelectIndicators(String applicationIds)' method");
        try {
            sqlQuery = "SELECT indicators.indicator_business_name,indicator_categories.category_name, "
                    + " indicator_categories.id AS indicator_category_id,indicators.id AS indicator_id "
                    + " FROM " + databaseName + ".indicators INNER JOIN " + databaseName + ".indicator_categories "
                    + " ON indicators.indicator_category_id=indicator_categories.id"
                    + " WHERE indicators.application_id IN (" + applicationIds + ")";
        } catch (Exception exception) {
            logManager.logInfo("Exception was thrown and caught in 'querySelectIndicators(String applicationIds)' method");
        }
        logManager.logInfo("Exiting 'querySelectIndicators(String applicationIds)' method");
        return sqlQuery;
    }

    public String querySelectMarkets(String applicationIds) {
        logManager.logInfo("Entering 'querySelectMarkets(String applicationIds)' method");
        try {
            sqlQuery = "SELECT markets.market_name AS market FROM " + databaseName + ".markets "
                    + " WHERE system_id IN (" + applicationIds + ") ORDER BY market ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarkets(String applicationIds)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarkets(String applicationIds)' method");
        return sqlQuery;
    }

    public String querySelectMarketZones() {
        logManager.logInfo("Entering 'querySelectMarketZones()' method");
        try {
            sqlQuery = "SELECT market_zone.market_zone_name AS market_zone FROM " + databaseName + ".market_zone "
                    + " ORDER BY market_zone ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectMarketZones()' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketZones()' method");
        return sqlQuery;
    }

    public String querySelectFieldAnalysts() {
        logManager.logInfo("Entering 'querySelectFieldAnalysts()' method");
        try {
            sqlQuery = "SELECT staff.staff_name AS field_analyst FROM " + databaseName + ".staff INNER JOIN " + databaseName + ".field_analyst "
                    + " ON staff.id=field_analyst.staff_id WHERE field_analyst.is_retired = 0 ORDER BY field_analyst ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectFieldAnalysts()' method");
        }
        logManager.logInfo("Exiting 'querySelectFieldAnalysts()' method");
        return sqlQuery;
    }

    public String querySelectRegions() {
        logManager.logInfo("Entering 'querySelectRegions()' method");
        try {
            sqlQuery = "SELECT region.region_name AS region FROM " + databaseName + ".region ORDER BY region ASC";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectRegions()' method");
        }
        logManager.logInfo("Exiting 'querySelectRegions()' method");
        return sqlQuery;
    }

    public String querySelectCommodities(String systemId) {
        logManager.logInfo("Entering 'querySelectCommodities(String systemId)' method");
        try {
            sqlQuery = "SELECT indicator_business_name AS indicator FROM " + databaseName
                    + ".indicators WHERE application_id IN (" + systemId + ") ORDER BY indicator ASC";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectCommodities(String systemId)' method");
        }
        logManager.logInfo("Exiting 'querySelectCommodities(String systemId)' method");
        return sqlQuery;
    }

    public String querySelectYears() {
        logManager.logInfo("Entering 'querySelectYears()' method");
        try {
            sqlQuery = "SELECT DISTINCT(year_name) AS year FROM " + databaseName + ".market_data ORDER BY year_name ASC";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectYears()' method");
        }
        logManager.logInfo("Exiting 'querySelectYears()' method");
        return sqlQuery;
    }

    public String querySelectMarketsAll(int systemId) {
        logManager.logInfo("Entering 'querySelectMarketsAll(int systemId)' method");
        try {
            sqlQuery = "SELECT markets.market_name AS market FROM " + databaseName + ".markets WHERE markets.system_id=1 ORDER BY market ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarketsAll(int systemId)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketsAll(int systemId)' method");
        return sqlQuery;
    }

    public String querySelectMarketsForMarketZone(int marketZoneId, int systemId) {
        logManager.logInfo("Entering 'querySelectMarketsForMarketZone(int marketZoneId, int systemId)' method");
        try {
            sqlQuery = " SELECT markets.market_name AS market FROM " + databaseName + ".markets "
                    + " INNER JOIN " + databaseName + ".district ON markets.district_id=district.id "
                    + " INNER JOIN " + databaseName + ".region ON district.region_id=region.id "
                    + " INNER JOIN " + databaseName + ".market_zone ON region.market_zone_id=market_zone.id "
                    + " WHERE market_zone.id=" + marketZoneId + " AND markets.system_id=" + systemId + " ORDER BY market ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarketsForMarketZone(int marketZoneId, int systemId)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketsForMarketZone(int marketZoneId, int systemId)' method");
        return sqlQuery;
    }

    public String querySelectMarketsForRegion(int regionId, int systemId) {
        logManager.logInfo("Entering 'querySelectMarketsForRegion(int regionId, int systemId)' method");
        try {
            sqlQuery = "SELECT markets.market_name AS market FROM " + databaseName + ".markets "
                    + " INNER JOIN " + databaseName + ".district ON markets.district_id=district.id "
                    + " INNER JOIN " + databaseName + ".region ON district.region_id=region.id "
                    + " WHERE region.id=" + regionId + " AND markets.system_id=" + systemId + " ORDER BY market ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectMarketsForRegion(int regionId, int systemId)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketsForRegion(int regionId, int systemId)' method");
        return sqlQuery;
    }

    public String querySelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId) {
        logManager.logInfo("Entering 'querySelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId)' method");
        try {
            sqlQuery = "  SELECT markets.market_name AS market FROM " + databaseName + ".markets "
                    + " WHERE markets.field_analyst_id=" + fieldAnalystId + " AND markets.system_id=" + systemId + " ORDER BY market ASC";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketsForFieldAnalyst(int fieldAnalystId, int systemId)' method");
        return sqlQuery;
    }

    public String querySelectMarketId(String marketName) {
        logManager.logInfo("Entering 'querySelectMarketId(String marketName)' method");
        try {
            sqlQuery = "SELECT markets.id FROM " + databaseName + ".markets WHERE markets.market_name='" + marketName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarketId(String marketName)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketId(String marketName)' method");
        return sqlQuery;
    }

    public String querySelectCivilInsecurityLevelId(String level) {
        logManager.logInfo("Entering 'querySelectCivilInsecurityLevelId(String level)' method");
        try {
            sqlQuery = "SELECT civil_insecurity_level.id FROM " + databaseName + ".civil_insecurity_level WHERE "
                    + " civil_insecurity_level.level='" + level + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectCivilInsecurityLevelId(String level)' method");
        }
        logManager.logInfo("Exiting 'querySelectCivilInsecurityLevelId(String level)' method");
        return sqlQuery;
    }

    public String querySelectCivilInsecurityLevelName(int levelId) {
        logManager.logInfo("Entering 'querySelectCivilInsecurityLevelName(int levelId)' method");
        try {
            sqlQuery = "SELECT civil_insecurity_level.level FROM " + databaseName + ".civil_insecurity_level WHERE "
                    + " civil_insecurity_level.level=" + levelId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectCivilInsecurityLevelName(int levelId)' method");
        }
        logManager.logInfo("Exiting 'querySelectCivilInsecurityLevelName(int levelId)' method");
        return sqlQuery;
    }

    public String querySelectSlimsPart2Comments(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'querySelectSlimsPart2Comments(int year, int monthId, int marketId)' method");
        try {
            sqlQuery = "SELECT comments FROM " + databaseName + ".slims_part2_comments "
                    + " WHERE year_name=" + year + " AND month_id=" + monthId + " AND market_id=" + marketId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectSlimsPart2Comments(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'querySelectSlimsPart2Comments(int year, int monthId, int marketId)' method");
        return sqlQuery;
    }

    public String querySelectMarketZoneId(String marketZoneName) {
        logManager.logInfo("Entering 'querySelectMarketZoneId(String marketZoneName)' method");
        try {
            sqlQuery = "SELECT market_zone.id AS market_zone_id FROM " + databaseName
                    + ".market_zone WHERE market_zone_name='" + marketZoneName + "'";
        } catch (Exception exception) {
            logManager.logError("Entering 'querySelectMarketZoneId(String marketZoneName)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketZoneId(String marketZoneName)' method");
        return sqlQuery;
    }

    public String querySelectFieldAnalystId(String fieldAnalystName) {
        logManager.logInfo("Entering 'querySelectFieldAnalystId(String fieldAnalystName)' method");
        try {
            sqlQuery = "SELECT staff.id AS field_analyst_id FROM " + databaseName + ".staff "
                    + " INNER JOIN " + databaseName + ".field_analyst ON staff.id=field_analyst.staff_id "
                    + " WHERE staff.staff_name='" + fieldAnalystName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectFieldAnalystId(String fieldAnalystName)' method");
        }
        logManager.logInfo("Exiting 'querySelectFieldAnalystId(String fieldAnalystName)' method");
        return sqlQuery;
    }

    public String querySelectRegionId(String regionName) {
        logManager.logInfo("Entering 'querySelectRegionId(String regionName)' method");
        try {
            sqlQuery = "SELECT region.id AS region_id FROM " + databaseName + ".region WHERE region.region_name='" + regionName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectRegionId(String regionName)' method");
        }
        logManager.logInfo("Exiting 'querySelectRegionId(String regionName)' method");
        return sqlQuery;
    }

    public String queryInsertSlims2MonthValuePrefix() {
        logManager.logInfo("Entering 'queryInsertSlims2MonthValuePrefix()' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".market_data (year_name,month_id,market_data.week,market_id,indicator_id,price) VALUES ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryInsertSlims2MonthValuePrefix()' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlims2MonthValuePrefix()' method");
        return sqlQuery;
    }

    public String queryInsertSlims2DetailsPrefix() {
        logManager.logInfo("Entering 'queryInsertSlims2DetailsPrefix()' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".slims_part2_details (slims_part2_details.year,month_id,market_id,indicator_id,"
                    + "location_name,key_informant,triangulation,data_trust_level) VALUES ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryInsertSlims2DetailsPrefix()' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlims2DetailsPrefix()' method");
        return sqlQuery;
    }

    public String queryInsertSlims2Comments(int year, int month, int marketId, String comments) {
        logManager.logInfo("Entering 'queryInsertSlims2Comments(int year, int month, int marketId, String comments)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".slims_part2_comments(year_name,month_id,market_id,comments) "
                    + "VALUES(" + year + "," + month + "," + marketId + ",'" + comments + "')";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryInsertSlims2Comments(int year, int month, int marketId, String comments)' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlims2Comments(int year, int month, int marketId, String comments)' method");
        return sqlQuery;
    }

    public String queryInsertSlims2CommentsPrefix() {
        logManager.logInfo("Entering 'queryInsertSlims2CommentsPrefix()' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".slims_part2_comments(year_name,month_id,market_id,comments) "
                    + "VALUES ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryInsertSlims2CommentsPrefix()' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlims2CommentsPrefix()' method");
        return sqlQuery;
    }

    /**
     * Get indicator id given indicator name
     *
     * @param marketName
     * @return
     */
    public String querySelectIndicatorCategoryId(String IndicatorCategoryName) {
        logManager.logInfo("Entering 'querySelectIndicatorCategoryId(String IndicatorCategoryName)' method");
        try {
            sqlQuery = "SELECT id FROM " + databaseName + ".indicator_categories WHERE category_name='" + IndicatorCategoryName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectIndicatorCategoryId(String IndicatorCategoryName)' method");
        }
        logManager.logInfo("Exiting 'querySelectIndicatorCategoryId(String IndicatorCategoryName)' method");
        return sqlQuery;
    }

    public String querySelectIndicatorId(String indicatorName) {
        logManager.logInfo("Entering 'querySelectIndicatorId(String indicatorName)' method");
        try {
            sqlQuery = "SELECT indicators.id FROM " + databaseName + ".indicators WHERE indicator_business_name='" + indicatorName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectIndicatorId(String indicatorName)' method");
        }
        logManager.logInfo("Exiting 'querySelectIndicatorId(String indicatorName)' method");
        return sqlQuery;
    }

    public String querySelectRecordCount(int year, int monthId, int marketId, int applicationId) {
        logManager.logInfo("Entering 'querySelectRecordCount(int year, int monthId, int marketId, int applicationId)' method");
        try {
            sqlQuery = "SELECT COUNT(*) AS record_count FROM " + databaseName + ".market_data INNER JOIN " + databaseName + ".indicators "
                    + " ON market_data.indicator_id=indicators.id WHERE year_name=" + year + " AND market_id=" + marketId + " AND month_id="
                    + monthId + " AND indicators.application_id=" + applicationId;
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectRecordCount(int year, int monthId, int marketId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'querySelectRecordCount(int year, int monthId, int marketId, int applicationId)' method");
        return sqlQuery;
    }

    public String querySelectMarketPrices(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'querySelectMarketPrices(int year, int monthId, int marketId)' method");
        try {
            sqlQuery = "SELECT market_data.week,indicators.indicator_business_name AS indicator,price,supply.supply "
                    + " FROM " + databaseName + ".market_data "
                    + " INNER JOIN " + databaseName + ".indicators ON market_data.indicator_id=indicators.id "
                    + " LEFT OUTER JOIN " + databaseName + ".supply ON market_data.supply_id=supply.id "
                    + " WHERE market_id=" + marketId + " AND year_name=" + year + " AND month_id=" + monthId + " AND price > 0";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarketPrices(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketPrices(int year, int monthId, int marketId)' method");
        return sqlQuery;
    }

    public String querySelectSlimsPart1Prices(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'querySelectSlimsPart1Prices(int year, int monthId, int marketId)' method");
        try {
            sqlQuery = "SELECT indicators.indicator_business_name AS indicator,week,price FROM " + databaseName + ".market_data "
                    + " INNER JOIN " + databaseName + ".indicators ON market_data.indicator_id=indicators.id "
                    + " WHERE year_name=" + year + " AND market_id=" + marketId + " AND month_id=" + monthId + " AND price > 0";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectSlimsPart1Prices(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'querySelectSlimsPart1Prices(int year, int monthId, int marketId)' method");
        return sqlQuery;
    }

    public String querySelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYeay, int currentMonth, int previousMonth,
            int marketId) {
        logManager.logInfo("Entering 'querySelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYeay, "
                + "int currentMonth, int previousMonth, int marketId)' method");
        try {
            sqlQuery = "SELECT market_data.indicator_id AS indic,indicators.indicator_business_name AS indicator,WEEK,price,"
                    + "ROUND((SELECT AVG(price) FROM " + databaseName + ".market_data WHERE year_name=" + previousMonthYeay + " AND market_id="
                    + marketId
                    + " AND month_id=" + previousMonth + " AND price > 0 AND market_data.indicator_id=indic "
                    + "GROUP BY year_name,month_id,market_id,indicators.indicator_business_name)) AS previous_average "
                    + "FROM " + databaseName + ".market_data "
                    + "INNER JOIN " + databaseName + ".indicators ON market_data.indicator_id=indicators.id "
                    + "WHERE year_name=" + currentMonthYear + " AND market_id=" + marketId + " AND month_id=" + currentMonth + " AND price > 0 "
                    + "ORDER BY indicators.indicator_business_name ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYeay, "
                    + "int currentMonth, int previousMonth, int marketId)' method");
        }
        logManager.logInfo("Exiting 'querySelectSlimsPart1PricesWithAverage(int currentMonthYear, int previousMonthYeay, "
                + "int currentMonth, int previousMonth, int marketId)' method");
        return sqlQuery;
    }

    public String querySelectSlimsPart1Details(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'querySelectSlimsPart1Details(int year, int monthId, int marketId)' method");
        try {
            sqlQuery = "SELECT category_name,location_name,key_informant,triangulation,data_trust_level "
                    + " FROM " + databaseName + ".slims_part1_details "
                    + " INNER JOIN " + databaseName + ".indicator_categories ON indicator_categories.id=slims_part1_details.category_id "
                    + " WHERE slims_part1_details.year=" + year + " AND month_id=" + monthId + " AND market_id=" + marketId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectSlimsPart1Details(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'querySelectSlimsPart1Details(int year, int monthId, int marketId)' method");
        return sqlQuery;
    }

    public String querySelectSlimsPart2Lookup(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'querySelectSlimsPart2Lookup(int year, int monthId, int marketId)' method");
        try {
            sqlQuery = "SELECT agricultural_activity,non_agricultural_activity,transport_means,"
                    + "transport_commodity,transport_origin,transport_destination,comments "
                    + "FROM " + databaseName + ".slims_part1_lookup "
                    + "WHERE slims_part1_lookup.year=" + year + " AND month_id=" + monthId + " AND market_id=" + marketId;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectSlimsPart2Lookup(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'querySelectSlimsPart2Lookup(int year, int monthId, int marketId)' method");
        return sqlQuery;
    }

    public String querySelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonthId, int previousMonthId,
            int marketId) {
        logManager.logInfo("Entering 'querySelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonthId, int previousMonthId, int marketId )' method");
        try {
            sqlQuery = "SELECT market_data.week,indicator_id AS indic,indicators.indicator_business_name AS indicator,price, "
                    + "ROUND((SELECT AVG(price) AS average_price FROM " + databaseName + ".market_data "
                    + " WHERE market_id=" + marketId + " AND year_name=" + previousMonthYear + " AND month_id="
                    + previousMonthId + " AND indicator_id=indic "
                    + " AND price > 0 GROUP BY indicator_id)) AS average_price "
                    //+ " -- ,supply.supply "
                    + " FROM " + databaseName + ".market_data INNER JOIN " + databaseName + ".indicators ON market_data.indicator_id=indicators.id "
                    //+ " -- LEFT OUTER JOIN "+databaseName+".supply ON market_data.supply_id=supply.id "
                    + " WHERE market_id=" + marketId + " AND year_name=" + currentMonthYear + " AND month_id=" + currentMonthId + " AND price > 0 "
                    + " GROUP BY market_id,year_name,month_id,indicator_id,market_data.week";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonthId, int previousMonthId, int marketId )' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketPricesWithAverage(int currentMonthYear, int previousMonthYear, int currentMonthId, int previousMonthId, int marketId )' method");
        return sqlQuery;
    }

    public String querySelectMarketPricesBetweenYears(int startYear, int endYear, int indicatorId, String marketIds) {
        logManager.logInfo("Entering 'querySelectMarketPricesBetweenYears(int startYear, int endYear, int indicatorId, String marketIds)' method");
        try {
            /*sqlQuery = "SELECT year_name,month_id,ROUND(AVG(price)) AS month_value FROM " + databaseName + ".market_data "
                    + " WHERE year_name BETWEEN " + startYear + " AND " + endYear + " AND market_id IN(" + marketIds + ")"
                    + " AND indicator_id=" + indicatorId + " AND price > 0 GROUP BY year_name,month_id "
                    + " ORDER BY year_name,month_id";*/
            //New query corrects discripancies with FIDS version 1
            sqlQuery = "SELECT  month_id,year_name,AVG(final_price) AS month_value FROM "
                    + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                    + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM mids.market_data "
                    + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0 "
                    + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                    + " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id "; 
            
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectMarketPricesBetweenYears(int startYear, int endYear, int indicatorId, String marketIds)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketPricesBetweenYears(int startYear, int endYear, int indicatorId, String marketIds)' method");
        return sqlQuery;
    }

    public String querySelectMarketPricesBetweenYearsTot() {
        logManager.logInfo("Entering 'querySelectMarketPricesBetweenYearsTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        try {
            /*sqlQuery = "SELECT  year_name AS yr,month_id AS mi,ROUND(AVG(price)) AS commodity_one_value, "
                    + " (SELECT ROUND(AVG(price)) FROM " + databaseName + ".market_data WHERE year_name=yr AND month_id=mi "
                    + " AND market_id IN(" + marketIds + ") AND indicator_id=" + indicatorTwoId
                    + " GROUP BY year_name,month_id) AS commodity_two_value "
                    + " FROM " + databaseName + ".market_data WHERE year_name BETWEEN " + startYear + " AND " + endYear + " "
                    + " AND market_id IN(" + marketIds + ") "
                    + " AND indicator_id IN (" + indicatorOneId + ") AND price > 0 GROUP BY yr,mi ORDER BY yr,mi";*/

           /* sqlQuery = "SELECT  year_name AS yr,market_id AS mrkt_id,month_id AS mi,AVG(price) AS commodity_one_value, "
                    + " (SELECT AVG(price) FROM " + databaseName + ".market_data WHERE year_name=yr AND month_id=mi "
                    + " AND market_id=mrkt_id AND indicator_id=" + indicatorTwoId
                    + " GROUP BY year_name,month_id) AS commodity_two_value "
                    + " FROM " + databaseName + ".market_data WHERE year_name BETWEEN " + startYear + " AND " + endYear + " "
                    + " AND market_id IN(" + marketIds + ") "
                    + " AND indicator_id IN (" + indicatorOneId + ") AND price > 0 GROUP BY yr,mi ORDER BY yr,mi";*/
            
          /*  sqlQuery = "SELECT  year_name AS yr,market_id AS mrkt_id,month_id AS mi,AVG(price) AS commodity_one_value, "
                    + " (SELECT AVG(price) FROM " + databaseName + ".market_data WHERE year_name=yr AND month_id=mi "
                    + " AND market_id=mrkt_id AND indicator_id=" + indicatorTwoId
                    + " and price > 0 GROUP BY market_id,year_name,month_id) AS commodity_two_value "
                    + " FROM " + databaseName + ".market_data WHERE year_name BETWEEN " + startYear + " AND " + endYear + " "
                    + " AND market_id IN(" + marketIds + ") "
                    + " AND indicator_id IN (" + indicatorOneId + ") AND price > 0 GROUP BY yr,mi ORDER BY yr,mi";*/     
            
                   // sqlQuery = "DROP TABLE IF EXISTS temp_tb1;"
                   // + " CREATE TEMPORARY TABLE temp_tb1  AS "
                   // + " SELECT  month_id,year_name,AVG(final_price) AS commodity_one_value FROM "
                   // + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                   // + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM mids.market_data"
                   // + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorOneId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0        "
                   // + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                   // + " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id; "
                   // + " DROP TABLE IF EXISTS temp_tb2; "
                   // + " CREATE TEMPORARY TABLE temp_tb2 AS "
                   // + " SELECT  month_id,year_name,AVG(final_price) AS commodity_two_value FROM "
                   // + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                   // + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM mids.market_data "
                   // + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorTwoId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0        "
                   // + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                   //+ " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id; "
                   sqlQuery = "SELECT temp_tb1.year_name AS yr, temp_tb1.month_id as mi,commodity_one_value,commodity_two_value FROM "
                    + " "+databaseName+".temp_tb1 INNER JOIN "+databaseName+".temp_tb2 ON temp_tb1.year_name=temp_tb2.year_name AND temp_tb1.month_id=temp_tb2.month_id";
            
             System.out.println("New Query \n" + sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectMarketPricesBetweenYearsTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketPricesBetweenYearsTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        return sqlQuery;
    }
    
    
    public String[] queryDropCreateTemporaryTablesTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds) {
        logManager.logInfo("Entering 'queryDropCreateTemporaryTablesTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        String sqlDropTableOne = "";
        String sqlDropTableTwo = "";
        String sqlCreateTableOne = "";
        String sqlCreateTableTwo = "";
        String[] sqlBatch = new String[4];
        try {
            
            sqlDropTableOne = " DROP TABLE IF EXISTS "+databaseName+".temp_tb1 ";
            
            sqlCreateTableOne = " CREATE TEMPORARY TABLE "+databaseName+".temp_tb1  AS "
                                + " SELECT  month_id,year_name,AVG(final_price) AS commodity_one_value FROM "
                                + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                                + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM "+databaseName+".market_data"
                                + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorOneId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0        "
                                + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                                + " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id";
            
            sqlDropTableTwo = " DROP TABLE IF EXISTS "+databaseName+".temp_tb2 ";
            
            sqlCreateTableTwo = " CREATE TEMPORARY TABLE "+databaseName+".temp_tb2 AS "
                                + " SELECT  month_id,year_name,AVG(final_price) AS commodity_two_value FROM "
                                + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                                + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM "+databaseName+".market_data "
                                + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorTwoId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0        "
                                + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                                + " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id";
            
           /* sqlQuery = "DROP TABLE IF EXISTS "+databaseName+".temp_tb1; "
                    + " CREATE TEMPORARY TABLE "+databaseName+".temp_tb1  AS "
                    + " SELECT  month_id,year_name,AVG(final_price) AS commodity_one_value FROM "
                    + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                    + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM "+databaseName+".market_data"
                    + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorOneId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0        "
                    + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                    + " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id; "
                    + " DROP TABLE IF EXISTS "+databaseName+".temp_tb2 ;"
                    + " CREATE TEMPORARY TABLE "+databaseName+".temp_tb2 AS "
                    + " SELECT  month_id,year_name,AVG(final_price) AS commodity_two_value FROM "
                    + " (SELECT month_id,year_name,market_id,AVG(monthly_price) AS final_price FROM "
                    + " (SELECT WEEK,month_id,year_name,market_id,AVG(price) AS monthly_price FROM "+databaseName+".market_data "
                    + " WHERE market_id IN(" + marketIds + ") AND indicator_id=" + indicatorTwoId + " AND year_name BETWEEN " + startYear + " AND " + endYear + " AND price > 0        "
                    + " GROUP BY month_id,WEEK,year_name,market_id ORDER BY year_name,month_id ASC) AS tbl_final "
                    + " GROUP BY market_id,year_name,month_id) AS tbl_last GROUP BY year_name,month_id";*/
                  //  + " SELECT temp_tb1.year_name AS yr, temp_tb1.month_id as mi,commodity_one_value,commodity_two_value FROM "
                  //  + " temp_tb1 INNER JOIN temp_tb2 ON temp_tb1.year_name=temp_tb2.year_name AND temp_tb1.month_id=temp_tb2.month_id";
            
            // System.out.println("New Query \n" + sqlQuery);
            sqlBatch[0] = sqlDropTableOne;
            sqlBatch[1] = sqlCreateTableOne;
            sqlBatch[2] = sqlDropTableTwo;
            sqlBatch[3] = sqlCreateTableTwo;
           
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryDropCreateTemporaryTablesTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        }
        logManager.logInfo("Exiting 'queryDropCreateTemporaryTablesTot(int startYear, int endYear, int indicatorOneId, int indicatorTwoId, String marketIds)' method");
        return sqlBatch;
    }    

    //Select market update values
    public String querySelectMarketUpdateValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds) {
        logManager.logInfo("Entering 'querySelectMarketUpdateValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds)' method");
        int previousYear = yearName - 1;
        int previousMonth = month == 1 ? 12 : month - 1;
        int previousYearForDecember = yearName - 1;
        try {
            /*sqlQuery = "SELECT indicator_id,indicators.indicator_business_name AS indicator,year_name,month_id,format(ROUND(AVG(price)),0) "
                    + " AS month_value FROM " + databaseName + ".market_data INNER JOIN " + databaseName + ".indicators "
                    + " ON market_data.indicator_id=indicators.id"
                    + " WHERE (year_name=" + previousYear + " AND month_id=" + month + ") OR (year_name=" + yearName
                    + " AND month_id=" + previousMonth + ") OR (year_name="
                    + yearName + " AND month_id=" + month + ") OR "
                    + " (month_id=" + month + " AND year_name IN (" + fiveYearsAverage + ")) "
                    + " AND market_id IN (" + marketIds + ") "
                    + " AND indicator_id IN (" + indicatorIds + ") AND price > 0 GROUP BY year_name,month_id,indicator_id "
                    + " ORDER BY year_name,month_id";*/
            
            sqlQuery = "SELECT indicator_id,indicator,year_name,month_id,FORMAT(AVG(month_value),0) AS month_value FROM "
                    + " (SELECT indicator_id,indicator,WEEK,market_id,year_name,month_id,AVG(month_value) AS month_value FROM "
                    + " (SELECT indicator_id,indicators.indicator_business_name AS indicator,market_id,year_name,month_id,WEEK,"
                    + " AVG(price) AS month_value FROM "+databaseName+".market_data "
                    + " INNER JOIN "+databaseName+".indicators ON market_data.indicator_id=indicators.id "
                    + " WHERE ((year_name=" + previousYear + " AND month_id=" + month + ") "
                    + " OR (year_name=" + yearName + " AND month_id=" + previousMonth + ") "
                    + " OR (year_name=" + previousYearForDecember + " AND month_id=" + previousMonth + ") "
                    + " OR (year_name=" + yearName + " AND month_id=" + month + ") "
                    + " OR (month_id=" + month + " AND year_name IN (" + fiveYearsAverage + ")))  "
                    + " AND market_id IN (" + marketIds + ") "
                    + " AND indicator_id IN (" + indicatorIds + ") AND price > 0 "
                    + " GROUP BY month_id,WEEK,year_name,indicator_id,market_id "
                    + " ORDER BY year_name,month_id ASC) AS tbl_intermediate "
                    + " GROUP BY market_id,year_name,indicator_id,month_id) AS tbl_final"
                    + " GROUP BY indicator_id,month_id,year_name";
            
            
            System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'querySelectMarketUpdateValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketUpdateValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds)' method");
        return sqlQuery;
    }

    //Select market update five year average values
    public String querySelectMarketUpdateFiveYearAverageValues(int yearName, int month, String marketIds, String fiveYearsAverage,
            String indicatorIds) {
        logManager.logInfo("Entering 'querySelectMarketUpdateFiveYearAverageValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds)' method");
        int previousYear = yearName - 1;
        int previousMonth = month - 1;
        try {
            sqlQuery = " SELECT indicator_id,indicators.indicator_business_name AS indicator,"
                    + " FORMAT(ROUND(AVG(price)),0) AS five_year_average FROM "
                    + databaseName + ".market_data INNER JOIN " + databaseName + ".indicators "
                    + " ON market_data.indicator_id=indicators.id WHERE (month_id=" + month + " AND year_name IN (" + fiveYearsAverage + "))"
                    + " AND market_id IN (" + marketIds + ") AND indicator_id IN (" + indicatorIds + ")  "
                    + " AND price > 0 GROUP BY indicator_id,year_name,month_id  ORDER BY year_name,month_id";
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectMarketUpdateFiveYearAverageValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds)' method");
        }
        logManager.logInfo("Exiting 'querySelectMarketUpdateFiveYearAverageValues(int yearName, int month, String marketIds, String fiveYearsAverage, String indicatorIds)' method");
        return sqlQuery;
    }

    //Select data for specified month in a year
    public String querySelectAllDataByMonth(String exportOption, int month, int year) {
        logManager.logInfo("Entering 'querySelectAllDataByMonth(String exportOption, int month, int year)' method");
        try {
            sqlQuery = "SELECT region.region_name,district.district_name,markets.market_name, system.system_name AS market_type,"
                    + " market_data.year_name,market_data.month_id,FORMAT(AVG(market_data.price),0) AS month_value, "
                    + " indicators.id AS indicator_id,indicators.indicator_business_name AS indicator "
                    + " FROM " + databaseName + ".market_data "
                    + " INNER JOIN " + databaseName + ".indicators ON market_data.indicator_id=indicators.id "
                    + " INNER JOIN " + databaseName + ".markets ON market_data.market_id=markets.id "
                    + " INNER JOIN " + databaseName + ".district ON markets.district_id=district.id "
                    + " INNER JOIN " + databaseName + ".region ON district.region_id=region.id "
                    + " INNER JOIN " + databaseName + ".system ON markets.system_id=system.id "
                    + " WHERE year_name=" + year + " AND month_id=" + month + " AND markets.system_id IN(" + exportOption + ")  AND price > 0 "
                    + " GROUP BY region.id,district.id,market_id,year_name,month_id,indicator_id "
                    + " ORDER BY region.region_name,district.district_name,market_name,indicators.id";
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectAllDataByMonth(String exportOption, int month, int year)' method");
        }
        logManager.logInfo("Exiting 'querySelectAllDataByMonth(String exportOption, int month, int year)' method");
        return sqlQuery;
    }

    //Select range of data from one year to another
    public String querySelectRangeDataByMonth(String exportOption, int fromYear, int toYear) {
        logManager.logInfo("Entering 'querySelectRangeDataByMonth(String exportOption, int fromYear, int toYear)' method");
        try {
            sqlQuery = "SELECT region.region_name,district.district_name,markets.market_name, system.system_name  AS market_type, "
                    + " market_data.year_name,market_data.month_id,FORMAT(AVG(market_data.price),0) AS month_value, "
                    + " indicators.id AS indicator_id,indicators.indicator_business_name AS indicator "
                    + " FROM " + databaseName + ".market_data INNER JOIN " + databaseName + ".indicators ON market_data.indicator_id=indicators.id "
                    + " INNER JOIN " + databaseName + ".markets ON market_data.market_id=markets.id "
                    + " INNER JOIN " + databaseName + ".district ON markets.district_id=district.id "
                    + " INNER JOIN " + databaseName + ".region ON district.region_id=region.id "
                    + " INNER JOIN " + databaseName + ".system ON markets.system_id=system.id "
                    + " WHERE year_name BETWEEN " + fromYear + " AND " + toYear + " AND markets.system_id IN(" + exportOption + ") AND price > 0 "
                    + " GROUP BY indicator_id, market_id, year_name, month_id "
                  //  + " ORDER BY region.region_name,district.district_name, market_name, year_name, month_id";
                    + " ORDER BY region.region_name,district.district_name, market_name, year_name, month_id, indicator_id";  
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectRangeDataByMonth(String exportOption, int fromYear, int toYear)' method");
        }
        logManager.logInfo("Exiting 'querySelectRangeDataByMonth(String exportOption, int fromYear, int toYear)' method");
        return sqlQuery;
    }

    /**
     * Get email properties
     *
     * @return select query
     */
    public String querySelectEmailProperties() {
        logManager.logInfo("Entering 'querySelectEmailProperties()' method");
        try {
            sqlQuery = "SELECT email_message,attachment_default_location,sender_email,sender_password,sender_host,"
                    + " sender_port,email_subject,email_recipient,email_cc FROM " + databaseName + ".email_properties WHERE id=1";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'querySelectEmailProperties()' method");
        }
        logManager.logInfo("Exiting 'querySelectEmailProperties()' method");
        return sqlQuery;
    }

    public String queryInsertMarketdata() {
        logManager.logInfo("Entering 'queryInsertMarketdata()' method");
        try {
            /*sqlQuery = "INSERT INTO " + databaseName + ".market_data (year_name,month_id,market_data.week,market_id,"
                    + "indicator_id,supply_id,price) VALUES ";*/
            sqlQuery = "INSERT INTO " + databaseName + ".market_data (year_name,month_id,market_data.week,market_id,"
                    + "indicator_id,supply_id,price) VALUES ";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryInsertMarketdata()' method");
        }
        logManager.logInfo("Exiting 'queryInsertMarketdata()' method");
        return sqlQuery;
    }

    public String queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week) {
        logManager.logInfo("Entering 'queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".market_data SET price=" + newValue + " WHERE year_name=" + year
                    + " AND month_id=" + month + " AND indicator_id=" + indicatorId + " AND market_id=" + marketId + " AND market_data.week=" + week;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        return sqlQuery;
    }
    
    public String queryInsertUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week) {
        logManager.logInfo("Entering 'queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".market_data(year_name,month_id,indicator_id,market_id,market_data.week,price)"
                    + " VALUES("+year+","+month+","+indicatorId+","+marketId+","+week+","+newValue+") "
                    + " ON DUPLICATE KEY UPDATE price="+newValue;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateMarketdata(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        return sqlQuery;
    }    

    public String queryUpdateSlimsPart1Price(int year, int month, int marketId, int indicatorId, int newValue, int week) {
        logManager.logInfo("Entering 'queryUpdateSlimsPart1Price(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".market_data SET price=" + newValue + " WHERE year_name=" + year
                    + " AND month_id=" + month + " AND indicator_id=" + indicatorId + " AND market_id=" + marketId + " AND market_data.week=" + week;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryUpdateSlimsPart1Price(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateSlimsPart1Price(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        return sqlQuery;
    }

    public String queryUpdateSlimsPart1Details(int year, int month, int marketId, int categoryId, String newValue, String detail) {
        logManager.logInfo("Entering 'queryUpdateSlimsPart1Details(int year, int month, int marketId, int categoryId, String newValue, String detail)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".slims_part1_details SET " + detail + "='" + newValue + "'"
                    + " WHERE slims_part1_details.year=" + year + " AND month_id=" + month + " AND market_id=" + marketId + " AND category_id=" + categoryId;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryUpdateSlimsPart1Details(int year, int month, int marketId, int categoryId, String newValue, String detail)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateSlimsPart1Details(int year, int month, int marketId, int categoryId, String newValue, String detail)' method");
        return sqlQuery;
    }

    public String queryUpdateSlimsPart1Lookup(int year, int month, int marketId, String newValue, String detail) {
        logManager.logInfo("Entering 'queryUpdateSlimsPart1Lookup(int year, int month, int marketId, String newValue, String detail)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".slims_part1_lookup SET " + detail + "=" + newValue
                    + " WHERE slims_part1_lookup.year=" + year + " AND month_id=" + month + " AND market_id=" + marketId;
        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'queryUpdateSlimsPart1Lookup(int year, int month, int marketId, String newValue, String detail)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateSlimsPart1Lookup(int year, int month, int marketId, String newValue, String detail)' method");
        return sqlQuery;
    }

    public String queryUpdateSlimsPart1Lookup(int year, int month, int marketId, int indicatorId, int newValue, int week) {
        logManager.logInfo("Entering 'queryUpdateSlimsPart1Lookup(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".market_data SET price=" + newValue + " WHERE year_name=" + year
                    + " AND month_id=" + month + " AND indicator_id=" + indicatorId + " AND market_id=" + marketId + " AND market_data.week=" + week;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryUpdateSlimsPart1Lookup(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateSlimsPart1Lookup(int year, int month, int marketId, int indicatorId, int newValue, int week)' method");
        return sqlQuery;
    }

    public String queryUpdateSlimsPart2Details(int year, int month, int marketId, int indicatorId, String newValue, String tableColumn) {
        logManager.logInfo("Entering 'queryUpdateSlimsPart2Details(int year, int month, int marketId, int indicatorId, String newValue, String tableColumn)' method");
        try {
            sqlQuery = "INSERT INTO mids.slims_part2_details(year,month_id,market_id,indicator_id," + tableColumn + ")"
                    + " VALUES(" + year + "," + month + "," + marketId + "," + indicatorId + ",'" + newValue + "') "
                    + " ON DUPLICATE KEY UPDATE " + tableColumn + "='" + newValue + "'";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryUpdateSlimsPart2Details(int year, int month, int marketId, int indicatorId, String newValue, String tableColumn)' method");
        }
        logManager.logInfo("Exiting 'queryUpdateSlimsPart2Details(int year, int month, int marketId, int indicatorId, String newValue, String tableColumn)' method");
        return sqlQuery;
    }

    public String queryInsertSlimOnePrices() {
        logManager.logInfo("Entering 'queryInsertSlimOnePrices()' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".market_data (year_name,month_id,market_data.week,market_id,"
                    + "indicator_id,price) VALUES ";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryInsertSlimOnePrices()' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlimOnePrices()' method");
        return sqlQuery;
    }

    public String queryInsertSlimOneDetails() {
        logManager.logInfo("Entering 'queryInsertSlimOneDetails()' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".slims_part1_details (slims_part1_details.year,month_id,market_id,"
                    + " category_id,location_name,key_informant,triangulation,data_trust_level) VALUES ";
        } catch (Exception exception) {
            logManager.logInfo("Exception thrown and caught in 'queryInsertSlimOneDetails()' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlimOneDetails()' method");
        return sqlQuery;
    }

    public String queryInsertSlimOneLookUp() {
        logManager.logInfo("Entering 'queryInsertSlimOneLookUp()' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".slims_part1_lookup (slims_part1_lookup.year,month_id,market_id,"
                    + " agricultural_activity,non_agricultural_activity,transport_means,transport_commodity,"
                    + " transport_origin,transport_destination,comments) VALUES ";
        } catch (Exception exception) {
            logManager.logError("Exiting 'queryInsertSlimOneLookUp()' method");
        }
        logManager.logInfo("Exiting 'queryInsertSlimOneLookUp()' method");
        return sqlQuery;
    }

    public String sqlSelectSlimTwoData(int year, int monthId, int marketId) {
        logManager.logInfo("Entering 'sqlSelectSlimTwoData(int year, int monthId, int marketId)' method");
        try {
            sqlQuery = "SELECT indicator_business_name  AS indicator,price,location_name,key_informant,triangulation,data_trust_level "
                    + "FROM " + databaseName + ".market_data INNER JOIN " + databaseName + ".slims_part2_details ON "
                    + "(market_data.year_name=slims_part2_details.year "
                    + "AND market_data.month_id=slims_part2_details.month_id AND market_data.market_id=slims_part2_details.market_id "
                    + "AND market_data.indicator_id=slims_part2_details.indicator_id) "
                    + "INNER JOIN " + databaseName + ".indicators ON indicators.id=market_data.indicator_id "
                    + "WHERE  market_data.market_id=" + marketId + " AND market_data.month_id=" + monthId + " AND market_data.year_name=" + year;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'sqlSelectSlimTwoData(int year, int monthId, int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectSlimTwoData(int year, int monthId, int marketId)' method");
        return sqlQuery;
    }

    public String sqlSelectSlimTwoDataWithPreviousMonth(int year, int monthId, int marketId, int previousMonthYear, int previousMonth) {
        logManager.logInfo("Entering 'sqlSelectSlimTwoDataWithPreviousMonth(int year, int monthId, int marketId,int previousMonthYear, int previousMonth)' method");
        try {
            sqlQuery = "SELECT indicator_business_name as indicator,market_data.indicator_id AS indic,price,"
                    + "(SELECT price FROM " + databaseName + ".market_data WHERE market_id=" + marketId + " AND month_id="
                    + previousMonth + " AND year_name=" + previousMonthYear
                    + " AND indicator_id=indic) AS previous_month_value,"
                    + "location_name,key_informant,triangulation,data_trust_level "
                    + "FROM " + databaseName + ".market_data INNER JOIN " + databaseName + ".slims_part2_details "
                    + "ON (market_data.year_name=slims_part2_details.year "
                    + "AND market_data.month_id=slims_part2_details.month_id "
                    + "AND market_data.market_id=slims_part2_details.market_id "
                    + "AND market_data.indicator_id=slims_part2_details.indicator_id) "
                    + "INNER JOIN " + databaseName + ".indicators ON indicators.id=market_data.indicator_id "
                    + "WHERE market_data.market_id=" + marketId + " AND market_data.month_id=" + monthId + " AND market_data.year_name=" + year;
            System.out.println("Data cleaning slims part 2 : " + sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'sqlSelectSlimTwoDataWithPreviousMonth(int year, int monthId, int marketId,int previousMonthYear, int previousMonth)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectSlimTwoDataWithPreviousMonth(int year, int monthId, int marketId,int previousMonthYear, int previousMonth)' method");
        return sqlQuery;
    }

    //Select market update indicators
    public String getMarketUpdateIndicators(int marketZoneId) {
        logManager.logInfo("Entering 'getMarketUpdateIndicators(int marketZoneId)' method");
        try {
            sqlQuery = "SELECT indicators.id AS indicator_id, indicators.indicator_business_name AS indicator,"
                    + " market_zone.id AS market_zone_id,market_update_category.name AS category_name,mids.market_update_category.id "
                    + " FROM " + databaseName + ".indicators INNER JOIN " + databaseName + ".market_update_indicator_region_category ON "
                    + databaseName + ".indicators.id=market_update_indicator_region_category.indicator_id "
                    + " INNER JOIN " + databaseName + ".market_zone ON "
                    + " market_zone.id=market_update_indicator_region_category.market_zone_id "
                    + " INNER JOIN " + databaseName + ".market_update_category ON "
                    + " market_update_indicator_region_category.category_id=market_update_category.id "
                    + " WHERE market_zone.id=" + marketZoneId
                    + " GROUP BY indicator_id,market_zone_id,category_name "
                    + " ORDER BY market_update_category.id,indicator ASC";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'getMarketUpdateIndicators(int marketZoneId)' method");
        }
        logManager.logInfo("Exiting 'getMarketUpdateIndicators(int marketZoneId)' method");
        return sqlQuery;
    }

    //Select whether the data already exists before saving
    public String queryCheckIfDataExists(int year, int month, int marketId, int applicationId) {
        logManager.logInfo("Entering 'queryCheckIfDataExists(int year, int month, int marketId, int applicationId)' method");
        try {
            sqlQuery = "SELECT COUNT(*) AS data_row_count FROM " + databaseName + ".market_data "
                    + " INNER JOIN " + databaseName + ".indicators ON mids.market_data.indicator_id=mids.indicators.id "
                    + " INNER JOIN " + databaseName + ".application ON mids.indicators.application_id=mids.application.id "
                    + " WHERE market_data.year_name=" + year + " AND mids.market_data.month_id=" + month
                    + " AND market_data.market_id=" + marketId + " AND mids.application.id=" + applicationId;
            System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'queryCheckIfDataExists(int year, int month, int marketId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'queryCheckIfDataExists(int year, int month, int marketId, int applicationId)' method");
        return sqlQuery;
    }

    //Get analysis settings
    public String sqlSelectAnalysisSettings() {
        logManager.logInfo("Entering 'sqlSelectAnalysisSettings()' method");
        try {
            sqlQuery = "SELECT series_years AS series,average_years AS average FROM " + databaseName + ".settings";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught 'sqlSelectAnalysisSettings()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectAnalysisSettings()' method");
        return sqlQuery;
    }

    //Update chart settings
    public String sqlSelectChartSettings(int userId) {
        logManager.logInfo("Entering 'sqlSelectChartSettings(int userId)' method");
        try {
            sqlQuery = "SELECT user_id,title_name,show_title,title_font_name, title_font_size,title_font_bold, "
                    + " title_font_italic,title_font_color,domain_axis_label_name,domain_axis_font_name,"
                    + " domain_axis_font_size,domain_axis_font_bold, domain_axis_font_italic,domain_axis_font_color,"
                    + " domain_axis_paint,domain_axis_show_tick_labels,domain_axis_show_tick_marks,"
                    + " domain_axis_tick_label_font_name,domain_axis_tick_label_font_size,domain_axis_tick_label_font_bold,"
                    + " domain_axis_tick_label_font_italic,domain_axis_tick_label_font_color,range_axis_label_name,range_axis_font_name,"
                    + " range_axis_font_size,range_axis_font_bold,range_axis_font_italic,range_axis_font_color,"
                    + " range_axis_paint,range_axis_show_tick_labels,range_axis_show_tick_marks,range_axis_label_font_name,"
                    + " range_axis_label_font_size,range_axis_label_font_bold,range_axis_label_font_italic,range_axis_label_font_color,"
                    + " appearance_outline_stroke,appearance_outline_paint,appearance_background_paint,appearance_orientation,"
                    + " other_draw_antialiasing,other_background_paint, "
                    + " range_axis_tick_label_font_name,range_axis_tick_label_font_size,range_axis_tick_label_font_bold,"
                    + " range_axis_tick_label_font_italic,range_axis_tick_label_font_color "
                    + " FROM " + databaseName + ".chart_settings  WHERE user_id=" + userId;
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'sqlSelectChartSettings(int userId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectChartSettings(int userId)' method");
        return sqlQuery;
    }

    //Save default chart settings
    public String sqlInsertDefaultChartSettings(int userId) {
        logManager.logInfo("Entering 'sqlInsertDefaultChartSettings(int userId)' method");
        try {
            sqlQuery = "INSERT INTO mids.chart_settings(user_id) VALUES(" + userId + ")";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertDefaultChartSettings(int userId)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertDefaultChartSettings(int userId)' method");
        return sqlQuery;
    }

    //Get user details
    public String sqlSelectAllUsers() {
        logManager.logInfo("Entering 'sqlSelectAllUsers()' method");
        try {
            sqlQuery = "SELECT first_name AS fname,last_name AS lname,username AS uname,gender,organisation AS org,role_name AS role  "
                    + "FROM " + databaseName + ".users INNER JOIN " + databaseName + ".user_roles ON users.role_id=user_roles.id";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectAllUsers()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectAllUsers()' method");
        return sqlQuery;
    }

    //Get user roles
    public String sqlSelectUserRoles() {
        logManager.logInfo("Entering 'sqlSelectUserRoles()' method");
        try {
            sqlQuery = "SELECT role_name as role FROM " + databaseName + ".user_roles ORDER BY role ASC ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectUserRoles()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectUserRoles()' method");
        return sqlQuery;
    }

    //Get languages
    public String sqlSelectLanguages() {
        logManager.logInfo("Entering 'sqlSelectLanguages()' method");
        try {
            sqlQuery = "SELECT language_name as lang FROM " + databaseName + ".language ORDER BY lang ASC ";
        } catch (Exception exception) {
            logManager.logInfo("Exception was thrown and caught in 'sqlSelectLanguages()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectLanguages()' method");
        return sqlQuery;
    }

    //Get organisations
    public String sqlSelectOrganisations() {
        logManager.logInfo("Entering 'sqlSelectOrganisations()' method");
        try {
            sqlQuery = "SELECT organisation_name AS org FROM " + databaseName + ".organisation ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectOrganisations()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectOrganisations()' method");
        return sqlQuery;
    }

    //Get job function
    public String sqlSelectJobFunction() {
        logManager.logInfo("Entering 'sqlSelectJobFunction()' method");
        try {
            sqlQuery = "SELECT job_function_name AS job FROM " + databaseName + ".job_function ";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectJobFunction()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectJobFunction()' method");
        return sqlQuery;
    }
    //Save new organisation

    public String sqlInsertOrganisation(String organisationName, String description) {
        logManager.logInfo("Entering 'sqlInsertOrganisation(String organisationName, String description)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".organisation(organisation_name,description) "
                    + " VALUES('" + organisationName + "','" + description + "')";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertOrganisation(String organisationName, String description)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertOrganisation(String organisationName, String description)' method");
        return sqlQuery;
    }

    //Save new job function
    public String sqlInsertJobFunction(String jobFunction, String description) {
        logManager.logInfo("Entering 'sqlInsertJobFunction(String jobFunction, String description)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".job_function(job_function_name,description) "
                    + " VALUES('" + jobFunction + "','" + description + "')";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertJobFunction(String jobFunction, String description)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertJobFunction(String jobFunction, String description)' method");
        return sqlQuery;
    }

    //Get  user details 
    public String sqlSelectUsers(int userId) {
        logManager.logInfo("Entering 'sqlSelectUsers(int userId)' method");
        try {
            sqlQuery = "SELECT first_name AS fname, last_name AS lname,middle_name AS mname,role_name AS rname,"
                    + " organisation_name AS org,language_name AS langname, job_function_name AS job_func, is_active AS active, "
                    + "email, gender FROM " + databaseName + ".users INNER JOIN "
                    + databaseName + ".user_roles ON users.role_id=user_roles.id INNER JOIN "
                    + databaseName + ".organisation ON organisation.id=users.organisation_id INNER JOIN "
                    + databaseName + ".job_function ON job_function.id=users.job_function_id INNER JOIN "
                    + databaseName + ".language AS lang ON users.language_id=lang.id WHERE users.id=" + userId;
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectUsers(int userId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectUsers(int userId)' method");
        return sqlQuery;
    }

    //Get all users
    public String sqlSelectUsers() {
        logManager.logInfo("Entering 'sqlSelectUsers()' method");
        try {
            sqlQuery = "SELECT users.id as user_id, first_name AS fname, last_name AS lname,middle_name AS mname,role_name AS rname,"
                    + " organisation_name AS org,language_name AS langname, job_function_name AS job_func, is_active AS active, "
                    + "email, gender FROM " + databaseName + ".users INNER JOIN "
                    + databaseName + ".user_roles ON users.role_id=user_roles.id INNER JOIN "
                    + databaseName + ".organisation ON organisation.id=users.organisation_id INNER JOIN "
                    + databaseName + ".job_function ON job_function.id=users.job_function_id INNER JOIN "
                    + databaseName + ".language as lang ON users.language_id=lang.id ORDER BY  user_id DESC";
            // System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectUsers()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectUsers()' method");
        return sqlQuery;
    }

    //Save new job function
    public String sqlInsertNewUser(String firstName, String lastName, String middleName, String userName, String password, String gender,
            String email, int organisationId, int roleId, int jobFunctionId, int languageId) {
        logManager.logInfo("Entering 'sqlInsertNewUser(String firstName, String lastName,String middleName, String userName,String password, String gender,"
                + "String email, int organisationId, int roleId, int jobFunctionId, int languageId)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".users(first_name,last_name,middle_name,username,passwrd,gender,email,organisation_id"
                    + ",role_id,job_function_id,language_id) VALUES('" + firstName + "','" + lastName + "','" + middleName + "',"
                    + "'" + userName + "',SHA1('" + password + "'),'" + gender + "',,'" + email + "'" + organisationId + "," + roleId + ","
                    + jobFunctionId + "," + languageId + ")";

        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertNewUser(String firstName, String lastName,String middleName, String userName,String password, String gender,"
                    + "String email, int organisationId, int roleId, int jobFunctionId, int languageId)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertNewUser(String firstName, String lastName,String middleName, String userName,String password, String gender,"
                + "String email, int organisationId, int roleId, int jobFunctionId, int languageId)' method");
        return sqlQuery;
    }

    //Get organisation id
    public String sqlSelectOrganisationId(String organisationName) {
        logManager.logInfo("Entering 'sqlSelectOrganisationId(String organisationName)' method");
        try {
            sqlQuery = "SELECT organisation.id AS org_id FROM " + databaseName + ".organisation WHERE organisation_name='" + organisationName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectOrganisationId(String organisationName)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectOrganisationId(String organisationName)' method");
        return sqlQuery;
    }

    //Get language id
    public String sqlSelectLanguageId(String languageName) {
        logManager.logInfo("Entering 'sqlSelectLanguageId(String languageName)' method");
        try {
            sqlQuery = "SELECT language.id AS lang_id FROM " + databaseName + ".language WHERE language_name='" + languageName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectLanguageId(String languageName)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectLanguageId(String languageName)' method");
        return sqlQuery;
    }

    //Get job function id
    public String sqlSelectJobFunctionId(String jobFunctionName) {
        logManager.logInfo("Entering 'sqlSelectJobFunctionId(String jobFunctionName)' method");
        try {
            sqlQuery = "SELECT job_function.id AS job_funct_id FROM " + databaseName + ".job_function WHERE job_function_name='"
                    + jobFunctionName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectJobFunctionId(String jobFunctionName)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectJobFunctionId(String jobFunctionName)' method");
        return sqlQuery;
    }

    //Get user role id
    public String sqlSelectRoleId(String roleName) {
        logManager.logInfo("Entering 'sqlSelectRoleId(String roleName)' method");
        try {
            sqlQuery = "SELECT user_roles.id AS role_id FROM " + databaseName + ".user_roles WHERE role_name='" + roleName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectRoleId(String roleName)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectRoleId(String roleName)' method");
        return sqlQuery;
    }

    //Reset password
    public String sqlUpdatePassword(String password, int userId) {
        logManager.logInfo("Entering 'sqlUpdatePassword(String password, int userId)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".users SET passwrd=SHA1('" + password + "') WHERE users.id=" + userId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlUpdatePassword(String password, int userId)' method");
        }
        logManager.logInfo("Exiting 'sqlUpdatePassword(String password, int userId)' method");
        return sqlQuery;
    }

    //Get username
    public String sqlSelectUsername(int userId) {
        logManager.logInfo("Entering 'sqlSelectUsername(int userId)' method");
        try {
            sqlQuery = "SELECT users.username AS usr FROM " + databaseName + ".users WHERE users.id = " + userId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectUsername(int userId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectUsername(int userId)' method");
        return sqlQuery;
    }

    //Set user inactive
    public String sqlUpdateUserSetInactive(int userId) {
        logManager.logInfo("Entering 'sqlUpdateUserSetInactive(int userId)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".users SET is_active=0 WHERE users.id=" + userId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlUpdateUserSetInactive(int userId)' method");
        }
        logManager.logInfo("Exiting 'sqlUpdateUserSetInactive(int userId)' method");
        return sqlQuery;
    }

    //Get user id    
    public String sqlSelectUserId(String username) {
        logManager.logInfo("Entering 'sqlSelectUserId(String username)' method");
        try {
            sqlQuery = "SELECT users.id AS user_id FROM " + databaseName + ".users WHERE username='" + username + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectUserId(String username)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectUserId(String username)' method");
        return sqlQuery;
    }

    //Get all indicators with details    
    public String sqlSelectAllIndicators() {
        logManager.logInfo("Entering 'sqlSelectAllIndicators()' method");
        try {
            sqlQuery = "SELECT indicators.id AS ind_id, indicator_business_name AS indicator, active, units.unit_name AS unit,"
                    + " indicator_categories.category_name AS category, application.application AS availability "
                    + " FROM " + databaseName + ".indicators "
                    + " INNER JOIN " + databaseName + ".indicator_categories ON indicators.indicator_category_id=indicator_categories.id "
                    + " INNER JOIN " + databaseName + ".application ON indicators.application_id=application.id "
                    + " LEFT OUTER JOIN " + databaseName + ".units ON indicators.unit_id=units.id "
                    + " ORDER BY indicators.id";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectAllIndicators()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectAllIndicators()' method");
        return sqlQuery;
    }

    //Get all indicator 
    public String sqlSelectIndicator(int indicatorId) {
        logManager.logInfo("Entering 'sqlSelectIndicator(int indicatorId)' method");
        try {
            sqlQuery = "SELECT indicators.id AS ind_id, indicator_name as indic_name, indicator_business_name AS indicator, "
                    + " units.unit_name AS unit,"
                    + " indicator_categories.category_name AS category, indicators.application_id AS availability "
                    + " FROM " + databaseName + ".indicators "
                    + " INNER JOIN " + databaseName + ".indicator_categories ON indicators.indicator_category_id=indicator_categories.id "
                    + " LEFT OUTER JOIN " + databaseName + ".units ON indicators.unit_id=units.id "
                    + " WHERE indicators.id=" + indicatorId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectIndicator(int indicatorId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectIndicator(int indicatorId)' method");
        return sqlQuery;
    }

    //Get indicator units    
    public String sqlSelectIndicatorUnits() {
        logManager.logInfo("Entering 'sqlSelectIndicatorUnits()' method");
        try {
            sqlQuery = "SELECT units.unit_name  AS unit FROM " + databaseName + ".units";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectIndicatorUnits()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectIndicatorUnits()' method");
        return sqlQuery;
    }

    //Get indicator categories 
    public String sqlSelectIndicatorCategories() {
        logManager.logInfo("Entering 'sqlSelectIndicatorCategories()' method");
        try {
            sqlQuery = "SELECT category_name AS cat_name FROM " + databaseName + ".indicator_categories";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectIndicatorCategories()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectIndicatorCategories()' method");
        return sqlQuery;
    }

    //Insert new indicator
    public String queryInsertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId) {
        logManager.logInfo("Entering 'queryInsertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".indicators(indicator_name,indicator_business_name,"
                    + " unit_id,indicator_category_id,application_id) "
                    + " VALUES('" + indicatorName + "','" + indicatorBusinessName + "'," + unitId + "," + categoryId + "," + applicationId + ")";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryInsertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId)' method");
        }
        logManager.logInfo("Exiting 'queryInsertIndicator(String indicatorName, String indicatorBusinessName, int unitId, int categoryId, int applicationId)' method");
        return sqlQuery;
    }

    //Get indicator unit id 
    public String selectIndicatorUnitId(String unit) {
        logManager.logInfo("Entering 'selectIndicatorUnitId(String unit)' method");
        try {
            sqlQuery = "SELECT units.id AS unit_id FROM " + databaseName + ".units WHERE units.unit_name='" + unit + "'";
        } catch (Exception exception) {
            logManager.logError("Exception thrown and caught in 'selectIndicatorUnitId(String unit)' method");
        }
        logManager.logInfo("Exiting 'selectIndicatorUnitId(String unit)' method");
        return sqlQuery;
    }

    //Get indicator category id 
    public String selectIndicatorCategoryId(String category) {
        logManager.logInfo("Entering 'selectIndicatorCategoryId(String category)' method");
        try {
            sqlQuery = "SELECT indicator_categories.id AS cat_id FROM " + databaseName + ".indicator_categories "
                    + " WHERE indicator_categories.category_name='" + category + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'selectIndicatorCategoryId(String category)' method");
        }
        logManager.logInfo("Exiting 'selectIndicatorCategoryId(String category)' method");
        return sqlQuery;
    }

    //Add new indicator category
    public String sqlInsertIndicatorCategory(String indicatorCategoryName, String description) {
        logManager.logInfo("Entering 'sqlInsertIndicatorCategory(String indicatorCategoryName, String description)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".indicator_categories(category_name,description) "
                    + " VALUES('" + indicatorCategoryName + "','" + description + "')";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertIndicatorCategory(String indicatorCategoryName, String description)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertIndicatorCategory(String indicatorCategoryName, String description)' method");
        return sqlQuery;
    }

    //Add new indicator unit 
    public String sqlInsertIndicatorUnit(String indicatorUnitName, String description) {
        logManager.logInfo("Entering 'sqlInsertIndicatorUnit(String indicatorUnitName, String description)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".units(unit_name,description) "
                    + " VALUES('" + indicatorUnitName + "','" + description + "')";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertIndicatorUnit(String indicatorUnitName, String description)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertIndicatorUnit(String indicatorUnitName, String description)' method");
        return sqlQuery;
    }

    //Set indicator inactive
    public String sqlUpdateIndicatorSetInactive(int indicatorId) {
        logManager.logInfo("Entering 'sqlUpdateIndicatorSetInactive(int indicatorId)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".indicators SET active=0 WHERE indicators.id=" + indicatorId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlUpdateIndicatorSetInactive(int indicatorId)' method");
        }
        logManager.logInfo("Exiting 'sqlUpdateIndicatorSetInactive(int indicatorId)' method");
        return sqlQuery;
    }

    //Add new market
    public String queryInsertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId) {
        logManager.logInfo("Entering 'queryInsertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".markets(market_name,field_analyst_id,"
                    + " district_id,system_id) "
                    + " VALUES('" + marketName + "'," + fieldAnalystId + "," + districtId + "," + marketTypeId + ")";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryInsertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        }
        logManager.logInfo("Exiting 'queryInsertMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        return sqlQuery;
    }
    
    //Edit new market
    public String queryEditMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId, int marketId) {
        logManager.logInfo("Entering 'queryEditMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        try {
            sqlQuery = "update "+databaseName+".markets set district_id="+districtId+" , field_analyst_id="+fieldAnalystId+" , system_id="+marketTypeId+" , market_name='"+marketName+"' where markets.id="+marketId;
            //sqlQuery = "update "+databaseName+".markets set field_analyst_id="+fieldAnalystId+" and system_id="+marketTypeId+" and market_name='"+marketName+"' where markets.id="+marketId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'queryEditMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        }
        logManager.logInfo("Exiting 'queryEditMarket(String marketName, int fieldAnalystId, int districtId, int marketTypeId)' method");
        return sqlQuery;
    }    

    //Add field analyst
    public String sqlInsertStaff(String staffName) {
        logManager.logInfo("Entering 'sqlInsertStaff(String staffName)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".staff(staff_name) "
                    + " VALUES('" + staffName + "')";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertStaff(String staffName)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertStaff(String staffName)' method");
        return sqlQuery;
    }

    //Add staff 
    public String sqlInsertFieldAnalyst(int staffId) {
        logManager.logInfo("Entering 'sqlInsertFieldAnalyst(int staffId)' method");
        try {
            sqlQuery = "INSERT INTO " + databaseName + ".field_analyst(staff_id) "
                    + " VALUES(" + staffId + ")";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlInsertFieldAnalyst(int staffId)' method");
        }
        logManager.logInfo("Exiting 'sqlInsertFieldAnalyst(int staffId)' method");
        return sqlQuery;
    }

    //Get staff id 
    public String selectStaffId(String staffName) {
        logManager.logInfo("Entering 'selectStaffId(String staffName)' method");
        try {
            sqlQuery = "SELECT staff.id AS staff_id FROM " + databaseName + ".staff WHERE staff.staff_name='" + staffName + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'selectStaffId(String staffName)' method");
        }
        logManager.logInfo("Exiting 'selectStaffId(String staffName)' method");
        return sqlQuery;
    }

    //Get field analysts    
    public String sqlSelectFieldAnalysts() {
        logManager.logInfo("Entering 'sqlSelectFieldAnalysts()' method");
        try {
            sqlQuery = "SELECT staff.staff_name  AS staff_name FROM " + databaseName + ".staff";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectFieldAnalysts()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectFieldAnalysts()' method");
        return sqlQuery;
    }

    //Get districts    
    public String sqlSelectDistricts() {
        logManager.logInfo("Entering 'sqlSelectDistricts()' method");
        try {
            sqlQuery = "SELECT district.district_name  AS dist_name FROM " + databaseName + ".district";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectDistricts()' method");
        }
        logManager.logInfo("Exiting 'sqlSelectDistricts()' method");
        return sqlQuery;
    }

    //Get district id 
    public String selectDistrictId(String district) {
        logManager.logInfo("Entering 'selectDistrictId(String district)' method");
        try {
            sqlQuery = "SELECT district.id AS dist_id FROM " + databaseName + ".district "
                    + " WHERE district.district_name='" + district + "'";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'selectDistrictId(String district)' method");
        }
        logManager.logInfo("Exiting 'selectDistrictId(String district)' method");
        return sqlQuery;
    }

    //Set market inactive
    public String sqlUpdateMarketSetInactive(int marketId) {
        logManager.logInfo("Entering 'sqlUpdateMarketSetInactive(int marketId)' method");
        try {
            sqlQuery = "UPDATE " + databaseName + ".markets SET active=0 WHERE markets.id=" + marketId;
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlUpdateMarketSetInactive(int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlUpdateMarketSetInactive(int marketId)' method");
        return sqlQuery;
    }

    //Get all markets with details
    public String querySelectMarkets() {
        logManager.logInfo("Entering 'querySelectMarkets()' method");
        try {
            sqlQuery = "SELECT markets.id AS market_id, markets.market_name AS market,district.district_name,"
                    + " system.system_name AS system, staff.staff_name, markets.active FROM " + databaseName + ".markets "
                    + " INNER JOIN " + databaseName + ".district ON markets.district_id=district.id "
                    + " INNER JOIN " + databaseName + ".system ON markets.system_id=system.id "
                    + " INNER JOIN " + databaseName + ".field_analyst ON markets.field_analyst_id=field_analyst.id "
                    + " INNER JOIN " + databaseName + ".staff ON field_analyst.staff_id=staff.id "
                    + " ORDER BY markets.id";
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'querySelectMarkets()' method");
        }
        logManager.logInfo("Exiting 'querySelectMarkets()' method");
        return sqlQuery;
    }

    //Get market 
    public String sqlSelectMarket(int marketId) {
        logManager.logInfo("Entering 'sqlSelectMarket(int marketId)' method");
        try {
            sqlQuery = "SELECT markets.market_name AS market,district.district_name, "
                    + " markets.system_id AS system, staff.staff_name, markets.active "
                    + " FROM " + databaseName + ".markets "
                    + " INNER JOIN " + databaseName + ".district ON markets.district_id=district.id  "
                    + " INNER JOIN " + databaseName + ".field_analyst ON markets.field_analyst_id=field_analyst.id  "
                    + " INNER JOIN " + databaseName + ".staff ON field_analyst.staff_id=staff.id  "
                    + " WHERE markets.id = " + marketId;
            //System.out.println(sqlQuery);
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'sqlSelectMarket(int marketId)' method");
        }
        logManager.logInfo("Exiting 'sqlSelectMarket(int marketId)' method");
        return sqlQuery;
    }
    
    //Get SQL table data
    public String sqlSelectTableData(){
        try{
            
        }catch(Exception exception){}
        return sqlQuery;
    }
}
