package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.response.GetResourceMgmtDepartment;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMgmtDepartmentRepo extends JpaRepository<ResourceMgmtDepartment,Integer> {

    @Query("select new com.forecastera.service.usersettingsmanagement.dto.response.GetResourceMgmtDepartment" +
            "(dm.departmentId, dm.department, dm.createdBy, dm.createdDate, dm.modifiedBy, dm.modifiedDate, " +
            "dm.parentDepartmentId, true, dm.rowOrder, dm.departmentCode, dm.status, dm.shortDescription, " +
            "dm.resourceId, concat(rm.firstName, ' ', rm.lastName), false) " +
            "from ResourceMgmtDepartment as dm " +
            "left join ResourceMgmt as rm on rm.resourceId = dm.resourceId " +
            "where dm.isActive = '1'")
    List<GetResourceMgmtDepartment> getActiveDepartmentList();


    @Query("select rfv.fieldValue from  ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfv.fieldId = rfm.fieldId and rfm.fields = 'Department' " +
            "where rm.resourceId!=0")
    List<String> getResourceDepartments();

}


