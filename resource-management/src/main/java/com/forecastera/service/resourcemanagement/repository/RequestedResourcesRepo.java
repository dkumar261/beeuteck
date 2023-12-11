package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Sowjanya Aare
 * @Create 11-05-2023
 * @Description
 */


import com.forecastera.service.resourcemanagement.entity.RequestedResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestedResourcesRepo extends JpaRepository<RequestedResources, Integer> {

//    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetRequestedResourcesData" +
//            "(rr.requestId, rm.resourceId, concat(rm.firstName, ' ', rm.lastName), rmr.role,  et.employmentType, pm.projectId, pmv.fieldValue, " +
//            "rr.allocationStartDate, rr.allocationEndDate, prme.requestedFte, rr.requestStatus, " +
//            "rr.createdBy, rr.createdDate, rr.modifiedBy, rr.modifiedDate, coalesce(sum(prme.fte), 0.00), coalesce(sum(prme.requestedFte), 0.00), rr.pmLastAction, rr.rmLastAction, rr.notify ) " +
//            "from RequestedResources as rr " +
//            "join ResourceMgmt as rm on rm.resourceId = rr.requestedResourceId " +
//            "join ResourceMgmtRoles as rmr on rmr.roleId = rr.requestedRoleId " +
//
//            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId "+
//            "join ResourceMgmtFieldMap as rfm on rfv.fieldId = rfm.fieldId and rfm.fields = 'Department' "+
//
//            "join ResourceMgmtFieldValue as rfv1 on rm.resourceId = rfv1.resourceId "+
//            "join ResourceMgmtFieldMap as rfm1 on rfv1.fieldId = rfm1.fieldId "+
//            "join ResourceMgmtEmploymentType as et on cast(et.empTypeId as text) = rfv1.fieldValue and rfm1.fields='Employment Type' "+
//
//            "join ProjectMgmt as pm on pm.projectId = rr.projectId " +
//            "join ProjectMgmtFieldValue as pmv on pmv.projectId = pm.projectId " +
//            "join ProjectMgmtFieldMap as pmf on pmf.fieldId = pmv.fieldId " +
//
//            "join ProjectResourceMapping as prm on prm.requestId = rr.requestId " +
//            "join ProjectResourceMappingExtended as prme on prme.mapId = prm.mapId " +
//
//            "where pmf.fields = 'Project Name' and rm.resourceId!=0 and " +
//            "rm.dateOfJoin <= :endDate and (rm.lastWorkingDate>= :startDate or rm.lastWorkingDate is null) " +
//            "and rfv.fieldValue in :departmentIds " +
//            "group by rr.requestId, rm.resourceId, rm.firstName, rm.lastName, rmr.role, et.employmentType, pm.projectId, pmv.fieldValue, " +
//            "rr.allocationStartDate, rr.allocationEndDate, prme.requestedFte, rr.requestStatus, " +
//            "rr.createdBy, rr.createdDate, rr.modifiedBy, rr.modifiedDate")
//    List<GetRequestedResourcesData> getAllRequestedResourceData(@RequestParam("endDate") Date endDate,@RequestParam("startDate") Date startDate,
//                                                                @RequestParam("departmentIds") List<String> departmentIds);


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

    @Query("select rqr from RequestedResources as rqr " +
            "join ProjectResourceMapping as prm on prm.requestId = rqr.requestId " +
            "where prm.mapId in :mapIds ")
    List<RequestedResources> getRequestedResourceListByMapIds(@Param("mapIds") List<Integer> mapIds);

    @Query("select rqr from RequestedResources as rqr " +
            "join ProjectResourceMapping as prm on prm.requestId = rqr.requestId " +
            "where (rqr.requestedResourceId in :resourceIds or rqr.requestedResourceId is null)")
    List<RequestedResources> getAllResourceNotifications(@Param("resourceIds") List<Integer> resourceIds);

    @Query("select rqr from RequestedResources as rqr " +
            "join ProjectResourceMapping as prm on prm.requestId = rqr.requestId " +
            "where rqr.projectId in :projectIds and rqr.notifyPm = '1' " +
            "and prm.isDeleted = '0'")
    List<RequestedResources> getAllProjectNotifications(@Param("projectIds") List<Integer> projectIds);
}

