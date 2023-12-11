package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.entity.ResourceMgmtDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceMgmtDepartmentRepo extends JpaRepository<ResourceMgmtDepartment,Integer> {

    @Query("select rd.department from ResourceMgmtDepartment as rd where rd.departmentId = :departmentId")
    String getDepartmentById(@Param("departmentId") Integer departmentId);
}
