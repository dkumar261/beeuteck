package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 04-08-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.ProjectResourceMappingExtended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectResourceMappingExtendedRepo extends JpaRepository<ProjectResourceMappingExtended, Integer> {

    List<ProjectResourceMappingExtended> findByMapId(Integer mapId);
}
