package com.forecastera.service.projectmanagement.controller;

import com.forecastera.service.projectmanagement.commonResponseUtil.Error;
import com.forecastera.service.projectmanagement.commonResponseUtil.ResponseEntityWrapper;
import com.forecastera.service.projectmanagement.dto.request.*;
import com.forecastera.service.projectmanagement.dto.response.GetRequestResourceByFilters;
import com.forecastera.service.projectmanagement.dto.utilityClass.GetDateRange;
import com.forecastera.service.projectmanagement.services.ProjectService;
import com.forecastera.service.projectmanagement.services.UserLoginService;
import com.forecastera.service.projectmanagement.util.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
/*@OpenAPIDefinition(info = @Info(title = "My App", description = "Some long and useful description",
        version = "v1", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")))*/
@RestController
@RequestMapping("/project")
//@CrossOrigin(origins = {"http://34.200.72.18:8761","http://172.31.57.25:8761","http://172.31.57.25:8004","http://34.200.72.18:8004","http://172.31.57.25:8001","http://34.200.72.18:8001","http://172.31.57.25:8003","http://34.200.72.18:8003","http://172.16.31.26:8003","http://172.16.31.26:8001"},allowedHeaders = "*")
//@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserLoginService userLoginService;

//    public ProjectController(ProjectService projectService) {
//        this.projectService = projectService;
//    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll(@Valid @RequestHeader(name = "email") String email) {
//        projectService.dateCheckExtraFunction();
        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getAll(email)));
    }

    @PostMapping("/getOverAllProgress")
    public ResponseEntity<Object> getOverAllProgress(@Valid @RequestBody GetDateRange getDateRange,
                                                     @RequestParam(name = "myView") Boolean myView,
                                                     @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getOverAllProgress(ProjectUtils.removeTimeData(getDateRange), email, myView)));
    }

    @PostMapping("/getNoOfProjectsByType")
    public ResponseEntity<Object> getNoOfProjectsByType(@Valid @RequestBody GetDateRange getDateRange,
                                                        @RequestParam(name = "myView") Boolean myView,
                                                        @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getNoOfProjectsByType(ProjectUtils.removeTimeData(getDateRange), email, myView)));
    }

    @PostMapping("/getTotalCostData")
    public ResponseEntity<Object> getTotalCostData(@Valid @RequestBody GetDateRange getDateRange,
                                                   @RequestHeader(name = "email") String email) {

    	return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getTotalCostData(ProjectUtils.removeTimeData(getDateRange), email)));
    }

    @PostMapping("/getTotalBudgetData")
    public ResponseEntity<Object> getTotalBudgetData(@Valid @RequestBody GetDateRange getDateRange,
                                                     @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getTotalBudgetData(ProjectUtils.removeTimeData(getDateRange), email)));
    }

    @GetMapping(value = "/downloadCreateProjectExcelFile")
    @ResponseBody
    public Object downloadCreateProjectExcelFile(@Valid @RequestHeader(name = "username") String username,
                                                  @RequestHeader(name = "accessToken")  String accessToken){

//        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);
//
//        if(isAccessTokenValid instanceof Error){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isAccessTokenValid);
//        }

        Object returnData;
        try{
            returnData = projectService.downloadCreateProjectExcelFile();
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ProjectUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }

        if(returnData instanceof Error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        }
        else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "projectDetails.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @PostMapping(path = "/createProjectFromExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Object createProjectDetailsFromExcel(@Valid @RequestHeader(name = "username") String username,
                                                 @RequestHeader(name = "accessToken")  String accessToken,
                                                 @RequestPart MultipartFile excelFile){

//        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);
//
//        if(isAccessTokenValid instanceof Error){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(isAccessTokenValid);
//        }

        Object returnData;
        try{
            returnData = projectService.createProjectDetailsFromExcel(excelFile, username);
        }
        catch(Exception e){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ProjectUtils.ERROR_WHILE_PROCESSING_UPLOADED_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData =  error;
        }
        if(returnData instanceof Error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        }
        else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "updateResult.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @PostMapping("/createProject")
    public ResponseEntity<Object> createProject(@Valid @RequestBody PostProjectMgmt postProjectMgmt,
                                                @RequestHeader(name = "username") String username) {

        Object returnData = projectService.createProject(postProjectMgmt, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @PostMapping("/getProjectDetailsById")
    public ResponseEntity<Object> getProjectDetails(@Valid @RequestParam Integer projectId) {

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getProjectDetails(projectId)));
    }

    @PostMapping("/updateProjectDetailsById")
    public ResponseEntity<Object> updateProjectDetails(@Valid @RequestParam Integer projectId, @RequestBody List<PostProjectDetails> postProjectDetails,
                                                       @RequestParam Boolean forceDeallocateResources,
                                                       @RequestHeader(name = "username") String username) {

        Object returnData = projectService.updateProjectDetails(projectId, postProjectDetails, forceDeallocateResources, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

//    @GetMapping("/getProjAnalyticsData")
//    public ResponseEntity<List<GetProjectAnalysisMaster>>getProjAnalyticsData() {
//        return ResponseEntity.ok(projectService.getProjAnalyticsData());
//    }

    @PostMapping("/getProjAnalyticsData")
    public ResponseEntity<Object> getProjAnalyticsData(@Valid @RequestBody GetDateRange getDateRange,
                                                       @RequestParam(name = "myView") Boolean myView,
                                                       @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getProjAnalyticsData(ProjectUtils.removeTimeData(getDateRange), email, myView)));
    }

    @PostMapping(value = "/downloadProjectAnalytics")
    @ResponseBody
    public Object downloadProjectAnalyticsTable(@Valid @RequestBody GetDateRange getDateRange,
                                                @RequestParam(name = "myView") Boolean myView,
                                                @RequestHeader(name = "email") String email) throws IOException {

        Object returnData;
        try {
            returnData = projectService.downloadProjectAnalyticsTable(getDateRange, email, myView);
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ProjectUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "projectAnalytics.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @GetMapping("/getProjectList")
    public ResponseEntity<Object> getProjectList() {

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getProjectList()));
    }

//    @GetMapping("/getKanbanData/{projectId}")
//    public ResponseEntity<List<GetKanbanData>> getKanbanData(@PathVariable("projectId") int projectId) {
//        List<GetKanbanData> kanbanData = projectService.fetchKanbanData(projectId);
//        return ResponseEntity.ok(kanbanData);
//    }

    @GetMapping("/getNamedAllocatedResourceDetails")
    public ResponseEntity<Object> getNamedAllocatedResourceDetails(@Valid @RequestParam Integer projectId,
                                                                   @RequestParam Integer financialYear) {

        Object kanbanData = projectService.getNamedAllocatedResourceDetails(projectId, financialYear);
        return ResponseEntity.ok(new ResponseEntityWrapper(kanbanData));
    }

    @GetMapping("/getUnnamedAllocatedResourceDetails")
    public ResponseEntity<Object> getUnnamedAllocatedResourceDetails(@Valid @RequestParam Integer projectId,
                                                                     @RequestParam Integer financialYear) {

        Object kanbanData = projectService.getUnnamedAllocatedResourceDetails(projectId, financialYear);
        return ResponseEntity.ok(new ResponseEntityWrapper(kanbanData));
    }
    

    @PostMapping("/createUpdateKanbanDataNew")
    public ResponseEntity<Object> createUpdateKanbanDataNew(@Valid @RequestBody List<PostProjectResourceAllocationData> postProjectResourceAllocationDataList,
                                                            @RequestParam Integer financialYear,
                                                            @RequestParam String allocationFrequency,
                                                            @RequestHeader(name = "username") String username) {

        Object returnData = projectService.createUpdateKanbanDataNew(postProjectResourceAllocationDataList, financialYear, allocationFrequency, username);
        if(returnData instanceof Error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        }
        else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }
    
    @DeleteMapping("/removeResource")
    public ResponseEntity<Object> removeResource(@Valid @RequestParam("mapId") Integer mapId,
                                                   @RequestHeader(name = "username") String username) {

        projectService.removeResource(mapId, username);
        return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success","1")));
    }

//    @PostMapping("/getAddResourceBySkillRoleId")
//    public ResponseEntity<List<AddResource>> getResourceByRolesAndSkills(@Valid @RequestParam String roles, @RequestParam String skills) {
//        List<AddResource> addResourceList = projectService.getResourceByRolesAndSkills(roles, skills);
//        return ResponseEntity.ok(addResourceList);
//    }

    @PostMapping("/getRequestResourceByFilters")
    public ResponseEntity<Object> getRequestResourceByFilters(@Valid @RequestBody PostResourceRequestByFilters postResourceRequestByFilters) {

        Object returnData = projectService.getRequestResourceByFilters(postResourceRequestByFilters);

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(returnData));
        }
    }

    @PostMapping("/getRequestResourceByFiltersByRM")
    public ResponseEntity<Object> getRequestResourceByFiltersByRM(@Valid @RequestBody PostResourceRequestByFilters postResourceRequestByFilters,
                                                              @RequestHeader(name = "email")  String email) {

        Object returnData = projectService.getRequestResourceByFiltersByRM(postResourceRequestByFilters, email);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(returnData));
        }
    }

//    @GetMapping("/getResourceBy{mapId}")
//    public ResponseEntity<List<ProjectResourceMapping>> getByMapId(@PathVariable Integer mapId) {
//        ProjectResourceMapping allocation = projectService.getByMapId(mapId);
//
//        if (allocation == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(Collections.singletonList(allocation));
//        }
//    }
//    @GetMapping("/getResourceByRole/{roleId}")
//    public ResponseEntity<List<Object[]>> getResourcesByRoleId(@PathVariable String roleId) {
//        List<Object[]> resources = projectService.getResourcesByRoleId(roleId);
//        return ResponseEntity.ok(resources);
//    }
//
//    @GetMapping("/getResourceBySkill/{skillId}")
//    public ResponseEntity<List<Object[]>> getResourcesBySkillId(@PathVariable String skillId) {
//        List<Object[]> resources = projectService.getResourcesBySkillId(skillId);
//        return ResponseEntity.ok(resources);
//    }

    /*
    PROJECT FINANCE
     */

    /*@GetMapping("/getProjectFinanceBudgetSummary")
    public ResponseEntity<Object> getProjectFinanceBudgetSummary(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId,
                                                                 @RequestHeader(name = "username") String username,
                                                                 @RequestHeader(name = "accessToken") String accessToken,
                                                                 @RequestHeader(name = "email")  String email) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(projectId);
        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getProjectFinanceBudgetSummary(financialYear, projectIds)));
    }

    @GetMapping("/getProjectFinanceForecastSummary")
    public ResponseEntity<Object> getProjectFinanceForecastSummary(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId,
                                                                   @RequestHeader(name = "username") String username,
                                                                   @RequestHeader(name = "accessToken") String accessToken,
                                                                   @RequestHeader(name = "email")  String email) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(projectId);
        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getProjectFinanceForecastSummary(financialYear, projectIds)));
    }

    @GetMapping("/getBudgetVsForecastChart")
    public ResponseEntity<Object> getBudgetVsForecastChart(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId,
                                                           @RequestHeader(name = "username") String username,
                                                           @RequestHeader(name = "accessToken") String accessToken,
                                                           @RequestHeader(name = "email")  String email) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getBudgetVsForecastChart(financialYear, projectId)));
    }

    @GetMapping("/getBreakdownChart")
    public ResponseEntity<Object> getBreakdownChart(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId,
                                                    @RequestHeader(name = "username") String username,
                                                    @RequestHeader(name = "accessToken") String accessToken,
                                                    @RequestHeader(name = "email")  String email) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        return ResponseEntity.ok(new ResponseEntityWrapper(projectService.getBreakdownChart(financialYear, projectId)));
    }*/



    /*
    Internal Labor Expenses
     */
//    @GetMapping("/getProjectFinanceBudgetInternalLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getProjectFinanceBudgetInternalLabor(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(projectService.getProjectFinanceBudgetInternalLabor(financialYear, projectId));
//    }
//
//    @GetMapping("/getProjectFinanceForecastInternalLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getProjectFinanceForecastInternalLabor(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(projectService.getProjectFinanceForecastInternalLabor(financialYear, projectId));
//    }

    /*
    Vendor Labor Expenses
     */
//    @GetMapping("/getProjectFinanceBudgetVendorLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getProjectFinanceBudgetVendorLabor(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(projectService.getProjectFinanceBudgetVendorLabor(financialYear, projectId));
//    }
//
//    @GetMapping("/getProjectFinanceForecastVendorLabor")
//    public ResponseEntity<List<SummaryDataResultDto>> getProjectFinanceForecastVendorLabor(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(projectService.getProjectFinanceForecastVendorLabor(financialYear, projectId));
//    }

    /*
    Total Capex Related Expenses
     */

//    @GetMapping("/getProjectFinanceBudgetTotalCapex")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryBudgetTotalCapex(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(projectService.getProjectFinanceBudgetTotalCapex(financialYear, projectId));
//    }
//
//    @GetMapping("/getProjectFinanceForecastTotalCapex")
//    public ResponseEntity<List<SummaryDataResultDto>> getFinancialSummaryForecastTotalCapex(@Valid @RequestParam Integer financialYear, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(projectService.getProjectFinanceForecastTotalCapex(financialYear, projectId));
//    }
}
