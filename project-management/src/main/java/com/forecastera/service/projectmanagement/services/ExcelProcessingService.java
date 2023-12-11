package com.forecastera.service.projectmanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 09-10-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.dto.request.PostProjectMgmt;
import com.forecastera.service.projectmanagement.dto.response.GetProjectAnalysisMaster;
import com.forecastera.service.projectmanagement.dto.utilityClass.CreateProjectData;
import com.forecastera.service.projectmanagement.dto.utilityClass.ResourceIdEmail;
import com.forecastera.service.projectmanagement.entity.*;
import com.forecastera.service.projectmanagement.util.ProjectUtils;
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

    public byte[] generateCreateProjectExcelFile (List<ProjectMgmtFieldMap> projectMgmtFieldMapList) throws IOException{
        Workbook workbook = new XSSFWorkbook();

//        create new sheet with a name
        Sheet sheet = workbook.createSheet("Project Data");

//        freeze the first row of the sheet
        sheet.createFreezePane(0, 1);

//        create first row
        Row headerRow = sheet.createRow(0);
        int i=0;
        List<ProjectMgmtFieldMap> validColumns = new ArrayList<>();

//        create cell style for mandatory fields
        CellStyle mandatoryFieldCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        mandatoryFieldCellStyle.setFont(font);
        mandatoryFieldCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        mandatoryFieldCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//        for each project management field
        for(ProjectMgmtFieldMap eachField: projectMgmtFieldMapList){
            if(eachField.getIsEnabled().equals("1") && eachField.getIsProjectCreationVisible().equals("1") && eachField.getProjCreationView().equals("1")){
                if(eachField.getFields().equals(ProjectUtils.FIELD_PROJECT_PROJECT_OWNER)) {
                    eachField.setFields(ProjectUtils.PROJECT_OWNER_EMAIL_ID);
                }
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(eachField.getFields());
                if(eachField.getIsProjectCreationFreeze().equals("1")){
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

        for(int j=1; j<=ProjectUtils.EXPECTED_NUMBER_OF_RECORDS; j++){
            Row row = sheet.createRow(j);

            CellStyle textStyle = workbook.createCellStyle();
            DataFormat dataFormat = workbook.createDataFormat();
            textStyle.setDataFormat(dataFormat.getFormat("@"));

            for(int k=0; k<i; k++){
                switch(validColumns.get(k).getFieldType()){
                    case ProjectUtils.FIELD_TYPE_CURRENCY:
                    case ProjectUtils.FIELD_TYPE_NUMBER:
                    case ProjectUtils.FIELD_TYPE_TEXT:
                    case ProjectUtils.FIELD_TYPE_TEXT_AREA:
                    case ProjectUtils.FIELD_TYPE_EMAIL:
                    case ProjectUtils.FIELD_TYPE_PICKLIST:
                    case ProjectUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                    case ProjectUtils.FIELD_TYPE_DATE:
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


    public Map<String, Object> excelToProjectDetails (MultipartFile multipartFile,
                                                      Map<String, ProjectMgmtFieldMap> projectMgmtFieldMapListToMap,
                                                      Map<String, ProjectMgmtStatus> projectMgmtStatusListToMap,
                                                      Map<String, ProjectMgmtType> projectMgmtTypeListToMap,
                                                      Map<String, ProjectMgmtPriority> projectMgmtPriorityListToMap,
                                                      Map<String, ResourceIdEmail> resourceIdEmailMap,
                                                      Map<Integer, Map<String, ProjectCustomPicklist>> projectCustomPicklistListToMap,
                                                      String dateFormat,
                                                      Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap) {

        Map<String, Object> returnData = new HashMap<>();

        List<PostProjectMgmt> projectList = new ArrayList<>();

        int numberOfFields = 0;

        try{
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming you're using the first sheet

            Row fieldNameList = sheet.getRow(0);
            numberOfFields = fieldNameList.getPhysicalNumberOfCells();

            for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                List<CreateProjectData> projectDetails = new ArrayList<>();

                for(int j=0; j<row.getLastCellNum(); j++){

                    // To Set the cell format to text
                    CellStyle textStyle = workbook.createCellStyle();
                    DataFormat dataFormat = workbook.createDataFormat();
                    textStyle.setDataFormat(dataFormat.getFormat("@"));

                    DataFormatter dataFormatter = new DataFormatter();

                    Cell cellValue = row.getCell(j);
                    String fieldValue = null;
                    String fieldName = fieldNameList.getCell(j).toString();
                    if(fieldName.equals(ProjectUtils.PROJECT_OWNER_EMAIL_ID)){
                        fieldName = ProjectUtils.FIELD_PROJECT_PROJECT_OWNER;
                    }

                    String check = null;
                    if(cellValue!=null) {
                        try {
                            check = cellValue.getStringCellValue();
                        } catch (Exception e) {
                            check = String.valueOf(cellValue.getDateCellValue());
                        }
                    }

                    if(projectMgmtFieldMapListToMap.containsKey(fieldName.toLowerCase()) ) {
                        if(check!=null && !check.isEmpty()) {
                            ProjectMgmtFieldMap fieldSetting = projectMgmtFieldMapListToMap.get(fieldName.toLowerCase());
                            CreateProjectData eachDetail;

                            try {
                                switch (fieldSetting.getFieldType()) {
                                    case ProjectUtils.FIELD_TYPE_CURRENCY:
                                    case ProjectUtils.FIELD_TYPE_NUMBER:
                                        try {
                                            if (cellValue.getStringCellValue().length() <= 25) {
                                                Double number = Double.valueOf(cellValue.getStringCellValue());
                                                fieldValue = cellValue.getStringCellValue();
                                            }
                                        } catch (Exception e) {
//                                        value entered not a number
                                        }
                                        break;
                                    case ProjectUtils.FIELD_TYPE_TEXT:
                                    case ProjectUtils.FIELD_TYPE_TEXT_AREA:
                                    case ProjectUtils.FIELD_TYPE_EMAIL:
                                        fieldValue = cellValue.getStringCellValue();
                                        break;
                                    case ProjectUtils.FIELD_TYPE_DATE:
                                        try {
                                            String dateFromExcel = cellValue.getStringCellValue();
                                            Date date = new SimpleDateFormat(dateFormat).parse(dateFromExcel);

                                            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
                                            fieldValue = String.valueOf(java.sql.Date.valueOf(localDate));
                                        } catch (Exception e) {
                                            fieldValue = "";
                                        }
                                        break;
                                    case ProjectUtils.FIELD_TYPE_PICKLIST:
                                        String[] picklistValues = cellValue.getStringCellValue().split(",");
                                        List<String> picklistIds = new ArrayList<>();
                                        switch (fieldSetting.getFields()) {
                                            case ProjectUtils.FIELD_PROJECT_STATUS:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ProjectUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (projectMgmtStatusListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(projectMgmtStatusListToMap.get(picklistValue).getStatusId()));
                                                    }
                                                }
                                                break;
                                            case ProjectUtils.FIELD_PROJECT_PROJECT_TYPE:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ProjectUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (projectMgmtTypeListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(projectMgmtTypeListToMap.get(picklistValue).getTypeId()));
                                                    }
                                                }
                                                break;
                                            case ProjectUtils.FIELD_PROJECT_PRIORITY:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ProjectUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (projectMgmtPriorityListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(projectMgmtPriorityListToMap.get(picklistValue).getPriorityId()));
                                                    }
                                                }
                                                break;
                                            case ProjectUtils.FIELD_PROJECT_DEPARTMENT:
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ProjectUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (resourceMgmtDepartmentListToMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(resourceMgmtDepartmentListToMap.get(picklistValue).getDepartmentId()));
                                                    }
                                                }
                                                break;
                                            case ProjectUtils.FIELD_PROJECT_PROJECT_OWNER:
                                                for (String picklistValue : picklistValues) {
                                                    if (resourceIdEmailMap.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(resourceIdEmailMap.get(picklistValue).getResourceId()));
                                                    }
                                                }
                                                break;
                                            default:
                                                Map<String, ProjectCustomPicklist> currentPicklistOptions = projectCustomPicklistListToMap.get(fieldSetting.getFieldId());
                                                for (String picklistValue : picklistValues) {
                                                    picklistValue = ProjectUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                                    if (currentPicklistOptions.containsKey(picklistValue)) {
                                                        picklistIds.add(String.valueOf(currentPicklistOptions.get(picklistValue).getId()));
                                                    }
                                                }
                                        }
                                        fieldValue = String.join(",", picklistIds);
                                        break;
                                    case ProjectUtils.FIELD_TYPE_PICKLIST_MULTISELECT:
                                        String[] multiPicklistValues = cellValue.getStringCellValue().split(",");
                                        List<String> multiPicklistIds = new ArrayList<>();

                                        Map<String, ProjectCustomPicklist> currentPicklistOptions = projectCustomPicklistListToMap.get(fieldSetting.getFieldId());
                                        for (String picklistValue : multiPicklistValues) {
                                            picklistValue = ProjectUtils.removeAllExtraSpace(picklistValue).toLowerCase();
                                            if (currentPicklistOptions.containsKey(picklistValue)) {
                                                multiPicklistIds.add(String.valueOf(currentPicklistOptions.get(picklistValue).getId()));
                                            }
                                        }

                                        fieldValue = String.join(",", multiPicklistIds);
                                        break;
                                }
                                eachDetail = new CreateProjectData(fieldSetting.getFieldId(), fieldSetting.getFields(), fieldValue, fieldSetting.getSettingType());
                                projectDetails.add(eachDetail);
                            } catch (Exception e) {
                                LOGGER.warn("Type-Conversion error from excel to variable for field: " + fieldSetting.getFields() + " of type: " + fieldSetting.getFieldType());
                            }
                        }
                    }
                    else{
                        returnData.put("Result", false);
                        return returnData;
                    }
                }

                if(!projectDetails.isEmpty()){
                    projectList.add(new PostProjectMgmt(projectDetails, null, null, null, null));
                }

            }
        }
        catch(Exception e){
            LOGGER.warn("--------------------------Error---------------------------->");
            e.printStackTrace();
        }
        if(projectList.isEmpty()){
            projectList = null;
        }
//        do not change order of insertion here, without changing order of retrieval in the method where it is processed
        returnData.put("result", true);
        returnData.put("projectList", projectList);
        returnData.put("numberOfFields", numberOfFields);
        return returnData;
    }


    public byte[] generateUploadedCreateProjectExcelFileResponse (MultipartFile multipartFile, List<String> response, Integer numberOfFields) throws IOException{
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

    public byte[] generateExcelFileForResourceAnalyticsTable(List<GetProjectAnalysisMaster> getProjectAnalysisMasterList,
                                                             List<ProjectMgmtFieldMap> projectMgmtFieldMapList) throws IOException{

        Workbook workbook = new XSSFWorkbook();

//        create new sheet with a name
        Sheet sheet = workbook.createSheet("Resource Data");

//        freeze the first row of the sheet
        sheet.createFreezePane(0, 1);

//        create first row
        Row headerRow = sheet.createRow(0);
        int rowNumber = 0;
        int i=0;
        List<ProjectMgmtFieldMap> validColumns = new ArrayList<>();

//        create cell style for mandatory fields
        CellStyle mandatoryFieldCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        mandatoryFieldCellStyle.setFont(font);
        mandatoryFieldCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        mandatoryFieldCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

//        for each resource management field
        for(ProjectMgmtFieldMap eachField: projectMgmtFieldMapList){
            if(eachField.getIsEnabled().equals("1") && eachField.getIsProjectAnalysisVisible().equals("1") && eachField.getProjAnalysisView().equals("1")){
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

        for(GetProjectAnalysisMaster eachResource: getProjectAnalysisMasterList){
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
