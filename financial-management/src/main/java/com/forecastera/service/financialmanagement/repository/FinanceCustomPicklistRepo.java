package com.forecastera.service.financialmanagement.repository;
/*
 * @Author Kanishk Vats
 * @Create 21-06-2023
 * @Description
 */

import com.forecastera.service.financialmanagement.entity.FinanceCustomPicklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceCustomPicklistRepo extends JpaRepository<FinanceCustomPicklist, Integer> {

}
