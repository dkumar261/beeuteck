package com.forecastera.service.projectmanagement.repository;/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.ProjectCustomPicklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectCustomPicklistRepo extends JpaRepository<ProjectCustomPicklist, Integer> {

//    @Query("select p from ProjectCustomPicklist p where p.picklistId= :picklist_id")
//    List<ProjectCustomPicklist> getProjectCustomPicklistByPicklistId (@RequestParam Integer picklist_id);

    @Query(value = "select string_agg(p.picklist_value,',') from proj_custom_pick_list p where p.id in :picklist_id", nativeQuery = true)
    String getProjectCustomPicklistByPicklistId (@RequestParam("picklist_id") List<Integer> picklist_id);

}
