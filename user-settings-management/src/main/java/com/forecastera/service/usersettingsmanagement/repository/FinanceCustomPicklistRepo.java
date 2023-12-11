package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.FinanceCustomPicklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface FinanceCustomPicklistRepo extends JpaRepository<FinanceCustomPicklist, Integer> {

    @Query("select f from FinanceCustomPicklist f where f.picklistId= :picklist_id")
    List<FinanceCustomPicklist> getFinanceCustomPicklistByPicklistId (@RequestParam Integer picklist_id);
}
