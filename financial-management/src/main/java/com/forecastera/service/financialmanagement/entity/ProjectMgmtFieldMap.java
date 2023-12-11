package com.forecastera.service.financialmanagement.entity;

/*
 * @Author Kanishk Vats
 * @Create 12-07-2023
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
    private Boolean projDetailsView;

    @Column(name = "proj_analysis_view")
    private Boolean projAnalysisView;

    @Column(name = "proj_creation_view")
    private Boolean projCreationView;

    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

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

}