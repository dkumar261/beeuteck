package com.forecastera.service.financialmanagement.entity.vendoremployeedetails;
/*
 * @Author Kanishk Vats
 * @Create 26-06-2023
 * @Description
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vendor_employment_details_forecast")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendorEmployeeDetailsForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_employment_details_forecast_id")
    private Integer id;

//    @Column(name = "employee_id")
//    private Integer employeeId;
//
//    @Column(name = "project_id")
//    private Integer projectId;
//
//    @Column(name = "location")
//    private String location;
//
//    @Column(name = "designation")
//    private String designation;
//
//    @Column(name = "work_location")
//    private String workLocation;
//
//    @Column(name = "salary")
//    private Long salary;
//
//    @Column(name = "fringe")
//    private Long fringe;
//
//    @Column(name = "start_date")
//    private Date startDate;
//
//    @Column(name = "end_date")
//    private Date endDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

//    @Column(name = "jan")
//    private Long jan;
//
//    @Column(name = "feb")
//    private Long feb;
//
//    @Column(name = "mar")
//    private Long mar;
//
//    @Column(name = "apr")
//    private Long apr;
//
//    @Column(name = "may")
//    private Long may;
//
//    @Column(name = "jun")
//    private Long jun;
//
//    @Column(name = "jul")
//    private Long jul;
//
//    @Column(name = "aug")
//    private Long aug;
//
//    @Column(name = "sep")
//    private Long sep;
//
//    @Column(name = "oct")
//    private Long oct;
//
//    @Column(name = "nov")
//    private Long nov;
//
//    @Column(name = "dec")
//    private Long dec;

    @Column(name = "financial_year")
    private Integer financialYear;

    @Column(name = "row_order")
    private Integer rowOrder;

    @Column(name = "is_active")
    private Boolean isActive;
}
