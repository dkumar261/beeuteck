package com.forecastera.service.financialmanagement.repository.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 26-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.VendorEmployeeDetailsBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorEmployeeDetailsBudgetRepo extends JpaRepository<VendorEmployeeDetailsBudget, Integer> {
}
