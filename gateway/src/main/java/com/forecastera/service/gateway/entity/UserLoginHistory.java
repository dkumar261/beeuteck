package com.forecastera.service.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Dinesh
 * 
 */
@Entity
@Table(name = "login_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "username")
	private String username;

	@Column(name = "role")
	private String role;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "login_time")
	private Date loginTime;

	@Column(name = "logout_time")
	private Date logoutTime;
}
