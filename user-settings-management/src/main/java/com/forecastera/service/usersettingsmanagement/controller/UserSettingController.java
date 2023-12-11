package com.forecastera.service.usersettingsmanagement.controller;

import com.forecastera.service.usersettingsmanagement.commonResponseUtil.Error;
import com.forecastera.service.usersettingsmanagement.commonResponseUtil.ResponseEntityWrapper;
import com.forecastera.service.usersettingsmanagement.dto.request.*;
import com.forecastera.service.usersettingsmanagement.dto.response.*;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.GetDateRange;
import com.forecastera.service.usersettingsmanagement.services.UserSettingsLoginService;
import com.forecastera.service.usersettingsmanagement.services.UserSettingsService;

import lombok.extern.slf4j.Slf4j;

import com.forecastera.service.usersettingsmanagement.util.UserSettingsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/*
 * @Author Uttam Kachhad
 * @Create 29-05-2023
 * @Description
 */
/*@OpenAPIDefinition(info = @Info(title = "My App", description = "Some long and useful description",
        version = "v1", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")))*/
@RestController
@RequestMapping("/user-setting")
@Slf4j
public class UserSettingController {

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private UserSettingsLoginService userSettingsLoginService;

    @PostMapping("/newUserRegistration")
    public ResponseEntity<Object> newUserRegistration(@Valid @RequestBody PostUserRegistration postUserRegistration) {

        Object returnData = userSettingsLoginService.newUserRegistration(postUserRegistration);

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            log.info("New user registered with email id: "+ UserSettingsUtil.removeAllExtraSpace(postUserRegistration.getEmailId()) + " on " + new Date());
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }
    /*
    Login Api
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody PostLoginCredentials postLoginCredentials, Boolean isAzure) {

        Object returnData = userSettingsLoginService.login(postLoginCredentials,isAzure);

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            log.info("User : " + postLoginCredentials.getUsername() + " login at " + new Date());
            return ResponseEntity.ok(new ResponseEntityWrapper(returnData));
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser(@Valid @RequestHeader(name = "username") String username, @RequestHeader(name = "accessToken") String accessToken) {
        Object returnData = userSettingsLoginService.logoutProcess(username, accessToken);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
        	log.info("User : " + username + " logout at " + new Date());
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    /*
    Admin General Settings
     */
    @GetMapping("/getGeneralSettings")
    public ResponseEntity<Object> getGeneralSettings() {
        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getGeneralSettings()));
    }

    @PostMapping("/updateGeneralSettings")
    public ResponseEntity<Object> updateGeneralSettings(@Valid @RequestBody List<PostGeneralSettings> postGeneralSettingsList,
                                                        @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateGeneralSettings(postGeneralSettingsList, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    /*
    Skill Settings
     */
    @GetMapping("/getResourceMgmtSkills")
    public ResponseEntity<Object> getResourceMgmtSkills(@Valid @RequestHeader(name = "username") String username, @RequestHeader(name = "accessToken") String accessToken,
                                                        @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getResourceMgmtSkills()));
    }

    @PostMapping("/updateResourceMgmtSkills")
    public ResponseEntity<Object> updateResourceMgmtSkills(@Valid @RequestBody List<PostResourceMgmtSkills> postResourceMgmtSkillsList,
                                                           @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateResourceMgmtSkills(postResourceMgmtSkillsList, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    /*
    Role Settings
     */
    @GetMapping("/getResourceMgmtRoles")
    public ResponseEntity<Object> getResourceMgmtRoles() {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getResourceMgmtRoles()));
    }

    @PostMapping("/updateResourceMgmtRoles")
    public ResponseEntity<Object> updateResourceMgmtRoles(@Valid @RequestBody List<PostResourceMgmtRoles> postResourceMgmtRolesList,
                                                          @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateResourceMgmtRoles(postResourceMgmtRolesList, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @GetMapping(value = "/downloadCreateRoleTemplateExcelFile")
    @ResponseBody
    public Object downloadCreateRoleTemplateExcelFile(){

        Object returnData;
        try {
            returnData = userSettingsService.downloadCreateRoleTemplateExcelFile();
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "roleDetails.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @PostMapping(path = "/createRoleFromExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Object createRoleFromExcel(@Valid @RequestHeader(name = "username") String username,
                                                 @RequestPart MultipartFile excelFile) {


        Object returnData;
        try {
            returnData = userSettingsService.createRoleFromExcel(excelFile, username);
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.ERROR_WHILE_PROCESSING_UPLOADED_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "updateResult.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    /*
    Department Settings
     */
    @GetMapping("/getResourceMgmtDepartment")
    public ResponseEntity<Object> getResourceMgmtDepartment() {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getResourceMgmtDepartment()));
    }

    @PostMapping("/updateResourceMgmtDepartment")
    public ResponseEntity<Object> updateResourceMgmtDepartment(@Valid @RequestBody PostResourceMgmtDepartment postResourceMgmtDepartment,
                                                               @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateResourceMgmtDepartment(postResourceMgmtDepartment, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @GetMapping(value = "/downloadCreateDepartmentTemplateExcelFile")
    @ResponseBody
    public Object downloadCreateDepartmentTemplateExcelFile(){

        Object returnData;
        try {
            returnData = userSettingsService.downloadCreateDepartmentTemplateExcelFile();
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "departmentDetails.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @PostMapping(path = "/createDepartmentFromExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Object createDepartmentFromExcel(@Valid @RequestHeader(name = "username") String username,
                                                 @RequestPart MultipartFile excelFile) {


        Object returnData;
        try {
            returnData = userSettingsService.createDepartmentFromExcel(excelFile, username);
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(UserSettingsUtil.ERROR_WHILE_PROCESSING_UPLOADED_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "updateResult.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    /*
    Location Settings
     */
    @GetMapping("/getGeneralSettingsLocation")
    public ResponseEntity<Object> getGeneralSettingsLocation() {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getGeneralSettingsLocation()));
    }

    @PostMapping("/updateGeneralSettingsLocation")
    public ResponseEntity<Object> updateGeneralSettingsLocation(@Valid @RequestBody List<PostGeneralSettingsLocation> postGeneralSettingsLocationList,
                                                                @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateGeneralSettingsLocation(postGeneralSettingsLocationList, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    /*
    Admin Project Management Settings
     */
    @GetMapping("/getPrjMgmtSettings")
    public ResponseEntity<Object> getProjectMgmtSettings() {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getProjectMgmtSettings()));
    }

    @PostMapping("/updatePrjMgmtSettings")
    public ResponseEntity<Object> updateProjectMgmtSettings(@Valid @RequestBody Map<String, List<PostProjectMgmtSettings>> updateProjectMgmtSettings,
                                                            @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateProjectMgmtSettings(updateProjectMgmtSettings, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    /*
    Admin Resource Management Settings
     */
    @GetMapping("/getResourceMgmtSettings")
    public ResponseEntity<Object> getResourceMgmtSettings() {

        Map<String, List<GetResourceMgmtSettings>> adminResourceManagementJson = userSettingsService.getResourceMgmtSettings();
        return ResponseEntity.ok(new ResponseEntityWrapper(adminResourceManagementJson));
    }

    @PostMapping("/updateResourseMgmtSettings")
    public ResponseEntity<Object> updateResourceMgmtSettings(@Valid @RequestBody Map<String, List<PostResourceMgmtSettings>> updateResourceMgmtSettings,
                                                             @RequestHeader(name = "username") String username) {

        Object returnData = userSettingsService.updateResourceMgmtSettings(updateResourceMgmtSettings, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @GetMapping("/getFinanceMgmtSettings")
    public ResponseEntity<Object> getFinanceMgmtSettings() {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getFinanceMgmtSettings()));
    }

    @PostMapping("/updateFinanceMgmtSettings")
    public ResponseEntity<Object> updateFinanceMgmtSettings(@Valid @RequestBody PostFinanceMgmtSettings postFinanceMgmtSettings,
                                                            @RequestHeader(name = "username") String username) {

        userSettingsService.updateFinanceMgmtSettings(postFinanceMgmtSettings);
        return ResponseEntity.ok(new ResponseEntityWrapper(Collections.singletonList("Success")));
    }

    @PostMapping("/saveDelegatedResourceHistory")
    public ResponseEntity<Object>delegatedResourceHistory(@Valid @RequestBody List<PostDelegatedResourceHistory> postDelegatedResourceHistory,
                                                            @RequestHeader(name = "username") String username)  {

        Object returnData = userSettingsService.saveDelegatedResourceHistory(postDelegatedResourceHistory, username);
        if(returnData instanceof Error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        }
        else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }
    @GetMapping("/getDelegatedResourceHistory")
    public ResponseEntity<Object> getDelegatedResourceHistory() {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getDelegatedResourceHistory()));
    }

    /*
    Other API controllers
     */

    @PostMapping("/getResourceListForDelegation")
    public ResponseEntity<Object> getResourceListForDelegation(@RequestBody GetDateRange getDateRange) {

        return ResponseEntity.ok(new ResponseEntityWrapper(userSettingsService.getResourceListForDelegation(UserSettingsUtil.removeTimeData(getDateRange))));
    }
}
