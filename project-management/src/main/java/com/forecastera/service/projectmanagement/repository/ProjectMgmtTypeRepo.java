package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.ProjectMgmtType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMgmtTypeRepo extends JpaRepository<ProjectMgmtType,Integer> {
    @Query("select pt.type from ProjectMgmtType as pt where pt.typeId = :typeId")
    String getTypeById(@Param("typeId") Integer typeId);
}
