package com.forecastera.service.resourcemanagement.repository;

/*
 * @Author Kanishk Vats
 * @Create 10-11-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.entity.DelegatedResourceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DelegatedResourceHistoryRepo extends JpaRepository<DelegatedResourceHistory, Integer> {

    @Query("select drh from DelegatedResourceHistory drh " +
            "where drh.isActive = '1' " +
            "and (drh.delegationStartDate<= :startDate or drh.delegationEndDate>= :endDate) " +
            "and (drh.delegatedResourceId = :resourceId or drh.delegatedToResourceId = :resourceId)")
    List<DelegatedResourceHistory> getAllResourceActiveDelegations(@Param("resourceId") Integer resourceId,
                                                                   @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select drh from DelegatedResourceHistory drh " +
            "where drh.isActive = '1' and drh.delegationStartDate<= :startDate " +
            "and (drh.delegatedResourceId = :resourceId or drh.delegatedToResourceId = :resourceId)")
    List<DelegatedResourceHistory> getAllResourceActiveDelegations(@Param("resourceId") Integer resourceId, @Param("startDate") Date startDate);
}
