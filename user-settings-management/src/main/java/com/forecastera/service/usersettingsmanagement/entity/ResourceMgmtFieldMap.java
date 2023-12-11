package com.forecastera.service.usersettingsmanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.request.PostResourceMgmtSettings;
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
    private Boolean enabled;

    @Column(name = "edit_access")
    private String editAccess;

    @Column(name = "resources_card_view")
    private Boolean resourcesCardView;

    @Column(name = "resource_analytics_view")
    private Boolean resourceAnalyticsView;

    @Column(name = "resource_creation_view")
    private Boolean resourceCreationView;

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
    private Boolean isRange;

    @Column(name = "isaddmore")
    private Boolean isAddMore;

    @Column(name = "ispriority")
    private Boolean isPriority;

    @Column(name = "isenabledvisible")
    private Boolean isEnabledVisible;

    @Column(name = "isenabledfreeze")
    private Boolean isEnabledFreeze;

    @Column(name = "isvisibilityddl")
    private Boolean isVisibilityDdl;

    @Column(name = "iseditaccessddl")
    private Boolean isEditAccessDdl;

    @Column(name = "isresourcecardvisible")
    private Boolean isResourceCardVisible;

    @Column(name = "isresourcecardfreeze")
    private Boolean isResourceCardFreeze;

    @Column(name = "isresourceanalysisvisible")
    private Boolean isResourceAnalysisVisible;

    @Column(name = "isresourceanalysisfreeze")
    private Boolean isResourceAnalysisFreeze;

    @Column(name = "isresourcecreationvisible")
    private Boolean isResourceCreationVisible;

    @Column(name = "isresourcecreationfreeze")
    private Boolean isResourceCreationFreeze;

    @Column(name = "setting_type")
    private Integer settingType;

    public ResourceMgmtFieldMap(PostResourceMgmtSettings postResourceMgmtSettings){
        this.fieldId = postResourceMgmtSettings.getFieldId();
        this.fields = postResourceMgmtSettings.getFields();

        if (postResourceMgmtSettings.getVisibility() != null && !postResourceMgmtSettings.getVisibility().isEmpty()) {
            this.visibility = String.join(",", postResourceMgmtSettings.getVisibility());
        }
        else{
            this.visibility = null;
        }

        this.enabled = postResourceMgmtSettings.getEnabled();

        if (postResourceMgmtSettings.getEditAccess() != null && !postResourceMgmtSettings.getEditAccess().isEmpty()) {
            this.editAccess = String.join(",", postResourceMgmtSettings.getEditAccess());
        }
        else{
            this.editAccess = null;
        }

        this.resourcesCardView = postResourceMgmtSettings.getResourcesCardView();
        this.resourceAnalyticsView = postResourceMgmtSettings.getResourceAnalyticsView();
        this.resourceCreationView = postResourceMgmtSettings.getResourceCreationView();
        this.resourceType = postResourceMgmtSettings.getResourceType();
        this.createdBy = postResourceMgmtSettings.getCreatedBy();
        this.createdDate = postResourceMgmtSettings.getCreatedDate();
        this.modifiedBy = postResourceMgmtSettings.getModifiedBy();
        this.modifiedDate = postResourceMgmtSettings.getModifiedDate();
        this.isRange = postResourceMgmtSettings.getIsRange();
        this.isAddMore = postResourceMgmtSettings.getIsAddMore();
        this.isPriority = postResourceMgmtSettings.getIsPriority();
        this.isEnabledVisible = postResourceMgmtSettings.getIsEnabledVisible();
        this.isEnabledFreeze = postResourceMgmtSettings.getIsEnabledFreeze();
        this.isVisibilityDdl = postResourceMgmtSettings.getIsVisibilityDdl();
        this.isEditAccessDdl = postResourceMgmtSettings.getIsEditAccessDdl();
        this.isResourceCardVisible = postResourceMgmtSettings.getIsResourceCardVisible();
        this.isResourceCardFreeze = postResourceMgmtSettings.getIsResourceCardFreeze();
        this.isResourceAnalysisVisible = postResourceMgmtSettings.getIsResourceAnalysisVisible();
        this.isResourceAnalysisFreeze = postResourceMgmtSettings.getIsResourceAnalysisFreeze();
        this.isResourceCreationVisible = postResourceMgmtSettings.getIsResourceCreationVisible();
        this.isResourceCreationFreeze = postResourceMgmtSettings.getIsResourceCreationFreeze();
        this.settingType = postResourceMgmtSettings.getSettingType();
    }
}

