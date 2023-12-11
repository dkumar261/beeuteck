package com.forecastera.service.projectmanagement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.projectmanagement.dto.utilityClass.GetDateRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
public class ProjectUtils {

    public static final String PROJECT_ZERO_PROGRESS = "0";

    public static final int COMPONENT_TYPE = 0;
    public static final int FIELD_TYPE = 2;
    public static final int BUTTON_TYPE = 1;
    public static final int CUSTOM_TYPE = 3;
    public static final int HIDE_PICKLIST_TYPE = 4;

    public static final String SUCCESS = "SUCCESS";
    public static final String NO_DATA = "NO DATA AVAILABLE";

    public static final int MAX_FTE = 3;
    public static final int MIN_FTE = 0;

    public static final Integer UNDEFINED_UTILIZATION_STATUS_ID = 0;
    public static final String UNDEFINED_UTILIZATION_NAME = "Undefined";
    public static final String UNDEFINED_UTILIZATION_COLOR = "#000000";

    public static final String ALLOCATION_FREQUENCY_DAILY = "Daily";
    public static final String ALLOCATION_FREQUENCY_WEEKLY = "Weekly";
    public static final String ALLOCATION_FREQUENCY_MONTHLY = "Monthly";
    public static final String ALLOCATION_FREQUENCY_INVALID = "Invalid Allocation Frequency";

    public static final String REQUEST_RESOURCE_NEW_REQUEST = "New request";
    public static final String REQUEST_RESOURCE_STATUS_OPEN = "Open";
    public static final String REQUEST_RESOURCE_STATUS_RELEASED = "Released";
    public static final String REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED = "Partially-Fulfilled";
    public static final String REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED = "Completely-Fulfilled";
    public static final String REQUEST_RESOURCE_STATUS_OVER_FULFILLED = "Over-Fulfilled";
    public static final String REQUEST_RESOURCE_FTE_PERIOD_INCREASED = "FTE requested period increased";
    public static final String REQUEST_RESOURCE_FTE_PERIOD_DECREASED = "FTE requested period decreased";
    public static final String REQUEST_RESOURCE_AVG_REQUESTED_FTE_INCREASED = "Avg requested FTE increased by ";
    public static final String REQUEST_RESOURCE_AVG_REQUESTED_FTE_DECREASED = "Avg requested FTE decreased by ";
    public static final String REQUEST_RESOURCE_RELEASED_FROM_PROJECT = "Resource released from project";

    public static final String FIELD_PROJECT_PROGRESS = "Progress";
    public static final String FIELD_PROJECT_DAYS_LEFT = "Days Left";
    public static final String FIELD_PROJECT_STATUS = "Status";
    public static final String FIELD_PROJECT_PRIORITY = "Priority";
    public static final String FIELD_PROJECT_PROJECT_TYPE = "Project Type";
    public static final String FIELD_PROJECT_PROJECT_OWNER = "Project Owner";
    public static final String FIELD_PROJECT_CREATED_BY = "Created By";
    public static final String FIELD_PROJECT_CREATED_DATE = "Created Date";
    public static final String FIELD_PROJECT_MODIFIED_BY = "Last Modified By";
    public static final String FIELD_PROJECT_MODIFIED_DATE = "Last Modified Date";
    public static final String FIELD_PROJECT_PROJECT_START_DATE = "Start Date";
    public static final String FIELD_PROJECT_PROJECT_END_DATE = "End Date";
    public static final String FIELD_PROJECT_PROJECT_ACTUAL_START_DATE = "Actual Start Date";
    public static final String FIELD_PROJECT_PROJECT_ACTUAL_END_DATE = "Actual End Date";
    public static final String FIELD_PROJECT_NAME = "Project Name";
    public static final String FIELD_PROJECT_DEPARTMENT = "Department";

    public static final Map<String, Integer> UNEDITABLE_FIELDS;
    static {

        UNEDITABLE_FIELDS = Map.of(
                ProjectUtils.FIELD_PROJECT_DAYS_LEFT, 0,
                ProjectUtils.FIELD_PROJECT_CREATED_BY, 0,
                ProjectUtils.FIELD_PROJECT_CREATED_DATE, 0,
                ProjectUtils.FIELD_PROJECT_MODIFIED_BY, 0,
                ProjectUtils.FIELD_PROJECT_MODIFIED_DATE, 0
        );
    }

    public static final int EXPECTED_NUMBER_OF_RECORDS = 1000;

    public static final String PROJECT_CREATED_SUCCESSFULLY = "Project Created Successfully";

    public static final String ERROR_WHILE_CREATING_EXCEL_FILE = "Error occurred while creating the excel file";

    public static final String ERROR_WHILE_PROCESSING_UPLOADED_FILE = "Error occurred while processing the uploaded file";

    public static final String INCORRECT_EXCEL_FILE = "Excel file uploaded is incorrect";

    public static final String PROJECT_OWNER_EMAIL_ID = "Project Owner's Email";

    public static final String START_DATE_DATE_OF_JOIN_MESSAGE = "The allocation start date is before the resource’s date of joining";
    public static final String START_DATE_PROJECT_START_DATE_MESSAGE = "The allocation start date is before the project’s start date";
    public static final String END_DATE_LAST_WORKING_DATE_MESSAGE = "The allocation end date is after the resource’s last working date";
    public static final String END_DATE_PROJECT_END_DATE_MESSAGE = "The allocation end date is after the project’s end date";

    public static final String ALLOCATION_OUTSIDE_DATE_RANGE = "One or more resources are allocated to project outside of project duration";
    public static final String RESOURCE_JOINED_AFTER_PROJECT_END_DATE = "One or more of selected resources have date of join after project end date";
    public static final String RESOURCE_LEFT_BEFORE_PROJECT_START_DATE = "One or more of selected resources have last working date before project start date";

    public static final String ESSENTIAL_FIELDS_MISSING = "Data missing for essential fields";

    public static final String PICKLIST_MORE_THAN_ONE_VALUE = "Picklist field type has more than one value";

    public static final String FIELD_ID_MISMATCH = "Field id is incorrect or does not exist";

    public static final String INCORRECT_EMAIL_FORMAT = "Incorrect email format";

    public static final String PROJECT_START_DATE_AFTER_END_DATE = "Project start date is after project end date";
    public static final String PROJECT_ACTUAL_START_DATE_AFTER_ACTUAL_END_DATE = "Project actual start date is after project actual end date";

    public static final String PROJECT_START_END_DATE_EMPTY = "Project start date/end date cannot be empty";

    public static final String PROJECT_PROJECT_NAME_DUPLICATE = "Project name already exist";

    public static final String FIELD_TYPE_PICKLIST = "Picklist";
    public static final String FIELD_TYPE_PICKLIST_MULTISELECT = "Picklist-Multiselect";
    public static final String FIELD_TYPE_DATE = "Date";
    public static final String FIELD_TYPE_EMAIL = "Email";
    public static final String FIELD_TYPE_NUMBER = "Number";
    public static final String FIELD_TYPE_CURRENCY = "Currency";
    public static final String FIELD_TYPE_TEXT = "Text";
    public static final String FIELD_TYPE_TEXT_AREA = "Textarea";

    public static final Map<String, String> VALID_DATE_FORMAT;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("DD/MM/YY", "dd/MM/yy");
        map.put("DD-MM-YY", "dd-MM-yy");
        map.put("DD/MM/YYYY", "dd/MM/yyyy");
        map.put("DD-MM-YYYY", "dd-MM-yyyy");
        map.put("MM/DD/YY", "MM/dd/yy");
        map.put("MM-DD-YY", "MM-dd-yy");
        map.put("MM/DD/YYYY", "MM/dd/yyyy");
        map.put("MM-DD-YYYY", "MM-dd-yyyy");
        map.put("YYYY/MM/DD", "yyyy/MM/dd");
        map.put("YYYY-MM-DD", "yyyy-MM-dd");

        VALID_DATE_FORMAT = Collections.unmodifiableMap(map);
    }


    public static String removeAllExtraSpace(String s){
        s = s.trim().replaceAll("\\s+", " ");
        return s;
    }

    public static boolean isEndDateBeforeStartDate(Date startDate, Date endDate){
        try {
            return endDate.compareTo(startDate) < 0;
        }
        catch(Exception e){
            return true;
        }
    }

    public static String returnOverlappingDateRangeErrorMessage(List<String> resourceNames){
        int recordCount = resourceNames.size();
        String message = null;
        if(recordCount == 1) {
            String names = String.join(", ", resourceNames);
            message = "For resource " + names + " newly allocated dates overlap with existing allocated dates on the same project";
        }
        else {
            String lastName = resourceNames.get(recordCount-1);
            resourceNames.remove(recordCount-1);
            String names = String.join(", ", resourceNames);
            names = names + " and " + lastName;
            message = "For resources " + names + " newly allocated dates overlap with existing allocated dates on the same project";
        }
        return message;
    }

    public static String outsideProjectDateRangeAllocation(List<String> resourceNames){
        int recordCount = resourceNames.size();
        String message = null;
        if(recordCount == 1) {
            String names = String.join(", ", resourceNames);
            message = "Please de-allocate resource " + names + " on appropriate dates before changing the start date/actual start date or end date/actual end date of the project";
        }
        else {
            String lastName = resourceNames.get(recordCount-1);
            resourceNames.remove(recordCount-1);
            String names = String.join(", ", resourceNames);
            names = names + " and " + lastName;
            message = "Please de-allocate resources " + names + " on appropriate dates before changing the start date/actual start date or end date/actual end date of the project";
        }
        return message;
    }

    public static Date convertDateToUTCTimeZoneDate(Date date){
        try {
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
            ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of("UTC"));
            return java.sql.Date.valueOf(adminSelectedTimeZone.toLocalDate());
        }
        catch(Exception e){
            return date;
        }
    }

    public static Date convertStringDateToUTCTimeZoneDate(String date){
        try {
            LocalDate localDate = LocalDate.parse(date);
            ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of("UTC"));
            return java.sql.Date.valueOf(adminSelectedTimeZone.toLocalDate());
        }
        catch(Exception e){
            return null;
        }
    }

    public static Date convertDateToAdminTimeZoneDate(Date date, String timeZone){
        try {
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
            ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
            return java.sql.Date.valueOf(adminSelectedTimeZone.toLocalDate());
        }
        catch(Exception e){
            return date;
        }
    }

    public static String convertDateToAdminTimeZoneStringDate(Date date, String timeZone){
        try {
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
            ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
            return String.valueOf(java.sql.Date.valueOf(adminSelectedTimeZone.toLocalDate()));
        }
        catch(Exception e){
            return "";
        }
    }

    public static String convertStringDateToAdminTimeZoneStringDate(String date, String timeZone){
        try {
            LocalDate localDate = LocalDate.parse(date);
            ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
            return String.valueOf(java.sql.Date.valueOf(adminSelectedTimeZone.toLocalDate()));
        }
        catch(Exception e){
            return "";
        }
    }
//
//    public static LocalDate convertDateToAdminSelectedTimeZone(Date date, String timeZone){
//        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
//        ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
//        return adminSelectedTimeZone.toLocalDate();
//    }

//    public static String formatDateToStringDate(Date date, String timeZone, String format){
//        try {
//            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
//            //ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
//            //return adminSelectedTimeZone.toLocalDate().format(formatter);
//            return localDate.format(formatter);
//        }
//        catch (Exception e){
//            return "";
//        }
//    }
//
//    public static String formatStringDateToStringDate(String date, String timeZone, String format){
//        try {
//            LocalDate localDate = LocalDate.parse(date);
//            //ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of(timeZone));
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
//            //return adminSelectedTimeZone.toLocalDate().format(formatter);
//            return localDate.format(formatter);
//        }
//        catch (Exception e){
//            return "";
//        }
//    }

//    public static Date formatStringDateToDate(String stringDate){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(stringDate, formatter);
//        ZonedDateTime adminSelectedTimeZone = localDate.atStartOfDay(ZoneId.of("UTC"));
//        return java.sql.Date.valueOf(adminSelectedTimeZone.toLocalDate());
//    }

    public static final List<String> PROJECT_FIELD_NAMES_DATA
            = List.of(new String[]{
            ProjectUtils.FIELD_PROJECT_DAYS_LEFT,
            ProjectUtils.FIELD_PROJECT_CREATED_BY,
            ProjectUtils.FIELD_PROJECT_CREATED_DATE,
            ProjectUtils.FIELD_PROJECT_MODIFIED_BY,
            ProjectUtils.FIELD_PROJECT_MODIFIED_DATE
    });

    public static final String INTERNAL_LABOR_EXPENSES = "Internal Labor Expenses";
    public static final String TOTAL_EMPLOYEE_SALARIES = "Total Employee Salaries";
    public static final String EMPTY_INTERNAL_OTHER_EXPENSES = "Other Internal Expenses";
    public static final String TOTAL_INTERNAL_LABOR_EXPENSES = "Total Internal Labor Expenses";

    public static final String VENDOR_LABOR_EXPENSES = "Vendor Labor Expenses";
    public static final String T_AND_M = "T&M";
    public static final String OTHER_VENDOR_EXPENSE = "Other Vendor Expense";
    public static final String TOTAL_VENDOR_LABOR_EXPENSES = "Total Vendor Labor Expenses";

    public static final String TOTAL_BUDGET_EXPENSES = "Total Budget Spend - Project";
    public static final String TOTAL_FORECAST_EXPENSES = "Total Forecast Spend - Project";

    public static final String TOTAL_CAPEX_RELATED_EXPENSES = "Total Capex Related Expenses";
    public static final String EMPTY_CAPEX_OTHER_EXPENSES = "Other Capex Expenses";
    public static final String TOTAL_CAPEX_EXPENSES_TOTAL = "Total Capex";

    public static final String ONSITE = "Onsite";
    public static final String OFFSHORE = "Offshore";

    public static final int LOOP_START_POINT = 0;
    public static final int LOOP_END_POINT = 11;

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

//    public static Double roundToOneDecimalPlace(Double data){
//        return Math.round(data*10.0)/10.0;
//    }

    public static GetDateRange removeTimeData(GetDateRange getDateRange){
        LocalDate localStartDate, localEndDate;
        Date startDate = null, endDate = null;

        if(getDateRange.getStartDate()!=null) {
            localStartDate = new java.sql.Date(getDateRange.getStartDate().getTime()).toLocalDate();
            startDate = java.sql.Date.valueOf(localStartDate);
        }
        if(getDateRange.getEndDate()!=null) {
            localEndDate = new java.sql.Date(getDateRange.getEndDate().getTime()).toLocalDate();
            endDate = java.sql.Date.valueOf(localEndDate);
        }

        return new GetDateRange(startDate, endDate);
    }

    public static Double roundToTwoDecimalPlace(Double data){
        return Math.round(data*100.0)/100.0;
    }

    public static Integer numberOfWorkingDays(LocalDate localStartDate, LocalDate localEndDate){

        Integer workingDays = 0;
        LocalDate currentDate = localStartDate;
        while (!currentDate.isAfter(localEndDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Check if the day is Saturday or Sunday
            if ((dayOfWeek != DayOfWeek.SATURDAY) && (dayOfWeek != DayOfWeek.SUNDAY) ) {
                workingDays++;
            }

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    /**
     * convert into Model object
     */
    public static <S> S toModel(Object fromObject, Class<S> toClass,
                                ObjectMapper objectMapper) {

        try {
            return objectMapper.convertValue(fromObject, toClass);
        } catch (Exception e) {
            System.out.println("Something Went Wrong: " + e);
        }
        return null;
    }

    public static String getFile(String fileName) {
        InputStream inputStream = ProjectUtils.class.getClassLoader().getResourceAsStream("queries/" + fileName);
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
