package com.forecastera.service.projectmanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 10-11-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.entity.DelegatedResourceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegatedResourceHistoryRepo extends JpaRepository<DelegatedResourceHistory, Integer> {

}
