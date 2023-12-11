package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 10-11-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.response.GetDelegatedResourceHistory;
import com.forecastera.service.usersettingsmanagement.entity.DelegatedResourceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegatedResourceHistoryRepo extends JpaRepository<DelegatedResourceHistory, Integer> {

    @Query("select new com.forecastera.service.usersettingsmanagement.dto.response.GetDelegatedResourceHistory" +
            "(drh.delegationId, drh.delegatedResourceId, concat(rm.firstName, ' ', rm.lastName), dm.department, " +
            "drh.delegatedToResourceId, concat(rm1.firstName, ' ', rm1.lastName), dm1.department, " +
            "drh.delegationStartDate, drh.delegationEndDate, " +
            "drh.createdBy, drh.createdDate, drh.modifiedBy, drh.modifiedDate, drh.isActive) " +

            "from DelegatedResourceHistory as drh " +

            "join ResourceMgmt as rm on rm.resourceId = drh.delegatedResourceId " +
            "join ResourceMgmtFieldValue as rfv on rfv.resourceId = rm.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "join ResourceMgmtDepartment as dm on cast(dm.departmentId as text) = rfv.fieldValue " +

            "join ResourceMgmt as rm1 on rm1.resourceId = drh.delegatedToResourceId " +
            "join ResourceMgmtFieldValue as rfv1 on rfv1.resourceId = rm1.resourceId " +
            "join ResourceMgmtFieldMap as rfm1 on rfm1.fieldId = rfv1.fieldId " +
            "join ResourceMgmtDepartment as dm1 on cast(dm1.departmentId as text) = rfv1.fieldValue " +

            "where rfm.fields = 'Department' and rfm1.fields = 'Department' and drh.isActive = '1' ")
    List<GetDelegatedResourceHistory> getDelegatedResourceHistory();

   @Query("select drh from DelegatedResourceHistory as drh where delegatedResourceId=:resourceId and delegatedToResourceId=:delegatedToResourceId")
   List<DelegatedResourceHistory> getDelegatedHistoryByResourceIdAndDelegatedToResourceId(Integer resourceId, Integer delegatedToResourceId);
}

