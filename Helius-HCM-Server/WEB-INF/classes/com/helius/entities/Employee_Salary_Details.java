/**
 * 
 */
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
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
/**
 * @author Tirumala
 * 22-Feb-2018
 */
@Entity
@Table(name="Employee_Salary_Details")
@Audited
public class Employee_Salary_Details extends HeliusEntity{

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_salary_details_id;
	@Column
	private String employee_id;
	@Column
	private int offer_id;
	@Column
	private String payroll_entity;
	@Column
	private String currency;
	@Column
	private String ctc_per_month;
	@Column
	private String ctc_per_year;
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	@NotAudited
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


	public String getCreated_by() {
		return created_by;
	}


	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}


	

	public String getLast_modified_by() {
		return last_modified_by;
	}


	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}


	public int getOffer_id() {
		return offer_id;
	}


	public void setOffer_id(int offer_id) {
		this.offer_id = offer_id;
	}


	public int getEmployee_salary_details_id() {
		return employee_salary_details_id;
	}


	public void setEmployee_salary_details_id(int employee_salary_details_id) {
		this.employee_salary_details_id = employee_salary_details_id;
	}


	public String getEmployee_id() {
		return employee_id;
	}


	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}


	public String getPayroll_entity() {
		return payroll_entity;
	}


	public void setPayroll_entity(String payroll_entity) {
		this.payroll_entity = payroll_entity;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getCtc_per_month() {
		return ctc_per_month;
	}


	public void setCtc_per_month(String ctc_per_month) {
		this.ctc_per_month = ctc_per_month;
	}


	public String getCtc_per_year() {
		return ctc_per_year;
	}


	public void setCtc_per_year(String ctc_per_year) {
		this.ctc_per_year = ctc_per_year;
	}


	/* (non-Javadoc)
	 * @see com.helius.entities.IEntity#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
