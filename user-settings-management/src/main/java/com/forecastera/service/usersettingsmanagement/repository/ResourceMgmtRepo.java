package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */


import com.forecastera.service.usersettingsmanagement.dto.utilityClass.ResourceIdDepartment;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.ResourceIdEmail;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ResourceMgmtRepo extends JpaRepository<ResourceMgmt, Integer> {

    // ask here, how to find among these values
    @Query(value = "\n" +
            "SELECT *\n" +
            "FROM dbo.resource_master AS rm\n" +
            "WHERE rm.resource_id != 0\n" +
            "AND EXISTS (\n" +
            "    SELECT 1\n" +
            "    FROM dbo.resource_mgmt_field_value AS rv\n" +
            "    INNER JOIN dbo.resource_mgmt_field_map AS rf ON rv.field_id = rf.field_id\n" +
            "    WHERE rv.resource_id = rm.resource_id\n" +
            "    AND rf.fields = 'Role'\n" +
            "    AND CHARINDEX('1', rv.field_value) > 0\n" +
            ");", nativeQuery = true)
    List<ResourceMgmt> getProjectOwners();

    @Query("select r from ResourceMgmt r where r.resourceId!=0")
    List<ResourceMgmt> getReportingManagers();

    @Query("select new com.forecastera.service.usersettingsmanagement.dto.utilityClass.ResourceIdDepartment" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName), dm.department)" +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "join ResourceMgmtDepartment as dm on cast(dm.departmentId as text) = rfv.fieldValue " +
            "where rfm.fields = 'Department' " +
            "and rm.dateOfJoin <= :startDate " +
            "and (rm.lastWorkingDate is null or rm.lastWorkingDate> :endDate)")
    List<ResourceIdDepartment> getResourceIdDepartmentList(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select new com.forecastera.service.usersettingsmanagement.dto.utilityClass.ResourceIdEmail" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName) as resourceName, rfv.fieldValue)" +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Email'")
    List<ResourceIdEmail> getAllResourceIdEmail();
}
