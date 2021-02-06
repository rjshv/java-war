package com.helius.entities;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "Users")
@Audited
public class Users {
	private int employee_user_details_id;
	private String employee_id;
	private String password;
	private int user_login_attempts;
	private String role;
	private String user_last_login;
	private String helius_role;
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	
	public String getHelius_role() {
		return helius_role;
	}

	public void setHelius_role(String helius_role) {
		this.helius_role = helius_role;
	}

	@Id
	public int getEmployee_user_details_id() {
		return employee_user_details_id;
	}

	public void setEmployee_user_details_id(int employee_user_details_id) {
		this.employee_user_details_id = employee_user_details_id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getUser_last_login() {
		return user_last_login;
	}

	public void setUser_last_login(String user_last_login) {
		this.user_last_login = user_last_login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUser_login_attempts() {
		return user_login_attempts;
	}

	public void setUser_login_attempts(int user_login_attempts) {
		this.user_login_attempts = user_login_attempts;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public String getLast_modified_by() {
		return last_modified_by;
	}

	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

}
