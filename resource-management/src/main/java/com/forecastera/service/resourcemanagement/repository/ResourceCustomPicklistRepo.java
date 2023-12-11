package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.ResourceCustomPicklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ResourceCustomPicklistRepo extends JpaRepository<ResourceCustomPicklist, Integer> {

//    @Query("select r from ResourceCustomPicklist r where r.picklistId= :picklist_id")
//    List<ResourceCustomPicklist> getResourceCustomPicklistByPicklistId (@RequestParam Integer picklist_id);

    @Query(value = "select string_agg(r.picklist_value,',') from resource_custom_pick_list r where r.id in :picklist_id", nativeQuery = true)
    String getResourceCustomPicklistByPicklistId (@RequestParam("picklist_id") List<Integer> picklist_id);
}
