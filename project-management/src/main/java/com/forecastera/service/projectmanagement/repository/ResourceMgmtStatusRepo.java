package com.forecastera.service.projectmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 18-07-2023
 * @Description
 */


import com.forecastera.service.projectmanagement.entity.ResourceMgmtStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceMgmtStatusRepo extends JpaRepository<ResourceMgmtStatus, Integer> {

}
