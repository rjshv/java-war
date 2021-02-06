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

@Entity
@Table(name="Employee_Leaves_Eligibility")
@Audited
public class Employee_Leaves_Eligibility {

	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int leaves_eligibility_id;
	@Column
	private String employee_id;
	@Column
	private String annual_leave;
	@Column
	private String medical_leave;
	@Column
	private String casual_leave;
	@Column
	private String maternity_leave;
	@Column
	private String paternity_leave;
	@Column
	private String childcare_leave;
	@Column
	private String compensatory_off_leave_allowed;
	@Column
	private String leave_encashments;
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
	public int getLeaves_eligibility_id() {
		return leaves_eligibility_id;
	}
	public void setLeaves_eligibility_id(int leaves_eligibility_id) {
		this.leaves_eligibility_id = leaves_eligibility_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getAnnual_leave() {
		return annual_leave;
	}
	public void setAnnual_leave(String annual_leave) {
		this.annual_leave = annual_leave;
	}
	public String getMedical_leave() {
		return medical_leave;
	}
	public void setMedical_leave(String medical_leave) {
		this.medical_leave = medical_leave;
	}
	public String getCasual_leave() {
		return casual_leave;
	}
	public void setCasual_leave(String casual_leave) {
		this.casual_leave = casual_leave;
	}
	public String getMaternity_leave() {
		return maternity_leave;
	}
	public void setMaternity_leave(String maternity_leave) {
		this.maternity_leave = maternity_leave;
	}
	public String getPaternity_leave() {
		return paternity_leave;
	}
	public void setPaternity_leave(String paternity_leave) {
		this.paternity_leave = paternity_leave;
	}
	public String getChildcare_leave() {
		return childcare_leave;
	}
	public void setChildcare_leave(String childcare_leave) {
		this.childcare_leave = childcare_leave;
	}
	public String getCompensatory_off_leave_allowed() {
		return compensatory_off_leave_allowed;
	}
	public void setCompensatory_off_leave_allowed(String compensatory_off_leave_allowed) {
		this.compensatory_off_leave_allowed = compensatory_off_leave_allowed;
	}
	public String getLeave_encashments() {
		return leave_encashments;
	}
	public void setLeave_encashments(String leave_encashments) {
		this.leave_encashments = leave_encashments;
	}
	
	
	
	
}
