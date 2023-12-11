package com.forecastera.service.usersettingsmanagement.repository;/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.ProjectCustomPicklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProjectCustomPicklistRepo extends JpaRepository<ProjectCustomPicklist, Integer> {
    @Query("select p from ProjectCustomPicklist p where p.picklistId= :picklist_id")
    List<ProjectCustomPicklist> getProjectCustomPicklistByPicklistId (@RequestParam Integer picklist_id);
}
