package com.forecastera.service.usersettingsmanagement.dto.response;
/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forecastera.service.usersettingsmanagement.dto.utilityClass.PicklistFormat;
import com.forecastera.service.usersettingsmanagement.entity.ProjectMgmtFieldMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetProjectMgmtSettings {

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


    public GetProjectMgmtSettings(ProjectMgmtFieldMap projectMgmtFieldMap){
        this.fieldId = projectMgmtFieldMap.getFieldId();
        this.fields = projectMgmtFieldMap.getFields();

        if(StringUtils.isNotBlank(projectMgmtFieldMap.getVisibility())){
            this.visibility = Arrays.asList(projectMgmtFieldMap.getVisibility().split(","));
        }
        else{
            this.visibility = new ArrayList<>();
        }

        this.isEnabled = projectMgmtFieldMap.getIsEnabled();

        if(StringUtils.isNotBlank(projectMgmtFieldMap.getEditAccess())){
            this.editAccess = Arrays.asList(projectMgmtFieldMap.getEditAccess().split(","));
        }
        else{
            this.editAccess = new ArrayList<>();
        }

        this.projDetailsView = projectMgmtFieldMap.getProjDetailsView();
        this.projAnalysisView = projectMgmtFieldMap.getProjAnalysisView();
        this.projCreationView = projectMgmtFieldMap.getProjCreationView();
        this.fieldType = projectMgmtFieldMap.getFieldType();
        this.createdBy = projectMgmtFieldMap.getCreatedBy();
        this.createdDate = projectMgmtFieldMap.getCreatedDate();
        this.modifiedBy = projectMgmtFieldMap.getModifiedBy();
        this.modifiedDate = projectMgmtFieldMap.getModifiedDate();
        this.isRange = projectMgmtFieldMap.getIsRange();
        this.isAddMore = projectMgmtFieldMap.getIsAddMore();
        this.isPriority = projectMgmtFieldMap.getIsPriority();
        this.isEnabledVisible = projectMgmtFieldMap.getIsEnabledVisible();
        this.isEnabledFreeze = projectMgmtFieldMap.getIsEnabledFreeze();
        this.isVisibilityDdl = projectMgmtFieldMap.getIsVisibilityDdl();
        this.isEditAccessDdl = projectMgmtFieldMap.getIsEditAccessDdl();
        this.isProjectDetailsVisible = projectMgmtFieldMap.getIsProjectDetailsVisible();
        this.isProjectDetailsFreeze = projectMgmtFieldMap.getIsProjectDetailsFreeze();
        this.isProjectAnalysisVisible = projectMgmtFieldMap.getIsProjectAnalysisVisible();
        this.isProjectAnalysisFreeze = projectMgmtFieldMap.getIsProjectAnalysisFreeze();
        this.isProjectCreationVisible = projectMgmtFieldMap.getIsProjectCreationVisible();
        this.isProjectCreationFreeze = projectMgmtFieldMap.getIsProjectCreationFreeze();
        this.settingType = projectMgmtFieldMap.getSettingType();
    }
}
