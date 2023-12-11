package com.forecastera.service.resourcemanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 05-06-2023
 * @Description
 */

import com.forecastera.service.resourcemanagement.dto.utilityClass.CreateResourceData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "resource_mgmt_field_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMgmtFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_mgmt_field_value_id")
    private Integer fieldValueId;

    @Column(name = "resource_id")
    private Integer resourceId;

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

    public ResourceMgmtFieldValue(ResourceMgmt resourceMgmt, CreateResourceData resourceData){
        this.resourceId = resourceMgmt.getResourceId();
        this.fieldId = resourceData.getFieldId();
        this.fieldValue = resourceData.getValue();
        this.createdBy = resourceMgmt.getCreatedBy();
        this.createdDate = resourceMgmt.getCreatedDate();
        this.modifiedBy = resourceMgmt.getModifiedBy();
        this.modifiedDate = resourceMgmt.getModifiedDate();
    }
}
