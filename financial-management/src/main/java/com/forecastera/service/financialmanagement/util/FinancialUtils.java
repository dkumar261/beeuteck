package com.forecastera.service.financialmanagement.util;/*
 * @Author Uttam Kachhad
 * @Create 05-06-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FinancialUtils {

    public static final String SUCCESS = "SUCCESS";
    public static final String NO_DATA = "NO DATA AVAILABLE";

    public static final String PICKLIST = "Picklist";
    public static final String PICKLIST_MULTI = "Picklist-Multiselect";


    public static final Integer DIRECT_EMPLOYEE_DETAILS = 1;
    public static final Integer OTHER_DIRECT_IN_HOUSE_COST = 2;
    public static final Integer VENDOR_EMPLOYEE_DETAILS = 3;
    public static final Integer VENDOR_RELATED_COST = 4;
    public static final Integer CAPEX_RELATED_EXPENSE = 5;

    public static final int NO_OF_RECORDS_TO_SEND = 5;

    public static final String FINANCE_FIRST_NAME = "First Name";
    public static final String FINANCE_LAST_NAME = "Last Name";
    public static final String FINANCE_LOCATION = "Location";
    public static final String FINANCE_DESIGNATION = "Designation";
    public static final String FINANCE_PROJECT = "Project";

    public static final String FIELD_TYPE_DATE = "Date";

    public static String formatDateToStringDate(Date date, String timeZone, String format){
        try {
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
            //ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            //return adminSelectedTimeZone.toLocalDate().format(formatter);
            return localDate.format(formatter);
        }
        catch (Exception e){
            return "";
        }
    }

    public static String formatStringDateToStringDate(String date, String timeZone, String format){
        try {
            LocalDate localDate = LocalDate.parse(date);
            //ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            //return adminSelectedTimeZone.toLocalDate().format(formatter);
            return localDate.format(formatter);
        }
        catch (Exception e){
            return "";
        }
    }

    public static final String JANUARY = "jan";
    public static final String FEBRUARY = "feb";
    public static final String MARCH = "mar";
    public static final String APRIL = "apr";
    public static final String MAY = "may";
    public static final String JUNE = "jun";
    public static final String JULY = "jul";
    public static final String AUGUST = "aug";
    public static final String SEPTEMBER = "sep";
    public static final String OCTOBER = "oct";
    public static final String NOVEMBER = "nov";
    public static final String DECEMBER = "dec";

    public static final String ONSITE = "Onsite";
    public static final String OFFSHORE = "Offshore";

    public static final int FIRST_QUARTER = 1;
    public static final int SECOND_QUARTER = 2;
    public static final int THIRD_QUARTER = 3;
    public static final int FOURTH_QUARTER = 4;


    public static final String INTERNAL_LABOR_EXPENSES = "Internal Labor Expenses";
    public static final String TOTAL_EMPLOYEE_SALARIES = "Total Employee Salaries";
    public static final String EMPTY_INTERNAL_OTHER_EXPENSES = "Other Internal Expenses";
    public static final String TOTAL_INTERNAL_LABOR_EXPENSES = "Total Internal Labor Expenses";


    public static final String VENDOR_LABOR_EXPENSES = "Vendor Labor Expenses";
    public static final String T_AND_M = "T&M";
    public static final String OTHER_VENDOR_EXPENSE = "Other Vendor Expense";
    public static final String TOTAL_VENDOR_LABOR_EXPENSES = "Total Vendor Labor Expenses";

    public static final String TOTAL_BUDGET_EXPENSES = "Total Budget Spend";
    public static final String TOTAL_FORECAST_EXPENSES = "Total Forecast Spend";

    public static final String TOTAL_CAPEX_RELATED_EXPENSES = "Total Capex Related Expenses";
    public static final String EMPTY_CAPEX_OTHER_EXPENSES = "Other Capex Expenses";
    public static final String TOTAL_CAPEX_EXPENSES_TOTAL = "Total Capex";

    public static final Map<String, Integer> MONTH_MAP;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("January", 1);
        map.put("February", 2);
        map.put("March", 3);
        map.put("April", 4);
        map.put("May", 5);
        map.put("June", 6);
        map.put("July", 7);
        map.put("August", 8);
        map.put("September", 9);
        map.put("October", 10);
        map.put("November", 11);
        map.put("December", 12);

        MONTH_MAP = Collections.unmodifiableMap(map);
    }


    public static final Map<String, Integer> STRING_INTEGER_MONTH_MAP;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("jan", 1);
        map.put("feb", 2);
        map.put("mar", 3);
        map.put("apr", 4);
        map.put("may", 5);
        map.put("jun", 6);
        map.put("jul", 7);
        map.put("aug", 8);
        map.put("sep", 9);
        map.put("oct", 10);
        map.put("nov", 11);
        map.put("dec", 12);

        STRING_INTEGER_MONTH_MAP = Collections.unmodifiableMap(map);
    }

    public static final Map<Integer, String> INTEGER_STRING_MONTH_MAP;

    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Jan");
        map.put(2, "Feb");
        map.put(3, "Mar");
        map.put(4, "Apr");
        map.put(5, "May");
        map.put(6, "Jun");
        map.put(7, "Jul");
        map.put(8, "Aug");
        map.put(9, "Sep");
        map.put(10, "Oct");
        map.put(11, "Nov");
        map.put(12, "Dec");

        INTEGER_STRING_MONTH_MAP = Collections.unmodifiableMap(map);
    }

    public static Double roundToTwoDecimalPlace(Double data){
        return Math.round(data*100.0)/100.0;
    }

    public static <S> S toModel(Object fromObject, Class<S> toClass, ObjectMapper objectMapper) {

        try {
            return objectMapper.convertValue(fromObject, toClass);
        } catch (Exception e) {
            System.out.println("Something Went Wrong: " + e);
        }
        return null;
    }


    public static String getFile(String fileName) {
        InputStream inputStream = FinancialUtils.class.getClassLoader().getResourceAsStream("queries/" + fileName);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line + System.lineSeparator());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }
}
