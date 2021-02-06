package com.helius.entities;

	import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


	/**
	 * The persistent class for the user database table.
	 * 
	 */
	@Entity
	@Audited
	@Table(name="Employee_Selfcare_Users")
	public class Employee_Selfcare_Users  implements Serializable {
		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column
		private int Employee_Selfcare_Users_Id;
		@Column
		private String employee_id;
		@Column
		private String employee_name;
		@Column
		private String password;
		@Column
		private String active;
		@Column
		private int user_login_attempts;
		@Column
		private Timestamp user_last_login;
		@Column
		private String token;
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
		public int getEmployee_Selfcare_Users_Id() {
			return Employee_Selfcare_Users_Id;
		}
		public void setEmployee_Selfcare_Users_Id(int employee_Selfcare_Users_Id) {
			Employee_Selfcare_Users_Id = employee_Selfcare_Users_Id;
		}
		
		public String getEmployee_name() {
			return employee_name;
		}
		public void setEmployee_name(String employee_name) {
			this.employee_name = employee_name;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getEmployee_id() {
			return employee_id;
		}
		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getActive() {
			return active;
		}
		public void setActive(String active) {
			this.active = active;
		}
		public int getUser_login_attempts() {
			return user_login_attempts;
		}
		public void setUser_login_attempts(int user_login_attempts) {
			this.user_login_attempts = user_login_attempts;
		}
	
		public Timestamp getUser_last_login() {
			return user_last_login;
		}
		public void setUser_last_login(Timestamp user_last_login) {
			this.user_last_login = user_last_login;
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
		public static long getSerialversionuid() {
			return serialVersionUID;
		}

	}
