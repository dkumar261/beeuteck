package com.forecastera.service.usersettingsmanagement.entity;/*
 * @Author Kanishk Vats
 * @Create 30-05-2023
 * @Description
 */

import com.forecastera.service.usersettingsmanagement.dto.utilityClass.PicklistFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employment_type_lkup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtEmploymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_type_id")
    private Integer empTypeId;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "color")
    private String color;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    public ResourceMgmtEmploymentType(PicklistFormat picklistFormat){
        this.empTypeId = picklistFormat.getId();
        this.employmentType = picklistFormat.getName();
        this.color = picklistFormat.getColor();
        this.createdBy = picklistFormat.getCreatedBy();
        this.createdDate = picklistFormat.getCreatedDate();
        this.modifiedBy = picklistFormat.getModifiedBy();
        this.modifiedDate = picklistFormat.getModifiedDate();
        this.isEnabled = picklistFormat.getIsEnabled();
    }
}
