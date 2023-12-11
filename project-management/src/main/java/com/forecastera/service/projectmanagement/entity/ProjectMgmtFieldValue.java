package com.forecastera.service.projectmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.forecastera.service.projectmanagement.dto.utilityClass.CreateProjectData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "project_mgmt_field_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMgmtFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_mgmt_field_value_id")
    private Integer fieldValueId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "field_id")
    private Integer fieldId;

    @Column(name = "field_value")
    private String fieldValue;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    public ProjectMgmtFieldValue(ProjectMgmt projectMgmt, CreateProjectData projectData){
        this.projectId = projectMgmt.getProjectId();
        this.fieldId = projectData.getFieldId();
        this.fieldValue = projectData.getValue();
        this.createdBy = projectMgmt.getCreatedBy();
        this.createdDate = projectMgmt.getCreatedDate();
        this.modifiedBy = projectMgmt.getModifiedBy();
        this.modifiedDate = projectMgmt.getModifiedDate();
    }
}
