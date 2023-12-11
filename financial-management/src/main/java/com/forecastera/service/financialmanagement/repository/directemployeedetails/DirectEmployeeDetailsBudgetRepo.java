package com.forecastera.service.financialmanagement.repository.directemployeedetails;

/*
 * @Author Uttam Kachhad
 * @Create 22-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.directemployeedetails.DirectEmployeeDetailsBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectEmployeeDetailsBudgetRepo extends JpaRepository<DirectEmployeeDetailsBudget, Integer> {
}
