package com.forecastera.service.financialmanagement.entity;
/*
 * @Author Kanishk Vats
 * @Create 21-07-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "location_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSettingsLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location")
    private String location;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "parent_location_id")
    private Integer parentLocationId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "row_order")
    private Integer rowOrder;

}
