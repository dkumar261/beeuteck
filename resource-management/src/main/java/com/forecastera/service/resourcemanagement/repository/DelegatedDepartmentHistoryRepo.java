package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Sowjanya Aare
 * @Create 20-10-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.DelegatedDepartmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegatedDepartmentHistoryRepo extends JpaRepository<DelegatedDepartmentHistory, Integer> {



}
