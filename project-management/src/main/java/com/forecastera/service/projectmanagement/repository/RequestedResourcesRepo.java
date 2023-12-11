package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Sowjanya Aare
 * @Create 11-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.RequestedResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestedResourcesRepo extends JpaRepository<RequestedResources, Integer> {


//
//    @Query("select new com.forecastera.service.projectmanagement.dto.response.GetRequestedResourcesData" +
//            "(rr.requestId, concat(rm.firstName, ' ', rm.lastName), rmr.role, pmv.fieldValue, " +
//            "rr.allocationStartDate, rr.allocationEndDate, rr.requestedFte, rr.requestStatus, " +
//            "rr.createdBy, rr.createdDate, rr.modifiedBy, rr.modifiedDate,rr.notify) " +
//            "from RequestedResources as rr " +
//            "join ResourceMgmt as rm on rm.resourceId = rr.requestedResourceId " +
//            "join ResourceMgmtRoles as rmr on rmr.roleId = rr.requestedRoleId " +
//            "join ProjectMgmt as pm on pm.projectId = rr.projectId " +
//            "join ProjectMgmtFieldValue as pmv on pmv.projectId = pm.projectId " +
//            "join ProjectMgmtFieldMap as pmf on pmf.fieldId = pmv.fieldId " +
//            "where pmf.fields = 'Project Name'")
//    List<GetRequestedResourcesData> getAllRequestedResourceData();

}
