package com.forecastera.service.projectmanagement.repository;/*
 * @Author Ashutosh Mishra
 * @Create 6/12/2023
 * @Description
 */


import com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation;
import com.forecastera.service.projectmanagement.dto.utilityClass.ResourceDailyFteSumData;
import com.forecastera.service.projectmanagement.entity.ProjectResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProjectResourceMappingRepo extends JpaRepository<ProjectResourceMapping,Integer> {

    @Query("select concat(rm.firstName, ' ', rm.lastName) from ProjectResourceMapping as prm " +
            "inner join ResourceMgmt as rm on rm.resourceId = prm.resourceId " +
            "where prm.isDeleted = '0' and prm.projectId = :projectId " +
            "and (prm.allocationStartDate < :projectStartDate or prm.allocationEndDate > :projectEndDate) ")
    List<String> getResourceListOutsideProjectDateRange(@Param("projectId") Integer projectId,
                                                        @Param("projectStartDate") Date projectStartDate, @Param("projectEndDate") Date projectEndDate);

    @Query("select prm from ProjectResourceMapping as prm " +
            "where prm.isDeleted = '0' and prm.projectId = :projectId and prm.resourceId = :resourceId and prm.mapId!=:mapId " +
            "and prm.allocationStartDate<= :endDate and prm.allocationEndDate>= :startDate")
    List<ProjectResourceMapping> getOverlappingAllocations(@Param("mapId") Integer mapId, @Param("projectId") Integer projectId,
                                                           @Param("resourceId") Integer resourceId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation" +
            "(prme.id as id, prme.mapId as mapId, prme.allocationDate as allocationDate, prme.fte as allocatedFte, " +
            "coalesce(prme.requestedFte, 0.00) as requestedFte, 0.00 as availableFte) " +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' and prm.projectId = :projectId and " +
            "prme.allocationDate>= :startDate and prme.allocationDate<= :endDate")
    List<AllResourceActiveAllocation> getAllResourceActiveAllocation(@Param("projectId") Integer projectId,
                                                                     @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.ResourceDailyFteSumData" +
            "(prm.resourceId, prme.allocationDate, coalesce(sum(prme.fte),0.00))" +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' and prm.resourceId in :resourceIds and " +
            "prme.allocationDate>= :startDate and prme.allocationDate<= :endDate " +
            "group by prm.resourceId, prme.allocationDate ")
    List<ResourceDailyFteSumData> getAllAllocationsForResources(@Param("resourceIds") List<Integer> resourceIds,
                                                                @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation" +
            "(prme.id as id, prme.mapId as mapId, prme.allocationDate as allocationDate, prme.fte as allocatedFte, " +
            "coalesce(prme.requestedFte, 0.00) as requestedFte, 0.00 as availableFte) " +
            "from ProjectResourceMapping as prm " +
            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' and prm.projectId = :projectId ")
    List<AllResourceActiveAllocation> getAllResourceActiveAllocationAtUpdateProjectDetails(@Param("projectId") Integer projectId);

//    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.AllResourceActiveAllocation(" +
//            "prme.id as id, prme.mapId as mapId, prme.allocationDate as allocationDate, prme.fte as allocatedFte, prme.requestedFte as requestedFte, 0.00 as availableFte) " +
//            "from ProjectResourceMapping as prm " +
//            "inner join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
//            "where prm.resourceId = :resourceId and prm.isDeleted = false " +
//            "and prm.allocationStartDate <= :endDate and prm.allocationEndDate >= :startDate ")
//    List<AllResourceActiveAllocation> getResourceByIdActiveAllocation(@Param("resourceId") Integer resourceId,
//                                                                      @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select coalesce(sum(prme.requestedFte),0.00) " +
            "from ProjectResourceMapping as prm " +
            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prme.allocationDate <= prm.allocationEndDate and prme.allocationDate >= prm.allocationStartDate and prm.mapId = :mapId")
    BigDecimal getTotalRequestedFte(@Param("mapId") Integer mapId);

//    @Query("SELECT new com.forecastera.service.projectmanagement.dto.response.GetKanbanData(" +
//            "pra.mapId as mapId, pra.resourceId as resourceId, pra.projectId as projectId, pra.allocationStartDate as allocationStartDate, pra.allocationEndDate as allocationEndDate, pra.fte as fte,pra.description as description, " +
//            "concat(rm.firstName,' ',COALESCE(rm.lastName,'')) as resourceFullName , rm.empLocation as empLocation , rmet.employmentType as employmentType , rmr.role as role , pm.projectName as projectName) " +
//            "FROM ProjectResourceMapping pra " +
//            "JOIN ResourceMgmt rm ON pra.resourceId = rm.resourceId " +
//            "JOIN ResourceMgmtEmploymentType rmet ON rm.empTypeId = rmet.empTypeId " +
//            "JOIN ResourceMgmtRoles rmr ON rm.role LIKE CONCAT('%', CAST(rmr.roleId AS string), '%') " +
//            "JOIN ProjectMgmt pm ON pra.projectId = pm.projectId " +
//            "WHERE pra.projectId = :projectId")
//    List<GetKanbanData> getKanbanDataByProjectId(@Param("projectId") int projectId);

//    @Query("SELECT new com.forecastera.service.projectmanagement.dto.response.GetKanbanData(" +
//            "pra.mapId as mapId, pra.resourceId as resourceId, pra.projectId as projectId, pra.allocationStartDate as allocationStartDate, pra.allocationEndDate as allocationEndDate, pra.fte as fte,pra.description as description, " +
//            "concat(rm.firstName,' ',COALESCE(rm.lastName,'')) as resourceFullName , rm.empLocation as empLocation , rmet.employmentType as employmentType , rmr.role as role , pm.projectName as projectName) " +
//            "FROM ProjectResourceMapping pra " +
//            "JOIN ResourceMgmt rm ON pra.resourceId = rm.resourceId " +
//            "JOIN ResourceMgmtEmploymentType rmet ON rm.empTypeId = rmet.empTypeId " +
//            "JOIN ResourceMgmtRoles rmr ON rm.role LIKE CONCAT('%', CAST(rmr.roleId AS string), '%') " +
//            "JOIN ProjectMgmt pm ON pra.projectId = pm.projectId " +
//            "WHERE pra.projectId = :projectId")
//    List<GetKanbanData> getKanbanDataByProjectId(@Param("projectId") int projectId);

//    ProjectResourceMapping findByMapId(Integer mapId);

//    @Transactional
//    @Modifying
//    @Query("UPDATE ProjectResourceMapping pra SET pra.isDeleted = 'true' WHERE pra.mapId = :mapId")
//    void deleteByMapId(@Param("mapId") Integer mapId);

    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.ResourceDailyFteSumData" +
            "(prm.resourceId, prme.allocationDate, coalesce(sum(prme.fte),0.00)) " +
            "from ProjectResourceMapping as prm " +
            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' and prm.resourceId in :resourceIds " +
            "and prme.allocationDate >= :startDate and prme.allocationDate <= :endDate " +
            "group by prme.allocationDate, prm.resourceId")
    List<ResourceDailyFteSumData> getResourceDailyFteData(@Param("resourceIds") List<Integer> resourceIds, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select prm from ProjectResourceMapping as prm " +
            "where prm.isDeleted = '0' and projectId = :projectId ")
    List<ProjectResourceMapping> getAlreadyAddedResourceList(@Param("projectId") Integer projectId);
}
