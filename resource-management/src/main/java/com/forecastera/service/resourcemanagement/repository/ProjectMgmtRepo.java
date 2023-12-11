package com.forecastera.service.resourcemanagement.repository;


import com.forecastera.service.resourcemanagement.dto.utilityClass.ProjectDetailsForNotification;
import com.forecastera.service.resourcemanagement.entity.ProjectMgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * @Author Kanishk Vats
 * @Create 05-08-2023
 * @Description
 */
@Repository
public interface ProjectMgmtRepo extends JpaRepository<ProjectMgmt,Integer> {

    @Query("select pfv.fieldValue " +
            "from ProjectMgmt as pm " +
            "join ProjectMgmtFieldValue as pfv on pm.projectId = pfv.projectId " +
            "join ProjectMgmtFieldMap as pfm on pfv.fieldId = pfm.fieldId " +
            "where pm.projectId = :projectId and pfm.fields = 'Project Name' ")
    String getProjectName(@Param("projectId") Integer projectId);


    @Query("select new com.forecastera.service.resourcemanagement.dto.utilityClass.ProjectDetailsForNotification" +
            "(pm.projectId, pfv.fieldValue, pm.createdBy) " +
            "from ProjectMgmt as pm " +
            "join ProjectMgmtFieldValue as pfv on pfv.projectId = pm.projectId " +
            "join ProjectMgmtFieldMap as pfm on pfm.fieldId = pfv.fieldId " +
            "where pfm.fields = 'Project Owner' ")
    List<ProjectDetailsForNotification> getProjectDetailsForNotification();
}
