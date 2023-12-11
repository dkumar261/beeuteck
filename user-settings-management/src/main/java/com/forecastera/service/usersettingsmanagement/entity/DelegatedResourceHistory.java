package com.forecastera.service.usersettingsmanagement.entity;

import com.forecastera.service.usersettingsmanagement.dto.request.PostDelegatedResourceHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/*
 * @Author Kanishk Vats
 * @Create 10-11-2023
 * @Description
 */
@Entity
@Table(name = "delegated_resource_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelegatedResourceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delegation_id")
    private Integer delegationId;

    @Column(name = "delegated_resource_id")
    private Integer delegatedResourceId;

    @Column(name = "delegated_to_resource_id")
    private Integer delegatedToResourceId;

    @Column(name = "delegation_start_date")
    private Date delegationStartDate;

    @Column(name = "delegation_end_date")
    private Date delegationEndDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "is_active")
    private Boolean isActive;

    public DelegatedResourceHistory(PostDelegatedResourceHistory postDelegatedResourceHistory) {
        this.delegationId = postDelegatedResourceHistory.getDelegationId();
        this.delegatedResourceId = postDelegatedResourceHistory.getDelegatedResourceId();
        this.delegatedToResourceId = postDelegatedResourceHistory.getDelegatedToResourceId();
        this.delegationStartDate = postDelegatedResourceHistory.getDelegationStartDate();
        this.delegationEndDate = postDelegatedResourceHistory.getDelegationEndDate();
        this.createdBy = postDelegatedResourceHistory.getCreatedBy();
        this.createdDate = postDelegatedResourceHistory.getCreatedDate();
        this.modifiedBy = postDelegatedResourceHistory.getModifiedBy();
        this.modifiedDate = postDelegatedResourceHistory.getModifiedDate();
        this.isActive = postDelegatedResourceHistory.getIsActive();
    }
}