package com.helius.utils;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Column(unique=true, nullable=false)
	private String userid;

	@Column(nullable=false, length=100)
	private List<String> country;

	@Column(nullable=false, length=45)
	private String edit;

	@Column(length=45)
	private String empid;

	@Column(length=45)
	private String heliususer;

	@Column(name="last_login", length=45)
	private String lastLogin;

	@Column(nullable=false, length=200)
	private String password;

	@Column(nullable=false, length=200)
	private List<String> role;

	@Column(name="user_login_attempts", length=45)
	private String userLoginAttempts;

	@Column(nullable=false, length=100)
	private String username;

	@Column(nullable=false, length=45)
	private String view;
	
	@Column(nullable=false)
	private String active;
	
	@Column
	private String token;
	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	public User() {
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public List<String> getCountry() {
		return this.country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
	}

	public String getEdit() {
		return this.edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public String getEmpid() {
		return this.empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getHeliususer() {
		return this.heliususer;
	}

	public void setHeliususer(String heliususer) {
		this.heliususer = heliususer;
	}

	public String getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRole() {
		return this.role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public String getUserLoginAttempts() {
		return this.userLoginAttempts;
	}

	public void setUserLoginAttempts(String userLoginAttempts) {
		this.userLoginAttempts = userLoginAttempts;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getView() {
		return this.view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public String toString(){
		return "User :" + this.username + "," + this.userid + "," + this.heliususer + "" + this.role 
				+ this.country + "," + this.view + "," + this.edit;
	}

}