package com.forecastera.service.usersettingsmanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 07-12-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.utilityClass.ResourceIdEmail;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtDepartment;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtFieldMap;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtRoles;
import com.forecastera.service.usersettingsmanagement.util.UserSettingsUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelProcessingService.class);

    public byte[] generateCreateRoleTemplateExcelFile () throws IOException {
        Workbook workbook = new XSSFWorkbook();

//        create new sheet with a name
        Sheet sheet = workbook.createSheet("Role Data");

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

        Cell roleCol = headerRow.createCell(i);
        roleCol.setCellValue("Role");
        roleCol.setCellStyle(mandatoryFieldCellStyle);
        i++;

        Cell departmentCol = headerRow.createCell(i);
        departmentCol.setCellValue("Department");
        departmentCol.setCellStyle(mandatoryFieldCellStyle);

        // Set the column widths based on the content in the first row
        for (int k = 0; k < headerRow.getLastCellNum(); k++) {
            sheet.autoSizeColumn(k);
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

    public Map<String, Object> excelToRoleData(MultipartFile multipartFile, Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap){
        Map<String, Object> returnData = new HashMap<>();

        List<ResourceMgmtRoles> roleList = new ArrayList<>();

        int numberOfFields = 0;

        try{
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming you're using the first sheet

            Row fieldNameList = sheet.getRow(0);
            numberOfFields = fieldNameList.getPhysicalNumberOfCells();

            for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                ResourceMgmtRoles resourceMgmtRoles = new ResourceMgmtRoles();

                try{
                    Cell cellValue = row.getCell(0);
                    String roleName = cellValue.getStringCellValue();
                    resourceMgmtRoles.setRole(UserSettingsUtil.removeAllExtraSpace(roleName));
                }
                catch(Exception e){
                    returnData.put("Result", false);
                    return returnData;
                }

                try{
                    Cell cellValue = row.getCell(1);
                    String departmentName = cellValue.getStringCellValue();
                    Integer departmentId = null;
                    if(resourceMgmtDepartmentListToMap.containsKey(UserSettingsUtil.removeAllExtraSpace(departmentName).toLowerCase())){
                        departmentId = resourceMgmtDepartmentListToMap.get(UserSettingsUtil.removeAllExtraSpace(departmentName).toLowerCase()).getDepartmentId();
                    }
                    resourceMgmtRoles.setDepartmentId(departmentId);
                }
                catch(Exception e){
                    returnData.put("Result", false);
                    return returnData;
                }

                resourceMgmtRoles.setIsActive(true);
                resourceMgmtRoles.setRowOrder(0);
                roleList.add(resourceMgmtRoles);
            }
        }
        catch(Exception e){
            LOGGER.warn("--------------------------Error---------------------------->");
            e.printStackTrace();
        }
        if(roleList.isEmpty()){
            roleList = null;
        }
//        do not change key name here, without changing key names in the method where it is processed
        returnData.put("result", true);
        returnData.put("roleList", roleList);
        returnData.put("numberOfFields", numberOfFields);
        return returnData;
    }

    public byte[] generateUploadedExcelFileResponse(MultipartFile multipartFile, List<String> response, Integer numberOfFields) throws IOException{
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

    public byte[] generateCreateDepartmentTemplateExcelFile () throws IOException {
        Workbook workbook = new XSSFWorkbook();

//        create new sheet with a name
        Sheet sheet = workbook.createSheet("Role Data");

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

        Cell departmentNameCol = headerRow.createCell(i);
        departmentNameCol.setCellValue("Department Name");
        departmentNameCol.setCellStyle(mandatoryFieldCellStyle);
        i++;

        Cell departmentCodeCol = headerRow.createCell(i);
        departmentCodeCol.setCellValue("Department Code");
        departmentCodeCol.setCellStyle(mandatoryFieldCellStyle);
        i++;

        Cell statusCol = headerRow.createCell(i);
        statusCol.setCellValue("Status");
        i++;

        Cell descriptionCol = headerRow.createCell(i);
        descriptionCol.setCellValue("Description");
        i++;

        Cell managerCol = headerRow.createCell(i);
        managerCol.setCellValue("Department Manager's Email");


        // Set the column widths based on the content in the first row
        for (int k = 0; k < headerRow.getLastCellNum(); k++) {
            sheet.autoSizeColumn(k);
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


    public Map<String, Object> excelToDepartmentData(MultipartFile multipartFile, Map<String, ResourceIdEmail> resourceIdEmailMap){
        Map<String, Object> returnData = new HashMap<>();

        List<ResourceMgmtDepartment> departmentList = new ArrayList<>();

        int numberOfFields = 0;

        try{
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming you're using the first sheet

            Row fieldNameList = sheet.getRow(0);
            numberOfFields = fieldNameList.getPhysicalNumberOfCells();

            for (int i=1; i<sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);

                ResourceMgmtDepartment resourceMgmtDepartment = new ResourceMgmtDepartment();

                try{
                    Cell cellValue = row.getCell(0);
                    String departmentName = cellValue.getStringCellValue();
                    resourceMgmtDepartment.setDepartment(UserSettingsUtil.removeAllExtraSpace(departmentName));
                }
                catch(Exception e){
                    resourceMgmtDepartment.setDepartment(null);
                }

                try{
                    Cell cellValue = row.getCell(1);
                    String departmentCode = cellValue.getStringCellValue();
                    resourceMgmtDepartment.setDepartmentCode(UserSettingsUtil.removeAllExtraSpace(departmentCode));
                }
                catch(Exception e){
                    resourceMgmtDepartment.setDepartmentCode(null);
                }

                try{
                    Cell cellValue = row.getCell(2);
                    String status = cellValue.getStringCellValue();
                    resourceMgmtDepartment.setStatus(UserSettingsUtil.removeAllExtraSpace(status));
                }
                catch(Exception e){
                    resourceMgmtDepartment.setStatus("");
                }

                try{
                    Cell cellValue = row.getCell(3);
                    String description = cellValue.getStringCellValue();
                    resourceMgmtDepartment.setShortDescription(UserSettingsUtil.removeAllExtraSpace(description));
                }
                catch(Exception e){
                    resourceMgmtDepartment.setShortDescription("");
                }

                try{
                    Cell cellValue = row.getCell(4);
                    String managerEmailId = cellValue.getStringCellValue();
                    Integer resourceId = null;
                    if(resourceIdEmailMap.containsKey(managerEmailId)){
                        resourceId = resourceIdEmailMap.get(managerEmailId).getResourceId();
                    }
                    resourceMgmtDepartment.setResourceId(resourceId);
                }
                catch(Exception e){
                    resourceMgmtDepartment.setResourceId(null);
                }

                resourceMgmtDepartment.setIsActive(true);
                resourceMgmtDepartment.setRowOrder(0);
                departmentList.add(resourceMgmtDepartment);
            }
        }
        catch(Exception e){
            LOGGER.warn("--------------------------Error---------------------------->");
            e.printStackTrace();
        }
        if(departmentList.isEmpty()){
            departmentList = null;
        }
//        do not change key name here, without changing key names in the method where it is processed
        returnData.put("result", true);
        returnData.put("departmentList", departmentList);
        returnData.put("numberOfFields", numberOfFields);
        return returnData;
    }
}
