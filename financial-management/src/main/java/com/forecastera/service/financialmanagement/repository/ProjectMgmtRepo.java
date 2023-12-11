package com.forecastera.service.financialmanagement.repository;


import com.forecastera.service.financialmanagement.dto.utilityDtoClass.ProjectIdName;
import com.forecastera.service.financialmanagement.entity.ProjectMgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/*
 * @Author Kanishk Vats
 * @Create 12-07-2023
 * @Description
 */
@Repository
public interface ProjectMgmtRepo extends JpaRepository<ProjectMgmt,Integer> {

    @Query("select new com.forecastera.service.financialmanagement.dto.utilityDtoClass.ProjectIdName(pm.projectId, pfv.fieldValue as projectName) " +
            "from ProjectMgmt as pm " +
            "join ProjectMgmtFieldValue as pfv on pfv.projectId = pm.projectId " +
            "join ProjectMgmtFieldMap as pfm on pfm.fieldId = pfv.fieldId " +
            "where pm.projectId in :projectIds and pfm.fields = 'Project Name' ")
    List<ProjectIdName> getProjectNamesByIds(Set<Integer> projectIds);
}
