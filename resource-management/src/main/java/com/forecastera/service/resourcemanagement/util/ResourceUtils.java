package com.forecastera.service.resourcemanagement.util;/*
 * @Author Uttam Kachhad
 * @Create 05-06-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.resourcemanagement.dto.utilityClass.GetDateRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ResourceUtils {

    public static final String SUCCESS = "SUCCESS";
    public static final String NO_DATA = "NO DATA AVAILABLE";

    public static final String USER_ROLE_MANAGER = "Manager";
    public static final String USER_ROLE_RESOURCE_MANAGER = "Resource Manager";

    public static final int COMPONENT_TYPE = 0;
    public static final int FIELD_TYPE = 2;
    public static final int BUTTON_TYPE = 1;
    public static final int CUSTOM_TYPE = 3;
    public static final int HIDE_PICKLIST_TYPE = 4;

    public static final Integer DIRECT_EMPLOYEE_DETAILS = 1;
    public static final Integer VENDOR_EMPLOYEE_DETAILS = 3;

    public static final String FIELD_RESOURCE_LWD_PLACEHOLDER = "2099-12-31";

    public static final String EMAIL_ALREADY_EXISTS = "Resource already exists";

    public static final String FIELD_RESOURCE_PICKLIST = "Picklist";
    public static final String FIELD_RESOURCE_ID = "Resource Id";
    public static final String FIELD_RESOURCE_STATUS = "Status";
    public static final String FIELD_RESOURCE_FIRST_NAME = "First Name";
    public static final String FIELD_RESOURCE_LAST_NAME = "Last Name";
    public static final String FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN = "Date Of Join";
    public static final String FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE = "Last Working Date";
    public static final String FIELD_RESOURCE_ALLOCATION_FTE = "Allocation(FTE)";
    public static final String FIELD_RESOURCE_EMAIL = "Email";
    public static final String FIELD_RESOURCE_ROLE = "Role";
    public static final String FIELD_RESOURCE_SKILL = "Skills";
    public static final String FIELD_RESOURCE_DEPARTMENT = "Department";
    public static final String FIELD_RESOURCE_LOCATION = "Location";
    public static final String FIELD_RESOURCE_REPORTING_MANAGER = "Reporting Manager";
    public static final String FIELD_RESOURCE_REPORTING_MANAGER_EMAIL = "Reporting Manager's Email";
    public static final String FIELD_RESOURCE_EMPLOYEE_LOCATION = "Employee Location";
    public static final String FIELD_RESOURCE_EMPLOYEE_LOCATION_ONSITE = "Onsite";
    public static final String FIELD_RESOURCE_EMPLOYEE_LOCATION_OFFSHORE = "Offshore";
    public static final String FIELD_RESOURCE_EMPLOYMENT_TYPE = "Employment Type";
    public static final String FIELD_RESOURCE_EMPLOYMENT_TYPE_IN_HOUSE = "In-House";
    public static final String FIELD_RESOURCE_EMPLOYMENT_TYPE_VENDOR = "Vendor";
    public static final String FIELD_RESOURCE_PHONE = "Phone";

    public static final List<String> RESOURCE_FIELD_NAMES_DATA
            = List.of(new String[]{
            ResourceUtils.FIELD_RESOURCE_FIRST_NAME,
            ResourceUtils.FIELD_RESOURCE_LAST_NAME,
            ResourceUtils.FIELD_RESOURCE_RESOURCE_DATE_OF_JOIN,
            ResourceUtils.FIELD_RESOURCE_RESOURCE_LAST_WORKING_DATE,
            ResourceUtils.FIELD_RESOURCE_ALLOCATION_FTE,
            ResourceUtils.FIELD_RESOURCE_STATUS
    });

    public static final Map<String, Integer> UNEDITABLE_FIELDS;
    static {

        UNEDITABLE_FIELDS = Map.of(
                ResourceUtils.FIELD_RESOURCE_ALLOCATION_FTE, 0,
                ResourceUtils.FIELD_RESOURCE_STATUS, 0
        );
    }

    public static final int EXPECTED_NUMBER_OF_RECORDS = 1000;

    public static final String RESOURCE_CREATED_SUCCESSFULLY = "Resource Created Successfully";

    public static final String ERROR_WHILE_CREATING_EXCEL_FILE = "Error occurred while creating the excel file";

    public static final String ERROR_WHILE_PROCESSING_UPLOADED_FILE = "Error occurred while processing the uploaded file";

    public static final String INCORRECT_EXCEL_FILE = "Excel file uploaded is incorrect";

    public static final String ESSENTIAL_FIELDS_MISSING = "Data missing for essential fields";

    public static final String PICKLIST_MORE_THAN_ONE_VALUE = "Picklist field type has more than one value";

    public static final String FIELD_ID_MISMATCH = "Field id is incorrect or does not exist";

    public static final String INCORRECT_EMAIL_FORMAT = "Incorrect email format";

    public static final String INCORRECT_RESOURCE_DATE_RANGE = "Resource's date of join cannot be after resource's last working date";

    public static final String EMPTY_RESOURCE_DATE_OF_JOIN = "Resource's date of join cannot be empty";

    public static final String HIERARCHICAL_CYCLE_DETECTED = "Circular Reporting Detected";

    public static final String FIELD_TYPE_PICKLIST = "Picklist";
    public static final String FIELD_TYPE_PICKLIST_MULTISELECT = "Picklist-Multiselect";
    public static final String FIELD_TYPE_DATE = "Date";
    public static final String FIELD_TYPE_EMAIL = "Email";
    public static final String FIELD_TYPE_NUMBER = "Number";
    public static final String FIELD_TYPE_CURRENCY = "Currency";
    public static final String FIELD_TYPE_TEXT = "Text";
    public static final String FIELD_TYPE_TEXT_AREA = "Textarea";

    public static final String REQUEST_RESOURCE_NEW_REQUEST = "New request";
    public static final String REQUEST_RESOURCE_STATUS_OPEN = "Open";
    public static final String REQUEST_RESOURCE_STATUS_RELEASED = "Released";
    public static final String REQUEST_RESOURCE_STATUS_PARTIALLY_FULFILLED = "Partially-Fulfilled";
    public static final String REQUEST_RESOURCE_STATUS_COMPLETELY_FULFILLED = "Completely-Fulfilled";
    public static final String REQUEST_RESOURCE_STATUS_OVER_FULFILLED = "Over-Fulfilled";
    public static final String REQUEST_RESOURCE_FTE_PERIOD_INCREASED = "FTE requested period increased";
    public static final String REQUEST_RESOURCE_FTE_PERIOD_DECREASED = "FTE requested period decreased";
    public static final String REQUEST_RESOURCE_AVG_APPROVED_FTE_INCREASED = "Average approved FTE increased by ";
    public static final String REQUEST_RESOURCE_AVG_APPROVED_FTE_DECREASED = "Average approved FTE decreased by ";

    public static String removeAllExtraSpace(String s){
        s = s.trim().replaceAll("\\s+", " ");
        return s;
    }

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

    public static boolean isEndDateBeforeStartDate(Date startDate, Date endDate){
        try {
            return endDate.compareTo(startDate) < 0;
        }
        catch(Exception e){
            return true;
        }
    }

    public static String outsideResourceDateRangeAllocation(List<String> projectNames){
        int recordCount = projectNames.size();
        String message = null;
        if(recordCount == 1) {
            String names = String.join(", ", projectNames);
            message = "Please de-allocate resource on project " + names + " on appropriate dates before changing the date of join or last working date of the resource";
        }
        else {
            String lastName = projectNames.get(recordCount-1);
            projectNames.remove(recordCount-1);
            String names = String.join(", ", projectNames);
            names = names + " and " + lastName;
            message = "Please de-allocate resource on projects " + names + " on appropriate dates before changing the date of join or last working date of the resource";
        }
        return message;
    }

    public static final String RESOLVE_RESOURCE_DELEGATIONS_BEFORE_DATE_CHANGE = "Please resolve all the active delegations done from and done to this resource before changing date of join or last working date";

    public static final String ALLOCATION_FREQUENCY_DAILY = "Daily";
    public static final String ALLOCATION_FREQUENCY_WEEKLY = "Weekly";
    public static final String ALLOCATION_FREQUENCY_MONTHLY = "Monthly";
    public static final String ALLOCATION_FREQUENCY_INVALID = "Invalid Allocation Frequency";
    public static final String ALLOCATION_BEYOND_PERMISSIBLE_LIMIT = "Allocation for certain dates is beyond the limits set by admin";

    public static final Integer UNDEFINED_UTILIZATION_STATUS_ID = 0;
    public static final String UNDEFINED_UTILIZATION_NAME = "Undefined";
    public static final String UNDEFINED_UTILIZATION_COLOR = "#000000";

    public static final String RESOURCE_ALLOCATION_BY_DEPARTMENT = "Department";
    public static final String RESOURCE_ALLOCATION_BY_PROJECT = "Project";
    public static final String RESOURCE_ALLOCATION_BY_UTILIZATION = "Utilization";
    public static final String RESOURCE_ALLOCATION_BY_PROJECT_OWNER = "Project Owner";
    
    public static final String FINANCE_EMPLOYEE_ID = "Employee ID";
    public static final String FINANCE_FIRST_NAME = "First Name";
    public static final String FINANCE_LAST_NAME = "Last Name";
    public static final String FINANCE_START_DATE = "Start Date";
    public static final String FINANCE_END_DATE = "End Date";
    public static final String FINANCE_WORK_LOCATION = "Work Location";
    public static final String FINANCE_DESIGNATION = "Designation";
    public static final String FINANCE_SALARY = "Salary";
    public static final String FINANCE_LOCATION = "Location";

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

    public static final List<String> FINANCIAL_DIRECT_FIELD_NAMES
            = List.of(new String[]{
                    ResourceUtils.FINANCE_EMPLOYEE_ID,
                    ResourceUtils.FINANCE_FIRST_NAME,
                    ResourceUtils.FINANCE_LAST_NAME,
                    ResourceUtils.FINANCE_START_DATE,
                    ResourceUtils.FINANCE_END_DATE,
                    ResourceUtils.FINANCE_WORK_LOCATION,
                    ResourceUtils.FINANCE_DESIGNATION,
                    ResourceUtils.FINANCE_SALARY,
                    ResourceUtils.FINANCE_LOCATION
            });

    public static final List<String> FINANCIAL_VENDOR_FIELD_NAMES
            = List.of(new String[]{
                    ResourceUtils.FINANCE_EMPLOYEE_ID,
                    ResourceUtils.FINANCE_FIRST_NAME,
                    ResourceUtils.FINANCE_LAST_NAME,
                    ResourceUtils.FINANCE_START_DATE,
                    ResourceUtils.FINANCE_END_DATE,
                    ResourceUtils.FINANCE_WORK_LOCATION,
                    ResourceUtils.FINANCE_DESIGNATION,
                    ResourceUtils.FINANCE_SALARY,
                    ResourceUtils.FINANCE_LOCATION
            });

    public static final double MIN_FTE = 0;

//    DO NOT DELETE THIS COMMENTED FUNCTION, UNLESS WE ARE SURE THAT roundToTwoDecimalPlace IS NOT THROWING ERROR
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
        InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream("queries/" + fileName);
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
