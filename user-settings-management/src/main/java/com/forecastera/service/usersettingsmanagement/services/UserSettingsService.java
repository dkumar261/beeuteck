package com.forecastera.service.usersettingsmanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.commonResponseUtil.Error;
import com.forecastera.service.usersettingsmanagement.dto.request.*;
import com.forecastera.service.usersettingsmanagement.dto.response.*;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.*;
import com.forecastera.service.usersettingsmanagement.entity.*;
import com.forecastera.service.usersettingsmanagement.repository.*;
import com.forecastera.service.usersettingsmanagement.util.UserSettingsUtil;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserSettingsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSettingsService.class);

    @Autowired
    private GeneralSettingsRepo generalSettingsRepo;

    @Autowired
    private ProjectMgmtFieldMapRepo projectMgmtFieldMapRepo;

    @Autowired
    private ProjectMgmtStatusRepo projectManagementStatusRepo;

    @Autowired
    private ProjectMgmtPriorityRepo projectManagementPriorityRepo;

    @Autowired
    private ProjectMgmtTypeRepo projectManagementTypeRepo;

    @Autowired
    private ProjectCustomPicklistRepo projectCustomPicklistRepo;

    @Autowired
    private ResourceMgmtFieldMapRepo resourceMgmtFieldMapRepo;

    @Autowired
    private ResourceMgmtStatusRepo resourceManagementStatusRepo;

    @Autowired
    private ResourceMgmtEmploymentTypeRepo resourceManagementEmploymentTypeRepo;

    @Autowired
    private ResourceCustomPicklistRepo resourceCustomPicklistRepo;

    @Autowired
    private ResourceMgmtSkillsRepo resourceMgmtSkillsRepo;

    @Autowired
    private ResourceMgmtRolesRepo resourceMgmtRolesRepo;

    @Autowired
    private ResourceMgmtDepartmentRepo resourceMgmtDepartmentRepo;

    @Autowired
    private ResourceMgmtRepo resourceMgmtRepo;

    @Autowired
    private FinanceMgmtFieldMapRepo financeMgmtFieldMapRepo;

    @Autowired
    private FinanceCustomPicklistRepo financeCustomPicklistRepo;

    @Autowired
    private GeneralSettingsLocationRepo generalSettingsLocationRepo;

    @Autowired
    private DelegatedResourceHistoryRepo delegatedResourceHistoryRepo;

    @Autowired
    private ExcelProcessingService excelProcessingService;


    /*
    Admin General Settings
     */
    public List<GetGeneralSettings> getGeneralSettings() {
        List<GeneralSettings> generalSettingsList = generalSettingsRepo.findAll();
        List<GetGeneralSettings> getGeneralSettingsList = new ArrayList<>();

        for (GeneralSettings eachSetting : generalSettingsList) {
            GetGeneralSettings getGeneralSettings = new GetGeneralSettings(eachSetting);
            getGeneralSettingsList.add(getGeneralSettings);
        }
        return getGeneralSettingsList;
    }

    public Object updateGeneralSettings(List<PostGeneralSettings> postGeneralSettingsList, String modifiedBy) {
        List<GeneralSettings> generalSettingsList = new ArrayList<>();

        for (PostGeneralSettings eachSetting : postGeneralSettingsList) {
            Optional<GeneralSettings> originalGeneralSettings = generalSettingsRepo.findById(eachSetting.getBaseSettingId());
            if(originalGeneralSettings.isPresent()){
                GeneralSettings generalSettings = new GeneralSettings(eachSetting);
                generalSettings.setBaseSettingId(originalGeneralSettings.get().getBaseSettingId());
                generalSettings.setCreatedBy(originalGeneralSettings.get().getCreatedBy());
                generalSettings.setCreatedDate(originalGeneralSettings.get().getCreatedDate());
                generalSettings.setModifiedBy(modifiedBy);
                generalSettings.setModifiedDate(new Date());
                if(!UserSettingsUtil.VALID_DATE_FORMAT.containsKey(generalSettings.getDateFormat())){
                    String message = UserSettingsUtil.INVALID_DATE_FORMAT;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                generalSettingsList.add(generalSettings);
            }
        }

        LOGGER.info("User " + modifiedBy + " updated general settings at " + new Date());

        generalSettingsRepo.saveAll(generalSettingsList);
        return true;
    }

    /*
    Skill Settings
     */
    public List<GetResourceMgmtSkills> getResourceMgmtSkills() {
        List<ResourceMgmtSkills> resourceMgmtSkillsList = resourceMgmtSkillsRepo.findAll();

        List<GetResourceMgmtSkills> getSkillsList = new ArrayList<>();

        for (ResourceMgmtSkills eachSkill : resourceMgmtSkillsList) {
            GetResourceMgmtSkills resourceMgmtSkills = new GetResourceMgmtSkills(eachSkill);
            getSkillsList.add(resourceMgmtSkills);
        }
        if(!getSkillsList.isEmpty()){
            getSkillsList.sort(Comparator.comparing(GetResourceMgmtSkills::getRowOrder));
        }
        return getSkillsList;
    }

    public Object updateResourceMgmtSkills(List<PostResourceMgmtSkills> postResourceMgmtSkillsList, String modifiedBy) {
        List<ResourceMgmtSkills> updateSkillsList = new ArrayList<>();

        HashSet<String> picklistList = new HashSet<>();

        for (PostResourceMgmtSkills eachSkill : postResourceMgmtSkillsList) {
            if(eachSkill.getSkill()!=null && !eachSkill.getSkill().isEmpty() && eachSkill.getIsActive()!=null && eachSkill.getRowOrder()!=null) {

                eachSkill.setSkill(UserSettingsUtil.removeAllExtraSpace(eachSkill.getSkill()));
                if(picklistList.contains(eachSkill.getSkill().toLowerCase())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachSkill.getSkill().toLowerCase());
                }

                ResourceMgmtSkills resourceMgmtSkills = new ResourceMgmtSkills(eachSkill);
                if (resourceMgmtSkills.getSkillId() == null) {
                    resourceMgmtSkills.setCreatedBy(modifiedBy);
                    resourceMgmtSkills.setCreatedDate(new Date());
                }
                resourceMgmtSkills.setModifiedDate(new Date());
                resourceMgmtSkills.setModifiedBy(modifiedBy);
                updateSkillsList.add(resourceMgmtSkills);
            }
        }
        if(!updateSkillsList.isEmpty()) {
            resourceMgmtSkillsRepo.saveAll(updateSkillsList);
            log.info("User " + modifiedBy + " updated skills at " + new Date());
        }
        return true;
    }

    /*
    Role Settings
     */
    public List<GetResourceMgmtRoles> getResourceMgmtRoles() {
//       List<ResourceMgmtRoles> resourceMgmtRolesList = resourceMgmtRolesRepo.findAll();
        List<GetResourceMgmtRoles> getRolesList= resourceMgmtRolesRepo.getResourceMgmtRoles();
//        List<GetResourceMgmtRoles> getRolesList = new ArrayList<>();
//
//        for (GetResourceMgmtRoles eachRole : resourceMgmtRolesList) {
//            GetResourceMgmtRoles resourceMgmtRoles = new GetResourceMgmtRoles(eachRole);
//            getRolesList.add(resourceMgmtRoles);
//        }
        if(!getRolesList.isEmpty()){
            getRolesList.sort(Comparator.comparing(GetResourceMgmtRoles::getRowOrder));
        }
        return getRolesList;
    }

    public Object updateResourceMgmtRoles(List<PostResourceMgmtRoles> postResourceMgmtRolesList, String modifiedBy) {
        List<ResourceMgmtRoles> updateRolesList = new ArrayList<>();

        HashSet<String> picklistList = new HashSet<>();
        Set<Integer> departmentIdList = new HashSet<>();
        List<GetResourceMgmtDepartment> getDepartmentList = resourceMgmtDepartmentRepo.getActiveDepartmentList();
        departmentIdList = getDepartmentList.stream().map(GetResourceMgmtDepartment::getDepartmentId).collect(Collectors.toSet());
        for (PostResourceMgmtRoles eachRole : postResourceMgmtRolesList) {
            if(eachRole.getRole()!=null && !eachRole.getRole().isEmpty() && eachRole.getIsActive()!=null && eachRole.getRowOrder()!=null) {

                eachRole.setRole(UserSettingsUtil.removeAllExtraSpace(eachRole.getRole()));
                if(picklistList.contains(eachRole.getRole().toLowerCase())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachRole.getRole().toLowerCase());
                }
                ResourceMgmtRoles resourceMgmtRoles = new ResourceMgmtRoles(eachRole);
                if(eachRole.getDepartmentId()!=null){
                    if(departmentIdList.contains(eachRole.getDepartmentId())){
                        resourceMgmtRoles.setDepartmentId(eachRole.getDepartmentId());
                    }
                    else{
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(UserSettingsUtil.INVALID_DEPARTMENT_ID);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }

                if (resourceMgmtRoles.getRoleId() == null) {
                    resourceMgmtRoles.setCreatedBy(modifiedBy);
                    resourceMgmtRoles.setCreatedDate(new Date());
                }
                resourceMgmtRoles.setModifiedDate(new Date());
                resourceMgmtRoles.setModifiedBy(modifiedBy);
                updateRolesList.add(resourceMgmtRoles);
            }
        }
        log.info("User " + modifiedBy + " updated skills at " + new Date());
        resourceMgmtRolesRepo.saveAll(updateRolesList);
        return true;
    }

    public Object downloadCreateRoleTemplateExcelFile(){

        byte[] excelFile = null;
        try {
            excelFile = excelProcessingService.generateCreateRoleTemplateExcelFile();
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        return excelFile;
    }

    public Object createRoleFromExcel(@RequestPart MultipartFile excelFile, String modifiedBy) throws IOException{

        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap = new HashMap<>();
        for(ResourceMgmtDepartment data: resourceMgmtDepartmentList){
            resourceMgmtDepartmentListToMap.put(data.getDepartment().toLowerCase(), data);
        }

//        get all the resource data converted to create resource format
        Map<String, Object> returnData = excelProcessingService.excelToRoleData(excelFile, resourceMgmtDepartmentListToMap);

        if(!(boolean)returnData.get("result")){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.INCORRECT_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        List<ResourceMgmtRoles> resourceMgmtRolesList = resourceMgmtRolesRepo.findAll();
        Map<String, ResourceMgmtRoles> resourceMgmtRolesMap = new HashMap<>();
        for(ResourceMgmtRoles data: resourceMgmtRolesList){
            resourceMgmtRolesMap.put(data.getRole().toLowerCase(), data);
        }

        List<String> result = new ArrayList<>();
        int numberOfFields = (int)returnData.get("numberOfFields");
        List<ResourceMgmtRoles> listOfRoles = (List<ResourceMgmtRoles>)returnData.get("roleList");

        List<ResourceMgmtRoles> rolesToSave = new ArrayList<>();

        if(listOfRoles!=null && !listOfRoles.isEmpty()) {
            for (ResourceMgmtRoles newRole : listOfRoles) {
//                TODO:: process and save role
                if(resourceMgmtRolesMap.containsKey(newRole.getRole().toLowerCase())){
                    result.add("Role already exists");
                }
                else if(newRole.getDepartmentId()==null){
                    result.add("Department does not exist");
                }
                else{
                    newRole.setCreatedBy(modifiedBy);
                    newRole.setCreatedDate(new Date());
                    newRole.setModifiedBy(modifiedBy);
                    newRole.setModifiedDate(new Date());
                    rolesToSave.add(newRole);
                    result.add("Success");
                    resourceMgmtRolesMap.put(newRole.getRole().toLowerCase(), newRole);
                }

            }
        }

        try{
            resourceMgmtRolesRepo.saveAll(rolesToSave);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage("Error while saving roles to database");
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
//
        return excelProcessingService.generateUploadedExcelFileResponse(excelFile, result, numberOfFields);
    }

    /*
    Department Settings
     */
//    public List<GetResourceMgmtDepartment> getResourceMgmtDepartment() {
//        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
//
//        List<GetResourceMgmtDepartment> getDepartmentList = new ArrayList<>();
//
//        for (ResourceMgmtDepartment eachDepartment : resourceMgmtDepartmentList) {
//            GetResourceMgmtDepartment resourceMgmtDepartment = new GetResourceMgmtDepartment(eachDepartment);
//            getDepartmentList.add(resourceMgmtDepartment);
//        }
//        if(!getDepartmentList.isEmpty()){
//            getDepartmentList.sort(Comparator.comparing(GetResourceMgmtDepartment::getRowOrder));
//        }
//        return getDepartmentList;
//    }
//
//    public Object updateResourceMgmtDepartment(List<PostResourceMgmtDepartment> postResourceMgmtDepartmentList, String modifiedBy) {
//        List<ResourceMgmtDepartment> updateDepartmentList = new ArrayList<>();
//
//        HashSet<String> picklistList = new HashSet<>();
//
//        for (PostResourceMgmtDepartment eachDepartment : postResourceMgmtDepartmentList) {
//            if(eachDepartment.getDepartment()!=null && !eachDepartment.getDepartment().isEmpty() && eachDepartment.getIsActive()!=null && eachDepartment.getRowOrder()!=null) {
//
//                eachDepartment.setDepartment(UserSettingsUtil.removeAllExtraSpace(eachDepartment.getDepartment()));
//                if(picklistList.contains(eachDepartment.getDepartment().toLowerCase())){
//                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
//                    Error error = new Error();
//                    error.setRequestAt(new Date());
//                    error.setMessage(message);
//                    error.setStatus(HttpStatus.BAD_REQUEST.value());
//                    return error;
//                }
//                else{
//                    picklistList.add(eachDepartment.getDepartment().toLowerCase());
//                }
//
//                ResourceMgmtDepartment resourceMgmtDepartment = new ResourceMgmtDepartment(eachDepartment);
//                if (resourceMgmtDepartment.getDepartmentId() == null) {
//                    resourceMgmtDepartment.setCreatedBy(modifiedBy);
//                    resourceMgmtDepartment.setCreatedDate(new Date());
//                }
//                resourceMgmtDepartment.setModifiedDate(new Date());
//                resourceMgmtDepartment.setModifiedBy(modifiedBy);
//                updateDepartmentList.add(resourceMgmtDepartment);
//            }
//        }
//        resourceMgmtDepartmentRepo.saveAll(updateDepartmentList);
//        return true;
//    }

    public List<GetResourceMgmtDepartment> getResourceMgmtDepartment() {

        List<GetResourceMgmtDepartment> getDepartmentList = resourceMgmtDepartmentRepo.getActiveDepartmentList();

        if(getDepartmentList.isEmpty()){
            getDepartmentList = null;
        }
        return getDepartmentList;
    }

    public Object updateResourceMgmtDepartment(PostResourceMgmtDepartment postResourceMgmtDepartment, String modifiedBy) {

        if(postResourceMgmtDepartment.getDepartmentId()==null && !postResourceMgmtDepartment.getIsActive()){
            return true;
        }

        if(postResourceMgmtDepartment.getDepartment()!=null && !postResourceMgmtDepartment.getDepartment().isEmpty()) {
            postResourceMgmtDepartment.setDepartment(UserSettingsUtil.removeAllExtraSpace(postResourceMgmtDepartment.getDepartment()));
        }
        else{
            String message = UserSettingsUtil.EMPTY_DEPARTMENT_NAME;
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(message);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

//        retrieve all saved records
        ResourceMgmtDepartment savedRecord = null;

//        to store child records of current record, in the event it is becoming inactive
        List<ResourceMgmtDepartment> childRecords = new ArrayList<>();

        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap = new HashMap<>();

        for(ResourceMgmtDepartment eachDepartment: resourceMgmtDepartmentList){
//            only add if it is active
            if(eachDepartment.getIsActive()) {
                resourceMgmtDepartmentListToMap.put(eachDepartment.getDepartment().toLowerCase(), eachDepartment);

//                if response record has id not null
                if(postResourceMgmtDepartment.getDepartmentId()!=null) {
                    if (eachDepartment.getDepartmentId().equals(postResourceMgmtDepartment.getDepartmentId())) {
                        savedRecord = eachDepartment;
                    }
                    if (!postResourceMgmtDepartment.getIsActive() && eachDepartment.getParentDepartmentId()!=null
                            && eachDepartment.getParentDepartmentId().equals(postResourceMgmtDepartment.getDepartmentId())) {
                        childRecords.add(eachDepartment);
                    }
                }

            }
        }

        List<ResourceMgmtDepartment> recordsToUpdate = new ArrayList<>();
        boolean checkDepartmentWithSameName = true;

//        check if saved record and current record have same department name
        if(savedRecord!=null && savedRecord.getDepartment().equalsIgnoreCase(postResourceMgmtDepartment.getDepartment())){
            checkDepartmentWithSameName = false;
        }

        if(checkDepartmentWithSameName) {
//            check if department with same name exist
            if (resourceMgmtDepartmentListToMap.containsKey(postResourceMgmtDepartment.getDepartment().toLowerCase())) {
                String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES + ": " + postResourceMgmtDepartment.getDepartment();
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }

        if(postResourceMgmtDepartment.getIsActive()!=null && postResourceMgmtDepartment.getRowOrder()!=null) {

            ResourceMgmtDepartment resourceMgmtDepartment = new ResourceMgmtDepartment(postResourceMgmtDepartment);
            if (resourceMgmtDepartment.getDepartmentId() == null) {
                resourceMgmtDepartment.setCreatedBy(modifiedBy);
                resourceMgmtDepartment.setCreatedDate(new Date());
            }

            if(resourceMgmtDepartment.getResourceId()!=null){
                Optional<ResourceMgmt> resourceMgmt = resourceMgmtRepo.findById(resourceMgmtDepartment.getResourceId());
                if(!resourceMgmt.isPresent()) {
                    resourceMgmtDepartment.setResourceId(null);
                }
            }

            resourceMgmtDepartment.setModifiedDate(new Date());
            resourceMgmtDepartment.setModifiedBy(modifiedBy);

            if(!resourceMgmtDepartment.getIsActive()){

//                if the resource is allocated with this departmentId, we are restricting to delete here.
                List<String> resourceMgmtDepartmentIds =resourceMgmtDepartmentRepo.getResourceDepartments();
                for (String DepartmentId :resourceMgmtDepartmentIds){
                    if(DepartmentId.equals(String.valueOf(postResourceMgmtDepartment.getDepartmentId()))){
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(UserSettingsUtil.INVALID_DEPARTMENT_DELETION_RESOURCE);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }

//                if a role is allocated with this departmentId, we are restricting to delete here.
                List<ResourceMgmtRoles> departmentRoles = resourceMgmtRolesRepo.getRolesByDepartmentId(resourceMgmtDepartment.getDepartmentId());
                if(departmentRoles!=null && !departmentRoles.isEmpty()){
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(UserSettingsUtil.INVALID_DEPARTMENT_DELETION_ROLE);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }

//                if record is no longer active, change name to avoid future duplicate issues
                resourceMgmtDepartment.setDepartment("Deleted Department " + resourceMgmtDepartment.getDepartmentId());
                resourceMgmtDepartment.setParentDepartmentId(null);
            }

            recordsToUpdate.add(resourceMgmtDepartment);
        }
        if(!childRecords.isEmpty()){
            for(ResourceMgmtDepartment eachChildRecord: childRecords){
                eachChildRecord.setParentDepartmentId(null);
                eachChildRecord.setModifiedDate(new Date());
                eachChildRecord.setModifiedBy(modifiedBy);
                recordsToUpdate.add(eachChildRecord);
            }
        }
        if(!recordsToUpdate.isEmpty()) {
            resourceMgmtDepartmentRepo.saveAll(recordsToUpdate);
        }
        log.info("User " + modifiedBy + " UpdateResourceMgmtDepartment at " + new Date());
        return true;
    }

    public Object downloadCreateDepartmentTemplateExcelFile(){

        byte[] excelFile = null;
        try {
            excelFile = excelProcessingService.generateCreateDepartmentTemplateExcelFile();
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        return excelFile;
    }

    public Object createDepartmentFromExcel(@RequestPart MultipartFile excelFile, String modifiedBy) throws IOException{
        List<ResourceIdEmail> resourceIdEmailList = resourceMgmtRepo.getAllResourceIdEmail();
        Map<String, ResourceIdEmail> resourceIdEmailMap = new HashMap<>();
        for(ResourceIdEmail data: resourceIdEmailList){
            resourceIdEmailMap.put(data.getEmail(), data);
        }

        List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
        Map<String, ResourceMgmtDepartment> resourceMgmtDepartmentListToMap = new HashMap<>();
        for(ResourceMgmtDepartment data: resourceMgmtDepartmentList){
            resourceMgmtDepartmentListToMap.put(data.getDepartment().toLowerCase(), data);
        }

//        get all the resource data converted to create resource format
        Map<String, Object> returnData = excelProcessingService.excelToDepartmentData(excelFile, resourceIdEmailMap);

        if(!(boolean)returnData.get("result")){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.INCORRECT_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }

        List<String> result = new ArrayList<>();
        int numberOfFields = (int)returnData.get("numberOfFields");
        List<ResourceMgmtDepartment> listOfDepartment = (List<ResourceMgmtDepartment>)returnData.get("departmentList");

        List<ResourceMgmtDepartment> departmentToSave = new ArrayList<>();

        if(listOfDepartment!=null && !listOfDepartment.isEmpty()) {
            for (ResourceMgmtDepartment newDepartment : listOfDepartment) {
//                TODO:: process and save role
                if(resourceMgmtDepartmentListToMap.containsKey(newDepartment.getDepartment().toLowerCase())){
                    ResourceMgmtDepartment existingDepartment = resourceMgmtDepartmentListToMap.get(newDepartment.getDepartment().toLowerCase());
                    if(existingDepartment.getIsActive()){
                        result.add("Department already exists");
                    }
                    else{
                        newDepartment.setDepartmentId(existingDepartment.getDepartmentId());
                        newDepartment.setCreatedBy(modifiedBy);
                        newDepartment.setCreatedDate(new Date());
                        newDepartment.setModifiedBy(modifiedBy);
                        newDepartment.setModifiedDate(new Date());
                        departmentToSave.add(newDepartment);
                        result.add("Success");
                        resourceMgmtDepartmentListToMap.put(newDepartment.getDepartment().toLowerCase(), newDepartment);
                    }
                }
                else{
                    newDepartment.setCreatedBy(modifiedBy);
                    newDepartment.setCreatedDate(new Date());
                    newDepartment.setModifiedBy(modifiedBy);
                    newDepartment.setModifiedDate(new Date());
                    departmentToSave.add(newDepartment);
                    result.add("Success");
                    resourceMgmtDepartmentListToMap.put(newDepartment.getDepartment().toLowerCase(), newDepartment);
                }

            }
        }

        try{
            resourceMgmtDepartmentRepo.saveAll(departmentToSave);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage("Error while saving departments to database");
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
//
        return excelProcessingService.generateUploadedExcelFileResponse(excelFile, result, numberOfFields);
    }

    /*
    Location Settings
     */
    public List<GetGeneralSettingsLocation> getGeneralSettingsLocation() {
        List<GeneralSettingsLocation> generalSettingsLocationList = generalSettingsLocationRepo.findAll();

        List<GetGeneralSettingsLocation> getLocationList = new ArrayList<>();

        for (GeneralSettingsLocation eachLocation : generalSettingsLocationList) {
            GetGeneralSettingsLocation generalSettingsLocation = new GetGeneralSettingsLocation(eachLocation);
            getLocationList.add(generalSettingsLocation);
        }

        if (!getLocationList.isEmpty()) {
            getLocationList.sort(Comparator.comparing(GetGeneralSettingsLocation::getRowOrder));
        }
        
        log.info("GetResourceMgmtRoles url is called at " + new Date());
        return getLocationList;
    }
    public Object updateGeneralSettingsLocation(List<PostGeneralSettingsLocation> postGeneralSettingsLocationList, String modifiedBy) {
        List<GeneralSettingsLocation> updateLocationList = new ArrayList<>();

        HashSet<String> picklistList = new HashSet<>();

        for (PostGeneralSettingsLocation eachLocation : postGeneralSettingsLocationList) {
            if(eachLocation.getLocation()!=null && !eachLocation.getLocation().isEmpty() && eachLocation.getIsActive()!=null && eachLocation.getRowOrder()!=null) {

                eachLocation.setLocation(UserSettingsUtil.removeAllExtraSpace(eachLocation.getLocation()));
                if(picklistList.contains(eachLocation.getLocation().toLowerCase())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachLocation.getLocation().toLowerCase());
                }

                GeneralSettingsLocation generalSettingsLocation = new GeneralSettingsLocation(eachLocation);
                if (generalSettingsLocation.getLocationId() == null || generalSettingsLocation.getLocationId() == 0) {
                    generalSettingsLocation.setLocationId(null);
                    generalSettingsLocation.setCreatedBy(modifiedBy);
                    generalSettingsLocation.setCreatedDate(new Date());
                }
                if (generalSettingsLocation.getParentLocationId() == null || generalSettingsLocation.getParentLocationId() == 0) {
                    generalSettingsLocation.setParentLocationId(null);
                }
                generalSettingsLocation.setModifiedDate(new Date());
                generalSettingsLocation.setModifiedBy(modifiedBy);
                updateLocationList.add(generalSettingsLocation);
            }
        }
        generalSettingsLocationRepo.saveAll(updateLocationList);
        log.info("User " + modifiedBy + " UpdateGeneralSettingsLocation skills at " + new Date());
        return true;
    }

    /*
    Admin Project Management Settings
     */
    public Map<String, List<GetProjectMgmtSettings>> getProjectMgmtSettings() {
        // create response to be sent
        Map<String, List<GetProjectMgmtSettings>> adminProjectManagementJson = new HashMap<>();
        /*
         * Part 1 -> component Settings retrieve admin project management component
         * settings convert them to dto format add to json object
         */

        // temp array object to store the component settings list converted from pojo to
        // Dto
        List<GetProjectMgmtSettings> adminProjectManagementDtoComponentList = new ArrayList<>();
        List<ProjectMgmtFieldMap> allProjectMgmtSettings = projectMgmtFieldMapRepo.findAll();
        Map<Integer, List<ProjectMgmtFieldMap>> objToMap = allProjectMgmtSettings.stream().collect(Collectors.groupingBy(ProjectMgmtFieldMap::getSettingType));
        List<ProjectMgmtFieldMap> adminProjectManagementComponentList = objToMap.get(UserSettingsUtil.COMPONENT_TYPE);
//        System.out.println("component"+adminProjectManagementComponentList);
        // convert components settings to dto format
        for (ProjectMgmtFieldMap adminProjectManagement : adminProjectManagementComponentList) {
            // temp dto object
            GetProjectMgmtSettings adminProjectManagementDto = new GetProjectMgmtSettings(adminProjectManagement);
            adminProjectManagementDtoComponentList.add(adminProjectManagementDto);
        }
        if (!ObjectUtils.isEmpty(adminProjectManagementDtoComponentList)) {
            adminProjectManagementJson.put("componentSettings", adminProjectManagementDtoComponentList);
        }




        /*
         * Part 2-> Field Settings retrieve admin project management field settings
         * convert them to dto format check if the field is a pre-defined picklist if
         * true, add their picklist value in their Dto format setAdminSettingsTableList
         * variable add list to json object
         */
        // temp array object to store the field settings list converted from pojo to Dto
        List<GetProjectMgmtSettings> adminProjectManagementDtoFieldList = new ArrayList<>();
        List<ProjectMgmtFieldMap> adminProjectManagementFieldList = objToMap.get(UserSettingsUtil.FIELD_TYPE);
        if(objToMap.containsKey(UserSettingsUtil.CUSTOM_TYPE)) {
            adminProjectManagementFieldList.addAll(objToMap.get(UserSettingsUtil.CUSTOM_TYPE));
        }
        adminProjectManagementFieldList.addAll(objToMap.get(UserSettingsUtil.SETTING_TYPE));

        // convert components settings to dto format
        for (ProjectMgmtFieldMap adminProjectManagement : adminProjectManagementFieldList) {
            // temp dto object
            GetProjectMgmtSettings adminProjectManagementDto = new GetProjectMgmtSettings(adminProjectManagement);
            List<PicklistFormat> adminPicklistSettingsList = new ArrayList<>();

            // check if its equal to project status, project type, or project priority, if
            // yes, retrieve their table and insert here
            if (Objects.equals(adminProjectManagementDto.getFields(), "Status")) {


                List<ProjectMgmtStatus> projectManagementStatusList = projectManagementStatusRepo.findAll();
                if(!projectManagementStatusList.isEmpty()){
                    projectManagementStatusList.sort(Comparator.comparing(ProjectMgmtStatus::getStatusId));
                }
//                System.out.println("Status"+projectManagementStatusList);

                for (ProjectMgmtStatus projectManagementStatus : projectManagementStatusList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(projectManagementStatus);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }

            } else if (Objects.equals(adminProjectManagementDto.getFields(), "Project Type")) {

                List<ProjectMgmtType> projectManagementTypeList = projectManagementTypeRepo.findAll();
                if(!projectManagementTypeList.isEmpty()){
                    projectManagementTypeList.sort(Comparator.comparing(ProjectMgmtType::getTypeId));
                }
//                System.out.println("Project Type"+projectManagementTypeList);
                for (ProjectMgmtType projectManagementType : projectManagementTypeList) {
                    // this loop converts each row in type to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(projectManagementType);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }

            } else if (Objects.equals(adminProjectManagementDto.getFields(), "Priority")) {
                List<ProjectMgmtPriority> projectManagementPriorityList = projectManagementPriorityRepo.findAll();
                if(!projectManagementPriorityList.isEmpty()){
                    projectManagementPriorityList.sort(Comparator.comparing(ProjectMgmtPriority::getPriorityId));
                }
//                System.out.println("Priority"+projectManagementPriorityList);

                for (ProjectMgmtPriority projectManagementPriority : projectManagementPriorityList) {
                    // this loop converts each row in priority to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(projectManagementPriority);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }

            } else if (Objects.equals(adminProjectManagementDto.getFields(), "Project Owner")) {
                List<ResourceMgmt> projectOwnersList = resourceMgmtRepo.getProjectOwners();
                if(!projectOwnersList.isEmpty()){
                    projectOwnersList.sort(Comparator.comparing(ResourceMgmt::getFirstName));
                }
//                System.out.println(projectOwnersList);

                for (ResourceMgmt eachProjectOwner : projectOwnersList) {
                    // this loop converts each row in resource to table format and adds it to the
                    // adminPicklistSettingsList
                    Date currentDate = new Date();
                    if(eachProjectOwner.getLastWorkingDate()==null || (eachProjectOwner.getLastWorkingDate().compareTo(currentDate)>=0)) {
                        PicklistFormat adminPicklistSettings = new PicklistFormat(eachProjectOwner);
                        adminPicklistSettingsList.add(adminPicklistSettings);
                    }
                }

            } else if (Objects.equals(adminProjectManagementDto.getFields(), "Department")) {
                List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
                if(!resourceMgmtDepartmentList.isEmpty()){
                    resourceMgmtDepartmentList.sort(Comparator.comparing(ResourceMgmtDepartment::getDepartment));
                }
//                System.out.println("Priority"+projectManagementPriorityList);

                for (ResourceMgmtDepartment resourceMgmtDepartment : resourceMgmtDepartmentList) {
                    // this loop converts each row in priority to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(resourceMgmtDepartment);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }

            } else if (adminProjectManagementDto.getFieldType().contains("Picklist")) {
                List<ProjectCustomPicklist> projectCustomPicklistList = projectCustomPicklistRepo.getProjectCustomPicklistByPicklistId(adminProjectManagementDto.getFieldId());
                if(!projectCustomPicklistList.isEmpty()){
                    projectCustomPicklistList.sort(Comparator.comparing(ProjectCustomPicklist::getId));
                }
//                System.out.println("Picklist"+projectCustomPicklistList);
                for (ProjectCustomPicklist projectCustomPicklist : projectCustomPicklistList) {
                    // this loop converts each row in custom to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(projectCustomPicklist);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }

            }
            adminProjectManagementDto.setFieldPicklistValues(adminPicklistSettingsList);
            adminProjectManagementDtoFieldList.add(adminProjectManagementDto);

        }
//        System.out.println("fieldSetting"+adminProjectManagementDtoFieldList);
        // pushing component settings to json object
        if (!ObjectUtils.isEmpty(adminProjectManagementDtoFieldList)) {
            adminProjectManagementDtoFieldList.sort(Comparator.comparingInt(GetProjectMgmtSettings::getFieldId));
            adminProjectManagementJson.put("fieldSettings", adminProjectManagementDtoFieldList);
        }


        /*
         * Part 3-> Bottom Button Settings retrieve admin project management button
         * settings convert them to dto format add to json object
         */
        // temp array object to store the Button settings list converted from pojo to Dto
        List<GetProjectMgmtSettings> adminProjectManagementDtoButtonList = new ArrayList<>();
        List<ProjectMgmtFieldMap> adminProjectManagementButtonList = objToMap.get(UserSettingsUtil.BUTTON_TYPE);

        // convert Buttons settings to dto format
        for (ProjectMgmtFieldMap adminProjectManagement : adminProjectManagementButtonList) {
            // temp dto object
            GetProjectMgmtSettings adminProjectManagementDto = new GetProjectMgmtSettings(adminProjectManagement);
            adminProjectManagementDtoButtonList.add(adminProjectManagementDto);
        }

        if (!ObjectUtils.isEmpty(adminProjectManagementDtoButtonList)) {
            adminProjectManagementJson.put("buttonSettings", adminProjectManagementDtoButtonList);
        }

        return adminProjectManagementJson;
    }

    public Object updateProjectMgmtSettings(Map<String, List<PostProjectMgmtSettings>> updateProjectMgmtSettings, String modifiedBy) {
        List<PostProjectMgmtSettings> postProjectMgmtSettings = new ArrayList<>();
        /*
        Add all the settings from request to the adminPrjMgmtUpdateDto List
         */
        for (Map.Entry<String, List<PostProjectMgmtSettings>> eachCategory : updateProjectMgmtSettings.entrySet()) {
            postProjectMgmtSettings.addAll(eachCategory.getValue());
        }
        List<ProjectMgmtFieldMap> projectMgmtFieldMaps = new ArrayList<>();
        List<ProjectMgmtStatus> projectMgmtStatusList = new ArrayList<>();
        List<ProjectMgmtPriority> projectMgmtPriorityList = new ArrayList<>();
        List<ProjectMgmtType> projectMgmtTypeList = new ArrayList<>();
        List<ProjectCustomPicklist> customPicklist = new ArrayList<>();

        HashSet<String> fieldsMap = new HashSet<>();

        for (PostProjectMgmtSettings eachProjectFieldSetting : postProjectMgmtSettings) {

            eachProjectFieldSetting.setFields(UserSettingsUtil.removeAllExtraSpace(eachProjectFieldSetting.getFields()));
            if(eachProjectFieldSetting.getFields()==null || eachProjectFieldSetting.getFields().isEmpty()){
                String message = UserSettingsUtil.EMPTY_FIELD_NAMES;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
            else if(fieldsMap.contains(eachProjectFieldSetting.getFields().toLowerCase())){
                String message = UserSettingsUtil.DUPLICATE_FIELD_NAMES;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
            else{
                fieldsMap.add(eachProjectFieldSetting.getFields().toLowerCase());
            }

            if((!eachProjectFieldSetting.getSettingType().equals(UserSettingsUtil.COMPONENT_TYPE))
                    && (!eachProjectFieldSetting.getSettingType().equals(UserSettingsUtil.BUTTON_TYPE))) {
                if (!UserSettingsUtil.VALID_FIELD_TYPE.containsKey(eachProjectFieldSetting.getFieldType())) {
                    String message = UserSettingsUtil.INVALID_PROJECT_FIELD_TYPE;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
            }

            ProjectMgmtFieldMap eachField = new ProjectMgmtFieldMap(eachProjectFieldSetting);
            eachField.setModifiedDate(new Date());
            eachField.setModifiedBy(modifiedBy);
//            adminPrjMgmt.setCreatedBy(0);

            if (eachField.getFieldId() == null) {
//                new custom fields
                eachField.setCreatedBy(modifiedBy);
                eachField.setCreatedDate(new Date());
                eachField.setIsEditAccessDdl(true);
                eachField.setIsEnabledVisible(true);
                eachField.setIsEnabledFreeze(false);
                eachField.setIsPriority(false);
                eachField.setIsProjectAnalysisFreeze(false);
                eachField.setIsProjectAnalysisVisible(true);
                eachField.setIsProjectCreationFreeze(false);
                eachField.setIsProjectCreationVisible(true);
                eachField.setIsProjectDetailsFreeze(false);
                eachField.setIsProjectDetailsVisible(true);
                eachField.setIsRange(false);
                eachField.setIsVisibilityDdl(true);
                eachField.setSettingType(3);
                if(eachField.getFieldType().contains(UserSettingsUtil.PICKLIST)){
                    eachField.setIsAddMore(true);
                    if(eachProjectFieldSetting.getFieldPicklistValues()==null || eachProjectFieldSetting.getFieldPicklistValues().isEmpty()){
                        String message = UserSettingsUtil.EMPTY_PICKLIST_VALUES_FOR_TYPE_PICKLIST + ":" + eachProjectFieldSetting.getFields();
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }
                ProjectMgmtFieldMap newField = projectMgmtFieldMapRepo.save(eachField);
                eachField.setFieldId(newField.getFieldId());
            }

            //set all column values
            projectMgmtFieldMaps.add(eachField);
            String fields = String.valueOf(eachProjectFieldSetting.getFields()).trim();
            List<PicklistFormat> adminSettings = eachProjectFieldSetting.getFieldPicklistValues();

            if(eachProjectFieldSetting.getFieldType()!=null && eachProjectFieldSetting.getFieldType().contains(UserSettingsUtil.PICKLIST)){
                if (!eachProjectFieldSetting.getSettingType().equals(UserSettingsUtil.SETTING_TYPE) && adminSettings != null && !adminSettings.isEmpty()) {
                    for (PicklistFormat eachAdminSetting : adminSettings) {
                        eachAdminSetting.setName(UserSettingsUtil.removeAllExtraSpace(eachAdminSetting.getName()));
//                    check for empty picklist names
                        if(eachAdminSetting.getName()==null || eachAdminSetting.getName().isEmpty()){
                            String message = UserSettingsUtil.EMPTY_PICKLIST_VALUES;
                            Error error = new Error();
                            error.setRequestAt(new Date());
                            error.setMessage(message);
                            error.setStatus(HttpStatus.BAD_REQUEST.value());
                            return error;
                        }
                        switch (fields) {
                            case "Project Type":
                                ProjectMgmtType eachProjectType = new ProjectMgmtType(eachAdminSetting);
                                eachProjectType.setModifiedDate(new Date());
                                eachProjectType.setModifiedBy(modifiedBy);
                                if(eachProjectType.getTypeId()==null) {
                                    eachProjectType.setCreatedBy(modifiedBy);
                                }
                                //set all properties
                                projectMgmtTypeList.add(eachProjectType);
                                break;
                            case "Status":
                                ProjectMgmtStatus eachProjectStatus = new ProjectMgmtStatus(eachAdminSetting);
                                eachProjectStatus.setModifiedDate(new Date());
                                eachProjectStatus.setModifiedBy(modifiedBy);
                                if(eachProjectStatus.getStatusId()==null) {
                                    eachProjectStatus.setCreatedBy(modifiedBy);
                                }
                                //set all properties
                                projectMgmtStatusList.add(eachProjectStatus);
                                break;
                            case "Priority":
                                ProjectMgmtPriority eachProjectPriority = new ProjectMgmtPriority(eachAdminSetting);
                                eachProjectPriority.setModifiedDate(new Date());
                                eachProjectPriority.setModifiedBy(modifiedBy);
                                if(eachProjectPriority.getPriorityId()==null) {
                                    eachProjectPriority.setCreatedBy(modifiedBy);
                                }
                                //set all properties
                                projectMgmtPriorityList.add(eachProjectPriority);
                                break;
                            default:
                                System.out.println("Custom Picklist Values: {}" + fields);
                                ProjectCustomPicklist eachProjectCustom = new ProjectCustomPicklist(eachAdminSetting);
                                if (eachProjectCustom.getId() == null) {
                                    eachProjectCustom.setCreatedDate(new Date());
                                    eachProjectCustom.setCreatedBy(modifiedBy);
                                }
                                eachProjectCustom.setModifiedDate(new Date());
                                eachProjectCustom.setModifiedBy(modifiedBy);
                                eachProjectCustom.setPicklistId(eachField.getFieldId());
                                customPicklist.add(eachProjectCustom);
                                break;
                        }
                    }
                }
                else{
                    if(!eachProjectFieldSetting.getSettingType().equals(UserSettingsUtil.SETTING_TYPE)) {
                        String message = UserSettingsUtil.EMPTY_PICKLIST_VALUES_FOR_TYPE_PICKLIST + ":" + eachProjectFieldSetting.getFields();
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }
            }
        }
        //Settings
        if (!projectMgmtFieldMaps.isEmpty()) {
            List<ProjectMgmtFieldMap> storedFieldSettings = projectMgmtFieldMapRepo.findAll();
            Map<Integer, ProjectMgmtFieldMap> storedFieldSettingsToMap
                    = storedFieldSettings.stream().collect(Collectors.toMap(ProjectMgmtFieldMap::getFieldId, fieldSetting-> fieldSetting));

            List<ProjectMgmtFieldMap> updatedFieldSettings = new ArrayList<>();

            if(!storedFieldSettings.isEmpty()) {
                for(ProjectMgmtFieldMap eachFieldSetting: projectMgmtFieldMaps){

//                    pre-existing fields
                    if(eachFieldSetting.getFieldId()!=null && storedFieldSettingsToMap.containsKey(eachFieldSetting.getFieldId())){

                        ProjectMgmtFieldMap fieldSettingToUpdate = storedFieldSettingsToMap.get(eachFieldSetting.getFieldId());

//                        is enabled
                        if(fieldSettingToUpdate.getIsEnabledVisible() && !fieldSettingToUpdate.getIsEnabledFreeze()){
                            if(eachFieldSetting.getIsEnabled()!=null) {
                                fieldSettingToUpdate.setIsEnabled(eachFieldSetting.getIsEnabled());
                            }
                            else{
                                fieldSettingToUpdate.setIsEnabled(true);
                            }
                        }
                        
//                        project analytics
                        if(fieldSettingToUpdate.getIsProjectAnalysisVisible() && !fieldSettingToUpdate.getIsProjectAnalysisFreeze()){
                            if(eachFieldSetting.getProjAnalysisView()!=null) {
                                fieldSettingToUpdate.setProjAnalysisView(eachFieldSetting.getProjAnalysisView());
                            }
                            else{
                                fieldSettingToUpdate.setProjAnalysisView(true);
                            }
                        }
                        
//                        project details
                        if(fieldSettingToUpdate.getIsProjectDetailsVisible() && !fieldSettingToUpdate.getIsProjectDetailsFreeze()){
                            if(eachFieldSetting.getProjDetailsView()!=null) {
                                fieldSettingToUpdate.setProjDetailsView(eachFieldSetting.getProjDetailsView());
                            }
                            else{
                                fieldSettingToUpdate.setProjDetailsView(true);
                            }
                        }
                        
//                        project creation
                        if(fieldSettingToUpdate.getIsProjectCreationVisible() && !fieldSettingToUpdate.getIsProjectCreationFreeze()){
                            if(eachFieldSetting.getProjCreationView()!=null) {
                                fieldSettingToUpdate.setProjCreationView(eachFieldSetting.getProjCreationView());
                            }
                            else{
                                fieldSettingToUpdate.setProjCreationView(true);
                            }
                        }
                        
                        updatedFieldSettings.add(fieldSettingToUpdate);
                    }
                }
                
                projectMgmtFieldMapRepo.saveAll(updatedFieldSettings);
            }
        }
        //Status
        if (!projectMgmtStatusList.isEmpty()) {

            HashSet<String> picklistList = new HashSet<>();

            for(ProjectMgmtStatus eachPicklist: projectMgmtStatusList){
                if(picklistList.contains(eachPicklist.getStatus())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachPicklist.getStatus());
                }
            }

            projectManagementStatusRepo.saveAll(projectMgmtStatusList);
        }
        //Priority
        if (!projectMgmtPriorityList.isEmpty()) {

            HashSet<String> picklistList = new HashSet<>();

            for(ProjectMgmtPriority eachPicklist: projectMgmtPriorityList){
                if(picklistList.contains(eachPicklist.getPriority())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachPicklist.getPriority());
                }
            }

            projectManagementPriorityRepo.saveAll(projectMgmtPriorityList);
        }
        //Type
        if (!projectMgmtTypeList.isEmpty()) {

            HashSet<String> picklistList = new HashSet<>();

            for(ProjectMgmtType eachPicklist: projectMgmtTypeList){
                if(picklistList.contains(eachPicklist.getType())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachPicklist.getType());
                }
            }

            projectManagementTypeRepo.saveAll(projectMgmtTypeList);
        }
        if (!customPicklist.isEmpty()) {

            Map<String, HashSet<Integer>> picklistList = new HashMap<>();

            for(ProjectCustomPicklist eachPicklist: customPicklist){

                HashSet<Integer> picklistIds;

                if(picklistList.containsKey(eachPicklist.getPicklistValue())){

                    picklistIds = picklistList.get(eachPicklist.getPicklistValue());

                    if(picklistIds.contains(eachPicklist.getPicklistId())){
                        String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }
                else{
                    picklistIds = new HashSet<>();
                }
                picklistIds.add(eachPicklist.getPicklistId());
                picklistList.put(eachPicklist.getPicklistValue(), picklistIds);
            }

            projectCustomPicklistRepo.saveAll(customPicklist);
        }
        log.info("User " + modifiedBy + " UpdatePrjMgmtSettings skills at " + new Date());
        return true;
    }

    /*
    Admin Resource Management Settings
     */
    public Map<String, List<GetResourceMgmtSettings>> getResourceMgmtSettings() {
        // create response to be sent
        Map<String, List<GetResourceMgmtSettings>> adminResourceManagementJson = new HashMap<>();
        /*
         * Part 1 -> component Settings retrieve admin resource management component
         * settings convert them to dto format add to json object
         */

        // temp array object to store the component settings list converted from pojo to
        // Dto
        List<GetResourceMgmtSettings> adminResourceManagementDtoComponentList = new ArrayList<>();
        List<ResourceMgmtFieldMap> allResourceMgmtSettings = resourceMgmtFieldMapRepo.findAll();
        Map<Integer, List<ResourceMgmtFieldMap>> objToMap = allResourceMgmtSettings.stream().collect(Collectors.groupingBy(ResourceMgmtFieldMap::getSettingType));

        List<ResourceMgmtFieldMap> adminResourceManagementComponentList = objToMap.get(UserSettingsUtil.COMPONENT_TYPE);
        // convert components settings to dto format
        for (ResourceMgmtFieldMap adminResourceManagement : adminResourceManagementComponentList) {
            // temp dto object
            GetResourceMgmtSettings adminResourceManagementDto = new GetResourceMgmtSettings(
                    adminResourceManagement);
            adminResourceManagementDtoComponentList.add(adminResourceManagementDto);
        }
        if (!ObjectUtils.isEmpty(adminResourceManagementDtoComponentList)) {
            adminResourceManagementJson.put("componentSettings", adminResourceManagementDtoComponentList);
        }

        /*
         * Part 2-> Field Settings retrieve admin Resource management field settings
         * convert them to dto format check if the field is a pre-defined picklist if
         * true, add their picklist value in their Dto format setAdminSettingsTableList
         * variable add list to json object
         */
        // temp array object to store the field settings list converted from pojo to Dto
        List<GetResourceMgmtSettings> adminResourceManagementDtoFieldList = new ArrayList<>();
        List<ResourceMgmtFieldMap> adminResourceManagementFieldList = objToMap.get(UserSettingsUtil.FIELD_TYPE);
        if(objToMap.containsKey(UserSettingsUtil.CUSTOM_TYPE)) {
            adminResourceManagementFieldList.addAll(objToMap.get(UserSettingsUtil.CUSTOM_TYPE));
        }
        adminResourceManagementFieldList.addAll(objToMap.get(UserSettingsUtil.SETTING_TYPE));

        // convert components settings to dto format
        for (ResourceMgmtFieldMap adminResourceManagement : adminResourceManagementFieldList) {
            // temp dto object
            GetResourceMgmtSettings adminResourceManagementDto = new GetResourceMgmtSettings(
                    adminResourceManagement);
            List<PicklistFormat> adminPicklistSettingsList = new ArrayList<>();

            // check if its equal to resource status, resource employment type, or custom
            // picklist
            // if yes, retrieve their table and insert here
            if (Objects.equals(adminResourceManagementDto.getFields(), "Status")) {
                List<ResourceMgmtStatus> resourceManagementStatusList = resourceManagementStatusRepo.findAll();
                if(!resourceManagementStatusList.isEmpty()){
                    resourceManagementStatusList.sort(Comparator.comparing(ResourceMgmtStatus::getStartValue));
                }
                for (ResourceMgmtStatus resourceManagementStatus : resourceManagementStatusList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(resourceManagementStatus);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }
            } else if (Objects.equals(adminResourceManagementDto.getFields(), "Employment Type")) {
                List<ResourceMgmtEmploymentType> resourceManagementEmploymentTypeList = resourceManagementEmploymentTypeRepo
                        .findAll();
                if(!resourceManagementEmploymentTypeList.isEmpty()){
                    resourceManagementEmploymentTypeList.sort(Comparator.comparing(ResourceMgmtEmploymentType::getEmpTypeId));
                }
                for (ResourceMgmtEmploymentType resourceManagementEmploymentType : resourceManagementEmploymentTypeList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(
                            resourceManagementEmploymentType);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }
            } else if (Objects.equals(adminResourceManagementDto.getFields(), "Role")) {
                List<ResourceMgmtRoles> resourceMgmtRolesList = resourceMgmtRolesRepo.findAll();
                if(!resourceMgmtRolesList.isEmpty()){
                    resourceMgmtRolesList.sort(Comparator.comparing(ResourceMgmtRoles::getRowOrder));
                }
                for (ResourceMgmtRoles resourceMgmtRoles : resourceMgmtRolesList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    if(resourceMgmtRoles.getIsActive()) {
                        PicklistFormat adminPicklistSettings = new PicklistFormat(resourceMgmtRoles);
                        adminPicklistSettingsList.add(adminPicklistSettings);
                    }
                }
            } else if (Objects.equals(adminResourceManagementDto.getFields(), "Skills")) {
                List<ResourceMgmtSkills> resourceMgmtSkillsList = resourceMgmtSkillsRepo.findAll();
                if(!resourceMgmtSkillsList.isEmpty()){
                    resourceMgmtSkillsList.sort(Comparator.comparing(ResourceMgmtSkills::getRowOrder));
                }
                for (ResourceMgmtSkills resourceMgmtSkills : resourceMgmtSkillsList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    if(resourceMgmtSkills.getIsActive()) {
                        PicklistFormat adminPicklistSettings = new PicklistFormat(resourceMgmtSkills);
                        adminPicklistSettingsList.add(adminPicklistSettings);
                    }
                }
            } else if (Objects.equals(adminResourceManagementDto.getFields(), "Department")) {
                List<ResourceMgmtDepartment> resourceMgmtDepartmentList = resourceMgmtDepartmentRepo.findAll();
                if(!resourceMgmtDepartmentList.isEmpty()){
                    resourceMgmtDepartmentList.sort(Comparator.comparing(ResourceMgmtDepartment::getRowOrder));
                }
                for (ResourceMgmtDepartment resourceMgmtDepartment : resourceMgmtDepartmentList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    if(resourceMgmtDepartment.getIsActive()) {
                        PicklistFormat adminPicklistSettings = new PicklistFormat(resourceMgmtDepartment);
                        adminPicklistSettingsList.add(adminPicklistSettings);
                    }
                }
            } else if (Objects.equals(adminResourceManagementDto.getFields(), "Location")) {
                List<GeneralSettingsLocation> generalSettingsLocationList = generalSettingsLocationRepo.findAll();
                if(!generalSettingsLocationList.isEmpty()){
                    generalSettingsLocationList.sort(Comparator.comparing(GeneralSettingsLocation::getRowOrder));
                }
                for (GeneralSettingsLocation generalSettingsLocation : generalSettingsLocationList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    if(generalSettingsLocation.getIsActive()) {
                        PicklistFormat adminPicklistSettings = new PicklistFormat(generalSettingsLocation);
                        adminPicklistSettingsList.add(adminPicklistSettings);
                    }
                }
            } else if (Objects.equals(adminResourceManagementDto.getFields(), "Reporting Manager")) {
                List<ResourceMgmt> reportingManagersList = resourceMgmtRepo.getReportingManagers();
                if(!reportingManagersList.isEmpty()){
                    reportingManagersList.sort(Comparator.comparing(ResourceMgmt::getFirstName));
                }
                Date currentDate = new Date();
                for (ResourceMgmt resourceMgmt : reportingManagersList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    if(resourceMgmt.getLastWorkingDate()==null || (resourceMgmt.getLastWorkingDate().compareTo(currentDate)>=0)) {
                        PicklistFormat adminPicklistSettings = new PicklistFormat(resourceMgmt);
                        adminPicklistSettingsList.add(adminPicklistSettings);
                    }
                }
            } else if (adminResourceManagementDto.getResourceType().contains("Picklist")) {
                List<ResourceCustomPicklist> resourceCustomPicklistList = resourceCustomPicklistRepo
                        .getResourceCustomPicklistByPicklistId(adminResourceManagementDto.getFieldId());
                if(!resourceCustomPicklistList.isEmpty()){
                    resourceCustomPicklistList.sort(Comparator.comparing(ResourceCustomPicklist::getId));
                }

                for (ResourceCustomPicklist resourceCustomPicklist : resourceCustomPicklistList) {
                    // this loop converts each row in status to table format and adds it to the
                    // adminPicklistSettingsList
                    PicklistFormat adminPicklistSettings = new PicklistFormat(resourceCustomPicklist);
                    adminPicklistSettingsList.add(adminPicklistSettings);
                }

            }
            adminResourceManagementDto.setFieldPicklistValues(adminPicklistSettingsList);
            adminResourceManagementDtoFieldList.add(adminResourceManagementDto);
        }

        // pushing component settings to json object
        if (!ObjectUtils.isEmpty(adminResourceManagementDtoFieldList)) {
            adminResourceManagementDtoFieldList.sort(Comparator.comparingInt(GetResourceMgmtSettings::getFieldId));
            adminResourceManagementJson.put("fieldSettings", adminResourceManagementDtoFieldList);
        }


        /*
         * Part 3-> Bottom Button Settings retrieve admin Resource management button
         * settings convert them to dto format add to json object
         */
        // temp array object to store the Button settings list converted from pojo to
        // Dto
        List<GetResourceMgmtSettings> adminResourceManagementDtoButtonList = new ArrayList<>();
        List<ResourceMgmtFieldMap> adminResourceManagementButtonList = objToMap.get(UserSettingsUtil.BUTTON_TYPE);

        if(adminResourceManagementButtonList==null){
            adminResourceManagementButtonList = new ArrayList<>();
        }
        // convert Buttons settings to dto format
        for (ResourceMgmtFieldMap adminResourceManagement : adminResourceManagementButtonList) {
            // temp dto object
            GetResourceMgmtSettings adminResourceManagementDto = new GetResourceMgmtSettings(
                    adminResourceManagement);
            adminResourceManagementDtoButtonList.add(adminResourceManagementDto);
        }

        if (!ObjectUtils.isEmpty(adminResourceManagementDtoButtonList)) {
            adminResourceManagementJson.put("buttonSettings", adminResourceManagementDtoButtonList);
        }
        return adminResourceManagementJson;
    }

    public Object updateResourceMgmtSettings(Map<String, List<PostResourceMgmtSettings>> updateResourceMgmtSettings, String modifiedBy) {
        List<PostResourceMgmtSettings> postResourceMgmtSettings = new ArrayList<>();
        /*
        Add all the settings from request to the adminPrjMgmtUpdateDto List
         */
        for (Map.Entry<String, List<PostResourceMgmtSettings>> eachCategory : updateResourceMgmtSettings.entrySet()) {
            postResourceMgmtSettings.addAll(eachCategory.getValue());
        }
        List<ResourceMgmtFieldMap> resourceMgmtFieldMaps = new ArrayList<>();
        List<ResourceMgmtStatus> resourceMgmtStatusList = new ArrayList<>();
        List<ResourceMgmtEmploymentType> resourceMgmtEmploymentTypeList = new ArrayList<>();
        List<ResourceCustomPicklist> customPicklist = new ArrayList<>();

        HashSet<String> fieldsMap = new HashSet<>();

        for (PostResourceMgmtSettings eachResourceFieldSetting : postResourceMgmtSettings) {

            eachResourceFieldSetting.setFields(UserSettingsUtil.removeAllExtraSpace(eachResourceFieldSetting.getFields()));

            if(eachResourceFieldSetting.getFields()==null || eachResourceFieldSetting.getFields().isEmpty()){
                String message = UserSettingsUtil.EMPTY_FIELD_NAMES;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
            else if(fieldsMap.contains(eachResourceFieldSetting.getFields().toLowerCase())){
                String message = UserSettingsUtil.DUPLICATE_FIELD_NAMES;
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage(message);
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
            else{
                fieldsMap.add(eachResourceFieldSetting.getFields().toLowerCase());
            }

            if((!eachResourceFieldSetting.getSettingType().equals(UserSettingsUtil.COMPONENT_TYPE))
                    && (!eachResourceFieldSetting.getSettingType().equals(UserSettingsUtil.BUTTON_TYPE))) {
                if (!UserSettingsUtil.VALID_FIELD_TYPE.containsKey(eachResourceFieldSetting.getResourceType())) {
                    String message = UserSettingsUtil.INVALID_RESOURCE_FIELD_TYPE;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
            }

            ResourceMgmtFieldMap eachField = new ResourceMgmtFieldMap(eachResourceFieldSetting);
            eachField.setModifiedDate(new Date());
            eachField.setModifiedBy(modifiedBy);

            if (eachField.getFieldId() == null) {
//                new custom field
                eachField.setCreatedBy(modifiedBy);
                eachField.setCreatedDate(new Date());
                eachField.setIsEditAccessDdl(true);
                eachField.setIsEnabledVisible(true);
                eachField.setIsEnabledFreeze(false);
                eachField.setIsPriority(false);
                eachField.setIsResourceAnalysisFreeze(false);
                eachField.setIsResourceAnalysisVisible(true);
                eachField.setIsResourceCreationFreeze(false);
                eachField.setIsResourceCreationVisible(true);
                eachField.setIsResourceCardFreeze(false);
                eachField.setIsResourceCardVisible(true);
                eachField.setIsRange(false);
                eachField.setIsVisibilityDdl(true);
                eachField.setSettingType(3);
                if(eachField.getResourceType().contains(UserSettingsUtil.PICKLIST)){
                    eachField.setIsAddMore(true);
                    if(eachResourceFieldSetting.getFieldPicklistValues()==null || eachResourceFieldSetting.getFieldPicklistValues().isEmpty()){
                        String message = UserSettingsUtil.EMPTY_PICKLIST_VALUES_FOR_TYPE_PICKLIST + ":" + eachResourceFieldSetting.getFields();
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }
                ResourceMgmtFieldMap newField = resourceMgmtFieldMapRepo.save(eachField);
                eachField.setFieldId(newField.getFieldId());
            }


            //set all column values
            resourceMgmtFieldMaps.add(eachField);
            //Now build other entities
            String fields = String.valueOf(eachResourceFieldSetting.getFields()).trim();
            List<PicklistFormat> adminSettings = eachResourceFieldSetting.getFieldPicklistValues();

            if(eachResourceFieldSetting.getResourceType()!=null && eachResourceFieldSetting.getResourceType().contains(UserSettingsUtil.PICKLIST)) {
                if (!eachResourceFieldSetting.getSettingType().equals(UserSettingsUtil.SETTING_TYPE) && adminSettings != null && !adminSettings.isEmpty()) {
                    for (PicklistFormat eachAdminSetting : adminSettings) {
                        eachAdminSetting.setName(UserSettingsUtil.removeAllExtraSpace(eachAdminSetting.getName()));

//                    check for empty picklist names
                        if (eachAdminSetting.getName() == null || eachAdminSetting.getName().isEmpty()) {
                            String message = UserSettingsUtil.EMPTY_PICKLIST_VALUES;
                            Error error = new Error();
                            error.setRequestAt(new Date());
                            error.setMessage(message);
                            error.setStatus(HttpStatus.BAD_REQUEST.value());
                            return error;
                        }
                        switch (fields) {
                            case "Status":
                                ResourceMgmtStatus resourceMgmtStatus = new ResourceMgmtStatus(eachAdminSetting);
                                //set all properties
                                resourceMgmtStatus.setModifiedDate(new Date());
                                resourceMgmtStatus.setModifiedBy(modifiedBy);
                                if (resourceMgmtStatus.getStatusId() == null) {
                                    resourceMgmtStatus.setCreatedBy(modifiedBy);
                                    resourceMgmtStatus.setCreatedDate(new Date());
                                }
                                if(resourceMgmtStatus.getStartValue()!=null && resourceMgmtStatus.getEndValue()!=null) {
                                    resourceMgmtStatusList.add(resourceMgmtStatus);
                                }
                                break;
                            case "Employment Type":
                                ResourceMgmtEmploymentType resourceMgmtEmploymentType = new ResourceMgmtEmploymentType(eachAdminSetting);
                                resourceMgmtEmploymentType.setModifiedDate(new Date());
                                resourceMgmtEmploymentType.setModifiedBy(modifiedBy);
                                if (resourceMgmtEmploymentType.getEmpTypeId() == null) {
                                    resourceMgmtEmploymentType.setCreatedBy(modifiedBy);
                                }
                                //set all properties
                                resourceMgmtEmploymentTypeList.add(resourceMgmtEmploymentType);
                                break;
                            default:
//                                System.out.println("Unrecognized field type: {}" + fields);
                                ResourceCustomPicklist resourceCustomPicklist = new ResourceCustomPicklist(eachAdminSetting);
                                if (resourceCustomPicklist.getId() == null) {
                                    resourceCustomPicklist.setCreatedDate(new Date());
                                    resourceCustomPicklist.setCreatedBy(modifiedBy);
                                }
                                resourceCustomPicklist.setModifiedDate(new Date());
                                resourceCustomPicklist.setModifiedBy(modifiedBy);
                                resourceCustomPicklist.setPicklistId(eachField.getFieldId());
                                customPicklist.add(resourceCustomPicklist);
                                break;
                        }
                    }
                }
                else{
                    if(!eachResourceFieldSetting.getSettingType().equals(UserSettingsUtil.SETTING_TYPE)) {
                        String message = UserSettingsUtil.EMPTY_PICKLIST_VALUES_FOR_TYPE_PICKLIST + ":" + eachResourceFieldSetting.getFields();
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }
            }
        }
        //Settings
        if (!resourceMgmtFieldMaps.isEmpty()) {
            List<ResourceMgmtFieldMap> storedFieldSettings = resourceMgmtFieldMapRepo.findAll();
            Map<Integer, ResourceMgmtFieldMap> storedFieldSettingsToMap
                    = storedFieldSettings.stream().collect(Collectors.toMap(ResourceMgmtFieldMap::getFieldId, fieldSetting-> fieldSetting));

            List<ResourceMgmtFieldMap> updatedFieldSettings = new ArrayList<>();

            if(!storedFieldSettings.isEmpty()) {
                for(ResourceMgmtFieldMap eachFieldSetting: resourceMgmtFieldMaps){

//                    pre-existing fields
                    if(eachFieldSetting.getFieldId()!=null && storedFieldSettingsToMap.containsKey(eachFieldSetting.getFieldId())){

                        ResourceMgmtFieldMap fieldSettingToUpdate = storedFieldSettingsToMap.get(eachFieldSetting.getFieldId());

//                        is enabled
                        if(fieldSettingToUpdate.getIsEnabledVisible() && !fieldSettingToUpdate.getIsEnabledFreeze()){
                            if(eachFieldSetting.getEnabled()!=null) {
                                fieldSettingToUpdate.setEnabled(eachFieldSetting.getEnabled());
                            }
                            else{
                                fieldSettingToUpdate.setEnabled(true);
                            }
                        }

//                        project analytics
                        if(fieldSettingToUpdate.getIsResourceAnalysisVisible() && !fieldSettingToUpdate.getIsResourceAnalysisFreeze()){
                            if(eachFieldSetting.getResourceAnalyticsView()!=null) {
                                fieldSettingToUpdate.setResourceAnalyticsView(eachFieldSetting.getResourceAnalyticsView());
                            }
                            else{
                                fieldSettingToUpdate.setResourceAnalyticsView(true);
                            }
                        }

//                        project details
                        if(fieldSettingToUpdate.getIsResourceCardVisible() && !fieldSettingToUpdate.getIsResourceCardFreeze()){
                            if(eachFieldSetting.getResourcesCardView()!=null) {
                                fieldSettingToUpdate.setResourcesCardView(eachFieldSetting.getResourcesCardView());
                            }
                            else{
                                fieldSettingToUpdate.setResourcesCardView(true);
                            }
                        }

//                        project creation
                        if(fieldSettingToUpdate.getIsResourceCreationVisible() && !fieldSettingToUpdate.getIsResourceCreationFreeze()){
                            if(eachFieldSetting.getResourceCreationView()!=null) {
                                fieldSettingToUpdate.setResourceCreationView(eachFieldSetting.getResourceCreationView());
                            }
                            else{
                                fieldSettingToUpdate.setResourceCreationView(true);
                            }
                        }

                        updatedFieldSettings.add(fieldSettingToUpdate);
                    }
                }

                resourceMgmtFieldMapRepo.saveAll(updatedFieldSettings);
            }
        }
        //Status
        if (!resourceMgmtStatusList.isEmpty()) {

            HashSet<String> picklistList = new HashSet<>();

            for(ResourceMgmtStatus eachPicklist: resourceMgmtStatusList){
                if(picklistList.contains(eachPicklist.getStatus())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachPicklist.getStatus());
                }
            }

            resourceManagementStatusRepo.saveAll(resourceMgmtStatusList);
        }
        //Priority
        if (!resourceMgmtEmploymentTypeList.isEmpty()) {

            HashSet<String> picklistList = new HashSet<>();

            for(ResourceMgmtEmploymentType eachPicklist: resourceMgmtEmploymentTypeList){
                if(picklistList.contains(eachPicklist.getEmploymentType())){
                    String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(message);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
                else{
                    picklistList.add(eachPicklist.getEmploymentType());
                }
            }

            resourceManagementEmploymentTypeRepo.saveAll(resourceMgmtEmploymentTypeList);
        }
        if (!customPicklist.isEmpty()) {

//            we are making sure that a single custom picklist does not have more than one picklist value with same name
            Map<String, HashSet<Integer>> picklistList = new HashMap<>();

            for(ResourceCustomPicklist eachPicklist: customPicklist){

                HashSet<Integer> picklistIds;

                if(picklistList.containsKey(eachPicklist.getPicklistValue())){

                    picklistIds = picklistList.get(eachPicklist.getPicklistValue());

                    if(picklistIds.contains(eachPicklist.getPicklistId())){
                        String message = UserSettingsUtil.DUPLICATE_PICKLIST_VALUES;
                        Error error = new Error();
                        error.setRequestAt(new Date());
                        error.setMessage(message);
                        error.setStatus(HttpStatus.BAD_REQUEST.value());
                        return error;
                    }
                }
                else{
                    picklistIds = new HashSet<>();
                }
                picklistIds.add(eachPicklist.getPicklistId());
                picklistList.put(eachPicklist.getPicklistValue(), picklistIds);
            }

            resourceCustomPicklistRepo.saveAll(customPicklist);
        }
        log.info("User " + modifiedBy + " UpdateResourseMgmtSettings skills at " + new Date());
        return true;
    }

    /*
    Admin Finance Management Settings
     */
    public GetFinanceMgmtSettings getFinanceMgmtSettings() {
        // create response to be sent
        GetFinanceMgmtSettings adminFinanceManagementJson = new GetFinanceMgmtSettings();
        /*
         * Part 1 -> component Settings retrieve admin finance management component
         * settings convert them to dto format add to json object
         */

        List<FinanceMgmtFieldMap> adminFinanceManagementComponentList = financeMgmtFieldMapRepo.getAdminFinanceManagementComponents();

//        list to store component settings converted to Dto
        List<FinanceMgmtAdminComponentsDto> financeMgmtAdminComponentDto = new ArrayList<>();

        for (FinanceMgmtFieldMap eachComponent : adminFinanceManagementComponentList) {
            FinanceMgmtAdminComponentsDto adminComponents = new FinanceMgmtAdminComponentsDto(eachComponent);
            financeMgmtAdminComponentDto.add(adminComponents);
        }

//        adding component settings to be sent to response
        adminFinanceManagementJson.setComponentSettings(financeMgmtAdminComponentDto);

        /*
         * Part 2-> Field Settings retrieve admin Finance management field settings
         */

//        retrieve button settings of tables, as well as their id, which here, is tableType
        List<FinanceMgmtFieldMap> adminFinanceButtonSettings = financeMgmtFieldMapRepo.getAdminFinanceManagementButtons();

//        list to store all tables and their fields setting
        List<FinanceMgmtTableSettings> adminFinanceTableSettings = new ArrayList<>();

//        going through each table

        for (FinanceMgmtFieldMap eachTable : adminFinanceButtonSettings) {

//            assign table settings
            FinanceMgmtTableSettings financeMgmtTableSettings = new FinanceMgmtTableSettings();
            financeMgmtTableSettings.setTableName(eachTable.getFields());
            financeMgmtTableSettings.setIsEnabled(eachTable.getEnabled().equals("1"));
            financeMgmtTableSettings.setTableType(eachTable.getTableType());

//            retrieve all the table field settings based on its table type id
            List<FinanceMgmtFieldMap> allTableFields = financeMgmtFieldMapRepo.getAdminFinanceManagementTableFields(eachTable.getTableType());

//            list to store all the fields settings converted to Dto format
            List<FinanceMgmtAdminFieldsDto> allTableFieldsDto = new ArrayList<>();

//            for each field, convert them to dto
            for (FinanceMgmtFieldMap eachField : allTableFields) {

                FinanceMgmtAdminFieldsDto eachFieldDto = new FinanceMgmtAdminFieldsDto(eachField);

//                check if they are of type picklist and are custom field settings? if yes, retrieve their picklist values
                if (eachField.getFieldType() != null && eachField.getFieldType().contains("Picklist") && Objects.equals(eachField.getSettingType(), UserSettingsUtil.CUSTOM_TYPE)) {
                    List<FinanceCustomPicklist> fieldPicklistValues = financeCustomPicklistRepo.getFinanceCustomPicklistByPicklistId(eachField.getFieldId());

                    if (fieldPicklistValues != null && !fieldPicklistValues.isEmpty()) {
                        fieldPicklistValues.sort(Comparator.comparing(FinanceCustomPicklist::getId));

                        List<PicklistFormat> financeCustomPicklistList = new ArrayList<>();

                        for (FinanceCustomPicklist eachPicklistValue : fieldPicklistValues) {
                            PicklistFormat eachPicklistValueDto = new PicklistFormat(eachPicklistValue);
                            financeCustomPicklistList.add(eachPicklistValueDto);
                        }
//                        add picklist values, if they exist, to the field Dto object
                        eachFieldDto.setAdminPicklistSettingsList(financeCustomPicklistList);
                    }
                }

//                add the field to the list of fields of the table
                allTableFieldsDto.add(eachFieldDto);
            }
            allTableFieldsDto.sort(Comparator.comparingInt(FinanceMgmtAdminFieldsDto::getFieldId));
//            add the table field settings to the financeMgmtTableSettings object, which contains this table's settings
            financeMgmtTableSettings.setFields(allTableFieldsDto);

//            add this table's setting to the list of all table settings object named adminFinanceTableSettings
            adminFinanceTableSettings.add(financeMgmtTableSettings);
        }

//        add all table settings to the response
        adminFinanceManagementJson.setFieldSettings(adminFinanceTableSettings);
        
        return adminFinanceManagementJson;
    }

    public void updateFinanceMgmtSettings(PostFinanceMgmtSettings postFinanceMgmtSettings) {

//        list to store all the settings which came for update
        List<FinanceMgmtFieldMap> financeAdminSettingsUpdate = new ArrayList<>();
        List<FinanceCustomPicklist> financeCustomPicklistUpdate = new ArrayList<>();

//        add all component settings to financeAdminSettingsUpdate list
        List<FinanceMgmtAdminComponentsDto> componentSettings = postFinanceMgmtSettings.getComponentSettings();
        for (FinanceMgmtAdminComponentsDto eachComponentSettings : componentSettings) {
            FinanceMgmtFieldMap eachSetting = new FinanceMgmtFieldMap(eachComponentSettings);
            eachSetting.setModifiedDate(new Date());
//            eachSetting.setModifiedBy(0);
//            eachSetting.setCreatedBy(0);
//            add components to update field list
            financeAdminSettingsUpdate.add(eachSetting);
        }

//        get button settings from the database, for comparison
        List<FinanceMgmtFieldMap> storedTableSettings = financeMgmtFieldMapRepo.getAdminFinanceManagementButtons();

//        convert button settings to map for faster access
        Map<Integer, FinanceMgmtFieldMap> storedTableSettingsMap = new HashMap<>();
        for (FinanceMgmtFieldMap eachTable : storedTableSettings) {
            storedTableSettingsMap.put(eachTable.getTableType(), eachTable);
        }

//        retrieve table settings from response
        List<FinanceMgmtTableSettings> tableSettings = postFinanceMgmtSettings.getFieldSettings();

//        traverse the table settings
        for (FinanceMgmtTableSettings eachTableSetting : tableSettings) {

//            retrieve table field settings fro tableSettings
            List<FinanceMgmtAdminFieldsDto> thisTableFieldSettings = eachTableSetting.getFields();

//            traverse each field
            for (FinanceMgmtAdminFieldsDto eachField : thisTableFieldSettings) {

//                retrieve picklist setting from the field setting
                List<PicklistFormat> picklistSetting = eachField.getAdminPicklistSettingsList();

//                convert field setting from dto to entity class
                FinanceMgmtFieldMap eachFieldSetting = new FinanceMgmtFieldMap(eachField);
                eachFieldSetting.setModifiedDate(new Date());
//                eachFieldSetting.setModifiedBy(0);
//                eachFieldSetting.setCreatedBy(0);

//                if the field is new, i.e. it doesn't exist in database and have no field_id
//                but is a picklist, then its picklist values need the field_id for this field
                if (eachField.getFieldId() == null && eachField.getFieldType().contains("Picklist") && picklistSetting != null && !picklistSetting.isEmpty()) {

                    eachFieldSetting.setCreatedDate(new Date());
//                    eachFieldSetting.setCreatedBy(0);
//                    save the field to get its field_id
                    FinanceMgmtFieldMap newFieldSettingWithId = financeMgmtFieldMapRepo.save(eachFieldSetting);

//                    convert picklist value to entity class and assign them picklist_id
                    for (PicklistFormat eachValue : picklistSetting) {
                        FinanceCustomPicklist eachPicklistValue = new FinanceCustomPicklist(eachValue);
                        eachPicklistValue.setPicklistId(newFieldSettingWithId.getFieldId());

                        eachPicklistValue.setModifiedDate(new Date());
//                        eachPicklistValue.setModifiedBy(0);
//                        eachPicklistValue.setCreatedBy(0);
//                        add picklist values to update custom picklist list
                        financeCustomPicklistUpdate.add(eachPicklistValue);
                    }

                } else {
//                    add field to update field list
                    eachFieldSetting.setModifiedDate(new Date());
//                    eachFieldSetting.setModifiedBy(0);
//                    eachFieldSetting.setCreatedBy(0);
                    financeAdminSettingsUpdate.add(eachFieldSetting);

                    if (picklistSetting != null && !picklistSetting.isEmpty()) {

                        for (PicklistFormat eachValue : picklistSetting) {
                            FinanceCustomPicklist eachPicklistValue = new FinanceCustomPicklist(eachValue);
                            eachPicklistValue.setModifiedDate(new Date());
//                            eachPicklistValue.setModifiedBy(0);
//                            eachPicklistValue.setCreatedBy(0);
//                            add picklist values to update custom picklist list
                            financeCustomPicklistUpdate.add(eachPicklistValue);
                        }
                    }
                }
            }

//            update button setting for this table
            FinanceMgmtFieldMap tableButtonSettingUpdate = storedTableSettingsMap.get(eachTableSetting.getTableType());
            tableButtonSettingUpdate.setEnabled(eachTableSetting.getIsEnabled()==true?"1":"0");
            tableButtonSettingUpdate.setModifiedDate(new Date());
//            tableButtonSettingUpdate.setModifiedBy(0);
//            tableButtonSettingUpdate.setCreatedBy(0);
//            add button to update field list
            financeAdminSettingsUpdate.add(tableButtonSettingUpdate);
        }

//        update all the fields
        financeMgmtFieldMapRepo.saveAll(financeAdminSettingsUpdate);

//        update custom picklist list
        if (!financeCustomPicklistUpdate.isEmpty()) {
            financeCustomPicklistRepo.saveAll(financeCustomPicklistUpdate);
        }
    }

    /*
    Other APIs
     */

    public List<ResourceIdDepartment> getResourceListForDelegation(GetDateRange getDateRange){
        List<ResourceIdDepartment> getResourceIdDepartmentList = resourceMgmtRepo.getResourceIdDepartmentList(java.sql.Date.valueOf(String.valueOf(getDateRange.getStartDate())),java.sql.Date.valueOf(String.valueOf(getDateRange.getEndDate())) );

        if(getResourceIdDepartmentList==null || getResourceIdDepartmentList.isEmpty()){
            getResourceIdDepartmentList = null;
        }

        return getResourceIdDepartmentList;
    }


    public Object saveDelegatedResourceHistory(List<PostDelegatedResourceHistory> postDelegatedResourceHistoryList, String username) {

//        get all records from database, to check for valid ids and setting created by and created date for existing records
        List<DelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.findAll();
        Map<Integer, DelegatedResourceHistory> delegatedDepartmentHistoryListToMap
                = delegatedResourceHistoryList.stream().collect(Collectors.toMap(DelegatedResourceHistory::getDelegationId, data->data));

        List<DelegatedResourceHistory> delegatedResourceHistory = new ArrayList<>();

//        traverse through response list coming from frontend
        for (PostDelegatedResourceHistory eachDelegatedResourceHistory : postDelegatedResourceHistoryList) {
            if(eachDelegatedResourceHistory.getIsActive()==null){
                eachDelegatedResourceHistory.setIsActive(true);
            }
            if (eachDelegatedResourceHistory.getIsActive()) {

                DelegatedResourceHistory delegatedDeptHistory = new DelegatedResourceHistory();

 //            check if essential data is missing
                if (eachDelegatedResourceHistory.getDelegatedResourceId() == null || eachDelegatedResourceHistory.getDelegatedToResourceId() == null
                        || eachDelegatedResourceHistory.getDelegationStartDate() == null || eachDelegatedResourceHistory.getDelegationEndDate() == null) {

                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage(UserSettingsUtil.ESSENTIAL_FIELDS_MISSING);
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;

                } else {
                    delegatedDeptHistory.setDelegatedResourceId(eachDelegatedResourceHistory.getDelegatedResourceId());
                    delegatedDeptHistory.setDelegatedToResourceId(eachDelegatedResourceHistory.getDelegatedToResourceId());
                    delegatedDeptHistory.setDelegationStartDate(eachDelegatedResourceHistory.getDelegationStartDate());
                    delegatedDeptHistory.setDelegationEndDate(eachDelegatedResourceHistory.getDelegationEndDate());
                }

                Integer resourceId = eachDelegatedResourceHistory.getDelegatedResourceId();
                Integer delegatedToResourceId = eachDelegatedResourceHistory.getDelegatedToResourceId();
                if(resourceId.equals(delegatedToResourceId)){
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage("Delegated from resource and delegated to resource is same");
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }

                List<DelegatedResourceHistory> drhListByResourceAndDelegatedResourceId
                        = delegatedResourceHistoryRepo.getDelegatedHistoryByResourceIdAndDelegatedToResourceId(resourceId, delegatedToResourceId);

                if (drhListByResourceAndDelegatedResourceId != null && !drhListByResourceAndDelegatedResourceId.isEmpty()) {

                    for (DelegatedResourceHistory eachSameResourceDepartmentRecord : drhListByResourceAndDelegatedResourceId) {

                        if (eachDelegatedResourceHistory.getDelegationId()==null
                                || !eachSameResourceDepartmentRecord.getDelegationId().equals(eachDelegatedResourceHistory.getDelegationId())) {

                            LocalDate recordToUpdateStartDate, recordToUpdateEndDate, savedRecordStartDate, savedRecordEndDate;

                            recordToUpdateStartDate = new java.sql.Date(eachDelegatedResourceHistory.getDelegationStartDate().getTime()).toLocalDate();
                            recordToUpdateEndDate = new java.sql.Date(eachDelegatedResourceHistory.getDelegationEndDate().getTime()).toLocalDate();

                            savedRecordStartDate = new java.sql.Date(eachSameResourceDepartmentRecord.getDelegationStartDate().getTime()).toLocalDate();
                            savedRecordEndDate = new java.sql.Date(eachSameResourceDepartmentRecord.getDelegationEndDate().getTime()).toLocalDate();

                            if (eachSameResourceDepartmentRecord.getIsActive() && !recordToUpdateStartDate.isAfter(savedRecordEndDate)
                                    && !recordToUpdateEndDate.isBefore(savedRecordStartDate)) {
                                Error error = new Error();
                                error.setRequestAt(new Date());
                                error.setMessage(UserSettingsUtil.INVALID_DATE_RANGES);
                                error.setStatus(HttpStatus.BAD_REQUEST.value());
                                return error;
                            }
                        }
                    }
                }

//                check if the id coming from frontend is valid or not
                if (eachDelegatedResourceHistory.getDelegationId() == null
                        || !delegatedDepartmentHistoryListToMap.containsKey(eachDelegatedResourceHistory.getDelegationId())) {

 //             set id to null, in case if an invalid id came from frontend
                    eachDelegatedResourceHistory.setDelegationId(null);
                    delegatedDeptHistory.setCreatedBy(username);
                    delegatedDeptHistory.setCreatedDate(new Date());
                }

//            if valid, replace the created by and created date with the data stored in database
                else {
                    delegatedDeptHistory.setDelegationId(eachDelegatedResourceHistory.getDelegationId());
                    delegatedDeptHistory.setCreatedBy(delegatedDepartmentHistoryListToMap.get(eachDelegatedResourceHistory.getDelegationId()).getCreatedBy());
                    delegatedDeptHistory.setCreatedDate(delegatedDepartmentHistoryListToMap.get(eachDelegatedResourceHistory.getDelegationId()).getCreatedDate());
                }

//            set remaining data
                delegatedDeptHistory.setIsActive(eachDelegatedResourceHistory.getIsActive());
                delegatedDeptHistory.setModifiedBy(username);
                delegatedDeptHistory.setModifiedDate(new Date());
                delegatedResourceHistory.add(delegatedDeptHistory);
            }
            else{
//                instead of again calling the database, check in the map above
                DelegatedResourceHistory delegatedResourceHistoryById = delegatedDepartmentHistoryListToMap.get(eachDelegatedResourceHistory.getDelegationId());
                if(delegatedResourceHistoryById !=null) {
                    delegatedResourceHistoryById.setIsActive(eachDelegatedResourceHistory.getIsActive());
                    delegatedResourceHistory.add(delegatedResourceHistoryById);
                }
//                try to save all records at once
//                delegatedResourceHistoryRepo.save(delegatedResourceHistoryById);
            }
        }

        if (!delegatedResourceHistory.isEmpty()) {
            delegatedResourceHistoryRepo.saveAll(delegatedResourceHistory);
        }
        return true;
    }

    public List<GetDelegatedResourceHistory> getDelegatedResourceHistory() {
        List<GetDelegatedResourceHistory> delegatedResourceHistoryList = delegatedResourceHistoryRepo.getDelegatedResourceHistory();

        if(delegatedResourceHistoryList==null || delegatedResourceHistoryList.isEmpty()){
            delegatedResourceHistoryList = null;
        }
        else{
            for(int i=0; i<delegatedResourceHistoryList.size(); i++){
                GetDelegatedResourceHistory currentRecord = delegatedResourceHistoryList.get(i);
                currentRecord.setDelegationStartDate(java.sql.Date.valueOf(new java.sql.Date(currentRecord.getDelegationStartDate().getTime()).toLocalDate()));
                currentRecord.setDelegationEndDate(java.sql.Date.valueOf(new java.sql.Date(currentRecord.getDelegationEndDate().getTime()).toLocalDate()));
                currentRecord.setCreatedDate(java.sql.Date.valueOf(new java.sql.Date(currentRecord.getCreatedDate().getTime()).toLocalDate()));
                currentRecord.setModifiedDate(java.sql.Date.valueOf(new java.sql.Date(currentRecord.getModifiedDate().getTime()).toLocalDate()));
                delegatedResourceHistoryList.set(i,currentRecord);
            }
            delegatedResourceHistoryList.sort(Comparator.comparingInt(GetDelegatedResourceHistory::getDelegationId).reversed());
        }
        return delegatedResourceHistoryList;
    }

}
