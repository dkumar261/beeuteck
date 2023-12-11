package com.forecastera.service.financialmanagement.repository.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 04-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.VendorEmploymentDetailsForecastExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorEmploymentDetailsForecastExpenseRepo extends JpaRepository<VendorEmploymentDetailsForecastExpense, Integer> {

    List<VendorEmploymentDetailsForecastExpense> findAllByVendorDetailsForecastId(Integer vendor_employment_details_forecast_id);
}
