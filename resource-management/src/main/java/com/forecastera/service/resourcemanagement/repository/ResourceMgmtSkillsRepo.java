package com.forecastera.service.resourcemanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create ResourceMgmtSkillsRepo
ResourceMgmtDepartmentRepo
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.ResourceMgmtSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMgmtSkillsRepo extends JpaRepository<ResourceMgmtSkills,Integer> {

    @Query(value = "select string_agg(rs.skill,',') from skill_master as rs where rs.skill_id in :skillIds", nativeQuery = true)
    String getSkillsByIds(@Param("skillIds") List<Integer> skillIds);
}
