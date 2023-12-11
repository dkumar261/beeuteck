package com.forecastera.service.financialmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 21-07-2023
 * @Description
 */


import com.forecastera.service.financialmanagement.entity.GeneralSettingsLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsLocationRepo extends JpaRepository<GeneralSettingsLocation, Integer> {

}
