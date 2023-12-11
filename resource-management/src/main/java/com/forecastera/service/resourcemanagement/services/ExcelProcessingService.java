package com.forecastera.service.resourcemanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 03-10-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.dto.request.PostResourceMgmt;
import com.forecastera.service.resourcemanagement.dto.response.GetResourceAnalyticsMaster;
import com.forecastera.service.resourcemanagement.dto.utilityClass.CreateResourceData;
import com.forecastera.service.resourcemanagement.entity.*;
import com.forecastera.service.resourcemanagement.util.ResourceUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ExcelProcessingService {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelProcessingService.class);

    public byte[] generateCreateResourceExcelFile (List<ResourceMgmtFieldMap> resourceMgmtFieldMapList) throws IOException{
        Workbook workbook = new XSSFWorkbook();

//        create new sheet with a name
        Sheet sheet = workbook.createSheet("Resource Data");

//        freeze the first row of the sheet
        sheet.createFreezePane(0, 1);

//        create first row
        Row headerRow = sheet.createRow(0);
        int i=0;
        List<ResourceMgmtFieldMap> validColumns = new ArrayList<>();

//        create cell style for mandatory fields
        CellStyle mandatoryFieldCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        mandatoryFieldCellStyle.setFont(font);
        mandatoryFieldCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        mandatoryFieldCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//        for each resource management field
        for(ResourceMgmtFieldMap eachField: resourceMgmtFieldMapList){
            if(eachField.getEnabled().equals("1") && eachField.getIsResourceCreationVisible().equals("1") && eachField.getResourceCreationView().equals("1")){
                Cell cell = headerRow.createCell(i);
                if(eachField.getFields().equals(ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER)){
                    cell.setCellValue(ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER_EMAIL);
                }
                else{
                    cell.setCellValue(eachField.getFields());
                }
                if(eachField.getIsResourceCreationFreeze().equals("1")){
                    cell.setCellStyle(mandatoryFieldCellStyle);
                }
                validColumns.add(eachField);
                i++;
            }
        }

        // Set the column widths based on the content in the first row
        for (int k = 0; k < headerRow.getLastCellNum(); k++) {
            sheet.autoSizeColumn(k);
        }

        for(int j=1; j<=ResourceUtils.EXPECTED_NUMBER_OF_RECORDS; j++){
            Row row = sheet.createRow(j);

            CellStyle textStyle = workbook.createCellStyle();
            DataFormat dataFormat = workbook.createDataFormat();
            textStyle.setDataFormat(dataFormat.getFormat("@"));

            for(int k=0; k<i; k++){
                switch(validColumns.get(k).getResourceType()){
                    case ResourceUtils.FIELD_TYPE_CURRENCY:
                    case ResourceUtils.FIELD_TYPE_NUMBER:
                    case ResourceUtils.FIELD_TYPE_TEXT:
                    case ResourceUtils.FIELD_TYPE_TEXT_AREA:
                    case ResourceUtils.FIELD_TYPE_EMAIL:
                    case ResourceUtils.FIELD_TYPE_PICKLIST:
                    case ResourceUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                    case ResourceUtils.FIELD_TYPE_DATE:
                        Cell stringValue = row.createCell(k, CellType.STRING);
                        stringValue.setCellStyle(textStyle);
                        stringValue.setCellValue("");
                        break;
                }

            }
        }

////        create excel file in local machine to ensure proper creation
//        try (FileOutputStream fos = new FileOutputStream("test.xlsx")) {
//            workbook.write(fos);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Set the response headers for Excel download
        byte[] excelBytes = outputStream.toByteArray();
        outputStream.close();
        return excelBytes;
    }


    public Map<String, Object> excelToResourceDetails (MultipartFile multipartFile,
                                                       Map<String, ResourceMgmtFieldMap> resourceMgmtFieldMapListToMap,
                                                       Map<String, ResourceMgmtEmploymentType> resourceMgmtEmploymentTypeListToMap,
                                                       Map<String, ResourceMgmtSkills> resourceMgmtSkillsListToMap,
                                                       Map<String, ResourceMgmtRoles> resourceMgmtRolesListToMap,
                                                       Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap,
                                                       Map<String, GeneralSettingsLocation> generalSettingsLocationListToMap,
                                                       Map<Integer, Map<String, ResourceCustomPicklist>> resourceCustomPicklistListToMap,
                                                       Map<String, Integer> resourceIdEmailMap,
                                                       String dateFormat) {

        Map<String, Object> returnData = new HashMap<>();

        List<PostResourceMgmt> resourceList = new ArrayList<>();

        int numberOfFields = 0;

        try{
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming you're using the first sheet

            Row fieldNameList = sheet.getRow(0);
            numberOfFields = fieldNameList.getPhysicalNumberOfCells();



            for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                List<CreateResourceData> resourceDetails = new ArrayList<>();

                for(int j=0; j<row.getLastCellNum(); j++){

                    // To Set the cell format to text
                    CellStyle textStyle = workbook.createCellStyle();
                    DataFormat dataFormat = workbook.createDataFormat();
                    textStyle.setDataFormat(dataFormat.getFormat("@"));

                    DataFormatter dataFormatter = new DataFormatter();

                    Cell cellValue = row.getCell(j);
                    String fieldValue = null;
                    String fieldName = fieldNameList.getCell(j).toString();
                    if(fieldName.equals(ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER_EMAIL)){
                        fieldName = ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER;
                    }
                    fieldName = fieldName.toLowerCase();

                    String check = null;
                    if(cellValue!=null) {
                        try {
                            check = cellValue.getStringCellValue();
                        } catch (Exception e) {
                            check = String.valueOf(cellValue.getDateCellValue());
                        }
                    }

                    if(resourceMgmtFieldMapListToMap.containsKey(fieldName)) {
                        if(check!=null && !check.isEmpty()) {
                            ResourceMgmtFieldMap fieldSetting = resourceMgmtFieldMapListToMap.get(fieldName);
                            CreateResourceData eachDetail;

                            try {
                                switch (fieldSetting.getResourceType()) {
                                    case ResourceUtils.FIELD_TYPE_CURRENCY:
                                    case ResourceUtils.FIELD_TYPE_NUMBER:
                                        try {
                                            if (cellValue.getStringCellValue().length() <= 25) {
                                                Double number = Double.valueOf(cellValue.getStringCellValue());
                                                fieldValue = cellValue.getStringCellValue();
                                            }
                                        } catch (Exception e) {
//                                        value entered not a number
                                        }
                                        break;
                                    case ResourceUtils.FIELD_TYPE_TEXT:
                                    case ResourceUtils.FIELD_TYPE_TEXT_AREA:
                                    case ResourceUtils.FIELD_TYPE_EMAIL:
                                        fieldValue = cellValue.getStringCellValue();
                                        break;
                                    case ResourceUtils.FIELD_TYPE_DATE:
                                        try {
                                            String dateFromExcel = cellValue.getStringCellValue();
                                            Date date = new SimpleDateFormat(dateFormat).parse(dateFromExcel);

                                            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
                                            fieldValue = String.valueOf(java.sql.Date.valueOf(localDate));
                                        } catch (Exception e) {
                                            fieldValue = "";
                                        }
                                        break;
                                    case ResourceUtils.FIELD_TYPE_PICKLIST:
                                        String[] picklistValues = cellValue.getStringCellValue().split(",");
                                        List<String> picklistIds = new ArrayList<>();
                                        switch (fieldSetting.getFields()) {
                                            case ResourceUtils.FIELD_RESOURCE_EMPLOYMENT_TYPE:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (resourceMgmtEmploymentTypeListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(resourceMgmtEmploymentTypeListToMap.get(picklistValue).getEmpTypeId()));
                                                    }
                                                }
                                                break;
                                            case ResourceUtils.FIELD_RESOURCE_DEPARTMENT:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (resourceMgmtDepartmentListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(resourceMgmtDepartmentListToMap.get(picklistValue).getDepartmentId()));
                                                    }
                                                }
                                                break;
                                            case ResourceUtils.FIELD_RESOURCE_LOCATION:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (generalSettingsLocationListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(generalSettingsLocationListToMap.get(picklistValue).getLocationId()));
                                                    }
                                                }
                                                break;
                                            case ResourceUtils.FIELD_RESOURCE_REPORTING_MANAGER:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue);
                                                    if (resourceIdEmailMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(resourceIdEmailMap.get(picklistValue)));
                                                    }
                                                }
                                                break;
                                            default:
                                                Map<String, ResourceCustomPicklist> currentPicklistOptions = resourceCustomPicklistListToMap.get(fieldSetting.getFieldId());
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (currentPicklistOptions.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(currentPicklistOptions.get(picklistValue).getId()));
                                                    }
                                                }
                                        }
                                        fieldValue = String.join(",", picklistIds);
                                        break;
                                    case ResourceUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                                        String[] multiPicklistValues = cellValue.getStringCellValue().split(",");
                                        List<String> multiPicklistIds = new ArrayList<>();
                                        switch (fieldSetting.getFields()) {
                                            case ResourceUtils.FIELD_RESOURCE_ROLE:
                                                for (String picklistValue : multiPicklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (resourceMgmtRolesListToMap.containsKey(picklistValue)) {
                                                        multiPicklistIds.add(String.valueOf(resourceMgmtRolesListToMap.get(picklistValue).getRoleId()));
                                                    }
                                                }
                                                break;
                                            case ResourceUtils.FIELD_RESOURCE_SKILL:
                                                for (String picklistValue : multiPicklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (resourceMgmtSkillsListToMap.containsKey(picklistValue)) {
                                                        multiPicklistIds.add(String.valueOf(resourceMgmtSkillsListToMap.get(picklistValue).getSkillId()));
                                                    }
                                                }
                                                break;
                                            default:
                                                Map<String, ResourceCustomPicklist> currentPicklistOptions = resourceCustomPicklistListToMap.get(fieldSetting.getFieldId());
                                                for (String picklistValue : multiPicklistValues) {
                                                    picklistValue = ResourceUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (currentPicklistOptions.containsKey(picklistValue)) {
                                                        multiPicklistIds.add(String.valueOf(currentPicklistOptions.get(picklistValue).getId()));
                                                    }
                                                }
                                        }
                                        fieldValue = String.join(",", multiPicklistIds);
                                        break;
                                }
                                eachDetail = new CreateResourceData(fieldSetting.getFieldId(), fieldSetting.getFields(), fieldValue, fieldSetting.getSettingType());
                                resourceDetails.add(eachDetail);
                            } catch (Exception e) {
                                LOGGER.warn("Type-Conversion error from excel to variable for field: " + fieldSetting.getFields() + " of type: " + fieldSetting.getResourceType());
                            }
                        }
                    }
                    else{
                        returnData.put("Result", false);
                        return returnData;
                    }
                }

                if(!resourceDetails.isEmpty()){
                    resourceList.add(new PostResourceMgmt(resourceDetails, null, null, null, null));
                }

            }
        }
        catch(Exception e){
            LOGGER.warn("--------------------------Error---------------------------->");
            e.printStackTrace();
        }
        if(resourceList.isEmpty()){
            resourceList = null;
        }
//        do not change order of insertion here, without changing order of retrieval in the method where it is processed
        returnData.put("result", true);
        returnData.put("resourceList", resourceList);
        returnData.put("numberOfFields", numberOfFields);
        return returnData;
    }


    public byte[] generateUploadedCreateResourceExcelFileResponse (MultipartFile multipartFile, List<String> response, Integer numberOfFields) throws IOException{
        Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        //        create cell style for mandatory fields
        CellStyle updateStatus = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        updateStatus.setFont(font);
        updateStatus.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        updateStatus.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.getRow(0);
        Cell cell = headerRow.createCell(numberOfFields);
        cell.setCellValue("Update Status");
        cell.setCellStyle(updateStatus);


        for (int i=1; i<=response.size(); i++) {
            Row row = sheet.getRow(i);

            row.createCell(numberOfFields).setCellValue(response.get(i-1));
        }

        sheet.autoSizeColumn(numberOfFields);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Set the response headers for Excel download
        byte[] excelBytes = outputStream.toByteArray();
        outputStream.close();
        return excelBytes;
    }

    public byte[] generateExcelFileForResourceAnalyticsTable(List<GetResourceAnalyticsMaster> getResourceAnalyticsMasterList,
                                                             List<ResourceMgmtFieldMap> resourceMgmtFieldMapList) throws IOException{

        Workbook workbook = new XSSFWorkbook();

//        create new sheet with a name
        Sheet sheet = workbook.createSheet("Resource Data");

//        freeze the first row of the sheet
        sheet.createFreezePane(0, 1);

//        create first row
        Row headerRow = sheet.createRow(0);
        int rowNumber = 0;
        int i=0;
        List<ResourceMgmtFieldMap> validColumns = new ArrayList<>();

//        create cell style for mandatory fields
        CellStyle mandatoryFieldCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        mandatoryFieldCellStyle.setFont(font);
        mandatoryFieldCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        mandatoryFieldCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//        for each resource management field
        for(ResourceMgmtFieldMap eachField: resourceMgmtFieldMapList){
            if(eachField.getEnabled().equals("1") && eachField.getIsResourceAnalysisVisible().equals("1") && eachField.getResourceAnalyticsView().equals("1")){
                Cell cell = headerRow.createCell(i);

                cell.setCellValue(eachField.getFields());

//                if(eachField.getIsResourceCreationFreeze()){
                    cell.setCellStyle(mandatoryFieldCellStyle);
//                }
                validColumns.add(eachField);
                i++;
            }
        }
        rowNumber++;

        for(GetResourceAnalyticsMaster eachResource: getResourceAnalyticsMasterList){
            Row eachRow = sheet.createRow(rowNumber);
            int colNumber=0;
            List<Map<String, Object>> resourceData =  eachResource.getFields();
            for(int fieldNumber = 1; fieldNumber<resourceData.size(); fieldNumber++){
                Map<String, Object> field = resourceData.get(fieldNumber);
                Cell cell = eachRow.createCell(colNumber);
                String fieldValue = String.valueOf(field.get("fieldValue"));
                if(fieldValue == null || fieldValue.equals("null")){
                    fieldValue = "";
                }
                cell.setCellValue(fieldValue);
                colNumber++;
            }
            rowNumber++;
        }

        // Set the column widths based on the content in the first row
        for (int k = 0; k < headerRow.getLastCellNum(); k++) {
            sheet.autoSizeColumn(k);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Set the response headers for Excel download
        byte[] excelBytes = outputStream.toByteArray();
        outputStream.close();
        return excelBytes;
    }
}
