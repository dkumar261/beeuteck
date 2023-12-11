package com.forecastera.service.usersettingsmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.PicklistFormat;
import com.forecastera.service.usersettingsmanagement.entity.ResourceMgmtFieldMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetResourceMgmtSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("visibility")
    private List<String> visibility;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("edit_access")
    private List<String> editAccess;

    @JsonProperty("resources_card_view")
    private Boolean resourcesCardView;

    @JsonProperty("resource_analytics_view")
    private Boolean resourceAnalyticsView;

    @JsonProperty("resource_creation_view")
    private Boolean resourceCreationView;

    @JsonProperty("resource_type")
    private String resourceType;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private Date createdDate;

    @JsonProperty("modified_by")
    private String modifiedBy;

    @JsonProperty("modified_date")
    private Date modifiedDate;

    @JsonProperty("adminPicklistSettingsList")
    private List<PicklistFormat> fieldPicklistValues;

    @JsonProperty("isrange")
    private Boolean isRange;

    @JsonProperty("isaddmore")
    private Boolean isAddMore;

    @JsonProperty("ispriority")
    private Boolean isPriority;

    @JsonProperty("isenabledvisible")
    private Boolean isEnabledVisible;

    @JsonProperty("isenabledfreeze")
    private Boolean isEnabledFreeze;

    @JsonProperty("isvisibilityddl")
    private Boolean isVisibilityDdl;

    @JsonProperty("iseditaccessddl")
    private Boolean isEditAccessDdl;

    @JsonProperty("isresourcecardvisible")
    private Boolean isResourceCardVisible;

    @JsonProperty("isresourcecardfreeze")
    private Boolean isResourceCardFreeze;

    @JsonProperty("isresourceanalysisvisible")
    private Boolean isResourceAnalysisVisible;

    @JsonProperty("isresourceanalysisfreeze")
    private Boolean isResourceAnalysisFreeze;

    @JsonProperty("isresourcecreationvisible")
    private Boolean isResourceCreationVisible;

    @JsonProperty("isresourcecreationfreeze")
    private Boolean isResourceCreationFreeze;

    @JsonProperty("setting_type")
    private Integer settingType;

    public GetResourceMgmtSettings(ResourceMgmtFieldMap resourceMgmtFieldMap){
        this.fieldId = resourceMgmtFieldMap.getFieldId();
        this.fields = resourceMgmtFieldMap.getFields();

        if(StringUtils.isNotBlank(resourceMgmtFieldMap.getVisibility())){
            this.visibility = Arrays.asList(resourceMgmtFieldMap.getVisibility().split(","));
        }
        else{
            this.visibility = new ArrayList<>();
        }

        this.enabled = resourceMgmtFieldMap.getEnabled();

        if(StringUtils.isNotBlank(resourceMgmtFieldMap.getEditAccess())){
            this.editAccess = Arrays.asList(resourceMgmtFieldMap.getEditAccess().split(","));
        }
        else{
            this.editAccess = new ArrayList<>();
        }

        this.resourcesCardView = resourceMgmtFieldMap.getResourcesCardView();
        this.resourceAnalyticsView = resourceMgmtFieldMap.getResourceAnalyticsView();
        this.resourceCreationView = resourceMgmtFieldMap.getResourceCreationView();
        this.resourceType = resourceMgmtFieldMap.getResourceType();
        this.createdBy = resourceMgmtFieldMap.getCreatedBy();
        this.createdDate = resourceMgmtFieldMap.getCreatedDate();
        this.modifiedBy = resourceMgmtFieldMap.getModifiedBy();
        this.modifiedDate = resourceMgmtFieldMap.getModifiedDate();
        this.isRange = resourceMgmtFieldMap.getIsRange();
        this.isAddMore = resourceMgmtFieldMap.getIsAddMore();
        this.isPriority = resourceMgmtFieldMap.getIsPriority();
        this.isEnabledVisible = resourceMgmtFieldMap.getIsEnabledVisible();
        this.isEnabledFreeze = resourceMgmtFieldMap.getIsEnabledFreeze();
        this.isVisibilityDdl = resourceMgmtFieldMap.getIsVisibilityDdl();
        this.isEditAccessDdl = resourceMgmtFieldMap.getIsEditAccessDdl();
        this.isResourceCardVisible = resourceMgmtFieldMap.getIsResourceCardVisible();
        this.isResourceCardFreeze = resourceMgmtFieldMap.getIsResourceCardFreeze();
        this.isResourceAnalysisVisible = resourceMgmtFieldMap.getIsResourceAnalysisVisible();
        this.isResourceAnalysisFreeze = resourceMgmtFieldMap.getIsResourceAnalysisFreeze();
        this.isResourceCreationVisible = resourceMgmtFieldMap.getIsResourceCreationVisible();
        this.isResourceCreationFreeze = resourceMgmtFieldMap.getIsResourceCreationFreeze();
        this.settingType = resourceMgmtFieldMap.getSettingType();
    }
}
