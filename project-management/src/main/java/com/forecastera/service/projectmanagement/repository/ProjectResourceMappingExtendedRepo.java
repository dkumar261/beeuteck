package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 07-08-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.entity.ProjectResourceMappingExtended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectResourceMappingExtendedRepo extends JpaRepository<ProjectResourceMappingExtended, Integer> {

    List<ProjectResourceMappingExtended> findByMapId(Integer mapId);
}
