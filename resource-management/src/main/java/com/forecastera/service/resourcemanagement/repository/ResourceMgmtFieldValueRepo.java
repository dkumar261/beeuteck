package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.dto.response.GetResourceDetailsById;
import com.forecastera.service.resourcemanagement.entity.ProjectResourceMapping;
import com.forecastera.service.resourcemanagement.entity.ResourceMgmtFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceMgmtFieldValueRepo extends JpaRepository<ResourceMgmtFieldValue,Integer> {

    @Query("select rcv from ResourceMgmtFieldValue as rcv where rcv.resourceId = :resourceId")
    List<ResourceMgmtFieldValue> getResourceDetailsForUpdateById(@Param("resourceId") Integer resourceId);

//    @Query("select new com.forecastera.service.resourcemanagement.dto.response.GetResourceDetailsById(" +
//            "rfv.fieldId, rfm.fields, rfv.fieldValue, rfm.resourceType, rfm.settingType) " +
//            "from ResourceMgmtFieldValue as rfv " +
//            "join ResourceMgmtFieldMap as rfm on rfv.fieldId = rfm.fieldId " +
//            "where rfv.resourceId = :resourceId and rfm.enabled = true and rfm.resourcesCardView = true")
//    List<GetResourceDetailsById> getResourceDetailsById (@Param("resourceId") Integer resourceId);

    @Query("select case " +
                "when count(rfv) > 0 then true else false " +
            "end " +
            " from ResourceMgmtFieldValue as rfv where rfv.fieldId = :fieldId and rfv.fieldValue = :fieldValue and rfv.resourceId!=:resourceId")
    Boolean checkIfEmailIdExists(@Param("resourceId") Integer resourceId, @Param("fieldId") Integer fieldId, @Param("fieldValue") String fieldValue);

    @Query("select rfv from ResourceMgmtFieldValue as rfv where rfv.resourceId = :resourceId")
    List<ResourceMgmtFieldValue> getAllResourceDetailsByResourceId(@Param("resourceId") Integer resourceId);

    @Query("select rfv from ResourceMgmtFieldValue as rfv where rfv.fieldId = :fieldId and rfv.fieldValue = :resourceId")
    List<ResourceMgmtFieldValue> getAllReportingResourcesByResourceId(@Param("fieldId") Integer fieldId, @Param("resourceId") String resourceId);
}
