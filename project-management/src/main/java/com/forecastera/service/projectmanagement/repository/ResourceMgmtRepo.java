package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */



import com.forecastera.service.projectmanagement.dto.utilityClass.ResourceIdDepartmentHead;
import com.forecastera.service.projectmanagement.dto.utilityClass.ResourceIdEmail;
import com.forecastera.service.projectmanagement.entity.ResourceMgmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMgmtRepo extends JpaRepository<ResourceMgmt, Integer> {

    // ask here, how to find among these values
   /* @Query(value = "select * from resource_master r where r.resource_id!=0 and string_to_array(role,',') && array['1']", nativeQuery = true)
    List<ResourceMgmt> getProjectOwners();*/

    @Query("select concat(rm.firstName,' ',rm.lastName) from ResourceMgmt as rm where rm.resourceId = :resourceId")
    String getResourceNameById(@Param("resourceId") Integer resourceId);

//    @Query("SELECT rm.firstName, rm.lastName FROM ResourceMgmt rm WHERE rm.role = :roleId")
//    List<Object[]> findByRoleId(@Param("roleId") String roleId);

//    @Query("SELECT rm.firstName, rm.lastName FROM ResourceMgmt rm WHERE CONCAT(',', rm.role, ',') LIKE CONCAT('%', :roleId, '%')")
//    List<Object[]> findByRoleId(@Param("roleId") String roleId);
//
//    @Query("SELECT rm.firstName, rm.lastName FROM ResourceMgmt rm WHERE CONCAT(',', rm.skills, ',') LIKE CONCAT('%', :skillId, '%')")
//    List<Object[]> findBySkillId(@Param("skillId") String skillId);

//    @Query("select rm " +
//            "from ResourceMgmt as rm " +
//            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
//            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
//            "where rfm.fields = 'Email' and rfv.fieldValue = :email ")
//    List<ResourceMgmt> getResourceByEmail(@Param("email") String email);

//    @Query("select rm " +
//            "from ResourceMgmt as rm " +
//            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
//            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
//            "where rfm.fields = 'Email' and rfv.fieldValue = :email ")
//    String getResourceDepartmentId(@Param("resourceId") Integer resourceId);


    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.ResourceIdDepartmentHead" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName) as resourceName, dm.departmentId as departmentId, " +
            "dm.department as departmentName, " +
            "coalesce(dm1.departmentId, null) as managedDepartmentId, coalesce(dm1.department, null) as managedDepartmentName) " +

            "from ResourceMgmt as rm " +

            "join ResourceMgmtFieldValue as rfv1 on rm.resourceId = rfv1.resourceId " +
            "join ResourceMgmtFieldMap as rfm1 on rfm1.fieldId = rfv1.fieldId " +

            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "join ResourceMgmtDepartment as dm on cast(dm.departmentId as text) = rfv.fieldValue " +

            "left join ResourceMgmtDepartment as dm1 on rm.resourceId = dm1.resourceId " +

            "where rfm.fields = 'Department' and rfm1.fields = 'Email' and rfv1.fieldValue = :email ")
    List<ResourceIdDepartmentHead> getAllDepartmentsUnderResource(@Param("email") String email);

    @Query("select rm " +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Email' and rfv.fieldValue = :email ")
    List<ResourceMgmt> getResourceByEmail(@Param("email") String email);

    @Query("select new com.forecastera.service.projectmanagement.dto.utilityClass.ResourceIdEmail" +
            "(rm.resourceId, concat(rm.firstName, ' ', rm.lastName) as resourceName, rfv.fieldValue)" +
            "from ResourceMgmt as rm " +
            "join ResourceMgmtFieldValue as rfv on rm.resourceId = rfv.resourceId " +
            "join ResourceMgmtFieldMap as rfm on rfm.fieldId = rfv.fieldId " +
            "where rfm.fields = 'Email'")
    List<ResourceIdEmail> getAllResourceIdEmail();
}
