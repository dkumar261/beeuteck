package com.forecastera.service.financialmanagement.repository.vendorrelatedcost;

import com.forecastera.service.financialmanagement.entity.vendorrelatedcost.VendorRelatedCostBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * @Author Uttam Kachhad
 * @Create 05-07-2023
 * @Description
 */
@Repository
public interface VendorRelatedCostBudgetRepo extends JpaRepository<VendorRelatedCostBudget, Integer> {
}
