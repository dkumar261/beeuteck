package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Sowjanya Aare
 * @Create 11-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.GeneralSettings;
import com.forecastera.service.projectmanagement.entity.ResourceMgmtRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMgmtRolesRepo extends JpaRepository<ResourceMgmtRoles, Integer> {

    @Query("select role  from ResourceMgmtRoles where roleId = :roleId")
    String roleNameByroleId (@Param("roleId") Integer roleId);
}
