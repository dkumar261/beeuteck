package com.forecastera.service.financialmanagement.repository.directemployeedetails;

import com.forecastera.service.financialmanagement.entity.directemployeedetails.FinanceDirectEmployeeBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * @Author Uttam Kachhad
 * @Create 26-06-2023
 * @Description
 */
@Repository
public interface FinanceDirectEmployeeBudgetRepo extends JpaRepository<FinanceDirectEmployeeBudget, Integer> {

    Optional<FinanceDirectEmployeeBudget> findByDirectEmployeeBudgetIdAndFieldId(Integer directEmployeeBudgetId, Integer fieldId);
}
