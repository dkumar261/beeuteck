package com.forecastera.service.financialmanagement.repository.otherdirectinhousecost;

import com.forecastera.service.financialmanagement.entity.otherdirectinhousecost.OtherDirectInHouseCostBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * @Author Uttam Kachhad
 * @Create 03-07-2023
 * @Description
 */
@Repository
public interface OtherDirectInHouseCostBudgetRepo extends JpaRepository<OtherDirectInHouseCostBudget, Integer> {
}
