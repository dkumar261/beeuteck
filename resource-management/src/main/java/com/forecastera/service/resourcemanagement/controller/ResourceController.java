package com.forecastera.service.resourcemanagement.controller;

import com.forecastera.service.resourcemanagement.commonResponseUtil.Error;
import com.forecastera.service.resourcemanagement.commonResponseUtil.ResponseEntityWrapper;
import com.forecastera.service.resourcemanagement.dto.request.PostAllocationDataByMapId;
import com.forecastera.service.resourcemanagement.dto.request.PostResourceAllocationChart;
import com.forecastera.service.resourcemanagement.dto.request.PostResourceDetailsMaster;
import com.forecastera.service.resourcemanagement.dto.request.PostResourceMgmt;
import com.forecastera.service.resourcemanagement.dto.utilityClass.GetDateRange;
import com.forecastera.service.resourcemanagement.services.ResourceService;
import com.forecastera.service.resourcemanagement.services.UserLoginService;
import com.forecastera.service.resourcemanagement.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 23-05-2023
 * @Description
 */

@RestController
@RequestMapping("/resource")
public class ResourceController {

//    @GetMapping("/getAll")
//    public ResponseEntity<List<String>> getAll() {
//        return ResponseEntity.ok(Arrays.asList("Resource Management"));
//    }

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserLoginService userLoginService;

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll(@Valid @RequestParam String email) {
//        projectService.dateCheckExtraFunction();
        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getAll(email)));
    }

    @GetMapping("/departmentDropdownOptions")
    public ResponseEntity<Object> departmentDropdownOptions(@Valid @RequestHeader(name = "username") String username,
                                                            @RequestHeader(name = "accessToken") String accessToken,
                                                            @RequestHeader(name = "email") String email,
                                                            @RequestHeader(name = "role") String role) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.departmentDropdownOptions(email, role)));
    }

    @GetMapping("/getResourceListForDepartmentHead")
    public ResponseEntity<Object> getResourceListForDepartmentHead() {

    	return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceListForDepartmentHead()));
    }

    @PostMapping("/getTotalResource")
    public ResponseEntity<Object> getTotalResource(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                   @RequestHeader(name = "email") String email,
                                                   @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getTotalResource(ResourceUtils.removeTimeData(getDateRange), departmentId, email, role)));
    }

    @PostMapping("/getUtilizationLevel")
    public ResponseEntity<Object> getUtilizationLevel(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                      @RequestHeader(name = "email") String email,
                                                      @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getUtilizationLevel(ResourceUtils.removeTimeData(getDateRange), departmentId, email, role)));
    }

    @PostMapping("/getUtilizationPercent")
    public ResponseEntity<Object> getUtilizationPercent(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                        @RequestHeader(name = "email") String email,
                                                        @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getUtilizationPercent(ResourceUtils.removeTimeData(getDateRange), departmentId, email, role)));
    }

    @PostMapping("/getResourcesByRoles")
    public ResponseEntity<Object> getResourcesByRoles(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                      @RequestHeader(name = "email") String email,
                                                      @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourcesByRoles(ResourceUtils.removeTimeData(getDateRange), departmentId, email, role)));
    }

    @PostMapping("/getResourcesByLocation")
    public ResponseEntity<Object> getResourcesByLocation(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                         @RequestHeader(name = "email") String email,
                                                         @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourcesByLocation(ResourceUtils.removeTimeData(getDateRange), departmentId, email, role)));
    }

    @GetMapping("/notificationViewedByUser")
    public ResponseEntity<Object> notificationViewedByUser(@Valid @RequestParam Boolean isRmTrue,
                                                           @RequestParam Integer requestId,
                                                           @RequestHeader(name = "username") String username,
                                                           @RequestHeader(name = "accessToken") String accessToken,
                                                           @RequestHeader(name = "email") String email,
                                                           @RequestHeader(name = "role") String role) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        Object returnData = resourceService.notificationViewedByUser(email, isRmTrue, requestId);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("1", "Success")));
        }
        // List<GetRequestedResourcesData> resourceRequestData = resourceService.fetchResourceRequestDataByDepartmentId(ResourceUtils.removeTimeData(getDateRange), departmentId);
        // return ResponseEntity.ok(new ResponseEntityWrapper(resourceRequestData));
    }

    @GetMapping("/getNotificationCount")
    public ResponseEntity<Object> getNotificationCount(@Valid @RequestParam Boolean isRMNotification,
                                                     @RequestHeader(name = "username") String username,
                                                     @RequestHeader(name = "accessToken") String accessToken,
                                                     @RequestHeader(name = "email") String email,
                                                       @RequestHeader(name = "role") String role) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }
        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getNotificationCount(isRMNotification, email, role)));
        // List<GetRequestedResourcesData> resourceRequestData = resourceService.fetchResourceRequestDataByDepartmentId(ResourceUtils.removeTimeData(getDateRange), departmentId);
        // return ResponseEntity.ok(new ResponseEntityWrapper(resourceRequestData));
    }

    @PostMapping("/getResourceRequest")
    public ResponseEntity<Object> getResourceRequest(@Valid @RequestParam Boolean isTeam,
                                                     @RequestHeader(name = "email") String email,
                                                     @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.fetchResourceRequestDataByDepartmentIdForRM(email, isTeam, role)));
        // List<GetRequestedResourcesData> resourceRequestData = resourceService.fetchResourceRequestDataByDepartmentId(ResourceUtils.removeTimeData(getDateRange), departmentId);
        // return ResponseEntity.ok(new ResponseEntityWrapper(resourceRequestData));
    }

    @PostMapping("/getResourceRequestPM")
    public ResponseEntity<Object> getResourceRequestPM(@Valid @RequestParam Boolean isTeam,
                                                       @RequestHeader(name = "username") String username,
                                                       @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.fetchResourceRequestDataByDepartmentIdForPM(email, isTeam, username)));
        // List<GetRequestedResourcesData> resourceRequestData = resourceService.fetchResourceRequestDataByDepartmentId(ResourceUtils.removeTimeData(getDateRange), departmentId);
        // return ResponseEntity.ok(new ResponseEntityWrapper(resourceRequestData));
    }

    @GetMapping(value = "/downloadCreateResourceExcelFile")
    @ResponseBody
    public Object downloadCreateResourceExcelFile(@Valid @RequestHeader(name = "username") String username) throws IOException {

        Object returnData;
        try {
            returnData = resourceService.downloadCreateResourceExcelFile();
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ResourceUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "resourceDetails.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @PostMapping(path = "/createResourceFromExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Object createResourceDetailsFromExcel(@Valid @RequestHeader(name = "username") String username,
                                                 @RequestPart MultipartFile excelFile) {


        Object returnData;
        try {
            returnData = resourceService.createResourceDetailsFromExcel(excelFile, username);
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ResourceUtils.ERROR_WHILE_PROCESSING_UPLOADED_FILE);
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

    @PostMapping("/createResource")
    public ResponseEntity<Object> createResource(@Valid @RequestBody PostResourceMgmt postResourceMgmt,
                                                 @RequestHeader(name = "username") String username) {


        Object returnData = resourceService.createResource(postResourceMgmt, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("1", "Success")));
        }
    }

    @PostMapping("/getResourceDetailsById")
    public ResponseEntity<Object> getResourceDetails(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer resourceId) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceDetails(ResourceUtils.removeTimeData(getDateRange), resourceId)));
    }

    @PostMapping("/updateResourceDetailsById")
    public ResponseEntity<Object> updateResourceDetails(@Valid @RequestParam Integer resourceId, @RequestBody PostResourceDetailsMaster postResourceDetailsMaster,
                                                        @RequestHeader(name = "username") String username) {


        Object returnData = resourceService.updateResourceDetails(resourceId, postResourceDetailsMaster, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @DeleteMapping("/deleteResourceDetailsById")
    public ResponseEntity<Object> deleteResourceDetails(@Valid @RequestParam Integer resourceId,
                                                        @RequestHeader(name = "username") String username) {


        Object returnData = resourceService.deleteResourceDetails(resourceId, username);
        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @GetMapping("/getResourceHierarchy")
    public ResponseEntity<Object> getResourceHierarchy() {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceHierarchy()));
    }

    @GetMapping("/getSpecificResourceHierarchy")
    public ResponseEntity<Object> getSpecificResourceHierarchy(@Valid @RequestParam String userEmail) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getSpecificResourceHierarchy(userEmail)));
    }

    @PostMapping("/getResourceAnalyticsData")
    public ResponseEntity<Object> getResourceAnalyticsData(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                           @RequestHeader(name = "email") String email,
                                                           @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceAnalyticsData(ResourceUtils.removeTimeData(getDateRange), departmentId, email, role)));
    }

    @PostMapping(value = "/downloadResourceAnalytics")
    @ResponseBody
    public Object downloadResourceAnalyticsTable(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId,
                                                 @RequestHeader(name = "email") String email,
                                                 @RequestHeader(name = "role") String role) throws IOException {

        Object returnData;
        try {
            returnData = resourceService.downloadResourceAnalyticsTable(getDateRange, departmentId, email, role);
        } catch (Exception e) {
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage(ResourceUtils.ERROR_WHILE_CREATING_EXCEL_FILE);
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            returnData = error;
        }

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(returnData);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "resourceAnalytics.xlsx");

            byte[] excelData = (byte[]) returnData;

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        }
    }

    @PostMapping("/getResourcesByDepartment")
    public ResponseEntity<Object> getResourcesByDepartment(@Valid @RequestBody GetDateRange getDateRange,
                                                           @RequestHeader(name = "email") String email,
                                                           @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourcesByDepartment(ResourceUtils.removeTimeData(getDateRange), email, role)));
    }

    @PostMapping("/getResourcesByProject")
    public ResponseEntity<Object> getResourcesByProject(@Valid @RequestBody GetDateRange getDateRange,
                                                        @RequestHeader(name = "email") String email,
                                                        @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourcesByProject(ResourceUtils.removeTimeData(getDateRange), email, role)));
    }

    @PostMapping("/getResourcesByUtilization")
    public ResponseEntity<Object> getResourcesByUtilization(@Valid @RequestBody GetDateRange getDateRange,
                                                            @RequestHeader(name = "email") String email,
                                                            @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourcesByUtilization(ResourceUtils.removeTimeData(getDateRange), email, role)));
    }

    @PostMapping("/getResourcesByProjectOwner")
    public ResponseEntity<Object> getResourcesByProjectOwner(@Valid @RequestBody GetDateRange getDateRange,
                                                             @RequestHeader(name = "email") String email,
                                                             @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourcesByProjectOwner(ResourceUtils.removeTimeData(getDateRange), email, role)));
    }

    @GetMapping("/getResourceAllocationMatrixForAllResources")
    public ResponseEntity<Object> getResourceAllocationMatrixForAllResources(@Valid @RequestParam String dropdown,
                                                                             @RequestParam Integer financialYear,
                                                                             @RequestParam Integer id,
                                                                             @RequestHeader(name = "email") String email,
                                                                             @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceAllocationMatrixForAllResources(dropdown, financialYear, id, email, role)));
    }

    /*@PostMapping("/getResourceAllocationMatrix")
    public ResponseEntity<Object> getResourceAllocationMatrix(@Valid @RequestParam String dropdown, @RequestParam Integer id,
                                                              @RequestBody GetDateRange getDateRange,
                                                              @RequestHeader(name = "username") String username,
                                                              @RequestHeader(name = "accessToken") String accessToken,
                                                              @RequestHeader(name = "email") String email) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceAllocationMatrix(dropdown, id, ResourceUtils.removeTimeData(getDateRange), email)));
//        return ResponseEntity.ok(null);
    }*/

//    @PostMapping("/getAllocationDataByMapId")
//    public ResponseEntity<List<AllResourceActiveAllocation>> getAllocationDataByMapId(@Valid @RequestParam Integer mapId) {
//        return ResponseEntity.ok(resourceService.getAllocationDataByMapId(mapId));
//    }

    @PostMapping("/getAllocationDataByMapId")
    public ResponseEntity<Object> getAllocationDataByMapId(@Valid @RequestParam Integer mapId, @RequestHeader(name = "username") String username,
                                                           @RequestHeader(name = "accessToken") String accessToken,
                                                           @RequestHeader(name = "email") String email) {

        Object isAccessTokenValid = userLoginService.accessTokenValidation(username, accessToken);

        if (isAccessTokenValid instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(isAccessTokenValid));
        }

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getAllocationDataByMapId(mapId)));
    }

//    @PostMapping("/getResourceAllocationMatrixByDepartmentId")
//    public ResponseEntity<List<GetResourceAllocationMatrix>> getResourceAllocationMatrixByDepartmentId(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer departmentId) {
//        return ResponseEntity.ok(resourceService.getResourceAllocationMatrixByDepartmentId(getDateRange, departmentId));
//    }
//
//    @PostMapping("/getResourceAllocationMatrixByProjectId")
//    public ResponseEntity<List<GetResourceAllocationMatrix>> getResourceAllocationMatrixByProjectId(@Valid @RequestBody GetDateRange getDateRange, @RequestParam Integer projectId) {
//        return ResponseEntity.ok(resourceService.getResourceAllocationMatrixByProjectId(getDateRange, projectId));
//    }

//    @PostMapping("/createUpdateProjectResourceAllocation")
//    public ResponseEntity<List<String>> createUpdateProjectResourceAllocation(@Valid @RequestBody PostResourceAllocationMatrix postResourceAllocationMatrix) {
//        resourceService.createUpdateProjectResourceAllocation(postResourceAllocationMatrix);
//        return ResponseEntity.ok(Arrays.asList("1", "Success"));
//    }

    @PostMapping("/createUpdateProjectResourceAllocation")
    public ResponseEntity<Object> createUpdateProjectResourceAllocation(@Valid @RequestBody PostAllocationDataByMapId postAllocationDataByMapId,
                                                                        @RequestParam Integer mapId,
                                                                        @RequestParam String allocationFrequency,
                                                                        @RequestParam String modifiedBy) {

        Object returnData = resourceService.createUpdateProjectResourceAllocation(postAllocationDataByMapId, mapId, allocationFrequency, modifiedBy);

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @PostMapping("/getResourceAllocationDataLimitsByMapId")
    public ResponseEntity<Object> getResourceAllocationDataLimitsByMapId(@Valid @RequestParam Integer resourceId, @RequestParam Integer mapId,
                                                                         @RequestBody GetDateRange getDateRange) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceAllocationDataLimitsByMapId(resourceId, mapId, ResourceUtils.removeTimeData(getDateRange))));
    }

    @GetMapping("/getDepartmentAllResourceAllocationsChart")
    public ResponseEntity<Object> getDepartmentAllResourceAllocationsChart(@Valid @RequestParam Integer resourceId, @RequestParam Integer financialYear,
                                                                           @RequestHeader(name = "email") String email,
                                                                           @RequestHeader(name = "role") String role) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getDepartmentAllResourceAllocationsChart(resourceId, financialYear, email, role)));
    }

    @PostMapping("/getResourceAllAllocationsChart")
    public ResponseEntity<Object> getResourceAllAllocationsChart(@Valid @RequestParam Integer resourceId, @RequestParam Integer financialYear) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceAllAllocationsChart(resourceId, financialYear)));
    }

    @PostMapping("/updateResourceAllAllocationsChart")
    public ResponseEntity<Object> updateResourceAllAllocationsChart(@Valid @RequestParam String allocationFrequency,
                                                                    @RequestBody PostResourceAllocationChart postGanttChartData,
                                                                    @RequestHeader(name = "username") String username) {

        Object returnData = resourceService.updateResourceAllAllocationsChart(username, allocationFrequency, postGanttChartData);

        if (returnData instanceof Error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        } else {
            return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
        }
    }

    @GetMapping("/getInHouseResourceDetails")
    public ResponseEntity<Object> getInHouseResourceDetails(@Valid @RequestParam Integer financialYear) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getInHouseResourceDetails(financialYear)));
    }

    @GetMapping("/getVendorResourceDetails")
    public ResponseEntity<Object> getVendorResourceDetails(@Valid @RequestParam Integer financialYear) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getVendorResourceDetails(financialYear)));
    }

//    @PostMapping("/getMaxPossibleFteByResourceId")
//    public ResponseEntity<ResourceMonthlyFteData> getMaxPossibleFteByResourceId(@Valid @RequestParam Integer resourceId, @RequestBody GetDateRange getDateRange) {
//        return ResponseEntity.ok(resourceService.getMaxPossibleFteByResourceId(resourceId, ResourceUtils.removeTimeData(getDateRange)));
//    }

    @PostMapping("/getMaxPossibleFteByResourceId")
    public ResponseEntity<Object> getMaxPossibleFteByResourceId(@Valid @RequestParam Integer resourceId, @RequestBody GetDateRange getDateRange,
                                                                @RequestParam Integer mapId) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getMaxPossibleFteByResourceId(resourceId, ResourceUtils.removeTimeData(getDateRange), mapId)));
    }

    @GetMapping("/getUtilizationGraph")
    public ResponseEntity<Object> getUtilizationGraph(@Valid @RequestParam Integer resourceId) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getUtilizationGraph(resourceId)));
    }

    @PostMapping("/getResourceAllocationChartForAllResources")
    public ResponseEntity<Object> getResourceAllocationChartForAllResources(@Valid @RequestBody List<Integer> resourceIds, @RequestParam Integer financialYear) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getResourceAllocationChartForAllResources(resourceIds, financialYear)));
    }

    @PostMapping("/sendResourceListEligibleForSwapping")
    public ResponseEntity<Object> sendResourceListEligibleForSwapping(@Valid @RequestParam Integer resourceId, @RequestParam Integer projectId) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.sendResourceListEligibleForSwapping(resourceId, projectId)));
    }

    @PostMapping("/swapResourceForResourceRequest")
    public ResponseEntity<Object> swapResourceForResourceRequest(@Valid @RequestParam Integer mapId, @RequestParam Integer resourceId,
    															 @RequestHeader(name = "username") String username) {

        Object returnData = resourceService.swapResourceForRequestedResource(mapId, resourceId, username);
        if(returnData instanceof Error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        }
        return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
    }


    @GetMapping("/getSearchFiltersForUnnamedRequest")
    public ResponseEntity<Object> getSearchFiltersForUnnamedRequest(@Valid @RequestParam Integer requestId) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getSearchFiltersForUnnamedRequest(requestId)));
    }

    @PostMapping("/assignUnnamedResourceToRequest")
    public ResponseEntity<Object> assignResourceToUnnamedRequest(@Valid @RequestParam Integer requestId, @RequestParam Integer resourceId, String username) {
        Object returnData = resourceService.assignResourceToUnnamedRequest(requestId, resourceId, username);
        if(returnData instanceof Error){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityWrapper(returnData));
        }
        return ResponseEntity.ok(new ResponseEntityWrapper(Arrays.asList("Success", "1")));
    }

    @GetMapping("/getUserProfileDetails")
    public ResponseEntity<Object> getUserProfileDetails(@Valid @RequestHeader(name = "email") String email) {

        return ResponseEntity.ok(new ResponseEntityWrapper(resourceService.getUserProfileDetails(email)));
    }
}
		