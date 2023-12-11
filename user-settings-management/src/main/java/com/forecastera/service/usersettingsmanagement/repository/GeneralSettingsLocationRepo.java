package com.forecastera.service.usersettingsmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 08-06-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.GeneralSettingsLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsLocationRepo extends JpaRepository<GeneralSettingsLocation, Integer> {

}
