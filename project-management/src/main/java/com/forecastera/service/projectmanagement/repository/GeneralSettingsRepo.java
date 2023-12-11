package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 11-05-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.GeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsRepo extends JpaRepository<GeneralSettings, Integer> {

    @Query("select g.financialYearStart from GeneralSettings g where g.baseSettingId = 1")
    String getFinancialYearStartMonth();
}
