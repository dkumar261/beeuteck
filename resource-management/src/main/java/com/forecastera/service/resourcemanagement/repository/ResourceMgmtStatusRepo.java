package com.forecastera.service.resourcemanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 20-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.ResourceMgmtStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceMgmtStatusRepo extends JpaRepository<ResourceMgmtStatus, Integer> {

}
