package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.dto.response.*;
import com.forecastera.service.resourcemanagement.dto.utilityClass.*;
import com.forecastera.service.resourcemanagement.entity.ResourceMgmt;
import com.forecastera.service.resourcemanagement.entity.ResourceMgmtFieldValue;
import org.hibernate.type.IntegerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ResourceMgmtRepo extends JpaRepository<ResourceMgmt, Integer> {
    /*@Query(value = "select * from resource_master r where rm.resource_id!=0 and string_to_array(role,',') && array['1']", nativeQuery = true)
    List<ResourceMgmt> getProjectOwners();*/

    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetTotalResource" +
            "(et.empTypeId as employmentTypeId, et.color as employmentTypeColor, et.employmentType as employmentTypeName, rm.resourceId, concat(rm.firstName, ' ', rm.lastName)) " +
            "from ResourceMgmt as rm " +
            "inner join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "inner join ResourceMgmtFieldMap as rfm on rfv.fieldId = rfm.fieldId " +
            "inner join ResourceMgmtEmploymentType as et on cast(et.empTypeId as text) = rfv.fieldValue " +

            "where rm.resourceId!=0 and rfm.fields = 'Employment Type' and rm.resourceId in :resourceIds " +
            "and rfm.settingType!=3 and rm.dateOfJoin<= :endDate " +
            "and (rm.lastWorkingDate >= :startDate or rm.lastWorkingDate is null)")
    List<GetTotalResource> getTotalResource(@Param("startDate") Date startDate, @Param("endDate") Date endDate,@Param("resourceIds") List<Integer> resourceIds);

    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetTotalResource" +
            "(et.empTypeId as employmentTypeId, et.color as employmentTypeColor, et.employmentType as employmentTypeName, rm.resourceId, concat(rm.firstName, ' ', rm.lastName)) " +
            "from ResourceMgmt as rm " +
            "inner join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "inner join ResourceMgmtFieldMap as rfm on rfv.fieldId = rfm.fieldId " +
            "inner join ResourceMgmtEmploymentType as et on cast(et.empTypeId as text) = rfv.fieldValue " +
            "inner join ResourceMgmtFieldValue as rfv1 on rm.resourceId = rfv1.resourceId " +
            "inner join ResourceMgmtFieldMap as rfm1 on rfv1.fieldId = rfm1.fieldId " +
            "where rm.resourceId!=0 and rfm.fields = 'Employment Type' and rfm.settingType!=3 and rm.dateOfJoin<= :endDate " +
            "and (rm.lastWorkingDate >= :startDate or rm.lastWorkingDate is null) " +
            "and rfm1.fields = 'Department' and rfv1.fieldValue = cast(:departmentId as text)")
    List<GetTotalResource> getTotalResourceByDepartmentId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("departmentId") Integer departmentId);

//    @Query(nativeQuery = true)
//    List<GetResourcesByRoles> getResourcesByRoles(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetResourceDetailsById" +
//            "(rm.resourceId as resourceId, rm.firstName as firstName, rm.lastName as lastName, rm.email as email, rm.phone as phone, " +
//            "rm.dateOfJoin as dateOfJoin, rm.location as location, rm.departmentId as departmentId, rm.role as role, " +
//            "rm.reportingMgr as reportingMgr, rm.costPerHour as costPerHour, rm.fteRate as fteRate, rm.variable as variable, " +
//            "rm.bonus as bonus, rm.skills as skills, concat(rm2.firstName,' ',COALESCE(rm2.lastName,'')) as createdBy, " +
//            "rm.createdDate as createdDate, concat(rm3.firstName,' ',COALESCE(rm3.lastName,'')) as modifiedBy, " +
//            "rm.modifiedDate as modifiedDate, rm.empTypeId as empTypeId, rm.empLocation as empLocation) " +
//            "from ResourceMgmt as rm " +
//            "join ResourceMgmt as rm2 on rm.createdBy = rm2.resourceId " +
//            "join ResourceMgmt as rm3 on rm.modifiedBy = rm3.resourceId " +
//            "where rm.resourceId = :resourceId")
//    GetResourceDetailsById getResourceDetailsById(@Param("resourceId") Integer resourceId);

    @Query("select concat(rm.firstName,' ',rm.lastName) from ResourceMgmt as rm where rm.resourceId = :resourceId")
    String getReportingManagerById(@Param("resourceId") Integer resourceId);

    @Query("select rm from ResourceMgmt as rm where rm.resourceId = :resourceId")
    ResourceMgmt getResourceDetailsById(@Param("resourceId") Integer resourceId);

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.AllResourceAllAllocationForGanttChart" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName), rm.dateOfJoin, rm.lastWorkingDate, prme.allocationDate, coalesce(sum(prme.fte),0.00)) " +
            "from ResourceMgmt as rm " +
            "join ProjectResourceMapping as prm on rm.resourceId = prm.resourceId " +
            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' " +
            "and prme.allocationDate <= :endDate and prme.allocationDate >= :startDate and rm.resourceId in :resourceIds " +
            "group by rm.resourceId, concat(rm.firstName, ' ', rm.lastName), rm.dateOfJoin, rm.lastWorkingDate, prme.fte, prme.allocationDate " +
            "order by rm.resourceId, prme.allocationDate")
//            "and prm.allocationStartDate <= :endDate and prm.allocationEndDate >= :startDate ")
    List<AllResourceAllAllocationForGanttChart> getAllResourceActiveAllocationForGanttChart(@Param("resourceIds") List<Integer> resourceIds,
                                                                                            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select rm " +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Email' and rfv.fieldValue = :email ")
    List<ResourceMgmt> getResourceByEmail(@Param("email") String email);

    @Query("select rfv " +
            "from ResourceMgmtFieldValue as rfv " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Reporting Manager' and rfv.resourceId = :resourceId ")
    List<ResourceMgmtFieldValue> getResourceReportingManagerById(@Param("resourceId") Integer resourceId);

    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceIdEmailDetail" +
            "(rm.resourceId, rfv.fieldValue)" +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Email'")
    List<ResourceIdEmailDetail> getResourceIdEmailList();


    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceIdDepartmentHead" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName) as resourceName, dm.departmentId as departmentId, " +
            "dm.department as departmentName, " +
            "coalesce(dm1.departmentId, null) as managedDepartmentId, coalesce(dm1.department, null) as managedDepartmentName) " +

            "from ResourceMgmt as rm " +

            "join ResourceMgmtFieldValue as rfv1 on rm.resourceId = rfv1.resourceId " +
            "join ResourceMgmtFieldMap as rfm1 on rfm1.fieldId = rfv1.fieldId " +

            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "join ResourceMgmtDepartment as dm on cast(dm.departmentId as text) = rfv.fieldValue " +

            "left join ResourceMgmtDepartment as dm1 on rm.resourceId = dm1.resourceId " +

            "where rfm.fields = 'Department' and rfm1.fields = 'Email' and rfv1.fieldValue = :email ")
    List<ResourceIdDepartmentHead> getAllDepartmentsUnderResource(@Param("email") String email);


    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.DepartmentStringIdName" +
            "(rfv.fieldValue, dm.department) " +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "join ResourceMgmtDepartment as dm on cast(dm.departmentId as text) = rfv.fieldValue " +
            "where rfm.fields = 'Department' and rm.resourceId = :resourceId ")
    DepartmentStringIdName getResourceDepartmentId(@Param("resourceId") Integer resourceId);

    @Query("select rm " +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Department' and rfv.fieldValue = :departmentId ")
    List<ResourceMgmt> getAllResourceByDepartmentId(@Param("departmentId") String departmentId);


    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.ResourceTotalAllocationSum" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName), rm.dateOfJoin, rm.lastWorkingDate, coalesce(sum(prme.fte), 0.00)) " +
            "from ResourceMgmt as rm " +
            "join ProjectResourceMapping as prm on prm.resourceId = rm.resourceId " +
            "join ProjectResourceMappingExtended as prme on prm.mapId = prme.mapId " +
            "where prm.isDeleted = '0' " +
            "and prme.allocationDate >= :startDate and prme.allocationDate<= :endDate " +
            "and rm.resourceId in :resourceIds " +
            "group by rm.resourceId, rm.firstName, rm.lastName, rm.dateOfJoin, rm.lastWorkingDate")
    List<ResourceTotalAllocationSum> getResourceTotalAllocationSum(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                                                   @Param("resourceIds") List<Integer> resourceIds);

    @Query("select rfv.fieldValue " +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Department' and rm.resourceId in :resourceIds")
    List<String> getAllResourceDepartmentList(@Param("resourceIds") List<Integer> resourceIds);
}
