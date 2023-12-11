package com.forecastera.service.usersettingsmanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMgmtFieldMapRepo extends JpaRepository<ResourceMgmtFieldMap,Integer> {
    @Query("select a from ResourceMgmtFieldMap a where a.settingType = 0")
    List<ResourceMgmtFieldMap> getAdminResourceManagementComponents();

    @Query("select a from ResourceMgmtFieldMap a where a.settingType in (2,3,4)")
    List<ResourceMgmtFieldMap> getAdminResourceManagementFields();

    @Query("select a from ResourceMgmtFieldMap a where a.settingType = 1")
    List<ResourceMgmtFieldMap> getAdminResourceManagementButtons();

}
