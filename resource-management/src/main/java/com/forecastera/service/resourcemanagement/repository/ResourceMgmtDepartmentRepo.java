package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create ResourceMgmtSkillsRepo
ResourceMgmtDepartmentRepo
 * @Description
 */


import com.forecastera.service.resourcemanagement.entity.ResourceMgmtDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMgmtDepartmentRepo extends JpaRepository<ResourceMgmtDepartment,Integer> {

    @Query("select rd.department from ResourceMgmtDepartment as rd where rd.departmentId = :departmentId")
    String getDepartmentById(@Param("departmentId") Integer departmentId);
}
