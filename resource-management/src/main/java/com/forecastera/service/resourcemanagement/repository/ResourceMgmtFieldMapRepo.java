package com.forecastera.service.resourcemanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.dto.response.GetResourceDetailsById;
import com.forecastera.service.resourcemanagement.entity.ResourceMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select a from ResourceMgmtFieldMap a where a.enabled = '1' and a.resourceAnalyticsView = '1' and a.settingType in (2,3,4)")
    List<ResourceMgmtFieldMap> getResourceAnalyticsSettings();

    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetResourceDetailsById(" +
            "rfm.fieldId, rfm.fields, rfv.fieldValue, rfm.resourceType, rfm.settingType) " +
            "from ResourceMgmtFieldMap as rfm " +
            "left outer join ResourceMgmtFieldValue as rfv on rfm.fieldId = rfv.fieldId and (rfv.resourceId = :resourceId) " +
            "where rfm.settingType in (2,3,4) and rfm.enabled = '1' and rfm.resourcesCardView = '1' order by rfm.fieldId")
    List<GetResourceDetailsById> getResourceDetailsById (@Param("resourceId") Integer resourceId);

    @Query("select rfm from ResourceMgmtFieldMap as rfm where rfm.fields in :fieldNames ")
    List<ResourceMgmtFieldMap> getFieldDataByFieldName (@Param("fieldNames") List<String> fieldNames);
}
