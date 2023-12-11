package com.forecastera.service.usersettingsmanagement.entity;

import com.forecastera.service.usersettingsmanagement.dto.request.PostDelegatedResourceHistory;
import com.forecastera.service.usersettingsmanagement.dto.request.PostUserRegistration;
import com.forecastera.service.usersettingsmanagement.util.UserSettingsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/*
 * @Author Sowjanya Aare
 * @Create 07-12-2023
 * @Description
 */
@Entity
@Table(name = "user_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role")
    private String role;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "emp_id")
    private String empId;

    @Column(name = "password")
    private String password;

    public UserRegistration(PostUserRegistration postUserRegistration){
        this.firstName = UserSettingsUtil.removeAllExtraSpace(postUserRegistration.getFirstName());
        this.lastName = UserSettingsUtil.removeAllExtraSpace(postUserRegistration.getLastName());
        this.role = postUserRegistration.getRole();
        this.emailId = UserSettingsUtil.removeAllExtraSpace(postUserRegistration.getEmailId());
        this.empId = postUserRegistration.getEmpId();
        this.password = postUserRegistration.getPassword();
    }
}