package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.ProjectMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMgmtFieldMapRepo extends JpaRepository<ProjectMgmtFieldMap, Integer> {

    @Query("select a from ProjectMgmtFieldMap a where a.settingType = 0")
    List<ProjectMgmtFieldMap> getAdminProjectManagementComponents();

    @Query("select a from ProjectMgmtFieldMap a where a.settingType in (2,3,4)")
    List<ProjectMgmtFieldMap> getAdminProjectManagementFields();

    @Query("select a from ProjectMgmtFieldMap a where a.settingType = 1")
    List<ProjectMgmtFieldMap> getAdminProjectManagementButtons();
}
