package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 20-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.dto.utilityClass.*;
import com.forecastera.service.resourcemanagement.entity.ProjectResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectResourceMappingRepo extends JpaRepository<ProjectResourceMapping, Integer> {

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.AllocatedFteAverageByMapId" +
            "(prm.mapId, coalesce(sum(prme.fte),0.00)) " +
            "from ProjectResourceMapping as prm " +
            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prme.allocationDate <= prm.allocationEndDate and prme.allocationDate >= prm.allocationStartDate and prm.mapId in :mapIds " +
            "group by prm.mapId ")
    List<AllocatedFteAverageByMapId> getTotalAllocatedFteForMapIds(@Param("mapIds") List<Integer> mapIds);

//    @Query(value="select new com.forecastera.service.resourcemanagement.dto.response.GetResourceFteDetails(prm.resourceId as resourceId, " +
//            "sum((least(prm.allocationEndDate, :endDate) - greatest(prm.allocationStartDate, :startDate)+1) * prm.fte)/" +
//            "(cast(:endDate as date) - cast(:startDate as date)) as avgFte) " +
//            "from ProjectResourceMapping as prm " +
//            "where prm.isDeleted = false " +
//            "group by prm.resourceId")
//    List<GetResourceFteDetails> getResourceAvgFte(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceActiveAllocation(" +
            "prme.id as id, prme.mapId as mapId, prme.allocationDate as allocationDate, prme.fte as fte, prme.requestedFte) " +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' " +
            "and prm.allocationStartDate <= :endDate and prm.allocationEndDate >= :startDate ")
    List<AllResourceActiveAllocation> getAllResourceActiveAllocation(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceActiveAllocation(" +
            "prme.id as id, prme.mapId as mapId, prme.allocationDate as allocationDate, prme.fte as fte, prme.requestedFte) " +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' and prm.mapId = :mapId " +
            "and prm.allocationStartDate <= prme.allocationDate and prm.allocationEndDate >= prme.allocationDate ")
    List<AllResourceActiveAllocation> getAllocationDataByMapId(@Param("mapId") Integer mapId);

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceActiveAllocation(" +
            "prme.id as id, prme.mapId as mapId, prme.allocationDate as allocationDate, prme.fte as fte, prme.requestedFte) " +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.resourceId = :resourceId and prm.isDeleted = '0' " +
            "and prm.allocationStartDate <= :endDate and prm.allocationEndDate >= :startDate " +
            "and prme.allocationDate >= prm.allocationStartDate and prme.allocationDate<= prm.allocationEndDate ")
    List<AllResourceActiveAllocation> getResourceByIdActiveAllocation(@Param("resourceId") Integer resourceId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select pfv.fieldValue from ProjectResourceMapping as prm " +
            "join ProjectMgmtFieldValue as pfv on pfv.projectId = prm.projectId " +
            "join ProjectMgmtFieldMap as pfm on pfm.fieldId = pfv.fieldId " +
            "where prm.isDeleted = '0' and prm.resourceId = :resourceId and pfm.fields = 'Project Name' " +
            "and (prm.allocationStartDate < :dateOfJoin or prm.allocationEndDate > :lastWorkingDate) ")
    List<String> getProjectListOutsideResourceDateRange(@Param("resourceId") Integer resourceId,
                                                        @Param("dateOfJoin") Date dateOfJoin, @Param("lastWorkingDate") Date lastWorkingDate);

    @Query("select pfv.fieldValue from ProjectResourceMapping as prm " +
            "inner join ProjectMgmtFieldValue as pfv on pfv.projectId = prm.projectId " +
            "inner join ProjectMgmtFieldMap as pfm on pfm.fieldId = pfv.fieldId " +
            "where prm.isDeleted = '0' and prm.resourceId = :resourceId and pfm.fields = 'Project Name' " +
            "and (prm.allocationStartDate < :dateOfJoin) ")
    List<String> getProjectListBeforeResourceDateOfJoin(@Param("resourceId") Integer resourceId, @Param("dateOfJoin") Date dateOfJoin);

//    @Query("select coalesce(sum(prme.fte),0.00) " +
//            "from ProjectResourceMapping as prm " +
//            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
//            "where prm.resourceId = :resourceId and prme.mapId != :mapId and prm.isDeleted = false " +
//            "and prme.allocationDate <= :endDate and prme.allocationDate >= :startDate ")
//    BigDecimal getMonthlyAllocatedTillNow(@Param("mapId") Integer mapId, @Param("resourceId") Integer resourceId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceRequestedProjectDetails" +
            "(prm.resourceId, prm.projectId, pfv.fieldValue, prm.allocationStartDate, prm.allocationEndDate, coalesce(prm.requestedFteAvg, 0.00)) " +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectMgmtFieldValue as pfv on pfv.projectId = prm.projectId " +
            "inner join ProjectMgmtFieldMap as pfm on pfm.fieldId = pfv.fieldId " +
            "where prm.isDeleted = '0' and prm.resourceId in :resourceIds and pfm.fields = 'Project Name' " +
            "and (prm.allocationStartDate <= :endDate) and (prm.allocationEndDate >= :startDate)")
    List<ResourceRequestedProjectDetails> getAllocatedProjectsForResourceIds(@Param("resourceIds") List<Integer> resourceIds, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Optional<ProjectResourceMapping> findByRequestId(Integer requestId);

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceDailyFteSumData" +
            "(prm.resourceId, prme.allocationDate, coalesce(sum(prme.fte),0.00)) " +
            "from ProjectResourceMapping as prm " +
            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' and prm.resourceId in :resourceIds " +
            "and prme.allocationDate >= :startDate and prme.allocationDate <= :endDate " +
            "group by prme.allocationDate, prm.resourceId")
    List<ResourceDailyFteSumData> getResourceDailyFteData(@Param("resourceIds") List<Integer> resourceIds, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select prm from ProjectResourceMapping as prm where prm.isDeleted = '0' and prm.resourceId = :resourceId")
    List<ProjectResourceMapping> getAllProjectResourceMappingByResourceId(@Param("resourceId") Integer resourceId);

    @Query("select prm from ProjectResourceMapping as prm where prm.isDeleted = '0' and prm.projectId = :projectId")
    List<ProjectResourceMapping> getAllocationResourcesOnProject(@Param("projectId") Integer projectId);
}
