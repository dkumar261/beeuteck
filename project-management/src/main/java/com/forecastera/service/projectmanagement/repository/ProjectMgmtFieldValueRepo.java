package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.dto.response.GetProjectDetailsById;
import com.forecastera.service.projectmanagement.entity.ProjectMgmtFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMgmtFieldValueRepo extends JpaRepository<ProjectMgmtFieldValue,Integer> {

    @Query("select pcv from ProjectMgmtFieldValue as pcv where pcv.projectId = :projectId")
    List<ProjectMgmtFieldValue> getProjectDetailsForUpdateById(@Param("projectId") Integer projectId);

//    @Query("select new com.forecastera.service.projectmanagement.dto.response.GetProjectDetailsById(" +
//            "pfv.fieldId, pfm.fields, pfv.fieldValue, pfm.fieldType, pfm.settingType) " +
//            "from ProjectMgmtFieldValue as pfv " +
//            "join ProjectMgmtFieldMap as pfm on pfv.fieldId = pfm.fieldId " +
//            "where pfv.projectId = :projectId and pfm.isEnabled = true and pfm.projDetailsView = true")
//    List<GetProjectDetailsById> getProjectDetailsById (@Param("projectId") Integer projectId);
}
