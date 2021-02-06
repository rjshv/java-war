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

@Entity
@Table(name="Employee_Identification_Details")
public class Employee_Identification_Details extends HeliusEntity {
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_identification_id;
	@Column
	private String employee_id;
	@Column
	private String identification_name;
	@Column
	private String identification_number;
	@Column
	@UpdateTimestamp
	private Timestamp last_modified_date;
	@Column
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	
	
	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public String getLast_modified_by() {
		return last_modified_by;
	}

	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}
	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getIdentification_number() {
		return identification_number;
	}

	public void setIdentification_number(String identification_number) {
		this.identification_number = identification_number;
	}

	public int getEmployee_identification_id() {
		return employee_identification_id;
	}

	public void setEmployee_identification_id(int employee_identification_id) {
		this.employee_identification_id = employee_identification_id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getIdentification_name() {
		return identification_name;
	}

	public void setIdentification_name(String identification_name) {
		this.identification_name = identification_name;
	}

	

	
	

}
