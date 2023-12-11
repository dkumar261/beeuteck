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
@Table(name = "resource_status_lkup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status")
    private String status;

    @Column(name = "color")
    private String color;

    @Column(name = "start_value")
    private Double startValue;

    @Column(name = "end_value")
    private Double endValue;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "columnis_enabled")
    private Boolean isEnabled;

    public ResourceMgmtStatus(PicklistFormat picklistFormat){
        this.statusId = picklistFormat.getId();
        this.status = picklistFormat.getName();
        this.color = picklistFormat.getColor();
        this.startValue = picklistFormat.getStartValue();
        this.endValue = picklistFormat.getEndValue();
        this.createdBy = picklistFormat.getCreatedBy();
        this.createdDate = picklistFormat.getCreatedDate();
        this.modifiedBy = picklistFormat.getModifiedBy();
        this.modifiedDate = picklistFormat.getModifiedDate();
        this.isEnabled = picklistFormat.getIsEnabled();
    }
}
