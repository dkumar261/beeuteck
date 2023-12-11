package com.forecastera.service.usersettingsmanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 29-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.request.PostProjectMgmtSettings;
import lombok.*;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "project_mgmt_field_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMgmtFieldMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Integer fieldId;

    @Column(name = "fields")
    private String fields;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "edit_access")
    private String editAccess;

    @Column(name = "proj_details_view")
    private Boolean projDetailsView;

    @Column(name = "proj_analysis_view")
    private Boolean projAnalysisView;

    @Column(name = "proj_creation_view")
    private Boolean projCreationView;

    @Column(name = "field_type")
    private String fieldType;

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

    @Column(name = "isprojectdetailsvisible")
    private Boolean isProjectDetailsVisible;

    @Column(name = "isprojectdetailsfreeze")
    private Boolean isProjectDetailsFreeze;

    @Column(name = "isprojectanalysisvisible")
    private Boolean isProjectAnalysisVisible;

    @Column(name = "isprojectanalysisfreeze")
    private Boolean isProjectAnalysisFreeze;

    @Column(name = "isprojectcreationvisible")
    private Boolean isProjectCreationVisible;

    @Column(name = "isprojectcreationfreeze")
    private Boolean isProjectCreationFreeze;

    @Column(name = "setting_type")
    private Integer settingType;

    public ProjectMgmtFieldMap(PostProjectMgmtSettings postProjectMgmtSettings){
        this.fieldId = postProjectMgmtSettings.getFieldId();
        this.fields = postProjectMgmtSettings.getFields();

        if (postProjectMgmtSettings.getVisibility() != null && !postProjectMgmtSettings.getVisibility().isEmpty()) {
            this.visibility = String.join(",", postProjectMgmtSettings.getVisibility());
        }
        else{
            this.visibility = null;
        }

        this.isEnabled = postProjectMgmtSettings.getIsEnabled();

        if (postProjectMgmtSettings.getEditAccess() != null && !postProjectMgmtSettings.getEditAccess().isEmpty()) {
            this.editAccess = String.join(",", postProjectMgmtSettings.getEditAccess());
        }
        else{
            this.editAccess = null;
        }

        this.projDetailsView = postProjectMgmtSettings.getProjDetailsView();
        this.projAnalysisView = postProjectMgmtSettings.getProjAnalysisView();
        this.projCreationView = postProjectMgmtSettings.getProjCreationView();
        this.fieldType = postProjectMgmtSettings.getFieldType();
        this.createdBy = postProjectMgmtSettings.getCreatedBy();
        this.createdDate = postProjectMgmtSettings.getCreatedDate();
        this.modifiedBy = postProjectMgmtSettings.getModifiedBy();
        this.modifiedDate = postProjectMgmtSettings.getModifiedDate();
        this.isRange = postProjectMgmtSettings.getIsRange();
        this.isAddMore = postProjectMgmtSettings.getIsAddMore();
        this.isPriority = postProjectMgmtSettings.getIsPriority();
        this.isEnabledVisible = postProjectMgmtSettings.getIsEnabledVisible();
        this.isEnabledFreeze = postProjectMgmtSettings.getIsEnabledFreeze();
        this.isVisibilityDdl = postProjectMgmtSettings.getIsVisibilityDdl();
        this.isEditAccessDdl = postProjectMgmtSettings.getIsEditAccessDdl();
        this.isProjectDetailsVisible = postProjectMgmtSettings.getIsProjectDetailsVisible();
        this.isProjectDetailsFreeze = postProjectMgmtSettings.getIsProjectDetailsFreeze();
        this.isProjectAnalysisVisible = postProjectMgmtSettings.getIsProjectAnalysisVisible();
        this.isProjectAnalysisFreeze = postProjectMgmtSettings.getIsProjectAnalysisFreeze();
        this.isProjectCreationVisible = postProjectMgmtSettings.getIsProjectCreationVisible();
        this.isProjectCreationFreeze = postProjectMgmtSettings.getIsProjectCreationFreeze();
        this.settingType = postProjectMgmtSettings.getSettingType();
    }
}
