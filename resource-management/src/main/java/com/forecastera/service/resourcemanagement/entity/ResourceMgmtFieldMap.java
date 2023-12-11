package com.forecastera.service.resourcemanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 12-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resource_mgmt_field_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtFieldMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Integer fieldId;

    @Column(name = "fields")
    private String fields;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "enabled")
    private String enabled;

    @Column(name = "edit_access")
    private String editAccess;

    @Column(name = "resources_card_view")
    private String resourcesCardView;

    @Column(name = "resource_analytics_view")
    private String resourceAnalyticsView;

    @Column(name = "resource_creation_view")
    private String resourceCreationView;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "isrange")
    private String isRange;

    @Column(name = "isaddmore")
    private String isAddMore;

    @Column(name = "ispriority")
    private String isPriority;

    @Column(name = "isenabledvisible")
    private String isEnabledVisible;

    @Column(name = "isenabledfreeze")
    private String isEnabledFreeze;

    @Column(name = "isvisibilityddl")
    private String isVisibilityDdl;

    @Column(name = "iseditaccessddl")
    private String isEditAccessDdl;

    @Column(name = "isresourcecardvisible")
    private String isResourceCardVisible;

    @Column(name = "isresourcecardfreeze")
    private String isResourceCardFreeze;

    @Column(name = "isresourceanalysisvisible")
    private String isResourceAnalysisVisible;

    @Column(name = "isresourceanalysisfreeze")
    private String isResourceAnalysisFreeze;

    @Column(name = "isresourcecreationvisible")
    private String isResourceCreationVisible;

    @Column(name = "isresourcecreationfreeze")
    private String isResourceCreationFreeze;

    @Column(name = "setting_type")
    private Integer settingType;

}

