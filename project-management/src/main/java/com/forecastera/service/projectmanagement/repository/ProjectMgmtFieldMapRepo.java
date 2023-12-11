package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.dto.response.GetProjectDetailsById;
import com.forecastera.service.projectmanagement.entity.ProjectMgmtFieldMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select a from ProjectMgmtFieldMap a where a.isEnabled = '1' and a.projAnalysisView = '1' and a.settingType in (2,3,4)")
    List<ProjectMgmtFieldMap> getProjectAnalyticsSettings();

    @Query("select new com.forecastera.service.projectmanagement.dto.response.GetProjectDetailsById(" +
            "pfm.fieldId, pfm.fields, pfv.fieldValue, pfm.fieldType, pfm.settingType) " +
            "from ProjectMgmtFieldMap as pfm " +
            "left join ProjectMgmtFieldValue as pfv on pfm.fieldId = pfv.fieldId and (pfv.projectId = :projectId) " +
            "where pfm.settingType in (2,3,4) and pfm.isEnabled = '1' and pfm.projDetailsView = '1' order by pfm.fieldId")
    List<GetProjectDetailsById> getProjectDetailsById (@Param("projectId") Integer projectId);

    @Query("select pfm.fieldId from ProjectMgmtFieldMap as pfm where pfm.fields = :fieldName")
    Integer getProjectFieldIdByName(@Param("fieldName") String fieldName);

    @Query("select pfm from ProjectMgmtFieldMap as pfm where pfm.fields in :fieldNames ")
    List<ProjectMgmtFieldMap> getFieldDataByFieldName (@Param("fieldNames") List<String> fieldNames);
}
