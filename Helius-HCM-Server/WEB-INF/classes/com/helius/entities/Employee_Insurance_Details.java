package com.helius.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
@Table(name="Employee_Insurance_Details")
public class Employee_Insurance_Details extends HeliusEntity {

	@Id
	@Column
	private int employee_insurance_id;
	/**
	 * @return the employee_insurance_id
	 */
	public int getEmployee_insurance_id() {
		return employee_insurance_id;
	}

	/**
	 * @param employee_insurance_id the employee_insurance_id to set
	 */
	public void setEmployee_insurance_id(int employee_insurance_id) {
		this.employee_insurance_id = employee_insurance_id;
	}

	@Column
	private String employee_id;
	@Column
	private String employee_name;
	@Column
	private String nric;
	@Column
	private String designation;
	@Column
	private String policy_number;
	@Column
	private String payable_by;
	@Column
	private Date birth_date;
	@Column
	private String gender;
	@Column
	private String marital_status;
	@Column
	private Date hired_date;
	@Column
	private Date effective_date;
	@Column
	private String corporate_email;
	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Date getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	public Date getHired_date() {
		return hired_date;
	}

	public void setHired_date(Date hired_date) {
		this.hired_date = hired_date;
	}

	public Date getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(Date effective_date) {
		this.effective_date = effective_date;
	}

	public String getCorporate_email() {
		return corporate_email;
	}

	public void setCorporate_email(String corporate_email) {
		this.corporate_email = corporate_email;
	}
	/* (non-Javadoc)
	 * @see com.helius.entities.IEntity#toJSON(
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
}
	
