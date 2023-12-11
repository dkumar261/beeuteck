package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Sowjanya Aare
 * @Create 20-10-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.entity.DelegatedDepartmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegatedDepartmentHistoryRepo extends JpaRepository<DelegatedDepartmentHistory, Integer> {



}
