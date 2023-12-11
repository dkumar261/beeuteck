package com.forecastera.service.usersettingsmanagement.util;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.GetDateRange;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserSettingsUtil {

    public static final String GATEWAY_URL = "http://172.16.31.26:8001";
    public static final String SWAGGER_URL = "http://172.16.31.26:8003";
    public static final Integer COMPONENT_TYPE = 0;
    public static final Integer FIELD_TYPE = 2;
    public static final Integer BUTTON_TYPE = 1;
    public static final Integer CUSTOM_TYPE = 3;
    public static final Integer SETTING_TYPE = 4;

    public static final Integer DIRECT_EMPLOYEE_DETAILS = 1;
    public static final Integer OTHER_DIRECT_IN_HOUSE_COST = 2;
    public static final Integer VENDOR_EMPLOYEE_DETAILS = 3;
    public static final Integer VENDOR_RELATED_COST = 4;
    public static final Integer CAPEX_RELATED_EXPENSE = 5;

    public static final String SUCCESS = "SUCCESS";
    public static final String NO_DATA = "NO DATA AVAILABLE";

    public static final String PICKLIST = "Picklist";

    public static final String DUPLICATE_FIELD_NAMES = "Duplicate field names found";
    public static final String EMPTY_FIELD_NAMES = "Empty field names found";
    public static final String DUPLICATE_PICKLIST_VALUES = "Duplicate values found";
    public static final String EMPTY_DEPARTMENT_NAME = "Department name is empty";
    public static final String EMPTY_PICKLIST_VALUES = "Empty picklist values found";
    public static final String EMPTY_PICKLIST_VALUES_FOR_TYPE_PICKLIST = "Field type Picklist/Picklist-Multiselect does not have any picklist values";
    public static final String INVALID_DEPARTMENT_DELETION_RESOURCE = "Cannot delete a department with resources assigned to it";
    public static final String INVALID_DEPARTMENT_DELETION_ROLE = "Cannot delete a department with roles allocated to it";
    public static final String INVALID_DATE_RANGES = "Delegation date ranges are overlapping with existing record for same resource and department";
    public static final String INVALID_DEPARTMENT_ID = "Invalid department";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";

    public static final String ERROR_WHILE_CREATING_EXCEL_FILE = "Error occurred while creating the excel file";
    public static final String INCORRECT_EXCEL_FILE = "Excel file uploaded is incorrect";
    public static final String ERROR_WHILE_PROCESSING_UPLOADED_FILE = "Error occurred while processing the uploaded file";

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
    public static final String ESSENTIAL_FIELDS_MISSING = "Data missing for essential fields";
    public static final String INVALID_PROJECT_FIELD_TYPE = "Invalid field type";
    public static final String INVALID_RESOURCE_FIELD_TYPE = "Invalid resource type";
    public static final Map<String, String> VALID_FIELD_TYPE;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("Currency", "");
        map.put("Date", "");
        map.put("Email", "");
        map.put("Number", "");
        map.put("Picklist", "");
        map.put("Picklist-Multiselect", "");
        map.put("Text", "");
        map.put("Textarea", "");

        VALID_FIELD_TYPE = Collections.unmodifiableMap(map);
    }

    public static String removeAllExtraSpace(String s){
        s = s.trim().replaceAll("\\s+", " ");
        return s;
    }

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

    /**
     * convert into Model object
     */
    public static <S> S toModel(Object fromObject, Class<S> toClass, ObjectMapper objectMapper) {

        try {
            return objectMapper.convertValue(fromObject, toClass);
        } catch (Exception e) {
            System.out.println("Something Went Wrong: " + e);
        }
        return null;
    }
}
