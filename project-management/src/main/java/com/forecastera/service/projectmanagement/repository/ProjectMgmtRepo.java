package com.forecastera.service.projectmanagement.repository;

import com.forecastera.service.projectmanagement.dto.response.AddProject;
import com.forecastera.service.projectmanagement.dto.response.GetNoOfProjectsByType;
import com.forecastera.service.projectmanagement.dto.response.GetOverAllProgress;
import com.forecastera.service.projectmanagement.dto.utilityClass.ProjectDetailsForNotification;
import com.forecastera.service.projectmanagement.entity.ProjectMgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/*
 * @Author Uttam Kachhad
 * @Create 17-05-2023
 * @Description
 */
@Repository
public interface ProjectMgmtRepo extends JpaRepository<ProjectMgmt,Integer> {

//    @Query(value = "select new com.forecastera.service.projectmanagement.dto.response.GetOverAllProgress" +
//            "(pm.statusId as statusId, s.color as statusColor, count(pm) as projectCount, s.status as statusName) from ProjectMgmt as pm " +
//            "inner join ProjectMgmtStatus as s on pm.statusId = s.statusId " +
//            "where (pm.startDate>= :startDate and pm.startDate <= :endDate) or (pm.endDate>= :startDate and pm.endDate <= :endDate) " +
//            "group by pm.statusId, s.status, s.color")
//    List<GetOverAllProgress> getOverAllProgress(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select pm.projectId from ProjectMgmt as pm " +
            "join ProjectMgmtFieldValue as pfv1 on pfv1.projectId = pm.projectId " +
            "join ProjectMgmtFieldMap as pfm1 on pfm1.fieldId = pfv1.fieldId " +
            "join ProjectMgmtFieldValue as pfv2 on pfv2.projectId = pm.projectId " +
            "join ProjectMgmtFieldMap as pfm2 on pfm2.fieldId = pfv2.fieldId " +
            "where pfm1.fields = 'Start Date' and pfm2.fields = 'End Date' " +
            "and pfv1.fieldValue <= cast(:endDate as text) and pfv2.fieldValue >= cast(:startDate as text)")
    List<Integer> getProjectIdsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select new com.forecastera.service.projectmanagement.dto.response.GetOverAllProgress" +
            "(ps.statusId as statusId, ps.color as statusColor, ps.status as statusName, pm.projectId as projectId, pfv1.fieldValue as projectName, pfv2.fieldValue) " +
            "from ProjectMgmt as pm " +

            "inner join ProjectMgmtFieldValue as pfv1 on pm.projectId = pfv1.projectId " +
            "inner join ProjectMgmtFieldMap as pfm1 on pfv1.fieldId = pfm1.fieldId and pfm1.fields = 'Project Name' " +

            "inner join ProjectMgmtFieldValue as pfv2 on pm.projectId = pfv2.projectId " +
            "inner join ProjectMgmtFieldMap as pfm2 on pfv2.fieldId = pfm2.fieldId and pfm2.fields = 'Department' " +

            "inner join ProjectMgmtFieldValue as pfv on pm.projectId = pfv.projectId " +
            "inner join ProjectMgmtFieldMap as pfm on pfv.fieldId = pfm.fieldId " +
            "left join ProjectMgmtStatus as ps on cast(ps.statusId as text) = pfv.fieldValue " +
            "where pfm.fields = 'Status' and pfm.settingType!=3 and " +
            "(" +
            "(select cast(pv.fieldValue as date) from ProjectMgmtFieldValue as pv " +
            "inner join ProjectMgmtFieldMap as pf on pv.fieldId = pf.fieldId " +
            "where pv.projectId = pm.projectId and pf.fields = 'Start Date') <= :endDate " +
            "and " +
            "(select cast(pv.fieldValue as date) from ProjectMgmtFieldValue as pv " +
            "inner join ProjectMgmtFieldMap as pf on pv.fieldId = pf.fieldId " +
            "where pv.projectId = pm.projectId and pf.fields = 'End Date') >= :startDate " +
            ") "
            )

    List<GetOverAllProgress> getOverAllProgress(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    @Query(value = "select new com.forecastera.service.projectmanagement.dto.response.GetNoOfProjectsByType" +
//            "(pm.typeId as typeId, t.color as typeColor, count(pm) as projectCount, t.type as typeName) from ProjectMgmt as pm " +
//            "inner join ProjectMgmtType as t on pm.typeId = t.typeId " +
//            "where (pm.startDate>= :startDate and pm.startDate <= :endDate) or (pm.endDate>= :startDate and pm.endDate <= :endDate) " +
//            "group by pm.typeId, t.type, t.color")
//    List<GetNoOfProjectsByType> getNoOfProjectsByType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select new com.forecastera.service.projectmanagement.dto.response.GetNoOfProjectsByType" +
            "(pt.typeId as typeId, pt.color as typeColor, pt.type as typeName, pm.projectId as projectId, pfv1.fieldValue as projectName, pfv2.fieldValue) " +
            "from ProjectMgmt as pm " +

            "inner join ProjectMgmtFieldValue as pfv1 on pm.projectId = pfv1.projectId " +
            "inner join ProjectMgmtFieldMap as pfm1 on pfv1.fieldId = pfm1.fieldId and pfm1.fields = 'Project Name' " +

            "inner join ProjectMgmtFieldValue as pfv2 on pm.projectId = pfv2.projectId " +
            "inner join ProjectMgmtFieldMap as pfm2 on pfv2.fieldId = pfm2.fieldId and pfm2.fields = 'Department' " +

            "inner join ProjectMgmtFieldValue as pfv on pm.projectId = pfv.projectId " +
            "inner join ProjectMgmtFieldMap as pfm on pfv.fieldId = pfm.fieldId " +
            "left join ProjectMgmtType as pt on cast(pt.typeId as text) = pfv.fieldValue " +
            "where pfm.fields = 'Project Type' and pfm.settingType!=3 and " +
            "(" +
                "(select cast(pv.fieldValue as date) from ProjectMgmtFieldValue as pv " +
                "inner join ProjectMgmtFieldMap as pf on pv.fieldId = pf.fieldId " +
                "where pv.projectId = pm.projectId and pf.fields = 'Start Date') <= :endDate " +
            "and " +
                "(select cast(pv.fieldValue as date) from ProjectMgmtFieldValue as pv " +
                "inner join ProjectMgmtFieldMap as pf on pv.fieldId = pf.fieldId " +
                "where pv.projectId = pm.projectId and pf.fields = 'End Date') >= :startDate" +
            ") "
            )
    List<GetNoOfProjectsByType> getNoOfProjectsByType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    @Query("select new com.forecastera.service.projectmanagement.dto.response.GetProjectDetailsById(pm.projectId as projectId, " +
//            "pm.projectName as projectName, pm.owner as owner, pm.statusId as statusId, " +
//            "pm.progress as progress, pm.startDate as startDate, pm.endDate as endDate, pm.daysLeft as daysLeft, " +
//            "pm.typeId as typeId, pm.actualStartDate as actualStartDate, pm.actualEndDate as actualEndDate, " +
//            "pm.budget as budget, pm.description as description, concat(rm2.firstName,' ',COALESCE(rm2.lastName,'')) as createdBy, " +
//            "pm.createdDate as createdDate, concat(rm3.firstName,' ',COALESCE(rm3.lastName,'')) as modifiedBy, " +
//            "pm.modifiedDate as modifiedDate, pm.priorityId as priorityId) " +
//            "from ProjectMgmt as pm " +
//            "join ResourceMgmt as rm2 on pm.createdBy = rm2.resourceId " +
//            "join ResourceMgmt as rm3 on pm.modifiedBy = rm3.resourceId " +
//            "where pm.projectId = :projectId")
//    GetProjectDetailsById getProjectDetailsById (@Param("projectId") Integer projectId);

//    @Query("select pm from ProjectMgmt as pm where pm.projectId = :projectId")
//    ProjectMgmt getProjectDetailsByProjectId (@Param("projectId") Integer projectId);

    @Query("select pm from ProjectMgmt as pm where pm.projectId = :projectId")
    ProjectMgmt getProjectDetailsById (@Param("projectId") Integer projectId);

    @Query("select new com.forecastera.service.projectmanagement.dto.response.AddProject" +
            "(pm.projectId as projectId, " +
            "pv1.fieldValue as projectName) " +
            "from ProjectMgmt as pm " +
            "join ProjectMgmtFieldValue as pv1 on pv1.projectId = pm.projectId " +
            "join ProjectMgmtFieldMap as pf1 on pf1.fieldId = pv1.fieldId and pf1.fields = 'Project Name' " +
            "order by pm.projectId")
    List<AddProject> getProjectList ();

//    @Query(value = "select new com.forecastera.service.projectmanagement.dto.response.GetProjectAnalysis(pm.projectId,pm.projectName" +
//            ",rm1.firstName as owner,ps.status as status,pm.progress,pm.startDate,pm.endDate,pm.daysLeft,pt.type as type" +
//            ",pm.createdDate,pm.modifiedDate,concat(rm2.firstName,' ',rm2.lastName) as createdBy" +
//            ",concat(rm3.firstName,' ',rm3.lastName) as modifiedBy,pm.actualStartDate,pm.actualEndDate" +
//            ",pp.priority as priority,pm.budget" +
//            ",0 as forecast" +
//            ",'' as teams" +
//            ",pfn.fields" +
//            ",CASE WHEN pfn.fieldType = 'Picklist' then " +
//            "(select picklistValue from ProjectCustomPicklist where id=CAST(pfv.fieldValue AS integer) ) " +
//
////            " WHEN pfn.fieldType = 'Picklist-Multiselect' then " +
////            "(select string_agg(picklistValue,',') from ProjectCustomPicklist where id= any(string_to_array(pfv.fieldValue,',')::integer[])) " +
//
//            " ELSE pfv.fieldValue END as fieldValue, pfn.fieldType ) " +
//            " FROM ProjectMgmt as pm" +
//            " LEFT JOIN ProjectMgmtFieldValue as pfv on pm.projectId = pfv.projectId" +
//            " LEFT JOIN ProjectMgmtFieldMap as pfn on pfn.fieldId = pfv.fieldId" +
//            " LEFT JOIN ResourceMgmt as rm1 on rm1.resourceId = pm.owner" +
//            " LEFT JOIN ProjectMgmtStatus as ps on ps.statusId = pm.statusId" +
//            " LEFT JOIN ProjectMgmtType as pt on pt.typeId = pm.typeId" +
//            " LEFT JOIN ProjectMgmtPriority as pp on pp.priorityId = pm.priorityId" +
//            " LEFT JOIN ResourceMgmt as rm2 on rm2.resourceId = pm.createdBy" +
//            " LEFT JOIN ResourceMgmt as rm3 on rm3.resourceId = pm.modifiedBy" +
//            " order by pm.projectId")
//    public List<GetProjectAnalysis> getProjAnalyticsData();

//    @Query(value = "select new com.forecastera.service.projectmanagement.dto.response.GetProjectAnalysis(pm.projectId, " +
//            "pm.createdDate, pm.modifiedDate,concat(rm2.firstName,' ',rm2.lastName) as createdBy, " +
//            "concat(rm3.firstName,' ',rm3.lastName) as modifiedBy, pfn.fields, " +
//            "CASE " +
//            "WHEN pfn.fieldType = 'Picklist' and pfn.settingType=3 then " +
//            "(select picklistValue from ProjectCustomPicklist where id=CAST(pfv.fieldValue AS integer) ) " +
//
////            " WHEN pfn.fieldType = 'Picklist-Multiselect' and pfn.settingType=3 then " +
////            "(select string_agg(picklistValue,',') from ProjectCustomPicklist where id= any(string_to_array(pfv.fieldValue,',')::integer[])) " +
//
//            "WHEN pfn.fields = 'Status' then " +
//            "(select ps.status from ProjectMgmtStatus as ps where ps.statusId=CAST(pfv.fieldValue AS integer) ) " +
//
//            "WHEN pfn.fields = 'Priority' then " +
//            "(select pp.priority from ProjectMgmtPriority as pp where pp.priorityId=CAST(pfv.fieldValue AS integer) ) " +
//
//            "WHEN pfn.fields = 'Project Type' then " +
//            "(select pt.type from ProjectMgmtType as pt where pt.typeId=CAST(pfv.fieldValue AS integer) ) " +
//
//            "WHEN pfn.fields = 'Project Owner' then " +
//            "(select concat(rm.firstName,' ',rm.lastName) from ResourceMgmt as rm where rm.resourceId=CAST(pfv.fieldValue AS integer) ) " +
//
//            "ELSE pfv.fieldValue END as fieldValue, pfn.fieldType) " +
//            "FROM ProjectMgmt as pm " +
//            "LEFT JOIN ProjectMgmtFieldValue as pfv on pm.projectId = pfv.projectId " +
//            "LEFT JOIN ProjectMgmtFieldMap as pfn on pfn.fieldId = pfv.fieldId " +
//            "LEFT JOIN ResourceMgmt as rm2 on rm2.resourceId = pm.createdBy " +
//            "LEFT JOIN ResourceMgmt as rm3 on rm3.resourceId = pm.modifiedBy " +
//            "order by pm.projectId")
//    List<GetProjectAnalysis> getProjAnalyticsData();

    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.ProjectDetailsForNotification" +
            "(pm.projectId, pfv.fieldValue, pm.createdBy) " +
            "from ProjectMgmt as pm " +
            "join ProjectMgmtFieldValue as pfv on pfv.projectId = pm.projectId " +
            "join ProjectMgmtFieldMap as pfm on pfm.fieldId = pfv.fieldId " +
            "where pfm.fields = 'Project Owner' ")
    List<ProjectDetailsForNotification> getProjectDetailsForNotification();
}
