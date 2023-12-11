package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 19-07-2023
 * @Description
 */


import com.forecastera.service.resourcemanagement.entity.GeneralSettingsLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsLocationRepo extends JpaRepository<GeneralSettingsLocation, Integer> {

    @Query("select gsl.location from GeneralSettingsLocation as gsl where gsl.locationId = :locationId")
    String getLocationById(@Param("locationId") Integer locationId);
}
