package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.ProjectMgmtStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMgmtStatusRepo extends JpaRepository<ProjectMgmtStatus,Integer> {
    @Query("select ps.status from ProjectMgmtStatus as ps where ps.statusId = :statusId")
    String getStatusById(@Param("statusId") Integer statusId);
}
