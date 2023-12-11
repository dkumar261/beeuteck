package com.forecastera.service.usersettingsmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.PicklistFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResourceMgmtSettings {

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
}
