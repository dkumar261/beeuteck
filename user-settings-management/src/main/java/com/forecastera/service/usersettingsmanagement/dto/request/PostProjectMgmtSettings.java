package com.forecastera.service.usersettingsmanagement.dto.request;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.PicklistFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostProjectMgmtSettings {

    @JsonProperty("field_id")
    private Integer fieldId;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("visibility")
    private List<String> visibility;

    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    @JsonProperty("edit_access")
    private List<String> editAccess;

    @JsonProperty("proj_details_view")
    private Boolean projDetailsView;

    @JsonProperty("proj_analysis_view")
    private Boolean projAnalysisView;

    @JsonProperty("proj_creation_view")
    private Boolean projCreationView;

    @JsonProperty("field_type")
    private String fieldType;

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

    @JsonProperty("isprojectdetailsvisible")
    private Boolean isProjectDetailsVisible;

    @JsonProperty("isprojectdetailsfreeze")
    private Boolean isProjectDetailsFreeze;

    @JsonProperty("isprojectanalysisvisible")
    private Boolean isProjectAnalysisVisible;

    @JsonProperty("isprojectanalysisfreeze")
    private Boolean isProjectAnalysisFreeze;

    @JsonProperty("isprojectcreationvisible")
    private Boolean isProjectCreationVisible;

    @JsonProperty("isprojectcreationfreeze")
    private Boolean isProjectCreationFreeze;

    @JsonProperty("setting_type")
    private Integer settingType;


}
