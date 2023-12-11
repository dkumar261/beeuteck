package com.forecastera.service.financialmanagement.repository.capexrelatedexpense;
/*
 * @Author Kanishk Vats
 * @Create 05-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.capexrelatedexpense.CapexRelatedExpenseBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapexRelatedExpenseBudgetRepo extends JpaRepository<CapexRelatedExpenseBudget, Integer> {
}
