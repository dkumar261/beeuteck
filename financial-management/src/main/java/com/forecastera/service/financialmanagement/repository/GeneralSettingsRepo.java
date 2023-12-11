package com.forecastera.service.financialmanagement.repository;/*
 * @Author Kanishk Vats
 * @Create 11-07-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.GeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsRepo extends JpaRepository<GeneralSettings, Integer> {

    @Query("select g.financialYearStart from GeneralSettings g where g.baseSettingId = 1")
    String getFinancialYearStartMonth();
}
