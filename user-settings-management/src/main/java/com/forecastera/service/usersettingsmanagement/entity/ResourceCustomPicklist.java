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
@Table(name = "resource_custom_pick_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCustomPicklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "picklist_id")
    private Integer picklistId;

    @Column(name = "picklist_value")
    private String picklistValue;

    @Column(name = "color")
    private String color;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    public ResourceCustomPicklist(PicklistFormat picklistFormat){
        this.id = picklistFormat.getId();
        this.picklistId = picklistFormat.getFieldId();
        this.picklistValue = picklistFormat.getName();
        this.color = picklistFormat.getColor();
        this.createdBy = picklistFormat.getCreatedBy();
        this.createdDate = picklistFormat.getCreatedDate();
        this.modifiedBy = picklistFormat.getModifiedBy();
        this.modifiedDate = picklistFormat.getModifiedDate();
        this.isEnabled = picklistFormat.getIsEnabled();
    }
}
