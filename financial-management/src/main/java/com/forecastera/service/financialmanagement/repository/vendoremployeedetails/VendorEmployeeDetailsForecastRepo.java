package com.forecastera.service.financialmanagement.repository.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 26-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.vendoremployeedetails.VendorEmployeeDetailsForecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorEmployeeDetailsForecastRepo extends JpaRepository<VendorEmployeeDetailsForecast, Integer> {
}
