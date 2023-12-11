package com.forecastera.service.usersettingsmanagement.repository;/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.entity.GeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralSettingsRepo extends JpaRepository<GeneralSettings, Integer> {

}
