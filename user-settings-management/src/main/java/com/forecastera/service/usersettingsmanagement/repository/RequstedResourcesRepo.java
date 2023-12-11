package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Sowjanya Aare
 * @Create 11-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.response.GetRequestedResourcesData;
import com.forecastera.service.usersettingsmanagement.entity.RequestedResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Repository
public interface RequstedResourcesRepo extends JpaRepository<RequestedResources, Integer> {

//    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetRequestedResourcesData" +
//            "(rr.requestId, concat(rm.firstName, ' ', rm.lastName), rmr.role,  et.employmentType, pmv.fieldValue, " +
//            "rr.allocationStartDate, rr.allocationEndDate, rr.requestedFte, rr.requestStatus, " +
//            "rr.createdBy, rr.createdDate, rr.modifiedBy, rr.modifiedDate) " +
//            "from RequestedResources as rr " +
//            "join ResourceMgmt as rm on rm.resourceId = rr.requestedResourceId " +
//            "join ResourceMgmtRoles as rmr on rmr.roleId = rr.requestedRoleId " +
//
//            "join ResourceMgmtFieldValue as rfv1 on rm.resourceId = rfv1.resourceId "+
//            "join ResourceMgmtFieldMap as rfm1 on rfv1.fieldId = rfm1.fieldId "+
//            "join ResourceMgmtEmploymentType as et on cast(et.empTypeId as text) = rfv1.fieldValue and rfm1.fields='Employment Type' "+
//
//            "join ProjectMgmt as pm on pm.projectId = rr.projectId " +
//            "join ProjectMgmtFieldValue as pmv on pmv.projectId = pm.projectId " +
//            "join ProjectMgmtFieldMap as pmf on pmf.fieldId = pmv.fieldId " +
//            "where pmf.fields = 'Project Name'and rm.resourceId!=0 and " +
//            "rm.dateOfJoin <= :endDate and (rm.lastWorkingDate>= :startDate or rm.lastWorkingDate is null) ")
//    List<GetRequestedResourcesData> getAllRequestedResourceData(@RequestParam("endDate") Date endDate, @RequestParam("startDate") Date startDate);
//
//
//    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetRequestedResourcesData"+
//            "(rr.requestId, concat(rm.firstName, ' ', rm.lastName), rmr.role, et.employmentType, " +
//            "pmv.fieldValue, rr.allocationStartDate, rr.allocationEndDate, rr.requestedFte, rr.requestStatus, " +
//            "rr.createdBy, rr.createdDate, rr.modifiedBy, rr.modifiedDate) " +
//            "from RequestedResources as rr " +
//            "join ResourceMgmt as rm on rm.resourceId = rr.requestedResourceId " +
//            "join ResourceMgmtRoles as rmr on rmr.roleId = rr.requestedRoleId " +
//
//            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId "+
//            "join ResourceMgmtFieldMap as rfm on rfv.fieldId = rfm.fieldId "+
//
//            "join ResourceMgmtFieldValue as rfv1 on rm.resourceId = rfv1.resourceId "+
//            "join ResourceMgmtFieldMap as rfm1 on rfv1.fieldId = rfm1.fieldId "+
//            "join ResourceMgmtEmploymentType as et on cast(et.empTypeId as text) = rfv1.fieldValue and rfm1.fields='Employment Type' "+
//
//            "join ProjectMgmt as pm on pm.projectId = rr.projectId " +
//            "join ProjectMgmtFieldValue as pmv on pmv.projectId = pm.projectId " +
//            "join ProjectMgmtFieldMap as pmf on pmf.fieldId = pmv.fieldId " +
//            "where pmf.fields = 'Project Name' and rm.resourceId!=0 and " +
//            "rm.dateOfJoin <= :endDate and (rm.lastWorkingDate>= :startDate or rm.lastWorkingDate is null) " +
//            "and rfm.fields = 'Department' and rfv.fieldValue = cast(:departmentId as text) ")
//    List<GetRequestedResourcesData> getAllRequestedResourceDataByDeptId(@RequestParam("endDate") Date endDate,
//                                                                        @RequestParam("startDate") Date startDate,
//                                                                        @RequestParam("departmentId") Integer departmentId);

}

