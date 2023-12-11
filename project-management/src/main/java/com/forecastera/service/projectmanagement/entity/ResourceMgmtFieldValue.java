package com.forecastera.service.projectmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 21-10-2023
 * @Description
 */

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
}
