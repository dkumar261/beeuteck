package com.forecastera.service.resourcemanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 21-08-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String projDetailsView;

    @Column(name = "proj_analysis_view")
    private String projAnalysisView;

    @Column(name = "proj_creation_view")
    private String projCreationView;

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

    @Column(name = "isprojectdetailsvisible")
    private String isProjectDetailsVisible;

    @Column(name = "isprojectdetailsfreeze")
    private String isProjectDetailsFreeze;

    @Column(name = "isprojectanalysisvisible")
    private String isProjectAnalysisVisible;

    @Column(name = "isprojectanalysisfreeze")
    private String isProjectAnalysisFreeze;

    @Column(name = "isprojectcreationvisible")
    private String isProjectCreationVisible;

    @Column(name = "isprojectcreationfreeze")
    private String isProjectCreationFreeze;

    @Column(name = "setting_type")
    private Integer settingType;

}