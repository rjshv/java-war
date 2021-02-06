package com.helius.entities;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Employee_On_Boarding_Checklist")
public class Employee_On_Boarding_Checklist {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_on_boarding_checklist_id;
	@Column
	private String employee_id;
	@Column
	private String document_name;
	@Column
	private String document_path;
	@Column
	@NotAudited
	private String lastmodified_by;
	@Column
	@UpdateTimestamp
	private Timestamp lastmodified_date;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	
	
	public Timestamp getLastmodified_date() {
		return lastmodified_date;
	}
	public void setLastmodified_date(Timestamp lastmodified_date) {
		this.lastmodified_date = lastmodified_date;
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
	public int getEmployee_on_boarding_checklist_id() {
		return employee_on_boarding_checklist_id;
	}
	public void setEmployee_on_boarding_checklist_id(int employee_on_boarding_checklist_id) {
		this.employee_on_boarding_checklist_id = employee_on_boarding_checklist_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getDocument_name() {
		return document_name;
	}
	public void setDocument_name(String document_name) {
		this.document_name = document_name;
	}
	public String getDocument_path() {
		return document_path;
	}
	public void setDocument_path(String document_path) {
		this.document_path = document_path;
	}
	public String getLastmodified_by() {
		return lastmodified_by;
	}
	public void setLastmodified_by(String lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}
	
	
	
	
}
