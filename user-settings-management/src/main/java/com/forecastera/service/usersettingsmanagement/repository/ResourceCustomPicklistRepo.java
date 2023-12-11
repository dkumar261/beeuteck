package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.ResourceCustomPicklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ResourceCustomPicklistRepo extends JpaRepository<ResourceCustomPicklist,Integer> {
    @Query("select r from ResourceCustomPicklist r where r.picklistId= :picklist_id")
    List<ResourceCustomPicklist> getResourceCustomPicklistByPicklistId (@RequestParam Integer picklist_id);

}
