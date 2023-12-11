package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.ProjectMgmtPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMgmtPriorityRepo extends JpaRepository<ProjectMgmtPriority,Integer> {
    @Query("select pp.priority from ProjectMgmtPriority as pp where pp.priorityId = :priorityId")
    String getPriorityById(@Param("priorityId") Integer priorityId);
}
