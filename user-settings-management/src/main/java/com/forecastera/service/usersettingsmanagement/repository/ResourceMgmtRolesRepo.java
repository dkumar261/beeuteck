package com.forecastera.service.usersettingsmanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.response.GetResourceMgmtRoles;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMgmtRolesRepo extends JpaRepository<ResourceMgmtRoles,Integer> {

    @Query("select new com.forecastera.service.usersettingsmanagement.dto.response.GetResourceMgmtRoles " +
            "(rr.roleId, rr.role, rr.createdBy, rr.createdDate, rr.modifiedBy, rr.modifiedDate, " +
            "rr.parentRoleId, rr.departmentId, dm.department, rr.isActive, rr.rowOrder) from ResourceMgmtRoles as rr " +
            "left join ResourceMgmtDepartment as dm on rr.departmentId = dm.departmentId ")
    List<GetResourceMgmtRoles> getResourceMgmtRoles();

    @Query("select rr from ResourceMgmtRoles as rr where rr.departmentId = :departmentId")
    List<ResourceMgmtRoles> getRolesByDepartmentId(@Param("departmentId") Integer departmentId);
}
